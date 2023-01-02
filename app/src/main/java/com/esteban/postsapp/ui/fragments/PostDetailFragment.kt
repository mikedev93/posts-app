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
import com.esteban.postsapp.R
import com.esteban.postsapp.databinding.FragmentPostDetailBinding
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.model.User
import com.esteban.postsapp.ui.viewmodels.PostDetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    lateinit var binding: FragmentPostDetailBinding

    val viewModel: PostDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        subscribeViewModel()
    }

    private fun setUpToolbar() {
        val navController = findNavController()
        binding.fragmentPostDetailToolbar.setupWithNavController(navController)
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle((Lifecycle.State.STARTED)) {
                viewModel.uiState.collect { uiState ->
                    uiState.post?.let {
                        bindPostData(it)
                    }
                    uiState.author?.let {
                        bindAuthorData(it)
                    }
                    uiState.comments?.let {
                        bindCommentsData(it)
                    }

                    handleLoading(uiState.isLoading)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { uiEvent ->
                when (uiEvent) {
                    is PostDetailViewModel.PostDetailViewEvent.DisplayError -> {
                        val snackbar = Snackbar.make(binding.root, uiEvent.message, Snackbar.LENGTH_INDEFINITE)
                        snackbar.setAction(R.string.post_detail_fragment_snackbar_try_again) {
                            val post = arguments?.getParcelable<Post>("post")
                            post?.let {
                                viewModel.getAuthor(it)
                                viewModel.getComments(it)
                            }
                        }
                        snackbar.show()
                    }
                }
            }
        }
    }

    fun bindPostData(post: Post) {
        with(binding) {
            fragmentPostDetailFavoriteIndicator.isVisible = post.isFavorite
            fragmentPostDetailTitle.text = post.title
            fragmentPostDetailBody.text = post.body
        }
    }

    fun bindAuthorData(author: User) {
        binding.fragmentPostDetailAboutAuthorView.user = author
    }

    fun bindCommentsData(comments: List<Comment>) {
        binding.fragmentPostDetailPostCommentsView.setUpView(comments)
    }

    fun handleLoading(loading: Boolean) {
        binding.fragmentPostDetailLoadingLayout.isVisible = loading
    }
}