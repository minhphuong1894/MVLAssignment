package com.mvl.assignment.domain.useCase

import com.mvl.assignment.data.Resource
import com.mvl.assignment.domain.model.HistoryList
import com.mvl.assignment.domain.repository.MVLRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val mvlRepository: MVLRepository
) {

    operator fun invoke() : Flow<Resource<HistoryList>> = flow {
        try {
            emit(Resource.Loading<HistoryList>())
            val coin = mvlRepository.getHistory()
            emit(Resource.Success<HistoryList>(coin))
        } catch (e : HttpException){
            emit(Resource.Error<HistoryList>(e.localizedMessage ?: "An unexpected error"))
        } catch (e : IOException){
            emit(Resource.Error<HistoryList>("Couldn't reach server.Check your internet connect!"))
        }
    }
}