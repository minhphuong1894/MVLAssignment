package com.mvl.assignment.data.aqi

import android.os.Parcelable
import com.mvl.assignment.domain.model.Aqi
import com.mvl.assignment.data.response.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AqiResponse(
    val status: String? = "",
    val data: Aqi? = null,
) : Parcelable