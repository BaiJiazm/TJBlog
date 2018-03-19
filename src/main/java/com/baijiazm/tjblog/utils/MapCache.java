package com.baijiazm.tjblog.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapCache {

    static class CacheObject {
        private String key;
        private Object value;
        private long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }

    /**
     * 默认存储1024个缓存
     */
    private static final int DEFAULT_CACHES = 1024;

    private static final MapCache INS = new MapCache();

    public Map<String, CacheObject> cachePool;

    public static MapCache single() {
        return INS;
    }

    public MapCache() {
        this(DEFAULT_CACHES);
    }

    public MapCache(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
    }

    public <T> T get(String key) {
        CacheObject cacheObject = cachePool.get(key);
        if (null != cacheObject) {
            long curTime = System.currentTimeMillis() / 1000;
            long expired = cacheObject.getExpired();
            if (expired <= 0 || expired >= curTime) {
                return (T) cacheObject.getValue();
            }
        }
        return null;
    }

    public void set(String key, Object value, long expired) {
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cachePool.put(key, cacheObject);
    }

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key     缓存key
     * @param field   缓存field
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void hset(String key, String field, Object value, long expired) {
        key = key + ":" + field;
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject cacheObject = new CacheObject(key, value, expired);
        cachePool.put(key, cacheObject);
    }
}
