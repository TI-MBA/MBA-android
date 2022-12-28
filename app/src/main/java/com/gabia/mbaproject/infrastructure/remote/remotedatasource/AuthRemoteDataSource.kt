package com.gabia.mbaproject.infrastructure.remote.remotedatasource

import com.gabia.mbaproject.infrastructure.remote.api.AuthApiDataSource
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.AuthRequest
import com.gabia.mbaproject.model.EditPasswordRequest
import com.gabia.mbaproject.model.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface AuthRemoteDataSourceContract {
    fun login(authRequest: AuthRequest, resultCallBack: BaseCallBack<Member?>)
    fun changePassword(authRequest: EditPasswordRequest, resultCallBack: BaseCallBack<Int>)
    fun resetPassword(authRequest: AuthRequest, resultCallBack: BaseCallBack<AuthRequest?>)
}

class AuthRemoteDataSource(private val apiDataSource: AuthApiDataSource):
    AuthRemoteDataSourceContract {

    override fun login(authRequest: AuthRequest, resultCallBack: BaseCallBack<Member?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiDataSource.login(authRequest)
            if (response.isSuccessful) {
                resultCallBack.onSuccess(response.body())
            } else {
                resultCallBack.onError(response.code())
            }
        }
    }

    override fun changePassword(authRequest: EditPasswordRequest, resultCallBack: BaseCallBack<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiDataSource.changePassword(authRequest)
            if (response.isSuccessful) {
                resultCallBack.onSuccess(response.code())
            } else {
                resultCallBack.onError(response.code())
            }
        }
    }

    override fun resetPassword(authRequest: AuthRequest, resultCallBack: BaseCallBack<AuthRequest?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiDataSource.resetPassword(authRequest)
            if (response.isSuccessful) {
                resultCallBack.onSuccess(response.body())
            } else {
                resultCallBack.onError(response.code())
            }
        }
    }
}