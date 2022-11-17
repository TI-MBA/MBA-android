package com.gabia.mbaproject.application.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.dashboard.AdminDashboardActivity;
import com.gabia.mbaproject.application.modules.member.HomeActivity;
import com.gabia.mbaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void loginDidPress(View view) {
        if (binding.inputEmail.getText().toString().isEmpty() || binding.inputPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        } else {
            executeLogin();
        }
    }

    private void executeLogin() {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        boolean isAdmin = email.equals("a") && password.equals("a");
        Intent intent;

         if (isAdmin) {
             intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
         } else {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
         }



        startActivity(intent);
    }
}
