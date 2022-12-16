package com.gabia.mbaproject.infrastructure.remote.providers

import com.gabia.mbaproject.infrastructure.remote.api.*

class ApiDataSourceProvider {

    companion object {
        private val retrofit = RetrofitInstance.getInstance()

        val authApiDataSource: AuthApiDataSource = retrofit.create(AuthApiDataSource::class.java)
        val memberApiDataSource: MemberApiDataSource = retrofit.create(MemberApiDataSource::class.java)
        val paymentApiDataSource: PaymentApiDataSource = retrofit.create(PaymentApiDataSource::class.java)
        val rehearsalApiDataSource: RehearsalApiDataSource = retrofit.create(RehearsalApiDataSource::class.java)
        val presenceApiDataSource: PresenceApiDataSource = retrofit.create(PresenceApiDataSource::class.java)
    }
}