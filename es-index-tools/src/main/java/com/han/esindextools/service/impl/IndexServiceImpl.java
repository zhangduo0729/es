package com.han.esindextools.service.impl;

import com.han.esindextools.bean.ModifyMappingResult;
import com.han.esindextools.common.CreateIndex;
import com.han.esindextools.common.DeleteIndex;
import com.han.esindextools.common.ModifyMapping;
import com.han.esindextools.dto.AnalyzerDTO;
import com.han.esindextools.dto.create.BasicMappingDTO;
import com.han.esindextools.dto.modify.MappingDTO;
import com.han.esindextools.service.IndexService;
import com.han.esindextools.tools.DataType;
import com.han.esindextools.tools.IndexField;
import com.han.esindextools.tools.IndexStruct;
import com.han.esindextools.tools.QueryObject;
import com.hivescm.common.domain.DataResult;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.essdkservice.common.bean.ESQueryBuilder;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
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

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:11
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private ModifyMapping modifyMapping;

    @Autowired
    private CreateIndex createIndex;

    @Autowired
    private DeleteIndex deleteIndex;

    @Autowired
    private TransportClient transportClient;

    public DataResult createMapping(String index, String type, List<BasicMappingDTO> list) {
        IndexStruct struct = IndexStruct.make(index, type);
        for (BasicMappingDTO bean : list) {
            String field = bean.getField();
            String fieldType = bean.getType();
            IndexField indexField = returnIndexField(fieldType, bean);
            if (null != indexField) {
                struct.addColumn(field, indexField);
            }
        }
        boolean result = this.createIndex.createIndex(index, type, struct.getMappings());
        if (result) {
            return DataResult.success(Boolean.TRUE);
        }
        return DataResult.faild(1510, "创建映射失败!!!");
    }

    public DataResult modifyMapping(String index, String type, List<MappingDTO> list) {
        String tmpIndex = index + "_tmp";
        try {
            ModifyMappingResult result = this.modifyMapping.modifyMappingByTmpIndex(index, tmpIndex, type, list, new IndexServiceImpl
            .1 (this));

            return DataResult.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return DataResult.faild(1509, e.getMessage());
        }
    }

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
                List props = bean.getProps();
                if ((null == props) || (0 >= props.size())) break;
                Map properties = new HashMap();
                for (BasicMappingDTO m : props) {
                    String nestedType = m.getType();
                    String nestedField = m.getField();
                    properties.put(nestedField, returnIndexField(nestedType, m).getResult());
                }
                return IndexField.make().setType(DataType.NESTED).put("properties", properties);
        }

        return null;
    }

    public DataResult queryAllIndexName() {
        ActionFuture isr = this.transportClient.admin().indices().stats(new IndicesStatsRequest().all());
        Map indexStatsMap = ((IndicesStatsResponse) isr.actionGet()).getIndices();
        Set set = ((IndicesStatsResponse) isr.actionGet()).getIndices().keySet();
        return DataResult.success(set);
    }

    public DataResult specifyFieldAnalyzer(AnalyzerDTO analyzerDTO) {
        AnalyzeRequest analyzeRequest = new AnalyzeRequest(analyzerDTO.getIndex());
        analyzeRequest.field(analyzerDTO.getField());
        analyzeRequest.text(new String[]{analyzerDTO.getText()});
        ActionFuture future = this.transportClient.admin().indices().analyze(analyzeRequest);
        AnalyzeResponse response = (AnalyzeResponse) future.actionGet();
        List list = response.getTokens();
        return DataResult.success(list);
    }

    public DataResult customerAnalyzer(AnalyzerDTO analyzerDTO) {
        AnalyzeRequest analyzeRequest = new AnalyzeRequest();
        analyzeRequest.analyzer(analyzerDTO.getAnalyzer());
        analyzeRequest.text(new String[]{analyzerDTO.getText()});
        ActionFuture future = this.transportClient.admin().indices().analyze(analyzeRequest);
        AnalyzeResponse response = (AnalyzeResponse) future.actionGet();
        List list = response.getTokens();
        return DataResult.success(list);
    }

    public DataResult deleteIndexByNames(String[] indexNames) {
        DataResult result = this.deleteIndex.deleteIndex(indexNames);
        return result;
    }

    @Override
    public DataResult deleteByQuery(String index, QueryObject queryObject) {
        QueryBuilder queryBuilder = null;
        if (null != queryObject) {
            queryBuilder = ESQueryBuilder.objectToQueryBuilder(queryObject);
        }
        Long deletedNum = this.deleteIndex.deleteByQuery(index, queryBuilder);
        return DataResult.success(deletedNum);
    }

    public Map<String, Object> updateFieldMapping(Map<String, Object> properties, List<MappingDTO> list) {
        for (MappingDTO bean : list) {
            String type = bean.getNewType();
            Map value = null;
            if ("text".equals(type))
                value = IndexField.make().setType(DataType.TEXT).setFieldKeywordWithIgnore().getResult();
            else if ("date".equals(type))
                value = IndexField.make().setType(DataType.DATE).setFormat().getResult();
            else {
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
        return (Map) GsonSerialize.INSTANCE.decode(json, Map.class);
    }
}
