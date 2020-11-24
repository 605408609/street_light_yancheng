package com.exc.street.light.sl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2020/1/8 15:32
 */

@Component
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfiguration {
    private String host;
    private String clientId;
    private String username;
    private String password;
    private int timeout;
    private int KeepAlive;
    private String[] topic;
    private int[] qos;

    public String getHost()
    {
        return host;
    }

    public String getClientId()
    {
        return clientId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public int getKeepAlive()
    {
        return KeepAlive;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public void setKeepAlive(int keepAlive)
    {
        KeepAlive = keepAlive;
    }

    public String[] getTopic()
    {
        return topic;
    }

    public int[] getQos()
    {
        return qos;
    }

    public void setTopic(String[] topic)
    {
        this.topic = topic;
    }

    public void setQos(int[] qos)
    {
        this.qos = qos;
    }

}
