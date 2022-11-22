package com.gabia.mbaproject.application.modules.admin.finance.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.model.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberListViewModel: ViewModel() {

    var memberListLiveData: MutableLiveData<List<Member>> = MutableLiveData()

    fun fetchAll(failCallback: (code: Int) -> Unit) {
        val apiDataSource = ApiDataSourceProvider.memberApiDataSource

        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.getAll()
            if (response.isSuccessful) {
                memberListLiveData.postValue(response.body())
            } else {
                failCallback(response.code())
            }
        }
    }
}