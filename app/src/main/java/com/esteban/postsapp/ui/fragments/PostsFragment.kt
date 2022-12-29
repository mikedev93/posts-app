package com.esteban.postsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteban.postsapp.databinding.FragmentPostsBinding
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.util.Resource
import com.esteban.postsapp.ui.adapters.PostsListAdapter
import com.esteban.postsapp.ui.viewmodels.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : Fragment(), PostsListAdapter.Listener {

    lateinit var binding: FragmentPostsBinding

    val viewModel: PostsViewModel by viewModels()
    lateinit var postsListAdapter: PostsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView()
        subscribeViewModel()
    }

    private fun configureRecyclerView() {
        postsListAdapter = PostsListAdapter(this)

        with(binding.fragmentPostsRecyclerView) {
            adapter = postsListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = View.OVER_SCROLL_NEVER
            setHasFixedSize(false)
        }
    }

    private fun subscribeViewModel() {
        viewModel.postsState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { postsListAdapter.submitList(it) }
                }

                is Resource.Error -> {
                    //handle error
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            //hide or show progressbar/loading indicator
        }
    }

    override fun selectedPost(post: Post) {
        TODO("Not yet implemented")
    }

    override fun favedPost(post: Post) {
        post.isFavorite = !post.isFavorite
        postsListAdapter.notifyDataSetChanged()
    }

    override fun deletedPost(post: Post) {
        TODO("Not yet implemented")
    }
}