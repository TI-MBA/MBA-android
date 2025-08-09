package com.gabia.mbaproject.application.modules.member.rollcall;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.CellRollCallPresenceBinding;
import com.gabia.mbaproject.model.MemberPresenceResponse;
import com.gabia.mbaproject.model.PresenceDto;
import com.gabia.mbaproject.model.enums.PresenceType;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberRollCallAdapter extends RecyclerView.Adapter<MemberRollCallAdapter.ViewHolder> {

    List<MemberPresenceResponse> presenceList = new ArrayList<>();

    public void setPresenceList(List<MemberPresenceResponse> presenceList) {
        this.presenceList.clear();
        this.presenceList = new ArrayList<>(presenceList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellRollCallPresenceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_roll_call_presence, parent, false
        );

        return new MemberRollCallAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberPresenceResponse current = presenceList.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return presenceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CellRollCallPresenceBinding cellBinding;

        public ViewHolder(CellRollCallPresenceBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }

        public void bind(MemberPresenceResponse presence) {
            String title = "Ensaio - " + DateUtils.changeFromIso(DateUtils.brazilianDate, presence.getRehearsalDate());
            cellBinding.rehearsalTitleText.setText(title);

            if (presence.getType().equals(PresenceType.PRESENT.getValue())) {
                cellBinding.presenceIcon.setImageResource(R.drawable.ic_presence_green);
            } else if (presence.getType().equals(PresenceType.OBSERVATION.getValue())) {
                cellBinding.presenceIcon.setImageResource(R.drawable.ic_observation_yellow);
            } else {
                cellBinding.presenceIcon.setImageResource(R.drawable.ic_absence_red);
            }
        }
    }
}
