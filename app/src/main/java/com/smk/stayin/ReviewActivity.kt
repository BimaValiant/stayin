package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // 1. Inisialisasi View/Komponen (Sesuai XML Bagus Lu)
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        val btnKirim = findViewById<Button>(R.id.BtnKirimReview)
        val tvNamaHotel = findViewById<TextView>(R.id.tvNamaHotelReview)
        val tvLokasiHotel = findViewById<TextView>(R.id.tvLokasiHotelReview)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBarHotel)
        val etKomentar = findViewById<EditText>(R.id.etKomentarReview)

        // 2. Tangkap Data Hotel dari Intent Lemparan PembayaranActivity
        val namaHotel = intent.getStringExtra("NAMA_HOTEL") ?: "StayIn Alun Alun Purwokerto"
        val lokasiHotel = intent.getStringExtra("LOKASI_HOTEL") ?: "📍 Purwokerto"
        val userName = intent.getStringExtra("USER_NAME") ?: "Pelanggan StayIn"

        // Tempel data ke layar biar estetik
        tvNamaHotel.text = namaHotel
        tvLokasiHotel.text = lokasiHotel

        // 3. Fungsi Tombol Back
        btnBack.setOnClickListener {
            finish()
        }

        // 4. Fungsi Tombol Kirim Ulasan Real API Laravel
        btnKirim.setOnClickListener {
            val jumlahBintang = ratingBar.rating
            val komentarUser = etKomentar.text.toString().trim()

            if (komentarUser.isEmpty()) {
                Toast.makeText(this, "Tolong beri kami ulasan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔥 TEMBAK API REAL KE LARAVEL REVIEWCONTROLLER
            ApiClient.instance.kirimReview(userName, namaHotel, jumlahBintang, komentarUser)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ReviewActivity, "Terima kasih atas review anda", Toast.LENGTH_LONG).show()

                            // Sukses total, lempar ke BookingActivity biar user bisa lihat list riwayatnya
                            val intent = Intent(this@ReviewActivity, BookingActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("REVIEW_ERR", "Gagal response code: ${response.code()}")
                            Toast.makeText(this@ReviewActivity, "Ditolak Laravel: Code ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("REVIEW_ERR", "Koneksi mati: ${t.message}")
                        Toast.makeText(this@ReviewActivity, "Koneksi Backend Putus/Gagal!", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}