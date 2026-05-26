package com.smk.stayin

import android.content.Intent // Jangan lupa import ini
import android.os.Bundle
import android.widget.Button // Dan import ini untuk Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Hubungkan variabel di Kotlin dengan ID Button yang ada di activity_login.xml
        // Pastikan di XML-mu, id tombol registernya adalah android:id="@+id/btnRegister"
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // 2. Pasang fungsi klik (OnClickListener) pada tombol
        btnRegister.setOnClickListener {
            // 3. Buat Intent untuk pindah dari LoginActivity ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}