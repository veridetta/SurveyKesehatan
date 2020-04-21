package com.veridetta.surveykesehatan.server;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Insert {
    @FormUrlEncoded
    @POST("insert_survey.php")
    Call<String> postResponden(
            @Field("nik") String nik,
            @Field("status") String status,
            @Field("surat_dokter") String surat_dokter,
            @Field("db") String db);
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://crb-dev.id/kesehatan/")
            .build();
}
