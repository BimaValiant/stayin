package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout // Atau sesuaikan jenis komponennya (CardView/LinearLayout)
import androidx.appcompat.app.AppCompatActivity

class ListHotelPurwokerto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hotel) // Sesuaikan dengan nama XML list hotelmu

        // 1. Kenalan dengan tombol Hotel Alun-Alun
        // Ganti 'LinearLayout' menjadi 'CardView' jika di XML kamu memakai CardView
        val btnHotelAlunAlun = findViewById<LinearLayout>(R.id.BtnHotelAlunAlun)

        // 2. Beri aksi klik untuk menuju ke halaman Booking
        btnHotelAlunAlun.setOnClickListener {
            // Berpindah dari halaman list menuju ke kelas BookingActivity
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }
    }
}