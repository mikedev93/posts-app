package com.esteban.postsapp.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.ui.viewholders.PostViewHolder

class PostsListAdapter(
    val listener: Listener
): ListAdapter<Post, PostViewHolder>(DiffUtilCallback) {

    interface Listener {
        fun selectedPost(post: Post)
        fun favedPost(position: Int)
        fun deletedPost(position: Int)
    }

    private object DiffUtilCallback: DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.title == newItem.title && oldItem.body == newItem.body
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)

        holder.post = post
        holder.binding.viewHolderPostItem.setOnClickListener {
            listener.selectedPost(post)
        }
    }
}