package com.mine.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;
import com.mine.app.R;

public class Battery extends AppCompatActivity {

    private TextView battery;

    private BroadcastReceiver batterylevelReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            battery.setText(String.valueOf(level)+"%");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        battery = (TextView)findViewById(R.id.batteryLevel);
        this.registerReceiver(this.batterylevelReciever,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));



    }}