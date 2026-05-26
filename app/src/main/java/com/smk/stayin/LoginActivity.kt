package com.smk.stayin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menyambungkan file Kotlin ini ke layout activity_login.xml milikmu
        setContentView(R.layout.activity_login)
    }
}