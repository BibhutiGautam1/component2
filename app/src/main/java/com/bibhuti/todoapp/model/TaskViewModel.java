package com.bibhuti.todoapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bibhuti.todoapp.data.DatabaseRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static DatabaseRepository repository;
    public final LiveData<List<Task>> allTasks;
    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new DatabaseRepository(application);
        allTasks = repository.getAllTodoTask();
    }

    public LiveData<List<Task>> getallTasks() {
        return allTasks;
    }

    public static void insert(Task task){
        repository.insert(task);
    }

    public LiveData<List<Task>> getSingleTask(long id){
        return repository.getSingleTask(id);
    }

    public static void update(Task task){
        repository.update(task);
    }

    public static void delete(Task task){
        repository.delete(task);
    }

    public static void deleteAll(){
        repository.deleteAll();
    }

    public static void deleteCompleted(){repository.deleteCompleted();}


}
