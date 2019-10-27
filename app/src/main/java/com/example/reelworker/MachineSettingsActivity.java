package com.example.reelworker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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


    private double leftPositionDefault;
    private double rightPositionDefault;
    private String reelSizeDefaultWidth;

    private NumberFormat traverseFormatter = NumberFormat.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_settings);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String wireName = extras.getString("WIRE_NAME");
        final String machineName = extras.getString("MACHINE_NAME");
        final String reelType = extras.getString("REEL_TYPE");
        // TODO: pass wire footage to calculate net weight on next step/screen
        final String wireFootage = extras.getString("WIRE_FOOTAGE");
        final double machineMultiplier = extras.getDouble("MACHINE_MULTIPLIER");
        final int machineDirection = extras.getInt("MACHINE_DIRECTION");

        traverseFormatter.setMinimumFractionDigits(3);

        FloatingActionButton buttonAddMachineSetting = findViewById(R.id.add_machine_setting_button);
        buttonAddMachineSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("MACHINE_NAME", machineName);
                extras.putDouble("MACHINE_MULTIPLIER", machineMultiplier);
                extras.putString("WIRE_NAME", wireName);
                extras.putString("REEL_TYPE", reelType);
                extras.putDouble("LEFT_POSITION_DEFAULT", leftPositionDefault);
                extras.putDouble("RIGHT_POSITION_DEFAULT", rightPositionDefault);
                if (reelSizeDefaultWidth!=null) {
                    int reelSizeDefaultInt = Integer.parseInt(reelSizeDefaultWidth);
                    extras.putInt("REEL_SIZE_DEFAULT", reelSizeDefaultInt);
                } else {
                    int reelSizeDefaultInt = 0;
                }
                extras.putDouble("LEFT_POSITION_DEFAULT", leftPositionDefault);
                extras.putDouble("RIGHT_POSITION_DEFAULT", rightPositionDefault);
                extras.putInt("MACHINE_DIRECTION", machineDirection);

                Intent intent = new Intent(MachineSettingsActivity.this,
                        AddMachineSetting.class);
                intent.putExtras(extras);

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
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        leftPositionDefault = machineSettingData.get(0).getLeftPosition();
                        rightPositionDefault = machineSettingData.get(0).getRightPosition();
                        reelSizeDefaultWidth = machineSettingData.get(0).getReelSize();
                        Log.d("MachineSettingActivity", "onChanged: rightPosition exists: " + rightPositionDefault);
                        Log.d("MachineSettingActivity", "onChanged: \nReel Size: " + reelSizeDefaultWidth);
                        reelSizeDefaultWidth = reelSizeDefaultWidth.substring(reelSizeDefaultWidth.indexOf("x")+1, reelSizeDefaultWidth.lastIndexOf("x"));
                        Log.d("REQUIRED STRING", "onChanged: \n"+ reelSizeDefaultWidth);
                    } catch (NullPointerException e) {
                        e.getStackTrace();
                        Log.d("MachineSettingsActivity", "onChanged: Error - " + e.getMessage());
                    }
                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MACHINE_SETTING_REQUEST && resultCode == RESULT_OK) {
            try {
                assert data != null;
                Bundle extras = data.getExtras();

                assert extras != null;
                String machineName = extras.getString("MACHINE_NAME");
                String wireName = extras.getString("WIRE_NAME");
                double leftPosition = extras.getDouble("LEFT_POSITION", 0);
                double rightPosition = extras.getDouble("RIGHT_POSITION", 0);
                double traverseSpeed = extras.getDouble("TRAVERSE_SPEED", 0);
                String reelType = extras.getString("REEL_TYPE");
                String reelSize = extras.getString("REEL_SIZE");

                MachineSetting machineSetting = new MachineSetting(machineName, wireName, leftPosition,
                        rightPosition,traverseSpeed, reelType, reelSize);
                machineSettingViewModel.insert(machineSetting);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "Machine setting saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Machine setting was not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
