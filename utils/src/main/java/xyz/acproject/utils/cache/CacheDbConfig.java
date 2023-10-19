package xyz.acproject.utils.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.acproject.utils.JodaTimeUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jane
 * @ClassName CacheDbConfig
 * @Description 用的时候才移除的内存缓存
 * @date 2022/5/20 15:11
 * @Copyright:2022
 */
public class CacheDbConfig {


    public static final Map<String, CacheDb> CACHE_DB_MAP = new ConcurrentHashMap<>(32);


    public static <T> CacheDb<T> getCacheDb(String key) {
        CacheDb<T> cacheDb = CACHE_DB_MAP.get(key);
        if (cacheDb == null) {
            return null;
        } else {
            if (cacheDb.isExpired()) {
                CACHE_DB_MAP.remove(key);
                return null;
            }
            return cacheDb;
        }
    }

    public static <T> T get(String key) {
        CacheDb<T> cacheDb = getCacheDb(key);
        if (cacheDb == null) {
            return null;
        } else {
            return cacheDb.getT();
        }
    }

    public static void put(String key, CacheDb cacheDb) {
        CACHE_DB_MAP.put(key, cacheDb);
    }

    public static <T> void put(String key, T t) {
        CACHE_DB_MAP.put(key, CacheDb.builder().t(t).createMills(JodaTimeUtils.getcurrMills()).expiredMills(Integer.MAX_VALUE).build());
    }

    public static <T> void put(String key, T t, long second) {
        CACHE_DB_MAP.put(key, CacheDb.builder().t(t).createMills(JodaTimeUtils.getcurrMills()).expiredMills(second*1000).build());
    }

    public static void remove(String key) {
        CACHE_DB_MAP.remove(key);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheDb<T> {
        public T t;
        @Builder.Default
        private long createMills = 0;
        @Builder.Default
        private long expiredMills = 5000;

        public boolean isExpired() {
            return JodaTimeUtils.getcurrMills()-expiredMills > this.createMills;
        }
    }
}
