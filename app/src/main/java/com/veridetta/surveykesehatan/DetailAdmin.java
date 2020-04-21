package com.veridetta.surveykesehatan;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.veridetta.surveykesehatan.adapter.DetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailAdmin extends AppCompatActivity {
    TextView nama, alamat, jk, umur, nik, total_d, tidak_d;
    RecyclerView recyclerView;
    String niko, txtNama, txtAlamat, txtJk, txtUmur,txtNik, txtTotal, txtTidak;
    Dialog dialog;
    private ArrayList<String> penyakitList, diagnosaList, tglList, suratList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_admin);
        nama = findViewById(R.id.detail_nama);
        alamat = findViewById(R.id.detail_alamat);
        jk = findViewById(R.id.detail_jk);
        umur = findViewById(R.id.detail_umur);
        nik = findViewById(R.id.detail_nik);
        total_d = findViewById(R.id.total_diagnosa);
        tidak_d = findViewById(R.id.total_tidak);
        Intent intent = getIntent();
        niko = (String) intent.getStringExtra("nik");
        dialog = new Dialog(DetailAdmin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        final TextView tt = dialog.findViewById(R.id.text_pesan);
        tt.setText("memuat..");
        dialog.show();
        getDetail(niko);
    }
    private void getDetail(final String niknya){
        String url_provinsi="https://crb-dev.id/kesehatan/get_detail.php?nik="+niknya;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        Log.wtf("URL Called", url_provinsi + "");
        penyakitList = new ArrayList<>();
        diagnosaList =  new ArrayList<>();
        suratList = new ArrayList<>();
        tglList=new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                url_provinsi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(Soal.class.getSimpleName(), "Register Response: " + response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jArray = jsonObject.getJSONArray("content");
                    JSONArray jDetail = jsonObject.getJSONArray("detail");
                    JSONArray jTotal = jsonObject.getJSONArray("total");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject1=jArray.getJSONObject(i);
                         txtNama =jsonObject1.getString("nama");
                         txtAlamat =jsonObject1.getString("alamat");
                         txtJk = jsonObject1.getString("jk");
                         txtUmur = jsonObject1.getString("umur");
                         txtNik = jsonObject1.getString("nik");
                    }
                    for(int x=0;x<jTotal.length();x++) {
                        JSONObject totalGet = jTotal.getJSONObject(x);
                        txtTotal = totalGet.getString("total_diagnosa");
                        txtTidak = totalGet.getString("total_tidak");
                    }
                    for(int h=0;h<jDetail.length();h++){
                        JSONObject dataGet = jDetail.getJSONObject(h);
                        String penyakit = dataGet.getString("penyakit");
                        String diagnosa = "Status "+dataGet.getString("diagnosa");
                        String surat = "Surat Dokter : "+dataGet.getString("surat");
                        String tanggal = "Survey telah dilakukan pada "+dataGet.getString("tanggal");
                        penyakitList.add(penyakit);
                        diagnosaList.add(diagnosa);
                        suratList.add(surat);
                        tglList.add(tanggal);
                    }
                    total_d.setText(txtTotal);
                    tidak_d.setText(txtTidak);
                    nama.setText(txtNama);
                    alamat.setText(txtAlamat);
                    jk.setText("Jenis Kelamin "+txtJk);
                    umur.setText("Usia : "+txtUmur+" Tahun");
                    nik.setText(txtNik);
                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rc_detail);
                    DetailAdapter mDataAdapter = new DetailAdapter( penyakitList, diagnosaList,
                            suratList, tglList);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(DetailAdmin.this,1);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mDataAdapter);
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }catch (JSONException e){e.printStackTrace(); }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(DetailAdmin.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        int socketTimeout = 1000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    public void showDialog(String kata){
        dialog = new Dialog(DetailAdmin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        final TextView tt = dialog.findViewById(R.id.text_pesan);
        tt.setText(kata);
        dialog.show();
    }
}
