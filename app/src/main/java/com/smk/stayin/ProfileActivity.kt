package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 1. Tombol Home (ID-nya disesuaikan jadi BtnHotel sesuai XML lu)
        val btnHome = findViewById<ImageView>(R.id.BtnHotel)
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // 2. Tombol Booking
        val btnBooking = findViewById<ImageView>(R.id.BtnBooking)
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // 3. Tombol Keluar (Logout)
        val btnKeluar = findViewById<LinearLayout>(R.id.BtnKeluar)
        btnKeluar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // Trik Pro: Hapus semua history halaman, biar pas udah logout gak bisa pencet tombol Back ke Profile lagi
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}