package com.example.encyclopedia.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.encyclopedia.entity.Article;

import java.sql.Blob;
import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Article... articles);

    @Query("SELECT * FROM article_table WHERE topicId is :topicId")
    LiveData<List<Article>> getByTopicId(int topicId);

    @Query("SELECT * FROM article_table WHERE title is :title")
    LiveData<Article> getByTitle(String title);

    @Query("SELECT * FROM article_table WHERE userId is :userId")
    LiveData<List<Article>> getByUserId(int userId);

    @Delete
    void delete(Article... articles);

    @Update
    void update(Article... articles);



}

