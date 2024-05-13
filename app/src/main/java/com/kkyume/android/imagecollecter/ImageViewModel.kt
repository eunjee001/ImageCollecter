package com.kkyume.android.imagecollecter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.image.ImageResponse
import com.kkyume.android.imagecollecter.model.video.VideoRequest
import com.kkyume.android.imagecollecter.model.video.VideoResponse
import com.kkyume.android.imagecollecter.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageViewModel(){

    // 주소명으로 시군구코드 조회 MutableLiveData
    private var mImageMutableLiveData = MutableLiveData<ImageResponse>()

    // 간편결제 메인 MutableLiveData
    private var mVideoMutableLiveData = MutableLiveData<VideoResponse>()
    private var combinedList = arrayListOf<CombinedListData>()


    val imageLiveData : LiveData<ImageResponse>
        get() = mImageMutableLiveData

    val videoLiveData : LiveData<VideoResponse>
        get() = mVideoMutableLiveData


    fun requestImageResponse(searchData: String) {
        val call = RetrofitService.getInterface().getImage(searchData, "recency", 10, 20)
        call.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                println(">>>> imageResponse" +response.body())
                response.body()?.let {
                    mImageMutableLiveData.value = it
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                println(">>>>> 실패")
                t.printStackTrace()

            }
        })
    }
//    private fun combinedList(imageResponse : ImageResponse){
//        for (i in imageResponse.documents){
////            combinedList.add(CombinedListData(i.thumbnailUrl, i.displaySitename, i.collection, i.docUrl, i.datetime))
//        }
//        println(">>>> combinedList " + combinedList)
//    }
    fun requestVideoResponse(searchData: String) {
        val call = RetrofitService.getInterface().getVideo(searchData, "recency", 10, 10)
        call.enqueue(object : Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                println(">>>>> 성공")
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                println(">>>>> 실패")

            }
        })
    }

}