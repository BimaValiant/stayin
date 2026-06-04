package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingActivity : AppCompatActivity() {

    private lateinit var rvBooking: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter
    private var tokenLengkap: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // 1. Ambil Token Sesi Login
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""
        tokenLengkap = "Bearer $tokenMentah"

        // 2. Setup RecyclerView
        rvBooking = findViewById(R.id.rvBooking)
        rvBooking.layoutManager = LinearLayoutManager(this)

        bookingAdapter = BookingAdapter(arrayListOf()) { itemBooking ->
            // AKSI KLIK TOMBOL BATALKAN BOOKING
            prosesBatalkanBookingKeLaravel(itemBooking.id)
        }
        rvBooking.adapter = bookingAdapter

        // 3. Ambil Data Real dari MySQL Backend
        if (tokenMentah.isNotEmpty()) {
            muatDataBookingDariDatabase()
        } else {
            Toast.makeText(this, "Sesi habis, Silahkan login ulang!", Toast.LENGTH_SHORT).show()
        }

        // 4. Logika Pindah Tab Bar Navigasi Bawah
        findViewById<LinearLayout>(R.id.LayoutHome).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.LayoutProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun muatDataBookingDariDatabase() {
        ApiClient.instance.getDaftarBooking(tokenLengkap)
            .enqueue(object : Callback<List<BookingResponse>> {
                override fun onResponse(call: Call<List<BookingResponse>>, response: Response<List<BookingResponse>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val dataDariDatabase = response.body()!!
                        bookingAdapter.updateData(dataDariDatabase)
                    } else {
                        Log.e("GET_ERR", "Gagal tarik data: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<BookingResponse>>, t: Throwable) {
                    Toast.makeText(this@BookingActivity, "Koneksi internet jelek!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun prosesBatalkanBookingKeLaravel(bookingId: Int) {
        ApiClient.instance.batalkanBooking(tokenLengkap, bookingId)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@BookingActivity, "Booking dibatalkan!", Toast.LENGTH_SHORT).show()

                        // PINDAH KE HALAMAN CANCELLED Sesuai Request Lu!
                        val intent = Intent(this@BookingActivity, BookingCancelledActivity::class.java)
                        startActivity(intent)
                        finish() // Tutup halaman booking lama
                    } else {
                        Toast.makeText(this@BookingActivity, "Gagal membatalkan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@BookingActivity, "Koneksi terputus!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}