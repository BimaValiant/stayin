package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvNamaLengkap = findViewById<TextView>(R.id.tvNamaLengkap)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)

        // 1. Ambil token login yang disimpan di memori HP tadi
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", "")

        // 2. Request data profil ke database MySQL lewat jembatan Ngrok
        if (!token.isNullOrEmpty()) {
            ApiClient.instance.getProfile(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val jsonStr = response.body()?.string()
                        val jsonObject = JSONObject(jsonStr)
                        val dataUser = jsonObject.getJSONObject("data")

                        // GANTI TEKS DI LAYAR HP DENGAN DATA ASLI DARI DATABASE MYSQL
                        tvNamaLengkap.text = dataUser.getString("name")
                        tvEmail.text = dataUser.getString("email")
                        tvPhone.text = dataUser.getString("phone")
                    } else {
                        Toast.makeText(this@ProfileActivity, "Sesi habis, gagal muat data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("PROFILE_ERROR", "Koneksi bermasalah: ${t.message}")
                }
            })
        } else {
            Toast.makeText(this, "Token kosong, silakan login ulang", Toast.LENGTH_SHORT).show()
        }

        // 3. Navigasi Menu Bawah: Tombol Home
        val btnHome = findViewById<ImageView>(R.id.BtnHotel)
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // Navigasi Menu Bawah: Tombol Booking
        val btnBooking = findViewById<ImageView>(R.id.BtnBooking)
        btnBooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // Tombol Keluar (Logout)
        val btnKeluar = findViewById<LinearLayout>(R.id.BtnKeluar)
        btnKeluar.setOnClickListener {
            // Hapus token di memori HP pas logout biar aman dari pembajakan akun
            sharedPref.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}