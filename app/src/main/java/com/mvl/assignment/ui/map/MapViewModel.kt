package com.mvl.assignment.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvl.assignment.base.BaseViewModel
import com.mvl.assignment.data.Resource
import com.mvl.assignment.data.aqi.AqiResponse
import com.mvl.assignment.domain.repository.MVLRepository
import com.mvl.assignment.domain.useCase.GetAgiUseCase
import com.mvl.assignment.ui.history.HistoryState
import com.mvl.assignment.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val useCase: GetAgiUseCase
) : BaseViewModel() {
    fun uiState(): LiveData<MapState> = _uiState
    private val _uiState: MutableLiveData<MapState> = MutableLiveData()

    fun loadAqiByLocation(lat: Double,lng: Double) {
        useCase(lat,lng).onEach {result ->
            when(result){
                is Resource.Loading -> {
                    _uiState.value = MapState(isLoading = true)
                }
                is Resource.Success -> {
                    result.data?.let {
                        _uiState.value = MapState(data = it)
                    }
                }
                is Resource.Error -> {
                    _uiState.value = MapState(error = result.message ?: "An unexpected error")
                }
            }

        }.launchIn(viewModelScope)
    }
}