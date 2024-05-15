package com.kkyume.android.imagecollecter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    private val visibleThreshold = 10
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, recyclerView)
            loading = true
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView)
}