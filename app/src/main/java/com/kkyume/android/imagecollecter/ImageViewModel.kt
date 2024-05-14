package com.kkyume.android.imagecollecter

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.kkyume.android.imagecollecter.adapter.SearchImageAdapter
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.image.ImageResponse
import com.kkyume.android.imagecollecter.model.video.VideoResponse
import com.kkyume.android.imagecollecter.network.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageViewModel(){

    // 주소명으로 시군구코드 조회 MutableLiveData
    private var mImageMutableLiveData = MutableLiveData<ImageResponse>()

    // 간편결제 메인 MutableLiveData
    private var mVideoMutableLiveData = MutableLiveData<VideoResponse>()
    private var combinedList = arrayListOf<CombinedListData>()
    private var currentPage = 1
    private var isEnd = false

    val imageLiveData : LiveData<ImageResponse>
        get() = mImageMutableLiveData

    val videoLiveData : LiveData<VideoResponse>
        get() = mVideoMutableLiveData


    fun requestImageResponse(searchData: String, currentPage : Int, combinedList:ArrayList<CombinedListData>, searchAdapter : SearchImageAdapter) {
        val call = RetrofitService.getInterface().getImage(searchData, "recency", currentPage, 10)
        call.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                response.body()?.let {
                    mImageMutableLiveData.value = it
                    isEnd = it.meta.isEnd
                    searchAdapter.submitList(combinedList)
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun requestVideoResponse(searchData: String, currentPage : Int, combinedList:ArrayList<CombinedListData>, searchAdapter : SearchImageAdapter) {
        val call = RetrofitService.getInterface().getVideo(searchData, "recency", currentPage, 10)
        call.enqueue(object : Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                response.body()?.let {
                    mVideoMutableLiveData.value = it
                    isEnd = it.meta.isEnd
                    searchAdapter.submitList(combinedList)

                    println(">>> cur" +currentPage)
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                t.printStackTrace()

            }
        })
    }


}