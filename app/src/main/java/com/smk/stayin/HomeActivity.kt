package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout // Memanggil library LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Kenalan dengan LinearLayout menggunakan tipe data LinearLayout
        val btnPilihHotelPwt = findViewById<LinearLayout>(R.id.BtnHotelAlunAlun)

        // 2. Beri aksi klik agar ketika kotak LinearLayout ditekan, dia langsung pindah halaman
        btnPilihHotelPwt.setOnClickListener {
            val intent = Intent(this, ListHotelPurwokerto::class.java)
            startActivity(intent)
        }
    }
}