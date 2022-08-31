package com.pradum786.samplenote.di

import com.pradum786.samplenote.api.AuthInterceptor
import com.pradum786.samplenote.api.NoteAPI
import com.pradum786.samplenote.api.UserAPI
import com.pradum786.samplenote.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofitBuilder: Retrofit.Builder):UserAPI{
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClint( authInterceptor: AuthInterceptor) : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()

    }

    @Singleton
    @Provides
    fun provideNoteAPI(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient):NoteAPI{
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NoteAPI::class.java)
    }

}
