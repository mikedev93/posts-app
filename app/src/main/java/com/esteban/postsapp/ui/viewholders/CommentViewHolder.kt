package com.esteban.postsapp.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esteban.postsapp.databinding.ViewHolderCommentBinding
import com.esteban.postsapp.domain.model.Comment
import kotlin.properties.Delegates

class CommentViewHolder(
    val binding: ViewHolderCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun inflate(parent: ViewGroup): CommentViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return CommentViewHolder(ViewHolderCommentBinding.inflate(inflater, parent, false))
        }
    }

    var comment: Comment? by Delegates.observable(null) { _, _, value ->
        value?.let {
            with(binding) {
                viewHolderCommentName.text = it.name
                viewHolderCommentEmail.text = it.email
                viewHolderCommentBody.text = it.body
            }
        }
    }
}