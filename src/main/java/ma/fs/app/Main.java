package ma.fs.app;

import ma.fs.core.ApplicationContext;
import ma.fs.service.TaskExecutorService;
import ma.fs.service.TaskHandlerService;
import ma.fs.service.TaskProcessorService;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Initialize the IoC container
        ApplicationContext container = new ApplicationContext();

        // Register component classes
        container.loadComponents(List.of(
                TaskExecutorService.class,
                TaskHandlerService.class,
                TaskProcessorService.class
        ));

        // Retrieve TaskHandlerService and invoke the method
        TaskHandlerService taskHandler = (TaskHandlerService) container.getBean("taskHandlerService");
        taskHandler.executeTask();
    }
}