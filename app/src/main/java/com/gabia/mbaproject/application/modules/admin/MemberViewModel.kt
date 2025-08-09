package com.gabia.mbaproject.application.modules.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack
import com.gabia.mbaproject.model.*
import com.gabia.mbaproject.model.enums.UserLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class MemberViewModel: ViewModel() {
    private val apiDataSource = ApiDataSourceProvider.memberApiDataSource
    var memberListLiveData: MutableLiveData<List<Member>> = MutableLiveData()
    var relatedRehearsalMemberListLiveData: MutableLiveData<List<PresenceResponse>> = MutableLiveData()
    var unrelatedRehearsalMemberListLiveData: MutableLiveData<List<Member>> = MutableLiveData()

    fun fetchAll(failCallback: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.getAll()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        memberListLiveData.postValue(
                            response.
                            body()?.
                            stream()?.
                            filter { it.adminLevel == UserLevel.ROLE_USER.value }?.
                            collect(Collectors.toList())?.
                            sortedBy { it.name } ?: emptyList()
                        )
                    } else {
                        failCallback(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    failCallback(9999)
                }
            }
        }
    }

    fun fetchRelatedWithRehearsal(rehearsalId: Int, failCallback: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.getRelatedWithRehearsal(rehearsalId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        relatedRehearsalMemberListLiveData.postValue(response.body()?.
                        stream()?.
                        filter { it.name.lowercase().contains("admin").not() }?.
                        collect(Collectors.toList()))
                    } else {
                        failCallback(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    failCallback(9999)
                }
            }
        }
    }

    fun fetchUnrelatedWithRehearsal(rehearsalId: Int, failCallback: (code: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.getUnrelatedWithRehearsal(rehearsalId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        unrelatedRehearsalMemberListLiveData.postValue(response.body()?.
                        stream()?.
                        filter { it.name.lowercase().contains("admin").not() }?.
                        collect(Collectors.toList()))
                    } else {
                        failCallback(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    failCallback(9999)
                }
            }
        }
    }

    fun create(member: CreateMemberRequest, callback: BaseCallBack<Member?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.create(member)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body())
                    } else {
                        callback.onError(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(9999)
                }
            }
        }
    }

    fun edit(id: Int, member: MemberRequest, callback: BaseCallBack<Member?>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiDataSource.edit(id, member)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body())
                    } else {
                        callback.onError(response.code())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(9999)
                }
            }
        }
    }
}