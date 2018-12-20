package com.han.esindextools.common;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 13:57
 */
@Service
public class CreateIndex {
    private static final Logger logger = LoggerFactory.getLogger(CreateIndex.class);

    @Autowired
    private TransportClient transportClient;

    public boolean createIndex(String _index)
    {
        CreateIndexResponse response = (CreateIndexResponse)this.transportClient.admin().indices().prepareCreate(_index).get();
        return response.isAcknowledged();
    }

    public boolean createIndex(String _index, String alias)
    {
        CreateIndexRequestBuilder indexRequestBuilder = this.transportClient.admin().indices().prepareCreate(_index);
        Map aliases = new HashMap();
        aliases.put(alias, new HashMap());
        indexRequestBuilder.setAliases(aliases);
        CreateIndexResponse response = (CreateIndexResponse)indexRequestBuilder.get();
        return response.isAcknowledged();
    }

    public boolean createIndex(String _index, String _type, Map<String, Object> mappings)
    {
        String alias = _index + "-alias";
        Map mappingMap = new HashMap();
        mappingMap.put(_type, mappings);
        return createIndex(_index, null, mappingMap, alias);
    }

    public boolean createIndex(String _index, Map<String, Map<String, Object>> mappingMap)
    {
        String alias = _index + "-alias";
        return createIndex(_index, null, mappingMap, alias);
    }

    public boolean createIndex(String _index, Map<String, Object> settingMap, Map<String, Map<String, Object>> mappingMap)
    {
        String alias = _index + "-alias";
        return createIndex(_index, settingMap, mappingMap, alias);
    }

    public boolean createIndex(String _index, Map<String, Object> settingMap, Map<String, Map<String, Object>> mappingMap, String alias)
    {
        CreateIndexRequestBuilder request = this.transportClient.admin().indices().prepareCreate(_index);
        if (null != settingMap) {
            request.setSettings(settingMap);
        }
        for (Map.Entry mapEntry : mappingMap.entrySet()) {
            request.addMapping((String)mapEntry.getKey(), (Map)mapEntry.getValue());
        }
        if (!StringUtils.isEmpty(alias)) {
            Object aliases = new HashMap();
            ((Map)aliases).put(alias, new HashMap());
            request.setAliases((Map)aliases);
        }
        CreateIndexResponse response = (CreateIndexResponse)request.get();
        boolean result = response.isAcknowledged();
        logger.info("index = {}, 创建: {}", _index, result ? "成功" : "失败");
        return result;
    }
}
