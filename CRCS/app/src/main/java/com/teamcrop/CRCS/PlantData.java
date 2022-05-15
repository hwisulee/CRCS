package com.teamcrop.CRCS;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlantData")
public class PlantData {
    // table items
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "plant")
    private String plant;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "tem")
    private double tem;

    @ColumnInfo(name = "reh")
    private double reh;

    // getter
    public int getId() { return id; }

    public double getTem() { return tem; }
    public double getReh() { return reh; }

    public String getName() { return name; }
    public String getPlant() { return plant; }
    public String getDate() { return date; }

    // setter
    public void setId(int id) { this.id = id; }

    public void setTem(double tem) { this.tem = tem; }
    public void setReh(double reh) { this.reh = reh; }

    public void setName(String name) { this.name = name; }
    public void setPlant(String plant) { this.plant = plant; }
    public void setDate(String date) { this.date = date; }
}
