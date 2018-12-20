package com.hivescm.estools.reindex;

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
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * author ygm
 * 此类主要是 reindex 的远程实现，可将指定远程的Index数据考本到本地Index中，可添加拷贝数据的条数及拷贝条件
 */
@Service
public class RemoteReIndex {

    private static final String remoteHost = "192.168.177.11";

    private static final Integer remotePort = 9200;

    private static final String remoteESUsername = "elastic";

    private static final String remoteESPassword = "changeme";

    @Autowired
    private TransportClient transportClient;


    /**
     * 重建索引，此方法将远程指定索引中的数据同步到本地指定的索引中
     * @param sourceIndex  远程索引名称
     * @param destIndex 目标索引名称
     * @return
     */
    public String reIndexFromRemote(String sourceIndex, String destIndex) {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return this.reIndexFromRemote(sourceIndex, null, destIndex, null, queryBuilder);
    }

    /**
     * 重建索引，此方法将远程指定索引中的size条数据同步到本地指定的索引中
     * @param sourceIndex 远程索引名称
     * @param size 拷贝的数量
     * @param destIndex 目标索引名称
     * @return
     */
    public String reIndexFromRemote(String sourceIndex, Integer size, String destIndex) {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return this.reIndexFromRemote(sourceIndex, size, destIndex, null, queryBuilder);
    }

    /**
     * 重建索引，此方法将远程指定索引中的数据同步到本地指定的索引和类型中
     * @param sourceIndex
     * @param destIndex
     * @param destType
     * @return
     */
    public String reIndexFromRemote(String sourceIndex, String destIndex, String destType) {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return this.reIndexFromRemote(sourceIndex, null, destIndex, destType, queryBuilder);
    }

    /**
     * 重建索引，此方法将远程指定索引中的size条数据同步到本地指定的索引和类型中
     * @param sourceIndex
     * @param size
     * @param destIndex
     * @param destType
     * @return
     */
    public String reIndexFromRemote(String sourceIndex, Integer size, String destIndex, String destType) {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return this.reIndexFromRemote(sourceIndex, size, destIndex, destType, queryBuilder);
    }


    /**
     * 重建索引，此方法可以根据条件将远程指定索引中的数据同步到本地指定的索引中，其原理也是通过滚动查询来实现。
     * 注意：此方法不会copy源索引的设置，包括分片，副本，映射等等；所以在调用此方法之前最好自己手动设置好分片，副本，映射等等。
     * @param sourceIndex 源索引名称
     * @param size 要拷贝的条数
     * @param destIndex 目标索引名称
     * @param destType 目标type名称
     * @param queryBuilder 过滤条件，如果只想同步源索引中指定type的数据，请在此参数中设置
     * @return
     */
    public String reIndexFromRemote(String sourceIndex, Integer size, String destIndex, String destType, QueryBuilder queryBuilder) {
        sourceIndex = Objects.requireNonNull(sourceIndex, "源Index不能为空");
        destIndex = Objects.requireNonNull(destIndex, "新Index不能为空");
        ReindexRequestBuilder requestBuilder = ReindexAction.INSTANCE.newRequestBuilder(transportClient);
        BytesArray bytesArray = new BytesArray(queryBuilder.toString());
        RemoteInfo remoteInfo = new RemoteInfo("http", remoteHost, remotePort, bytesArray,
                remoteESUsername, remoteESPassword, new HashMap<>(),
                new TimeValue(10000), new TimeValue(10000));
        requestBuilder.setRemoteInfo(remoteInfo);
        requestBuilder.source(sourceIndex);
        requestBuilder.destination(destIndex);
        if (!StringUtils.isEmpty(destType)) {
            requestBuilder.destination(destIndex, destType);
        }
        if (null != size && 0 < size) {
            requestBuilder.size(size);
        }
        BulkByScrollResponse response = requestBuilder.execute().actionGet();
        return response.toString();
    }


}
