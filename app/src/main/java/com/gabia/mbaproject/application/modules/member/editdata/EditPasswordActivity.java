package com.gabia.mbaproject.application.modules.member.editdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.App;
import com.gabia.mbaproject.application.modules.login.LoginActivity;
import com.gabia.mbaproject.databinding.ActivityEditPasswordBinding;
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.infrastructure.remote.remotedatasource.AuthRemoteDataSource;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.AuthRequest;
import com.gabia.mbaproject.model.User;

public class EditPasswordActivity extends AppCompatActivity {

    ActivityEditPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password);
        binding.setActivity(this);
        binding.setIsLoading(false);
    }

    public void editPasswordDidPress(View view) {
        if (isFieldEmpty(binding.currentPasswordField) || isFieldEmpty(binding.newPasswordField) || isFieldEmpty(binding.repeatNewPasswordField))  {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        } else if (haveDifferentText(binding.newPasswordField, binding.repeatNewPasswordField)) {
            showAlert(R.string.match_password_message);
        } else {
            binding.setIsLoading(true);
            requestUpdatePassword();
        }
    }

    private void requestUpdatePassword() {
        User currentUser = App.getCurrentUser(this);
        if (currentUser != null) {
            AuthRemoteDataSource remoteDataSource = new AuthRemoteDataSource(ApiDataSourceProvider.Companion.getAuthApiDataSource());
            AuthRequest authRequest = new AuthRequest(currentUser.getEmail(), binding.newPasswordField.getText().toString());
            remoteDataSource.changePassword(authRequest, new BaseCallBack<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditPasswordActivity.this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                        binding.setIsLoading(false);
                        finish();
                    });
                }

                @Override
                public void onError(int code) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditPasswordActivity.this, "Falha ao editar usuário - " + code, Toast.LENGTH_SHORT).show();
                        binding.setIsLoading(false);
                    });
                }
            });
        } else {
            Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
            App.logout(this);
        }
    }

    private boolean haveDifferentText(EditText firstEditText, EditText secondEditText) {
        return !firstEditText.getText().toString().equals(secondEditText.getText().toString());
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private void showAlert(int message) {
        new AlertDialog.Builder(this)
                .setTitle("Editar senha")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> {})
                .show();
    }
}