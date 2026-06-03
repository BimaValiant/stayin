package com.smk.stayin

import com.google.gson.annotations.SerializedName

data class BookingRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("hotel_id")
    val hotel_id: Int,

    @SerializedName("jenis_kamar")
    val jenis_kamar: String,

    @SerializedName("waktu_pemesanan")
    val waktu_pemesanan: String
)