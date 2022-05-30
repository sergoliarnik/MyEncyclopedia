package com.example.encyclopedia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.encyclopedia.R;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.User;
import com.example.encyclopedia.view_model.UserViewModel;

public class LogInActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button logInButton;
    private Button signUpButton;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // view initialization
        usernameInput = findViewById(R.id.logIn_usernameInput);
        passwordInput = findViewById(R.id.logIn_passwordInput);
        logInButton = findViewById(R.id.logInButton);
        signUpButton = findViewById(R.id.signUpButton);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setupLayout();
    }

    private void setupLayout() {
        logInButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            userViewModel.getByNameAndPassword(username, password).observe(this, user -> {
                if (user != null) {
                    user.isLoggedIn = true;
                    userViewModel.update(user);
                    finish();
                }
            });
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

}