package com.mvl.assignment.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvl.assignment.data.remote.toBaseException
import com.mvl.assignment.util.SingleLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {

    // loading flag
    val isLoading by lazy { SingleLiveData<Boolean>().apply { value = false } }

    // error message
    val errorMessage by lazy { SingleLiveData<String>() }

    // optional flags
    val noInternetConnectionEvent by lazy { SingleLiveData<Unit>() }
    val connectTimeoutEvent by lazy { SingleLiveData<Unit>() }
    val unknownErrorEvent by lazy { SingleLiveData<Unit>() }

    // exception handler for coroutine
    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { context, throwable ->
            viewModelScope.launch {
                onError(throwable)
            }
        }
    }
    protected val viewModelScopeExceptionHandler by lazy { viewModelScope + exceptionHandler }

    /**
     * handle throwable when load fail
     */
    protected open fun onError(throwable: Throwable) {
        when (throwable) {
            // case no internet connection
            is UnknownHostException -> {
                noInternetConnectionEvent.call()
            }
            is ConnectException -> {
                noInternetConnectionEvent.call()
            }
            // case request time out
            is SocketTimeoutException -> {
                connectTimeoutEvent.call()
            }
            else -> {
                // convert throwable to base exception to get error information
                val baseException = throwable.toBaseException()
                when (baseException.httpCode) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        errorMessage.value = baseException.message
                    }
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                        errorMessage.value = baseException.message
                    }
                    else -> {
                        unknownErrorEvent.call()
                    }
                }
            }
        }
        hideLoading()
    }

    open fun showError(e: Throwable) {
        errorMessage.value = e.message
    }

    fun showLoading() {
        isLoading.value = true
    }

    fun hideLoading() {
        isLoading.value = false
    }
}