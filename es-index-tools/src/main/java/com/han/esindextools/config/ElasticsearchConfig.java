package com.han.esindextools.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:01
 */
@Configuration
public class ElasticsearchConfig {
    @Value("${es.cluster-nodes}")
    private String clusterNodes;

    @Value("${es.cluster-name}")
    private String clusterName;

    @Value("${es.username}")
    private String username;

    @Value("${es.password}")
    private String password;

    @Bean
    public TransportClient clientFactory()
            throws UnknownHostException
    {
        Settings settings = Settings.builder().put("cluster.name", this.clusterName)
                .put("xpack.security.user", this.username + ":" + this.password)
                .build();

        TransportClient transportClient = new PreBuiltXPackTransportClient(settings, new Class[0]);
        String[] addresses = this.clusterNodes.split(",");
        for (String address : addresses) {
            String[] hostAndPort = address.split(":");

            InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName(hostAndPort[0]),
                    Integer.valueOf(hostAndPort[1]).intValue());
            transportClient.addTransportAddress(inetSocketTransportAddress);
        }
        return transportClient;
    }
}
