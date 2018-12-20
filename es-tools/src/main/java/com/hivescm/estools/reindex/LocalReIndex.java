package com.hivescm.estools.reindex;

import com.hivescm.estools.bean.ReIndexResult;
import com.hivescm.estools.dto.modify.MappingDTO;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class LocalReIndex {

    @Autowired
    private TransportClient transportClient;

    /**
     * 重建索引，此方法将指定索引中的数据同步到指定的索引中
     * @param sourceIndex  源索引名称
     * @param destIndex 目标索引名称
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, String destIndex) {
        return this.reIndexFromLocal(sourceIndex, null, destIndex, null, null);
    }

    /**
     * 重建索引，此方法将指定索引中的数据同步到指定的索引中
     * @param sourceIndex  源索引名称
     * @param destIndex 目标索引名称
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, String destIndex, QueryBuilder queryBuilder) {
        return this.reIndexFromLocal(sourceIndex, null, destIndex, null, queryBuilder);
    }

    /**
     * 重建索引，此方法将指定索引中的size条数据同步到指定的索引中
     * @param sourceIndex 远程索引名称
     * @param size 拷贝的数量
     * @param destIndex 目标索引名称
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex) {
        return this.reIndexFromLocal(sourceIndex, size, destIndex, null, null);
    }

    /**
     * 重建索引，此方法将指定索引中的数据同步到指定的索引和类型中
     * @param sourceIndex
     * @param destIndex
     * @param destType
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, String destIndex, String destType) {
        return this.reIndexFromLocal(sourceIndex, null, destIndex, destType, null);
    }

    /**
     * 重建索引，此方法将指定索引中的size条数据同步到指定的索引和类型中
     * @param sourceIndex
     * @param size
     * @param destIndex
     * @param destType
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex, String destType) {
        return this.reIndexFromLocal(sourceIndex, size, destIndex, destType, null);
    }

    /**
     * 重建索引，此方法可以根据条件将指定索引中的数据同步到指定的索引中，其原理也是通过滚动查询来实现。
     * 注意：此方法不会copy源索引的设置，包括分片，副本，映射等等；所以在调用此方法之前最好自己手动设置好分片，副本，映射等等。
     * @param sourceIndex 源索引名称
     * @param size 要拷贝的条数
     * @param destIndex 目标索引名称
     * @param destType 目标type名称
     * @param queryBuilder 过滤条件，如果只想同步源索引中指定type的数据，请在此参数中设置
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex,
                      String destType, QueryBuilder queryBuilder) {
        return this.reIndexFromLocal(sourceIndex, size, destIndex, destType, queryBuilder, null);
    }

    /**
     * 重建索引，此方法可以根据条件将指定索引中的数据同步到指定的索引中，其原理也是通过滚动查询来实现。
     * 注意：此方法不会copy源索引的设置，包括分片，副本，映射等等；所以在调用此方法之前最好自己手动设置好分片，副本，映射等等。
     * @param sourceIndex 源索引名称
     * @param size 要拷贝的条数
     * @param destIndex 目标索引名称
     * @param destType 目标type名称
     * @param queryBuilder 过滤条件，如果只想同步源索引中指定type的数据，请在此参数中设置
     * @return
     */
    public ReIndexResult reIndexFromLocal(String sourceIndex, Integer size, String destIndex,
                      String destType, QueryBuilder queryBuilder, List<MappingDTO> updateFields) {
        sourceIndex = Objects.requireNonNull(sourceIndex, "源Index不能为空");
        destIndex = Objects.requireNonNull(destIndex, "新Index不能为空");
        ReindexRequestBuilder requestBuilder = ReindexAction.INSTANCE.newRequestBuilder(transportClient);
        requestBuilder.source(sourceIndex);
        if (!StringUtils.isEmpty(destType)) {
            requestBuilder.destination(destIndex, destType);
        } else {
            requestBuilder.destination(destIndex);
        }
        if (null != queryBuilder) {
            requestBuilder.filter(queryBuilder);
        }
        if (null != size && 0 < size) {
            requestBuilder.size(size);
        }
        if (null != updateFields) {
            StringBuilder sb = new StringBuilder();
            for (MappingDTO mappingBean : updateFields) {
                sb.append("ctx._source.").append(mappingBean.getNewField())
                  .append("=").append("ctx._source.remove('").append(mappingBean.getOldField())
                  .append("');");
            }
            requestBuilder.script(new Script(sb.toString()));
        }
        BulkByScrollResponse response = requestBuilder.execute().actionGet();
        ReIndexResult reIndexResult = new ReIndexResult();
        reIndexResult.setTook(response.getTook().getMillis());
        reIndexResult.setTotal(response.getStatus().getTotal());
        reIndexResult.setCreated(response.getCreated());
        reIndexResult.setUpdated(response.getUpdated());
        reIndexResult.setFailures(response.getBulkFailures().size());
        if (response.getBulkFailures().size() > 0) {
            reIndexResult.setCause(response.getBulkFailures().get(0).getCause().getMessage());
//            reIndexResult.setCauseDetailMessage(response.getBulkFailures().get(0).getCause().toString());
        }
        return reIndexResult;
    }
}
