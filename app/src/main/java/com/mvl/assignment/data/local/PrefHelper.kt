package com.mvl.assignment.data.local

interface PrefHelper {

    fun isFirstRun(): Boolean

    fun remove(key: String)

    fun clear()

}