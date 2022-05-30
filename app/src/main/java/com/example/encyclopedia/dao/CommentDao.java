package com.example.encyclopedia.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.entity.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Comment... comments);

    @Query("SELECT * FROM comment_table WHERE articleId IS :id")
    LiveData<List<Comment>> getFromArticle(int id);

    @Update
    void update(Comment... comments);

    @Delete
    void delete(Comment... comments);


}


