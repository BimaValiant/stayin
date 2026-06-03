package com.smk.stayin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class PesanHotelActivity : AppCompatActivity() {

    private var tanggalTerpilih: String = ""
    private var hargaDasarHotel: Long = 0
    private var hargaTotalFinal: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesan_hotel)

        // 1. Inisialisasi View
        val tvNamaHotelPesan = findViewById<TextView>(R.id.tvNamaHotelPesan)
        val tvHargaHotelPesan = findViewById<TextView>(R.id.tvHargaHotelPesan)
        val tvTanggalPesan = findViewById<TextView>(R.id.tvTanggalPesan)
        val spJenisKamar = findViewById<Spinner>(R.id.spJenisKamar)
        val btnPesanSekarang = findViewById<Button>(R.id.BtnPesanSekarang)
        val btnBack = findViewById<ImageView>(R.id.BtnBack)

        // 2. Tangkap Data dari Intent List Hotel Sebelumnya
        val namaHotel = intent.getStringExtra("NAMA_HOTEL") ?: "Hotel Pilihan"
        val hargaHotelString = intent.getStringExtra("HARGA_HOTEL") ?: "Rp 500.000"
        val idHotel = intent.getIntExtra("HOTEL_ID", 1)

        // Bersihin string "Rp 500.000" jadi angka murni 500000 biar bisa dihitung matematika
        hargaDasarHotel = hargaHotelString.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 500000

        // Set nama hotel di awal
        tvNamaHotelPesan.text = namaHotel

        // 3. Setup Dropdown Pilihan Kamar
        val listKamar = arrayOf("Standard Room", "Deluxe Room", "Luxury Room")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listKamar)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spJenisKamar.adapter = adapter

        // ========================================================
        // LOGIKA DINAMIS: HARGA BERUBAH SESUAI JENIS KAMAR
        // ========================================================
        spJenisKamar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val kamarTerpilih = listKamar[position]

                // FIX: Menggunakan variabel kamarTerpilih yang benar (bukan kamarTerpisted)
                hargaTotalFinal = when (kamarTerpilih) {
                    "Standard Room" -> hargaDasarHotel          // Harga normal kartunya
                    "Deluxe Room"   -> hargaDasarHotel + 150000 // Naik 150 ribu
                    "Luxury Room"   -> hargaDasarHotel + 350000 // Naik 350 ribu
                    else            -> hargaDasarHotel
                }

                // Format angka murni ke rupiah string lagi biar cantik di UI
                val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                tvHargaHotelPesan.text = formatRupiah.format(hargaTotalFinal).replace("Rp", "Rp ")
            }

            // FIX:override fun tidak dobel lagi
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 4. Logika Klik Tanggal Keluar Kalender
        tvTanggalPesan.setOnClickListener {
            val kalender = Calendar.getInstance()
            val tahun = kalender.get(Calendar.YEAR)
            val bulan = kalender.get(Calendar.MONTH)
            val hari = kalender.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val bulanBenar = month + 1
                tanggalTerpilih = String.format("%04d-%02d-%02d", year, bulanBenar, dayOfMonth)
                tvTanggalPesan.text = tanggalTerpilih
            }, tahun, bulan, hari)

            datePicker.show()
        }

        // 5. Tombol Pesan Klik
        btnPesanSekarang.setOnClickListener {
            val kamarTerpilih = spJenisKamar.selectedItem.toString()

            if (tanggalTerpilih.isEmpty()) {
                Toast.makeText(this, "Pilih tanggal check-in dulu, cuk!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim data ter-update ke fungsi database
            kirimDataBookingKeDatabase(idHotel, kamarTerpilih, tanggalTerpilih)
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun kirimDataBookingKeDatabase(idHotel: Int, tipeKamar: String, tanggalCheckin: String) {
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""

        if (tokenMentah.isEmpty()) {
            Toast.makeText(this, "Lu belum login/sesi habis, cuk!", Toast.LENGTH_SHORT).show()
            return
        }

        val tokenLengkap = "Bearer $tokenMentah"

        val dataBooking = HashMap<String, Any>()
        dataBooking["hotel_id"] = idHotel
        dataBooking["jenis_kamar"] = tipeKamar
        dataBooking["waktu_pemesanan"] = tanggalCheckin
        // dataBooking["total_harga"] = hargaTotalFinal // <-- Aktifin ini kalau di DB Laravel lu nanti mau ditambahin kolom harga total!

        ApiClient.instance.buatBooking(tokenLengkap, dataBooking)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PesanHotelActivity, "Booking Disimpan! Silakan Bayar 💳", Toast.LENGTH_SHORT).show()

                        // PINDAH KE PAYMENT ACTIVITY
                        val intent = Intent(this@PesanHotelActivity, PembayaranActivity::class.java)
                        // Kirim data tambahan jika PaymentActivity lu butuh ID buat bayar
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("API_ERR", "Gagal insert. Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@PesanHotelActivity, "Koneksi Backend Putus/Gagal!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}