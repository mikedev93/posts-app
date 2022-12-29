package com.esteban.postsapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esteban.postsapp.R
import com.esteban.postsapp.ui.viewmodels.PostDetailViewModel

class PostDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PostDetailFragment()
    }

    private lateinit var viewModel: PostDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}