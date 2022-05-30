package com.example.encyclopedia.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.entity.Comment;
import com.example.encyclopedia.repository.ArticleRepository;
import com.example.encyclopedia.repository.CommentRepository;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {
    private ArticleRepository repository;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleRepository(application);
    }

    public void insert(Article article) {
        repository.insert(article);
    }

    public void update(Article article) {
        repository.update(article);
    }

    public void delete(Article article) {
        repository.delete(article);
    }

    public LiveData<List<Article>> getByTopicId(int topicId) {
        return repository.getByTopicId(topicId);
    }

    public LiveData<Article> getByTitle(String title) {
        return repository.getByTitle(title);
    }

    public LiveData<List<Article>> getByUserId(int userId){
        return repository.getByUserId(userId);
    }

}
