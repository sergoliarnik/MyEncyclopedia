package com.example.encyclopedia.db;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.encyclopedia.dao.ArticleDao;
import com.example.encyclopedia.dao.CommentDao;
import com.example.encyclopedia.dao.TopicDao;
import com.example.encyclopedia.dao.UserDao;
import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.entity.Comment;
import com.example.encyclopedia.entity.Topic;
import com.example.encyclopedia.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.Destroyable;


@Database(entities = {User.class, Comment.class, Article.class, Topic.class}, version = 1)
public abstract class EncyclopediaRoomDatabase extends RoomDatabase {

    public static User loggedInUser = null;

    public abstract UserDao userDao();

    public abstract CommentDao commentDao();

    public abstract ArticleDao articleDao();

    public abstract TopicDao topicDao();

    private static volatile EncyclopediaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized EncyclopediaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    EncyclopediaRoomDatabase.class, "encyclopedia_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}