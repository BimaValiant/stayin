package com.smk.stayin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListHotelBandungActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hotel_bandung)

        // 1. Tombol Back Kembali ke Home (Sesuai ID di XML: btnBack)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // ==========================================
        // FITUR BARU: KLIK CARD HOTEL KE PEMBAYARAN
        // ==========================================

        // Hotel 1: The Trans Luxury Hotel (Sesuai XML)
        val btnHotelTrans = findViewById<LinearLayout>(R.id.btnHotelTrans)
        btnHotelTrans.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            intent.putExtra("HOTEL_ID", 4)
            intent.putExtra("NAMA_HOTEL", "The Trans Luxury StayIn Hotel") // Nama dari XML
            intent.putExtra("HARGA_HOTEL", "Rp 1.250.000")                 // Harga dari XML
            startActivity(intent)
        }

        // Hotel 2: Padma Hotel (Sesuai XML)
        val btnHotelPadma = findViewById<LinearLayout>(R.id.btnHotelPadma)
        btnHotelPadma.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            intent.putExtra("HOTEL_ID", 5)
            intent.putExtra("NAMA_HOTEL", "StayIn Padma")                  // Nama dari XML
            intent.putExtra("HARGA_HOTEL", "Rp 950.000")                   // Harga dari XML
            startActivity(intent)
        }
        // ==========================================

        // 2. Inisialisasi Google Play Services Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 3. Jalankan ritual pengecekan izin GPS HP
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
            Toast.makeText(this, "Membutuhkan izin lokasi!", Toast.LENGTH_LONG).show()
            // Default koordinat Alun-Alun Bandung
            kirimKoordinatKeLaravel(-6.9214, 107.6071)
        }
    }

    private fun getDeviceLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    Log.d("STAYIN_GPS", "Koordinat HP Lu: $lat, $lng")
                    kirimKoordinatKeLaravel(lat, lng)
                } else {
                    kirimKoordinatKeLaravel(-6.9214, 107.6071)
                }
            }
        } catch (e: SecurityException) {
            Log.e("STAYIN_GPS_ERROR", e.message.toString())
        }
    }

    private fun kirimKoordinatKeLaravel(latitude: Double, longitude: Double) {
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""

        if (tokenMentah.isEmpty()) {
            Toast.makeText(this, "Sesi login habis, silakan login ulang!", Toast.LENGTH_SHORT).show()
            return
        }

        val tokenLengkap = "Bearer $tokenMentah"

        ApiClient.instance.kirimLokasiUser(tokenLengkap, latitude, longitude)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val jsonStr = response.body()?.string()
                        Log.d("SINKRON_LOKASI_OK", jsonStr.toString())
                        Toast.makeText(this@ListHotelBandungActivity, "Rekomendasi hotel terdekat siap!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API_ERROR", "Respon gagal: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_FAILURE", "Gagal koneksi server: ${t.message}")
                }
            })
    }
}