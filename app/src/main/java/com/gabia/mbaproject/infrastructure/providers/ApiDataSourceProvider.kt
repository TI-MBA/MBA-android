package com.gabia.mbaproject.infrastructure.providers

import com.gabia.mbaproject.infrastructure.api.AuthApiDataSource
import com.gabia.mbaproject.infrastructure.api.RetrofitInstance
import com.gabia.mbaproject.infrastructure.api.UserApiDataSource

class ApiDataSourceProvider {

    companion object {
        private val retrofit = RetrofitInstance.getInstance()

        val authApiDataSource = retrofit.create(AuthApiDataSource::class.java)
        val userApiDataSource = retrofit.create(UserApiDataSource::class.java)
    }
}