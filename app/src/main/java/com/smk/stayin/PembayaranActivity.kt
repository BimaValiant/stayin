package com.smk.stayin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PembayaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nyambungin ke desain XML pembayaran lu
        setContentView(R.layout.activity_pembayaran)

        // 1. Fungsi Tombol Back (Kiri Atas)
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        btnBack.setOnClickListener {
            finish() // Balik ke halaman sebelumnya (List Hotel)
        }

        // 2. Fungsi Tombol Bayar Sekarang (Bawah)
        val btnBayar = findViewById<Button>(R.id.BtnHasilPembayaran)
        btnBayar.setOnClickListener {
            // Nanti di sini lu bisa arahin ke halaman Payment Success atau ngirim API
            Toast.makeText(this, "Proses Pembayaran Berhasil Disimulasikan!", Toast.LENGTH_SHORT).show()

            // Kalau lu udah ada halaman Sukses, tinggal buka gembok bawah ini:
            // val intent = Intent(this, PaymentSuccessActivity::class.java)
            // startActivity(intent)
            // finish()
        }
    }
}