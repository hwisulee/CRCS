package com.teamcrop.CRCS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    List<UserData> UserdataList = new ArrayList<>();

    RecyclerView recyclerView;
    Adapter adapter;

    RoomDB database;

    ImageView list_location;
    ImageButton listaddbtn, listokbtn;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_userlist);

        database = RoomDB.getInstance(this);

        list_location = findViewById(R.id.list_location);
        listaddbtn = findViewById(R.id.userlist_addbtn);
        listokbtn = findViewById(R.id.userlist_okbtn);

        recyclerView = findViewById(R.id.userlist_addrlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(getBaseContext(), UserdataList);
        recyclerView.setAdapter(adapter);

        UserdataList.clear();

        SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
        int checkedList = pref.getInt("checkedList", 0);

        if (UserdataList.size() != 0) {
            UserdataList.addAll(database.userDao().getAll());
            adapter.setCheckedList(checkedList);
            adapter.notifyDataSetChanged();
        }

        // OnClickListener
        listaddbtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), UserSetupActivity.class);
            startActivity(intent);
        });

        listokbtn.setOnClickListener(view -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("checkedList", adapter.getCheckedList());
            editor.apply();
            finish();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        UserdataList.clear();
        UserdataList.addAll(database.userDao().getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
        int checkedList = pref.getInt("checkedList", 0);

        if (checkedList >= UserdataList.size()) {
            SharedPreferences.Editor editor = pref.edit();

            if (UserdataList.size() != 0) {
                editor.putInt("checkedList", UserdataList.size() - 1);
            }
            else {
                editor.putInt("checkedList", 0);
            }
            editor.apply();
        }
    }

    public class Adapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {
        private final Context context;
        private int CheckedList;
        private int temp;
        private UserDataAdapter.ViewHolder view;

        List<UserData> UserdataList = new ArrayList<>();

        public int getCheckedList() { return CheckedList; }

        public void setCheckedList(int checkedList) {
            CheckedList = checkedList;
        }

        @SuppressLint("NotifyDataSetChanged")
        public Adapter(Context context, List<UserData> userdataList) {
            SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
            CheckedList = pref.getInt("checkedList", 0);
            this.context = context;
            this.UserdataList = userdataList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UserDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_user, parent, false);
            return new UserDataAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final UserDataAdapter.ViewHolder holder, int position) {
            final UserData data = UserdataList.get(position);
            database = RoomDB.getInstance(getBaseContext());
            holder.textView1.setText(data.getName());
            holder.textView2.setText(data.getAddr());

            int checkHolder = holder.getAdapterPosition();

            if (checkHolder == CheckedList) {
                temp = CheckedList;
                holder.imageView.setImageResource(R.drawable.ic_baseline_location_on_24);
            }

            holder.itemView.setOnClickListener(view -> {
                if (CheckedList != position) {
                    setCheckedList(position);
                    updateItem(temp, position);
                    temp = CheckedList;
                }
            });

            holder.editbtn.setOnClickListener(view -> {
                UserData userData = UserdataList.get(holder.getAdapterPosition());

                final int ID = userData.getId();
                int DataId = userData.getDataID();
                String Name = userData.getName();
                String Addr = userData.getAddr();

                Intent intent = new Intent(getBaseContext(), UserSetupActivity.class);
                intent.putExtra("ResultCode", 1001);
                intent.putExtra("ID", ID);
                intent.putExtra("DataId", DataId);
                intent.putExtra("Name", Name);
                intent.putExtra("Addr", Addr);
                intent.putExtra("MyPointGeo0", userData.getLat());
                intent.putExtra("MyPointGeo1", userData.getLon());
                launcher.launch(intent);
            });

            holder.delbtn.setOnClickListener(view -> {
                UserData userData = UserdataList.get(holder.getAdapterPosition());
                database.userDao().delete(userData);
                onResume();
            });
        }

        private void updateItem(int oldPosition, int newPosition) {
            view = (UserDataAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(oldPosition);
            if (view != null) {
                view.imageView.setImageResource(R.drawable.ic_outline_location_on_24);
            }

            view = (UserDataAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(newPosition);
            if (view != null) {
                view.imageView.setImageResource(R.drawable.ic_baseline_location_on_24);
            }
        }

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> { });

        @Override
        public int getItemCount() { return UserdataList.size(); }
    }
}