package com.veridetta.surveykesehatan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.veridetta.surveykesehatan.server.DaftarResponden;

import retrofit2.Call;
import retrofit2.Callback;

import static com.veridetta.surveykesehatan.server.DaftarResponden.retrofit;

public class DataDiri extends AppCompatActivity {
    EditText input_nik, input_nama, input_umur, input_alamat;
    Spinner input_jk;
    String[] arrayJK;
    Button btn_daftar;
    LinearLayout loginA;
    int ii = 1;
    Dialog dialog;
    SharedPreferences sharedpreferences;
    boolean session;
    String responden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_diri);
        //initial form
        input_nik = findViewById(R.id.input_nik);
        input_nama = findViewById(R.id.input_nama);
        input_umur = findViewById(R.id.input_umur);
        input_alamat = findViewById(R.id.input_alamat);
        input_jk = findViewById(R.id.input_jk);
        arrayJK = new String[]{"Laki-laki", "Perempuan"};
            //initial spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.jk_layout, arrayJK);
        adapter.setDropDownViewResource(R.layout.jk_layout);
        loginA = findViewById(R.id.login_admin);
        loginA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nn = new Intent(DataDiri.this, AdminLogin.class);
                startActivity(nn);
            }
        });
        input_jk.setAdapter(adapter);
        //initial dialog
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_loading);
        TextView tt = dialog.findViewById(R.id.text_pesan);
        tt.setText("Menyimpan data...");
        btn_daftar = findViewById(R.id.btn_daftar);
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtnik, txtnama, txtumur, txtalamat, txtjk;
                int jumlah = 0;
                txtnik = input_nik.getText().toString();
                txtnama = input_nama.getText().toString();
                txtumur = input_umur.getText().toString();
                txtalamat = input_alamat.getText().toString();
                txtjk = input_jk.getSelectedItem().toString();
                if(txtnik.length()>1){
                    jumlah++;
                }
                if(txtnama.length()>1){
                    jumlah++;
                }
                if(txtumur.length()>1){
                    jumlah++;
                }
                if(txtalamat.length()>1){
                    jumlah++;
                }
                if(jumlah == 4){
                    cobaPost(txtnik, txtnama,txtumur,txtalamat,txtjk);
                    dialog.show();
                }else{
                    Toast.makeText(DataDiri.this, "Mohon periksa kembali data anda", Toast.LENGTH_LONG).show();
                }
            }
        });
        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean("session_status", false);
        responden = sharedpreferences.getString("tipe",null);
        if (session){
            String res = "responden";
            if (responden.equals(res)){
                Intent ii = new Intent(DataDiri.this, Soal.class);
                startActivity(ii);
            }
        }
    }
    private void cobaPost(final String nik, final String nama, final String umur,
                          final String alamat, final String jk){
        DaftarResponden scalarService = retrofit.create(DaftarResponden.class);
        Call<String> stringCall = scalarService.postResponden(nik, nama, umur,alamat
                ,jk);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e(DataDiri.class.getSimpleName(), "Register Response: " + response.body());
                Toast.makeText(DataDiri.this,response.body(),Toast.LENGTH_LONG).show();
                String berhasil = "Data disimpan!";
                if(berhasil.equals(response.body())){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("session_status", true);
                    editor.putString("nik", input_nik.getText().toString());
                    editor.putString("tipe", "responden");
                    editor.commit();
                    Intent ii = new Intent(DataDiri.this, Soal.class);
                    startActivity(ii);
                    dialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Log.wtf("", t.toString(), t);
                Log.d(DataDiri.class.getSimpleName(), t.toString());
                Toast.makeText(DataDiri.this,"Silahkan ulangi lagi"+t.toString(),Toast.LENGTH_LONG).show();
            }
        });
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

