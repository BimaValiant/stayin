package com.smk.stayin

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("hotels")
    fun getHotels(): Call<ResponseBody>

    // --- PASTIKAN BARIS DI BAWAH INI SUDAH ADA DAN SAMA ---
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    // -- fungsi login
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseBody>
    @GET("user")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ResponseBody>
}