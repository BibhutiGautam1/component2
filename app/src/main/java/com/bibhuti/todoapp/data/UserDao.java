package com.bibhuti.todoapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bibhuti.todoapp.model.Task;
import com.bibhuti.todoapp.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM user WHERE username=:username and password=:password")
    LiveData<User> getUserDataByUsernameAndPassword(String username, String password);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User... user);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();
}
