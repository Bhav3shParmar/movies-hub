package com.app.mvvmdemo.repositorys

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.mvvmdemo.apidata.ApiService
import com.app.mvvmdemo.models.MovieDetailsResponse
import com.app.mvvmdemo.models.PeopleDetailsResponse
import com.app.mvvmdemo.models.QueryResultDataClass
import com.app.mvvmdemo.models.ReleaseDateDataModel
import com.app.mvvmdemo.models.ResponseDataModel
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException

class DataRepository(private val context: Context) {
    private var mutableLiveData = MutableLiveData<Response<ResponseDataModel>>()
    private var mutableLiveData2 = MutableLiveData<Response<MovieDetailsResponse>>()
    private var mutableLiveData3 = MutableLiveData<Response<ArrayList<ResponseDataModel>>>()
    private var mutableLiveData4 = MutableLiveData<Response<PeopleDetailsResponse>>()
    private var mutableLiveDataCertification = MutableLiveData<Response<ReleaseDateDataModel>>()
    private var mutableLiveDataQueryList = MutableLiveData<Response<QueryResultDataClass>>()

    val mData get() = mutableLiveData
    val mData2 get() = mutableLiveData2
    val mData3 get() = mutableLiveData3
    val mData4 get() = mutableLiveData4
    val mData5 get() = mutableLiveDataCertification
    val mDataQuery get() = mutableLiveDataQueryList

    suspend fun getQueryList(str: String) {
        mDataQuery.value = Response.Loading()
        val result = ApiService.service2.getSearchQuery(query = str)
        if (result.isSuccessful) {
            mDataQuery.value = Response.Success(result.body()!!)
        } else {
            mDataQuery.value = Response.Error("")
        }
    }

    suspend fun callApi(page: Int) {
        mData.value = Response.Loading()
        val p1 = ApiService.service.getMovieList(language = "en-IN", page = page)
        if (p1.isSuccessful) {
            mData.value = Response.Success(p1.body()!!)
        } else {
            mData.value = Response.Error("Something went wrong...")
        }
    }

    suspend fun getMovieDetails(id: Int) {
        try {
            mData2.value = Response.Loading()
            val response = ApiService.service.getMovieDetails(movieId = id, language = "en-US")
            if (response.isSuccessful) {
                mData2.value = Response.Success(response.body()!!)
            } else {
                mData2.value = Response.Error("Unable to get Details")
            }
        } catch (e: SocketException) {
            mData2.value = Response.Error(e.localizedMessage ?: "connection reset")

        }catch (e: IOException){
            mData3.value = Response.Error(e.localizedMessage ?: "connection reset")
        }

    }

    suspend fun getPeopleList(page: Int) {
        try {
            mData3.value = Response.Loading()
            val tempList2: ArrayList<ResponseDataModel> = ArrayList()
            val response = ApiService.service.getPeopleList(language = "en-US", page = page)
            if (response.isSuccessful) {
                tempList2.add(response.body()!!)
                mData3.value = Response.Success(tempList2)
            } else {
                mData3.value = Response.Error("Unable to get people list")
            }
        }catch (e: SocketException){
            mData3.value = Response.Error(e.localizedMessage ?: "connection reset")
        }catch (e: IOException){
            mData3.value = Response.Error(e.localizedMessage ?: "connection reset")
        }

    }

    suspend fun getPeopleDetails(id: Int) {
        mData4.value = Response.Loading()
        val response = ApiService.service.getPeopleDetails(id)
        if (response.isSuccessful) {
            mData4.value = Response.Success(response.body()!!)
        } else {
            mData4.value = Response.Error("Unable to get Details")
        }

    }

    suspend fun getCertification(id: Int) {
        mData5.value = Response.Loading()
        val response = ApiService.service.getCertification(id)
        if (response.isSuccessful) {
            mData5.value = Response.Success(response.body()!!)
        }
    }

}