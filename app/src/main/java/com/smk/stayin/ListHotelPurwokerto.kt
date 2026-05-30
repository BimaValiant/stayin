package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ListHotelPurwokerto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hotel_purwokerto)

        // 1. Logika Tombol Back (Biar bisa balik ke Home)
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        btnBack.setOnClickListener {
            finish() // Perintah 'finish()' otomatis nutup halaman ini dan balik ke sebelumnya
        }
// Kalau diklik yang atas (ID-nya ketuker), kirim data Alun-Alun
        val btnHotelAlunAlun = findViewById<LinearLayout>(R.id.BtnHotelGor) // <- Perhatiin ini
        btnHotelAlunAlun.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            intent.putExtra("NAMA_HOTEL", "StayIn Alun Alun Purwokerto")
            intent.putExtra("HARGA_HOTEL", "Rp 300.000")
            startActivity(intent)
        }

        // Kalau diklik yang bawah, kirim data GOR
        val btnHotelGor = findViewById<LinearLayout>(R.id.BtnHotelAlunAlun) // <- Perhatiin ini
        btnHotelGor.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            intent.putExtra("NAMA_HOTEL", "StayIn GOR Purwokerto")
            intent.putExtra("HARGA_HOTEL", "Rp 350.000")
            startActivity(intent)
        }
    }
}