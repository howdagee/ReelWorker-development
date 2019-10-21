package com.example.reelworker;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reelworker.entities.MachineSettingData;

import java.util.ArrayList;
import java.util.List;

public class MachineSettingAdapter extends RecyclerView.Adapter<MachineSettingAdapter.MachineSettingHolder> {
    private List<MachineSettingData> machineSettings = new ArrayList<>();

    private Context context;

    @NonNull
    @Override
    public MachineSettingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.machine_setting_item, viewGroup, false);
        return new MachineSettingHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull MachineSettingAdapter.MachineSettingHolder machineSettingHolder, int position) {
        MachineSettingData currentMachineSetting = machineSettings.get(position);

        machineSettingHolder.wireName.setText(currentMachineSetting.getWireName());
        machineSettingHolder.traverseSpeed.setText(String.valueOf(currentMachineSetting.getTraverseSpeed()));
        machineSettingHolder.leftPosition.setText(String.valueOf(currentMachineSetting.getLeftPosition()));
        machineSettingHolder.rightPosition.setText(String.valueOf(currentMachineSetting.getRightPosition()));
        machineSettingHolder.reelType.setText(currentMachineSetting.getReelType() + ": ");
        machineSettingHolder.reelSize.setText(currentMachineSetting.getReelSize());

        Intent intent = new Intent("message_subject_intent");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return machineSettings.size();
    }

    public void setMachineSetting(List<MachineSettingData> machineSettings) {
        this.machineSettings = machineSettings;
        notifyDataSetChanged();
    }

    class MachineSettingHolder extends RecyclerView.ViewHolder {
        private TextView sparkSetting;
        private TextView leftPosition;
        private TextView rightPosition;
        private TextView traverseSpeed;
        private TextView wireName;
        private TextView reelType;
        private TextView reelSize;

        public MachineSettingHolder(View itemView) {
            super(itemView);
            sparkSetting = itemView.findViewById(R.id.machine_name_info);
            leftPosition = itemView.findViewById(R.id.left_position_info);
            rightPosition = itemView.findViewById(R.id.right_position_info);
            traverseSpeed = itemView.findViewById(R.id.traverse_speed_info);
            wireName = itemView.findViewById(R.id.wire_name_info);
            reelType = itemView.findViewById(R.id.reel_type_info);
            reelSize = itemView.findViewById(R.id.reel_size_info);
        }
    }
}
