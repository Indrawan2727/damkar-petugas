package com.wain.petugas.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wain.petugas.R
import com.wain.petugas.app.ApiConfig
import com.wain.petugas.halper.SharedPref
import com.wain.petugas.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edt_email
import kotlinx.android.synthetic.main.activity_register.edt_password
import kotlinx.android.synthetic.main.activity_register.pb
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        s = SharedPref(this)

        btn_register.setOnClickListener {
            register()
        }
        btn_google.setOnClickListener {
            dataDummy()
        }
    }

    fun dataDummy(){
        edt_nama.setText("wain")
        edt_nik.setText("4386754232465788")
        edt_email.setText("wain@gmail.com")
        edt_phone.setText("099864326857")
        edt_password.setText("wain123")

    }

    fun register() {
        if (edt_nama.text.isEmpty()) {
            edt_nama.error = "Kolom Nama tidak boleh kosong"
            edt_nama.requestFocus()
            return
        } else if (edt_nik.text.isEmpty()) {
            edt_nik.error = "Kolom NIK tidak boleh kosong"
            edt_nik.requestFocus()
            return
        } else if (edt_email.text.isEmpty()) {
            edt_email.error = "Kolom Email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_phone.text.isEmpty()) {
            edt_phone.error = "Kolom Phone tidak boleh kosong"
            edt_phone.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Kolom Password tidak boleh kosong"
            edt_password.requestFocus()
            return
        }

        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(
            edt_nama.text.toString(),
            edt_nik.text.toString(),
            edt_email.text.toString(),
            edt_phone.text.toString(),
            edt_password.text.toString()
        ).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                //handel ketika gagal
                Toast.makeText(this@RegisterActivity,"Error"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb.visibility = View.VISIBLE
                val response = response.body()!!
                if(response.success == 1){
                    val intent = Intent( this@RegisterActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity,"Register Berhasil"+ response.user.name, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@RegisterActivity,"Error"+response, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}