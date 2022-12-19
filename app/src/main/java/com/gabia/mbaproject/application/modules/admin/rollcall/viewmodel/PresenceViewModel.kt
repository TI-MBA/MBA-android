package com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.MemberPresenceResponse
import com.gabia.mbaproject.model.PresenceRequest
import com.gabia.mbaproject.model.PresenceResponse
import com.gabia.mbaproject.model.RehearsalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PresenceViewModel: ViewModel() {

    private val apiDataSource = ApiDataSourceProvider.presenceApiDataSource
    var presenceLiveData: MutableLiveData<List<MemberPresenceResponse>> = MutableLiveData()

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

    fun fetchAllBy(userId: Int, failCallBack: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.getAll(userId)
            if (response.isSuccessful) {
                presenceLiveData.postValue(response.body());
            } else {
                failCallBack(response.code())
            }
        }
    }
}