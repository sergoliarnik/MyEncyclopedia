package com.example.encyclopedia.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.encyclopedia.entity.Topic;
import com.example.encyclopedia.repository.TopicRepository;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {
    private TopicRepository repository;

    public TopicViewModel(@NonNull Application application) {
        super(application);
        repository = new TopicRepository(application);
    }

    public void insert(Topic topic) {
        repository.insert(topic);
    }

    public void update(Topic topic) {
        repository.update(topic);
    }

    public void delete(Topic topic) {
        repository.delete(topic);
    }

    public LiveData<List<Topic>> getAll() {
        return repository.getAll();
    }

    public Topic getByName(String name) {
        return repository.getByName(name);
    }

    public LiveData<Topic> getById(int id){
        return repository.getById(id);
    }

}
