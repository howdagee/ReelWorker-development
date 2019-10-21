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

    public static final String EXTRA_MACHINE_NAME =
            "com.example.reelworker.EXTRA_MACHINE_NAME";

    public static final String EXTRA_MACHINE_MULTIPLIER =
            "com.example.reelworker.EXTRA_MACHINE_MULTIPLIER";

    private EditText wireName;
    private String selectedReelType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wire_properties);

        Intent intentInfo = getIntent();
        final String machineName = intentInfo.getStringExtra(EXTRA_MACHINE_NAME);
        final String machineMultiplierExtra = intentInfo.getStringExtra(EXTRA_MACHINE_MULTIPLIER);

//        setTitle("Reel Worker (" + machineName + ")");

        populateReelTypeButtons();
        wireName = findViewById(R.id.wire_name_for_search);
        TextView machineSelectedTitle = findViewById(R.id.title_machine_selected);
        machineSelectedTitle.setText("Machine: " + machineName);

        Button  searchWireButton = findViewById(R.id.search_wire_button);
        searchWireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String wireNameText = wireName.getText().toString();
                Bundle extras = new Bundle();
                extras.putString("MACHINE_NAME", machineName);
                extras.putString("MACHINE_MULTIPLIER", machineMultiplierExtra);
                extras.putString("WIRE_NAME", wireNameText);
                extras.putString("REEL_TYPE", selectedReelType);

                Intent intent = new Intent(SelectWireProperties.this,
                                MachineSettingsActivity.class);
                intent.putExtras(extras);

                startActivity(intent);
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


//            TODO: set on-click callbacks
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(SelectWireProperties.this, "Selected: " + reel,
//                            Toast.LENGTH_SHORT).show();
                    selectedReelType = reel;
                }
            });

            // Add to radio group
            reelTypes.addView(button);
        }

    }
}
