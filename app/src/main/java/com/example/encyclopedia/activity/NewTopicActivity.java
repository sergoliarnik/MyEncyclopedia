package com.example.encyclopedia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.encyclopedia.R;
import com.example.encyclopedia.entity.Topic;
import com.example.encyclopedia.view_model.TopicViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewTopicActivity extends AppCompatActivity {
    TextView textView;
    Button createButton;
    Button loadImageButton;
    ImageView imageView;
    private int GET_FROM_GALLERY;

    TopicViewModel topicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        textView = findViewById(R.id.newTitle_topicNameInput);
        createButton = findViewById(R.id.newTopic_createButton);
        loadImageButton = findViewById(R.id.newTopic_loadImageButton);
        imageView = findViewById(R.id.newTopic_imageView);

        imageView.setVisibility(View.GONE);
        createButton.setEnabled(false);

        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        createButton.setOnClickListener(v -> {
            topicViewModel.insert(new Topic(textView.getText().toString()));
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            createDirectoryAndSaveFile(bitmap, textView.getText().toString());
            finish();
        });

        loadImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GET_FROM_GALLERY);
        });


    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/EncyclopediaMedia");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/EncyclopediaMedia/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/EncyclopediaMedia/", fileName);
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