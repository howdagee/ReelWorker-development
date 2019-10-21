package com.example.reelworker.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.reelworker.entities.Machine;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface MachineDao {

    @Query("SELECT * FROM machine ORDER BY name ASC")
    LiveData<List<Machine>> getAllMachines();

    @Query("SELECT * FROM machine WHERE machineId = :machineId LIMIT 1" )
    Machine getMachineById(long machineId);

    @Query("SELECT * FROM machine WHERE name = :name")
    List<Machine> getMachineByName(String name);

    @Insert(onConflict = IGNORE)
    void insertMachine(Machine machine);

    @Insert(onConflict = IGNORE)
    void insertMultipleMachines(List<Machine> machines);

    @Delete
    void deleteMachine(Machine machine);

    @Query("DELETE FROM machine")
    void deleteAll();
}
