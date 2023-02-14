package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createTestLoggingClass() throws ClassNotFoundException {
        var handler = new LoggingInvocationHandler(new TestLogging(),
                collectLoggedMethods(TestLogging.class.getName(), new HashMap<>()));

        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    private static Map<String, List<Integer>> collectLoggedMethods(String className, Map<String,
            List<Integer>> loggedMethods) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (var method : declaredMethods) {
            if (method.isAnnotationPresent(Log.class)) {
                if (!loggedMethods.containsKey(method.getName())) {
                    loggedMethods.put(method.getName(), new ArrayList<>());
                }
                loggedMethods.get(method.getName()).add(method.getParameterCount());
            }
        }
        return loggedMethods;
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
