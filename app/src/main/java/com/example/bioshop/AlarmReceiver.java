package com.example.bioshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
     new NotificationHandler(context).send("Vár vissza a bioshop!");
    }
}