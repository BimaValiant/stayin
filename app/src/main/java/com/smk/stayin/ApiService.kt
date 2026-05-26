package com.smk.stayin

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("hotels")
    fun getHotels(): Call<ResponseBody>
}