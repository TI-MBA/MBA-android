package com.gabia.mbaproject.application.modules.member.payment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource;

public class MemberPaymentListViewModelFactory implements ViewModelProvider.Factory {

    private PaymentApiDataSource apiDataSource;

    MemberPaymentListViewModelFactory(PaymentApiDataSource apiDataSource) {
        this.apiDataSource = apiDataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MemberPaymentListViewModel(apiDataSource);
    }
}
