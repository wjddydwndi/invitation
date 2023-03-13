package com.invitation.module.rds.config.redis;

import com.invitation.module.common.model.user.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableRedisRepositories
//@EnableTransactionManagement
public class RedisConfiguration {

    @Value("${spring.redis.enable}")
    private boolean enable;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

/** factory library는 spring data redis에 내장되어있는 Lettuce 사용**/

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(Integer.parseInt(port));
        // redisStandaloneConfiguration.setPassword(password);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }



    @Bean(name = "redisTokenTemplate")
    public RedisTemplate<?, ?> redisTokenTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

    @Bean(name = "redisUserTemplate")
    public RedisTemplate<?, ?> redisUserTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

    @Bean(name = "redisConfigurationTemplate")
    public RedisTemplate<?, ?> redisConfigurationTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }



}
