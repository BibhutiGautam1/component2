package com.bibhuti.todoapp.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bibhuti.todoapp.model.Task;
import com.bibhuti.todoapp.model.User;
import com.bibhuti.todoapp.util.TaskRoomDatabase;

import java.util.List;

public class DatabaseRepository {
    private TaskDao taskDao;
    private UserDao userDao;
    private LiveData<List<Task>> allTodoTask;

    private LiveData<List<User>> allUsers;

    public DatabaseRepository(Application application){
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        userDao = database.userDao();
        allTodoTask = taskDao.getTasks();
        allUsers = userDao.getAllUsers();
    }



    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void insertUser(User user){
        new insertUserAysncTask(userDao).execute(user);
    }

    public LiveData<User> getUserByUsernameAndPassword(String username, String password){

        LiveData<User> user = userDao.getUserDataByUsernameAndPassword(username, password);
        User user1 = user.getValue();
        return  user;
    }

    public LiveData<List<User>> getAllUsers(){
        return userDao.getAllUsers();
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public LiveData<List<Task>> getAllTodoTask() {
        return allTodoTask;
    }

    public LiveData<List<Task>> getSingleTask(long id) {
        return taskDao.getSingleTask(id);
    }

    public void setAllTodoTask(LiveData<List<Task>> allTodoTask) {
        this.allTodoTask = allTodoTask;
    }

    public void insert(Task task){
        new insertTodoAysncTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new deleteTodoAysncTask(taskDao).execute(task);
    }

    public void update(Task task){
        new updateTodoAysncTask(taskDao).execute(task);
    }

    public void deleteAll(){
        new deleteAllTodoAysncTask(taskDao).execute();
    }

    public void deleteCompleted(){new deleteCompletedTodoAsyncTask(taskDao).execute();}

    private static class insertTodoAysncTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao taskDao;
        private insertTodoAysncTask(TaskDao taskDao){
            this.taskDao=taskDao;
        }

        @Override
        protected Void doInBackground(Task... task) {
            taskDao.insert(task[0]);
            return null;
        }
    }

    private static class deleteCompletedTodoAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private deleteCompletedTodoAsyncTask(TaskDao taskDao){
            this.taskDao=taskDao;
        }

        @Override
        protected Void doInBackground(Task... task) {
            taskDao.deleteCompleted();
            return null;
        }
    }

    private static class deleteAllTodoAysncTask extends AsyncTask<Task, Void, Void>{
        private final TaskDao taskDao;
        private deleteAllTodoAysncTask(TaskDao taskDao){
            this.taskDao=taskDao;
        }

        @Override
        protected Void doInBackground(Task... task) {
            taskDao.deleteAll();
            return null;
        }
    }

    private static class deleteTodoAysncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;
        private deleteTodoAysncTask(TaskDao taskDao){
            this.taskDao=taskDao;
        }

        @Override
        protected Void doInBackground(Task... task) {
            taskDao.deleteById(task[0]);
            return null;
        }
    }

    private static class updateTodoAysncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;
        private updateTodoAysncTask(TaskDao taskDao){
            this.taskDao=taskDao;
        }

        @Override
        protected Void doInBackground(Task... task) {
            taskDao.update(task[0]);
            return null;
        }
    }

    // start user actions
    private static class insertUserAysncTask extends AsyncTask<User, Void, Void> {
        private final UserDao userDao;
        private insertUserAysncTask(UserDao userDao){
            this.userDao=userDao;
        }

        @Override
        protected Void doInBackground(User... user) {
            userDao.insert(user[0]);
            return null;
        }
    }
}
