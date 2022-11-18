package com.gabia.mbaproject.infrastructure.remote.remotedatasource

import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.PaymentResponse

interface PaymentRemoteDataSourceContract {

    fun getByUser(id: Int, resultCallBack: BaseCallBack<List<PaymentResponse>?>)
}