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

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder>{

    private ArrayList<DataLeaderboard> listPemain;
    private Context context;
    private FirebaseAuth auth;

    RecyclerViewAdapter2(ArrayList<DataLeaderboard> listPemain, Context context) {
        this.listPemain= listPemain;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView no;
        private TextView nama;
        private TextView score;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
            score = itemView.findViewById(R.id.score);
            ListItem = itemView.findViewById(R.id.listItem2);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_leader_board, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        int no = listPemain.get(position).getNomor();
        String nama = listPemain.get(position).getNama();
        int score = listPemain.get(position).getScore();
        if (listPemain.get(position).getNomor() == 1){
            holder.no.setTextSize(18);
            holder.nama.setTextSize(18);
            holder.score.setTextSize(18);
        }
        else if (listPemain.get(position).getNomor() == 2){
            holder.no.setTextSize(17);
            holder.nama.setTextSize(17);
            holder.score.setTextSize(17);
        }
        else if (listPemain.get(position).getNomor() == 3){
            holder.no.setTextSize(16);
            holder.nama.setTextSize(16);
            holder.score.setTextSize(16);
        }else{
            holder.no.setTextSize(15);
            holder.nama.setTextSize(15);
            holder.score.setTextSize(15);
        }
        holder.no.setText(String.valueOf(no));
        holder.nama.setText(nama);
        holder.score.setText(String.valueOf(score));
    }

    @Override
    public int getItemCount() {
        return listPemain.size();
    }
}
