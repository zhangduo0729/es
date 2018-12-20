package com.hivescm.estools.common;

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

@Service
public class CreateIndex {

    private final static Logger logger = LoggerFactory.getLogger(CreateIndex.class);

    @Autowired
    private TransportClient transportClient;

    /**
     * 最简单的创建Index的方法，只用指定一个Index即可
     * @param _index
     * @return
     */
    public boolean createIndex(String _index) {
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate(_index).get();
        return response.isAcknowledged();
    }

    /**
     * 创建一个Index，并指定其别名
     * @param _index
     * @param alias 索引别名
     * @return
     */
    public boolean createIndex(String _index, String alias) {
        CreateIndexRequestBuilder indexRequestBuilder = transportClient.admin().indices().prepareCreate(_index);
        Map<String, Object> aliases = new HashMap<>();
        aliases.put(alias, new HashMap<>());
        indexRequestBuilder.setAliases(aliases);
        CreateIndexResponse response = indexRequestBuilder.get();
        return response.isAcknowledged();
    }

    /**
     * 创建索引，并指定settings设置和Mappings映射
     * @param _index 索引名称
     * @param mappings 为其具体的映射
     * @return
     */
    public boolean createIndex(String _index, String _type, Map<String, Object> mappings) {
        String alias = _index + "-alias";
        Map<String, Map<String, Object>> mappingMap = new HashMap<>();
        mappingMap.put(_type, mappings);
        return this.createIndex(_index, null, mappingMap, alias);
    }

    /**
     * 创建索引，并指定settings设置和Mappings映射
     * @param _index 索引名称
     * @param mappingMap Map<String, Map<String, Object>> key为type，value(Map<String, Object>)为其具体的映射
     * @return
     */
    public boolean createIndex(String _index, Map<String, Map<String, Object>> mappingMap) {
        String alias = _index + "-alias";
        return this.createIndex(_index, null, mappingMap, alias);
    }

    /**
     * 创建索引，并指定settings设置和Mappings映射
     * @param _index 索引名称
     * @param settingMap 自定义setting设置，比如分片数，副本数，自定义分析器等等
     * @param mappingMap Map<String, Map<String, Object>> key为type，value(Map<String, Object>)为其具体的映射
     * @return
     */
    public boolean createIndex(String _index, Map<String, Object> settingMap, Map<String, Map<String, Object>> mappingMap) {
        String alias = _index + "-alias";
        return this.createIndex(_index, settingMap, mappingMap, alias);
    }

    /**
     * 创建索引，并指定settings设置和Mappings映射
     * @param _index 索引名称
     * @param settingMap 自定义setting设置，比如分片数，副本数，自定义分析器等等
     * @param mappingMap Map<String, Map<String, Object>> key为type，value(Map<String, Object>)为其具体的映射
     * @param alias 别名
     * @return
     */
    public boolean createIndex(String _index, Map<String, Object> settingMap, Map<String, Map<String, Object>> mappingMap, String alias) {
        CreateIndexRequestBuilder request = transportClient.admin().indices().prepareCreate(_index);
        if (null != settingMap) {
            request.setSettings(settingMap);
        }
        for (Map.Entry<String, Map<String, Object>> mapEntry : mappingMap.entrySet()) {
            request.addMapping(mapEntry.getKey(), mapEntry.getValue());
        }
        if (!StringUtils.isEmpty(alias)) {
            Map<String, Object> aliases = new HashMap<>();
            aliases.put(alias, new HashMap<>());
            request.setAliases(aliases);
        }
        CreateIndexResponse response = request.get();
        boolean result = response.isAcknowledged();
        logger.info("index = {}, 创建: {}", _index , (result ? "成功" : "失败"));
        return result;
    }
}
