package ru.otus.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.cache.Listener;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        CacheEngine<String, Integer> cacheEngine = new CacheEngineImpl<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        Listener<String, Integer> listener = new Listener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cacheEngine.addListener(listener);
        cacheEngine.put("1", 1);

        logger.info("getValue:{}", cacheEngine.get("1"));
        cacheEngine.remove("1");
        cacheEngine.removeListener(listener);
    }
}
