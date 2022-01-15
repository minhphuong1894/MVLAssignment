package com.mvl.assignment.di

import com.mvl.assignment.domain.repository.MVLRepository
import com.mvl.assignment.data.repository.MVLRepositoryImpl
import com.mvl.assignment.network.MVLService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMVLRepository(mvlService: MVLService): MVLRepository {
        return MVLRepositoryImpl(mvlService)
    }
}