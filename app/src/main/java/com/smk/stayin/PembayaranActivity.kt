package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class PembayaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        // 1. Inisialisasi komponen UI dari XML pembayaran kamu
        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        val btnBayar = findViewById<Button>(R.id.BtnHasilPembayaran)

        val tvNamaKamar = findViewById<TextView>(R.id.tvNamaKamar)
        val tvHargaRincian = findViewById<TextView>(R.id.tvHargaRincian)
        val tvTotalAtas = findViewById<TextView>(R.id.tvTotalPembayaranAtas)
        val tvTotalBawah = findViewById<TextView>(R.id.tvTotalPembayaranBawah)

        // 2. Tangkap data dari KategoriHotelJakartaActivity
        val jenisKamar = intent.getStringExtra("JENIS_KAMAR") ?: "Kamar Hotel"
        val hargaMentah = intent.getIntExtra("HARGA_MENTAH", 0)

        // 3. Hitung rincian matematika (Biaya Tambahan)
        val pajak = (hargaMentah * 0.1).toInt() // Pajak 10%
        val biayaLayanan = 10000 // Biaya layanan flat Rp 10.000
        val totalPembayaran = hargaMentah + pajak + biayaLayanan // Angka ini yang siap ditembak ke field 'amount' Laravel

        // Helper untuk mengubah angka biasa menjadi format Rupiah (e.g. 580000 -> Rp 580.000)
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        // 4. Set teks hasil hitungan ke komponen XML kamu
        tvNamaKamar.text = jenisKamar
        tvHargaRincian.text = formatRupiah.format(hargaMentah)
        tvTotalAtas.text = formatRupiah.format(totalPembayaran)
        tvTotalBawah.text = formatRupiah.format(totalPembayaran)

        // 5. Fungsi Tombol Back (Kiri Atas)
        btnBack.setOnClickListener {
            finish()
        }

        // 6. Fungsi Tombol Bayar Sekarang (Bawah)
        btnBayar.setOnClickListener {
            Toast.makeText(this, "Proses Pembayaran Berhasil Disimulasikan!", Toast.LENGTH_SHORT).show()

            // Variabel 'totalPembayaran' di atas sudah siap lu pakai di sini buat dilempar ke API/Retrofit

            // Buka gembok ini kalau PaymentSuccessActivity udah siap:
            // val intent = Intent(this, PaymentSuccessActivity::class.java)
            // startActivity(intent)
            // finish()
        }
    }
}