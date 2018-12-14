package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class RecyclerViewAdapter3 extends RecyclerView.Adapter<RecyclerViewAdapter3.ViewHolder>{


    private ArrayList<DataRiwayat> listRiwayat;
    private Context context;

    public RecyclerViewAdapter3(ArrayList<DataRiwayat> listRiwayat, Context context) {
        this.listRiwayat = listRiwayat;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nomor, nama, score, exp;

        ViewHolder(View itemView) {
            super(itemView);
            nomor = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
            score = itemView.findViewById(R.id.score);
            exp = itemView.findViewById(R.id.exp);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_riwayat_main, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int nomor = listRiwayat.get(position).getNomor();
        String nama = listRiwayat.get(position).getNama();
        int score = listRiwayat.get(position).getScore();
        int exp = listRiwayat.get(position).getExp();
        holder.nomor.setText(String.valueOf(nomor));
        holder.nama.setText(String.valueOf(nama));
        holder.score.setText("Score :"+String.valueOf(score));
        holder.exp.setText("Exp :"+String.valueOf(exp));
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

}
