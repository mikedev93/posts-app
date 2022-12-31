package com.esteban.postsapp.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.ui.viewholders.CommentViewHolder

class CommentsListAdapter: ListAdapter<Comment, CommentViewHolder>(DiffUtilCallback) {

    private object DiffUtilCallback: DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.name == newItem.name && oldItem.body == newItem.body
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.comment = comment
    }
}