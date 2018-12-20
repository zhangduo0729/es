package com.han.esindextools.reindex;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;
import org.elasticsearch.index.reindex.RemoteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:08
 */
@Service
public class RemoteReIndex {
    private static final String remoteHost = "192.168.177.11";
    private static final Integer remotePort = Integer.valueOf(9200);
    private static final String remoteESUsername = "elastic";
    private static final String remoteESPassword = "changeme";

    @Autowired
    private TransportClient transportClient;

    public String reIndexFromRemote(String sourceIndex, String destIndex)
    {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return reIndexFromRemote(sourceIndex, null, destIndex, null, queryBuilder);
    }

    public String reIndexFromRemote(String sourceIndex, Integer size, String destIndex)
    {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return reIndexFromRemote(sourceIndex, size, destIndex, null, queryBuilder);
    }

    public String reIndexFromRemote(String sourceIndex, String destIndex, String destType)
    {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return reIndexFromRemote(sourceIndex, null, destIndex, destType, queryBuilder);
    }

    public String reIndexFromRemote(String sourceIndex, Integer size, String destIndex, String destType)
    {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return reIndexFromRemote(sourceIndex, size, destIndex, destType, queryBuilder);
    }

    public String reIndexFromRemote(String sourceIndex, Integer size, String destIndex, String destType, QueryBuilder queryBuilder)
    {
        sourceIndex = (String)Objects.requireNonNull(sourceIndex, "源Index不能为空");
        destIndex = (String)Objects.requireNonNull(destIndex, "新Index不能为空");
        ReindexRequestBuilder requestBuilder = ReindexAction.INSTANCE.newRequestBuilder(this.transportClient);
        BytesArray bytesArray = new BytesArray(queryBuilder.toString());
        RemoteInfo remoteInfo = new RemoteInfo("http", "192.168.177.11", remotePort.intValue(), bytesArray, "elastic", "changeme", new HashMap(), new TimeValue(10000L), new TimeValue(10000L));

        requestBuilder.setRemoteInfo(remoteInfo);
        requestBuilder.source(new String[] { sourceIndex });
        requestBuilder.destination(destIndex);
        if (!StringUtils.isEmpty(destType)) {
            requestBuilder.destination(destIndex, destType);
        }
        if ((null != size) && (0 < size.intValue())) {
            requestBuilder.size(size.intValue());
        }
        BulkByScrollResponse response = (BulkByScrollResponse)requestBuilder.execute().actionGet();
        return response.toString();
    }
}
