package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class Ioc<T> {
    private final T object;

    public Ioc(T object) {
        this.object = object;
    }

    public T createProxy() {
        InvocationHandler handler = new LoggingHandler(object);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), object.getClass().getInterfaces(), handler);
    }

    class LoggingHandler implements InvocationHandler {

        private final T targetClassInstance;
        private final Set<Method> methodsForLogging;

        LoggingHandler(T targetClassInstance) {
            this.targetClassInstance = targetClassInstance;
            this.methodsForLogging = getMethodsForLogging(targetClassInstance);
        }

        private Set<Method> getMethodsForLogging(T targetClassInstance) {
            return Arrays.stream(targetClassInstance.getClass().getMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .map(this::getMethodsFromInterface)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }

        private Set<Method> getMethodsFromInterface(Method methods) {
            return Arrays.stream(targetClassInstance.getClass().getInterfaces())
                    .map(Class::getDeclaredMethods).flatMap(Arrays::stream)
                    .filter(m -> m.getName().equals(methods.getName()))
                    .filter(m -> Arrays.equals(m.getParameterTypes(), methods.getParameterTypes()))
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodsForLogging.contains(method)) {
                System.out.printf("executed method: %s, params: %s%n", method.getName(), Arrays.toString(args));
            }
            return method.invoke(targetClassInstance, args);
        }
    }
}
