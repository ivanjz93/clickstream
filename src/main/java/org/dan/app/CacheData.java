package org.dan.app;

import java.util.HashMap;
import java.util.Map;

public class CacheData {
    private static Map<String, Integer> pvMap = new HashMap<>();
    private static Map<String, Long> uvMap = new HashMap<>();


    public static void setPvMap(HashMap<String,Integer> pvMap) {
        CacheData.pvMap = pvMap;
    }

    public static void setUvMap(HashMap<String,Long> uvMap) {
        CacheData.uvMap = uvMap;
    }

    public static int getPv(int pv, String indexName) {
        Integer cacheValue = pvMap.get(indexName);
        if(cacheValue == null) {
            cacheValue = 0;
            pvMap.put(indexName, cacheValue);
        }

        if(pv > cacheValue.intValue()){
            pvMap.put(indexName, pv);
            return pv - cacheValue.intValue();
        }
        return 0;
    }

    public static long getUv(long uv, String indexName) {
        Long cacheValue = uvMap.get(indexName);
        if(cacheValue == null) {
            cacheValue = 0l;
            uvMap.put(indexName, cacheValue);

        }
        if(uv > cacheValue.longValue()) {
            uvMap.put(indexName, uv);
            return uv - cacheValue.longValue();
        }
        return 0;
    }
}
