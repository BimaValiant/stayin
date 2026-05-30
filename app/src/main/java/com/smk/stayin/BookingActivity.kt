package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // 1. Tombol Home (Biar bisa balik ke halaman utama)
        val btnHome = findViewById<ImageView>(R.id.BtnHome)
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            // Trik Pro: FLAG ini biar halaman homenya gak numpuk berulang-ulang di memori HP
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // 2. Tombol Profile (Biar dari booking bisa langsung loncat ke Profile)
        val btnProfile = findViewById<ImageView>(R.id.BtnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}