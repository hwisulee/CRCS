package com.teamcrop.CRCS;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert
    void insert(LocationData locationData);

    @Delete
    void delete(LocationData locationData);

    @Delete
    void reset(List<LocationData> locationData);

    @Update
    void update(LocationData locationData);

    @Query("SELECT * FROM LocationData")
    List<LocationData> getAll();

    @Query("SELECT * FROM LocationData WHERE id LIKE :query")
    List<LocationData> getID(int query);

    @Query("SELECT * FROM LocationData WHERE second LIKE :query " + "OR third LIKE :query " + "OR fourth LIKE :query")
    List<LocationData> getAllSTF(String query);

    @Query("SELECT * FROM LocationData WHERE first LIKE :query1 " + "AND second LIKE :query234 " + "OR third LIKE :query234 " + "OR fourth LIKE :query234")
    List<LocationData> getCheckedST(String query1, String query234);
}
