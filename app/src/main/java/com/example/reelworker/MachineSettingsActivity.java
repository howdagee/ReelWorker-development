package com.example.reelworker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reelworker.entities.MachineSetting;
import com.example.reelworker.entities.MachineSettingData;
import com.example.reelworker.entities.MachineSettingViewModel;

import java.text.NumberFormat;
import java.util.List;

public class MachineSettingsActivity extends AppCompatActivity {

    public static final int ADD_MACHINE_SETTING_REQUEST = 1;

    private MachineSettingViewModel machineSettingViewModel;

    private String wireODEXTRA;
    private double wireODExtraHint;
    private String machineMultiplierExtra;

    private NumberFormat traverseFormatter = NumberFormat.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_settings);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message_subject_intent"));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String wireName = extras.getString("WIRE_NAME");
        final String machineName = extras.getString("MACHINE_NAME");
        final String reelType = extras.getString("REEL_TYPE");
        machineMultiplierExtra = extras.getString("MACHINE_MULTIPLIER");

        traverseFormatter.setMinimumFractionDigits(3);

        FloatingActionButton buttonAddMachineSetting = findViewById(R.id.add_machine_setting_button);
        buttonAddMachineSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachineSettingsActivity.this,
                        AddMachineSetting.class);
                intent.putExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_NAME, machineName);
                intent.putExtra(AddMachineSetting.EXTRA_MACHINE_MULTIPLIER, machineMultiplierExtra);
                intent.putExtra(AddMachineSetting.EXTRA_WIRE_OD, wireODEXTRA);
                intent.putExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_WIRENAME, wireName);
                intent.putExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_REEL_TYPE, reelType);
                intent.putExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_TRAVERSE_HINT, wireODEXTRA);
                startActivityForResult(intent, ADD_MACHINE_SETTING_REQUEST);
            }
        });

        TextView machineForSettingsSearch = findViewById(R.id.machine_setting_list_title);
        machineForSettingsSearch.setText("Machine: " + machineName);

        RecyclerView recyclerView = findViewById(R.id.machine_setting_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MachineSettingAdapter adapter = new MachineSettingAdapter();
        recyclerView.setAdapter(adapter);

        machineSettingViewModel = ViewModelProviders.of(this).get(MachineSettingViewModel.class);
        machineSettingViewModel.getMachineSetting(wireName, machineName, reelType).observe(this, new Observer<List<MachineSettingData>>() {
            @Override
            public void onChanged(@Nullable List<MachineSettingData> machineSettingData) {
                adapter.setMachineSetting(machineSettingData);
                if (adapter.getItemCount() == 0) {
                    Toast.makeText(MachineSettingsActivity.this, "No Data Found",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String capturedOD = intent.getStringExtra("EXTRA_WIRE_OD");
            if (capturedOD != null) {
                wireODEXTRA = capturedOD;
                wireODExtraHint = Double.parseDouble(capturedOD);
                double multiplier = Double.parseDouble(machineMultiplierExtra);
                wireODExtraHint = calculateTraverseSpeed(multiplier, wireODExtraHint);
            } else {
                wireODEXTRA = "0";
            }
//            Toast.makeText(MachineSettingsActivity.this, "Suggested: " + wireODExtraHint, Toast.LENGTH_SHORT).show();
        }
    };

    private double calculateTraverseSpeed(double machineMultiplier, double wireOD) {
        if (machineMultiplier != 0) {
            double traverseSpeed = (machineMultiplier * wireOD) + wireOD;
            return Double.parseDouble(traverseFormatter.format(traverseSpeed));
        } else {
            return wireOD;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MACHINE_SETTING_REQUEST && resultCode == RESULT_OK) {
            String machineName = data.getStringExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_NAME);
            String wireName = data.getStringExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_WIRENAME);
            double leftPosition = data.getDoubleExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_LEFTPOSITION, 0);
            double rightPosition = data.getDoubleExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_RIGHTPOSITION, 0);
            double traverseSpeed = data.getDoubleExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_TRAVERSE_SPEED, 0);
            String reelType = data.getStringExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_REEL_TYPE);
            String reelSize = data.getStringExtra(AddMachineSetting.EXTRA_MACHINE_SETTING_REEL_SIZE);

            MachineSetting machineSetting = new MachineSetting(machineName, wireName, leftPosition,
                    rightPosition,traverseSpeed, reelType, reelSize);
            machineSettingViewModel.insert(machineSetting);

            Toast.makeText(this, "Machine setting saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Machine setting was not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
