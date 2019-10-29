package com.example.reelworker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reelworker.database.ServiceWireDatabase;
import com.example.reelworker.entities.Wire;

import java.lang.ref.WeakReference;

public class SelectWireProperties extends AppCompatActivity {

    private static final int ADD_WIRE_REQUEST = 1;

    private EditText wireNameInput;
    private EditText wireFootageInput;

    private String selectedReelType;
    private String wireName;
    private String machineName;
    private double machineMultiplier;
    private int machineDirection;
    private int wireFootage;

    private ServiceWireDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wire_properties);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        machineName = extras.getString("MACHINE_NAME");
        machineMultiplier = extras.getDouble("MACHINE_MULTIPLIER");
        machineDirection = extras.getInt("MACHINE_DIRECTION", 0);

        populateReelTypeButtons();
        wireNameInput = findViewById(R.id.wire_name_for_search);
        TextView machineSelectedTitle = findViewById(R.id.title_machine_selected);
        wireFootageInput = findViewById(R.id.wire_footage_input);
        machineSelectedTitle.setText("Machine: " + machineName);

        Button  searchWireButton = findViewById(R.id.search_wire_button);
        searchWireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Maybe add a button to go to the AddWireActivity if the wire doesn't exist.
                if (selectedReelType != null && !wireNameInput.getText().toString().isEmpty()) {
                    wireName = wireNameInput.getText().toString();
                    if (wireFootageInput.getText().toString().isEmpty()) {
                        wireFootage = 0;
                    } else {
                        wireFootage = Integer.parseInt(wireFootageInput.getText().toString());
                    }

                    checkIfWireExists(wireName);
                } else {
                    Toast.makeText(SelectWireProperties.this, "Reel type and wire name must be filled out", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button addWireButton = findViewById(R.id.add_wire_button);
        addWireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectWireProperties.this, AddWireActivity.class);
                startActivityForResult(intent, ADD_WIRE_REQUEST);
            }
        });

    }

    private void createBundleAndProceed() {
        Bundle extras = new Bundle();
        extras.putString("MACHINE_NAME", machineName);
        extras.putDouble("MACHINE_MULTIPLIER", machineMultiplier);
        extras.putString("WIRE_NAME", wireName);
        extras.putString("REEL_TYPE", selectedReelType);
        extras.putInt("WIRE_FOOTAGE", wireFootage);
        extras.putInt("MACHINE_DIRECTION", machineDirection);

        Intent intent = new Intent(SelectWireProperties.this,
                MachineSettingsActivity.class);
        intent.putExtras(extras);

        startActivity(intent);
    }

    private void checkIfWireExists(String wireName) {
        database = ServiceWireDatabase.getDatabase(SelectWireProperties.this);

        AsyncTaskCheckWireExists runner = new AsyncTaskCheckWireExists(this);
        runner.execute(wireName);
    }

    private static class AsyncTaskCheckWireExists extends AsyncTask<String, Boolean, Boolean> {
        WeakReference<SelectWireProperties> activityReference;

        AsyncTaskCheckWireExists(SelectWireProperties context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean success = false;
            try {
                Wire theSelectedWire = activityReference.get().database.wireDao().getWireProperties(strings[0]);
                if (theSelectedWire != null) {
                    Log.d("SELECT WIRE PROPERTIES", "doInBackground: Wire Exists!");
                    success = true;
                } else  {
                    Log.d("SELECT WIRE PROPERTIES", "doInBackground: Wire NOT Found!");
                    success = false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("NULL POINTER", "doInBackground: ERROR occured: " + e.getMessage());
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                activityReference.get().createBundleAndProceed();
            } else {
                Log.d("SELECTWIRE PROPERTIES", "onPostExecute: No wire entry");
                activityReference.get().notifyUser("Wire does not exist");
            }
        }
    }

    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void populateReelTypeButtons() {

        // Sets styles for the radio buttons to be generated
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        RadioGroup reelTypes = findViewById(R.id.radio_group_reel_types);
        String[] reelTypeValues = getResources().getStringArray(R.array.reel_types);

        // Create Buttons
        for (int i = 0; i< reelTypeValues.length;i++) {
            final String reel = reelTypeValues[i];

            RadioButton button = new RadioButton(this);
            button.setText(reel);
            button.setLayoutParams(params);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedReelType = reel;
                }
            });

            // Add to radio group
            reelTypes.addView(button);
        }

    }
}
