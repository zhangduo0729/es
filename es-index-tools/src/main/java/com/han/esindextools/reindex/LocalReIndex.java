package com.han.esindextools.reindex;

import com.han.esindextools.bean.ReIndexResult;
import com.han.esindextools.dto.modify.MappingDTO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:07
 */
@Service
public class LocalReIndex {
    @Autowired
    private TransportClient transportClient;

    public ReIndexResult reIndexFromLocal(String sourceIndex, String destIndex)
    {
        return reIndexFromLocal(sourceIndex, null, destIndex, null, null);
    }

    public ReIndexResult reIndexFromLocal(String sourceIndex, String destIndex, QueryBuilder queryBuilder)
    {
        return reIndexFromLocal(sourceIndex, null, destIndex, null, queryBuilder);
    }

    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex)
    {
        return reIndexFromLocal(sourceIndex, size, destIndex, null, null);
    }

    public ReIndexResult reIndexFromLocal(String sourceIndex, String destIndex, String destType)
    {
        return reIndexFromLocal(sourceIndex, null, destIndex, destType, null);
    }

    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex, String destType)
    {
        return reIndexFromLocal(sourceIndex, size, destIndex, destType, null);
    }

    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex, String destType, QueryBuilder queryBuilder)
    {
        return reIndexFromLocal(sourceIndex, size, destIndex, destType, queryBuilder, null);
    }

    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex, String destType, QueryBuilder queryBuilder, List<MappingDTO> updateFields)
    {
        sourceIndex = (String)Objects.requireNonNull(sourceIndex, "源Index不能为空");
        destIndex = (String)Objects.requireNonNull(destIndex, "新Index不能为空");
        ReindexRequestBuilder requestBuilder = ReindexAction.INSTANCE.newRequestBuilder(this.transportClient);
        requestBuilder.source(new String[] { sourceIndex });
        if (!StringUtils.isEmpty(destType))
            requestBuilder.destination(destIndex, destType);
        else {
            requestBuilder.destination(destIndex);
        }
        if (null != queryBuilder) {
            requestBuilder.filter(queryBuilder);
        }
        if ((null != size) && (0 < size.intValue())) {
            requestBuilder.size(size.intValue());
        }
        if (null != updateFields) {
            StringBuilder sb = new StringBuilder();
            for (MappingDTO mappingBean : updateFields) {
                sb.append("ctx._source.").append(mappingBean.getNewField())
                        .append("=")
                        .append("ctx._source.remove('").append(mappingBean.getOldField())
                        .append("');");
            }

            requestBuilder.script(new Script(sb.toString()));
        }
        BulkByScrollResponse response = (BulkByScrollResponse)requestBuilder.execute().actionGet();
        ReIndexResult reIndexResult = new ReIndexResult();
        reIndexResult.setTook(Long.valueOf(response.getTook().getMillis()));
        reIndexResult.setTotal(Long.valueOf(response.getStatus().getTotal()));
        reIndexResult.setCreated(Long.valueOf(response.getCreated()));
        reIndexResult.setUpdated(Long.valueOf(response.getUpdated()));
        reIndexResult.setFailures(Integer.valueOf(response.getBulkFailures().size()));
        if (response.getBulkFailures().size() > 0) {
            reIndexResult.setCause(((BulkItemResponse.Failure)response.getBulkFailures().get(0)).getCause().getMessage());
        }

        return reIndexResult;
    }
}
