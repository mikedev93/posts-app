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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    val repository: PostsRepository
): ViewModel() {

    var page: Int = 0
    var nextPage: Int? = null
    var totalPages: Int? = null
    var canLoadMore: Boolean = true

    private val _postsState = MutableLiveData<Resource<List<Post>?>>()
    val postsState: LiveData<Resource<List<Post>?>>
        get() = _postsState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            page++
            handleResponse(repository.getPosts(page))
            _isLoading.value = false
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            handleResponse(repository.getPosts())
            _isLoading.value = false
        }
    }

    fun handleResponse(response: PaginatedResponse) {
        _postsState.value = response.data
        nextPage = response.nextPage
        totalPages = response.lastPage

        response.lastPage?.let {
            canLoadMore = page < it
        } ?: run {
            canLoadMore = false
        }
    }
}