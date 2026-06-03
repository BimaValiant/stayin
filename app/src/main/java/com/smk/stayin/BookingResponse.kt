package com.smk.stayin

data class BookingResponse(
    val id: Int,
    val email: String,
    val hotel_id: Int,
    val jenis_kamar: String,
    val waktu_pemesanan: String,
    val status: String
)