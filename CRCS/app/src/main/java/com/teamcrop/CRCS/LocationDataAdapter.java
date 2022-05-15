package com.teamcrop.CRCS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationDataAdapter extends RecyclerView.Adapter<LocationDataAdapter.ViewHolder> {
    private final List<LocationData> dataList;
    private final Activity context;
    private RoomDB database;

    public interface onItemClickListener {
        void onItemClick(View v, int pos);
    }

    private onItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public LocationDataAdapter(Activity context, List<LocationData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final LocationDataAdapter.ViewHolder holder, int position) {
        final LocationData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.textView.setText(data.getFirst() + ", " + data.getSecond() + ", " + data.getThird() + ", " + data.getFourth() + "\n" + data.getNx() + ", " + data.getNy() + "\n" + data.getLat() + ", " + data.getLon() + "\n" + data.getTmx() + ", " + data.getTmy());

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull final LocationDataAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final LocationDataAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.aa);
        }
    }
}
