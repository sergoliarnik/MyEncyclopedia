package com.example.encyclopedia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.encyclopedia.R;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.User;
import com.example.encyclopedia.view_model.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText repasswordInput;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameInput = findViewById(R.id.signUp_usernameInput);
        passwordInput = findViewById(R.id.signUp_passwordInput);
        repasswordInput = findViewById(R.id.signUp_repasswordInput);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setupLayout();

    }

    private void setupLayout() {
        Button signUpButton = findViewById(R.id.signUp_signUpButton);
        signUpButton.setOnClickListener(v -> {
            if (passwordInput.getText().toString().equals(repasswordInput.getText().toString())) {
                saveNewUser(usernameInput.getText().toString(), passwordInput.getText().toString(), true, "default");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button logInButton = findViewById(R.id.signUp_logInButton);
        logInButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        });
    }

    private void saveNewUser(String username, String password, boolean isLoggedIn, String role) {
        User user = new User(username, password, isLoggedIn, role);
        userViewModel.insert(user);
    }
}