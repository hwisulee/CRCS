package com.teamcrop.CRCS;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserData")
public class UserData {
    // table items
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "addr")
    private String addr;

    @ColumnInfo(name = "dataID")
    private int dataID;

    @ColumnInfo(name = "dbx")
    private int dbx;

    @ColumnInfo(name = "dby")
    private int dby;

    @ColumnInfo (name = "lon")
    private double lon;

    @ColumnInfo (name = "lat")
    private double lat;

    // getter
    public int getId() { return id; }
    public int getDataID() { return dataID; }
    public int getDbx() { return dbx; }
    public int getDby() { return dby; }

    public double getLon() { return lon; }
    public double getLat() { return lat; }

    public String getName() { return name; }
    public String getAddr() { return addr; }

    // setter
    public void setId(int id) { this.id = id; }
    public void setDataID(int dataID) { this.dataID = dataID; }
    public void setDbx(int dbx) { this.dbx = dbx; }
    public void setDby(int dby) { this.dby = dby; }

    public void setLon(double lon) { this.lon = lon; }
    public void setLat(double lat) { this.lat = lat; }

    public void setName(String name) {this.name = name; }
    public void setAddr(String addr) {this.addr = addr; }
}
