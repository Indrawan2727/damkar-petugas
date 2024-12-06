package com.wain.petugas.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.wain.petugas.MainActivity
import com.wain.petugas.R
import com.wain.petugas.app.ApiConfig
import com.wain.petugas.halper.SharedPref
import com.wain.petugas.model.Laporan
import com.wain.petugas.model.ResponModel
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.activity_lokasi.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class LokasiActivity :  AppCompatActivity(), OnMapReadyCallback, DirectionCallback {
    lateinit var mapsView: GoogleMap
    lateinit var progressDialog: ProgressDialog

    lateinit var simpleLocation: SimpleLocation
    lateinit var strPhone: String
    lateinit var fromLatLng: LatLng
    lateinit var toLatLng: LatLng
    lateinit var strCurrentLocation: String
    lateinit var strCurrentLocationto: String
    var strCurrentLatitude = 0.0
    var strLatitude = 0.0
    var strCurrentLongitude = 0.0
    var strLongitude = 0.0
    lateinit var laporan : Laporan
    lateinit var s: SharedPref

    private var googleMap: GoogleMap? = null

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi)

        s = SharedPref(this)
        s.setStatusLogin(true)

        //progressDialog = ProgressDialog(this)
        //progressDialog.setTitle("Mohon Tunggu…")
        //progressDialog.setCancelable(false)
        //progressDialog.setMessage("sedang menampilkan detail rute")

        // setSupportActionBar(toolbar)
        //if (supportActionBar != null) {
        //  supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowTitleEnabled(false)
        // }
        val verfiyPermission: Int = Build.VERSION.SDK_INT
        if (verfiyPermission > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkIfAlreadyhavePermission()) {
                requestForSpecificPermission()
            }
        }

        simpleLocation = SimpleLocation(this)
        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this)
        }

        //get location
        strCurrentLatitude = simpleLocation.latitude
        strCurrentLongitude = simpleLocation.longitude



        //set location lat long
        strCurrentLocation = "$strCurrentLatitude,$strCurrentLongitude"
        strCurrentLocationto = "$strLatitude , $strLongitude"

        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        supportMapFragment.getMapAsync(this)


        //latlong origin & destination
        fromLatLng = LatLng(strCurrentLatitude, strCurrentLongitude)
        toLatLng = LatLng(strLatitude, strLongitude)
        

        //intent open google maps
        llRoute.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + strLatitude + "," + strLongitude))
            startActivity(intent)
        }

        //intent to call number
        llPhone.setOnClickListener {
            val intent: Intent
            intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$strPhone"))
            startActivity(intent)
        }

        //show route
        showDirection()
       // progressDialog.dismiss()
        getInfo()

        btn_selesai.setOnClickListener{
            Selesai()
            val intent = Intent( this@LokasiActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun Selesai(){
        val petugas = edt_petugas.setText("petugas")
        ApiConfig.instanceRetrofit.selesai(
                laporan.id,
                petugas
        ).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb2.visibility = View.GONE
            }
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb2.visibility = View.VISIBLE
                if(response.isSuccessful){
                    if(response.body()!!.success==1){
                        s.setStatusLogin(true)
                        val intent = Intent( this@LokasiActivity, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@LokasiActivity,"Berhasil" + response, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@LokasiActivity,"GAGAL"+response, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        laporan = Gson().fromJson<Laporan>(data, Laporan::class.java)

        // set Value
        tvNamaLokasi.text = laporan.name
        //tv_Keterangana.text = laporan.kategori
        tvNamaJalan.text = laporan.lokasi
        //tv_latlong.text = laporan.latlong
        tvDeskripsi.text = laporan.deskripsi
        strLatitude = laporan.lat
        strLongitude = laporan.long
        strPhone = laporan.hp

    }
    private fun showDirection() {
        //get latlong for polyline
        GoogleDirection.withServerKey("AIzaSyDP9NIcN_8xmUgcxj5t4qgn46InT2u3OJ4")
            .from(fromLatLng)
            .to(toLatLng)
            .transportMode(TransportMode.DRIVING)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        mapsView = googleMap
        mMap= googleMap!!
        val YOUR_API_KEY = "AIzaSyDP9NIcN_8xmUgcxj5t4qgn46InT2u3OJ4"
        // Sample coordinates
        val latLngOrigin = LatLng(strCurrentLatitude, strCurrentLongitude) // Saya
        val latLngDestination = LatLng(strLatitude, strLongitude) // tujuan
        this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin).title("Saya"))
        this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination).title("Lokasi"))
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))
        val path: MutableList<List<LatLng>> = ArrayList()
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin="+strCurrentLatitude+","+strCurrentLongitude+
                "&destination="+strLatitude+","+strLongitude+"&key="+YOUR_API_KEY

        val directionsRequest = object : StringRequest(
            Method.GET,
            urlDirections,
            Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                // Get routes
                val routes = jsonResponse.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val points =
                        steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    path.add(PolyUtil.decode(points))
                }
                for (i in 0 until path.size) {
                    this.googleMap!!.addPolyline(
                        PolylineOptions().addAll(path[i]).color(Color.BLUE)
                            .geodesic(true)
                            .clickable(true)
                            .visible(true)
                    )
                }
            },
            Response.ErrorListener { _ ->
            }){}

        this.googleMap!!.addCircle(CircleOptions()
                .center(LatLng(strLatitude, strLongitude))
                .radius(50.0)
                .strokeColor(Color.TRANSPARENT)
                .fillColor(Color.argb(70,200,50,50)))


        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mapsView.isMyLocationEnabled = true
            mapsView.setPadding(0, 60, 0, 0)
            mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 14f))
        }
        mapsView.isMyLocationEnabled = true
        mapsView.setPadding(0, 60, 0, 0)
        mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 14f))
    }

    override fun onDirectionSuccess(direction: Direction) {
        if (direction.isOK) {
            //show distance & duration
            val route = direction.routeList[0]
            val leg = route.legList[0]
            val distanceInfo = leg.distance
            val durationInfo = leg.duration
            val strDistance = distanceInfo.text
            val strDuration = durationInfo.text.replace("mins", "mnt")
            //tvDistance.text = "Jarak lokasi tujuan dari rumah kamu $strDistance dan waktu tempuh sekitar $strDuration"

            //set marker current location
            val saya = LatLng(strCurrentLatitude, strCurrentLongitude)
            mapsView.addMarker(MarkerOptions()
                    .title("Lokasi Kamu")
                    .position(saya)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

            //set marker destination
            val tujuan = LatLng(strLatitude, strLongitude)
            mapsView.addMarker(MarkerOptions()
                    .title("tujuan")
                    .position(tujuan)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

            //show polyline
            val directionPositionList = direction.routeList[0].legList[0].directionPoint
            mapsView.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED))
        }

    }


    override fun onDirectionFailure(t: Throwable?) {
        Toast.makeText(this, "Oops, gagal menampilkan rute!", Toast.LENGTH_SHORT).show()
    }


    private fun checkIfAlreadyhavePermission(): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 101)
    }

}