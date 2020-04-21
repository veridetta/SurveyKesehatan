package com.veridetta.surveykesehatan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veridetta.surveykesehatan.R;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    private ArrayList<String> penyakitList = new ArrayList<>();
    private ArrayList<String> diagnosaList = new ArrayList<>();
    private ArrayList<String> suratList = new ArrayList<>();
    private ArrayList<String> tglList = new ArrayList<>();
    private Activity mActivity;
    Intent intent;
    String dx;
    private Context context;
    private int lastPosition = -1;

    public DetailAdapter( ArrayList<String> penyakitList,
                           ArrayList<String> diagnosaList,
                           ArrayList<String> suratList,
                          ArrayList<String> tglList) {

        this.penyakitList = penyakitList;
        this.diagnosaList = diagnosaList;
        this.suratList = suratList;
        this.tglList = tglList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPenyakit, txtDiagnosa, txtSurat, txtTgl;
        CardView cardBaru;
        public MyViewHolder(View view) {
            super(view);
            txtPenyakit = (TextView) view.findViewById(R.id.detail_penyakit);
            txtDiagnosa = (TextView) view.findViewById(R.id.detail_status);
            txtSurat = (TextView) view.findViewById(R.id.detail_surat);
            txtTgl= (TextView) view.findViewById(R.id.detail_tanggal);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtPenyakit.setText(penyakitList.get(position));
        holder.txtDiagnosa.setText(diagnosaList.get(position));
        holder.txtSurat.setText(suratList.get(position));
        holder.txtTgl.setText(tglList.get(position));
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }
}
