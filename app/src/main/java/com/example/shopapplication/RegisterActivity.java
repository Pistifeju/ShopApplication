package com.example.shopapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key != 99) {
            finish();
        }

        usernameEditText = findViewById(R.id.registrationUsernameEditText);
        emailEditText = findViewById(R.id.registrationEmailEditText);
        passwordEditText = findViewById(R.id.registrationPasswordEditText);
        passwordAgainEditText = findViewById(R.id.registrationPasswordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String username = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        usernameEditText.setText(username);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);
    }

    public void register(View view) {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();
    }

    public void login(View view) {
        finish();
    }
}