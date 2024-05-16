package com.kkyume.android.imagecollecter.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.kkyume.android.imagecollecter.PreviewActivity
import com.kkyume.android.imagecollecter.R
import com.kkyume.android.imagecollecter.adapter.SearchImageAdapter
import com.kkyume.android.imagecollecter.databinding.FragmentSearchImageBinding
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.CombinedStoredListData
import com.kkyume.android.imagecollecter.viewModel.ImageViewModel

class SearchFragment : Fragment(), SearchImageAdapter.OnClickListener {

    private lateinit var binding: FragmentSearchImageBinding
    private lateinit var searchAdapter: SearchImageAdapter
    private lateinit var viewModel: ImageViewModel
    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false
    private var currentItems = mutableListOf<CombinedListData>()
    private var sharedPreferences: SharedPreferences? = null
    private val sharedPreferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "STORAGE_ITEMS") {
                updateRecyclerView()
            }
        }

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
        binding.view.visibility = View.GONE
        initRecyclerView()
        initViewModel()
        initEditTextListener()
        initScrollListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        searchAdapter.submitList(currentItems)
        requireActivity().getPreferences(Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences =
            context.getSharedPreferences("STORAGE_ITEMS", Context.MODE_PRIVATE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().getPreferences(Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    private fun initRecyclerView() {
        searchAdapter = SearchImageAdapter(requireContext(), this)
        binding.rvSearchImage.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun updateRecyclerView() {
        val itemList = getPrefsStorageItems()
        if (itemList.isEmpty()) {
            binding.rvSearchImage.visibility = View.GONE
        } else {
            searchAdapter.submitList(currentItems)
            binding.rvSearchImage.visibility = View.VISIBLE

        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        viewModel.combinedListLiveData.observe(viewLifecycleOwner) { newItems ->
            currentItems.addAll(newItems)
            searchAdapter.submitList(currentItems.toList())
            isLoading = false
        }
        val itemList = getPrefsStorageItems()

        for (item in currentItems) {
            val isBookmarked = itemList.any { it.id == item.id }
            searchAdapter.ivFavorite().isSelected = isBookmarked
        }
        searchAdapter.notifyDataSetChanged()
    }

    private fun initEditTextListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                currentPage = 1
                binding.tvPageNumber.text = currentPage.toString()
                binding.view.visibility = View.VISIBLE
                currentItems.clear()
                viewModel.requestImageResponse(searchText, currentPage)
                viewModel.requestVideoResponse(searchText, currentPage)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.ivDelete.setOnClickListener {
            binding.etSearch.text.clear()
            binding.rvSearchImage.removeAllViewsInLayout()
        }
    }

    private fun initScrollListener() {
        binding.rvSearchImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData()
                }
            }
        })
    }

    private fun updatePageNumber() {
        binding.tvPageNumber.text = currentPage.toString()
    }

    private fun loadMoreData() {
        isLoading = true
        currentPage++
        val searchText = binding.etSearch.text.toString()
        viewModel.requestImageResponse(searchText, currentPage)
        viewModel.requestVideoResponse(searchText, currentPage)
        updatePageNumber()
    }

    override fun onClickFavorite(position: Int, select: Boolean) {
        val item = currentItems[position] // 현재 클릭된 아이템

        // 아이템 정보를 SharedPreferences에 저장 또는 삭제
        if (select) {
            saveItemToSharedPreferences(item)
        } else {
            removeItemFromSharedPreferences(item)
        }
    }

    override fun onClickPreview(position: Int) {
        val item = currentItems[position]
        val intent = Intent(requireActivity(), PreviewActivity::class.java)
        intent.putExtra("title", item.title)
        intent.putExtra("thumbnailUrl", item.thumbnailUrl)
        intent.putExtra("date", item.date)
        intent.putExtra("imageType", item.type)

        startActivity(intent)
    }

    private fun saveItemToSharedPreferences(itemList: CombinedListData) {

        val favoriteItems = getPrefsStorageItems().toMutableList()
        val findItem = favoriteItems.find { it.id == itemList.id }
        val storedItem = mutableListOf<CombinedStoredListData>()
        if (findItem == null) {
            favoriteItems.add(itemList)
            savePrefsStorageItems(favoriteItems)
            val stored = CombinedStoredListData(
                itemList.thumbnailUrl,
                itemList.title,
                itemList.category,
                itemList.contents,
                itemList.date,
                itemList.type,
                itemList.id,
                true
            )
            storedItem.add(stored)
        }
    }

    private fun savePrefsStorageItems(items: List<CombinedListData>) {
        val jsonString = Gson().toJson(items)
        sharedPreferences?.edit()?.putString("STORAGE_ITEMS", jsonString)?.apply()

    }

    private fun getPrefsStorageItems(): List<CombinedListData> {

        val jsonString = sharedPreferences?.getString("STORAGE_ITEMS", "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(jsonString, object : TypeToken<List<CombinedListData>>() {}.type)
        }
    }

    private fun removeItemFromSharedPreferences(itemList: CombinedListData) {
        val favoriteItems = getPrefsStorageItems().toMutableList()
        val findItem = favoriteItems.find { it.id == itemList.id }
        val storedItem = mutableListOf<CombinedStoredListData>()

        if (findItem != null) {
            favoriteItems.remove(findItem)
            val stored = CombinedStoredListData(
                findItem?.thumbnailUrl,
                findItem?.title,
                findItem?.category,
                findItem?.contents,
                findItem?.date,
                findItem?.type,
                findItem?.id,
                false
            )
            storedItem.remove(stored)
            savePrefsStorageItems(favoriteItems)
        }
    }
}
