package com.example.shopapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int SECRECT_KEY = 99;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    EditText usernameEditText;
    EditText passwordEditText;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    public void login(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRECT_KEY);
        startActivity(intent);
    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", usernameEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.apply();

        super.onPause();
    }
}