package com.smk.stayin

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("hotels")
    fun getHotels(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("user/location")
    fun kirimLokasiUser(
        @Header("Authorization") token: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lng: Double
    ): Call<ResponseBody>

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

    @GET("booking")
    fun getDaftarBooking(
        @Header("Authorization") token: String
    ): Call<List<BookingResponse>>

    @DELETE("booking/{id}")
    fun batalkanBooking(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ResponseBody>

    @POST("booking")
    fun buatBooking(
        @Header("Authorization") token: String,
        @Body request: HashMap<String, Any>
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("payment")
    fun bayarBooking(
        @Header("Authorization") token: String,
        @Field("booking_id") bookingId: Int,
        @Field("amount") amount: Int,
        @Field("payment_method") paymentMethod: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("review")
    fun kirimReview(
        @Field("user_name") userName: String,
        @Field("nama_hotel") namaHotel: String,
        @Field("rating") rating: Float,
        @Field("komentar") komentar: String
    ): Call<ResponseBody>
}