package com.example.reelworker.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.reelworker.entities.Wire;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface WireDao {

    @Query("SELECT * FROM wire")
    LiveData<List<Wire>> getAllWires();

    @Query("SELECT * FROM wire WHERE name = :name ORDER BY name ASC")
    LiveData<List<Wire>> getWireByName(String name);


    @Query("SELECT * FROM wire WHERE name = :name LIMIT 1")
    Wire getWireProperties(String name);

    @Insert(onConflict = IGNORE)
    void addMultipleWire(List<Wire> wires);

    @Insert(onConflict = IGNORE)
    void addWire(Wire wire);

    @Update
    void update(Wire wire);

    @Delete
    void delete(Wire wire);

    @Query("DELETE FROM wire")
    void deleteAllWires();
}
