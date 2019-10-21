package com.example.reelworker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddWireActivity extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "com.example.reelworker.EXTRA_NAME";
    public static final String EXTRA_DIAMETER =
            "com.example.reelworker.EXTRA_DIAMETER";
    public static final String EXTRA_SPARK =
            "com.example.reelworker.EXTRA_SPARK";
    public static final String EXTRA_SCRAP =
            "com.example.reelworker.EXTRA_SCRAP";
    public static final String EXTRA_WEIGHT =
            "com.example.reelworker.EXTRA_WEIGHT";

    private EditText wireNameInput;
    private EditText wireDiameterInput;
    private EditText wireSparkSettingInput;
    private EditText wireScrapAmountInput;
    private EditText wireWeightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wire);

        wireNameInput = findViewById(R.id.wire_name_input);
        wireDiameterInput = findViewById(R.id.wire_diameter_input);
        wireSparkSettingInput = findViewById(R.id.wire_spark_input);
        wireScrapAmountInput = findViewById(R.id.wire_scrap_input);
        wireWeightInput = findViewById(R.id.wire_weight_input);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Wire");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void isFormComplete() {
        String wireName = wireNameInput.getText().toString();
        String wireDiameter = wireDiameterInput.getText().toString();
        String sparkSetting = wireSparkSettingInput.getText().toString();
        String scrapAmount = wireScrapAmountInput.getText().toString();
        String weight = wireWeightInput.getText().toString();

        if (wireName.trim().isEmpty() || wireDiameter.trim().isEmpty()
                || sparkSetting.trim().isEmpty() || scrapAmount.trim().isEmpty()
                || weight.trim().isEmpty()) {
            Toast.makeText(this, "Please make sure all fields are filled out", Toast.LENGTH_SHORT).show();
        } else {
            saveWire(wireName, sparkSetting, scrapAmount, wireDiameter, weight);
        }

    }

    private void saveWire(String wireName, String sparkSetting, String scrapAmount, String wireDiameter, String weight) {

        double sparkValue = Double.parseDouble(sparkSetting);
        double wireDiameterValue = Double.parseDouble(wireDiameter);


        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, wireName);
        data.putExtra(EXTRA_DIAMETER, wireDiameterValue);
        data.putExtra(EXTRA_SPARK, sparkValue);
        data.putExtra(EXTRA_SCRAP, scrapAmount);
        data.putExtra(EXTRA_WEIGHT, weight);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_wire:
                isFormComplete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
