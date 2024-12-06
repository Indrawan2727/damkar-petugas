package com.wain.petugas.app

import com.wain.petugas.model.ResponModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("registerp")
    fun register(
            @Field("name") name: String,
            @Field("nik") nik: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String,
    ):Call<ResponModel>

    @FormUrlEncoded
    @POST("loginp")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ):Call<ResponModel>

    @FormUrlEncoded
    @POST("update/{id}")
    fun update(
            @Path("id") id: Int?,
            @Field("name") name: String?,
            @Field("email") email: String?,
            @Field("phone") phone: String?,
    ):Call<ResponModel>

    @Multipart
    @POST("store")
    fun laporan(
            @Part( "name") name: RequestBody,
            @Part("lokasi") lokasi: RequestBody,
            @Part("kategori") kategori: RequestBody,
            @Part("deskripsi") deskripsi: RequestBody,
            @Part image: MultipartBody.Part
    ):Call<ResponModel>

    @GET("laporan")
    fun getLaporan(): Call<ResponModel>

    @FormUrlEncoded
    @POST("update1/{id}")
    fun selesai(
            @Path("id") id: Integer,
            @Field("petugas") petugas: Unit,
    ):Call<ResponModel>
}