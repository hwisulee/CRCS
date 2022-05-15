package com.teamcrop.CRCS;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantDataAdapter extends RecyclerView.Adapter<PlantDataAdapter.ViewHolder> {
    private List<PlantData> dataList;
    private Activity context;
    private RoomDB database;

    public interface onItemClickListener {
        void onItemClick(View v, int pos);
    }

    private onItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public PlantDataAdapter(Activity context, List<PlantData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlantDataAdapter.ViewHolder holder, int position) {
        final PlantData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        //holder.textView.setText(data.getFirst() + ", " + data.getSecond() + ", " + data.getThird() + "\n" + data.getNx() + ", " + data.getNy() + "\n" + data.getLat() + ", " + data.getLon() + "\n" + data.getTmx() + ", " + data.getTmy());

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.aa);
        }
    }
}
