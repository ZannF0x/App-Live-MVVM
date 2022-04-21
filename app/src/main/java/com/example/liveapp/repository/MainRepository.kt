package com.example.liveapp.repository

import com.example.liveapp.service.RetrofitService
import retrofit2.await

class MainRepository
constructor (private val retrofitService: RetrofitService) {

    suspend fun getAllLives() = retrofitService.getAllLives().await()

}