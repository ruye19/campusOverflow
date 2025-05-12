package com.frontendmasters.app.di

import com.frontendmasters.app.data.repository.QuestionRepository
import com.frontendmasters.app.data.repository.UserRepository
import com.frontendmasters.app.data.remote.CampusOverflowApi
import com.frontendmasters.app.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideQuestionRepository(api: CampusOverflowApi): QuestionRepository {
        return QuestionRepository(api)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: CampusOverflowApi): UserRepository {
        return UserRepository(api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: CampusOverflowApi): AuthRepository {
        return AuthRepository(api)
    }
} 