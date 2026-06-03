package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BookingCancelledActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan Activity dengan layout XML cancelled yang sudah lu siapin
        setContentView(R.layout.activity_booking_cancelled)

        // LOGIKA TOMBOL: Kembali ke Halaman Utama (HomeActivity) setelah sukses cancel
        // Pastikan di dalam activity_booking_cancelled.xml lu ada Button dengan id btnBackToHome
        val btnBackToHome = findViewById<Button>(R.id.BtnHome)
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            // Bersihin tumpukan activity lama biar ga numpuk pas back
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}