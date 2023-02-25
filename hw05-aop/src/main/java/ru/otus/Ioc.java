package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

class Ioc {
    private Ioc() {
    }

    public static <T> T createProxy(Class<? extends T> targetClass) {
        T instance = createInstance(targetClass);
        return replaceWithProxy(instance, targetClass);
    }

    private static <T> T replaceWithProxy(T instance, Class<? extends T> targetClass) {
        InvocationHandler handler = new LoggingHandler(instance, targetClass);
        return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), handler);
    }

    private static <T> T createInstance(Class<? extends T> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
