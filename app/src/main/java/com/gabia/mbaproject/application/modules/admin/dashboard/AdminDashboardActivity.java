package com.gabia.mbaproject.application.modules.admin.dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.annotation.SuppressLint;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.App;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.finance.FinanceHomeActivity;
import com.gabia.mbaproject.application.modules.admin.rollcall.RollCallHomeActivity;
import com.gabia.mbaproject.databinding.ActivityAdminDashboardBinding;
import com.gabia.mbaproject.model.AdminFeatureModel;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.AdminFeatureCode;
import com.gabia.mbaproject.model.enums.UserLevel;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements SelectListener<AdminFeatureModel> {

    private ActivityAdminDashboardBinding binding;
    private AdminDashboardAdapter adminDashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard);
        adminDashboardAdapter = new AdminDashboardAdapter(this);
        binding.featureRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.featureRecyclerView.setAdapter(adminDashboardAdapter);

        setupAdminFeatures();
    }


    public void setupAdminFeatures() {
        Member member = App.getCurrentUser(this);
        List<AdminFeatureModel> featureList = new ArrayList<>();

        if (member.getAdminLevel() == UserLevel.ROLE_USER.getValue()) {
            Toast.makeText(this, "Essa tela é só para adms", Toast.LENGTH_SHORT).show();
            App.logout(this);
        } else if (member.getAdminLevel() == UserLevel.ROLE_ADMIN.getValue()) {
            featureList.add(new AdminFeatureModel("Financeiro", AdminFeatureCode.FINANCE));
            featureList.add(new AdminFeatureModel("Chamada", AdminFeatureCode.ROLL_CALL));
        } else if (member.getAdminLevel() == UserLevel.ROLE_FINANCE.getValue()) {
            featureList.add(new AdminFeatureModel("Financeiro", AdminFeatureCode.FINANCE));
        } else if (member.getAdminLevel() == UserLevel.ROLE_ROLL_CALL.getValue()) {
            featureList.add(new AdminFeatureModel("Chamada", AdminFeatureCode.ROLL_CALL));
        }

        adminDashboardAdapter.setFeatures(featureList);
    }


    @Override
    public void didSelect(AdminFeatureModel model) {
        if (model.getCode() == AdminFeatureCode.FINANCE) {
            Intent intent = new Intent(getApplicationContext(), FinanceHomeActivity.class);
            startActivity(intent);
        } else if (model.getCode() == AdminFeatureCode.ROLL_CALL) {
            Intent intent = new Intent(getApplicationContext(), RollCallHomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Opção não encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.DeleteDialogTheme)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair do app?")

                .setPositiveButton("Sair", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.logout(AdminDashboardActivity.this);
                    }

                })
                .setNegativeButton("Ficar", null)
                .show();
    }
}