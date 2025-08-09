package com.gabia.mbaproject.application.modules.member.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.model.PaymentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberPaymentListViewModel : ViewModel() {
    var paymentListLiveData: MutableLiveData<List<PaymentResponse>> = MutableLiveData()

    fun fetchPayments(userId: Int, failCallback: (code: Int) -> Unit) {
        val apiDataSource = ApiDataSourceProvider.paymentApiDataSource

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.getByUserId(userId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        paymentListLiveData.postValue(response.body())
                    } else {
                        failCallback(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    failCallback(9999)
                }
            }
        }
    }
}