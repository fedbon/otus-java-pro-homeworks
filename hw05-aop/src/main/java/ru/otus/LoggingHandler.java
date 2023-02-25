package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class LoggingHandler implements InvocationHandler {

    private final Class<?> targetClass;
    private final Object instance;
    private final Set<Method> methodsForLogging;

    public LoggingHandler(Object instance, Class<?> targetClass) {
        this.targetClass = targetClass;
        this.instance = instance;
        this.methodsForLogging = getMethodsForLogging(targetClass);
    }

    private Set<Method> getMethodsForLogging(Class<?> targetClass) {
        return Arrays.stream(targetClass.getMethods())
                .filter(m -> m.isAnnotationPresent(Log.class))
                .map(this::getMethodsFromInterface)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Method> getMethodsFromInterface(Method methodsFromTargetClass) {
        return Arrays.stream(targetClass.getInterfaces())
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(m -> m.getName().equals(methodsFromTargetClass.getName()))
                .filter(m -> Arrays.equals(m.getParameterTypes(), methodsFromTargetClass.getParameterTypes()))
                .collect(Collectors.toSet());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methodsForLogging.contains(method)) {
            System.out.printf("executed method: %s, params: %s%n", method.getName(), Arrays.toString(args));
        }
        return method.invoke(instance, args);
    }

}
