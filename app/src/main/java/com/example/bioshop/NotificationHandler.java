package com.example.bioshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID="BioShop";
    private NotificationManager mNotMan;
    private final int NOTIFICATION_ID=0;
    private Context mC;
    public NotificationHandler(Context context) {
        this.mC=context;
        this.mNotMan=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



    }
    private void createChannel(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.O) {return;}
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Bioshop értesítés",NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableVibration(true);
        channel.setDescription("Értesítés a bioshoptól");
        this.mNotMan.createNotificationChannel(channel);
    }
    public void send(String message){

        Intent intent = new Intent(mC,ShopListActivity.class);
        PendingIntent pc= PendingIntent.getActivity(mC,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder bob=new NotificationCompat.Builder(mC,CHANNEL_ID)
                .setContentTitle("BioShop")
                .setContentText(message)
                .setSmallIcon(R.drawable.shopping_cart_icon)
                .setContentIntent(pc);
        this.mNotMan.notify(NOTIFICATION_ID,bob.build());
    }
    public void cancel(){
        this.mNotMan.cancel(NOTIFICATION_ID);
    }
}
