package com.wain.petugas.petugas.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wain.petugas.R
import com.wain.petugas.app.ApiConfig
import com.wain.petugas.halper.SharedPref
import com.wain.petugas.model.Laporan
import com.wain.petugas.model.ResponModel
import com.wain.petugas.petugas.adapter.AdapterLaporan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragmentp : Fragment() {

    lateinit var s: SharedPref
    lateinit var rvLaporan: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_homep, container, false)
        init(view)
        
        getLaporan()

        return view
    }

    fun displayLaporan() {
        Log.d("cekini", "size:" + listLaporan.size)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rvLaporan.adapter = AdapterLaporan(requireActivity(), listLaporan)
        rvLaporan.layoutManager = layoutManager

    }

    private var listLaporan: ArrayList<Laporan> = ArrayList()
    fun getLaporan() {
        ApiConfig.instanceRetrofit.getLaporan().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1 ) {
                   val arrayLaporan = ArrayList<Laporan>()
                    for (p in res.laporans){
                      if( p.status == "Terima") {
                         arrayLaporan.add(p)
                      }
                        listLaporan = arrayLaporan
                        displayLaporan()
                    }
                }
            }
        })
    }


    fun init(view: View) {
        rvLaporan = view.findViewById(R.id.rv_laporan)
    }

    override fun onResume() {
        displayLaporan()
        super.onResume()
    }
}
