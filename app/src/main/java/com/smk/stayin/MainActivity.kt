package com.smk.stayin

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // PANGGIL FUNGSI TEST API DI SINI
        testKoneksiLaravel()
    }

    private fun testKoneksiLaravel() {
        ApiClient.instance.getHotels().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.string()
                    // Kalau berhasil, datanya muncul di Logcat dengan filter: HASIL_LARAVEL
                    Log.d("HASIL_LARAVEL", "Sukses! Data dari Laravel: $jsonResponse")
                } else {
                    Log.e("HASIL_LARAVEL", "Gagal merespon: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Kalau error/RTO, detailnya muncul di sini
                Log.e("HASIL_LARAVEL", "Error Koneksi: ${t.message}")
            }
        })
    }
}