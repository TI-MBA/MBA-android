package com.gabia.mbaproject.application.modules.admin.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.CellMemberBinding;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.Situation;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    List<Member> memberList = new ArrayList<>();
    private final SelectListener<Member> listener;
    private Context context;

    public MemberAdapter(SelectListener<Member> listener) {
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
        CellMemberBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_member, parent, false
        );

        context = parent.getContext();

        return new MemberAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member current = memberList.get(position);
        holder.bind(current, context);
        holder.cellBinding.cellMemberContent
                .setOnClickListener(view -> listener.didSelect(current));
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CellMemberBinding cellBinding;

        public ViewHolder(CellMemberBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }

        public void bind(Member member, Context context) {
            Situation userSituation = Situation.valueOf(member.getSituation());
            int contentColor = member.getActive() ? R.color.white : R.color.gray;
            cellBinding.cellMemberNameTitle.setText(member.getName());
            cellBinding.cellMemberContent.setBackgroundColor(context.getColor(contentColor));
            cellBinding.cellMemberSituationTag.setBackgroundTintList(context.getResources().getColorStateList(userSituation.getSituationColor()));
            cellBinding.cellMemberSituationTag.setText(userSituation.getFormattedValue());
        }
    }
}
