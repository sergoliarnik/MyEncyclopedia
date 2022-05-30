package com.example.encyclopedia.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "article_table", indices = {@Index(value = {"title"},
        unique = true), @Index(value = "userId")}, foreignKeys = {@ForeignKey(
        entity = User.class,
        parentColumns = "uid",
        childColumns = "userId",
        onDelete = CASCADE),
        @ForeignKey(
                entity = Topic.class,
                parentColumns = "id",
                childColumns = "topicId",
                onDelete = CASCADE)
})
public class Article {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    public String title;
    public String text;
    public int topicId;
    public int userId;

    public Article(String title, String text, int topicId, int userId){
        this.title = title;
        this.text = text;
        this.topicId = topicId;
        this.userId = userId;
    }
}
