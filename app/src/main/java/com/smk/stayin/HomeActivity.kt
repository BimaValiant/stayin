package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo // Ini penting buat ngebaca tombol Enter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Tombol Purwokerto
        val btnPilihHotelPwt = findViewById<CardView>(R.id.BtnHotelAlunAlun)
        btnPilihHotelPwt.setOnClickListener {
            val intent = Intent(this, ListHotelPurwokerto::class.java)
            startActivity(intent)
        }

        // 2. Tombol Jakarta
        val btnPilihHotelJkt = findViewById<CardView>(R.id.BtnPilihHotelJkt)
        btnPilihHotelJkt.setOnClickListener {
            val intent = Intent(this, ListHotelJakartaActivity::class.java)
            startActivity(intent)
        }

        // 3. Tombol Booking di Bottom Nav
        val btnBooking = findViewById<ImageView>(R.id.BtnBooking)
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        // 4. Tombol Profile di Bottom Nav
        val btnProfile = findViewById<ImageView>(R.id.BtnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // 5. FITUR BARU: Search Bar Dinamis (Udah di-upgrade Anti-Gagal)
        val searchBar = findViewById<EditText>(R.id.etSearchCity)
        val btnSearch = findViewById<ImageView>(R.id.btnSearchIcon)

        // Bikin mesinnya dulu
        fun prosesPencarian() {
            val keyword = searchBar.text.toString().trim().lowercase()

            when {
                keyword.contains("purwokerto") -> {
                    startActivity(Intent(this@HomeActivity, ListHotelPurwokerto::class.java))
                }
                keyword.contains("jakarta") -> {
                    startActivity(Intent(this@HomeActivity, ListHotelJakartaActivity::class.java))
                }
                keyword.isEmpty() -> {
                    Toast.makeText(this@HomeActivity, "Ketik nama kota dulu, bro!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@HomeActivity, "Waduh, kota belum tersedia nih.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Kalau lu klik ikon kaca pembesar
        btnSearch.setOnClickListener {
            prosesPencarian()
        }

        // Kalau lu pencet tombol ENTER/SEARCH di keyboard HP lu
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_NULL) {

                prosesPencarian()
                true
            } else {
                false
            }
        }

        // 6. FITUR FILTER KATEGORI (Biar nggak cuma pajangan)
        val btnPopuler = findViewById<CardView>(R.id.btnFilterPopuler)
        val btnPromo = findViewById<CardView>(R.id.btnFilterPromo)
        val btnHemat = findViewById<CardView>(R.id.btnFilterHemat)

        btnPopuler.setOnClickListener {
            Toast.makeText(this, "Menampilkan daftar hotel Paling Populer 🌟", Toast.LENGTH_SHORT).show()
        }

        btnPromo.setOnClickListener {
            Toast.makeText(this, "Mencari promo diskon terbesar hari ini 🔥", Toast.LENGTH_SHORT).show()
        }

        btnHemat.setOnClickListener {
            Toast.makeText(this, "Menampilkan penginapan pas di kantong 💰", Toast.LENGTH_SHORT).show()
        }
    }
}