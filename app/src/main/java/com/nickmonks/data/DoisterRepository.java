package com.nickmonks.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nickmonks.Util.TaskRoomDatabase;
import com.nickmonks.model.Task;

import java.util.List;

// Repository: single source of truth for our ViewModel
// API used from the VM
public class DoisterRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public DoisterRepository(Application application) {
        TaskRoomDatabase database= TaskRoomDatabase.getDatabase(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getTasks();
    }

    // GET ALL
    public LiveData<List<Task>> getAllTasks(){ return allTasks; }

    // INSERT
    public void insert(Task task) {
        TaskRoomDatabase.datbaseWriterExecutor.execute( () -> {
        taskDao.insertTask(task);
        });
    }

    //
    public LiveData<Task> get(long id) { return taskDao.get(id);}

    public void update(Task task) {
        TaskRoomDatabase.datbaseWriterExecutor.execute(() -> {
            taskDao.update(task);
        });
    }

    public void delete(Task task) {
        TaskRoomDatabase.datbaseWriterExecutor.execute(() -> {
            taskDao.delete(task);
        });
    }
}
