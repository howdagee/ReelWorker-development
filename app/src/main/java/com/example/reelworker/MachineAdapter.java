package com.example.reelworker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reelworker.entities.Machine;


import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineHolder> {
    private List<Machine> machines = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MachineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.machine_list_item, viewGroup, false);
        return new MachineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineHolder machineHolder, int position) {
        Machine currentMachine = machines.get(position);
        machineHolder.machineName.setText(currentMachine.getName());
    }

    @Override
    public int getItemCount() {
        return machines.size();
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
        notifyDataSetChanged();
    }

    class MachineHolder extends  RecyclerView.ViewHolder {
        private TextView machineName;


        public MachineHolder(View itemView) {
            super(itemView);
            machineName = itemView.findViewById(R.id.machine_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(machines.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Machine machine);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
