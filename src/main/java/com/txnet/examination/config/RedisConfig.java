package com.txnet.examination.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * redisConfig
 */
@Configuration
@Slf4j
public class RedisConfig {
    @Value("${jedis.database:0}")
    private int database;
    @Value("${jedis.host}")
    private String host;
    @Value("${jedis.port}")
    private int port;
    @Value("${jedis.password}")
    private String password;
    @Value("${jedis.connectionTimeout}")
    private int timeout;
    @Value("${jedis.maxTotal}")
    private int maxActive;
    @Value("${jedis.maxWaitMillis}")
    private long maxWaitMillis;
    @Value("${jedis.maxIdle}")
    private int maxIdle;
    @Value("${jedis.minIdle}")
    private int minIdle;

    /**
     * 连接池配置信息
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大连接数
        jedisPoolConfig.setMaxTotal(maxActive);
        // 当池内没有可用连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 最大空闲连接数
        jedisPoolConfig.setMinIdle(maxIdle);
        // 最小空闲连接数
        jedisPoolConfig.setMinIdle(minIdle);
        // 其他属性可以自行添加
        return jedisPoolConfig;
    }

    /**
     * Jedis 连接
     * @param jedisPoolConfig   config
     * @return JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
                .poolConfig(jedisPoolConfig).and().readTimeout(Duration.ofMillis(timeout)).build();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        redisStandaloneConfiguration.setDatabase(database);
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(jedisConnectionFactory(jedisPoolConfig()));
        return redisTemplate;
    }


}
