package com.wain.petugas.petugas.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.wain.petugas.R
import com.wain.petugas.activity.LokasiActivity
import com.wain.petugas.model.Laporan
import com.wain.petugas.utils.Config
import kotlinx.android.synthetic.main.activity_detail_laporan.*


class DetailLaporanActivity : AppCompatActivity() {

    lateinit var laporan : Laporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_laporan)

        getInfo()

        btn_menujulokasi.setOnClickListener{
            val intent = Intent( this, LokasiActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        laporan = Gson().fromJson<Laporan>(data, Laporan::class.java)

        // set Value
        tv_namana.text = laporan.name
        tv_Keterangana.text = laporan.kategori
        tv_alamata.text = laporan.lokasi
        tv_latlong.text = laporan.lat.toString()
        tv_deskripsia.text = laporan.deskripsi

        val img = Config.productUrl + laporan.image
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.gambar)
                .error(R.drawable.gambar)
                .resize(400, 400)
                .into(image)

        // setToolbar
       // Helper().setToolbar(this, toolbar, laporan.kategori)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun menujulokasi(){
        val gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr="+tv_latlong)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}