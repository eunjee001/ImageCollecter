package com.kkyume.android.imagecollecter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.kkyume.android.imagecollecter.adapter.SearchImageAdapter
import com.kkyume.android.imagecollecter.databinding.FragmentSearchImageBinding
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.image.ImageResponse
import com.kkyume.android.imagecollecter.model.image.Image_Document
import com.kkyume.android.imagecollecter.model.video.VideoResponse
import com.kkyume.android.imagecollecter.model.video.Video_Document
import com.kkyume.android.imagecollecter.network.RetrofitService
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.Date

class SearchFragment : Fragment(), SearchImageAdapter.OnClickListener{
    private lateinit var binding: FragmentSearchImageBinding
    private var mLayoutView: View? = null
    private var searchFor : String = ""
    private val handler = Handler(Looper.getMainLooper())
    private var mViewModel: ImageViewModel? = null
    private var currentPage = 1
    private var isEnd = false
    private lateinit var searchAdapter: SearchImageAdapter
    private lateinit var combinedAllList: ArrayList<CombinedListData>
    private lateinit var imageResponse :ImageResponse
    private lateinit var videoResponse :VideoResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_image,
            container,
            false
        )
        mLayoutView = binding.root

        init()
        initRecyclerView()
        initLiveData()

        return mLayoutView
    }


    val runnable = Runnable {
        initLiveData()
    }
    private fun init() {
        mViewModel = ImageViewModel()
        combinedAllList = ArrayList()
        binding.etSearch.addTextChangedListener {
            searchFor = it.toString()
            mViewModel?.requestImageResponse(searchFor, currentPage, combinedAllList,searchAdapter)
            mViewModel?.requestVideoResponse(searchFor,currentPage, combinedAllList, searchAdapter)
            // 시간을 잠깐 멈췄을때 대기
            handler.removeCallbacks(runnable)
            handler.postDelayed(
                runnable,
                100,
            )

        }

    }

    private fun initLiveData() {
        mViewModel?.let { viewModel ->
            viewModel.imageLiveData.observe(this as LifecycleOwner) { res ->
                res?.let {
                    combinedImageList(it)
                    isEnd = it.meta.isEnd
                }
            }
            viewModel.videoLiveData.observe(this as LifecycleOwner) { res ->
                res?.let {
                    combinedVideoList(it)
                    isEnd = it.meta.isEnd

                }
            }
        }

    }
    private fun initRecyclerView(){
        searchAdapter = SearchImageAdapter(requireContext(), this)
        binding.rvSearchImage.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(EndlessScrollListener{
                loadMoreVideos()
            })

        }
    }

    private fun combinedImageList(imageResponse: ImageResponse){
        val imageDocumentList: List<Image_Document> = imageResponse.documents

        for (document in imageDocumentList) {
            // 이미지 문서에서 필요한 정보 추출
            val thumbnailUrl: String? = document.thumbnailUrl
            val title: String? = document.displaySitename
            val category: String? = document.collection
            val contents :String? = document.docUrl
            val date : Date? = document.datetime

            val combinedData = CombinedListData(thumbnailUrl, title, category, contents, date)

            // CombinedList에 추가
            println(">>> combinedAllList" + combinedAllList)
            combinedAllList.add(combinedData)

        }
    }

    private fun combinedVideoList(videoResponse: VideoResponse){
        val videoDocumentList: List<Video_Document> = videoResponse.documents

        for (document in videoDocumentList) {
            // 이미지 문서에서 필요한 정보 추출
            val thumbnailUrl: String? = document.thumbnail
            val title: String? = document.title
            val category: String? = null
            val contents :String? = document.url
            val date : Date? = document.datetime

            val combinedData = CombinedListData(thumbnailUrl, title, category, contents, date)

            // CombinedList에 추가
            combinedAllList.add(combinedData)
        }

    }

    private fun loadMoreVideos() {
        if (isEnd) return

        lifecycleScope.launch {

            mViewModel?.requestImageResponse(searchFor, currentPage, combinedAllList, searchAdapter)
            mViewModel?.requestVideoResponse(searchFor, currentPage, combinedAllList, searchAdapter)
            handler.postDelayed(
                runnable,
                200
            )
            currentPage ++

            println(">>> cur" +currentPage)
        }
    }

    override fun onClick(position: Int) {
        val jsonArr = JSONArray()
        jsonArr.put(combinedAllList[position])

        val result = jsonArr.toString()
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()){
            putString("collectorPref", result)
            commit()
        }
        println(">>> pre" +sharedPreferences.all)
    }
}
