package com.example.reelworker.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "wire",
    indices = {@Index(value = "name", unique = true)})
public class Wire {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wireId")
    private long wireId;

    @NonNull
    private String name;

    @ColumnInfo(name = "sparkSetting")
    private double sparkSetting;

    @ColumnInfo(name = "scrapAmount")
    private String scrapAmount;

    private double diameter;
    private String weight;

    public Wire(@NonNull String name, double sparkSetting, String scrapAmount, double diameter, String weight) {
        this.name = name;
        this.sparkSetting = sparkSetting;
        this.scrapAmount = scrapAmount;
        this.diameter = diameter;
        this.weight = weight;
    }

    public long getWireId() {
        return wireId;
    }

    public void setWireId(long wireId) {
        this.wireId = wireId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSparkSetting() {
        return sparkSetting;
    }

    public void setSparkSetting(double sparkSetting) {
        this.sparkSetting = sparkSetting;
    }

    public String getScrapAmount() {
        return scrapAmount;
    }

    public void setScrapAmount(String scrapAmount) {
        this.scrapAmount = scrapAmount;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
