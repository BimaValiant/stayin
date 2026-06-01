package com.smk.stayin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // ==========================================
        // 1. LOGIC TOMBOL NAVIGASI BAWAAN (AWAL)
        // ==========================================
        val btnBooking = findViewById<ImageView>(R.id.BtnBooking)
        btnBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        val btnProfile = findViewById<ImageView>(R.id.BtnProfile)
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Tombol Destinasi Favorit Grid Bawah
        val btnHotelAlunAlun = findViewById<CardView>(R.id.BtnHotelAlunAlun)
        btnHotelAlunAlun.setOnClickListener {
            startActivity(Intent(this, ListHotelPurwokerto::class.java))
        }

        val btnPilihHotelJkt = findViewById<CardView>(R.id.BtnPilihHotelJkt)
        btnPilihHotelJkt.setOnClickListener {
            startActivity(Intent(this, ListHotelJakartaActivity::class.java))
        }

        // ==========================================
        // 2. JALANKAN GPS UNTUK REKOMENDASI HORIZONTAL
        // ==========================================
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                101
            )
        } else {
            getDeviceLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation()
        } else {
            // Default pusat Purwokerto kalau user nolak GPS
            ambilDataHotelBerdasarkanLokasi(-7.4243, 109.2302)
        }
    }

    private fun getDeviceLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    ambilDataHotelBerdasarkanLokasi(location.latitude, location.longitude)
                } else {
                    ambilDataHotelBerdasarkanLokasi(-7.4243, 109.2302)
                }
            }
        } catch (e: SecurityException) {
            Log.e("STAYIN_GPS_ERROR", e.message.toString())
        }
    }

    // ==========================================
    // 3. SINKRONISASI REKOMENDASI BY ID DI TEMPAT
    // ==========================================
    private fun ambilDataHotelBerdasarkanLokasi(latitude: Double, longitude: Double) {
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""
        if (tokenMentah.isEmpty()) return

        val tokenLengkap = "Bearer $tokenMentah"

        ApiClient.instance.kirimLokasiUser(tokenLengkap, latitude, longitude)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        try {
                            val jsonStr = response.body()?.string()
                            val jsonObject = JSONObject(jsonStr)
                            val dataObj = jsonObject.getJSONObject("data")

                            // Ambil nama kota terdekat dari deteksi backend Laravel
                            val kotaTerdekat = dataObj.optString("kota_terdekat", "Purwokerto")

                            // SIKAT ID REKOMENDASI UTAMA & PAKSA VISIBLE
                            findViewById<TextView>(R.id.tvTitleRekomendasi).text = "Rekomendasi Terdekat di $kotaTerdekat 📍"
                            findViewById<android.widget.HorizontalScrollView>(R.id.scrollRekomendasiHorizontal).visibility = View.VISIBLE

                            // SET ISI TIAP-TIAP CARD REKOMENDASI YANG MIRING KE SAMPING BERDASARKAN KOTA
                            if (kotaTerdekat.equals("Purwokerto", ignoreCase = true)) {

                                // Card 1 (Purwokerto Alun-Alun)
                                findViewById<TextView>(R.id.tvNamaReko1).text = "StayIn Alun Alun Purwokerto"
                                findViewById<TextView>(R.id.tvRatingReko1).text = "★ 4.6"
                                findViewById<TextView>(R.id.tvAlamatReko1).text = "Jl. Jend. Sudirman, Purwokerto"
                                findViewById<TextView>(R.id.tvHargaReko1).text = "Rp 300.000"
                                findViewById<ImageView>(R.id.ivHotelImg1).setImageResource(R.drawable.purwokertohome)

                                // Card 2 (Purwokerto GOR)
                                findViewById<TextView>(R.id.tvNamaReko2).text = "StayIn GOR Purwokerto"
                                findViewById<TextView>(R.id.tvRatingReko2).text = "★ 4.3"
                                findViewById<TextView>(R.id.tvAlamatReko2).text = "Jl. Prof. Dr. Suharso, Purwokerto"
                                findViewById<TextView>(R.id.tvHargaReko2).text = "Rp 350.000"
                                findViewById<ImageView>(R.id.ivHotelImg2).setImageResource(R.drawable.purwokertohome)

                                // Klik Card miringnya langsung oper ke Halaman List Purwokerto
                                val intentPwt = Intent(this@HomeActivity, ListHotelPurwokerto::class.java)
                                findViewById<CardView>(R.id.cardReko1).setOnClickListener { startActivity(intentPwt) }
                                findViewById<CardView>(R.id.cardReko2).setOnClickListener { startActivity(intentPwt) }

                            } else if (kotaTerdekat.equals("Jakarta", ignoreCase = true)) {

                                // Card 1 (Jakarta Senayan)
                                findViewById<TextView>(R.id.tvNamaReko1).text = "StayIn Senayan Jakarta"
                                findViewById<TextView>(R.id.tvRatingReko1).text = "★ 4.8"
                                findViewById<TextView>(R.id.tvAlamatReko1).text = "Gelora, Tanah Abang, Jakarta Pusat"
                                findViewById<TextView>(R.id.tvHargaReko1).text = "Rp 550.000"
                                findViewById<ImageView>(R.id.ivHotelImg1).setImageResource(R.drawable.jakartahome)

                                // Card 2 (Jakarta Gading)
                                findViewById<TextView>(R.id.tvNamaReko2).text = "StayIn Kelapa Gading"
                                findViewById<TextView>(R.id.tvRatingReko2).text = "★ 4.5"
                                findViewById<TextView>(R.id.tvAlamatReko2).text = "Kelapa Gading, Jakarta Utara"
                                findViewById<TextView>(R.id.tvHargaReko2).text = "Rp 490.000"
                                findViewById<ImageView>(R.id.ivHotelImg2).setImageResource(R.drawable.jakartahome)

                                // Klik Card miringnya langsung oper ke Halaman List Jakarta
                                val intentJkt = Intent(this@HomeActivity, ListHotelJakartaActivity::class.java)
                                findViewById<CardView>(R.id.cardReko1).setOnClickListener { startActivity(intentJkt) }
                                findViewById<CardView>(R.id.cardReko2).setOnClickListener { startActivity(intentJkt) }
                            }

                        } catch (e: Exception) {
                            Log.e("PARSING_ERR", e.message.toString())
                        }
                    } else {
                        Log.e("API_HOME_ERR", "Gagal respon backend: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_HOME_FAILURE", t.message.toString())
                }
            })
    }
}