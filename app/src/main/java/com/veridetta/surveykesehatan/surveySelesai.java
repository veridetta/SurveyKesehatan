package com.veridetta.surveykesehatan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class surveySelesai extends AppCompatActivity {
    Button btn_lagi;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_selesai);
        btn_lagi = findViewById(R.id.btn_survey_lagi);
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        btn_lagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("skip", false);
                editor.remove("nik");
                editor.putString("tipe","");
                editor.remove("selesai");
                editor.commit();
                Intent f = new Intent(surveySelesai.this, DataDiri.class);
                startActivity(f);
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
