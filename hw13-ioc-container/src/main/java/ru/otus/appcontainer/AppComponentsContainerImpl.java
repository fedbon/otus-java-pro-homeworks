package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        var configConstructor = configClass.getDeclaredConstructor();
        var configMethods = Arrays.asList(configClass.getDeclaredMethods());
        var configInstance = configConstructor.newInstance();

        var beanDefinitions = loadBeanDefinitions(configMethods);
        createContext(beanDefinitions, configInstance);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not a config %s", configClass.getName()));
        }
    }

    private List<Method> loadBeanDefinitions(List<Method> methods) {
        return methods.stream()
                .filter(this::isBean)
                .sorted(invokeOrder())
                .toList();
    }

    private boolean isBean(Method method) {
        return method.isAnnotationPresent(AppComponent.class);
    }

    private Comparator<Method> invokeOrder() {
        return Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order());
    }

    private void createContext(List<Method> beanDefinitions, Object configInstance) throws InvocationTargetException, IllegalAccessException {
        for (var beanDefinition : beanDefinitions) {
            var dependencies = createDependencies(beanDefinition);
            var bean = beanDefinition.invoke(configInstance, dependencies.toArray());
            var name = beanDefinition.getDeclaredAnnotation(AppComponent.class).name();
            addToContext(name, bean);
        }
    }

    private List<Object> createDependencies(Method beanDefinition) {
        var numberOfArgs = beanDefinition.getParameterCount();
        var argTypes = beanDefinition.getParameterTypes();

        List<Object> dependencies = new ArrayList<>(numberOfArgs);
        for (var type : argTypes) {
            dependencies.add(getAppComponent(type));
        }
        return dependencies;
    }

    private void addToContext(String name, Object bean) {
        if (appComponentsByName.containsKey(name)) {
            throw new IllegalArgumentException("%s already exists in the context".formatted(name));
        } else {
            appComponents.add(bean);
            appComponentsByName.put(name, bean);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(object -> componentClass.isAssignableFrom(object.getClass())).toList();
        if (components.isEmpty()) {
            throw new IllegalArgumentException(String.format("Component with type %s wasn't found",
                    componentClass.getName()));
        }
        if (components.size() > 1) {
            throw new IllegalArgumentException(String.format("There is more than 1 components: %s",
                    components));
        }
        return (C) components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Component with name %s wasn't found",
                    componentName));
        }
        return (C) component;
    }
}
