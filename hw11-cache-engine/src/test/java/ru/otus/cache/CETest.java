package ru.otus.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class CETest {
    private static final long START_KEY = 1L;
    private static final long END_KEY = 100L;
    private CacheEngine<String, Object> cacheEngine;

    @BeforeEach
    void init() {
        cacheEngine = new CacheEngineImpl<>();
        loopWithKeys((key) -> cacheEngine.put(key, new Object()));
    }

    @DisplayName("после запуска gc кэш должен очиститься")
    @Test
    void clearCache() throws InterruptedException {
        loopWithKeys((key) -> Assertions.assertNotNull(cacheEngine.get(key)));
        System.gc();
        Thread.sleep(100);
        loopWithKeys((key) -> Assertions.assertNull(cacheEngine.get(key)));
    }

    private void loopWithKeys(Consumer<String> consumer) {
        for (long key = START_KEY; key < END_KEY; key++) {
            consumer.accept(String.valueOf(key));
        }
    }
}
