package com.wain.petugas.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wain.petugas.MainActivity
import com.wain.petugas.R
import com.wain.petugas.app.ApiConfig
import com.wain.petugas.halper.SharedPref
import com.wain.petugas.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)

        btn_register1.setOnClickListener {
            val intent = Intent( this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener{
           login()
        }

    }
    fun login() {
        if  (edt_email.text.isEmpty()) {
            edt_email.error = "Kolom Email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Kolom Password tidak boleh kosong"
            edt_password.requestFocus()
            return
        }

        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.login(
                edt_email.text.toString(),
                edt_password.text.toString()
        ).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                //handel ketika gagal
                pb.visibility = View.GONE
                Toast.makeText(applicationContext,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb.visibility = View.GONE
                val response = response.body()!!
                if(response.success == 1 ){
                   // if(response.user?.level=="petugas"){
                        s.setStatusLogin(true)
                        s.setUser(response.user)
                        //                   s.setString(s.name, response.user.name)
                        //                  s.setString(s.phone, response.user.phone)
                        //                s.setString(s.email, response.user.email)
                        val intent = Intent( applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Toast.makeText(applicationContext,"Selamat Datang"+response.user.name, Toast.LENGTH_SHORT).show()
                    //}else{
                     //   Toast.makeText(applicationContext,"Error"+response, Toast.LENGTH_SHORT).show()
                    //}
                }else{
                    Toast.makeText(applicationContext,"Error"+response, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}