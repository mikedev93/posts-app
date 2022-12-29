package com.esteban.postsapp.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.esteban.postsapp.databinding.ViewHolderPostBinding
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.ui.adapters.PostsListAdapter

class PostViewHolder(
    val binding: ViewHolderPostBinding
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun inflate(parent: ViewGroup): PostViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return PostViewHolder(ViewHolderPostBinding.inflate(inflater, parent, false))
        }
    }

    fun setUpView(post: Post, listener: PostsListAdapter.Listener) {
        post.let {
            binding.viewHolderPostTitle.text = it.title
            binding.viewHolderPostFavoriteIndicator.isVisible = it.isFavorite

            binding.viewHolderPostTitle.setOnClickListener {
                listener.favedPost(post)
            }
        }
    }
}