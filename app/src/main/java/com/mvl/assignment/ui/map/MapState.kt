package com.mvl.assignment.ui.map

import com.mvl.assignment.data.aqi.AqiResponse

data class MapState(
    var isLoading : Boolean = false,
    var data : AqiResponse = AqiResponse(),
    var error : String = ""
)
