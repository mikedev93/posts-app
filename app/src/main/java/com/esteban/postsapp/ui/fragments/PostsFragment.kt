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
import com.esteban.postsapp.ui.util.EndlessScrollListener
import com.esteban.postsapp.ui.util.MarginItemDecoration
import com.esteban.postsapp.ui.util.SwipeToFavDeleteCallback
import com.esteban.postsapp.ui.viewmodels.PostsViewModel
import com.esteban.postsapp.ui.viewmodels.PostsViewModel.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment(), PostsListAdapter.Listener, SwipeToFavDeleteCallback.Listener,
    EndlessScrollListener.PaginationListener {

    lateinit var binding: FragmentPostsBinding

    lateinit var postsListAdapter: PostsListAdapter

    val viewModel: PostsViewModel by viewModels()

    val endlessScrollListener: EndlessScrollListener = EndlessScrollListener(this)

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
            addOnScrollListener(endlessScrollListener)
            setHasFixedSize(false)
            itemAnimator = null
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

                        binding.fragmentPostsNoDataLayout.isVisible = it.size == 0

                        val defaultPageSize = requireContext().resources.getInteger(R.integer.config_query_page_size)
                        val resultsPageSize = if (it.size < defaultPageSize) it.size else defaultPageSize

                        endlessScrollListener.setResultsPageSize(resultsPageSize)
                    }

                    endlessScrollListener.setCanLoadMore(uiState.canLoadMore)

                    handleLoading(uiState.isLoading)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { uiEvent ->
                when (uiEvent) {
                    is PostsListViewEvent.DisplayErrorGetPostsEmpty -> {
                        binding.fragmentPostsNoDataLayout.isVisible = true
                    }
                    is PostsListViewEvent.DisplayErrorGetPosts -> {
                        Snackbar.make(binding.root, uiEvent.message, Snackbar.LENGTH_LONG).show()
                    }
                    is PostsListViewEvent.DisplayErrorDelete -> {
                        Snackbar.make(binding.root, uiEvent.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.fragmentPostsLoadingLayout.isVisible = loading
        endlessScrollListener.setIsLoading(loading)
    }

    private fun setUpListeners() {
        with(binding) {
            fragmentPostsEfabDeleteButton.setOnClickListener { deleteAllButFavoritePosts() }
            fragmentPostsEfabLoadFromApiButton.setOnClickListener { loadAllFromApi() }
            fragmentPostsTryAgainButton.setOnClickListener { retryLoading() }
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

    override fun getNextPage() {
        viewModel.getPosts()
    }

    fun deleteAllButFavoritePosts() {
        viewModel.deleteAllButFavorites()
    }

    fun loadAllFromApi() {
        viewModel.getAllPosts()
    }
    fun retryLoading() {
        viewModel.getPosts(retry = true)
    }
}