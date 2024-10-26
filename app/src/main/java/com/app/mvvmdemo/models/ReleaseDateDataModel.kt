package com.app.mvvmdemo.models

import java.util.Date

data class ReleaseDateDataModel(
    val id: Int,
    val results: ArrayList<ResultX>
)

data class ResultX(
    val iso_3166_1: String,
    val release_dates: ArrayList<ReleaseDate>
)

data class ReleaseDate(
    val certification: String,
    val descriptors: ArrayList<String>,
    val iso_639_1: String,
    val note: String,
    val release_date: Date,
    val type: Int,
)


