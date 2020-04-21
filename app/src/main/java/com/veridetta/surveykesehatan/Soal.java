package com.veridetta.surveykesehatan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.veridetta.surveykesehatan.adapter.HistoryAdapter;
import com.veridetta.surveykesehatan.server.DaftarResponden;
import com.veridetta.surveykesehatan.server.Insert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.veridetta.surveykesehatan.server.DaftarResponden.retrofit;

public class Soal extends AppCompatActivity {
    TextView tanya_kategori, tanya_isi;
    LinearLayout btn_tidak, btn_ya, belum_ada, surat_ada, surat_tidak, cardsoal,
    muat_ulang;
    RecyclerView rc_history;
    CardView ly_surat;
    SharedPreferences sharedpreferences;
    String nik, soal_satu, soal_dua, kategori, tb_kategori, pen;
    int jenis_soal =1, hitung=0;
    Dialog dialog;
    private ArrayList<String> penyakitList;
    private ArrayList<String> diagnosaList;
    private ArrayList<String> suratList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);
        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        nik = sharedpreferences.getString("nik", null);
        //initial item
        tanya_kategori = findViewById(R.id.tanya_kategori);
        tanya_isi = findViewById(R.id.tanya_isi);
        ly_surat = findViewById(R.id.ly_surat);
        cardsoal = findViewById(R.id.card_soal);
        belum_ada = findViewById(R.id.belum_ada);
        muat_ulang = findViewById(R.id.muat_ulang);
        muat_ulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSoal(nik);
                showDialog("memuat ulang ..");
            }
        });
        showDialog("Memuat data..");
        getSoal(nik);
        //initial buton
        btn_ya = findViewById(R.id.btn_ya);
        //btn ya di klik
        btn_ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Menyimpan..");
                final Handler handler  = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                };
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                        ly_surat.setVisibility(View.VISIBLE);
                    }
                });
                handler.postDelayed(runnable, 500);
            }
        });
        btn_tidak = findViewById(R.id.btn_tidak);
        //btn tidak di klik
        btn_tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(jenis_soal<2){
                    final Handler handler  = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                jenis_soal++;
                            }
                        }
                    };
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            handler.removeCallbacks(runnable);
                            tanya_isi.setText(soal_dua);
                        }
                    });
                    handler.postDelayed(runnable, 1000);
                }else if(jenis_soal==2){
                    getSoal(nik);
                    cobaPost(nik, "tidak", "Tidak", tb_kategori);
                    showDialog("Menyimpan data..");
                }
            }
        });
        ///btn surat
        surat_ada = findViewById(R.id.surat_ada);
        //clik
        surat_ada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSoal(nik);
                cobaPost(nik, "terdiagnosa","Ada", tb_kategori);
                showDialog("Menyimpan data..");
                ly_surat.setVisibility(View.GONE);
            }
        });
        surat_tidak = findViewById(R.id.surat_tidak);
        surat_tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSoal(nik);
                cobaPost(nik, "terdiagnosa","Tidak", tb_kategori);
                showDialog("Menyimpan data...");
                ly_surat.setVisibility(View.GONE);
            }
        });
    }
    private void getSoal(final String nike){
        String url_provinsi="https://crb-dev.id/kesehatan/get_soal.php?nik="+nike;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        Log.wtf("URL Called", url_provinsi + "");
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                url_provinsi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response=new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(response.isEmpty()){
                    Intent o = new Intent(Soal.this, surveySelesai.class);
                    o.putExtra("nik", nike);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("selesai", true);
                    editor.commit();
                    startActivity(o);
                }
                Log.e(Soal.class.getSimpleName(), "Register Response: " + response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jArray = jsonObject.getJSONArray("content");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject1=jArray.getJSONObject(i);
                         soal_satu=jsonObject1.getString("soal_satu");
                         soal_dua=jsonObject1.getString("soal_dua");
                         kategori = jsonObject1.getString("kategori");
                         tb_kategori = jsonObject1.getString("tb_kategori");
                        tanya_kategori.setText(kategori);
                        tanya_isi.setText(soal_satu);
                    }
                    jenis_soal=1;
                    getHistory(nik);
                }catch (JSONException e){e.printStackTrace(); }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
    private void cobaPost(final String niki, final String status, final String surat_dokter,
                          final String db){
        Insert scalarService = retrofit.create(Insert.class);
        Call<String> stringCall = scalarService.postResponden(niki, status, surat_dokter, db);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e(Soal.class.getSimpleName(), "Register Response: " + response.body());
                String berhasil = "Data disimpan!";
                if(berhasil.equals(response.body())){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                Log.wtf("", t.toString(), t);
                Toast.makeText(Soal.this,"Silahkan ulangi lagi",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getHistory(final String nike){
        String url_provinsi="https://crb-dev.id/kesehatan/get_history.php?nik="+nike;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        penyakitList = new ArrayList<>();
        diagnosaList =  new ArrayList<>();
        suratList = new ArrayList<>();
        Log.wtf("URL Called", url_provinsi + "");
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                url_provinsi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(Soal.class.getSimpleName(), "Register Response: " + response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jArray = jsonObject.getJSONArray("content");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject1=jArray.getJSONObject(i);
                        String penyakit =jsonObject1.getString("penyakit");
                        String diagnosa =jsonObject1.getString("diagnosa");
                        String surat = jsonObject1.getString("surat");
                        penyakitList.add(penyakit);
                        diagnosaList.add(diagnosa);
                        suratList.add(surat);


                    }
                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rc_history);
                    HistoryAdapter mDataAdapter = new HistoryAdapter( penyakitList, diagnosaList,
                            suratList);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Soal.this,1);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mDataAdapter);
                    belum_ada.setVisibility(View.GONE);
                }catch (JSONException e){e.printStackTrace(); }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
    public void showDialog(String kata){
        dialog = new Dialog(Soal.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        final TextView tt = dialog.findViewById(R.id.text_pesan);
        tt.setText(kata);
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Keluar dari Aplikasi")
                .setMessage("Yakin mau menutup aplikasi ini ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}
