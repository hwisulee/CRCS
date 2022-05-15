package com.teamcrop.CRCS;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {
    private final List<UserData> dataList;
    private final Activity context;
    private RoomDB database;

    public interface onItemClickListener {
        void onItemClick(View v, int pos);
    }

    private onItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public UserDataAdapter(Activity context, List<UserData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserDataAdapter.ViewHolder holder, int position) {
        final UserData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.textView1.setText(data.getName());
        holder.textView2.setText(data.getAddr());

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1, textView2;
        ImageButton editbtn, delbtn;

        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.list_location);
            textView1 = view.findViewById(R.id.list_addr1);
            textView2 = view.findViewById(R.id.list_addr2);
            editbtn = view.findViewById(R.id.list_editbtn);
            delbtn = view.findViewById(R.id.list_delbtn);
        }
    }
}
