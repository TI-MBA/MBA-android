package com.gabia.mbaproject.application.modules.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.model.Member
import com.gabia.mbaproject.model.enums.UserLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.streams.toList

class MemberViewModel: ViewModel() {

    var memberListLiveData: MutableLiveData<List<Member>> = MutableLiveData()

    fun fetchAll(failCallback: (code: Int) -> Unit) {
        val apiDataSource = ApiDataSourceProvider.memberApiDataSource

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
}