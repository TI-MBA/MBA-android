package com.gabia.mbaproject.application.modules.admin.dashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.CellAdminDashboardBinding;
import com.gabia.mbaproject.model.AdminFeatureModel;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardAdapter extends RecyclerView.Adapter<AdminDashboardAdapter.ViewHolder> {

    private List<AdminFeatureModel> adminFeatureModelList;
    private SelectListener<AdminFeatureModel> listener;

    public AdminDashboardAdapter(SelectListener<AdminFeatureModel> listener) {
        adminFeatureModelList = new ArrayList<>();
        this.listener = listener;
    }

    public void setFeatures(List<AdminFeatureModel> adminFeatureModelList) {
        this.adminFeatureModelList.clear();
        this.adminFeatureModelList.addAll(adminFeatureModelList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellAdminDashboardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_admin_dashboard, parent, false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminFeatureModel current = adminFeatureModelList.get(position);
        holder.bind(current);
        holder.cellBinding.adminDashboardContentView
                .setOnClickListener(view -> listener.didSelect(current));
    }

    @Override
    public int getItemCount() {
        return adminFeatureModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CellAdminDashboardBinding cellBinding;

        public ViewHolder(CellAdminDashboardBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }

        // TODO: Add model
        public void bind(AdminFeatureModel model) {
            cellBinding.adminFeatureTitle.setText(model.getFeatureTitle());
        }
    }
}
