package com.gabia.mbaproject.application.modules.member.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource
import com.gabia.mbaproject.model.PaymentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberPaymentListViewModel(private val apiDataSource: PaymentApiDataSource): ViewModel() {
    var paymentListLiveData: MutableLiveData<List<PaymentResponse>> = MutableLiveData()

    fun fetchPayments(userId: Int, failCallback: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.getByUserId(userId)
            if (response.isSuccessful) {
                paymentListLiveData.postValue(response.body())
            } else {
                failCallback(response.code())
            }
        }
    }
}