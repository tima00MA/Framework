package ma.fs.core;

import ma.fs.xml.Bean;
import ma.fs.xml.Beans;
import ma.fs.xml.Property;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles dependency injection using XML config.
 */
public class XmlIoCContainer {

    // Stores bean instances by their ID
    private final Map<String, Object> beans = new HashMap<>();

    /**
     * Loads beans from an XML file.
     */
    public void loadConfig(String resourcePath) throws Exception {
        // Load XML file from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) throw new RuntimeException("XML file not found: " + resourcePath);

        // Parse XML using JAXB
        JAXBContext context = JAXBContext.newInstance(Beans.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Beans config = (Beans) unmarshaller.unmarshal(inputStream);

        // Step 1: Create instances without dependencies
        for (Bean bean : config.beans) {
            Class<?> clazz = Class.forName(bean.className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beans.put(bean.id, instance);
        }

        // Step 2: Inject dependencies
        for (Bean bean : config.beans) {
            Object instance = beans.get(bean.id);
            Class<?> clazz = instance.getClass();

            if (bean.properties != null) {
                for (Property property : bean.properties) {
                    Object dependency = beans.get(property.ref);

                    // a. Inject by field
                    try {
                        Field field = clazz.getDeclaredField(property.name);
                        field.setAccessible(true);
                        field.set(instance, dependency);
                        continue;
                    } catch (NoSuchFieldException ignored) {}

                    // b. Inject by setter
                    String setterName = "set" + capitalize(property.name);
                    for (Method method : clazz.getMethods()) {
                        if (method.getName().equalsIgnoreCase(setterName) && method.getParameterCount() == 1) {
                            method.invoke(instance, dependency);
                            break;
                        }
                    }
                }
            }

            // c. Inject by constructor if parameters match
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (paramTypes.length > 0) {
                    Object[] args = new Object[paramTypes.length];
                    boolean match = true;

                    for (int i = 0; i < paramTypes.length; i++) {
                        boolean found = false;
                        for (Property property : bean.properties) {
                            Object dep = beans.get(property.ref);
                            if (paramTypes[i].isAssignableFrom(dep.getClass())) {
                                args[i] = dep;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        constructor.setAccessible(true);
                        instance = constructor.newInstance(args);
                        beans.put(bean.id, instance); // Replace old instance
                        break;
                    }
                }
            }
        }
    }

    // Get bean by ID
    public Object getBean(String id) {
        return beans.get(id);
    }

    // Capitalize property name for setter method
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
