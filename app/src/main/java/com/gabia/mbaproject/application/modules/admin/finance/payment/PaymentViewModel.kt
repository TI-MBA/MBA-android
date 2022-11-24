package com.gabia.mbaproject.application.modules.admin.finance.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.PaymentRequest
import com.gabia.mbaproject.model.PaymentResponse
import com.gabia.mbaproject.model.UpdatePaymentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel: ViewModel() {

    private val apiDataSource = ApiDataSourceProvider.paymentApiDataSource

    fun create(payment: PaymentRequest, callback: BaseCallBack<PaymentResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiDataSource.createPayment(payment)
            if (result.isSuccessful) {
                callback.onSuccess(result.body())
            } else {
                callback.onError(result.code())
            }
        }
    }

    fun update(paymentId: Int, payment: UpdatePaymentRequest, callback: BaseCallBack<PaymentResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = apiDataSource.updatePayment(paymentId, payment)
            if (result.isSuccessful) {
                callback.onSuccess(result.body())
            } else {
                callback.onError(result.code())
            }
        }
    }
}