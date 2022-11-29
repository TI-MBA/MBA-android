package com.gabia.mbaproject.infrastructure.utils

interface BaseCallBack<T> {
    fun onSuccess(result: T)
    fun onError(code: Int)
}