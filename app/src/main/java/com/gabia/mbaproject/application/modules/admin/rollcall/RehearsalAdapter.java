package com.gabia.mbaproject.application.modules.admin.rollcall;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.DeleteListener;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.CellRehearsalBinding;
import com.gabia.mbaproject.model.RehearsalResponse;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class RehearsalAdapter extends RecyclerView.Adapter<RehearsalAdapter.ViewHolder> {

    List<RehearsalResponse> rehearsalList;
    private final SelectListener<RehearsalResponse> listener;
    private final DeleteListener<RehearsalResponse> deleteListener;

    public RehearsalAdapter(
            SelectListener<RehearsalResponse> listener,
            DeleteListener<RehearsalResponse> deleteListener
    ) {
        rehearsalList = new ArrayList<>();
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRehearsal(List<RehearsalResponse> rehearsalList) {
        this.rehearsalList.clear();
        this.rehearsalList = new ArrayList<>(rehearsalList);
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
        holder.bind(current, deleteListener);
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


        public void bind(RehearsalResponse response, DeleteListener<RehearsalResponse> deleteListener) {
            String title = getTitle(response);
            cellBinding.rehearsalTitleText.setText(title);
            cellBinding.rehearsalContentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDeleteAlert(response, deleteListener);
                    return false;
                }
            });
        }

        private void showDeleteAlert(RehearsalResponse model, DeleteListener<RehearsalResponse> deleteListener) {
            String title = "Deletar o " + getTitle(model) + " ?";
            AlertDialog dialog = new AlertDialog.Builder(itemView.getContext())
                    .setTitle(title)
                    .setMessage("Essa ação não pode ser desfeita")
                    .setPositiveButton("Cancelar", null)
                    .setNegativeButton("Deletar", (dialogInterface, i) -> {
                        deleteListener.didDelete(model);
                    })
                    .create();
            dialog.show();
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark));
        }

        private String getTitle(RehearsalResponse response) {
            String dateInPattern = DateUtils.changeFromIso(DateUtils.brazilianDate, response.getDate());
            return "Ensaio - " + dateInPattern;
        }
    }
}