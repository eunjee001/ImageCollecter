package com.kkyume.android.imagecollecter.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.ItemType
import com.kkyume.android.imagecollecter.model.image.ImageResponse
import com.kkyume.android.imagecollecter.model.video.VideoResponse
import com.kkyume.android.imagecollecter.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class ImageViewModel : ViewModel() {

    private val imageList = ArrayList<CombinedListData>()
    private val videoList = ArrayList<CombinedListData>()

    private val _combinedListLiveData = MutableLiveData<List<CombinedListData>>()
    val combinedListLiveData: LiveData<List<CombinedListData>> = _combinedListLiveData

    fun requestImageResponse(searchData: String, currentPage: Int) {
        val call = RetrofitService.getInterface().getImage(searchData, "recency", currentPage, 10)
        call.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                response.body()?.let {
                    val newList = ArrayList<CombinedListData>()
                    for (document in it.documents) {
                        val thumbnailUrl: String? = document.thumbnailUrl
                        val title: String? = document.displaySitename
                        val category: String? = document.collection
                        val contents: String? = document.docUrl
                        val date: Date? = document.datetime

                        val combinedData = CombinedListData(
                            thumbnailUrl,
                            title,
                            category,
                            contents,
                            date,
                            ItemType.IMAGE,
                            thumbnailUrl
                        )
                        newList.add(combinedData)
                    }
                    imageList.clear()
                    imageList.addAll(newList)
                    updateCombinedList()
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun requestVideoResponse(searchData: String, currentPage: Int) {
        val call = RetrofitService.getInterface().getVideo(searchData, "recency", currentPage, 10)
        call.enqueue(object : Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                response.body()?.let {
                    val newList = ArrayList<CombinedListData>()
                    for (document in it.documents) {
                        val thumbnailUrl: String? = document.thumbnail
                        val title: String? = document.title
                        val category: String? = null
                        val contents: String? = document.url
                        val date: Date? = document.datetime

                        val combinedData = CombinedListData(
                            thumbnailUrl,
                            title,
                            category,
                            contents,
                            date,
                            ItemType.VIDEO,
                            thumbnailUrl
                        )
                        newList.add(combinedData)
                    }
                    videoList.clear()
                    videoList.addAll(newList)
                    updateCombinedList()
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun updateCombinedList() {
        val sortedImageList = imageList.sortedByDescending { it.date }
        val sortedVideoList = videoList.sortedByDescending { it.date }

        val combinedList = ArrayList<CombinedListData>()
        combinedList.addAll(sortedImageList)
        combinedList.addAll(sortedVideoList)

        val sortedList = combinedList.sortedByDescending { it.date }

        _combinedListLiveData.value = sortedList
    }

}