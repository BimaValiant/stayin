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

class ListHotelPurwokerto : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hotel_purwokerto)

        // 1. Tombol Back Kembali ke Home
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // ==========================================
        // FITUR: KLIK CARD HOTEL KE PEMBAYARAN
        // ==========================================

        // Hotel 1: GOR Purwokerto (Pastikan R.id.BtnHotelGor sesuai dengan XML kamu)
        val btnHotelGor = findViewById<LinearLayout>(R.id.BtnHotelGor)
        btnHotelGor.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            intent.putExtra("HOTEL_ID", 2)
            intent.putExtra("NAMA_HOTEL", "StayIn GOR Purwokerto")
            intent.putExtra("HARGA_HOTEL", "Rp 350.000")
            startActivity(intent)
        }

        // Hotel 2: Alun Alun Purwokerto (Pastikan R.id.BtnHotelAlunAlun sesuai dengan XML kamu)
        val btnHotelAlunAlun = findViewById<LinearLayout>(R.id.BtnHotelAlunAlun)
        btnHotelAlunAlun.setOnClickListener {
            val intent = Intent(this, PesanHotelActivity::class.java)
            intent.putExtra("HOTEL_ID", 3)
            intent.putExtra("NAMA_HOTEL", "StayIn Alun Alun Purwokerto")
            intent.putExtra("HARGA_HOTEL", "Rp 300.000")
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

            // Munculkan pop-up izin lokasi di HP
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                101
            )
        } else {
            // Jika sudah diizinkan, langsung ambil koordinatnya
            getDeviceLocation()
        }
    }

    // Menangkap respon klik dari dialog izin lokasi
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation()
        } else {
            Toast.makeText(this, "Membutuhkan izin lokasi!", Toast.LENGTH_LONG).show()
            // Default pakai koordinat pusat Purwokerto kalau user menolak akses GPS
            kirimKoordinatKeLaravel(-7.4243, 109.2302)
        }
    }

    private fun getDeviceLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    Log.d("STAYIN_GPS", "Koordinat HP Lu: $lat, $lng")

                    // Kirim ke database Laravel via Ngrok
                    kirimKoordinatKeLaravel(lat, lng)
                } else {
                    // Jikalau GPS HP mati atau null, set default Purwokerto
                    kirimKoordinatKeLaravel(-7.4243, 109.2302)
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

        // Tembak API Laravel
        ApiClient.instance.kirimLokasiUser(tokenLengkap, latitude, longitude)
            .enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val jsonStr = response.body()?.string()
                        Log.d("SINKRON_LOKASI_OK", jsonStr.toString())

                        Toast.makeText(this@ListHotelPurwokerto, "Rekomendasi hotel terdekat siap!", Toast.LENGTH_SHORT).show()
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