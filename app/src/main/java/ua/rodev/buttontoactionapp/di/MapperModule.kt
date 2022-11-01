package ua.rodev.buttontoactionapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.presentation.action.ActionDomainToUiMapper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MapperModule {

    @Singleton
    @Provides
    fun provideActionDomainToUiMapper(): ActionDomainToUiMapper = ActionDomainToUiMapper()
}