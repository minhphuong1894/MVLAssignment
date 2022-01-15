package com.mvl.assignment.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.content.res.Resources
import com.mvl.assignment.data.local.AppPrefs
import com.mvl.assignment.data.local.PrefHelper
import com.mvl.assignment.util.ZoneDateTimeAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.nio.file.attribute.AclEntry.newBuilder
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MainApplicationModule {
    @Singleton
    @Provides
    fun provideAppContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources

    @Singleton
    @Provides
    fun provideAssetManager(context: Context): AssetManager = context.assets

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePrefHelper(prefs: AppPrefs): PrefHelper = prefs

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}