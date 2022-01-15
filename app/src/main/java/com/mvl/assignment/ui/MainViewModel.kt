package com.mvl.assignment.ui

import androidx.lifecycle.viewModelScope
import com.mvl.assignment.base.BaseViewModel
import com.mvl.assignment.domain.repository.MVLRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mvlRepository: MVLRepository
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoadingScreen = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }
    }
}