package com.kkyume.android.imagecollecter.network

import com.kkyume.android.imagecollecter.model.image.ImageResponse
import com.kkyume.android.imagecollecter.model.video.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryService {

    @GET("image")
    fun getImage(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<ImageResponse>

    @GET("vclip")
    fun getVideo(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<VideoResponse>
}