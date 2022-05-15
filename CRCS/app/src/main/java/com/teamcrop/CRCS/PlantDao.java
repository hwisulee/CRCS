package com.teamcrop.CRCS;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDao {
    @Insert
    void insert(PlantData plantData);

    @Delete
    void delete(PlantData plantData);

    @Delete
    void reset(List<PlantData> plantData);

    @Update
    void update(PlantData plantData);

    @Query("SELECT * FROM PlantData")
    List<PlantData> getAll();

    @Query("SELECT * FROM PlantData WHERE name LIKE :query")
    List<PlantData> getName(String query);
}
