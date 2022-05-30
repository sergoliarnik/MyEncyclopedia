package com.example.encyclopedia.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.encyclopedia.dao.TopicDao;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Topic;

import java.util.List;

public class TopicRepository {
    private TopicDao topicDao;

    public TopicRepository(Application application) {
        EncyclopediaRoomDatabase db = EncyclopediaRoomDatabase.getDatabase(application);
        topicDao = db.topicDao();
    }

    public void insert(Topic topic) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            topicDao.insert(topic);
        });
    }

    public void update(Topic topic) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            topicDao.update(topic);
        });
    }


    public void delete(Topic topic) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            topicDao.delete(topic);
        });
    }

    public LiveData<List<Topic>> getAll() {
        return topicDao.getAll();
    }

    public Topic getByName(String name) {
        return topicDao.getByName(name);
    }

    public LiveData<Topic> getById(int id){
        return topicDao.getById(id);
    }
}
