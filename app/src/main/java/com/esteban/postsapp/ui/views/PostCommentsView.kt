package com.esteban.postsapp.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteban.postsapp.R
import com.esteban.postsapp.databinding.ViewPostCommentsBinding
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.ui.adapters.CommentsListAdapter
import com.esteban.postsapp.ui.util.MarginItemDecoration

class PostCommentsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    var commentsListAdapter = CommentsListAdapter()

    val binding: ViewPostCommentsBinding by lazy {
        ViewPostCommentsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setUpView(comments: List<Comment>) {
        configureRecyclerView()
        commentsListAdapter.submitList(comments)
    }

    private fun configureRecyclerView() {
        with(binding.viewPostsCommentsRecyclerView) {
            adapter = commentsListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = View.OVER_SCROLL_NEVER
            setHasFixedSize(false)
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.card_margin)))
        }
    }
}