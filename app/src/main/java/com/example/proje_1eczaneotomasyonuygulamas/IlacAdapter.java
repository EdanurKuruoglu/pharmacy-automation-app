package com.example.proje_1eczaneotomasyonuygulamas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IlacAdapter extends RecyclerView.Adapter<IlacAdapter.IlacViewHolder> {
    private List<Ilac_2> ilacList;

    public IlacAdapter(List<Ilac_2> ilacList) {
        this.ilacList = ilacList;
    }

    @NonNull
    @Override
    public IlacViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ilac_item, parent, false);
        return new IlacViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IlacViewHolder holder, int position) {
        Ilac_2 ilac_2 = ilacList.get(position);
        holder.ilacAdiTextView.setText(ilac_2.getIlacAdi());
        holder.barkodTextView.setText(ilac_2.getBarkod());
    }

    @Override
    public int getItemCount() {
        return ilacList.size();
    }
    public List<Ilac_2> getIlacList() {
        return ilacList;
    }

    public static class IlacViewHolder extends RecyclerView.ViewHolder {
        TextView ilacAdiTextView;
        TextView barkodTextView;

        public IlacViewHolder(@NonNull View itemView) {
            super(itemView);
            ilacAdiTextView = itemView.findViewById(R.id.ilacAdiTextView);
            barkodTextView = itemView.findViewById(R.id.barkodTextView);
        }
    }
}
