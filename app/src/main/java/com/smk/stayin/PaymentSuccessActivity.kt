package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PaymentSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        // 1. Hubungkan tombol dari XML
        val btnKeReview = findViewById<Button>(R.id.btnKeReview)

        // 2. Beri perintah ketika tombol diklik
        btnKeReview.setOnClickListener {
            // Berpindah ke ReviewActivity
            val intent = Intent(this, ReviewActivity::class.java)

            // (Opsional) Mengoper kembali data nama hotel jika halaman review butuh
            val namaHotel = intent.getStringExtra("NAMA_HOTEL")
            intent.putExtra("NAMA_HOTEL", namaHotel)

            startActivity(intent)

            // Tutup halaman sukses ini agar user tidak bisa back ke halaman ini lagi
            finish()
        }
    }
}