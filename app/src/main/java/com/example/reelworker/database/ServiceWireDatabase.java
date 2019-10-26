package com.example.reelworker.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.reelworker.dao.MachineDao;
import com.example.reelworker.dao.MachineSettingDao;
import com.example.reelworker.dao.WireDao;
import com.example.reelworker.entities.Machine;
import com.example.reelworker.entities.MachineSetting;
import com.example.reelworker.entities.Wire;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {
            Machine.class,
            Wire.class,
            MachineSetting.class
        },
        version = 2, exportSchema = false)
public abstract class ServiceWireDatabase extends RoomDatabase {

    private static ServiceWireDatabase INSTANCE;
    private static final String DB_NAME = "service_wire.db";

    public static ServiceWireDatabase getDatabase(final Context context) {
        synchronized (ServiceWireDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,
                        ServiceWireDatabase.class, DB_NAME)
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Log.d("ServiceWireDatabase", "populating data...");
                                new PopulateDbAsync(INSTANCE).execute();
                            }
                        })
                        .build();
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void clearDb() {
        if (INSTANCE != null) {
            new PopulateDbAsync(INSTANCE).execute();
        }
    }



    public abstract MachineDao machineDao();

    public abstract WireDao wireDao();

    public abstract MachineSettingDao machineSettingDao();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final MachineDao machineDao;
        private final WireDao wireDao;
        private final MachineSettingDao machineSettingDao;

        private PopulateDbAsync(ServiceWireDatabase instance) {
            machineDao = instance.machineDao();
            wireDao = instance.wireDao();
            machineSettingDao = instance.machineSettingDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            machineSettingDao.deleteAll();
            wireDao.deleteAllWires();
            machineDao.deleteAll();

            // Add machines to database
            // TODO: add new property, where it will keep track of number grid high to low. Since
            //  we will need to know if it goes left/right or vice versa. Value might be -1 or 1
            machineDao.insertMachine(new Machine("SW318", .02569));
            machineDao.insertMachine(new Machine("SW437", .20569));
            machineDao.insertMachine(new Machine("SW327", 27.0));

            // Add wire
            wireDao.addWire(new Wire("XH500", 15.0, "150", .926, "1631"));
            wireDao.addWire(new Wire("XH250", 15.0, "150", .702, "835"));
            wireDao.addWire(new Wire("USE14", 7.5, "1000", .163, "21"));
            wireDao.addWire(new Wire("USE12", 7.5, "1000", .182, "30"));
            wireDao.addWire(new Wire("USE10", 7.5, "1000", .205, "43"));
            wireDao.addWire(new Wire("USE8", 10.0, "500", .263, "69"));
            wireDao.addWire(new Wire("USE6", 10.0, "500", .301, "103"));
            wireDao.addWire(new Wire("USE4", 10.0, "500", .350, "156"));
            wireDao.addWire(new Wire("USE3", 10.0, "500", .374, "192"));
            wireDao.addWire(new Wire("USE2", 10.0, "500", .404, "238"));
            wireDao.addWire(new Wire("USE1", 12.5, "500", .476, "307"));
            wireDao.addWire(new Wire("USE1/0", 12.5, "250", .517, "384"));
            wireDao.addWire(new Wire("USE2/0", 12.5, "250", .562, "476"));
            wireDao.addWire(new Wire("USE3/0", 12.5, "250", .610, "590"));
            wireDao.addWire(new Wire("USE4/0", 12.5, "150", .669, "735"));

            wireDao.addWire(new Wire("BST7S8", 0, "500", .143, "50.90"));


            // Add some machine setting data
            machineSettingDao.addMachineSetting(new MachineSetting("SW318", "XH500", 24.40, 34.30, .654, "Wood", "24x18x10"));
            machineSettingDao.addMachineSetting(new MachineSetting("SW318", "XH250", 26.40, 31.30, .654, "Plywood", "24x12x10"));
            machineSettingDao.addMachineSetting(new MachineSetting("SW437", "BST7S8", 38.650, 27.450, .200, "Plywood", "24x12x10"));


            return null;
        }
    }
}
