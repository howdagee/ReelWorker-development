package com.example.reelworker.entities;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.reelworker.dao.MachineSettingDao;
import com.example.reelworker.database.ServiceWireDatabase;

import java.util.List;

public class MachineSettingRepository {

    private MachineSettingDao machineSettingDao;
    private LiveData<List<MachineSetting>> allMachineSettings;
    private LiveData<List<MachineSettingData>> machineSettingBySearch;

    public MachineSettingRepository(Application application) {
        ServiceWireDatabase database = ServiceWireDatabase.getDatabase(application);
        machineSettingDao = database.machineSettingDao();
        allMachineSettings = machineSettingDao.getAllMachineSettings();
    }

    public void insert(MachineSetting machineSetting) {
        new InsertMachineSettingAsyncTask(machineSettingDao).execute(machineSetting);
    }

    public LiveData<List<MachineSettingData>> getMachineSettings(String wireName, String machineName, String reelType) {
        return machineSettingBySearch = machineSettingDao.getMachineSetting(wireName, machineName, reelType);
    }
    public double getWireOD(String wireName) {
        return machineSettingDao.getMachineSettingWireOD(wireName);
    }

    private static class InsertMachineSettingAsyncTask extends AsyncTask<MachineSetting, Void, Void> {
        private MachineSettingDao machineSettingDao;

        private InsertMachineSettingAsyncTask(MachineSettingDao machineSettingDao) {
            this.machineSettingDao = machineSettingDao;
        }

        @Override
        protected Void doInBackground(MachineSetting... machineSettings) {
            machineSettingDao.addMachineSetting(machineSettings[0]);
            return null;
        }
    }

}
