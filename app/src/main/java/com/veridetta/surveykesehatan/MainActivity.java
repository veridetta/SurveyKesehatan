package com.veridetta.surveykesehatan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn;
    SharedPreferences sharedpreferences;
    String responden ;
    Boolean skip, selesai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("skip", true);
                editor.commit();
                Intent f = new Intent(MainActivity.this, DataDiri.class);
                startActivity(f);
            }
        });
        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        skip = sharedpreferences.getBoolean("skip", false);
        responden = sharedpreferences.getString("tipe","");
        selesai = sharedpreferences.getBoolean("selesai",false);
        String res = "responden";
        if(skip){
            if(selesai){
                Intent fx = new Intent(MainActivity.this, surveySelesai.class);
                startActivity(fx);
            }else{
                if(responden.equals(res)){
                    Intent fx = new Intent(MainActivity.this, Soal.class);
                    startActivity(fx);
                }else{
                    Intent fx = new Intent(MainActivity.this, DataDiri.class);
                    startActivity(fx);
                }
            }
        }
    }
}

