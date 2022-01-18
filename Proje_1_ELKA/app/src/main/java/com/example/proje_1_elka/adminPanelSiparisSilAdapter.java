package com.example.proje_1_elka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adminPanelSiparisSilAdapter extends RecyclerView.Adapter<adminPanelSiparisSilAdapter.SiparisHolder> {
    private ArrayList<String> siparisNoFromFbRec;
    private ArrayList<String> SiparisiAlanEmailFromFbRec;
    private ArrayList<String> SiparisiVerenEmailFromFbRec;



    public adminPanelSiparisSilAdapter(ArrayList<String> siparisNoFromFbRec, ArrayList<String> SiparisiAlanEmailFromFbRec, ArrayList<String> SiparisiVerenEmailFromFbRec) {
        this.siparisNoFromFbRec = siparisNoFromFbRec;
        this.SiparisiAlanEmailFromFbRec = SiparisiAlanEmailFromFbRec;
        this.SiparisiVerenEmailFromFbRec = SiparisiVerenEmailFromFbRec;

    }

    @NonNull
    @Override
    public SiparisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recadminsiparissil,parent,false);
        return new SiparisHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SiparisHolder holder, int position) {
        holder.TVsiparisNo.setText(siparisNoFromFbRec.get(position));
        holder.TVsiparisAlan.setText(SiparisiAlanEmailFromFbRec.get(position));
        holder.TVsiparisVeren.setText(SiparisiVerenEmailFromFbRec.get(position));


    }

    @Override
    public int getItemCount() {
        return siparisNoFromFbRec.size();

    }

    class SiparisHolder extends RecyclerView.ViewHolder{


        TextView TVsiparisNo;
        TextView TVsiparisAlan;
        TextView TVsiparisVeren;

        public SiparisHolder(@NonNull View itemView) {
            super(itemView);

            TVsiparisNo=itemView.findViewById(R.id.TVsiparisNo);
            TVsiparisAlan=itemView.findViewById(R.id.TVsiparisAlan);
            TVsiparisVeren=itemView.findViewById(R.id.TVsiparisVeren);

        }
    }
}