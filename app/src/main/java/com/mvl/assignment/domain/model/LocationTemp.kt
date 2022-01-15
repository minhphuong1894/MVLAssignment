package com.mvl.assignment.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationTemp(
    val id: Int,
    var address: String, var nickName: String,
    var lat: Double, var lng: Double,
    var aqi : String
) : Parcelable

@Parcelize
class LocationTemps: ArrayList<LocationTemp>(), Parcelable