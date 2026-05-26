package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inisialisasi View (PASTIIN ID DI XML LU COCOK YA, BREE!)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.BtnMasuk) // ID tombol login lu
        val tvRegisterBawah = findViewById<TextView>(R.id.BtnRegister) // Teks "Belum punya akun? Daftar di sini"

        // 2. Aksi Klik Tombol Login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password jangan dikosongin, bree!", Toast.LENGTH_SHORT).show()
            } else {
                // Jalankan fungsi nembak API Login
                prosesLoginKeLaravel(email, password)
            }
        }

        // 3. Aksi kalau user kesasar dan mau balik ke halaman Register
        tvRegisterBawah.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun prosesLoginKeLaravel(email: String, password: String) {
        ApiClient.instance.loginUser(email, password).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonRespon = response.body()?.string()
                    Log.d("LOGIN_SUCCESS", "Respon Token: $jsonRespon")

                    Toast.makeText(this@LoginActivity, "Login Berhasil! Welcome", Toast.LENGTH_SHORT).show()

                    // Pindah ke Dashboard / Home Activity setelah login sukses
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java) // atau MainActivity sesuai dashboard lu
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LOGIN_FAILED", "Gagal login. Kode: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Email atau Password Salah!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("LOGIN_ERROR", "Error Jaringan: ${t.message}")
                Toast.makeText(this@LoginActivity, "Koneksi Bermasalah: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}