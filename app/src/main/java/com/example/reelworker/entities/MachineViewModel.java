package com.example.reelworker.entities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MachineViewModel extends AndroidViewModel {

    private MachineRepository repository;
    private LiveData<List<Machine>> allMachines;

    public MachineViewModel(@NonNull Application application) {
        super(application);
        repository = new MachineRepository(application);
        allMachines = repository.getAllMachines();
    }

    public void insert(Machine machine) {
        repository.insert(machine);
    }

    public LiveData<List<Machine>> getAllMachines() {
        return allMachines;
    }
}
