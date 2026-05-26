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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Inisialisasi semua view berdasarkan ID di XML
        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnDaftar = findViewById<Button>(R.id.BtnMasuk) // ID tombol "Daftar Sekarang" di XML lu
        val tvLoginBawah = findViewById<TextView>(R.id.BtnLogin) // Teks klik "Masuk di sini"

        // 2. Aksi ketika tombol "Daftar Sekarang" diklik
        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi sederhana biar inputan tidak kosong
            if (nama.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Wajib diisi semua ya, bree!", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                Toast.makeText(this, "Password minimal harus 8 karakter!", Toast.LENGTH_SHORT).show()
            } else {
                // SAKRAL: Memicu fungsi nembak database Laravel
                kirimDataKeLaravel(nama, email, phone, password)
            }
        }

        // 3. Aksi kalau user nge-klik tulisan "Masuk di sini" di bagian bawah
        tvLoginBawah.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun kirimDataKeLaravel(nama: String, email: String, phone: String, password: String) {
        ApiClient.instance.registerUser(nama, email, phone, password).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val hasilLog = response.body()?.string()
                    Log.d("DATABASE_SUCCESS", "Respon VSCode: $hasilLog")

                    Toast.makeText(this@RegisterActivity, "Akun Berhasil Masuk Database MySQL!", Toast.LENGTH_LONG).show()

                    // Pindah otomatis ke halaman login
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("DATABASE_FAILED", "Gagal menyimpan. Kode: ${response.code()}")
                    Toast.makeText(this@RegisterActivity, "Gagal Masuk Database! Kode: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DATABASE_ERROR", "Error Jaringan: ${t.message}")
                Toast.makeText(this@RegisterActivity, "Koneksi Bermasalah: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}