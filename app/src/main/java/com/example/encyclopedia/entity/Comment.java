package com.example.encyclopedia.entity;


import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment_table", indices = {@Index(value = {"userId", "articleId"})},
        foreignKeys = {@ForeignKey(
                entity = User.class,
                parentColumns = "uid",
                childColumns = "userId",
                onDelete = CASCADE),
                @ForeignKey(
                        entity = Article.class,
                        parentColumns = "uid",
                        childColumns = "articleId",
                        onDelete = CASCADE)})
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(index = true)
    public int userId;
    @ColumnInfo(index = true)
    public int articleId;
    public String text;

    public Comment(String text, int userId, int articleId) {
        this.text = text;
        this.userId = userId;
        this.articleId = articleId;
    }
}
