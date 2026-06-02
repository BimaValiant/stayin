package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ListHotelSoloActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nyambungin ke XML desain Solo
        setContentView(R.layout.activity_list_hotel_solo)

        // 1. Fungsi Tombol Back (Kiri Atas)
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        btnBack.setOnClickListener {
            finish() // Langsung nutup halaman dan balik ke Home
        }

        // 2. Fungsi Klik Hotel 1 (Slamet Riyadi) -> Pindah ke halaman Pesan
        val btnHotelSlamet = findViewById<LinearLayout>(R.id.btnHotelSlamet)
        btnHotelSlamet.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            startActivity(intent)
        }

        // 3. Fungsi Klik Hotel 2 (Manahan) -> Pindah ke halaman Pesan
        val btnHotelManahan = findViewById<LinearLayout>(R.id.btnHotelManahan)
        btnHotelManahan.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            startActivity(intent)
        }
    }
}