package com.mvl.assignment.di

import android.content.Context
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.mvl.assignment.BuildConfig
import com.mvl.assignment.enableLogging
import com.mvl.assignment.network.MVLService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private val TIMEOUT = 10L

    @Singleton
    @Provides
    fun provideOkHttpCache(context: Context): Cache =
        Cache(context.cacheDir, (10 * 1024 * 1024).toLong())

    @Singleton
    @Provides
    @Named("logging")
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = if (enableLogging()) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

    @Singleton
    @Provides
    @Named("header")
    fun provideHeaderInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val newUrl = request.url.newBuilder()
                .addQueryParameter("token", BuildConfig.MVL_API_KEY)
                .build()
            val newRequest = request.newBuilder()
                .url(newUrl)
                .method(request.method, request.body)
                .build()
            chain.proceed(newRequest)
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @Named("logging") logging: Interceptor,
        @Named("header") header: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .apply {
                addInterceptor(logging)
                if (BuildConfig.DEBUG) addInterceptor(OkHttpProfilerInterceptor())
                addInterceptor(header)
            }
            .build()

    @Singleton
    @Provides
    fun provideAppRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.MVL_BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MVLService =
        retrofit.create(MVLService::class.java)
}
