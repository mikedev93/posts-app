package com.esteban.postsapp.ui.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esteban.postsapp.databinding.ViewHolderPostBinding
import com.esteban.postsapp.domain.model.Post
import kotlin.properties.Delegates

class PostViewHolder(
    val binding: ViewHolderPostBinding
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun inflate(parent: ViewGroup): PostViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return PostViewHolder(ViewHolderPostBinding.inflate(inflater, parent, false))
        }
    }

    var post: Post? by Delegates.observable(null) {_, _, value ->
        value?.let {
            with(binding) {
                viewHolderPostTitle.text = it.title
                viewHolderPostFavoriteIndicator.visibility = if (it.isFavorite) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}