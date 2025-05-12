package ma.fs.core;


import ma.fs.annotations.Component;
import ma.fs.annotations.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

// A simple IoC container that supports annotation-based dependency injection
public class ApplicationContext {
    private final Map<String, Object> beans = new HashMap<>();

    // Loads all @Component classes and performs injection
    public void loadComponents(List<Class<?>> classes) throws Exception {
        // Create instances of all classes marked with @Component
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Component component = clazz.getAnnotation(Component.class);
                String id = component.id();
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beans.put(id, instance);
            }
        }

        // Inject dependencies into those instances
        for (Object bean : beans.values()) {
            injectDependencies(bean);
        }
    }

    // Handles constructor, setter, and field injection
    private void injectDependencies(Object bean) throws Exception {
        Class<?> clazz = bean.getClass();

        // Field injection (@Inject on attributes)
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object dependency = findBeanByType(field.getType());
                field.setAccessible(true);
                field.set(bean, dependency);
            }
        }

        // Setter injection (@Inject on methods)
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Inject.class)) {
                Object dependency = findBeanByType(method.getParameterTypes()[0]);
                method.setAccessible(true);
                method.invoke(bean, dependency);
            }
        }

        // Constructor injection (@Inject on constructors)
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = findBeanByType(paramTypes[i]);
                }
                Object newInstance = constructor.newInstance(params);
                beans.put(getComponentId(clazz), newInstance);
            }
        }
    }

    // Finds a bean by its type
    private Object findBeanByType(Class<?> type) {
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }
        throw new RuntimeException("No dependency found for type: " + type.getName());
    }

    // Gets the component ID from its annotation
    private String getComponentId(Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        return component.id();
    }

    // Returns a bean by its ID
    public Object getBean(String id) {
        return beans.get(id);
    }
}
