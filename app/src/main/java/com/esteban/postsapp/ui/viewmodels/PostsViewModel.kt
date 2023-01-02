package com.esteban.postsapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esteban.postsapp.R
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.repository.PostsRepository
import com.esteban.postsapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    val repository: PostsRepository,
    val application: Application
) : ViewModel() {

    var pageToQuery: Int = 0
    var nextPage: Int? = null
    var totalPages: Int? = null
    var canLoadMore: Boolean = true

    private val _uiState = MutableStateFlow<PostsListViewState>(PostsListViewState())
    val uiState: StateFlow<PostsListViewState> = _uiState

    private val eventChannel = Channel<PostsListViewEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        getPosts()
    }

    fun getPosts(retry: Boolean = false) {
        viewModelScope.launch {
            if (canLoadMore) {
                if (retry) {
                    _uiState.value = _uiState.value.copy(
                        posts = null,
                        isLoading = true
                    )
                } else {
                    pageToQuery++
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }

                val resultsPerPage = application.resources.getInteger(R.integer.config_query_page_size)

                val result = repository.getPosts(pageToQuery, resultsPerPage)
                nextPage = result.nextPage
                totalPages = result.lastPage

                totalPages?.let {
                    canLoadMore = pageToQuery < it
                } ?: run {
                    canLoadMore = false
                }

                result.posts?.let { posts ->
                    when (posts) {
                        is Resource.Success -> {
                            posts.data?.let { handleResponse(it, canLoadMore) }
                        }
                        is Resource.Error -> {
                            canLoadMore = true

                            if (!_uiState.value.posts.isNullOrEmpty()) {
                                eventChannel.send(PostsListViewEvent.DisplayErrorGetPosts(application.getString(R.string.posts_view_model_error_loading_posts)))
                            } else {
                                eventChannel.send(PostsListViewEvent.DisplayErrorGetPostsEmpty)
                            }
                            _uiState.value = PostsListViewState(
                                isLoading = false,
                                hasError = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.getPosts()
            result.posts?.let { posts ->
                when (posts) {
                    is Resource.Success -> {
                        posts.data?.let { handleResponse(it) }
                    }
                    is Resource.Error -> {
                        if (!_uiState.value.posts.isNullOrEmpty()) {
                            eventChannel.send(PostsListViewEvent.DisplayErrorGetPosts(application.getString(R.string.posts_view_model_error_loading_posts)))

                            _uiState.value = PostsListViewState(
                                isLoading = false,
                                hasError = true
                            )
                        } else {
                            eventChannel.send(PostsListViewEvent.DisplayErrorGetPostsEmpty)

                            _uiState.value = PostsListViewState(
                                posts = null,
                                isLoading = false,
                                hasError = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteAllButFavorites() {
        var currentPosts = _uiState.value.posts

        currentPosts?.let { posts -> currentPosts = posts.filter { it.isFavorite }.toMutableList() }

        _uiState.value = PostsListViewState(
            posts = currentPosts,
            isLoading = false,
            hasError = false
        )
    }

    fun handleResponse(response: MutableList<Post>, canLoadMore: Boolean = false) {
        val currentPosts = _uiState.value.posts ?: mutableListOf()

        currentPosts.addAll(response)

        _uiState.value = PostsListViewState(
            posts = currentPosts,
            isLoading = false,
            canLoadMore = canLoadMore,
            hasError = false
        )
    }

    fun setFavoritePost(position: Int) {
        viewModelScope.launch {
            val posts = _uiState.value.posts
            posts?.let { it[position].isFavorite = !it[position].isFavorite }

            val sortedList = posts?.sortedWith(compareBy({ !it.isFavorite }, { it.id }))?.toMutableList()

            _uiState.value = PostsListViewState(
                posts = sortedList,
                isLoading = false,
                hasError = false
            )
        }
    }

    fun deletePost(position: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val posts = _uiState.value.posts
            val postId = posts?.get(position)?.id ?: 0

            val result = repository.deletePost(postId)
            when (result) {
                is Resource.Success -> {
                    val filteredList = posts?.filter { it.id != postId }?.toMutableList()

                    _uiState.value = PostsListViewState(
                        posts = filteredList,
                        isLoading = false,
                        hasError = false
                    )
                }
                is Resource.Error -> {
                    eventChannel.send(PostsListViewEvent.DisplayErrorDelete(application.getString(R.string.posts_view_model_error_deleting_posts)))
                    _uiState.value = PostsListViewState(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
        }
    }

    data class PostsListViewState(
        val posts: MutableList<Post>? = null,
        val isLoading: Boolean = true,
        val canLoadMore: Boolean = true,
        val hasError: Boolean = false
    )

    sealed class PostsListViewEvent {
        object DisplayErrorGetPostsEmpty : PostsListViewEvent()
        data class DisplayErrorGetPosts(val message: String) : PostsListViewEvent()
        data class DisplayErrorDelete(val message: String) : PostsListViewEvent()
    }
}