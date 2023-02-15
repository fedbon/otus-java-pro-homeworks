package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createTestLoggingClass() {
        var handler = new LoggingInvocationHandler(new TestLogging(),
                collectMethods(new HashMap<>()));

        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    private static Map<String, List<Integer>> collectMethods(Map<String,
            List<Integer>> methods) {
        Method[] declaredMethods = TestLogging.class.getDeclaredMethods();
        for (var method : declaredMethods) {
            if (method.isAnnotationPresent(Log.class)) {
                if (!methods.containsKey(method.getName())) {
                    methods.put(method.getName(), new ArrayList<>());
                }
                methods.get(method.getName()).add(method.getParameterCount());
            }
        }
        return methods;
    }

    static class LoggingInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface testLoggingClass;
        private final Map<String, List<Integer>> loggedMethods;

        LoggingInvocationHandler(TestLoggingInterface testLoggingClass, Map<String, List<Integer>> loggedMethods) {
            this.testLoggingClass = testLoggingClass;
            this.loggedMethods = loggedMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.containsKey(method.getName()) &&
                    loggedMethods.get(method.getName()).contains(method.getParameterCount())) {
                System.out.printf("executed method: %s, param: %s%n", method.getName(), Arrays.toString(args));
            }
            return method.invoke(testLoggingClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "testLoggingClass=" + testLoggingClass +
                    '}';
        }
    }
}
