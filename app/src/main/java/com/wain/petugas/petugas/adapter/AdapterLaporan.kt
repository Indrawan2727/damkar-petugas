package com.wain.petugas.petugas.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.wain.petugas.R
import com.wain.petugas.activity.LokasiActivity
import com.wain.petugas.model.Laporan
import com.wain.petugas.utils.Config

class AdapterLaporan (var activity: Activity, var data: ArrayList<Laporan>) : RecyclerView.Adapter<AdapterLaporan.Holder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterLaporan.Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_laporan, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: AdapterLaporan.Holder, position: Int) {
        val laporan = data[position]
        holder.tv_kategori.text = data[position].kategori
        holder.tv_nama.text = data[position].name
        holder.tv_deskripsi.text = data[position].deskripsi
        val img = Config.productUrl + laporan.image
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.gambar)
                .error(R.drawable.gambar)
                .resize(400, 400)
                .into(holder.imgLaporan)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, LokasiActivity::class.java)
            val str = Gson().toJson(data[position], Laporan::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun update(modelList:ArrayList<String>){

    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_kategori = view.findViewById<TextView>(R.id.tv_kategori)
        val tv_nama = view.findViewById<TextView>(R.id.tv_nama)
        val tv_deskripsi = view.findViewById<TextView>(R.id.tv_deskripsi)
        val imgLaporan = view.findViewById<ImageView>(R.id.img_laporan)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

}