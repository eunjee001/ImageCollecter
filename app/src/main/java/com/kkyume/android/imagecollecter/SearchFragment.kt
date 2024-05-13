package com.kkyume.android.imagecollecter

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkyume.android.imagecollecter.adapter.SearchImageAdapter
import com.kkyume.android.imagecollecter.databinding.FragmentSearchImageBinding
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.image.ImageResponse
import com.kkyume.android.imagecollecter.model.image.Image_Document
import java.util.Date

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchImageBinding
    private var mLayoutView: View? = null
    private var searchFor : String = ""
    private val handler = Handler(Looper.getMainLooper())
    private var mViewModel: ImageViewModel? = null

    private lateinit var searchAdapter: SearchImageAdapter

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

        initLiveData()

        return mLayoutView
    }

    private fun init() {
        mViewModel = ImageViewModel()
        searchAdapter = SearchImageAdapter()
        val runnable = Runnable {
            initLiveData()
        }

        binding.etSearch.addTextChangedListener {
            searchFor = it.toString()
            mViewModel?.requestImageResponse(searchFor)
//            mViewModel?.requestVideoResponse(searchFor)
            // 시간을 잠깐 멈췄을때 대기
            handler.removeCallbacks(runnable)
            handler.postDelayed(
                runnable,
                500
            )

        }

        binding.rvSearchImage.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    private fun initLiveData() {
        mViewModel?.let { viewModel ->
            viewModel.imageLiveData.observe(this as LifecycleOwner) { res ->
                res?.let {
                    println(">>>> it $it")
                    combinedList(it)

                }
            }
//            viewModel.videoLiveData.observe(this as LifecycleOwner) { res ->
//                res?.let {
//                    println(">>>video" + it.meta)
//                }
//            }
        }
    }

    private fun combinedList(imageResponse : ImageResponse){

        val imageDocumentList: List<Image_Document> = imageResponse.documents
        val combinedList: ArrayList<CombinedListData> = ArrayList()

        for (document in imageDocumentList) {
            // 이미지 문서에서 필요한 정보 추출
            val thumbnailUrl: String? = document.thumbnailUrl
            val title: String? = document.displaySitename
            val category: String? = document.collection
            val contents :String? = document.docUrl
            val date : Date? = document.datetime

            val combinedData = CombinedListData(thumbnailUrl, title, category, contents, date)

            // CombinedList에 추가
            combinedList.add(combinedData)
            searchAdapter.submitList(combinedList)

        }
        println(">>>> combinedList"+combinedList)

    }

}
