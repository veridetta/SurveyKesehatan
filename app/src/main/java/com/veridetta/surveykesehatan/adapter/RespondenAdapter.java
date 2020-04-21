package com.veridetta.surveykesehatan.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.veridetta.surveykesehatan.DetailAdmin;
import com.veridetta.surveykesehatan.R;

import java.util.ArrayList;

public class RespondenAdapter extends RecyclerView.Adapter<RespondenAdapter.MyViewHolder> {

    private ArrayList<String> namaList = new ArrayList<>(),
            alamatList= new ArrayList<>(),
            tglList= new ArrayList<>(),
            jkList= new ArrayList<>(),
            umurList= new ArrayList<>(),
            nikList= new ArrayList<>();
    private Activity mActivity;
    Intent intent;
    String dx;
    private Context context;
    private int lastPosition = -1;

    public RespondenAdapter( ArrayList<String> namaList,
                             ArrayList<String> alamatList,
                             ArrayList<String>tglList,
                             ArrayList<String> jkList,
                             ArrayList<String> umurList,
                             ArrayList<String> nikList) {
        this.namaList = namaList;
        this.alamatList = alamatList;
        this.jkList = jkList;
        this.umurList = umurList;
        this.nikList = nikList;
        this.tglList = tglList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNama, txtAlamat, txtJk, txtUmur, txtNik, txtTgl;
        CardView cardResponden;
        ImageView bghome;
        public MyViewHolder(View view) {
            super(view);
            txtNama = (TextView) view.findViewById(R.id.res_nama);
            txtAlamat = (TextView) view.findViewById(R.id.res_alamat);
            txtJk = (TextView) view.findViewById(R.id.res_jk);
            txtUmur= (TextView) view.findViewById(R.id.res_usia);
            txtNik = (TextView) view.findViewById(R.id.res_nik);
            txtTgl= (TextView) view.findViewById(R.id.res_tgl);
            cardResponden = view.findViewById(R.id.card_responden);
            bghome = view.findViewById(R.id.bg_home);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_responden, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtNama.setText(namaList.get(position));
        holder.txtAlamat.setText(alamatList.get(position));
        holder.txtJk.setText(jkList.get(position));
        holder.txtUmur.setText(umurList.get(position));
        holder.txtNik.setText(nikList.get(position));
        holder.txtTgl.setText(tglList.get(position));
        holder.cardResponden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jh = nikList.get(position).toString();
                Intent p = new Intent(v.getContext()
                        , DetailAdmin.class);
                p.putExtra("nik", jh);
                v.getContext().startActivity(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return namaList.size();
    }
}

