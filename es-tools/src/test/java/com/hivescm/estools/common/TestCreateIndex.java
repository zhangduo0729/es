package com.hivescm.estools.common;

import com.hivescm.estools.EsToolsApplication;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasAction;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EsToolsApplication.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class TestCreateIndex {

    @Autowired
    private TransportClient transportClient;

    @Autowired
    private CreateIndex createIndex;

    @Test
    public void testCreateIndex() {
        String _index = "my_index_v10";
        boolean b = createIndex.createIndex(_index);
        System.out.println("创建带别名的索引结果为：" + b);

    }

    @Test
    public void testCreateWithAlias() {
        String _index = "my_index_v8";
        String _alias = "my_v10";
        boolean b = createIndex.createIndex(_index, _alias);
        System.out.println("创建带别名的索引结果为：" + b);
    }

    @Test
    public void testIndexDocument() {
        Map<String, Object> soureMap = new HashMap<>();
        soureMap.put("goodsName", "雅戈尔天蓝XL");
        IndexResponse response = transportClient
                .prepareIndex("my_index_v11", "goods", null)
                .setSource(soureMap)
                .get();
        System.out.println(response.getResult().getLowercase());
    }

    @Test
    public void testIndexExists() {
        String indexName = "my_index_v55_tmp";
        IndicesExistsRequest existsRequest = new IndicesExistsRequest(indexName, "my_index_v5", "my_index_");
        IndicesExistsResponse response = transportClient.admin().indices().exists(existsRequest).actionGet();
        boolean result = response.isExists();
        System.out.println("result : " + result);
    }
}
