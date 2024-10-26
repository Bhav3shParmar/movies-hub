package com.app.mvvmdemo.repositorys

sealed class Response <T>(var data: T?= null ,var errorMsg: String?=null) {
    class Loading<T>(): Response<T>()
    class Success<T>(private val mData: T):Response<T>(data = mData)
    class Error<T>(private val msg :String): Response<T>(errorMsg =  msg)
}