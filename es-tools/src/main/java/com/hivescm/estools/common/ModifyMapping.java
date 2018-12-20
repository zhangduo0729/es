package com.hivescm.estools.common;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.estools.bean.ModifyMappingResult;
import com.hivescm.estools.bean.ReIndexResult;
import com.hivescm.estools.dto.modify.MappingDTO;
import com.hivescm.estools.reindex.LocalReIndex;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ModifyMapping {

    private static final Logger logger = LoggerFactory.getLogger(ModifyMapping.class);

    @Autowired
    private TransportClient transportClient;

    @Autowired
    private CreateIndex createIndex;

    @Autowired
    private DeleteIndex deleteIndex;

    @Autowired
    private LocalReIndex localReIndex;

    /**
     * 重建mapping映射
     * 实现思路：1. 新建一个临时的tmpIndex，并将原Index中修改后的mapping使用于临时的tmpIndex；
     *         2. 为其创建和原Index相同的别名,并删除原Index的别名；
     *         3. 删除原索引；
     *         4. 根据新的映射重新创建原索引
     *         5. 将临时索引中的数据拷贝到新创建的索引中
     * @param _index 索引名称
     * @param tmp_index 临时索引名称
     * @param _type type名称
     * @param modifyProperties 修改Mapping操作类
     * @return ModifyMappingResult
     * @throws IOException
     */
    public ModifyMappingResult modifyMappingByTmpIndex(String _index, String tmp_index, String _type, List<MappingDTO> list, ModifyProperties modifyProperties) throws IOException {
        Map<String, Object> mappings = this.getMappings(_index, _type);
        if (null == mappings || mappings.isEmpty()) {
            logger.info("获取mapping映射失败...");
            return new ModifyMappingResult(false);
        }
        Map<String, Object> settings = this.getSettings(_index);
        Map<String, Map<String, Object>> mappingMap = new HashMap<>();
        mappingMap.put(_type, modifyProperties.execute(mappings, list));
        boolean createTmpIndexResult =  createIndex.createIndex(tmp_index, settings, mappingMap);
        if (!createTmpIndexResult) {
            logger.info("临时索引创建出错...");
            return new ModifyMappingResult(true, false);
        }
        QueryBuilder queryBuilder = QueryBuilders.typeQuery(_type);
        ReIndexResult copyDataResult = localReIndex.reIndexFromLocal(_index, null, tmp_index, _type, queryBuilder, list);
        System.out.println(GsonSerialize.INSTANCE.encode(copyDataResult));
        if (copyDataResult.getFailures() < 1) {
            String alias = this.getAlias(_index);
            boolean result = this.addAndRemoveAlias(_index, tmp_index, alias);
            DataResult deleteOldIndexResult = deleteIndex.deleteIndex(_index);
            mappings = this.getMappings(tmp_index, _type);
            boolean createIndexResult = createIndex.createIndex(_index, _type, mappings);
            logger.info("删除原索引结果为：{}", deleteOldIndexResult.toString());
            logger.info("新建原索引结果为：{}", createIndexResult);
            ReIndexResult copyDataToIndex = localReIndex.reIndexFromLocal(tmp_index, _index, queryBuilder);
            int num = 0, sum = 0;
            while (copyDataToIndex.getTotal() < copyDataResult.getTotal()) {
                num++;
                deleteIndex.deleteIndex(_index);
                copyDataToIndex = localReIndex.reIndexFromLocal(tmp_index, _index, queryBuilder);
                sum += num*copyDataToIndex.getTook();
                System.out.println("sleep " + num + " times, waste time " + sum + "millis...");
            }
            copyDataResult.setCreated(copyDataToIndex.getCreated());
            copyDataResult.setTook(copyDataResult.getTook() + copyDataToIndex.getTook() + sum);
            System.out.println(GsonSerialize.INSTANCE.encode(copyDataToIndex));
            if (copyDataToIndex.getFailures() < 1) {
                DataResult deleteTmpIdnexResult = deleteIndex.deleteIndex(tmp_index);
                logger.info("删除临时索引结果为：{}", deleteTmpIdnexResult.toString());
            }
        } else {
            deleteIndex.deleteIndex(tmp_index);
        }
        return new ModifyMappingResult(true, true, copyDataResult);
    }

    /**
     * 获取指定Index，指定type的映射信息 {"properties"={k1=v1, k2=v2, k3=v3.....}}
     * @param _index
     * @param _type
     * @return
     */
    public Map<String, Object> getMappings(String _index, String _type) throws IOException {
        ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = transportClient.admin()
                .cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices();
        if (null != immutableOpenMap) {
            IndexMetaData metaData = immutableOpenMap.get(_index);
            if (metaData != null) {
                ImmutableOpenMap<String, MappingMetaData> mappings = metaData.getMappings();
                if (mappings != null) {
                    return mappings.get(_type).getSourceAsMap();
                }
            }
        }
        if (logger.isErrorEnabled()) {
            logger.error("获取ES映射信息失败 index: {} | type: {}", _index, _type);
        }
        return null;
    }

    /**
     * 获取指定Index的settings配置信息
     * @param _index
     * @return
     */
    public Map<String, Object> getSettings(String _index) {
        ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = transportClient.admin()
                .cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices();
        if (null != immutableOpenMap) {
            IndexMetaData indexMetaData = immutableOpenMap.get(_index);
            if (null != indexMetaData) {
                Settings settings = indexMetaData.getSettings();
                Map<String, Object> settingMap = settings.getAsGroups().get("index").getAsStructuredMap();
                settingMap.remove("provided_name");
                settingMap.remove("creation_date");
                settingMap.remove("uuid");
                settingMap.remove("version");
                return settingMap;
            }
        }
        logger.error("获取ES Settings设置信息失败 index: {}", _index);
        return null;
    }

    /**
     * 获取指定Index的别名
     * @param _index
     * @return
     */
    public String getAlias(String _index) {
        ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = transportClient.admin()
                .cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices();
        if (null == immutableOpenMap) {
            logger.error("获取ES映射信息失败 index: {}", _index);
            return null;
        }
        IndexMetaData metaData = immutableOpenMap.get(_index);
        ImmutableOpenMap<String, AliasMetaData> metaDataAliases = metaData.getAliases();
        if (metaDataAliases != null) {
            Iterator<String> it = metaDataAliases.keysIt();
            while (it.hasNext()) {
                String alias = it.next();
                return alias;
            }
        }
        return null;
    }

    /**
     * 删除指定原索引的别名，同时给指定的新Index添加相同的别名
     * @param oldIndex
     * @param newIndex
     * @param alias
     * @return
     */
    public boolean addAndRemoveAlias(String oldIndex, String newIndex, String alias) {
        if (StringUtils.isEmpty(alias)) {
            logger.info("别名为空,不能创建别名...");
            return false;
        }
        IndicesAliasesResponse response = transportClient.admin().indices().prepareAliases()
                .addAlias(newIndex, alias)
                .removeAlias(oldIndex, alias)
                .get();
        return response.isAcknowledged();
    }

    /**
     * 处理mapping映射的类
     */
    public interface ModifyProperties {

        /**
         * 修改有变化的Field类型方法
         * @param properties
         * @return
         */
        Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list);

        default Map<String, Object> execute(Map<String, Object> data, List<MappingDTO> list) {
            Map<String, Object> properties = (Map<String, Object>) data.get("properties");
            properties = this.updateFieldMapping(properties, list);
            data.put("properties", properties);
            return data;
        }
    }
}
