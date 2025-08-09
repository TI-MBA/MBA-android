package com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.application.DeleteListener
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.RehearsalRequest
import com.gabia.mbaproject.model.RehearsalResponse
import com.gabia.mbaproject.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors
import kotlin.streams.toList

class RehearsalViewModel: ViewModel() {
    private val apiDataSource = ApiDataSourceProvider.rehearsalApiDataSource
    var rehearsalLiveData: MutableLiveData<List<RehearsalResponse>> = MutableLiveData()

    fun fetchAll(failCallBack: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.getAll()
                if(response.isSuccessful) {
                    val orderedDate = response.body()?.stream()?.sorted { o1, o2 ->
                        val firstDate = DateUtils.toDate(DateUtils.isoDateFormat, o1.date)
                        val secondDate = DateUtils.toDate(DateUtils.isoDateFormat, o2.date)
                        secondDate.compareTo(firstDate)
                    }

                    withContext(Dispatchers.Main) {
                        rehearsalLiveData.postValue(orderedDate?.collect(Collectors.toList()))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        failCallBack(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    failCallBack(9999)
                }
            }
        }
    }

    fun create(request: RehearsalRequest, resultCallback: BaseCallBack<RehearsalResponse?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.create(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        resultCallback.onSuccess(response.body())
                    } else {
                        resultCallback.onError(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    resultCallback.onError(9999)
                }
            }
        }
    }

    fun delete(rehearsalId: Int, resultCallback: DeleteListener<String?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.deleteRehearsal(rehearsalId)
                withContext(Dispatchers.Main) {
                    resultCallback.didDelete("" + response)
                }
            } catch (e: Exception) {
                resultCallback.didDelete("")
            }
        }
    }

    fun finalizeRehearsal(rehearsalId: Int, successCallBack: () -> Unit, failCallBack: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.finalizeRehearsal(rehearsalId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        successCallBack()
                    } else {
                        failCallBack(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    failCallBack(9999)
                }
            }
        }
    }
}