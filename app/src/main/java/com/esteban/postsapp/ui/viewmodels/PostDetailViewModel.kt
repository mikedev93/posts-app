package com.esteban.postsapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esteban.postsapp.R
import com.esteban.postsapp.domain.model.Comment
import com.esteban.postsapp.domain.model.Post
import com.esteban.postsapp.domain.model.User
import com.esteban.postsapp.domain.repository.PostsRepository
import com.esteban.postsapp.domain.repository.UsersRepository
import com.esteban.postsapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    val postsRepository: PostsRepository,
    val usersRepository: UsersRepository,
    val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailViewState>(PostDetailViewState())
    val uiState: StateFlow<PostDetailViewState> = _uiState

    private val eventChannel = Channel<PostDetailViewEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        val post = savedStateHandle.get<Post>("post")

        post?.let {
            setPost(it)
            getAuthor(it)
            getComments(it)
        }
    }

    fun setPost(post: Post) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _uiState.value = PostDetailViewState(
                post = post,
                isLoading = false,
                hasError = false
            )
        }
    }

    fun getAuthor(post: Post) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = usersRepository.getUser(post.userId)

            when(result) {
                is Resource.Success -> {
                    result.data?.let {
                        _uiState.value = PostDetailViewState(
                            author = it,
                            isLoading = false,
                            hasError = false
                        )
                    }
                }
                is Resource.Error -> {
                    eventChannel.send(PostDetailViewEvent.DisplayError(application.getString(R.string.post_view_model_error_getting_author_comments)))
                    _uiState.value = PostDetailViewState(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
        }
    }

    fun getComments(post: Post) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = postsRepository.getComments(post.id)

            when(result) {
                is Resource.Success -> {
                    result.data?.let {
                        _uiState.value = PostDetailViewState(
                            comments = it,
                            isLoading = false,
                            hasError = false
                        )
                    }
                }
                is Resource.Error -> {
                    eventChannel.send(PostDetailViewEvent.DisplayError(application.getString(R.string.post_view_model_error_getting_author_comments)))
                    _uiState.value = PostDetailViewState(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
        }
    }

    data class PostDetailViewState(
        val post: Post? = null,
        val author: User? = null,
        val comments: List<Comment>? = null,
        val isLoading: Boolean = true,
        val hasError: Boolean = false
    )

    sealed class PostDetailViewEvent {
        data class DisplayError(val message: String) : PostDetailViewEvent()
    }
}