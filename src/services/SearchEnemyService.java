package services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


public class SearchEnemyService extends Service<String[]> {

    public SearchEnemyService(ModuleLayer.Controller controller){
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {

            }
        });
    }

    @Override
    protected Task<String[]> createTask() {
        return null;
    }
}
