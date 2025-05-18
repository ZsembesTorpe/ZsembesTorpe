package com.example.bioshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class TemaBeallitasok extends AppCompatActivity {
    private BroadcastReceiver themeChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TemaSeged.temaValaszt(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tema_valtas);

       // registerReceiver(themeChangeReceiver, new IntentFilter(String.valueOf(RECEIVER_EXPORTED)));
    }


    public void changeTheme(View view) {
        TemaSeged.temaValto(this);

      Intent intent = new Intent("com.example.bioshop.TEMA_VALTOZOTT");
       sendBroadcast(intent);

        recreate();
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(themeChangeReceiver);
    }
}
