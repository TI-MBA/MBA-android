package com.gabia.mbaproject.infrastructure.remote.providers

import com.gabia.mbaproject.infrastructure.remote.api.AuthApiDataSource
import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource
import com.gabia.mbaproject.infrastructure.remote.api.RetrofitInstance
import com.gabia.mbaproject.infrastructure.remote.api.MemberApiDataSource

class ApiDataSourceProvider {

    companion object {
        private val retrofit = RetrofitInstance.getInstance()

        val authApiDataSource = retrofit.create(AuthApiDataSource::class.java)
        val memberApiDataSource = retrofit.create(MemberApiDataSource::class.java)
        val paymentApiDataSource = retrofit.create(PaymentApiDataSource::class.java)
    }
}