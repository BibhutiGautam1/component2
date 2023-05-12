package com.bibhuti.todoapp.util;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bibhuti.todoapp.data.TaskDao;
import com.bibhuti.todoapp.data.UserDao;
import com.bibhuti.todoapp.model.Task;
import com.bibhuti.todoapp.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, User.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public  abstract class TaskRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    public abstract UserDao userDao();

    public  static TaskRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static TaskRoomDatabase getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "newTodo.db")
                            //.allowMainThreadQueries()
                            .addCallback(CALLBACK)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback CALLBACK = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
                TaskDao taskDao = INSTANCE.taskDao();
                taskDao.deleteAll();
            });

        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
