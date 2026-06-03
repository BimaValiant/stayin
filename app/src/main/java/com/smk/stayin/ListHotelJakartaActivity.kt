package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout // <-- Balik pakai LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ListHotelJakartaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hotel_jakarta)

        // 1. Fungsi Tombol Back
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // 2. Fungsi Klik Hotel 1 (StayIn Senayan)
        val btnHotelJkt1 = findViewById<LinearLayout>(R.id.BtnHotelJakarta1)
        btnHotelJkt1.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            startActivity(intent)
        }

        // 3. Fungsi Klik Hotel 2 (StayIn Kelapa Gading)
        val btnHotelJkt2 = findViewById<LinearLayout>(R.id.BtnHotelJakarta2)
        btnHotelJkt2.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            startActivity(intent)
        }
    }
}