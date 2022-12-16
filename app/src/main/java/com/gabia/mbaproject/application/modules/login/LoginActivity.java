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
import com.gabia.mbaproject.infrastructure.local.UserDefaults;
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.infrastructure.remote.remotedatasource.AuthRemoteDataSource;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.AuthRequest;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.MemberRequest;
import com.gabia.mbaproject.model.enums.Instrument;
import com.gabia.mbaproject.model.enums.Situation;
import com.gabia.mbaproject.model.enums.UserLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public void createUsers() {
        List<MemberRequest> memberRequestList = new ArrayList<>();

        try {
            InputStream is = getAssets().open("inexistent.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line = br.readLine();

            while (line != null) {
                String[] user = line.split(",");
                String email = user[2];
                int adminLevel = 0;
                String name = user[1];
                String instrument = "";
                switch (user[3]) {
                    case "Agbe":
                        instrument = Instrument.AGBE.getValue();
                        break;
                    case "Agogo ":
                        instrument = Instrument.AGOGO.getValue();
                        break;
                    case "Gonguê":
                        instrument = Instrument.GONGUE.getValue();
                        break;
                    case "Alfaia":
                        instrument = Instrument.ALFAIA.getValue();
                        break;
                    case "Canto":
                        instrument = Instrument.CANTO.getValue();
                        break;
                    default:
                        instrument = Instrument.CAIXA.getValue();
                }

                String situation = "";

                switch (user[4]) {
                    case "Em acordo":
                        situation = Situation.AGREEMENT.getValue();
                        break;
                    case "Isento":
                        situation = Situation.EXEMPT.getValue();
                        break;
                    case "Débito":
                        situation = Situation.DEBIT.getValue();
                        break;
                    default:
                        situation = Situation.UP_TO_DATE.getValue();
                }

                boolean active = true;
                boolean associated = true;

                if (user[5].equals("Não")) {
                    associated = false;
                }

                if (user[6].equals("Não")) {
                    active = false;
                }


                memberRequestList.add(new MemberRequest(email, adminLevel, name, instrument, situation, active, associated));

                line = br.readLine();
            }

        } catch(IOException e) {
            e.getMessage();
        }

        memberRequestList.remove(0);
        int i = 0;
    }

    private void executeLogin() {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        AuthRemoteDataSource remoteDataSource = new AuthRemoteDataSource(ApiDataSourceProvider.Companion.getAuthApiDataSource());
        binding.setIsLoading(true);
        remoteDataSource.login(new AuthRequest(email, password), new BaseCallBack<Member>() {
             @Override
             public void onSuccess(Member result) {
                 UserDefaults.getInstance(getApplicationContext()).save(result);

                 boolean isAdmin = result.getAdminLevel() >= UserLevel.ROLE_ADMIN.getValue();
                 Intent intent = isAdmin ?
                         new Intent(getApplicationContext(), AdminDashboardActivity.class) :
                         new Intent(getApplicationContext(), HomeActivity.class);

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
