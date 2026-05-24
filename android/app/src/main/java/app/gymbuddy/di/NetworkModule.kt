package app.gymbuddy.di

import app.gymbuddy.data.remote.ApiClient
import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient =
        ApiClient.okHttp(authInterceptor)

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = ApiClient.retrofit(client)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = ApiClient.apiService(retrofit)
}
