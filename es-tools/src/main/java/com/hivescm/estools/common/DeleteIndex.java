package com.hivescm.estools.common;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.exception.StatusDefinition;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.hivescm.common.exception.ErrorCode.SYSTEM_ERROR;

@Service
public class DeleteIndex {

    private final static Logger logger = LoggerFactory.getLogger(DeleteIndex.class);

    @Autowired
    private TransportClient transportClient;

    /**
     * 删除指定索引，可批量删除
     * @param _index
     * @return
     */
    public DataResult deleteIndex(String... _index) {
        logger.info("待删除的索引名称为：{}", _index);
        DeleteIndexRequest indexRequest = new DeleteIndexRequest(_index);
        try {
            DeleteIndexResponse response = transportClient.admin().indices().delete(indexRequest).get();
            boolean result = response.isAcknowledged();
            if (result) {
                return DataResult.success(Boolean.TRUE);
            } else {
                return DataResult.faild(SYSTEM_ERROR.SYSTEM_ERROR);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return DataResult.faild(1508, e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return DataResult.faild(1508, e.getMessage());
        }
    }

    /**
     * 根据条件删除指定index下的数据
     * @param index
     * @param queryBuilder
     * @return
     */
    public Long deleteByQuery(String index, QueryBuilder queryBuilder) {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient);
        builder.source(index);
        if (null != queryBuilder) {
            builder.filter(queryBuilder);
        }
        BulkByScrollResponse response = builder.get();
        long deletedNum = response.getDeleted();
        return deletedNum;
    }
}
