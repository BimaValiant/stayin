package com.smk.stayin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PesanHotelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesan_hotel)

        // 1. Tangkap "oleh-oleh" data dari halaman List Hotel
        val namaHotel = intent.getStringExtra("NAMA_HOTEL")
        val hargaHotel = intent.getStringExtra("HARGA_HOTEL")

        // 2. Kenalan sama TextView yang baru kita tambahin di XML
        val tvNama = findViewById<TextView>(R.id.tvNamaHotelPesan)
        val tvHarga = findViewById<TextView>(R.id.tvHargaHotelPesan)

        // 3. Tampilkan datanya ke layar
        tvNama.text = namaHotel
        tvHarga.text = hargaHotel

        // 4. Kasih fungsi buat tombol Back
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        btnBack.setOnClickListener {
            finish() // Balik ke halaman sebelumnya
        }

        // 5. Kasih fungsi sementara buat tombol Pembayaran
        val btnPembayaran = findViewById<Button>(R.id.BtnPembayaran)
        btnPembayaran.setOnClickListener {
            // Nanti lu tinggal ganti ini pakai Intent buat pindah ke halaman Pembayaran betulan
            Toast.makeText(this, "Lanjut ke Pembayaran $namaHotel", Toast.LENGTH_SHORT).show()
        }
    }
}