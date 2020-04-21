package com.veridetta.surveykesehatan.server;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DaftarResponden {
    @FormUrlEncoded
    @POST("insert_responden.php")
    Call<String> postResponden(
            @Field("nik") String nik,
            @Field("nama") String nama,
            @Field("umur") String umur,
            @Field("alamat") String alamat,
            @Field("jk") String jk);
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://crb-dev.id/kesehatan/")
            .build();
}