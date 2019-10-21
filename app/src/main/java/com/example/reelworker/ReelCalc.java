package com.example.reelworker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReelCalc extends AppCompatActivity {

    EditText flangeDiameter;
    EditText traverseSize;
    EditText barrelDiameter;
    EditText wireDiameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reel_calc);

        flangeDiameter = findViewById(R.id.flange_diameter_input);
        traverseSize = findViewById(R.id.traverse_input);
        barrelDiameter = findViewById(R.id.barrel_diameter_input);
        wireDiameter = findViewById(R.id.wire_diameter_reel_input);
        Button calculateReelSizeButton = findViewById(R.id.calculate_reel_size_button);

        calculateReelSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double diameter = Double.parseDouble(flangeDiameter.getText().toString());
                double width = Double.parseDouble(traverseSize.getText().toString());
                double belly = Double.parseDouble(barrelDiameter.getText().toString());
                double cable = Double.parseDouble(wireDiameter.getText().toString());

                double reelFactor = getReelFactor(diameter, width, belly, cable);
                Log.d("ReelCalc", "getReelFactor: " + reelFactor);
                Toast.makeText(ReelCalc.this, "Wire Length on Reel = " + reelFactor, Toast.LENGTH_LONG).show();
            }
        });

    }

    private double getReelFactor(double diameter, double width, double belly, double cable) {

        diameter = diameter * 25.4;
        width = width * 25.4;
        belly = belly * 25.4;
        cable = cable * 25.4;
        double turns = Math.round(width/cable -2);
        double layers = Math.round(((diameter-belly)/2-cable)/cable);
        double averageDiameter = (((diameter - (2 * cable)) - belly) / 2) + belly;
        double averageCircle = averageDiameter * Math.PI;
        double length = Math.round(averageCircle*layers*turns/1000);
        double feet = Math.round(length * 3.281);

        Log.i("ReelCalc", "getReelFactor: Footage on reel = " + feet);

        return feet;
    }
}
