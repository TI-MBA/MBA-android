package com.gabia.mbaproject.infrastructure.remote.remotedatasource

import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.PaymentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface PaymentRemoteDataSourceContract {
    fun getByUser(id: Int, resultCallBack: BaseCallBack<List<PaymentResponse>?>)
}

class PaymentRemoteDataSource(private val apiDataSource: PaymentApiDataSource): PaymentRemoteDataSourceContract {
    override fun getByUser(id: Int, resultCallBack: BaseCallBack<List<PaymentResponse>?>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiDataSource.getByUserId(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        resultCallBack.onSuccess(response.body())
                    } else {
                        resultCallBack.onError(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    resultCallBack.onError(9999)
                }
            }
        }
    }
}