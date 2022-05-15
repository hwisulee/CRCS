package com.teamcrop.CRCS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

public class LocationSetupActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private static final String KakaoKey = MainActivity.KakaoKey;

    List<LocationData> dataList = new ArrayList<>();

    MapPOIItem MyPoint = new MapPOIItem();
    MapPOIItem StationPoint = new MapPOIItem();

    Double[] MyPointGeo = new Double[2];
    Double[] StationGeo = new Double[2];
    String MyPointReverseGeo, StationReverseGeo;
    String transCoord_84toTM;
    String ResultCode;
    String TM_X, TM_Y;
    double DB_X, DB_Y;
    int StationID;

    MapView mapView;
    ViewGroup mapViewContainer;
    MapReverseGeoCoder mapReverseGeoCoder;
    MapPoint initVal;

    RecyclerView recyclerView;
    LocationDataAdapter adapter;

    RoomDB database;

    ImageButton search;
    EditText edit_addr;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_locationsetup);

        mapViewContainer = findViewById(R.id.locationsetup_mapview);

        database = RoomDB.getInstance(this);

        recyclerView = findViewById(R.id.locationsetup_addrlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LocationDataAdapter(LocationSetupActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        dataList.addAll(database.locationDao().getAll());
        adapter.notifyDataSetChanged();

        edit_addr = findViewById(R.id.locationsetup_addredit);
        search = findViewById(R.id.locationsetup_addrsearch);

        search.setOnClickListener(view -> matchingData());

        adapter.setOnItemClickListener((v, pos) -> {
            DB_X = dataList.get(pos).getNx();
            DB_Y = dataList.get(pos).getNy();

            ResultCode = "station";
            double templat = dataList.get(pos).getLat();
            double templon = dataList.get(pos).getLon();
            StationGeo[0] = templat;
            StationGeo[1] = templon;
            matchingMap(pos, templat, templon);

            initVal = MapPoint.mapPointWithGeoCoord(templat, templon);
            mapReverseGeoCoder = new MapReverseGeoCoder(KakaoKey, initVal, this, this);
            mapReverseGeoCoder.startFindingAddress();

            if (StationReverseGeo == null) {
                //Toast.makeText(LocationSetupActivity.this, "Error : 측정소 위치 선택 필요", Toast.LENGTH_LONG).show();
            }
            if (MyPointReverseGeo == null) {
                Toast.makeText(LocationSetupActivity.this, "Error : 내 위치 선택 필요", Toast.LENGTH_LONG).show();
            }
            if (StationReverseGeo != null && MyPointReverseGeo != null) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(LocationSetupActivity.this);
                dlg.setTitle("데이터 체크");
                dlg.setMessage("선택하신 곳이 이 곳이 맞나요?" + "\n" + "측정소 주소 : " + StationReverseGeo + "\n" + "상세 주소 : " + MyPointReverseGeo);
                dlg.setPositiveButton("예", (dialogInterface, i1) -> {
                    StationID = dataList.get(pos).getId();

                    Intent intent = new Intent();
                    intent.putExtra("MyReverseGeo", MyPointReverseGeo);
                    intent.putExtra("StationReverseGeo", StationReverseGeo);
                    intent.putExtra("MyPointGeo0", MyPointGeo[0]);
                    intent.putExtra("MyPointGeo1", MyPointGeo[1]);
                    intent.putExtra("StationGeo0", StationGeo[0]);
                    intent.putExtra("StationGeo1", StationGeo[1]);
                    intent.putExtra("StationID", StationID);
                    intent.putExtra("DB_X", DB_X);
                    intent.putExtra("DB_Y", DB_Y);

                    setResult(RESULT_OK, intent);
                    mapViewContainer.removeView(mapView);
                    finish();
                });
                dlg.setNegativeButton("아니요", (dialogInterface, i1) -> { });
                dlg.show();
            }
        });
    }

    static class bgThread extends Thread {
        private final Thread targetThread;

        public bgThread(Thread targetThread) {
            this.targetThread = targetThread;
        }

        public void run() {
            while(true) {
                Thread.State state = targetThread.getState();

                if (state == Thread.State.NEW) {
                    targetThread.start();
                }

                if (state == State.TERMINATED) {
                    break;
                }

                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }

    class TargetThread extends Thread {
        public void run() {
            // transCoord (WGS84 -> TM) - for using OpenAPI | lon, lat
            transCoord_84toTM = transCoord.transCoord(MyPointGeo[1], MyPointGeo[0]);

            String[] transCoordData = transCoord_84toTM.split(",");
            TM_X = transCoordData[0].substring(4);
            TM_Y = transCoordData[1].substring(4);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void matchingData() {
        dataList.clear();

        edit_addr.setText(edit_addr.getText().toString().trim());
        edit_addr.setSelection(edit_addr.length());
        dataList.addAll(database.locationDao().getAllSTF(edit_addr.getText().toString()));
        adapter.notifyDataSetChanged();

        if (dataList.size() != 0) {
            ResultCode = "station";
            matchingMap(0, dataList.get(0).getLat(), dataList.get(0).getLon());
        }
        else if (dataList.size() == 0) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(LocationSetupActivity.this);
            dlg.setTitle("잠시만요!");
            dlg.setMessage("해당 주소에 측정소가 없는 것 같아요." + "\n" + "해당 위치를 지도에서 찾아서 클릭해보세요!" + "\n" + "그러면 가까운 위치의 측정소를 보여줄거에요.");
            dlg.setPositiveButton("예", (dialogInterface, i1) -> System.out.println("yes"));
            dlg.show();
        }
    }

    public void matchingMap(int pos, double templat, double templon) {
        if ("station".equals(ResultCode)) {
            StationPoint.setItemName("측정소 위치");
            StationPoint.setTag(0);
            StationPoint.setMapPoint(MapPoint.mapPointWithGeoCoord(templat, templon));
            StationPoint.setMarkerType(MapPOIItem.MarkerType.BluePin);
            StationPoint.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            mapView.removePOIItem(StationPoint);
            mapView.addPOIItem(StationPoint);
            mapView.selectPOIItem(StationPoint, true);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(templat, templon), true);
        }
    }

    public void checkDistance() {
        String[] Geotemp = MyPointReverseGeo.split(" ");

        if (Geotemp.length == 4) {
            edit_addr.setText(Geotemp[2]);
        }
        else if (Geotemp.length == 5) {
            edit_addr.setText(getString(R.string.blank2, Geotemp[2], Geotemp[3]));
        }

        edit_addr.setSelection(edit_addr.getText().length());

        dataList.clear();
        dataList.addAll(database.locationDao().getAllSTF(Geotemp[1]));

        // using TM coordinate System to check distance between two coordinates
        if (TM_X != null && TM_Y != null) {
            double[] tempdis1 = new double[dataList.size()];
            double[] tempdis2 = new double[dataList.size()];

            // Pythagorean theorem : check distance between two coordinates
            for (int i = 0; i < dataList.size(); i++) {
                double tempX = dataList.get(i).getTmx();
                double tempY = dataList.get(i).getTmy();

                double distance = Math.sqrt(Math.pow(Math.abs(Double.parseDouble(TM_X) - tempX), 2) + Math.pow(Math.abs(Double.parseDouble(TM_Y) - tempY), 2));

                tempdis1[i] = distance;
                tempdis2[i] = distance;
            }

            // tempdis2 = sort by ascending power
            double temp;
            for (int i = 0; i < tempdis2.length; i++) {
                for (int j = i + 1; j < tempdis2.length; j++) {
                    if (tempdis2[j] < tempdis2[i]) {
                        temp = tempdis2[j];
                        tempdis2[j] = tempdis2[i];
                        tempdis2[i] = temp;
                    }
                }
            }

            // Indexlist = Compare tempdis1 and tempdis2, and if they are the same, store the index of tempdis1 on Indexlist.
            int[] Indexlist = new int[tempdis1.length];
            for (int i = 0; i < tempdis1.length; i++) {
                for (int j = 0; j < tempdis1.length; j++) {
                    if (tempdis2[i] == tempdis1[j]) {
                        Indexlist[i] = j;

                        if (i >= tempdis1.length) {
                            break;
                        }
                    }
                }
            }

            // IDlist = Get ID value in DB of list tempdis1(original) based on tempdis2(sort by ascending power)
            int[] IDlist = new int[Indexlist.length];
            for (int i = 0; i < IDlist.length; i++) {
                IDlist[i] = dataList.get(Indexlist[i]).getId();
            }

            // dataList update with IDlist
            dataList.clear();
            for (int i = 0; i < Indexlist.length; i ++) {
                dataList.addAll(database.locationDao().getID(IDlist[i]));
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() { super.onStart(); }

    @Override
    protected void onDestroy() { super.onDestroy(); }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

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
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        mapView.removePOIItem(MyPoint);

        MyPoint.setItemName("내 위치");
        MyPoint.setTag(0);
        MyPoint.setMapPoint(mapPoint);
        MyPoint.setMarkerType(MapPOIItem.MarkerType.BluePin);
        MyPoint.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(MyPoint);
        mapView.selectPOIItem(MyPoint, true);

        MyPointGeo[0] = MyPoint.getMapPoint().getMapPointGeoCoord().latitude;
        MyPointGeo[1] = MyPoint.getMapPoint().getMapPointGeoCoord().longitude;

        initVal = MapPoint.mapPointWithGeoCoord(MyPointGeo[0], MyPointGeo[1]);
        mapReverseGeoCoder = new MapReverseGeoCoder(KakaoKey, initVal, this, this);

        bgThread thread = new bgThread(new TargetThread());
        thread.start();

        ResultCode = "mypoint";
        mapReverseGeoCoder.startFindingAddress();
    }

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
        if (ResultCode.equals("station")) {
            StationReverseGeo = s;
        }
        else if (ResultCode.equals("mypoint")) {
            MyPointReverseGeo = s;

            checkDistance();
        }
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        System.out.println("주소 미확인");
    }
}
