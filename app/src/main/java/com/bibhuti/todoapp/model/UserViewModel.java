package com.bibhuti.todoapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bibhuti.todoapp.data.DatabaseRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    public static DatabaseRepository repository;
    public final LiveData<List<User>> allUsers;
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new DatabaseRepository(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<User>> getallUsers() {
        return allUsers;
    }

    public void insertUser(User user){
        repository.insertUser(user);
    }

    public LiveData<User> getSingleUser(String username, String password){
        return repository.getUserByUsernameAndPassword(username, password);
    }
}
