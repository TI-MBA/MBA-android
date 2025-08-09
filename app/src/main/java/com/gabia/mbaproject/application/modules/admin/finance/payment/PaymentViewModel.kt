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
import kotlinx.coroutines.withContext

class PaymentViewModel: ViewModel() {

    private val apiDataSource = ApiDataSourceProvider.paymentApiDataSource

    fun create(payment: PaymentRequest, callback: BaseCallBack<PaymentResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiDataSource.createPayment(payment)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessful) {
                        callback.onSuccess(result.body())
                    } else {
                        callback.onError(result.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(9999)
                }
            }
        }
    }

    fun update(paymentId: Int, payment: UpdatePaymentRequest, callback: BaseCallBack<PaymentResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiDataSource.updatePayment(paymentId, payment)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessful) {
                        callback.onSuccess(result.body())
                    } else {
                        callback.onError(result.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(9999)
                }
            }
        }
    }

    fun delete(paymentId: Int, resultCallback: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiDataSource.deletePayment(paymentId)
                withContext(Dispatchers.Main) {
                    resultCallback(result.code())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    resultCallback(9999)
                }
            }
        }
    }
}