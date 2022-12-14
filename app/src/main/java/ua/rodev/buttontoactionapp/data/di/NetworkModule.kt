package ua.rodev.buttontoactionapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.rodev.buttontoactionapp.BuildConfig
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.data.MainActionRepository
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.ActionService
import ua.rodev.buttontoactionapp.data.cloud.CloudDataSource
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.domain.HandleError
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchers.Main()

    @Singleton
    @Provides
    fun provideClient() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .build()


    @Singleton
    @Provides
    fun provideAuthCalls(retrofit: Retrofit): ActionService =
        retrofit.create(ActionService::class.java)

    @Singleton
    @Provides
    fun provideActionRepository(
        cloudDataSource: CloudDataSource,
        cacheDataSource: CacheDataSource,
        mapper: ActionCloud.Mapper<ActionDomain>,
        handleError: HandleError<Exception>
    ): ActionRepository = MainActionRepository(cloudDataSource, cacheDataSource, mapper, handleError)
}
