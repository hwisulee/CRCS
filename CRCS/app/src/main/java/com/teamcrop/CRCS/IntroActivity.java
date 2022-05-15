package com.teamcrop.CRCS;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {
    RoomDB database;
    MainHandler mainHandler;

    ArrayList<String> ExcelData = new ArrayList<>();

    UserData userData = new UserData();
    LocationData LocationData = new LocationData();
    PlantData plantData = new PlantData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        database = RoomDB.getInstance(this);
        mainHandler = new MainHandler(Looper.getMainLooper());

        SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);
        if (!checkFirst) {
            bgThread thread = new bgThread(new TargetThread());
            thread.setDaemon(true);
            thread.start();
        }
        else if (checkFirst) {
            Handler handler = new Handler();
            handler.postDelayed((Runnable) () -> {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    // readExceldata
    public void readExcelFromAssets() {
        try {
            AssetManager assetManager = this.getResources().getAssets();
            InputStream inputStream = assetManager.open("data.xls");
            ZipSecureFile.setMinInflateRatio(0);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

            int rows = hssfSheet.getPhysicalNumberOfRows();


            for (int i = 0; i < rows; i++) {
                ArrayList<String> arrLine = new ArrayList<String>();

                HSSFRow row = hssfSheet.getRow(i);
                if (row == null) { continue; }

                int cells = row.getPhysicalNumberOfCells();
                for (int j = 0; j <= cells; j++) {
                    HSSFCell cell = hssfSheet.getRow(i).getCell(j);
                    if (cell == null) { continue; }

                    String value = "";
                    switch (cell.getCellType()) {
                        case FORMULA :
                            value = cell.getCellFormula();
                            break;
                        case NUMERIC :
                            value = cell.getNumericCellValue() + "";
                            break;
                        case STRING :
                            value = cell.getStringCellValue() + "";
                            break;
                        case BLANK :
                            value = cell.getBooleanCellValue() + "";
                            break;
                        case ERROR :
                            value = cell.getErrorCellValue() + "";
                            break;
                    }
                    arrLine.add(value);
                }
                String temp = String.valueOf(arrLine);
                ExcelData.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

            runOnUiThread(() -> {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    class bgThread extends Thread {
        private final Thread targetThread;
        private State bgState;

        public bgThread(TargetThread targetThread) {
            this.targetThread = targetThread;
        }

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
            // Works only on first launch of the app
            SharedPreferences pref = getSharedPreferences("PrefData", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("checkFirst", true);
            editor.putInt("checkedList", 0);
            editor.apply();

            readExcelFromAssets();

            for (int i = 0; i < ExcelData.size(); i++) {
                if (ExcelData.get(i).startsWith("[") && ExcelData.get(i).endsWith("]")) {
                    String[] DBtemp = ExcelData.get(i).substring(1, ExcelData.get(i).length()-1).split(", ");

                    LocationData.setFirst(DBtemp[0]);
                    LocationData.setSecond(DBtemp[1]);
                    LocationData.setThird(DBtemp[2]);
                    LocationData.setFourth(DBtemp[3]);
                    LocationData.setNx(Double.parseDouble(DBtemp[4]));
                    LocationData.setNy(Double.parseDouble(DBtemp[5]));
                    LocationData.setLon(Double.parseDouble(DBtemp[6]));
                    LocationData.setLat(Double.parseDouble(DBtemp[7]));
                    LocationData.setTmx(Double.parseDouble(DBtemp[8].substring(4)));
                    LocationData.setTmy(Double.parseDouble(DBtemp[9].substring(4)));
                }
                database.locationDao().insert(LocationData);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
