package com.example.reelworker.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.reelworker.entities.MachineSetting;
import com.example.reelworker.entities.MachineSettingData;

import java.util.List;

@Dao
public interface MachineSettingDao {

    @Transaction
    @Query("SELECT MachineSetting.machineSettingId, MachineSetting.machineName, MachineSetting.wireName, MachineSetting.leftPosition, " +
            "MachineSetting.rightPosition, MachineSetting.traverseSpeed," +
            "MachineSetting.reelType, MachineSetting.reelSize " +
            "FROM MachineSetting " +
            "WHERE machineSetting.wireName = UPPER(:name) " +
            "   AND machineSetting.machineName = :machine" +
            "   AND machineSetting.reelType = :reelType;")
    LiveData<List<MachineSettingData>> getMachineSetting(String name, String machine, String reelType);

    @Insert
    void addMachineSetting(MachineSetting machineSetting);

    @Query("SELECT Wire.diameter " +
            "FROM machineSetting LEFT JOIN Wire on Wire.name = MachineSetting.wireName " +
            "WHERE machineSetting.wireName = UPPER(:wireName);")
    double getMachineSettingWireOD(String wireName);

    @Query("DELETE FROM machineSetting")
    void deleteAll();

    @Query("SELECT * FROM machineSetting")
    LiveData<List<MachineSetting>> getAllMachineSettings();

}
