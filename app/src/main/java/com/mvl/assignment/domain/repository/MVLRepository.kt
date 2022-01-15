package com.mvl.assignment.domain.repository

import com.mvl.assignment.data.aqi.AqiResponse
import com.mvl.assignment.domain.model.HistoryList

interface MVLRepository {

    suspend fun getAqiByLatAndLng(lat: Double,lng: Double): AqiResponse

    suspend fun getHistory() : HistoryList

}