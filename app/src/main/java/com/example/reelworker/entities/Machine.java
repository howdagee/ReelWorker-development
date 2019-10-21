package com.example.reelworker.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "machine",
    indices = {@Index(value = "name", unique = true)})
public class Machine {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "machineId")
    private long machineId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "multiplier")
    private double multiplier;

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public Machine(@NonNull String name, double multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }


    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
