package ru.otus.engine;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.service.ConsoleOutputService;
import ru.otus.service.OutputService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestRunner {
    private final OutputService outputService = new ConsoleOutputService();

    public void runTests(String className)
            throws IllegalAccessException, InvocationTargetException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor();
        int failedTestCount = 0;
        Object object;

        List<Method> beforeAnnotationMethods = getMethodsByAnnotation(clazz, Before.class);
        List<Method> testAnnotationMethods = getMethodsByAnnotation(clazz, Test.class);
        List<Method> afterAnnotationMethods = getMethodsByAnnotation(clazz, After.class);

        for (Method method : testAnnotationMethods) {
            object = constructor.newInstance();
            try {
                invokeMethods(beforeAnnotationMethods, object);
                invokeMethods(Collections.singletonList(method), object);
            } catch (Exception e) {
                outputService.outputMessage("This method should fail the test");
                failedTestCount++;
            } finally {
                invokeMethods(afterAnnotationMethods, object);
            }
        }
        outputService.printBorder();
        outputService.printTotalNumberOfTests(testAnnotationMethods.size());
        outputService.printPassedTests(testAnnotationMethods.size() - failedTestCount);
        outputService.printFailedTests(failedTestCount);
        outputService.printBorder();
    }

    private List<Method> getMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    private void invokeMethods(List<Method> methodsWithAnnotation, Object object)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methodsWithAnnotation) {
            method.invoke(object);
        }
    }
}
