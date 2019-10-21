package com.example.reelworker.entities;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.reelworker.dao.WireDao;
import com.example.reelworker.database.ServiceWireDatabase;

import java.util.List;

public class WireRepository {

    private WireDao wireDao;
    private LiveData<List<Wire>> allWires;
    private LiveData<List<Wire>> wireByName;

    public WireRepository(Application application) {
        ServiceWireDatabase database = ServiceWireDatabase.getDatabase(application);
        wireDao = database.wireDao();
        allWires = wireDao.getAllWires();
    }

    public void insert(Wire wire) {
        new InsertWireAsyncTask(wireDao).execute(wire);
    }

    public void update(Wire wire) {
        new UpdateWireAsyncTask(wireDao).execute(wire);
    }

    public void delete(Wire wire) {
        new DeleteWireAsyncTask(wireDao).execute(wire);
    }

    public void deleteAllWires() {
        new DeleteAllWiresAsyncTask(wireDao).execute();
    }

    public LiveData<List<Wire>> getAllWires() {
        return allWires;
    }

    public LiveData<List<Wire>> getWireByName(String name) {
        return wireByName = wireDao.getWireByName(name);
    }

    private static class InsertWireAsyncTask extends AsyncTask<Wire, Void, Void> {
        private WireDao wireDao;

        private InsertWireAsyncTask(WireDao wireDao) {
            this.wireDao = wireDao;
        }


        @Override
        protected Void doInBackground(Wire... wires) {
            wireDao.addWire(wires[0]);
            return null;
        }
    }

    private static class UpdateWireAsyncTask extends AsyncTask<Wire, Void, Void> {
        private WireDao wireDao;

        private UpdateWireAsyncTask(WireDao wireDao) {
            this.wireDao = wireDao;
        }


        @Override
        protected Void doInBackground(Wire... wires) {
            wireDao.update(wires[0]);
            return null;
        }
    }

    private static class DeleteWireAsyncTask extends AsyncTask<Wire, Void, Void> {
        private WireDao wireDao;

        private DeleteWireAsyncTask(WireDao wireDao) {
            this.wireDao = wireDao;
        }


        @Override
        protected Void doInBackground(Wire... wires) {
            wireDao.delete(wires[0]);
            return null;
        }
    }

    private static class DeleteAllWiresAsyncTask extends AsyncTask<Void, Void, Void> {
        private WireDao wireDao;

        private DeleteAllWiresAsyncTask(WireDao wireDao) {
            this.wireDao = wireDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            wireDao.deleteAllWires();
            return null;
        }
    }
}
