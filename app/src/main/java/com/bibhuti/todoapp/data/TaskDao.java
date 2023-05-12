package com.bibhuti.todoapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bibhuti.todoapp.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("DELETE FROM task_table where is_completed = 1")
    void deleteCompleted();

    @Delete
    void deleteById(Task task);

    @Query("SELECT * FROM task_table WHERE taskID=:id")
    LiveData<List<Task>> getSingleTask(long id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Task... task);

    @Query("SELECT * FROM task_table ORDER BY date_created, priority desc")
    LiveData<List<Task>> getTasks();

}
