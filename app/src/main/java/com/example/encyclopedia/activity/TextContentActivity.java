package com.example.encyclopedia.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encyclopedia.ArticleAdapter;
import com.example.encyclopedia.CommentAdapter;
import com.example.encyclopedia.R;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.entity.Comment;
import com.example.encyclopedia.entity.User;
import com.example.encyclopedia.view_model.ArticleViewModel;
import com.example.encyclopedia.view_model.CommentViewModel;
import com.example.encyclopedia.view_model.TopicViewModel;
import com.example.encyclopedia.view_model.UserViewModel;

import java.util.List;

public class TextContentActivity extends AppCompatActivity {

    private Article article;
    private User user;
    private Comment comment;


    private TextView titleTextView, textTextView, authorTextView;
    private ImageView imageView;
    private RecyclerView commentRecyclerView;
    private TextView commentInputView;
    private View commentInputLayout;
    private Button addCommentButton, updateArticleButton, deleteArticleButton;

    private UserViewModel userViewModel;
    private ArticleViewModel articleViewModel;
    private CommentViewModel commentViewModel;


    CommentAdapter commentAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_content);
        titleTextView = findViewById(R.id.textContent_titleText);
        textTextView = findViewById(R.id.textContent_textText);
        authorTextView = findViewById(R.id.textContent_authorText);
        addCommentButton = findViewById(R.id.textContent_addCommentButton);
        commentInputView = findViewById(R.id.textContent_commentInput);
        commentRecyclerView = findViewById(R.id.textContent_commentList);
        imageView = findViewById(R.id.textContent_imageView);
        updateArticleButton = findViewById(R.id.textContent_updateArticleButton);
        deleteArticleButton = findViewById(R.id.textContent_deleteArticleButton);
        commentInputLayout = findViewById(R.id.textContent_commentInputLayout);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        setupLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupLayout() {
        String title = this.getIntent().getStringExtra("articleTitle");
        articleViewModel.getByTitle(title)
                .observe(this, receivedArticle -> {
                    if (receivedArticle != null) {
                        article = receivedArticle;
                        titleTextView.setText(article.title);
                        String filePath = getString(R.string.articleImagePath) + article.title;
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        imageView.setImageBitmap(bitmap);

                        textTextView.setText(article.text);
                        userViewModel.getById(article.userId).observe(this, receivedUser -> {
                            user = receivedUser;
                            if (EncyclopediaRoomDatabase.loggedInUser != null) {

                                if(EncyclopediaRoomDatabase.loggedInUser.uid == article.userId){
                                    updateArticleButton.setVisibility(View.VISIBLE);
                                    deleteArticleButton.setVisibility(View.VISIBLE);
                                } else if(EncyclopediaRoomDatabase.loggedInUser.role.equals("admin")){
                                    deleteArticleButton.setVisibility(View.VISIBLE);
                                }else {
                                    updateArticleButton.setVisibility(View.INVISIBLE);
                                    deleteArticleButton.setVisibility(View.INVISIBLE);
                                }

                            } else {
                                updateArticleButton.setVisibility(View.INVISIBLE);
                                deleteArticleButton.setVisibility(View.INVISIBLE);
                            }
                            authorTextView.setText("Author: " + user.username);
                        });

                        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        commentAdapter = new CommentAdapter(this, userViewModel);
                        commentRecyclerView.setAdapter(commentAdapter);

                        commentViewModel.getFromArticle(article.uid)
                                .observe(this, comments -> {
                                    commentAdapter.setComments(comments);
                                    if (commentRecyclerView.getLayoutManager().getItemCount() != 0) {
                                        commentRecyclerView.scrollToPosition(
                                                commentRecyclerView.getLayoutManager().getItemCount() - 1);
                                    }
                                });

                        addCommentButton.setOnClickListener(v -> {
                            comment = new Comment(commentInputView.getText().toString(),
                                    EncyclopediaRoomDatabase.loggedInUser.uid, article.uid);
                            commentViewModel.insert(comment);

                        });

                        updateArticleButton.setOnClickListener(v -> {
                            Intent intent = new Intent(this, NewArticleActivity.class);
                            intent.putExtra("method", "update");
                            intent.putExtra("title", article.title);
                            intent.putExtra("text", article.text);
                            intent.putExtra("topic", article.topicId);
                            intent.putExtra("uid", article.uid);
                            intent.putExtra("image", getString(R.string.articleImagePath) + article.title);
                            startActivity(intent);
                        });

                        deleteArticleButton.setOnClickListener(v -> {
                            articleViewModel.delete(article);
                            finish();
                        });
                    }
                });
    }

    @Override
    public void onResume() {
        if (EncyclopediaRoomDatabase.loggedInUser == null) {
            addCommentButton.setVisibility(View.INVISIBLE);
            commentInputLayout.setVisibility(View.INVISIBLE);
        } else {
            addCommentButton.setVisibility(View.VISIBLE);
            commentInputView.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}