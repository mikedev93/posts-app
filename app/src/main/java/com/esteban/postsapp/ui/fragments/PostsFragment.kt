package com.esteban.postsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteban.postsapp.R
import com.esteban.postsapp.databinding.FragmentPostsBinding
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.ui.adapters.PostsListAdapter
import com.esteban.postsapp.ui.util.MarginItemDecoration
import com.esteban.postsapp.ui.util.SwipeToFavDeleteCallback
import com.esteban.postsapp.ui.viewmodels.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment(), PostsListAdapter.Listener, SwipeToFavDeleteCallback.Listener {

    lateinit var binding: FragmentPostsBinding

    lateinit var postsListAdapter: PostsListAdapter

    val viewModel: PostsViewModel by viewModels()

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

        setUpToolbar()
        configureRecyclerView()
        subscribeViewModel()
        setUpListeners()
    }

    private fun setUpToolbar() {
        val navController = findNavController()
        binding.fragmentPostsToolbar.setupWithNavController(navController)
    }

    private fun configureRecyclerView() {
        postsListAdapter = PostsListAdapter(this)

        with(binding.fragmentPostsRecyclerView) {
            adapter = postsListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = View.OVER_SCROLL_NEVER
            setHasFixedSize(false)
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.card_margin)))
        }

        val itemTouchHelper = ItemTouchHelper(SwipeToFavDeleteCallback(requireContext(), this))
        itemTouchHelper.attachToRecyclerView(binding.fragmentPostsRecyclerView)
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.posts?.let {
                        postsListAdapter.submitList(it)
                        postsListAdapter.notifyDataSetChanged()
                    }

                    handleLoading(uiState.isLoading)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { uiEvent ->
                when (uiEvent) {
                    is PostsViewModel.PostsListViewEvent.DisplayError -> {
                        //TODO: handle error
                    }
                }
            }
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.fragmentPostsLoadingLayout.isVisible = loading
    }

    private fun setUpListeners() {
        with(binding) {
            fragmentPostsEfabDeleteButton.setOnClickListener { deleteAllButFavoritePosts() }
            fragmentPostsEfabLoadFromApiButton.setOnClickListener { loadAllFromApi() }
        }
    }

    override fun selectedPost(post: Post) {
        val action = PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(
            post = post
        )
        findNavController().navigate(action)
    }

    override fun favedPost(position: Int) {
        viewModel.setFavoritePost(position)
        postsListAdapter.notifyItemChanged(position)
    }

    override fun deletedPost(position: Int) {
        viewModel.deletePost(position)
        postsListAdapter.notifyItemRemoved(position)
    }

    override fun onSwipedRight(position: Int) {
        favedPost(position)
    }

    override fun onSwipedLeft(position: Int) {
        deletedPost(position)
    }

    fun deleteAllButFavoritePosts() {
        viewModel.deleteAllButFavorites()
    }

    fun loadAllFromApi() {
        viewModel.getAllPosts()
    }
}