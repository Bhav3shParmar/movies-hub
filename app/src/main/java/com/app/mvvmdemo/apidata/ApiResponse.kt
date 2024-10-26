package com.app.mvvmdemo.apidata

import com.app.mvvmdemo.models.CastAndCrewDataClass
import com.app.mvvmdemo.models.CombineCreditDataClass
import com.app.mvvmdemo.models.MovieDetailsResponse
import com.app.mvvmdemo.models.PeopleDetailsResponse
import com.app.mvvmdemo.models.QueryResultDataClass
import com.app.mvvmdemo.models.ReleaseDateDataModel
import com.app.mvvmdemo.models.ResponseDataModel
import com.app.mvvmdemo.models.TvDetailsDataModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiResponse {

    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("trending/all/{time_window}")
    suspend fun getMovieList(
        @Path("time_window") pop: String = "day",
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseDataModel>


    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Response<MovieDetailsResponse>

    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("tv/{series_id}")
    suspend fun getTVDetails(
        @Path("series_id") tvId: Int,
        @Query("language") language: String = "en-US"
    ): Response<TvDetailsDataModel>


    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("search/multi")
    fun getSearchMovie(
        @Query("query") movieName: String,
        @Query("include_adult") isAdult: Boolean,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Call<ResponseDataModel>

    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("person/popular")
    suspend fun getPeopleList(
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<ResponseDataModel>


    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("person/{person_id}")
    suspend fun getPeopleDetails(
        @Path("person_id") id: Int
    ): Response<PeopleDetailsResponse>


    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<CastAndCrewDataClass>


    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("tv/{series_id}/credits")
    suspend fun getTvCast(
        @Path("series_id") series: Int,
        @Query("language") language: String = "en-US"
    ): Response<CastAndCrewDataClass>

    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("person/{person_id}/combined_credits")
    suspend fun getCombineCredits(
        @Path("person_id") id: Int,
        @Query("language") language: String = "en-US"
    ): Response<CombineCreditDataClass>

    @Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Y2MzZWExYWY2YjViYTExY2M0ZDdkYzBlYmE4NDMxMSIsIm5iZiI6MTcyNjIwNjY2MS42MDQzMDIsInN1YiI6IjY2ZTNkMTNmYzgxYjI0YjNmZTIzZTZiNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.x4-ccu9SCjZt9GtQdwpdoJDpkOf5RuAB1jdK1tHxCK8",
        "accept: application/json"
    )
    @GET("movie/{movie_id}/release_dates")
    suspend fun getCertification(
        @Path("movie_id") id: Int
    ): Response<ReleaseDateDataModel>

    @GET("search/trending")
    suspend fun getSearchQuery(
        @Query("query") query: String? = ""
    ): Response<QueryResultDataClass>

}