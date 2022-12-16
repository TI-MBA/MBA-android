package com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.PresenceRequest
import com.gabia.mbaproject.model.PresenceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PresenceViewModel: ViewModel() {

    private val apiDataSource = ApiDataSourceProvider.presenceApiDataSource

    fun create(presence: PresenceRequest, resultCallback: BaseCallBack<PresenceResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.create(presence)
            if (response.isSuccessful) {

                resultCallback.onSuccess(response.body())
            } else {
                resultCallback.onError(response.code())
            }
        }
    }

    fun update(presenceId: Int, presence: PresenceRequest, resultCallback: BaseCallBack<PresenceResponse?>) {
            viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.update(presenceId, presence)
            if (response.isSuccessful) {
                resultCallback.onSuccess(response.body())
            } else {
                resultCallback.onError(response.code())
            }
        }
    }

    fun delete(presenceId: Int, resultCallback: BaseCallBack<Unit?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.delete(presenceId)
            if (response.isSuccessful) {
                resultCallback.onSuccess(response.body())
            } else {
                resultCallback.onError(response.code())
            }
        }
    }
}