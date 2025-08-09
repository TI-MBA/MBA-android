package com.gabia.mbaproject.application.modules.admin.finance.payment;

import static com.gabia.mbaproject.utils.DateUtils.brazilianDate;
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
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<PaymentResponse> paymentList = new ArrayList<>();
    private ActionsListener<PaymentResponse> listener;

    public PaymentAdapter(ActionsListener<PaymentResponse> listener) {
        this.listener = listener;
    }

    public void setPaymentList(List<PaymentResponse> paymentList) {
        this.paymentList.clear();
        this.paymentList = new ArrayList<>(paymentList);
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
        PaymentResponse current = paymentList.get(position);
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

        public void bind(PaymentResponse payment) {
            String relativeDate = DateUtils.changeFromIso(monthAndYear, payment.getDate());
            String paymentDate = DateUtils.changeFromIso(brazilianDate, payment.getPaymentDate());
            String paymentValue = moneyFormat(payment.getPaymentValue());
            String observationText = payment.getObservation().isEmpty() ? "-" : payment.getObservation();

            cellBinding.cellPaymentMonth.setText(relativeDate);
            cellBinding.cellPaymentValue.setText(paymentValue);
            cellBinding.paymentDateText.setText(paymentDate);
            cellBinding.cellPaymentObservationText.setText(observationText);
        }
    }
}
