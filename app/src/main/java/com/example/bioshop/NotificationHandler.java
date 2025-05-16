package com.example.bioshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID="shop_notification_channel";
    private final int NOTIFICATION_ID=0;
    private Context mContext;
    private NotificationManager mManager;
    public NotificationHandler(Context context) {
        this.mContext=context;
        this.mManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }
    private void createChannel(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.O) return;
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Shop notification",NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.setDescription("Bioshop alkalmazás értesítése.");
        this.mManager.createNotificationChannel(channel);
    }
    public void send(String message){
        Intent intent=new Intent(mContext,ShopListActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setContentTitle("BioShop")
                .setContentText(message)
                .setSmallIcon(R.drawable.images_vagott)
                .setContentIntent(pendingIntent);
        this.mManager.notify(NOTIFICATION_ID,builder.build());


    }
    public void cancel(){
        this.mManager.cancel(NOTIFICATION_ID);
    }
}
