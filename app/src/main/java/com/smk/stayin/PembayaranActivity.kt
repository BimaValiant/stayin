package com.smk.stayin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class PembayaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        val btnBack = findViewById<ImageView>(R.id.BtnBack)
        val btnBayar = findViewById<Button>(R.id.BtnHasilPembayaran)

        val tvNamaKamar = findViewById<TextView>(R.id.tvNamaKamar)
        val tvHargaRincian = findViewById<TextView>(R.id.tvHargaRincian)
        val tvTotalAtas = findViewById<TextView>(R.id.tvTotalPembayaranAtas)
        val tvTotalBawah = findViewById<TextView>(R.id.tvTotalPembayaranBawah)

        // 1. Tangkap semua data kiriman dari activity sebelumnya
        val bookingId = intent.getIntExtra("BOOKING_ID", 0)
        val jenisKamar = intent.getStringExtra("JENIS_KAMAR") ?: "Kamar Hotel"
        val hargaMentah = intent.getIntExtra("HARGA_MENTAH", 0)

        // 🔥 INI DIA! Biar gak merah, variabel ini wajib dideklarasikan di sini!
        val namaHotel = intent.getStringExtra("NAMA_HOTEL") ?: "StayIn Hotel"

        // Hitung rincian matematika pajak & layanan
        val pajak = (hargaMentah * 0.1).toInt()
        val biayaLayanan = 10000
        val totalPembayaran = hargaMentah + pajak + biayaLayanan

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        tvNamaKamar.text = "Kategori: $jenisKamar"
        tvHargaRincian.text = ": ${formatRupiah.format(hargaMentah).replace("Rp", "Rp ")}"
        tvTotalAtas.text = formatRupiah.format(totalPembayaran).replace("Rp", "Rp ")
        tvTotalBawah.text = formatRupiah.format(totalPembayaran).replace("Rp", "Rp ")

        btnBack.setOnClickListener { finish() }

        // 2. Tombol bayar diklik, oper variabel namaHotel ke fungsi bawah
        btnBayar.setOnClickListener {
            prosesPembayaranKeLaravel(bookingId, totalPembayaran, namaHotel)
        }
    }

    // 3. Fungsi dengan parameter namaHotel tambahan biar dinamis
    private fun prosesPembayaranKeLaravel(bookingId: Int, totalAmount: Int, namaHotel: String) {
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""

        if (tokenMentah.isEmpty()) {
            Toast.makeText(this, "Sesi habis, silakan login ulang!", Toast.LENGTH_SHORT).show()
            return
        }

        val tokenLengkap = "Bearer $tokenMentah"

        // Tembak API Laravel
        ApiClient.instance.bayarBooking(tokenLengkap, bookingId, totalAmount, "Transfer Bank")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PembayaranActivity, "Pembayaran Berhasil!", Toast.LENGTH_SHORT).show()

                        // Kirim data nama hotel asli ke halaman ReviewActivity
                        val intent = Intent(this@PembayaranActivity, ReviewActivity::class.java)
                        intent.putExtra("NAMA_HOTEL", namaHotel)
                        intent.putExtra("USER_NAME", "Pelanggan StayIn")
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("PAY_ERR", "Gagal bayar. Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@PembayaranActivity, "Koneksi Backend Gagal!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}