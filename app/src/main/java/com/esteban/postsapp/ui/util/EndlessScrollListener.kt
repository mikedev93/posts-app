package com.esteban.postsapp.ui.util

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollListener(
    val paginationListener: PaginationListener
): RecyclerView.OnScrollListener() {

    interface PaginationListener {
        fun getNextPage()
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private var canLoadMore = true
    private var resultsPageSize: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
        val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
        val isNotAtBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemCount >= resultsPageSize
        val shouldPaginate = isNotLoadingAndNotAtLastPage && isAtLastItem && isNotAtBeginning &&
                isTotalMoreThanVisible && isScrolling && canLoadMore

        if (shouldPaginate) {
            paginationListener.getNextPage()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }

    fun setIsLoading(isLoading: Boolean){
        this.isLoading = isLoading
    }

    fun setResultsPageSize(size: Int) {
        this.resultsPageSize = size
    }

    fun setCanLoadMore(canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore
    }
}