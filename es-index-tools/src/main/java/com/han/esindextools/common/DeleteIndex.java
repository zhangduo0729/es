package com.han.esindextools.common;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.exception.ErrorCode;
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

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 13:58
 */
@Service
public class DeleteIndex {
    private static final Logger logger = LoggerFactory.getLogger(DeleteIndex.class);

    @Autowired
    private TransportClient transportClient;

    public DataResult deleteIndex(String[] _index)
    {
        logger.info("待删除的索引名称为：{}", _index);
        DeleteIndexRequest indexRequest = new DeleteIndexRequest(_index);
        try {
            DeleteIndexResponse response = (DeleteIndexResponse)this.transportClient.admin().indices().delete(indexRequest).get();
            boolean result = response.isAcknowledged();
            if (result) {
                return DataResult.success(Boolean.TRUE);
            }
            return DataResult.faild(ErrorCode.SYSTEM_ERROR);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return DataResult.faild(1508, e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return DataResult.faild(1508, e.getMessage());
        }
    }

    public Long deleteByQuery(String index, QueryBuilder queryBuilder)
    {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(this.transportClient);
        builder.source(new String[] { index });
        if (null != queryBuilder) {
            builder.filter(queryBuilder);
        }
        BulkByScrollResponse response = (BulkByScrollResponse)builder.get();
        long deletedNum = response.getDeleted();
        return Long.valueOf(deletedNum);
    }
}
