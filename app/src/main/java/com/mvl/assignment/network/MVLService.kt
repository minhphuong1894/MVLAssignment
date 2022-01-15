package com.mvl.assignment.network

import com.mvl.assignment.data.aqi.AqiResponse
import com.mvl.assignment.domain.model.HistoryList
import retrofit2.http.GET
import retrofit2.http.Path

interface MVLService {

    @GET("feed/geo:{lat};{lng}/")
    suspend fun getAqiByLocation(@Path("lat") lat: Double, @Path("lng") lng: Double): AqiResponse

    @GET("/books?year=2020&month=11")
    suspend fun getHistory() : HistoryList
}