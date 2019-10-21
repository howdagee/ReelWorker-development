package com.example.reelworker.entities;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.reelworker.dao.MachineDao;
import com.example.reelworker.database.ServiceWireDatabase;

import java.util.List;

public class MachineRepository {

    private MachineDao machineDao;
    private LiveData<List<Machine>> allMachines;

    public MachineRepository(Application application) {
        ServiceWireDatabase database = ServiceWireDatabase.getDatabase(application);
        machineDao = database.machineDao();
        allMachines = machineDao.getAllMachines();
    }

    public void insert(Machine machine) {
        new InsertMachineAsyncTask(machineDao).execute(machine);
    }

    public LiveData<List<Machine>> getAllMachines() {
        return allMachines;
    }

    private static class InsertMachineAsyncTask extends AsyncTask<Machine,Void,Void> {
        private MachineDao machineDao;

        private InsertMachineAsyncTask(MachineDao machineDao) {
            this.machineDao = machineDao;
        }

        @Override
        protected Void doInBackground(Machine... machines) {
            machineDao.insertMachine(machines[0]);
            return null;
        }
    }

}
