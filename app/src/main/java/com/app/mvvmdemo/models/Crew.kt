package com.app.mvvmdemo.models

data class Crew(
    val adult: Boolean?,
    val backdrop_path: String?,
    val credit_id: String?,
    val department: String?,
    val genre_ids: List<Int?>?,
    val id: Int?,
    val job: String?,
    val media_type: String?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
)