package ma.fs.app;

import ma.fs.core.XmlIoCContainer;
import ma.fs.service.TaskHandlerService;
import ma.fs.service.TaskProcessorService;

public class xmlTest {

    public static void main(String[] args) throws Exception {
        // Initialize the XML-based IoC container
        XmlIoCContainer container = new XmlIoCContainer();

        // Load bean definitions from XML configuration
        container.loadConfig("beans.xml");

        // Test setter-based injection
        TaskHandlerService handler = (TaskHandlerService) container.getBean("taskHandlerService");
        handler.executeTask();

        // Test constructor-based injection
        TaskProcessorService processor = (TaskProcessorService) container.getBean("taskProcessorService");
        processor.executeTask();
    }
}