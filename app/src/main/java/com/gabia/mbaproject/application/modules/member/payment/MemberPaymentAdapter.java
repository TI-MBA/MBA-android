package com.gabia.mbaproject.application.modules.member.payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentAdapter;
import com.gabia.mbaproject.databinding.CellPaymentBinding;
import com.gabia.mbaproject.model.Payment;
import com.gabia.mbaproject.model.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

public class MemberPaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<PaymentResponse> paymentList = new ArrayList<>();

    public void setPaymentList(List<PaymentResponse> paymentList) {
        this.paymentList.clear();
        this.paymentList.addAll(paymentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellPaymentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_payment, parent, false
        );

        return new PaymentAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        holder.cellBinding.paymentCellActionsContent.setVisibility(View.GONE);
        PaymentResponse current = paymentList.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }
}
