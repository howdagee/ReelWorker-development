package com.example.reelworker.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "machineSetting",
        foreignKeys = {@ForeignKey(entity = Wire.class,
        parentColumns = "name",
        childColumns = "wireName"),@ForeignKey(entity = Machine.class,
        parentColumns = "name",
        childColumns = "machineName")},
        indices = {@Index(value = "machineName"), @Index(value = "wireName")})
public class MachineSetting {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "machineSettingId")
    private long machineSettingId;

    @ColumnInfo(name = "machineName")
    private String machineName;

    @ColumnInfo(name = "wireName")
    private String wireName;

    @ColumnInfo(name = "leftPosition")
    private double leftPosition;

    @ColumnInfo(name = "rightPosition")
    private double rightPosition;

    @ColumnInfo(name = "traverseSpeed")
    private double traverseSpeed;

    @ColumnInfo(name = "reelType")
    private String reelType;

    @ColumnInfo(name = "reelSize")
    private String reelSize;

    public MachineSetting(String machineName, String wireName, double leftPosition,
                          double rightPosition, double traverseSpeed, String reelType,
                          String reelSize) {
        this.machineName = machineName;
        this.wireName = wireName;
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.traverseSpeed = traverseSpeed;
        this.reelType = reelType;
        this.reelSize = reelSize;
    }


    public long getMachineSettingId() {
        return machineSettingId;
    }

    public void setMachineSettingId(long machineSettingId) {
        this.machineSettingId = machineSettingId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getWireName() {
        return wireName;
    }

    public void setWireName(String wireName) {
        this.wireName = wireName;
    }

    public double getLeftPosition() {
        return leftPosition;
    }

    public void setLeftPosition(double leftPosition) {
        this.leftPosition = leftPosition;
    }

    public double getRightPosition() {
        return rightPosition;
    }

    public void setRightPosition(double rightPosition) {
        this.rightPosition = rightPosition;
    }

    public double getTraverseSpeed() {
        return traverseSpeed;
    }

    public void setTraverseSpeed(double traverseSpeed) {
        this.traverseSpeed = traverseSpeed;
    }

    public String getReelType() {
        return reelType;
    }

    public void setReelType(String reelType) {
        this.reelType = reelType;
    }

    public String getReelSize() {
        return reelSize;
    }

    public void setReelSize(String reelSize) {
        this.reelSize = reelSize;
    }
}
