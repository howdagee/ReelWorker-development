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
import android.view.View;
import android.widget.Toast;

import com.example.reelworker.entities.Wire;
import com.example.reelworker.entities.WireViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MACHINE_NAME =
            "com.example.reelworker.EXTRA_MACHINE_NAME";
    public static final String EXTRA_WIRE_NAME =
            "com.example.reelworker.EXTRA_WIRE_NAME";

    public static final int ADD_NOTE_REQUEST = 1;

    private WireViewModel wireViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String wireName = intent.getStringExtra(EXTRA_WIRE_NAME);

        // Floating action button for adding wire
        FloatingActionButton buttonAddWire = findViewById(R.id.add_wire_button);
        buttonAddWire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddWireActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.wire_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final WireAdapter adapter = new WireAdapter();
        recyclerView.setAdapter(adapter);

        // Set the ViewModel for the Wire class
        wireViewModel = ViewModelProviders.of(this).get(WireViewModel.class);
        wireViewModel.getWireByName(wireName).observe(this, new Observer<List<Wire>>() {
            @Override
            public void onChanged(@Nullable List<Wire> wires) {
                adapter.setWires(wires);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddWireActivity.EXTRA_NAME);
            double spark = data.getDoubleExtra(AddWireActivity.EXTRA_SPARK, 0.0);
            String scrap = data.getStringExtra(AddWireActivity.EXTRA_SCRAP);
            double diameter = data.getDoubleExtra(AddWireActivity.EXTRA_DIAMETER, 0.0);
            String weight = data.getStringExtra(AddWireActivity.EXTRA_WEIGHT);

            Wire wire = new Wire(name, spark, scrap, diameter, weight);
            wireViewModel.insert(wire);

            Toast.makeText(this, "Wire Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wire not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
