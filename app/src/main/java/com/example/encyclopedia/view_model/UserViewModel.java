package com.example.encyclopedia.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.entity.User;
import com.example.encyclopedia.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void updateLogInStatus(int id, int status) {
        repository.updateLogInStatus(id, status);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public LiveData<User> getById(int id) {
        return repository.getById(id);
    }

    public LiveData<User> getLoggedIn() {
        return repository.getLoggedIn();
    }

    public LiveData<User> getByNameAndPassword(String name, String pass) {
        return repository.getByNameAndPassword(name, pass);
    }

    public LiveData<List<User>> getAll(){
        return repository.getAll();
    }
}
