package com.smk.stayin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(
    private var list: List<BookingResponse>,
    private val onBatalClick: (BookingResponse) -> Unit
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaHotel: TextView = view.findViewById(R.id.tvNamaHotelItem)
        val tvJenisKamar: TextView = view.findViewById(R.id.tvJenisKamarItem)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggalItem)
        val btnBatal: Button = view.findViewById(R.id.btnBatalkanItem)
        val tvStatusText: TextView = view.findViewById(R.id.tvStatusItemText)

        // FIX: Inisialisasi CardView badge status agar warnanya bisa diubah secara dinamis
        val cvStatusBadge: CardView = view.findViewById(R.id.cvStatusBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.tvNamaHotel.text = "StayIn Hotel (ID: ${data.hotel_id})"
        holder.tvJenisKamar.text = data.jenis_kamar
        holder.tvTanggal.text = data.waktu_pemesanan

        // ========================================================
        // 🔒 LOGIKA AMAN & ESTETIK: CEK STATUS PEMBAYARAN
        // ========================================================
        if (data.status.equals("unpaid", ignoreCase = true)) {
            // 1. Kalau BELUM BAYAR (unpaid) -> Tombol Batal MUNCUL & Badge Merah/Oranye
            holder.btnBatal.visibility = View.VISIBLE
            holder.tvStatusText.text = "UNPAID"
            holder.cvStatusBadge.setCardBackgroundColor(Color.parseColor("#FFD2D2")) // Warna merah muda lembut

            // Set aksi klik batalkan transaksi
            holder.btnBatal.setOnClickListener { onBatalClick(data) }
        } else {
            // 2. Kalau SUDAH BAYAR (paid) -> Tombol Batal HILANG TOTAL & Badge Hijau
            holder.btnBatal.visibility = View.GONE
            holder.tvStatusText.text = "PAID"
            holder.cvStatusBadge.setCardBackgroundColor(Color.parseColor("#4EFF9A")) // Warna hijau bawaan lu
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<BookingResponse>) {
        this.list = newList
        notifyDataSetChanged()
    }
}