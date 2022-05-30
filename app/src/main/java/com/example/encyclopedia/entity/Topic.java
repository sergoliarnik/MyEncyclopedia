package com.example.encyclopedia.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "topic_table", indices = {@Index(value = {"name"},
        unique = true)})
public class Topic {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;

    public Topic(String name) {
        this.name = name;
    }
}