package com.suraj.instaloaderapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.suraj.instaloaderapp.BuildConfig
import com.suraj.instaloaderapp.api.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val NETWORK_TIMEOUT = 30L

var networkModule= module{


    single { provideGson() }
    single { httpLoggingInterceptor() }
    single { provideOkHttpClient(get ()) }
    single {  provideRetrofit(get(),get())}
    factory { provideApiBackend(get()) }


}
fun provideGson(): Gson = GsonBuilder().create()

fun httpLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()
}
fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL).client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
}


fun provideApiBackend(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)