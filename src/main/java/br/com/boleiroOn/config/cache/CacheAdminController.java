package br.com.boleiroOn.config.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/cache")
@PreAuthorize("hasRole('ADMIN')")
public class CacheAdminController {

    private final CacheManager cacheManager;

    public CacheAdminController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCacheStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                Map<String, Object> cacheInfo = new LinkedHashMap<>();
                cacheInfo.put("name", name);
                Object nativeCache = cache.getNativeCache();
                cacheInfo.put("nativeCacheClass", nativeCache != null ? nativeCache.getClass().getSimpleName() : "null");
                
                // Caffeine-specific stats
                if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> caffeineCache) {
                    cacheInfo.put("estimatedSize", caffeineCache.estimatedSize());
                    if (caffeineCache.stats() != null) {
                        var statsObj = caffeineCache.stats();
                        Map<String, Object> statsMap = new LinkedHashMap<>();
                        statsMap.put("hitCount", statsObj.hitCount());
                        statsMap.put("missCount", statsObj.missCount());
                        statsMap.put("hitRate", statsObj.hitRate());
                        statsMap.put("evictionCount", statsObj.evictionCount());
                        statsMap.put("averageLoadPenalty", statsObj.averageLoadPenalty());
                        cacheInfo.put("stats", statsMap);
                    }
                }
                stats.put(name, cacheInfo);
            }
        });
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{cacheName}")
    public ResponseEntity<Map<String, Object>> getCacheDetails(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("name", cacheName);
        Object nativeCache = cache.getNativeCache();
        details.put("nativeCacheClass", nativeCache != null ? nativeCache.getClass().getSimpleName() : "null");
        
        if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> caffeineCache) {
            details.put("estimatedSize", caffeineCache.estimatedSize());
            if (caffeineCache.stats() != null) {
                var statsObj = caffeineCache.stats();
                Map<String, Object> statsMap = new LinkedHashMap<>();
                statsMap.put("hitCount", statsObj.hitCount());
                statsMap.put("missCount", statsObj.missCount());
                statsMap.put("hitRate", statsObj.hitRate());
                statsMap.put("evictionCount", statsObj.evictionCount());
                statsMap.put("averageLoadPenalty", statsObj.averageLoadPenalty());
                details.put("stats", statsMap);
            }
        }
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/{cacheName}")
    public ResponseEntity<Map<String, String>> evictCache(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return ResponseEntity.notFound().build();
        }
        cache.clear();
        return ResponseEntity.ok(Map.of("message", "Cache '" + cacheName + "' cleared successfully"));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> evictAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
        return ResponseEntity.ok(Map.of("message", "All caches cleared successfully"));
    }

    @DeleteMapping("/{cacheName}/{key}")
    public ResponseEntity<Map<String, String>> evictCacheKey(@PathVariable String cacheName, @PathVariable String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return ResponseEntity.notFound().build();
        }
        cache.evict(key);
        return ResponseEntity.ok(Map.of("message", "Key '" + key + "' evicted from cache '" + cacheName + "'"));
    }
}