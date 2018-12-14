package com.example.harsayamani.kuisacak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder>{


    private ArrayList<DataSoal> listSoal;
    private Context context;
    private FirebaseAuth auth;

    public RecyclerViewAdapter1(ArrayList<DataSoal> listSoal, Context context) {
        this.listSoal = listSoal;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama_akun;
        private TextView soal;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            nama_akun = itemView.findViewById(R.id.nama_akun_saya);
            soal = itemView.findViewById(R.id.soal_saya);
            ListItem = itemView.findViewById(R.id.listItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_daftar_soal, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        auth = FirebaseAuth.getInstance();
        String soal_saya = listSoal.get(position).getSoal();
        String displayName = listSoal.get(position).getAuthor();
        holder.nama_akun.setText("Author : "+displayName);
        holder.soal.setText("Soal : \n" + soal_saya);

    }

    @Override
    public int getItemCount() {
        return listSoal.size();
    }

}