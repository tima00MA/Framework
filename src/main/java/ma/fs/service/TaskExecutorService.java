package ma.fs.service;

import ma.fs.annotations.Component;

@Component(id = "taskExecutorService")
public class TaskExecutorService {
    public void execute() {
        System.out.println("TaskExecutorService is running.");
    }
}
