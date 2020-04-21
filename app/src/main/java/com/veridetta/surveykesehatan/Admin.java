package com.veridetta.surveykesehatan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
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
import com.veridetta.surveykesehatan.adapter.RespondenAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<String> namaList, alamatList, tglList, jkList, umurList, nikList;
    Dialog dialog;
    TextView totalD, totalR, tidak;
    String txtNama, txtAlamat, txtJk, txtUmur, txtNik, txtTgl, txtTotalR, txtTotalD, txtTidak;
    LinearLayout res_kosong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        dialog = new Dialog(Admin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        final TextView tt = dialog.findViewById(R.id.text_pesan);
        res_kosong = findViewById(R.id.res_kosong);
        totalD = findViewById(R.id.total_diagnosa);
        totalR = findViewById(R.id.total_responden);
        tidak = findViewById(R.id.total_tidak);
        tt.setText("Memuat ...");
        dialog.show();
        getResponden();
    }
    private void getResponden(){
        String url_provinsi="https://crb-dev.id/kesehatan/get_responden.php?key=D2102";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        Log.wtf("URL Called", url_provinsi + "");
        namaList = new ArrayList<>();
        alamatList =  new ArrayList<>();
        jkList = new ArrayList<>();
        nikList =new ArrayList<>();
        tglList=new ArrayList<>();
        umurList=new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                url_provinsi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(Soal.class.getSimpleName(), "Register Response: " + response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jArray = jsonObject.getJSONArray("content");
                    JSONArray jTotal = jsonObject.getJSONArray("total");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject1=jArray.getJSONObject(i);
                         txtNama =jsonObject1.getString("nama");
                         txtAlamat =jsonObject1.getString("alamat");
                         txtJk = jsonObject1.getString("jk");
                         txtUmur = jsonObject1.getString("umur")+" Tahun";
                         txtNik = jsonObject1.getString("nik");
                         txtTgl = jsonObject1.getString("tgl");
                        namaList.add(txtNama);
                        alamatList.add(txtAlamat);
                        jkList.add(txtJk);
                        umurList.add(txtUmur);
                        nikList.add(txtNik);
                        tglList.add(txtTgl);
                        res_kosong.setVisibility(View.GONE);
                    }
                    for(int m=0;m<jTotal.length();m++) {
                        JSONObject getTotal = jTotal.getJSONObject(m);
                        txtTotalD = getTotal.getString("total_diagnosa");
                        txtTidak = getTotal.getString("total_tidak");
                        txtTotalR = getTotal.getString("total_responden");
                    }
                    totalD.setText(txtTotalD);
                    totalR.setText(txtTotalR);
                    tidak.setText(txtTidak);
                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rc_responden);
                    RespondenAdapter mDataAdapter = new RespondenAdapter( namaList, alamatList,
                            tglList, jkList, umurList  ,nikList);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Admin.this,1);
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
                Toast.makeText(Admin.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    public void showDialog(String kata){
        dialog = new Dialog(Admin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        final TextView tt = dialog.findViewById(R.id.text_pesan);
        tt.setText(kata);
        dialog.show();
    }
}
