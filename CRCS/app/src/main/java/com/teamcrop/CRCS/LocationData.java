package com.teamcrop.CRCS;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "LocationData")
public class LocationData implements Serializable {
    // table items
    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo (name = "first")
    private String first;

    @ColumnInfo (name = "second")
    private String second;

    @ColumnInfo (name = "third")
    private String third;

    @ColumnInfo (name = "fourth")
    private String fourth;

    @ColumnInfo (name = "nx")
    private double nx;

    @ColumnInfo (name = "ny")
    private double ny;

    @ColumnInfo (name = "lon")
    private double lon;

    @ColumnInfo (name = "lat")
    private double lat;

    @ColumnInfo (name = "tmx")
    private double tmx;

    @ColumnInfo (name = "tmy")
    private double tmy;

    // getter
    public int getId() { return id; }

    public double getNx() { return nx; }
    public double getNy() { return ny; }
    public double getLon() { return lon; }
    public double getLat() { return lat; }
    public double getTmx() { return tmx; }
    public double getTmy() { return tmy; }

    public String getFirst() { return first; }
    public String getSecond() { return second; }
    public String getThird() { return third; }
    public String getFourth() { return fourth; }

    // setter
    public void setId(int id) { this.id = id; }

    public void setNx(double nx) { this.nx = nx; }
    public void setNy(double ny) { this.ny = ny; }
    public void setLon(double lon) { this.lon = lon; }
    public void setLat(double lat) { this.lat = lat; }
    public void setTmx(double tmx) { this.tmx = tmx; }
    public void setTmy(double tmy) { this.tmy = tmy; }

    public void setFirst(String first) { this.first = first; }
    public void setSecond(String second) { this.second = second; }
    public void setThird(String third) { this.third = third; }
    public void setFourth(String fourth) { this.fourth = fourth; }
}
