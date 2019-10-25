package com.example.reelworker.entities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class WireViewModel extends AndroidViewModel {

    private  WireRepository repository;
    private LiveData<List<Wire>> allWires;


    public WireViewModel(@NonNull Application application) {
        super(application);
        repository = new WireRepository(application);
//        allWires = repository.getAllWires();
    }

    public void insert(Wire wire) {
        repository.insert(wire);
    }

    public void update(Wire wire) {
        repository.update(wire);
    }

    public void delete(Wire wire) {
        repository.delete(wire);
    }

    public void deleteAllWires() {
        repository.deleteAllWires();
    }

    public LiveData<List<Wire>> getAllWires() {
        return allWires;
    }

    public Wire getWireProperties(String name) {
        return repository.getWireProperties(name);
    }
}
