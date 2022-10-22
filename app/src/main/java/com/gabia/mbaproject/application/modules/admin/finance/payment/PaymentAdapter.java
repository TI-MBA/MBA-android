package com.gabia.mbaproject.application.modules.admin.finance.payment;

import static com.gabia.mbaproject.utils.DateUtils.monthAndYear;
import static com.gabia.mbaproject.utils.FloatUtils.moneyFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.ActionsListener;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.CellPaymentBinding;
import com.gabia.mbaproject.model.Payment;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<Payment> paymentList = new ArrayList<>();
    private ActionsListener<Payment> listener;

    public PaymentAdapter(ActionsListener<Payment> listener) {
        this.listener = listener;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList.clear();
        this.paymentList.addAll(paymentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellPaymentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_payment, parent, false
        );

        return new PaymentAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment current = paymentList.get(position);
        holder.cellBinding.editPaymentIcon.setOnClickListener(view -> listener.edit(current));
        holder.cellBinding.deletePaymentIcon.setOnClickListener(view -> listener.delete(current));
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final CellPaymentBinding cellBinding;

        public ViewHolder(CellPaymentBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }

        public void bind(Payment payment) {
            String relativeDate = DateUtils.toString(monthAndYear, payment.getRelativeDate());
            String paymentValue = moneyFormat(payment.getValue());

            cellBinding.cellPaymentMonth.setText(relativeDate);
            cellBinding.cellPaymentValue.setText(paymentValue);
            cellBinding.cellPaymentObservationText.setText(payment.getObservation());
        }
    }
}
