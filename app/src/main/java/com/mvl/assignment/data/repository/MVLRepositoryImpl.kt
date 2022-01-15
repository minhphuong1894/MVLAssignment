package com.mvl.assignment.data.repository

import com.mvl.assignment.data.aqi.AqiResponse
import com.mvl.assignment.domain.model.HistoryList
import com.mvl.assignment.domain.repository.MVLRepository
import com.mvl.assignment.network.MVLService
import javax.inject.Inject

class MVLRepositoryImpl @Inject constructor(
    private val apiService: MVLService,
) : MVLRepository {

    override suspend fun getAqiByLatAndLng(lat: Double, lng: Double): AqiResponse {
        return apiService.getAqiByLocation(lat,lng)
    }

    override suspend fun getHistory(): HistoryList {
        return apiService.getHistory()
    }


}