package com.smk.stayin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menyambungkan file Kotlin ini ke layout activity_register.xml milikmu
        setContentView(R.layout.activity_register)
    }
}