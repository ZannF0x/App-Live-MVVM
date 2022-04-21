package com.example.liveapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.liveapp.model.Live
import kotlinx.coroutines.launch
import com.example.liveapp.repository.MainRepository
import java.net.UnknownHostException

class MainViewModel
constructor(private val repository: MainRepository) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
        const val NO_NETWORK = "NO_NETWORK"
        const val GENERIC_ERROR = "GENERIC_ERROR"
        const val NO_ERROR = "NO_ERROR"
    }

    private var _lives = MutableLiveData<List<Live>>()
    val live: LiveData<List<Live>>
        get() = _lives

    private var _error = MutableLiveData("")
    val error: LiveData<String>
        get() = _error

    fun requestAllLives() = viewModelScope.launch{
        Log.d(TAG, "requestAllLives")
        try {
            val result = repository.getAllLives()
            _lives.value = result
            _error.value = NO_ERROR
            Log.d(TAG, "Request Success: list_size=${result.size}")
        } catch (e: Exception) {
            _lives.value = listOf()
            if (e is UnknownHostException) {
                _error.value = NO_NETWORK
            } else {
                _error.value = GENERIC_ERROR
            }
            e.printStackTrace()
            Log.d(TAG, "Request Failure: ERRO=${e.message}")
        }
    }

}