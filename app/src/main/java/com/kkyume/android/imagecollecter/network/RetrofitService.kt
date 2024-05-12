package com.kkyume.android.imagecollecter.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private val retrofitClient: Retrofit.Builder by lazy {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("Authorization", "KakaoAK 42c21f922bf8ef75d78ff975b3877807")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/v2/search/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val apiInterface: GalleryService by lazy {
        retrofitClient
            .build()
            .create(GalleryService::class.java)
    }
    fun getInterface(): GalleryService {
        return apiInterface
    }
}

