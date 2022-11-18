package com.gabia.mbaproject.infrastructure.remote.providers

import com.gabia.mbaproject.infrastructure.remote.api.AuthApiDataSource
import com.gabia.mbaproject.infrastructure.remote.api.RetrofitInstance
import com.gabia.mbaproject.infrastructure.remote.api.UserApiDataSource

class ApiDataSourceProvider {

    companion object {
        private val retrofit = RetrofitInstance.getInstance()

        val authApiDataSource = retrofit.create(AuthApiDataSource::class.java)
        val userApiDataSource = retrofit.create(UserApiDataSource::class.java)
    }
}