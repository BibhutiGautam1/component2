package com.bibhuti.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bibhuti.todoapp.model.TaskViewModel;
import com.bibhuti.todoapp.model.User;
import com.bibhuti.todoapp.model.UserViewModel;
import com.bibhuti.todoapp.util.Utils;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bibhuti.todoapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    EditText userName, password;

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userViewModel = new ViewModelProvider.AndroidViewModelFactory(RegisterActivity.this.getApplication()).create(UserViewModel.class);
        userName = findViewById(R.id.text_username_id);
        password = findViewById(R.id.text_password_id);
        btnLogin = findViewById(R.id.register_activity_btn_login);
        btnRegister = findViewById(R.id.register_activity_btn_register);
        btnLogin.setOnClickListener(v ->
        {
            Utils.hideSoftKeyboard(v);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        btnRegister.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(v);
            if(!userName.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
                User user = new User(userName.getText().toString(), password.getText().toString());
                userViewModel.insertUser(user);
                Snackbar snackbar = Snackbar.make(v, "Added new User Credentials", 2000);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        // The Snackbar has been dismissed, start the new Activity here
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                if(userName.getText().toString().equals("")){
                    userName.requestFocus();
                    userName.setError("Name is required");
                }
                else if(password.getText().toString().equals("")){
                    password.requestFocus();
                    password.setError("Password is required");
                }
                else{
                    Snackbar.make(v, "Invalid User Credentials",2000).show();
                }
            }
        });
    }

}