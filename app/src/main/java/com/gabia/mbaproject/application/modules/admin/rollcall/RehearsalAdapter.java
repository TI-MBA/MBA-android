package com.gabia.mbaproject.application.modules.admin.rollcall;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.CellRehearsalBinding;
import com.gabia.mbaproject.model.RehearsalResponse;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class RehearsalAdapter extends RecyclerView.Adapter<RehearsalAdapter.ViewHolder> {

    List<RehearsalResponse> rehearsalList;
    private SelectListener<RehearsalResponse> listener;

    public RehearsalAdapter(SelectListener<RehearsalResponse> listener) {
        rehearsalList = new ArrayList<>();
        this.listener = listener;
    }

    public void setRehearsal(List<RehearsalResponse> rehearsalList) {
        this.rehearsalList.clear();
        this.rehearsalList.addAll(rehearsalList);
        notifyDataSetChanged();
    }

    public List<RehearsalResponse> getRehearsalList() {
        return rehearsalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellRehearsalBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_rehearsal,
                parent,
                false
        );

        return new RehearsalAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RehearsalResponse current = rehearsalList.get(position);
        holder.bind(current);
        holder.cellBinding.rehearsalContentView.setOnClickListener(v -> listener.didSelect(current));
    }

    @Override
    public int getItemCount() {
        return rehearsalList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CellRehearsalBinding cellBinding;

        public ViewHolder(CellRehearsalBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }


        public void bind(RehearsalResponse response) {
            String dateInPattern = DateUtils.changeFromIso(DateUtils.brazilianDate, response.getDate());
            String title = "Ensaio - " + dateInPattern;
            cellBinding.rehearsalTitleText.setText(title);
        }
    }
}
