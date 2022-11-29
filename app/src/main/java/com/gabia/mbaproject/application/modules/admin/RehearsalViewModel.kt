package com.gabia.mbaproject.application.modules.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.RehearsalRequest
import com.gabia.mbaproject.model.RehearsalResponse
import com.gabia.mbaproject.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.streams.toList

class RehearsalViewModel: ViewModel() {
    private val apiDataSource = ApiDataSourceProvider.rehearsalApiDataSource
    var rehearsalLiveData: MutableLiveData<List<RehearsalResponse>> = MutableLiveData()

    fun fetchAll(failCallBack: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.getAll()
            if(response.isSuccessful) {
                val orderedDate = response.body()?.stream()?.sorted { o1, o2 ->
                    val firstDate = DateUtils.toDate(DateUtils.isoDateFormat, o1.date)
                    val secondDate = DateUtils.toDate(DateUtils.isoDateFormat, o2.date)
                    secondDate.compareTo(firstDate)
                }

                rehearsalLiveData.postValue(orderedDate?.toList())
            } else {
                failCallBack(response.code())
            }
        }
    }

    fun create(request: RehearsalRequest, resultCallback: BaseCallBack<RehearsalResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiDataSource.create(request)
            if (response.isSuccessful) {
                resultCallback.onSuccess(response.body())
            } else {
                resultCallback.onError(response.code())
            }
        }
    }
}