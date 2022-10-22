package com.gabia.mbaproject.application.modules.admin.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.finance.FinanceHomeActivity;
import com.gabia.mbaproject.databinding.ActivityAdminDashboardBinding;
import com.gabia.mbaproject.model.AdminFeatureModel;

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
        List<AdminFeatureModel> featureList = new ArrayList<>();

        // TODO: Add switch to check user type
        featureList.add(new AdminFeatureModel("Financeiro"));

        adminDashboardAdapter.setFeatures(featureList);
    }


    @Override
    public void didSelect(AdminFeatureModel model) {
        Intent i = new Intent(getApplicationContext(), FinanceHomeActivity.class);
        startActivity(i);
    }
}