package com.bibhuti.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.bibhuti.todoapp.adapter.RecyclerViewAdapter;
import com.bibhuti.todoapp.model.SharedUserViewModel;
import com.bibhuti.todoapp.model.SharedViewModel;
import com.bibhuti.todoapp.model.User;
import com.bibhuti.todoapp.model.UserViewModel;
import com.bibhuti.todoapp.util.TaskRoomDatabase;
import com.bibhuti.todoapp.util.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText userName, password;

    UserViewModel viewModel;

    private SharedUserViewModel sharedUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.text_username_id);
        password = findViewById(R.id.text_password_id);
        btnLogin = findViewById(R.id.newLoginButton);
        btnRegister = findViewById(R.id.login_activity_btn_register);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(LoginActivity.this.getApplication()).create(UserViewModel.class);
        sharedUserViewModel = new ViewModelProvider(this).get(SharedUserViewModel.class);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("todopref", 0);
        boolean authentication = preferences.getBoolean("authentication", false);
        if (authentication) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        btnRegister.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(v);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        btnLogin.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(v);
            if (!userName.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
//                User user = new User(userName.getText().toString(), password.getText().toString());
                viewModel.getSingleUser(userName.getText().toString(), password.getText().toString()).observe(this, user -> {
                    if (user != null) {
                        sharedUserViewModel.selectItem(user);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("authentication", true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(v, "No Valid User Found", 2000).show();
                    }
                });
            } else {
                if (userName.getText().toString().equals("")) {
                    userName.requestFocus();
                    userName.setError("Name is required");
                } else if (password.getText().toString().equals("")) {
                    password.requestFocus();
                    password.setError("Password is required");
                } else {
                    Snackbar.make(v, "Invalid User Credentials", 2000).show();
                }
            }
        });
    }
}