package com.smk.stayin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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

        // Inisialisasi UI komponen dari XML
        val tvNamaHotel = findViewById<TextView>(R.id.tvNamaHotel)
        val tvNamaKamar = findViewById<TextView>(R.id.tvNamaKamar)
        val tvHargaRincian = findViewById<TextView>(R.id.tvHargaRincian)
        val tvTotalAtas = findViewById<TextView>(R.id.tvTotalPembayaranAtas)
        val tvTotalBawah = findViewById<TextView>(R.id.tvTotalPembayaranBawah)

        // Komponen Metode Pembayaran
        val layoutTransferBank = findViewById<LinearLayout>(R.id.layoutTransferBank)
        val tvTextTransferBank = findViewById<TextView>(R.id.tvTextTransferBank)
        val tvCheckTransferBank = findViewById<TextView>(R.id.tvCheckTransferBank)

        val layoutEwallet = findViewById<LinearLayout>(R.id.layoutEwallet)
        val tvTextEwallet = findViewById<TextView>(R.id.tvTextEwallet)
        val tvCheckEwallet = findViewById<TextView>(R.id.tvCheckEwallet)

        val layoutDebit = findViewById<LinearLayout>(R.id.layoutDebit)
        val tvTextDebit = findViewById<TextView>(R.id.tvTextDebit)
        val tvCheckDebit = findViewById<TextView>(R.id.tvCheckDebit)

        // 1. Tangkap data kiriman dari intent sebelum ini
        val bookingId = intent.getIntExtra("BOOKING_ID", 0)
        val jenisKamar = intent.getStringExtra("JENIS_KAMAR") ?: "Kamar Hotel"
        val hargaMentah = intent.getIntExtra("HARGA_MENTAH", 0)

        // 🔥 PELINDUNG INTENT
        val namaHotel = intent.getStringExtra("NAMA_HOTEL")
            ?: intent.getStringExtra("nama_hotel")
            ?: intent.getStringExtra("nama")
            ?: "StayIn Purwokerto ($jenisKamar)"

        Log.d("DEBUG_STAYIN", "Booking ID: $bookingId")
        Log.d("DEBUG_STAYIN", "Nama Hotel Terdeteksi: $namaHotel")
        Log.d("DEBUG_STAYIN", "Kategori Kamar: $jenisKamar")

        // Hitung rincian matematika pajak & layanan
        val pajak = (hargaMentah * 0.1).toInt()
        val biayaLayanan = 10000
        val totalPembayaran = hargaMentah + pajak + biayaLayanan

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        // Set teks ke komponen UI secara dinamis
        tvNamaHotel.text = namaHotel
        tvNamaKamar.text = "Kategori: $jenisKamar"
        tvHargaRincian.text = ": ${formatRupiah.format(hargaMentah).replace("Rp", "Rp ")}"
        tvTotalAtas.text = formatRupiah.format(totalPembayaran).replace("Rp", "Rp ")
        tvTotalBawah.text = formatRupiah.format(totalPembayaran).replace("Rp", "Rp ")


        // --- LOGIKA PEMILIHAN METODE PEMBAYARAN ---
        var selectedPaymentMethod = "Transfer Bank" // Default

        fun resetAllPaymentMethods() {
            // Reset Transfer Bank
            layoutTransferBank.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tvTextTransferBank.setTextColor(Color.parseColor("#000000"))
            tvCheckTransferBank.visibility = View.GONE

            // Reset E-Wallet
            layoutEwallet.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tvTextEwallet.setTextColor(Color.parseColor("#000000"))
            tvCheckEwallet.visibility = View.GONE

            // Reset Debit
            layoutDebit.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tvTextDebit.setTextColor(Color.parseColor("#000000"))
            tvCheckDebit.visibility = View.GONE
        }

        layoutTransferBank.setOnClickListener {
            resetAllPaymentMethods()
            layoutTransferBank.setBackgroundColor(Color.parseColor("#E8F0FE"))
            tvTextTransferBank.setTextColor(Color.parseColor("#1877F2"))
            tvCheckTransferBank.visibility = View.VISIBLE
            selectedPaymentMethod = "Transfer Bank"
        }

        layoutEwallet.setOnClickListener {
            resetAllPaymentMethods()
            layoutEwallet.setBackgroundColor(Color.parseColor("#E8F0FE"))
            tvTextEwallet.setTextColor(Color.parseColor("#1877F2"))
            tvCheckEwallet.visibility = View.VISIBLE
            selectedPaymentMethod = "E-Wallet"
        }

        layoutDebit.setOnClickListener {
            resetAllPaymentMethods()
            layoutDebit.setBackgroundColor(Color.parseColor("#E8F0FE"))
            tvTextDebit.setTextColor(Color.parseColor("#1877F2"))
            tvCheckDebit.visibility = View.VISIBLE
            selectedPaymentMethod = "Kartu Debit"
        }
        // --- AKHIR LOGIKA METODE PEMBAYARAN ---


        btnBack.setOnClickListener { finish() }

        // 2. Klik Tombol Bayar Sekarang
        btnBayar.setOnClickListener {
            // Mengirim selectedPaymentMethod ke dalam fungsi
            prosesPembayaranKeLaravel(bookingId, totalPembayaran, namaHotel, selectedPaymentMethod)
        }
    }

    // Menambahkan parameter paymentMethod
    private fun prosesPembayaranKeLaravel(bookingId: Int, totalAmount: Int, namaHotel: String, paymentMethod: String) {
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""

        if (tokenMentah.isEmpty()) {
            Toast.makeText(this, "Sesi habis, silakan login ulang!", Toast.LENGTH_SHORT).show()
            return
        }

        val tokenLengkap = "Bearer $tokenMentah"

        // Tembak API Laravel dengan paymentMethod dinamis
        ApiClient.instance.bayarBooking(tokenLengkap, bookingId, totalAmount, paymentMethod)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PembayaranActivity, "Pembayaran Berhasil!", Toast.LENGTH_SHORT).show()

                        // Pindah ke PaymentSuccessActivity
                        val intent = Intent(this@PembayaranActivity, PaymentSuccessActivity::class.java)
                        intent.putExtra("NAMA_HOTEL", namaHotel)
                        intent.putExtra("USER_NAME", "Pelanggan StayIn")
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("PAY_ERR", "Gagal bayar. Code: ${response.code()}")
                        Toast.makeText(this@PembayaranActivity, "Gagal memproses pembayaran!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("PAY_ERR", "Koneksi gagal: ${t.message}")
                    Toast.makeText(this@PembayaranActivity, "Koneksi Backend Gagal!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}