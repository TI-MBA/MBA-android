package com.gabia.mbaproject.application.modules.admin.rollcall.memberlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.CellMemberPresenceBinding;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.Instrument;

import java.util.ArrayList;
import java.util.List;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {

    List<Member> memberList = new ArrayList<>();
    private final SelectListener<Member> listener;

    public MemberListAdapter(SelectListener<Member> listener) {
        this.listener = listener;
    }

    public void setMembers(List<Member> memberList) {
        this.memberList.clear();
        this.memberList.addAll(memberList);
        notifyDataSetChanged();
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellMemberPresenceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_member_presence, parent, false
        );

        return new MemberListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member current = memberList.get(position);
        holder.bind(current);
        holder.cellBinding.cellMemberContent
                .setOnClickListener(view -> listener.didSelect(current));
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CellMemberPresenceBinding cellBinding;

        public ViewHolder(CellMemberPresenceBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }

        public void bind(Member member) {
            String instrument = Instrument.valueOf(member.getInstrument()).getFormattedValue();
            cellBinding.cellMemberPresenceIndicator.setVisibility(View.GONE);
            cellBinding.cellMemberName.setText(member.getName());
            cellBinding.cellMemberInstrumentTag.setText(instrument);
        }
    }
}
