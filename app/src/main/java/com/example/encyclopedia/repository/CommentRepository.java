package com.example.encyclopedia.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.encyclopedia.dao.CommentDao;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Comment;

import java.util.List;


public class CommentRepository {

    private CommentDao commentDao;

    public CommentRepository(Application application) {
        EncyclopediaRoomDatabase db = EncyclopediaRoomDatabase.getDatabase(application);
        commentDao = db.commentDao();
    }

    public void insert(Comment comment) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.insert(comment);
        });
    }

    public void update(Comment comment) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.update(comment);
        });
    }


    public void delete(Comment comment) {
        EncyclopediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.delete(comment);
        });
    }

    public LiveData<List<Comment>> getFromArticle(int id) {
        return commentDao.getFromArticle(id);
    }
}
