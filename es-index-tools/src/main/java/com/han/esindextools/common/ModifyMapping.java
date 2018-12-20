package com.han.esindextools.common;

import com.han.esindextools.bean.ModifyMappingResult;
import com.han.esindextools.bean.ReIndexResult;
import com.hivescm.common.domain.DataResult;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.client.transport.TransportClient;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 17:55
 */
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

    public ModifyMappingResult modifyMappingByTmpIndex(String _index, String tmp_index, String _type, List<MappingDTO> list, ModifyMapping.ModifyProperties modifyProperties)
            throws IOException
    {
        Map mappings = getMappings(_index, _type);
        if ((null == mappings) || (mappings.isEmpty())) {
            logger.info("获取mapping映射失败...");
            return new ModifyMappingResult(Boolean.valueOf(false));
        }
        Map settings = getSettings(_index);
        Map mappingMap = new HashMap();
        mappingMap.put(_type, modifyProperties.execute(mappings, list));
        boolean createTmpIndexResult = this.createIndex.createIndex(tmp_index, settings, mappingMap);
        if (!createTmpIndexResult) {
            logger.info("临时索引创建出错...");
            return new ModifyMappingResult(Boolean.valueOf(true), Boolean.valueOf(false));
        }
        QueryBuilder queryBuilder = QueryBuilders.typeQuery(_type);
        ReIndexResult copyDataResult = this.localReIndex.reIndexFromLocal(_index, null, tmp_index, _type, queryBuilder, list);
        System.out.println(GsonSerialize.INSTANCE.encode(copyDataResult));
        if (copyDataResult.getFailures().intValue() < 1) {
            String alias = getAlias(_index);
            boolean result = addAndRemoveAlias(_index, tmp_index, alias);
            DataResult deleteOldIndexResult = this.deleteIndex.deleteIndex(new String[] { _index });
            mappings = getMappings(tmp_index, _type);
            boolean createIndexResult = this.createIndex.createIndex(_index, _type, mappings);
            logger.info("删除原索引结果为：{}", deleteOldIndexResult.toString());
            logger.info("新建原索引结果为：{}", Boolean.valueOf(createIndexResult));
            ReIndexResult copyDataToIndex = this.localReIndex.reIndexFromLocal(tmp_index, _index, queryBuilder);
            int num = 0; int sum = 0;
            while (copyDataToIndex.getTotal().longValue() < copyDataResult.getTotal().longValue()) {
                num++;
                this.deleteIndex.deleteIndex(new String[] { _index });
                copyDataToIndex = this.localReIndex.reIndexFromLocal(tmp_index, _index, queryBuilder);
                sum = (int)(sum + num * copyDataToIndex.getTook().longValue());
                System.out.println("sleep " + num + " times, waste time " + sum + "millis...");
            }
            copyDataResult.setCreated(copyDataToIndex.getCreated());
            copyDataResult.setTook(Long.valueOf(copyDataResult.getTook().longValue() + copyDataToIndex.getTook().longValue() + sum));
            System.out.println(GsonSerialize.INSTANCE.encode(copyDataToIndex));
            if (copyDataToIndex.getFailures().intValue() < 1) {
                DataResult deleteTmpIdnexResult = this.deleteIndex.deleteIndex(new String[] { tmp_index });
                logger.info("删除临时索引结果为：{}", deleteTmpIdnexResult.toString());
            }
        } else {
            this.deleteIndex.deleteIndex(new String[] { tmp_index });
        }
        return new ModifyMappingResult(Boolean.valueOf(true), Boolean.valueOf(true), copyDataResult);
    }

    public Map<String, Object> getMappings(String _index, String _type)
            throws IOException
    {
        ImmutableOpenMap immutableOpenMap = ((ClusterStateResponse)this.transportClient.admin()
                .cluster().prepareState().execute().actionGet()).getState().getMetaData().getIndices();
        if (null != immutableOpenMap) {
            IndexMetaData metaData = (IndexMetaData)immutableOpenMap.get(_index);
            if (metaData != null) {
                ImmutableOpenMap mappings = metaData.getMappings();
                if (mappings != null) {
                    return ((MappingMetaData)mappings.get(_type)).getSourceAsMap();
                }
            }
        }
        if (logger.isErrorEnabled()) {
            logger.error("获取ES映射信息失败 index: {} | type: {}", _index, _type);
        }
        return null;
    }

    public Map<String, Object> getSettings(String _index)
    {
        ImmutableOpenMap immutableOpenMap = ((ClusterStateResponse)this.transportClient.admin()
                .cluster().prepareState().execute().actionGet()).getState().getMetaData().getIndices();
        if (null != immutableOpenMap) {
            IndexMetaData indexMetaData = (IndexMetaData)immutableOpenMap.get(_index);
            if (null != indexMetaData) {
                Settings settings = indexMetaData.getSettings();
                Map settingMap = ((Settings)settings.getAsGroups().get("index")).getAsStructuredMap();
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

    public String getAlias(String _index)
    {
        ImmutableOpenMap immutableOpenMap = ((ClusterStateResponse)this.transportClient.admin()
                .cluster().prepareState().execute().actionGet()).getState().getMetaData().getIndices();
        if (null == immutableOpenMap) {
            logger.error("获取ES映射信息失败 index: {}", _index);
            return null;
        }
        IndexMetaData metaData = (IndexMetaData)immutableOpenMap.get(_index);
        ImmutableOpenMap metaDataAliases = metaData.getAliases();
        if (metaDataAliases != null) {
            Iterator it = metaDataAliases.keysIt();
            if (it.hasNext()) {
                String alias = (String)it.next();
                return alias;
            }
        }
        return null;
    }

    public boolean addAndRemoveAlias(String oldIndex, String newIndex, String alias)
    {
        if (StringUtils.isEmpty(alias)) {
            logger.info("别名为空,不能创建别名...");
            return false;
        }

        IndicesAliasesResponse response = (IndicesAliasesResponse)this.transportClient.admin().indices().prepareAliases()
                .addAlias(newIndex, alias)
                .removeAlias(oldIndex, alias)
                .get();
        return response.isAcknowledged();
    }

    public static interface ModifyProperties{
        public abstract Map<String, Object> updateFieldMapping(Map<String, Object> paramMap, List<MappingDTO> paramList);

        default Map<String, Object> execute(Map<String, Object> data, List<MappingDTO> list)
        {
            Map properties = (Map)data.get("properties");
            properties = updateFieldMapping(properties, list);
            data.put("properties", properties);
            return data;
        }
    }
}
