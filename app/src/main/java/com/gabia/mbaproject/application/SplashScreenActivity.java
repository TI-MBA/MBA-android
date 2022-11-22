package com.gabia.mbaproject.application;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gabia.mbaproject.application.modules.admin.dashboard.AdminDashboardActivity;
import com.gabia.mbaproject.application.modules.login.LoginActivity;
import com.gabia.mbaproject.application.modules.member.HomeActivity;
import com.gabia.mbaproject.infrastructure.local.UserDefaults;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.UserLevel;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Member member = UserDefaults.getInstance(getApplicationContext()).getCurrentUser();
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);

        if (member != null) {
            boolean isAdmin = member.getAdminLevel() >= UserLevel.ROLE_ADMIN.getValue();
            intent = isAdmin ?
                    new Intent(getApplicationContext(), AdminDashboardActivity.class) :
                    new Intent(getApplicationContext(), HomeActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
