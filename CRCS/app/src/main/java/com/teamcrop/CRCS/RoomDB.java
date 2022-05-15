package com.teamcrop.CRCS;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocationData.class, UserData.class, PlantData.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;

    private static String DATABASE_NAME = "CRCSdatabase";

    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract LocationDao locationDao();
    public abstract UserDao userDao();
    public abstract PlantDao plantDao();

}
