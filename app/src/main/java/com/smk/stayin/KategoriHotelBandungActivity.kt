package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class KategoriHotelBandungActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Menghubungkan ke layout XML
        setContentView(R.layout.activity_kategori_hotel_bandung)

        // 2. Inisialisasi tombol dari XML
        val btnBudget = findViewById<Button>(R.id.BtnKategoriBudget)
        val btnLuxury = findViewById<Button>(R.id.BtnKategoriLuxury) // Pastikan ID di XML tombol kedua adalah BtnKategoriLuxury


        btnBudget.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("JENIS_KAMAR", "Smart Budget Room")
            intent.putExtra("HARGA", 510000)
            startActivity(intent)
        }


        btnLuxury.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("JENIS_KAMAR", "Smart Budget Room")
            intent.putExtra("HARGA", 700000) // Silakan sesuaikan harganya dengan desain Anda
            startActivity(intent)
        }
    }
}