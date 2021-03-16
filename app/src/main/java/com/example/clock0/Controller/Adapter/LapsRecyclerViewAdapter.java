package com.example.clock0.Controller.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock0.Model.Lap;
import com.example.clock0.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Yassine Abou on 2/28/2021.
 */
public class LapsRecyclerViewAdapter extends RecyclerView.Adapter<LapsRecyclerViewAdapter.LapsViewHolder> {

    ArrayList<Lap> listLaps = new ArrayList<Lap>();



    @NonNull
    @Override
    public LapsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.laps_rows, null, false);
        return new LapsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LapsViewHolder holder, int position) {
        Lap mLaps = listLaps.get(position);
        holder.textId.setText((position + 1) + ".");
        holder.textTime.setText(getFormattedTimeFromMill(listLaps.get(position).getTime()));

    }

    private String getFormattedTimeFromMill(Long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        return String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds);

    }

    @Override
    public int getItemCount() {
        return listLaps.size();
    }

    public void addNewLap(Lap lap) {
        listLaps.add(lap);
        notifyItemInserted(listLaps.size() - 1);
    }

    public void deleteAllLaps() {
        listLaps.clear();
        notifyDataSetChanged();
    }


    class LapsViewHolder extends RecyclerView.ViewHolder {

        TextView textId, textTime;

        public LapsViewHolder(@NonNull View itemView) {
            super(itemView);

            textId = itemView.findViewById(R.id.row_lap_id);
            textTime = itemView.findViewById(R.id.row_lap_time);
        }
    }

}
