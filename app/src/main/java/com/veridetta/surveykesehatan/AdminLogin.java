package com.veridetta.surveykesehatan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
    EditText secKey;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        secKey = findViewById(R.id.sec_key);
        btnLogin = findViewById(R.id.btn_key);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String security = "D2102";
                if(secKey.getText().toString().equals(security)){
                    Intent j = new Intent(AdminLogin.this, Admin.class);
                    startActivity(j);
                }else{
                    Toast.makeText(AdminLogin.this, "Harap Coba Lagi!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
