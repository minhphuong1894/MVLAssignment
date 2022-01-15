package com.mvl.assignment.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvl.assignment.base.BaseViewModel
import com.mvl.assignment.data.Resource
import com.mvl.assignment.domain.repository.MVLRepository
import com.mvl.assignment.domain.useCase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val useCase: GetHistoryUseCase
) : BaseViewModel() {

    fun uiState(): LiveData<HistoryState> = _uiState
    private val _uiState: MutableLiveData<HistoryState> = MutableLiveData()

    fun getHistory(){
        useCase().onEach {result ->
            when(result){
                is Resource.Loading -> {
                    _uiState.value = HistoryState(isLoading = true)
                }
                is Resource.Success -> {
                    result.data?.let {
                        _uiState.value = HistoryState(histories = it)
                    }
                }
                is Resource.Error -> {
                    _uiState.value = HistoryState(error = result.message ?: "An unexpected error")
                }
            }

        }.launchIn(viewModelScope)
    }
}