package com.kzh.sys.app;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gang on 2019/5/6.
 */
public class AppLock {
    private static ConcurrentHashMap<String, Long> repeatSubmitMap = new ConcurrentHashMap<>();

    public static boolean lockSubmit(String username, String url, Integer lockMilliseconds) {
        return lock(repeatSubmitMap, username + url, lockMilliseconds);
    }

    public static boolean lock(ConcurrentHashMap<String, Long> map, String key, Integer lockMilliseconds) {
        Long lockEnd = repeatSubmitMap.get(key);
        Long current = System.currentTimeMillis();
        if (lockEnd == null || lockEnd < current) {
            map.put(key, current + lockMilliseconds);
            return true;
        }
        return false;
    }

    public static void unlock(ConcurrentHashMap<String, Long> map, String key) {
        map.remove(key);
    }
}
