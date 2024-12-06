
package com.wain.petugas.petugas.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wain.petugas.R
import com.wain.petugas.activity.LoginActivity
import com.wain.petugas.activity.UpdateprofileActivity
import com.wain.petugas.halper.SharedPref


class ProfileFragmentp : Fragment() {

    lateinit var s: SharedPref
    lateinit var btn_logout: Button
    lateinit var btnUpdate: ImageButton
    lateinit var tvNama: TextView
    lateinit var tvNik: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView
    lateinit var tvAlamat: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        init(view)

        s = SharedPref(requireActivity())
        mainButton()
        setData()
        return view
    }
    override fun onResume() {
        super.onResume()
        setData()
    }

    fun mainButton() {
        btn_logout.setOnClickListener {
            s.setStatusLogin(false)
            val intent = Intent (this@ProfileFragmentp.context, LoginActivity::class.java)
            startActivity(intent)
        }
        btnUpdate.setOnClickListener{
            val intent = Intent (this@ProfileFragmentp.context, UpdateprofileActivity::class.java)
           startActivity(intent)
        }
    }
    fun setData() {
            val user = s.getUser()
            if (user != null) {
                apply {
                    tvNama.text = user.name
                    tvNik.text = user.nik
                    tvEmail.text = user.email
                    tvPhone.text = user.phone
                    tvAlamat.text = user?.alamat
                }
            }

    }
    private fun init(view: View) {
        btn_logout = view.findViewById(R.id.btn_logout)
        btnUpdate = view.findViewById(R.id.update)
        tvNama = view.findViewById(R.id.tv_nama)
        tvNik = view.findViewById(R.id.tv_nik)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvAlamat = view.findViewById(R.id.tv_alamat)
    }

}