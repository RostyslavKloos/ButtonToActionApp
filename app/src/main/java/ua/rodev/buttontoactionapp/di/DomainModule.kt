package ua.rodev.buttontoactionapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.data.ActionInteractorImpl
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.presentation.action.ActionDomainToUiMapper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Singleton
    @Provides
    fun provideActionInteractor(
        repository: ActionRepository,
        mapper: ActionDomainToUiMapper,
    ): ActionInteractor = ActionInteractorImpl(repository, mapper)
}