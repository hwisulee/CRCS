package com.teamcrop.CRCS;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(UserData userData);

    @Delete
    void delete(UserData userData);

    @Delete
    void reset(List<UserData> userData);

    @Update
    void update(UserData userData);

    @Query("SELECT * FROM UserData")
    List<UserData> getAll();

    @Query("SELECT * FROM UserData WHERE name LIKE :query")
    List<UserData> getName(String query);
}
