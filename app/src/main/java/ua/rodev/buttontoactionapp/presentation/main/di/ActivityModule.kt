package ua.rodev.buttontoactionapp.presentation.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ActivityModule {

    @Provides
    @Singleton
    fun provideActionFlow(): Communication.Mutable<NavigationStrategy> = Communication.NavigationFlow()
}
