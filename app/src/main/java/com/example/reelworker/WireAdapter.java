package com.example.reelworker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reelworker.entities.Wire;

import java.util.ArrayList;
import java.util.List;

public class WireAdapter extends RecyclerView.Adapter<WireAdapter.WireHolder> {
    private List<Wire> wires = new ArrayList<>();

    @NonNull
    @Override
    public WireHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wire_item, viewGroup, false);
        return new WireHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WireHolder wireHolder, int position) {
        Wire currentWire = wires.get(position);
        wireHolder.wireNameText.setText(currentWire.getName());
        wireHolder.sparkText.setText(String.valueOf(currentWire.getSparkSetting()));
        wireHolder.diameterText.setText(String.valueOf(currentWire.getDiameter()));
        wireHolder.scrapText.setText(currentWire.getScrapAmount());
        wireHolder.weightText.setText(currentWire.getWeight() + "lbs");
    }

    @Override
    public int getItemCount() {
        return wires.size();
    }

    public void setWires(List<Wire> wires) {
        this.wires = wires;
        notifyDataSetChanged();
    }

    class WireHolder extends RecyclerView.ViewHolder {
        private TextView wireNameText;
        private TextView sparkText;
        private TextView diameterText;
        private TextView scrapText;
        private TextView weightText;

        public WireHolder(View itemView) {
            super(itemView);
            wireNameText = itemView.findViewById(R.id.wire_name_text);
            sparkText = itemView.findViewById(R.id.spark_tester_text);
            diameterText = itemView.findViewById(R.id.diameter_text);
            scrapText = itemView.findViewById(R.id.scrap_text);
            weightText = itemView.findViewById(R.id.wire_weight_input);
        }
    }


}
