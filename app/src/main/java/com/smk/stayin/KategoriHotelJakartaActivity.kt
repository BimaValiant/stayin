package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class KategoriHotelJakartaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_hotel_jakarta)

        val btnBudget = findViewById<Button>(R.id.BtnKategoriBudget)
        val btnLuxury = findViewById<Button>(R.id.BtnKategoriLuxury)

        btnBudget.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("JENIS_KAMAR", "Smart Budget Room")
            intent.putExtra("HARGA_MENTAH", 580000) // Dikirim dalam bentuk angka Int
            startActivity(intent)
        }

        btnLuxury.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("JENIS_KAMAR", "Smart Luxury Room") // Diubah biar beda dengan budget
            intent.putExtra("HARGA_MENTAH", 749000) // Dikirim dalam bentuk angka Int
            startActivity(intent)
        }
    }
}