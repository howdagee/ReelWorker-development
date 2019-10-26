package com.example.reelworker;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reelworker.database.ServiceWireDatabase;
import com.example.reelworker.entities.MachineSettingData;
import com.example.reelworker.entities.MachineSettingViewModel;
import com.example.reelworker.entities.Wire;
import com.example.reelworker.entities.WireViewModel;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AddMachineSetting extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_MACHINE_SETTING_NAME =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_NAME";
    public static final String  EXTRA_MACHINE_MULTIPLIER =
            "com.example.reelworker.EXTRA_MACHINE_MULTIPLIER";
    public static final String EXTRA_MACHINE_SETTING_WIRENAME =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_WIRENAME";
    public static final String EXTRA_MACHINE_SETTING_LEFTPOSITION =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_LEFTPOSITION";
    public static final String EXTRA_MACHINE_SETTING_RIGHTPOSITION =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_RIGHTPOSITION";
    public static final String EXTRA_MACHINE_SETTING_TRAVERSE_SPEED =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_TRAVERSE_SPEED";
    public static final String EXTRA_MACHINE_SETTING_REEL_TYPE =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_REEL_TYPE";
    public static final String EXTRA_MACHINE_SETTING_REEL_SIZE =
            "com.example.reelworker.EXTRA_MACHINE_SETTING_REEL_SIZE";


    private EditText machineNameInput;
    private EditText wireNameInput;
    private EditText leftPositionInput;
    private EditText rightPositionInput;
    private EditText traverseSpeedInput;
    private String reelSizeSelected;
    private String selectedReelType;
    private Spinner reelSizeSpinner;
    private RadioGroup reelTypesGroup;
    private String initialReelType;
    String[] reelTypeValues;
    private double machineMultiplier;

    private static Wire theSelectedWire;
    private ServiceWireDatabase database;

    // For formatting numbers
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private NumberFormat percentage = NumberFormat.getPercentInstance();
    private NumberFormat traverseFormatter = NumberFormat.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_machine_setting);

        // Setup the NumberFormat percentage to round to two decimal places.
        percentage.setMinimumFractionDigits(2);
        traverseFormatter.setMinimumFractionDigits(3);

        machineNameInput = findViewById(R.id.machine_name_setting_input);
        wireNameInput = findViewById(R.id.wire_name_setting_input);
        leftPositionInput = findViewById(R.id.left_position_setting_input);
        rightPositionInput = findViewById(R.id.right_position_setting_input);
        traverseSpeedInput = findViewById(R.id.traverse_speed_setting_input);

        reelSizeSpinner = findViewById(R.id.reel_sizes_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.reel_sizes_wood, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reelSizeSpinner.setAdapter(spinnerAdapter);
        reelSizeSpinner.setOnItemSelectedListener(this);


        Intent intentInfo = getIntent();
        final String machineName = intentInfo.getStringExtra(EXTRA_MACHINE_SETTING_NAME);
        final String machineMultiplierExtra = intentInfo.getStringExtra(EXTRA_MACHINE_MULTIPLIER);
        machineMultiplier = Double.parseDouble(machineMultiplierExtra);

        final String wireName = intentInfo.getStringExtra(EXTRA_MACHINE_SETTING_WIRENAME);
        final String theReelType = intentInfo.getStringExtra(EXTRA_MACHINE_SETTING_REEL_TYPE);


        if (machineName != null) {
            machineNameInput.setText(machineName);
        }
        if (wireName != null) {
            wireNameInput.setText(wireName);
        }
        if (theReelType != null) {
            initialReelType = theReelType;
        }

        reelTypesGroup = findViewById(R.id.radio_group_reel_types);

        populateReelTypeOptions();

        selectInitialReelType();


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Machine Setting");

        setupDatabase(wireName);
    }

    private void setupDatabase(String wireName) {
        database = ServiceWireDatabase.getDatabase(AddMachineSetting.this);

        AsyncTaskRunner runner = new AsyncTaskRunner(this);
        runner.execute(wireName);
    }

    private void showToast(){
        Toast.makeText(this, "Wire OD: " + theSelectedWire.getDiameter(), Toast.LENGTH_SHORT).show();

    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String> {

        private String resp;
        WeakReference<AddMachineSetting> activityReference;

        AsyncTaskRunner(AddMachineSetting context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... names) {
            try {
                theSelectedWire = activityReference.get().database.wireDao().getWireProperties(names[0]);
                Log.d("TESTING THREAD", "doInBackground: " + theSelectedWire.getDiameter());
                Log.d("TESTING THREAD", "doInBackground: \n");
//                Thread.sleep(2*1000);
                String traverseSuggested = calculateTraverseSpeed(machineMultiplier, theSelectedWire.getDiameter());
                activityReference.get().traverseSpeedInput.setHint("Suggested: " + traverseSuggested);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }
    }


    private String calculateTraverseSpeed(double machineMultiplier, double wireOD) {
        if (machineMultiplier != 0) {
            double traverseSpeed = (machineMultiplier * wireOD) + wireOD;
            return traverseFormatter.format(traverseSpeed);
        } else {
            return "0.000";
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_machine_setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_machine_setting:
                isMachineSettingFormComplete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectInitialReelType() {
        for (int i=0; i<reelTypeValues.length;i++) {
            final String reel = reelTypeValues[i];
            if (initialReelType.equals(reel)) {
                reelTypesGroup.getChildAt(i).performClick();
            }
        }
    }

    private void isMachineSettingFormComplete() {
        String machineName = machineNameInput.getText().toString();
        String wireName = wireNameInput.getText().toString();
        String leftPosition = leftPositionInput.getText().toString();
        String rightPosition = rightPositionInput.getText().toString();
        String traverseSpeed = traverseSpeedInput.getText().toString();
        String reelSize = reelSizeSelected;

        if (machineName.trim().isEmpty() || wireName.trim().isEmpty()
                || leftPosition.trim().isEmpty() || rightPosition.trim().isEmpty()
                || traverseSpeed.trim().isEmpty() || reelSize.trim().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            saveMachineSetting(machineName, wireName, leftPosition, rightPosition, traverseSpeed,
                    selectedReelType, reelSize);
        }
    }

    private void saveMachineSetting(String machineName, String wireName, String leftPosition, String rightPosition, String traverseSpeed, String reelType, String reelSize) {
        double leftPositionDouble = Double.parseDouble(leftPosition);
        double rightPositionDouble = Double.parseDouble(rightPosition);
        double traverseSpeedDouble = Double.parseDouble(traverseSpeed);

        Intent data = new Intent();
        data.putExtra(EXTRA_MACHINE_SETTING_NAME, machineName);
        data.putExtra(EXTRA_MACHINE_SETTING_WIRENAME, wireName);
        data.putExtra(EXTRA_MACHINE_SETTING_LEFTPOSITION, leftPositionDouble);
        data.putExtra(EXTRA_MACHINE_SETTING_RIGHTPOSITION, rightPositionDouble);
        data.putExtra(EXTRA_MACHINE_SETTING_TRAVERSE_SPEED, traverseSpeedDouble);
        data.putExtra(EXTRA_MACHINE_SETTING_REEL_TYPE, reelType);
        data.putExtra(EXTRA_MACHINE_SETTING_REEL_SIZE, reelSize);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void populateReelTypeOptions() {

        // Sets styles for the radio buttons to be generated
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        reelTypeValues = getResources().getStringArray(R.array.reel_types);

        // Create Buttons
        for (int i = 0; i < reelTypeValues.length; i++) {
            final String reel = reelTypeValues[i];

            final RadioButton button = new RadioButton(this);
            button.setText(reel);
            button.setLayoutParams(params);
            button.setId(i);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AddMachineActivity", "onClick: ID: " + v.getId());
                    switch (reel) {
                        case "Wood":
                            reelTypesGroup.getCheckedRadioButtonId();
                            ArrayAdapter<CharSequence> spinnerWoodAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                    R.array.reel_sizes_wood, android.R.layout.simple_spinner_item);
                            spinnerWoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            reelSizeSpinner.setAdapter(spinnerWoodAdapter);
                            reelSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    reelSizeSelected = parent.getItemAtPosition(position).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case "Plywood":
                            ArrayAdapter<CharSequence> spinnerPlywoodAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                    R.array.reel_sizes_plywood, android.R.layout.simple_spinner_item);
                            spinnerPlywoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            reelSizeSpinner.setAdapter(spinnerPlywoodAdapter);
                            reelSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    reelSizeSelected = parent.getItemAtPosition(position).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case "Plastic":
                            ArrayAdapter<CharSequence> spinnerPlasticAdapter =
                                    ArrayAdapter.createFromResource(
                                            getApplicationContext(),
                                            R.array.reel_sizes_plastic,
                                            android.R.layout.simple_spinner_item
                                    );
                            spinnerPlasticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            reelSizeSpinner.setAdapter(spinnerPlasticAdapter);
                            reelSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    reelSizeSelected = parent.getItemAtPosition(position).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                    }
                    selectedReelType = reel;
                }
            });

            // Add to radio group
            reelTypesGroup.addView(button);
        }
        reelTypesGroup.check(0);

    }


}
