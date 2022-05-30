package com.example.encyclopedia.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.encyclopedia.entity.Topic;

import java.util.List;

@Dao
public interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Topic... topics);

    @Query("SELECT * FROM topic_table")
    LiveData<List<Topic>> getAll();

    @Query("SELECT * FROM topic_table WHERE name is :name")
    Topic getByName(String name);

    @Query("SELECT * FROM topic_table WHERE id is :id")
    LiveData<Topic> getById(int id);

    @Update
    void update(Topic... topics);

    @Delete
    void delete(Topic... topics);


}
