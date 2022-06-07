package com.teamcrop.CRCS;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HashKey";
    static Context context;
    static String KakaoKey = null;
    static String restkey = null;
    static String service_key = null;

    String UltraSrtFcstData, VilageFcstData, MyReverseGeo, StationReverseGeo;
    String name;
    String T1H, REH, SKY, PTY;

    List<UserData> UserdataList = new ArrayList<>();

    UserData userData = new UserData();
    PlantData plantData = new PlantData();

    MainHandler mainHandler;
    SeekBarOnChangedListener onChangedListener;
    BtnOnClickListener onClickListener;
    ProgressDialog StartProgressDialog;
    AlertDialog dlg;
    RoomDB database;

    AppBarLayout appBarLayout;
    ConstraintLayout main_listbtn;
    ImageButton main_alertbtn, main_settingbtn, temMbtn, temPbtn, rehMbtn, rehPbtn, btnav_btn;
    ImageView weatherimg;
    TextView tem, tem_data, addr, main_listbtn_text, main_plantState, temSeekBartxt, rehSeekBartxt;
    TextView btnav_tem, btnav_reh;
    ArcSeekBar temSeekBar, rehSeekBar;
    BottomNavigationView btnav;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // apiKey setting
        MainActivity.context = getApplicationContext();
        KakaoKey = context.getString(R.string.kakaoKey);
        restkey = context.getString(R.string.restKey);
        service_key = context.getString(R.string.apiKey);

        database = RoomDB.getInstance(this);

        // Refresh event that occurs when this app is launched
        StartProgressDialog = new ProgressDialog(this);
        StartProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mainHandler = new MainHandler(Looper.getMainLooper());
        dlg = new AlertDialog.Builder(MainActivity.this).create();

        onClickListener = new BtnOnClickListener();
        onChangedListener = new SeekBarOnChangedListener();

        SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
        int checkedList = pref.getInt("checkedList", 0);

        // appBarAnimator
        appBarLayout = findViewById(R.id.main_appBarLayout);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> updateViews(Math.abs(verticalOffset / appBarLayout.getTotalScrollRange())));

        // findViewById
        tem = findViewById(R.id.main_tem);
        tem_data = findViewById(R.id.main_tem_data);
        addr = findViewById(R.id.main_addr);
        weatherimg = findViewById(R.id.main_weatherimg);
        main_listbtn = findViewById(R.id.main_listbtn);
        main_listbtn_text = findViewById(R.id.main_listbtn_text);
        main_alertbtn = findViewById(R.id.main_alertbtn);
        main_settingbtn = findViewById(R.id.main_settingbtn);
        main_plantState = findViewById(R.id.main_plantState);
        temSeekBar = findViewById(R.id.main_temseekbar);
        temSeekBartxt = findViewById(R.id.main_temseekbartxt);
        temMbtn = findViewById(R.id.main_temseekbarminusbtn);
        temPbtn = findViewById(R.id.main_temseekbarplusbtn);
        rehSeekBar = findViewById(R.id.main_rehseekbar);
        rehSeekBartxt = findViewById(R.id.main_rehseekbartxt);
        rehMbtn = findViewById(R.id.main_rehseekbarminusbtn);
        rehPbtn = findViewById(R.id.main_rehseekbarplusbtn);
        btnav = findViewById(R.id.btnav);
        btnav_tem = findViewById(R.id.btnav_temv);
        btnav_reh = findViewById(R.id.btnav_rehv);
        btnav_btn = findViewById(R.id.btnav_btn);

        btnav.setVisibility(View.GONE);

        // onClickListener
        main_listbtn.setOnClickListener(onClickListener);
        main_alertbtn.setOnClickListener(onClickListener);
        main_settingbtn.setOnClickListener(onClickListener);
        btnav_btn.setOnClickListener(onClickListener);

        temMbtn.setOnClickListener(onChangedListener);
        temPbtn.setOnClickListener(onChangedListener);
        rehMbtn.setOnClickListener(onChangedListener);
        rehPbtn.setOnClickListener(onChangedListener);

        temSeekBar.setOnProgressChangedListener(onChangedListener);
        rehSeekBar.setOnProgressChangedListener(onChangedListener);

        // onTouchListener
        temSeekBar.setOnTouchListener(onChangedListener);
        rehSeekBar.setOnTouchListener(onChangedListener);
        temMbtn.setOnTouchListener(onChangedListener);
        temPbtn.setOnTouchListener(onChangedListener);
        rehMbtn.setOnTouchListener(onChangedListener);
        rehPbtn.setOnTouchListener(onChangedListener);

        // check userdata
        UserdataList.clear();
        UserdataList.addAll(database.userDao().getAll());
        System.out.println(UserdataList.size());
    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.main_listbtn) {
                Intent intent = new Intent(getBaseContext(), UserListActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.main_alertbtn) {
                System.out.println("Clicked alertbtn");
            }
            else if (id == R.id.main_settingbtn) {
                System.out.println("Clicked settingbtn");
                Intent intent = new Intent(getBaseContext(), OssLicensesMenuActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.btnav_btn) {
                System.out.println("Clicked btnav_btn");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference tem = myRef.child("temStatus").child("temSetValue");
                        DatabaseReference reh = myRef.child("rehStatus").child("rehSetValue");

                        tem.setValue(temSeekBar.getProgress());
                        reh.setValue(rehSeekBar.getProgress());

                        btnav.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    class SeekBarOnChangedListener implements View.OnTouchListener, View.OnClickListener, ProgressListener {
        View main_view;

        @Override
        public void invoke(int progress) {
            int id = main_view.getId();

            if (id == R.id.main_temseekbar) {
                temSeekBartxt.setText(getString(R.string.bar_tem, Integer.toString(progress)));
                btnav_tem.setText(getString(R.string.bar_tem, Integer.toString(temSeekBar.getProgress())));
                btnav.setVisibility(View.VISIBLE);
            }
            else if (id == R.id.main_rehseekbar) {
                rehSeekBartxt.setText(getString(R.string.bar_reh, Integer.toString(progress)));
                btnav_reh.setText(getString(R.string.bar_reh, Integer.toString(rehSeekBar.getProgress())));
                btnav.setVisibility(View.VISIBLE);
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            main_view = view;
            int action = motionEvent.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN :
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP :
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            view.onTouchEvent(motionEvent);
            return true;
        }

        @Override
        public void onClick(View view) {
            main_view = view;
            int id = view.getId();

            if (id == R.id.main_temseekbarminusbtn) {
                temSeekBar.setProgress(temSeekBar.getProgress() - 1);
                temSeekBartxt.setText(getString(R.string.bar_tem, Integer.toString(temSeekBar.getProgress())));
                btnav_tem.setText(getString(R.string.bar_tem, Integer.toString(temSeekBar.getProgress())));
                btnav.setVisibility(View.VISIBLE);
            }
            else if (id == R.id.main_temseekbarplusbtn) {
                temSeekBar.setProgress(temSeekBar.getProgress() + 1);
                temSeekBartxt.setText(getString(R.string.bar_tem, Integer.toString(temSeekBar.getProgress())));
                btnav_tem.setText(getString(R.string.bar_tem, Integer.toString(temSeekBar.getProgress())));
                btnav.setVisibility(View.VISIBLE);
            }
            else if (id == R.id.main_rehseekbarminusbtn) {
                rehSeekBar.setProgress(rehSeekBar.getProgress() - 1);
                rehSeekBartxt.setText(getString(R.string.bar_reh, Integer.toString(rehSeekBar.getProgress())));
                btnav_reh.setText(getString(R.string.bar_reh, Integer.toString(rehSeekBar.getProgress())));
                btnav.setVisibility(View.VISIBLE);
            }
            else if (id == R.id.main_rehseekbarplusbtn) {
                rehSeekBar.setProgress(rehSeekBar.getProgress() + 1);
                rehSeekBartxt.setText(getString(R.string.bar_reh, Integer.toString(rehSeekBar.getProgress())));
                btnav_reh.setText(getString(R.string.bar_reh, Integer.toString(rehSeekBar.getProgress())));
                btnav.setVisibility(View.VISIBLE);
            }
        }
    }

    class MainHandler extends Handler {
        private final Looper mainLooper;

        public MainHandler(Looper mainLooper) {
            this.mainLooper = Looper.getMainLooper();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
            int checkedList = pref.getInt("checkedList", 0);

            StartProgressDialog.dismiss();
            runOnUiThread(() -> {
                main_listbtn_text.setText(getString(R.string.list_txt, UserdataList.get(checkedList).getName(), UserdataList.get(checkedList).getAddr()));
                name = UserdataList.get(checkedList).getName();
                MyReverseGeo = UserdataList.get(checkedList).getAddr();
                getNcastView();
            });
        }
    }

    class bgThread extends Thread {
        private final Thread targetThread;
        private State bgState;

        public bgThread(TargetThread targetThread) {
            this.targetThread = targetThread;
        }

        @NonNull
        public State getState() { return bgState; }

        public void run() {
            while(true) {
                Thread.State state = targetThread.getState();
                System.out.println("Thread state : " + state);

                if (state == Thread.State.NEW) {
                    targetThread.start();
                }

                if (state == State.TERMINATED) {
                    bgState  = state;
                    mainHandler.sendEmptyMessage(0);
                    break;
                }

                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class TargetThread extends Thread {
        public void run() {
            UltraSrtFcstData = XmlParsing.getXmlUltraSrtFcstData(UserdataList.get(0).getDbx(), UserdataList.get(0).getDby());
            VilageFcstData = XmlParsing.getXmlVilageFcstData(UserdataList.get(0).getDbx(), UserdataList.get(0).getDby());
        }
    }

    // when Scroll down to change appbar
    private void updateViews(float offset) {
        System.out.println(offset);

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();

            if (intent != null) {
                MyReverseGeo = intent.getExtras().getString("MyReverseGeo");
                StationReverseGeo = intent.getExtras().getString("StationReverseGeo");
                name = intent.getExtras().getString("name");
            }
        }
    });

    public void checkedData() {
        if (UserdataList.size() == 0) {
            // changing UI
            weatherimg.setImageResource(0);
            tem.setText("");
            tem_data.setText("");
            addr.setText("");
            main_listbtn_text.setText("");

            dlg.setTitle("데이터 미확인");
            dlg.setMessage("유저 정보가 없습니다." + "\n" + "정보를 작성하려면 '예'를 클릭해주세요.");
            dlg.setButton(DialogInterface.BUTTON_POSITIVE, "예", (dialogInterface, i) -> {
                Intent intent = new Intent(getBaseContext(), UserSetupActivity.class);
                launcher.launch(intent);
                dlg.dismiss();
            });
            dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "아니요", (dialogInterface, i) -> {
                System.out.println("No");
                dlg.dismiss();
            });
            dlg.show();
        }
        else {
            StartProgressDialog.show();

            bgThread thread = new bgThread(new TargetThread());
            thread.setDaemon(true);
            thread.start();
        }
    }

    // get XML Ncast data(LocationSetupActivity -> MainActivity)
    public void getNcastView() {
        if (UltraSrtFcstData != null && VilageFcstData != null) {
            String[] UltraSrtFcstTemp = UltraSrtFcstData.split("\n");
            String[] VilageFcstTemp = VilageFcstData.split("\n");

            if (UltraSrtFcstTemp[0].equals("00") && VilageFcstTemp[0].equals("00")) {
                // T1H, SKY, REH, PTY
                String[] NowFcstData = new String[10];
                String[] today = getCurrentTimeData("today").split(" ");

                String base_time = today[1];
                base_time = base_time.substring(0, 2) + "00";

                int x = 0;
                for (int i = 0; i < UltraSrtFcstTemp.length; i++) {
                    if (UltraSrtFcstTemp[i].contains(base_time)) {
                        NowFcstData[x] = UltraSrtFcstTemp[i];
                        x += 1;
                    }
                    else {
                        continue;
                    }
                }

                for (int i = 0; i < NowFcstData.length; i++) {
                    if (NowFcstData[i].contains("T1H")) {
                        T1H = NowFcstData[i].substring(21);
                    }
                    else if (NowFcstData[i].contains("REH")) {
                        REH = NowFcstData[i].substring(21);
                    }
                    else if (NowFcstData[i].contains("SKY")) {
                        SKY = NowFcstData[i].substring(21);
                    }
                    else if (NowFcstData[i].contains("PTY")) {
                        PTY = NowFcstData[i].substring(21);
                    }
                    else {
                        continue;
                    }
                }

                switch (SKY) {
                    case "1" :
                        SKY = "맑음";
                        weatherimg.setImageResource(R.drawable.ic_outline_light_mode_24);
                        break;
                    case "3" :
                        SKY = "구름많음";
                        weatherimg.setImageResource(R.drawable.ic_baseline_cloud_queue_24);
                        break;
                    case "4" :
                        SKY = "흐림";
                        weatherimg.setImageResource(R.drawable.ic_baseline_cloud_24);
                        break;
                }

                switch (PTY) {
                    case "0" :
                        PTY = "없음";
                        break;
                    case "1" :
                        PTY = "비";
                        break;
                    case "2" :
                        PTY = "비/눈";
                        break;
                    case "3" :
                        PTY = "눈";
                        break;
                    case "5" :
                        PTY = "빗방울";
                        break;
                    case "6" :
                        PTY = "빗방울/눈날림";
                        break;
                    case "7" :
                        PTY = "눈날림";
                        break;
                }

                // TMX, TMN
                String[] TMX = new String[3], TMN = new String[3];

                int a = 0, b = 0;
                for (int i = 0; i < VilageFcstTemp.length; i++) {
                    if (VilageFcstTemp[i].contains("TMX")) {
                        TMX[a] = VilageFcstTemp[i];
                        a += 1;
                    }
                    else if (VilageFcstTemp[i].contains("TMN")) {
                        TMN[b] = VilageFcstTemp[i];
                        b += 1;
                    }
                    else {
                        continue;
                    }
                }

                TMX[0] = TMX[0].substring(5);
                TMN[0] = TMN[0].substring(5);

                String[] temp24 = getCurrentTimeData("onview24").split(" ");
                String[] temp12 = getCurrentTimeData("onview12").split(" ");
                String[] Geotemp = MyReverseGeo.split(" ");

                String AMPM;
                if (Integer.parseInt(temp24[1].substring(0, 2)) >= 0 && Integer.parseInt(temp24[1].substring(0, 2)) < 12) {
                    AMPM = "오전";

                    if (Integer.parseInt(temp24[1].substring(0, 2)) >= 0 && Integer.parseInt(temp24[1].substring(0, 2)) < 6) {
                        if (SKY.equals("맑음")) {
                            weatherimg.setImageResource(R.drawable.ic_baseline_bedtime_24);
                        }
                    }
                }
                else {
                    AMPM = "오후";

                    if (Integer.parseInt(temp24[1].substring(0, 2)) >= 20 && Integer.parseInt(temp24[1].substring(0, 2)) < 24) {
                        if (SKY.equals("맑음")) {
                            weatherimg.setImageResource(R.drawable.ic_baseline_bedtime_24);
                        }
                    }
                }

                // changing UI
                tem.setText(getString(R.string.bar_tem, T1H));
                tem_data.setText(getString(R.string.tem_data, TMX[0], TMN[0], temp24[0], AMPM, temp12[1]));
                addr.setText(getString(R.string.blank3, Geotemp[0], Geotemp[1], Geotemp[2]));
                main_listbtn_text.setText(getString(R.string.list_txt, name, MyReverseGeo));

                myRef.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        temSeekBartxt.setText(getString(R.string.bar_tem, snapshot.child("temStatus").child("temSetValue").getValue()));
                        rehSeekBartxt.setText(getString(R.string.bar_reh, snapshot.child("rehStatus").child("rehSetValue").getValue()));
                        btnav_tem.setText(getString(R.string.bar_tem, snapshot.child("temStatus").child("temSetValue").getValue()));
                        btnav_reh.setText(getString(R.string.bar_reh, snapshot.child("rehStatus").child("rehSetValue").getValue()));
                        main_plantState.setText("현재 미니온실 온도 : " + snapshot.child("temStatus").child("temRealValue").getValue() + "°C" + "   습도 : " + snapshot.child("rehStatus").child("rehRealValue").getValue() + "%");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else if (UltraSrtFcstTemp[0].equals("10") || VilageFcstTemp[0].equals("10")) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("데이터 업데이트중");
                dlg.setMessage("[점검 시간]" + "\n" + "매일 오전 2:00 ~ 2:15" + "\n" + "[점검 내용]" + "\n" + " - 날씨 API 데이터 동기화");
                dlg.setPositiveButton("예", (dialogInterface, i1) -> System.out.println("yes"));
                dlg.show();
            }
            else if (UltraSrtFcstTemp[0].equals("") || VilageFcstTemp[0].equals("")) {
                Toast.makeText(MainActivity.this, "Error : 위치 재확인 필요", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(MainActivity.this, "API 에러 코드 : " + UltraSrtFcstTemp[0] + ", " + VilageFcstTemp[0], Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "위치 정보가 존재하지 않습니다!", Toast.LENGTH_LONG).show();
        }
    }

    // get System current date, time to String
    public static String getCurrentTimeData(String trigger) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatOnView24 = new SimpleDateFormat("EE HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatOnView12 = new SimpleDateFormat("EE hh:mm");
        String data = dateFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch(trigger) {
            case "today" :
                data = dateFormat.format(date);
                break;
            case "yesterday" :
                calendar.add(Calendar.DATE, -1);
                data = dateFormat.format(calendar.getTime());
                break;
            case "onehour" :
                calendar.add(Calendar.HOUR, -1);
                data = dateFormat.format(calendar.getTime());
                break;
            case "onview24" :
                data = dateFormatOnView24.format(date);
                break;

            case "onview12" :
                data = dateFormatOnView12.format(date);
                break;
        }
        return data;
    }

    // release Hash Key
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // debugging Hash Key
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = Base64.getEncoder().encodeToString(md.digest());
                Log.e("Hash Key", something);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getKeyHash(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        UserdataList.clear();
        UserdataList.addAll(database.userDao().getAll());
        System.out.println(UserdataList.size());

        checkedData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
        if (StartProgressDialog != null && StartProgressDialog.isShowing()) {
            StartProgressDialog.dismiss();
        }
    }

}