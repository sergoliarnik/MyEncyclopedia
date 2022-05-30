package com.example.encyclopedia.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.encyclopedia.entity.Comment;
import com.example.encyclopedia.entity.User;
import com.example.encyclopedia.repository.CommentRepository;
import com.example.encyclopedia.repository.UserRepository;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    private CommentRepository repository;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        repository = new CommentRepository(application);
    }

    public void insert(Comment comment) {
        repository.insert(comment);
    }

    public void update(Comment comment) {
        repository.update(comment);
    }


    public void delete(Comment comment) {
        repository.delete(comment);
    }

    public LiveData<List<Comment>> getFromArticle(int id) {
        return repository.getFromArticle(id);
    }
}
