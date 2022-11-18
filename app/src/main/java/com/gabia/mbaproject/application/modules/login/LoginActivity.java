package com.gabia.mbaproject.application.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.dashboard.AdminDashboardActivity;
import com.gabia.mbaproject.application.modules.member.HomeActivity;
import com.gabia.mbaproject.databinding.ActivityLoginBinding;
import com.gabia.mbaproject.infrastructure.api.AuthApiDataSource;
import com.gabia.mbaproject.infrastructure.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.infrastructure.remotedatasource.AuthRemoteDataSource;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.AuthRequest;
import com.gabia.mbaproject.model.User;

import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        binding.setIsLoading(false);
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

        AuthRemoteDataSource remoteDataSource = new AuthRemoteDataSource(ApiDataSourceProvider.Companion.getAuthApiDataSource());
        binding.setIsLoading(true);
        remoteDataSource.login(new AuthRequest(email, password), new BaseCallBack<User>() {
             @Override
             public void onSuccess(User result) {
                 boolean isAdmin = email.equals("a") && password.equals("a");
                 Intent intent;

                 if (isAdmin) {
                     intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                 } else {
                     intent = new Intent(getApplicationContext(), HomeActivity.class);
                 }
                 startActivity(intent);
                 binding.setIsLoading(false);
             }

             @Override
             public void onError(int code) {
                 binding.setIsLoading(false);
                 runOnUiThread(() -> {
                     showError(code);
                 });
             }
         });
    }

    private void showError(int code) {
        String message = "falha ao entrar - Codigo" + code;
        if (code == 401) {
            message = "Login ou senha incorretos";
        }
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
