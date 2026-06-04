package com.smk.stayin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.json.JSONObject // <-- PASTIKAN INI TER-IMPORT
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

                hargaTotalFinal = when (kamarTerpilih) {
                    "Standard Room" -> hargaDasarHotel
                    "Deluxe Room"   -> hargaDasarHotel + 150000
                    "Luxury Room"   -> hargaDasarHotel + 350000
                    else            -> hargaDasarHotel
                }

                // Format angka murni ke rupiah string
                val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                tvHargaHotelPesan.text = formatRupiah.format(hargaTotalFinal).replace("Rp", "Rp ")
            }

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
                Toast.makeText(this, "Silahkan pilih tanggal check-in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            kirimDataBookingKeDatabase(idHotel, kamarTerpilih, tanggalTerpilih, namaHotel)
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun kirimDataBookingKeDatabase(idHotel: Int, tipeKamar: String, tanggalCheckin: String, namaHotel: String) {
        val sharedPref = getSharedPreferences("StayInPref", MODE_PRIVATE)
        val tokenMentah = sharedPref.getString("auth_token", "") ?: ""

        if (tokenMentah.isEmpty()) {
            Toast.makeText(this, "Sesi habis, Silahkan login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        val tokenLengkap = "Bearer $tokenMentah"

        val dataBooking = HashMap<String, Any>()
        dataBooking["hotel_id"] = idHotel
        dataBooking["jenis_kamar"] = tipeKamar
        dataBooking["waktu_pemesanan"] = tanggalCheckin

        ApiClient.instance.buatBooking(tokenLengkap, dataBooking)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        // KODE BARU: Mengambil Booking ID asli dari Laravel
                        val jsonStr = response.body()?.string()
                        var realBookingId = idHotel // Nilai cadangan

                        try {
                            if (!jsonStr.isNullOrEmpty()) {
                                val jsonObject = JSONObject(jsonStr)
                                // Mengambil ID dari dalam objek "data"
                                val dataObj = jsonObject.getJSONObject("data")
                                realBookingId = dataObj.getInt("id")
                                Log.d("DEBUG_STAYIN", "Booking ID Asli dari Server: $realBookingId")
                            }
                        } catch (e: Exception) {
                            Log.e("PARSE_ERR", "Gagal parse ID Booking: ${e.message}")
                        }

                        Toast.makeText(this@PesanHotelActivity, "Booking Disimpan! Silakan Bayar 💳", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@PesanHotelActivity, PembayaranActivity::class.java)
                        intent.putExtra("NAMA_HOTEL", namaHotel)

                        // MENGIRIM BOOKING ID YANG BENAR
                        intent.putExtra("BOOKING_ID", realBookingId)
                        intent.putExtra("JENIS_KAMAR", tipeKamar)
                        intent.putExtra("HARGA_MENTAH", hargaTotalFinal.toInt())

                        startActivity(intent)
                        finish()
                    } else {
                        val err = response.errorBody()?.string()
                        Log.e("API_ERR", "Gagal insert data. Response: $err")
                        Toast.makeText(this@PesanHotelActivity, "Gagal membuat booking!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@PesanHotelActivity, "Koneksi Backend Putus/Gagal!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}