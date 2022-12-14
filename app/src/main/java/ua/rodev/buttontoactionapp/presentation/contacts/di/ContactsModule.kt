package ua.rodev.buttontoactionapp.presentation.contacts.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.contacts.ContactUi
import ua.rodev.buttontoactionapp.presentation.contacts.LocalContacts
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactsModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class Snackbar

    @Provides
    @Snackbar
    fun provideContactsSnackbar(): Target.Mutable<String> = Target.SnackbarTarget()

    @Provides
    fun provideLocalContactsTarget(): Target.Mutable<List<ContactUi>> = Target.LocalContactsTarget()

    @Provides
    @Singleton
    fun provideLocalContacts(@ApplicationContext context: Context): LocalContacts {
        return LocalContacts.Main(context)
    }

}
