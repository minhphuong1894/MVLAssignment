package com.mvl.assignment.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class HistoryList(
    val data: List<HistoryItem>
) : Parcelable

@Parcelize
data class HistoryItem(
    val a: LocationTemp?,
    val b: LocationTemp?,
    val price: Double?
) : Parcelable

fun tempHistoryLocation(): HistoryList {
    return HistoryList(
        arrayListOf(
            HistoryItem(
                a = LocationTemp(
                    id = 1,
                    address = "Seoul Location",
                    lat = 36.564,
                    lng = 127.001,
                    aqi = "40",
                    nickName = ""

                ),
                b = LocationTemp(
                    id = 2,
                    address = "HongKong Location",
                    lat = 36.564,
                    lng = 127.001,
                    aqi = "40",
                    nickName = ""
                ),
                price = 10000.0
            ),
            HistoryItem(
                a = LocationTemp(
                    id = 1,
                    address = "USA Location",
                    lat = 36.564,
                    lng = 127.001,
                    aqi = "40",
                    nickName = ""
                ),
                b = LocationTemp(
                    id = 2,
                    address = "Canada Location",
                    lat = 36.564,
                    lng = 127.001,
                    aqi = "40",
                    nickName = ""
                ),
                price = 10000.0
            )
        )
    )
}

fun tempSeoulLocation(): LocationTemp {
    return LocationTemp(
        id = 0,
        address = "Seoul Location",
        lat = 36.564,
        lng = 127.001,
        aqi = "40",
        nickName = ""
    )
}

fun tempHKLocation(): LocationTemp {
    return LocationTemp(
        id = 0,
        address = "HongKong Location",
        lat = 36.564,
        lng = 127.001,
        aqi = "40",
        nickName = ""
    )
}

fun tempUSALocation(): LocationTemp {
    return LocationTemp(
        id = 0,
        address = "USA Location",
        lat = 36.564,
        lng = 127.001,
        aqi = "40",
        nickName = ""
    )
}

fun tempCanadaLocation(): LocationTemp {
    return LocationTemp(
        id = 0,
        address = "Canada Location",
        lat = 36.564,
        lng = 127.001,
        aqi = "40",
        nickName = ""
    )
}

