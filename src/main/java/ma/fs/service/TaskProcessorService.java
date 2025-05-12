package ma.fs.service;

import ma.fs.annotations.Component;


@Component(id = "taskProcessorService")
public class TaskProcessorService {
    private TaskExecutorService taskExecutorService;

    public TaskProcessorService() {}

    // Constructor-based injection
    public TaskProcessorService(TaskExecutorService taskExecutorService) {
        this.taskExecutorService = taskExecutorService;
    }

    public void executeTask() {
        taskExecutorService.execute();
    }
}
