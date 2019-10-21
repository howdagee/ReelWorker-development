package com.example.reelworker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

import com.example.reelworker.entities.Machine;
import com.example.reelworker.entities.MachineViewModel;

import java.util.List;

public class MachinesActivity extends AppCompatActivity {

    private MachineViewModel machineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines);

        RecyclerView recyclerView = findViewById(R.id.machine_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        Button goToReelCalc = findViewById(R.id.reel_calculator_page);

        goToReelCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachinesActivity.this, ReelCalc.class);
                startActivity(intent);
            }
        });

        final MachineAdapter adapter = new MachineAdapter();
        recyclerView.setAdapter(adapter);

        machineViewModel = ViewModelProviders.of(this).get(MachineViewModel.class);
        machineViewModel.getAllMachines().observe(this, new Observer<List<Machine>>() {
            @Override
            public void onChanged(@Nullable List<Machine> machines) {
                adapter.setMachines(machines);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new MachineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Machine machine) {
                Intent intent =
                        new Intent(MachinesActivity.this, SelectWireProperties.class);
                intent.putExtra(SelectWireProperties.EXTRA_MACHINE_NAME, machine.getName());
                intent.putExtra(SelectWireProperties.EXTRA_MACHINE_MULTIPLIER, String.valueOf(machine.getMultiplier()));
                startActivity(intent);
            }
        });

    }

}


