package com.smk.stayin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // 1. Inisialisasi View/Komponen
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        val btnKirim = findViewById<Button>(R.id.BtnKirimReview)
        val tvNamaHotel = findViewById<TextView>(R.id.tvNamaHotelReview)
        val tvLokasiHotel = findViewById<TextView>(R.id.tvLokasiHotelReview)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBarHotel)
        val etKomentar = findViewById<EditText>(R.id.etKomentarReview)

        // 2. Tangkap Data Hotel dari Intent Lemparan (Biar Fleksibel)
        val namaHotel = intent.getStringExtra("NAMA_HOTEL") ?: "StayIn Hotel"
        val lokasiHotel = intent.getStringExtra("LOKASI_HOTEL") ?: "📍 Indonesia"

        // Tempel data ke layar
        tvNamaHotel.text = namaHotel
        tvLokasiHotel.text = lokasiHotel

        // 3. Fungsi Tombol Back
        btnBack.setOnClickListener {
            finish()
        }

        // 4. Fungsi Tombol Kirim Ulasan
        btnKirim.setOnClickListener {
            val jumlahBintang = ratingBar.rating.toInt()
            val komentarUser = etKomentar.text.toString().trim()

            if (komentarUser.isEmpty()) {
                Toast.makeText(this, "Tolong isi ulasan tulisan lu dulu, bro!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulasi Berhasil (Nanti bagian ini bisa kalian hubungkan ke ApiClient Laravel)
            val pesanSukses = "Review $namaHotel dengan $jumlahBintang Bintang Berhasil Dikirim!"
            Toast.makeText(this, pesanSukses, Toast.LENGTH_LONG).show()

            // Tutup halaman review setelah berhasil kirim
            finish()
        }
    }
}