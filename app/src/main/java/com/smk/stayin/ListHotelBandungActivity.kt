package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ListHotelBandungActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nyambungin ke XML desain Bandung lu
        setContentView(R.layout.activity_list_hotel_bandung)

        // 1. Fungsi Tombol Back (Kiri Atas)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Nutup halaman ini dan balik ke Home
        }

        // 2. Fungsi Tombol Klik Hotel (Pindah ke halaman Pesan)
        // (Pastikan ID btnHotelTrans udah lu tambahin di XML sesuai instruksi di atas)
        val btnHotelTrans = findViewById<LinearLayout>(R.id.btnHotelTrans)
        btnHotelTrans.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            startActivity(intent)
        }
    }
}