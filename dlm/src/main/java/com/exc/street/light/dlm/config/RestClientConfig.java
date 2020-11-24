package com.exc.street.light.dlm.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;

@Configuration
public class RestClientConfig {
    @Autowired
    private MappingJackson2HttpMessageConverter jsonConverter;
    private final PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
    private final CloseableHttpClient client = HttpClients.custom().setConnectionManager(pool).build();
    private final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate rest = new RestTemplate(factory);
        int index=0;
        for (int i=0;i<rest.getMessageConverters().size();i++) {
            HttpMessageConverter<?> converter = rest.getMessageConverters().get(i);
            if(MappingJackson2HttpMessageConverter.class.isInstance(converter)){
                rest.getMessageConverters().set(i,jsonConverter);
                index=1;
            }
        }
        if(index==0){
            rest.getMessageConverters().add(jsonConverter);
        }
        return rest;
    }
    @PreDestroy
    public void preDestroy() throws Exception{
        client.close();
        pool.close();
    }
}
