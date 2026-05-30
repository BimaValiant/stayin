package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Tombol Purwokerto (Kodingan lu yang udah bener)
        val btnPilihHotelPwt = findViewById<LinearLayout>(R.id.BtnHotelAlunAlun)
        btnPilihHotelPwt.setOnClickListener {
            val intent = Intent(this, ListHotelPurwokerto::class.java)
            startActivity(intent)
        }

        // 2. TAMBAHIN INI: Tombol Jakarta
        val btnPilihHotelJkt = findViewById<LinearLayout>(R.id.BtnPilihHotelJkt)
        btnPilihHotelJkt.setOnClickListener {
            val intent = Intent(this, ListHotelJakartaActivity::class.java)
            startActivity(intent)
        }

        // 1. Tombol Booking di Bottom Nav
        val btnBooking = findViewById<ImageView>(R.id.BtnBooking)
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        // 2. Tombol Profile di Bottom Nav
        val btnProfile = findViewById<ImageView>(R.id.BtnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}