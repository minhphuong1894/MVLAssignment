package com.mvl.assignment.ui.history

import com.mvl.assignment.domain.model.HistoryList

data class HistoryState(
    var isLoading : Boolean = false,
    var histories : HistoryList = HistoryList(data = emptyList()),
    var error : String = ""
)