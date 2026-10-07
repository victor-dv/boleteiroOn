package br.com.boleiroOn.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    // ===========================================
    // CACHE LOCAL (Caffeine) - Para desenvolvimento / single instance
    // ===========================================
    @Bean
    @Primary
    @ConditionalOnProperty(name = "cache.type", havingValue = "caffeine", matchIfMissing = true)
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .expireAfterAccess(Duration.ofMinutes(10))
                .recordStats());
        cacheManager.setCacheNames(Arrays.asList(
                "leiloes",
                "leiloes-status",
                "lotes-por-leilao",
                "arrematantes-por-leilao",
                "arrematacoes-feed",
                "arrematacoes-feed-nulas",
                "arrematacoes-feed-assinadas",
                "users",
                "leilao-detalhado"
        ));
        return cacheManager;
    }

    // ===========================================
    // CACHE DISTRIBUÍDO (Redis) - Para produção multi-instância
    // ===========================================
    @Bean
    @ConditionalOnProperty(name = "cache.type", havingValue = "redis")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(
                        java.util.Map.of(
                                "leiloes", config.entryTtl(Duration.ofMinutes(60)),
                                "leiloes-status", config.entryTtl(Duration.ofMinutes(60)),
                                "lotes-por-leilao", config.entryTtl(Duration.ofMinutes(15)),
                                "arrematantes-por-leilao", config.entryTtl(Duration.ofMinutes(15)),
                                "arrematacoes-feed", config.entryTtl(Duration.ofMinutes(5)),
                                "arrematacoes-feed-nulas", config.entryTtl(Duration.ofMinutes(5)),
                                "arrematacoes-feed-assinadas", config.entryTtl(Duration.ofMinutes(5)),
                                "users", config.entryTtl(Duration.ofMinutes(60)),
                                "leilao-detalhado", config.entryTtl(Duration.ofMinutes(30))
                        )
                )
                .transactionAware()
                .build();
    }

    // ===========================================
    // Configuração de TTL por cache (Caffeine)
    // ===========================================
    @Bean
    @ConditionalOnProperty(name = "cache.type", havingValue = "caffeine", matchIfMissing = true)
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .expireAfterAccess(Duration.ofMinutes(10))
                .recordStats();
    }
}