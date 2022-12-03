package com.gabia.mbaproject.application.modules.admin.rollcall.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.CellMemberPresenceBinding;
import com.gabia.mbaproject.model.PresenceResponse;
import com.gabia.mbaproject.model.enums.PresenceType;

import java.util.ArrayList;
import java.util.List;

public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder> {

    List<PresenceResponse> presenceList = new ArrayList<>();

    public void setPresenceList(List<PresenceResponse> presenceList) {
        this.presenceList.clear();
        this.presenceList = presenceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellMemberPresenceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_member_presence, parent, false
        );

        return new PresenceAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PresenceResponse current = presenceList.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return presenceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CellMemberPresenceBinding cellBinding;

        public ViewHolder(CellMemberPresenceBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }

        public void bind(PresenceResponse model) {
            PresenceType presence = PresenceType.valueOf(model.getType());
            cellBinding.cellMemberName.setText(model.getUser().getName());
            cellBinding.cellMemberInstrumentTag.setText(model.getUser().getInstrument());
            cellBinding.cellMemberPresenceIndicator.setImageResource(presence.getPresenceIcon());
        }
    }
}
