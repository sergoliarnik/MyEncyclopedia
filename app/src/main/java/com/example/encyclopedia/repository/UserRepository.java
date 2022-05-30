package com.example.encyclopedia.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.encyclopedia.dao.UserDao;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {
        EncyclopediaRoomDatabase db = EncyclopediaRoomDatabase.getDatabase(application);
        userDao = db.userDao();
    }


    public void insert(User user) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }

    public void update(User user) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
        });
    }

    public void updateLogInStatus(int id, int status) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateLogInStatus(id, status);
        });
    }

    public void delete(User user) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.delete(user);
        });
    }

    public LiveData<User> getById(int id) {
        return userDao.getById(id);
    }

    public LiveData<User> getLoggedIn() {
        return userDao.getLoggedIn();
    }

    public LiveData<User> getByNameAndPassword(String name, String pass) {
        return userDao.getByNameAndPassword(name, pass);
    }

    public LiveData<List<User>> getAll(){
        return userDao.getAll();
    }
}
