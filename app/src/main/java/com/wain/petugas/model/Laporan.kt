package com.wain.petugas.model

class Laporan {
    lateinit var id:Integer
    lateinit var name:String
    lateinit var hp:String
    lateinit var lokasi:String
    var lat:Double = 0.0
    var long:Double = 0.0
    lateinit var kategori:String
    lateinit var deskripsi:String
    lateinit var image:String
    lateinit var petugas:String
    var status:String? = null
    lateinit var created_at:String
    lateinit var updated_at:String
}