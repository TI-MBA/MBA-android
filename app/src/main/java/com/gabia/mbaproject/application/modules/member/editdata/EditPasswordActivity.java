package com.gabia.mbaproject.application.modules.member.editdata;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.ActivityEditPasswordBinding;

public class EditPasswordActivity extends AppCompatActivity {

    ActivityEditPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password);
        binding.setActivity(this);
    }

    public void editPasswordDidPress(View view) {
        if (isFieldEmpty(binding.currentPasswordField) || isFieldEmpty(binding.newPasswordField) || isFieldEmpty(binding.repeatNewPasswordField))  {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        } else if (haveDifferentText(binding.newPasswordField, binding.repeatNewPasswordField)) {
            showAlert(R.string.match_password_message);
        } else {
            Toast.makeText(this, "Senha editada com sucesso", Toast.LENGTH_SHORT).show();
            onBackPressed();
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