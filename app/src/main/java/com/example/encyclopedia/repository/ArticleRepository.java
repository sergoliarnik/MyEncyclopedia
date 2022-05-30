package com.example.encyclopedia.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.encyclopedia.dao.ArticleDao;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Article;

import java.util.List;

public class ArticleRepository {
    private ArticleDao articleDao;

    public ArticleRepository(Application application) {
        EncyclopediaRoomDatabase db = EncyclopediaRoomDatabase.getDatabase(application);
        articleDao = db.articleDao();
    }

    public void insert(Article article) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            articleDao.insert(article);
        });
    }

    public void update(Article article) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            articleDao.update(article);
        });
    }


    public void delete(Article article) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            articleDao.delete(article);
        });
    }

    public LiveData<List<Article>> getByTopicId(int topicId) {
        return articleDao.getByTopicId(topicId);
    }

    public LiveData<Article> getByTitle(String title) {
        return articleDao.getByTitle(title);
    }

    public LiveData<List<Article>> getByUserId(int userId){
        return articleDao.getByUserId(userId);
    }
}
