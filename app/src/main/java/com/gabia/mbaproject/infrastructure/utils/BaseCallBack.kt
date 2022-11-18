package com.gabia.mbaproject.infrastructure.utils

import okhttp3.ResponseBody

interface BaseCallBack<T> {
    fun onSuccess(result: T)
    fun onError(cause: String, code: Int)
}