package com.example.encyclopedia.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.encyclopedia.entity.User;

import java.sql.Blob;
import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User... users);

    @Delete
    void delete(User... users);

    @Update
    void update(User... users);

    @Query("UPDATE user_table SET isLoggedIn = :status WHERE uid is :id ")
    void updateLogInStatus(int id, int status);

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user_table WHERE isLoggedIn != 0")
    LiveData<User> getLoggedIn();

    @Query("SELECT * FROM user_table WHERE username is :name AND password is :pass")
    LiveData<User> getByNameAndPassword(String name, String pass);

    @Query("SELECT * FROM user_table WHERE uid is :id")
    LiveData<User> getById(int id);


}
