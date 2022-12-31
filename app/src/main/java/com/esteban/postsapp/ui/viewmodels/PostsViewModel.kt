package com.esteban.postsapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esteban.postsapp.domain.model.PaginatedResponse
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.repository.PostsRepository
import com.esteban.postsapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.FieldPosition
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    val repository: PostsRepository
) : ViewModel() {

    private val resultsPerPage: Int = 20

    var page: Int = 0
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

    fun getPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            page++

            val result = repository.getPosts(page, resultsPerPage)
            nextPage = result.nextPage
            totalPages = result.lastPage

            totalPages?.let {
                canLoadMore = page < it
            } ?: run {
                canLoadMore = false
            }

            result.posts?.let { posts ->
                when (posts) {
                    is Resource.Success -> {
                        posts.data?.let { handleResponse(it) }
                    }
                    is Resource.Error -> {
                        eventChannel.send(PostsListViewEvent.DisplayError)
                        _uiState.value = PostsListViewState(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(posts = null, isLoading = true)

            val result = repository.getPosts()
            result.posts?.let { posts ->
                when (posts) {
                    is Resource.Success -> {
                        posts.data?.let { handleResponse(it) }
                    }
                    is Resource.Error -> {
                        eventChannel.send(PostsListViewEvent.DisplayError)
                        _uiState.value = PostsListViewState(
                            isLoading = false,
                            hasError = true
                        )
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

    fun handleResponse(response: MutableList<Post>) {
        val currentPosts = _uiState.value.posts ?: mutableListOf()

        currentPosts.addAll(response)

        _uiState.value = PostsListViewState(
            posts = currentPosts,
            isLoading = false,
            hasError = false
        )
    }

    fun setFavoritePost(position: Int) {
        viewModelScope.launch {
            var posts = _uiState.value.posts

            posts?.let { it[position].isFavorite = !it[position].isFavorite }
            posts = posts?.sortedWith(compareBy({ !it.isFavorite }, { it.id }))?.toMutableList()

            _uiState.value = PostsListViewState(
                posts = posts,
                isLoading = false,
                hasError = false
            )
        }
    }

    fun deletePost(position: Int) {
        viewModelScope.launch {
            var posts = _uiState.value.posts

            posts?.removeAt(position)

            _uiState.value = PostsListViewState(
                posts = posts,
                isLoading = false,
                hasError = false
            )
        }
    }

    data class PostsListViewState(
        val posts: MutableList<Post>? = null,
        val isLoading: Boolean = true,
        val hasError: Boolean = false
    )

    sealed class PostsListViewEvent {
        object DisplayError : PostsListViewEvent()
    }
}