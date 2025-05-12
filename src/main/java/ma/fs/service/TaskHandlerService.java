package ma.fs.service;

import ma.fs.annotations.Component;
import ma.fs.annotations.Inject;

@Component(id = "taskHandlerService")
public class TaskHandlerService {

    @Inject
    private TaskExecutorService taskExecutorService;

    // Setter-based injection
    public void setTaskExecutorService(TaskExecutorService taskExecutorService) {
        this.taskExecutorService = taskExecutorService;
    }

    public void executeTask() {
        taskExecutorService.execute();
    }
}