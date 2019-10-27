package com.example.reelworker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SelectWireProperties extends AppCompatActivity {

    private EditText wireName;
    private String selectedReelType;
    private EditText wireFootage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wire_properties);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String machineName = extras.getString("MACHINE_NAME");
        final double machineMultiplierExtra = extras.getDouble("MACHINE_MULTIPLIER");
        final int machineDirection = extras.getInt("MACHINE_DIRECTION", 0);

        populateReelTypeButtons();
        wireName = findViewById(R.id.wire_name_for_search);
        TextView machineSelectedTitle = findViewById(R.id.title_machine_selected);
        wireFootage = findViewById(R.id.wire_footage_input);
        machineSelectedTitle.setText("Machine: " + machineName);

        Button  searchWireButton = findViewById(R.id.search_wire_button);
        searchWireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Before next screen is retrieved a thread should most likely be run to do
                //  a quick database check to verify that the wire does exist
                //  (If not maybe add a button to go to the AddWireActivity).
                if (selectedReelType != null && !wireName.getText().toString().isEmpty()) {
                    final String wireNameText = wireName.getText().toString();
                    final String wireFootageText = wireFootage.getText().toString();
                    Bundle extras = new Bundle();
                    extras.putString("MACHINE_NAME", machineName);
                    extras.putDouble("MACHINE_MULTIPLIER", machineMultiplierExtra);
                    extras.putString("WIRE_NAME", wireNameText);
                    extras.putString("REEL_TYPE", selectedReelType);
                    extras.putString("WIRE_FOOTAGE", wireFootageText);
                    extras.putInt("MACHINE_DIRECTION", machineDirection);

                    Intent intent = new Intent(SelectWireProperties.this,
                                    MachineSettingsActivity.class);
                    intent.putExtras(extras);

                    startActivity(intent);
                } else {
                    Toast.makeText(SelectWireProperties.this, "Reel type and wire name must be filled out", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
