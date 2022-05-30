package com.example.encyclopedia.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.encyclopedia.R;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.view_model.ArticleViewModel;
import com.example.encyclopedia.view_model.TopicViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewArticleActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView textTextView;
    private AutoCompleteTextView topicView;
    private Button createButton;
    private Button loadPictureButton;
    private int GET_FROM_GALLERY;
    private ImageView imageView;

    ArrayAdapter<String> arrayAdapter;

    private ArticleViewModel articleViewModel;
    private TopicViewModel topicViewModel;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        titleTextView = findViewById(R.id.newArticle_titleInput);
        textTextView = findViewById(R.id.newArticle_textInput);
        topicView = findViewById(R.id.newArticle_tagMenu);
        createButton = findViewById(R.id.newArticle_createButton);
        loadPictureButton = findViewById(R.id.newArticle_loadPictureButton);
        imageView = findViewById(R.id.newArticle_imageView);

        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new ArrayList<>());
        topicViewModel.getAll().observe(this, topics -> {
            arrayAdapter.addAll(topics.stream().map(v -> v.name).collect(Collectors.toList()));
//            arrayAdapter.clear();
//            arrayAdapter.addAll(topics.stream().map(v -> v.name).collect(Collectors.toList()));
//            arrayAdapter.notifyDataSetChanged();
        });
        topicView.setAdapter(arrayAdapter);

        resolveUpdateRequest();

        setupLayout();
    }

    private void resolveUpdateRequest() {
        Intent intent = getIntent();
        if ("update".equals(intent.getStringExtra("method"))) {
            titleTextView.setText(intent.getStringExtra("title"));
            textTextView.setText(intent.getStringExtra("text"));
            topicViewModel.getById(intent.getIntExtra("topic", -1)).observe(this, topic -> topicView.setText(topic.name));
            topicView.setText(intent.getStringExtra("topic"));
            String imagePath = intent.getStringExtra("image");
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            createButton.setText("Update");
        }
        else {
            imageView.setVisibility(View.GONE);
            createButton.setEnabled(false);
        }
    }

    void setupLayout() {

        // Topic menu
        // Create Button
        createButton.setOnClickListener(v -> {
            String title = titleTextView.getText().toString();
            String text = textTextView.getText().toString();
            String topic = topicView.getText().toString();
            int topicId = topicViewModel.getByName(topic).id;
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            if ("update".equals(getIntent().getStringExtra("method"))) {
                Article article = new Article(title, text, topicId,
                        EncyclopediaRoomDatabase.loggedInUser.uid);
                article.uid = getIntent().getIntExtra("uid", -1);
                articleViewModel.update(article);

                Intent intent = new Intent(this, TextContentActivity.class);
                intent.putExtra("articleTitle", article.title);
                startActivity(intent);
            }
            else {
                articleViewModel.insert(
                        new Article(title, text, topicId, EncyclopediaRoomDatabase.loggedInUser.uid));
            }
            createDirectoryAndSaveFile(bitmap, title);
            finish();
        });

        loadPictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GET_FROM_GALLERY);
        });
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/EncyclopediaMedia/articles/");

        if (!direct.exists()) {
            File wallpaperDirectory = new File(getString(R.string.articleImagePath));
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/EncyclopediaMedia/articles/", fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                imageView.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
                createButton.setEnabled(true);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}