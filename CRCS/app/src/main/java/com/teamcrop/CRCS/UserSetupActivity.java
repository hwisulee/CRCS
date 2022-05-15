package com.teamcrop.CRCS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

public class UserSetupActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private static final String KakaoKey = MainActivity.KakaoKey;

    ActivityResultLauncher<Intent> activityResultLauncher;

    UserData userData = new UserData();

    MapPOIItem MyPoint = new MapPOIItem();
    MapPOIItem StationPoint = new MapPOIItem();

    String MyReverseGeo, StationReverseGeo, name;
    Double[] MyPointGeo = new Double[2];
    Double[] StationGeo = new Double[2];
    int ID, StationID;
    int ResultCode = 0;
    double dbx, dby;

    RoomDB database;

    EditText nameedit, locationedit, detaillocationedit;
    Button okbtn;
    ImageButton locationfindbtn;

    MapView mapView;
    MapReverseGeoCoder mapReverseGeoCoder;
    ViewGroup mapViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_usersetup);

        // findViewById
        nameedit = findViewById(R.id.usersetup_nameedit);
        locationedit = findViewById(R.id.usersetup_locationedit);
        detaillocationedit = findViewById(R.id.usersetup_detail_locationedit);
        locationfindbtn = findViewById(R.id.usersetup_locationfindbtn);
        okbtn = findViewById(R.id.usersetup_okbtn);
        mapViewContainer = findViewById(R.id.usersetup_mapview);

        database = RoomDB.getInstance(this);

        try {
            Intent ListIntent = getIntent();

            if (ListIntent.getExtras().getInt("ResultCode") == 1001) {
                ResultCode = 1001;
                ID = ListIntent.getExtras().getInt("ID");
                name = ListIntent.getExtras().getString("Name");
                MyReverseGeo = ListIntent.getExtras().getString("Addr");
                StationID = ListIntent.getExtras().getInt("DataId");

                nameedit.setText(name);

                List<LocationData> dataList = new ArrayList<>();
                dataList.addAll(database.locationDao().getID(StationID));
                MyPointGeo[0] = ListIntent.getExtras().getDouble("MyPointGeo0");
                MyPointGeo[1] = ListIntent.getExtras().getDouble("MyPointGeo1");
                StationGeo[0] = dataList.get(0).getLat();
                StationGeo[1] = dataList.get(0).getLon();
                dbx = dataList.get(0).getNx();
                dby = dataList.get(0).getNy();

                mapReverseGeoCoder = new MapReverseGeoCoder(KakaoKey, MapPoint.mapPointWithGeoCoord(StationGeo[0], StationGeo[1]), this, this);
                mapReverseGeoCoder.startFindingAddress();

                settingAgain();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();

        // onClickListener
        locationfindbtn.setOnClickListener(view -> {
            mapViewContainer.removeView(mapView);
            Intent intent = new Intent(getBaseContext(), LocationSetupActivity.class);
            activityResultLauncher.launch(intent);
        });

        okbtn.setOnClickListener(view -> {
            switch (ResultCode) {
                case 1001 :
                    userData.setId(ID);
                    userData.setName(nameedit.getText().toString());
                    userData.setAddr(MyReverseGeo);
                    userData.setDataID(StationID);
                    userData.setDbx((int)dbx);
                    userData.setDby((int)dby);
                    userData.setLat(MyPointGeo[0]);
                    userData.setLon(MyPointGeo[1]);
                    database.userDao().update(userData);

                    finish();
                    break;

                case 0 :
                    Intent intent = new Intent();
                    intent.putExtra("MyReverseGeo", MyReverseGeo);
                    intent.putExtra("StationReverseGeo", StationReverseGeo);
                    intent.putExtra("name", nameedit.getText().toString());

                    userData.setName(nameedit.getText().toString());
                    userData.setAddr(MyReverseGeo);
                    userData.setDataID(StationID);
                    userData.setDbx((int)dbx);
                    userData.setDby((int)dby);
                    userData.setLat(MyPointGeo[0]);
                    userData.setLon(MyPointGeo[1]);
                    database.userDao().insert(userData);

                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        });
    }

    public void init() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                if (intent != null) {
                    MyReverseGeo = intent.getExtras().getString("MyReverseGeo");
                    StationReverseGeo = intent.getExtras().getString("StationReverseGeo");
                    MyPointGeo[0] = intent.getExtras().getDouble("MyPointGeo0");
                    MyPointGeo[1] = intent.getExtras().getDouble("MyPointGeo1");
                    StationGeo[0] = intent.getExtras().getDouble("StationGeo0");
                    StationGeo[1] = intent.getExtras().getDouble("StationGeo1");
                    StationID = intent.getExtras().getInt("StationID");
                    dbx = intent.getExtras().getDouble("DB_X");
                    dby = intent.getExtras().getDouble("DB_Y");
                    setupViewUpdate();
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void settingAgain() {
        // kakaomap setting
        mapView.removeAllPOIItems();

        if (MyPointGeo[0] != null && MyPointGeo[1] != null) {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(MyPointGeo[0], MyPointGeo[1]), true);
            MyPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(MyPointGeo[0], MyPointGeo[1]));
        }
        else {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.565770577319036, 126.97825215604381), true);
            MyPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(37.565770577319036, 126.97825215604381));
        }

        if (StationGeo[0] != null && StationGeo[1] != null) {
            StationPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(StationGeo[0], StationGeo[1]));
        }

        mapView.setZoomLevel(5, true);
        mapView.setMapViewEventListener(this);
        mapView.setOnTouchListener((view, motionEvent) -> true);

        MyPoint.setItemName("내 위치");
        MyPoint.setTag(0);
        MyPoint.setMarkerType(MapPOIItem.MarkerType.BluePin);
        MyPoint.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(MyPoint);
        mapView.selectPOIItem(MyPoint, true);

        StationPoint.setItemName("측정소 위치");
        StationPoint.setTag(0);
        StationPoint.setMarkerType(MapPOIItem.MarkerType.BluePin);
        StationPoint.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(StationPoint);

        mapViewContainer.addView(mapView);
    }

    public void setupViewUpdate() {
        String[] Geotemp = StationReverseGeo.split(" ");
        String[] Geotemp1 = MyReverseGeo.split(" ");

        locationedit.setText(getString(R.string.blank3, Geotemp[0], Geotemp[1], Geotemp[2]));
        detaillocationedit.setText(getString(R.string.blank4, Geotemp1[0], Geotemp1[1], Geotemp1[2], Geotemp1[3]));
    }

    @Override
    protected void onStart() { super.onStart(); }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeView(mapView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();

        // kakaomap setting
        mapView = new MapView(this);

        mapView.removeAllPOIItems();

        if (MyPointGeo[0] != null && MyPointGeo[1] != null) {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(MyPointGeo[0], MyPointGeo[1]), true);
            MyPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(MyPointGeo[0], MyPointGeo[1]));
        }
        else {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.565770577319036, 126.97825215604381), true);
            MyPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(37.565770577319036, 126.97825215604381));
        }

        if (StationGeo[0] != null && StationGeo[1] != null) {
            StationPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(StationGeo[0], StationGeo[1]));
        }

        mapView.setZoomLevel(5, true);
        mapView.setMapViewEventListener(this);
        mapView.setOnTouchListener((view, motionEvent) -> true);

        MyPoint.setItemName("내 위치");
        MyPoint.setTag(0);
        MyPoint.setMarkerType(MapPOIItem.MarkerType.BluePin);
        MyPoint.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(MyPoint);
        mapView.selectPOIItem(MyPoint, true);

        StationPoint.setItemName("측정소 위치");
        StationPoint.setTag(0);
        StationPoint.setMarkerType(MapPOIItem.MarkerType.BluePin);
        StationPoint.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(StationPoint);

        mapViewContainer.addView(mapView);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) { }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) { }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) { }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) { }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) { }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) { }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) { }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) { }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) { }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        StationReverseGeo = s;

        String[] Geotemp = StationReverseGeo.split(" ");
        String[] Geotemp1 = MyReverseGeo.split(" ");

        locationedit.setText(getString(R.string.blank3, Geotemp[0], Geotemp[1], Geotemp[2]));
        detaillocationedit.setText(getString(R.string.blank4, Geotemp1[0], Geotemp1[1], Geotemp1[2], Geotemp1[3]));
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        System.out.println("주소 미확인");
    }
}
