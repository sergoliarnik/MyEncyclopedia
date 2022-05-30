package com.example.encyclopedia.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import com.example.encyclopedia.dao.UserDao;

import java.sql.Blob;
import java.util.List;

@Entity(tableName = "user_table", indices = {@Index(value = {"username"},
        unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    public String username;
    public String password;
    public boolean isLoggedIn;
    public String role;

    public User(String username, String password, boolean isLoggedIn, String role) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.role = role;
    }

}



