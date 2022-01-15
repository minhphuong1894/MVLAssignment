package com.mvl.assignment.domain.useCase

import com.mvl.assignment.data.Resource
import com.mvl.assignment.data.aqi.AqiResponse
import com.mvl.assignment.domain.model.HistoryList
import com.mvl.assignment.domain.repository.MVLRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAgiUseCase @Inject constructor(
    private val mvlRepository: MVLRepository
) {
    operator fun invoke(lat: Double,lng: Double) : Flow<Resource<AqiResponse>> = flow {
        try {
            emit(Resource.Loading<AqiResponse>())
            val coin = mvlRepository.getAqiByLatAndLng(lat, lng)
            emit(Resource.Success<AqiResponse>(coin))
        } catch (e : HttpException){
            emit(Resource.Error<AqiResponse>(e.localizedMessage ?: "An unexpected error"))
        } catch (e : IOException){
            emit(Resource.Error<AqiResponse>("Couldn't reach server.Check your internet connect!"))
        }
    }
}