package com.webfontaine.ghclient.helper;

import com.webfontaine.ghclient.dto.api.StatisticDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

/**
 * The helper class for putting and getting object to cache.
 */

@Component
@Slf4j
public class CacheHelper {

    private static final String CACHE_NAME = "statisticCache";

    private final CacheManager cacheManager;

    @Autowired
    public CacheHelper(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Add statistic object to cache
     *
     * @param statistic statistic
     */
    public void addToCache(StatisticDto statistic) {
        getCache().put(statistic.getCacheId(), statistic);
    }

    /**
     * Get statistic object from cache.
     * @param cacheId cache id
     * @return statistic object
     */
    public StatisticDto getById(String cacheId) {
        log.debug("Get from cache with it {}", cacheId);
        Cache.ValueWrapper valueWrapper = getCache().get(cacheId);
        if (nonNull(valueWrapper)) {
            return (StatisticDto) valueWrapper.get();
        }
        return null;
    }

    private Cache getCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            throw new RuntimeException("Failed get cache");
        }

        return cache;
    }
}
