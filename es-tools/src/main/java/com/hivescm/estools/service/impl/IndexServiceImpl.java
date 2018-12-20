package com.hivescm.estools.service.impl;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.estools.bean.ModifyMappingResult;
import com.hivescm.estools.common.CreateIndex;
import com.hivescm.estools.common.DeleteIndex;
import com.hivescm.estools.dto.AnalyzerDTO;
import com.hivescm.estools.dto.create.BasicMappingDTO;
import com.hivescm.estools.dto.modify.MappingDTO;
import com.hivescm.estools.common.ModifyMapping;
import com.hivescm.estools.service.IndexService;
import com.hivescm.estools.tools.*;
import com.hivescm.essdkservice.common.bean.QueryObject;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class IndexServiceImpl implements IndexService{

    @Autowired
    private ModifyMapping modifyMapping;

    @Autowired
    private CreateIndex createIndex;

    @Autowired
    private DeleteIndex deleteIndex;

    @Autowired
    private TransportClient transportClient;

    /**
     * 创建索引同时创建映射
     * @param index
     * @param type
     * @param list 字段集合
     * @return
     */
    @Override
    public DataResult createMapping(String index, String type, List<BasicMappingDTO> list) {
        IndexStruct struct = IndexStruct.make(index, type);
        for (BasicMappingDTO bean : list)  {
            String field = bean.getField();
            String fieldType = bean.getType();
            IndexField indexField = this.returnIndexField(fieldType, bean);
            if (null != indexField) {
                struct.addColumn(field, indexField);
            }
        }
        boolean result = createIndex.createIndex(index, type, struct.getMappings());
        if (result) {
            return DataResult.success(Boolean.TRUE);
        }
        return DataResult.faild(1510,"创建映射失败!!!");
    }

    /**
     * 修改映射
     * @param index
     * @param type
     * @param list 待修改映射的字段集合
     * @return
     */
    @Override
    public DataResult modifyMapping(String index, String type, List<MappingDTO> list) {
        String tmpIndex = index + "_tmp";
        try {
           ModifyMappingResult result = modifyMapping.modifyMappingByTmpIndex(index, tmpIndex, type, list, new ModifyMapping.ModifyProperties() {
                @Override
                public Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list) {
                    for (MappingDTO bean : list) {
                        String type = bean.getNewType();
                        Map<String, Object> value = null;
                        if ("text".equals(type)) {
                            value = IndexField.make().setType(DataType.TEXT).setFieldKeywordWithIgnore().getResult();
                        } else if ("date".equals(type)) {
                            value = IndexField.make().setType(DataType.DATE).setFormat().getResult();
                        } else {
                            value = IndexField.make().setType(MappingDTO.getDataType(bean.getNewType())).getResult();
                        }
                        if (properties.containsKey(bean.getOldField())) {
                            properties.put(bean.getNewField(), value);
                            properties.remove(bean.getOldField());
                        } else {
                            properties.put(bean.getOldField(), value);
                        }
                    }
                    String json = GsonSerialize.INSTANCE.encode(properties);
                    return GsonSerialize.INSTANCE.decode(json, Map.class);
                }
            });
            return DataResult.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return DataResult.faild(1509, e.getMessage());
        }
    }

    /**
     * 根据字段类型，返回该字段的映射配置类
     * @param fieldType
     * @param bean
     * @return
     */
    private IndexField returnIndexField(String fieldType, BasicMappingDTO bean) {
        switch (fieldType) {
            case "boolean":
                return IndexField.make().setType(DataType.BOOLEAN);
            case "integer":
                return IndexField.make().setType(DataType.INTEGER);
            case "long":
                return IndexField.make().setType(DataType.LONG);
            case "float":
                return IndexField.make().setType(DataType.FLOAT);
            case "double":
                return IndexField.make().setType(DataType.DOUBLE);
            case "keyword":
                return IndexField.make().setType(DataType.KEYWORD);
            case "date":
                String format = bean.getFormat();
                if (null == format) {
                    return IndexField.make().setType(DataType.DATE);
                }
                return IndexField.make().setType(DataType.DATE).setFormat(format.toString());
            case "text":
                String analyzer = bean.getAnalyzer();
                if (null == analyzer) {
                    return IndexField.make().setType(DataType.TEXT).setFieldKeywordWithIgnore();
                }
                return IndexField.make().setType(DataType.TEXT).setAnalyzer(analyzer.toString()).setFieldKeywordWithIgnore();
            case "nested":
                List<BasicMappingDTO> props = bean.getProps();
                if (null != props && 0 < props.size()) {
                    Map<String, Object> properties = new HashMap<>();
                    for (BasicMappingDTO m : props)  {
                        String nestedType = m.getType();
                        String nestedField = m.getField();
                        properties.put(nestedField, this.returnIndexField(nestedType, m).getResult());
                    }
                    return IndexField.make().setType(DataType.NESTED).put("properties", properties);
                }
        }
        return null;
    }

    /**
     * 获取所有的索引名称
     * @return
     */
    @Override
    public DataResult queryAllIndexName() {
        ActionFuture<IndicesStatsResponse> isr = transportClient.admin().indices().stats(new IndicesStatsRequest().all());
        Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
        Set<String> set = isr.actionGet().getIndices().keySet();
        return DataResult.success(set);
    }

    /**
     * 根据指定索引指定字段的分词器来分析指定的字符串
     * @param analyzerDTO
     * @return
     */
    public DataResult specifyFieldAnalyzer(AnalyzerDTO analyzerDTO) {
        AnalyzeRequest analyzeRequest = new AnalyzeRequest(analyzerDTO.getIndex());
        analyzeRequest.field(analyzerDTO.getField());
        analyzeRequest.text(analyzerDTO.getText());
        ActionFuture<AnalyzeResponse> future = transportClient.admin().indices().analyze(analyzeRequest);
        AnalyzeResponse response = future.actionGet();
        List<AnalyzeResponse.AnalyzeToken> list = response.getTokens();
        return DataResult.success(list);
    }

    /**
     * 根据指定的分词器来分析指定的字符串
     * @param analyzerDTO
     * @return
     */
    @Override
    public DataResult customerAnalyzer(AnalyzerDTO analyzerDTO) {
        AnalyzeRequest analyzeRequest = new AnalyzeRequest();
        analyzeRequest.analyzer(analyzerDTO.getAnalyzer());
        analyzeRequest.text(analyzerDTO.getText());
        ActionFuture<AnalyzeResponse> future = transportClient.admin().indices().analyze(analyzeRequest);
        AnalyzeResponse response = future.actionGet();
        List<AnalyzeResponse.AnalyzeToken> list = response.getTokens();
        return DataResult.success(list);
    }

    /**
     * 批量删除索引
     * @param indexNames
     * @return
     */
    @Override
    public DataResult deleteIndexByNames(String[] indexNames) {
        DataResult result = deleteIndex.deleteIndex(indexNames);
        return result;
    }

    /**
     * 根据条件删除指定index下的数据
     * @param index
     * @param queryObject
     * @return
     */
    @Override
    public DataResult deleteByQuery(String index, QueryObject queryObject) {
        QueryBuilder queryBuilder = null;
        if (null != queryObject) {
            queryBuilder = ESQueryBuilder.queryObjectToFilterQueryBuilder(queryObject);
        }
        Long deletedNum = deleteIndex.deleteByQuery(index, queryBuilder);
        return DataResult.success(deletedNum);
    }
}
