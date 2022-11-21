package com.gabia.mbaproject.application.modules.member.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource
import com.gabia.mbaproject.infrastructure.remote.remotedatasource.PaymentRemoteDataSource
import com.gabia.mbaproject.model.PaymentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberPaymentListViewModel(val apiDataSource: PaymentApiDataSource): ViewModel() {
    lateinit var paymentListLiveData: MutableLiveData<List<PaymentResponse>>

    init {
        paymentListLiveData = MutableLiveData()
    }

    fun getPayments(): MutableLiveData<List<PaymentResponse>> {
        return paymentListLiveData
    }

    fun fetchPayments(userId: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            val payments = apiDataSource.getByUserId(userId)
            paymentListLiveData.postValue(payments.body())
        }
    }

}