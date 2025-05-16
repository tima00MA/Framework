# Mini Java IoC Framework (Annotation + XML)

This project is a simplified implementation of an **IoC (Inversion of Control) container** in Java that supports both:

* **Annotation-based dependency injection** (@Component, @Inject)
* **XML-based configuration** (via JAXB and `beans.xml`)

---

## ⚙️ Project Structure

```
main/
└── java/
    ├── ma/fs/                      # Root package
    │   ├── annotations/         # Custom annotations
    │   │   ├── Component.java
    │   │   ├── Inject.java
    │   │   └── Configuration.java (optional)
    │   ├── core/               # Framework core logic
    │   │   ├── AnnotationIoCContainer.java
    │   │   └── XmlIoCContainer.java
    │   ├── xml/                # JAXB data classes for XML
    │   │   ├── Bean.java
    │   │   ├── Beans.java
    │   │   └── Property.java
    │   ├── util/               # Utility classes
    │   │   └── ReflectionUtils.java (optional)
    │   ├── service/            # Example business logic services
    │   │   ├── TaskExecutorService.java
    │   │   ├── TaskHandlerService.java
    │   │   └── TaskProcessorService.java
    │   ├── app/
    │   │   ├── AppAnnotation.java   # Main class for annotation demo
    │   │   └── xmlTest.java         # Main class for XML demo
    └── resources/
        └── beans.xml                # Configuration file for XML injection
```

---

## 🔎 1. Annotation-Based Injection

### Annotations

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String id();
}

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {}
```

### Sample Service

```java
@Component(id = "taskExecutorService")
public class TaskExecutorService {
    public void execute() {
        System.out.println("TaskExecutorService is running.");
    }
}

@Component(id = "taskHandlerService")
public class TaskHandlerService {
    @Inject
    private TaskExecutorService taskExecutorService;

    public void executeTask() {
        taskExecutorService.execute();
    }
}
```

### App Runner

```java
public class AppAnnotation {
    public static void main(String[] args) throws Exception {
        AnnotationIoCContainer container = new AnnotationIoCContainer();
        container.loadComponents(List.of(TaskExecutorService.class, TaskHandlerService.class));

        TaskHandlerService handler = (TaskHandlerService) container.getBean("taskHandlerService");
        handler.executeTask();
    }
}
```

---

## 📂 2. XML-Based Injection

### XML File (resources/beans.xml)

```xml
<beans>
    <bean id="taskExecutorService" class="ma.fs.service.TaskExecutorService"/>

    <bean id="taskProcessorService" class="ma.fs.service.TaskProcessorService">
        <property name="taskExecutorService" ref="taskExecutorService"/>
    </bean>
</beans>
```

### JAXB Beans

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
    @XmlAttribute public String id;
    @XmlAttribute(name = "class") public String className;
    @XmlElement(name = "property") public Property[] properties;
}
```

### XML Container

```java
public class XmlIoCContainer {
    private final Map<String, Object> beans = new HashMap<>();

    public void loadConfig(String resourcePath) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) throw new RuntimeException("XML file not found: " + resourcePath);

        JAXBContext context = JAXBContext.newInstance(Beans.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Beans config = (Beans) unmarshaller.unmarshal(inputStream);

        // Create instances
        for (Bean bean : config.beans) {
            Class<?> clazz = Class.forName(bean.className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beans.put(bean.id, instance);
        }

        // Inject dependencies
        for (Bean bean : config.beans) {
            Object instance = beans.get(bean.id);
            for (Property property : bean.properties) {
                Object dependency = beans.get(property.ref);
                Field field = instance.getClass().getDeclaredField(property.name);
                field.setAccessible(true);
                field.set(instance, dependency);
            }
        }
    }

    public Object getBean(String id) {
        return beans.get(id);
    }
}
```

### App Runner

```java
public class xmlTest {
    public static void main(String[] args) throws Exception {
        XmlIoCContainer container = new XmlIoCContainer();
        container.loadConfig("beans.xml");

        TaskProcessorService processor = (TaskProcessorService) container.getBean("taskProcessorService");
        processor.executeTask();
    }
}
```

---

## 🚀 Sample Output

```
TaskExecutorService is running.
```

---

## 🔗 Credits

Built as a mini educational project to learn how IoC works internally in frameworks like Spring.

---

Feel free to extend this with scopes, lifecycle management, or autowiring!
