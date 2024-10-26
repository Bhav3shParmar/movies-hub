package com.app.mvvmdemo.repositorys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DataViewModel(private val repo: DataRepository) : ViewModel() {
    var page: Int = 0
    private var peoplePage: Int = 0

    val mainData get() = repo.mData
    val mainData2 get() = repo.mData2
    val mainData3 get() = repo.mData3
    val mainData4 get() = repo.mData4
    val mainData5 get() = repo.mData5
    val mainDataQuery get() = repo.mDataQuery

    fun loadData() {
        page++
        viewModelScope.launch {
            repo.callApi(page)
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            repo.getMovieDetails(id)
        }
    }

    fun getPeopleList() {
        peoplePage++
        viewModelScope.launch {
            repo.getPeopleList(peoplePage)
        }
    }

    fun getPersonDetail(id: Int) {
        viewModelScope.launch {
            repo.getPeopleDetails(id)
        }

    }

    fun getCertification(id: Int) {
        viewModelScope.launch {
            repo.getCertification(id)
        }
    }

    fun getQueryResult(str: String) {
        viewModelScope.launch {
            repo.getQueryList(str)
        }
    }
}