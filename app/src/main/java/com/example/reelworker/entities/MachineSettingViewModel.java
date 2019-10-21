package com.example.reelworker.entities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MachineSettingViewModel extends AndroidViewModel {
    private MachineSettingRepository repository;
    private LiveData<List<MachineSetting>> allMachineSettings;

    public MachineSettingViewModel(@NonNull Application application) {
        super(application);
        repository = new MachineSettingRepository(application);

    }

    public void insert(MachineSetting machineSetting) {
        repository.insert(machineSetting);
    }

    public LiveData<List<MachineSettingData>> getMachineSetting(String wireName, String machineName, String reelType) {
        return  repository.getMachineSettings(wireName, machineName, reelType);
    }
    public double getWireOD(String wireName) {
        return repository.getWireOD(wireName);
    }
}
