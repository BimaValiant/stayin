package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pasang layout welcome
        setContentView(R.layout.activity_welcome)

        // Tombol login dan register langsung beraksi
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val intentMasuk = Intent(this, LoginActivity::class.java)
            startActivity(intentMasuk)
        }

        btnRegister.setOnClickListener {
            val intentDaftar = Intent(this, RegisterActivity::class.java)
            startActivity(intentDaftar)
        }

        // Jalankan test API Laravel
        testKoneksiLaravel()
    }

    private fun testKoneksiLaravel() {
        ApiClient.instance.getHotels().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.string()
                    Log.d("HASIL_LARAVEL", "Sukses! Data dari Laravel: $jsonResponse")
                } else {
                    Log.e("HASIL_LARAVEL", "Gagal merespon: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("HASIL_LARAVEL", "Error Koneksi: ${t.message}")
            }
        })
    }
}