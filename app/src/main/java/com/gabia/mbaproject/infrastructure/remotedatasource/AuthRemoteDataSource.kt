package com.gabia.mbaproject.infrastructure.remotedatasource

import com.gabia.mbaproject.infrastructure.api.AuthApiDataSource
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.AuthRequest
import com.gabia.mbaproject.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthRemoteDataSource(private val apiDataSource: AuthApiDataSource): AuthRemoteDataSourceContract {

    override fun login(authRequest: AuthRequest, resultCallBack: BaseCallBack<User?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiDataSource.login(authRequest)
            if (response.isSuccessful) {
                resultCallBack.onSuccess(response.body())
            } else {
                resultCallBack.onError("Failed request " + response.code(), response.code())
            }
        }
    }
}