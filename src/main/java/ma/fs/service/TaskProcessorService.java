package ma.fs.service;

import ma.fs.annotations.Component;
import ma.fs.annotations.Inject;


@Component(id = "taskProcessorService")
public class TaskProcessorService {
    private TaskExecutorService taskExecutorService;
    @Inject
    public TaskProcessorService() {}

    // Constructor-based injection to initialize the taskExecutorService dependency
    public TaskProcessorService(TaskExecutorService taskExecutorService) {
        this.taskExecutorService = taskExecutorService;
    }
    // Method to execute the task using taskExecutorService
    public void executeTask() {
        taskExecutorService.execute();
    }
}
