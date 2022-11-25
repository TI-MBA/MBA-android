package com.gabia.mbaproject.application.modules.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.CreateMemberRequest
import com.gabia.mbaproject.model.Member
import com.gabia.mbaproject.model.MemberRequest
import com.gabia.mbaproject.model.PaymentResponse
import com.gabia.mbaproject.model.enums.UserLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.streams.toList

class MemberViewModel: ViewModel() {
    private val apiDataSource = ApiDataSourceProvider.memberApiDataSource
    var memberListLiveData: MutableLiveData<List<Member>> = MutableLiveData()

    fun fetchAll(failCallback: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.getAll()
            if (response.isSuccessful) {
                memberListLiveData.postValue(
                    response.
                    body()?.
                    stream()?.
                    filter { it.adminLevel == UserLevel.ROLE_USER.value }?.
                    toList()?.
                    sortedBy { it.name } ?: emptyList()
                )
            } else {
                failCallback(response.code())
            }
        }
    }

    fun create(member: CreateMemberRequest, callback: BaseCallBack<Member?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.create(member)
            if (response.isSuccessful) {
                callback.onSuccess(response.body())
            } else {
                callback.onError(response.code())
            }
        }
    }

    fun edit(id: Int, member: MemberRequest, callback: BaseCallBack<Member?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.edit(id, member)
            if (response.isSuccessful) {
                callback.onSuccess(response.body())
            } else {
                callback.onError(response.code())
            }
        }
    }
}