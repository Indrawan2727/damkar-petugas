package com.wain.petugas.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wain.petugas.R
import com.wain.petugas.app.ApiConfig
import com.wain.petugas.halper.SharedPref
import com.wain.petugas.model.ResponModel
import kotlinx.android.synthetic.main.activity_update_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateprofileActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        s = SharedPref(this)
        

        btn_simpan.setOnClickListener {
            Updateprofile()
        }
        setData()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setData() {
        val user = s.getUser()
        if (user != null) {
            apply {
                up_nama.setText(user.name)
                up_email.setText(user.email)
                up_phone.setText(user.phone)
            }
        }
    }

    fun Updateprofile() {
        if (up_nama.text.isEmpty()) {
            return
        } else if (up_email.text.isEmpty()) {
            return
        } else if (up_phone.text.isEmpty()) {
            return
        }
        ApiConfig.instanceRetrofit.update(
                s.getUser()?.id,
                up_nama.text.toString(),
                up_email.text.toString(),
                up_phone.text.toString())
                        .enqueue(object : Callback<ResponModel> {
                            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                                val response = response.body()!!
                                if (response.success == 1) {
                                    Toast.makeText(
                                            applicationContext,"Berhasil Update"+response.user.name, Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                            }
                            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                            }
                        })
    }

}