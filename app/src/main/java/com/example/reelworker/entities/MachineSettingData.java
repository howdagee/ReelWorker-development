package com.example.reelworker.entities;

public class MachineSettingData {

    private long machineSettingId;
    private double leftPosition;
    private double rightPosition;
    private double traverseSpeed;
    private String wireName;
    private String machineName;
    private String reelType;
    private String reelSize;

    public long getMachineSettingId() {
        return machineSettingId;
    }

    public void setMachineSettingId(long machineSettingId) {
        this.machineSettingId = machineSettingId;
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

    public String getWireName() {
        return wireName;
    }

    public void setWireName(String wireName) {
        this.wireName = wireName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
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
