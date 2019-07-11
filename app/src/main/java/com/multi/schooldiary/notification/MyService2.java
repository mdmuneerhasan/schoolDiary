package com.multi.schooldiary.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.dashboard.teacher.GuardianRequestActivity;
import com.multi.schooldiary.dashboard.teacher.StudentRequestActivity;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import static com.multi.schooldiary.notification.App.CHANNEL_1_ID;
import static com.multi.schooldiary.notification.App.CHANNEL_ID;

public class MyService2 extends Service {

    Connection connection;
    SavedData savedData;
    Notification notification;
    PendingIntent pendingIntent, pendingIntent2 ,pendingIntent3;
    StringBuilder message;
    private NotificationManagerCompat managerCompat;
    private Intent notificationIntent,notificationIntent2,notificationIntent3;

    @Override
    public void onCreate() {
        super.onCreate();
        connection=new Connection();
        savedData =new SavedData(this);
        notificationIntent = new Intent(this, NotificationControlActivity.class);
        notificationIntent2 = new Intent(this, StudentRequestActivity.class);
        notificationIntent3 = new Intent(this, GuardianRequestActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        pendingIntent2 = PendingIntent.getActivity(this, 0, notificationIntent2, 0);
        pendingIntent3 = PendingIntent.getActivity(this, 0, notificationIntent3, 0);
        managerCompat=NotificationManagerCompat.from(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        message=new StringBuilder();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(savedData.getValue("schoolName"))
                .setContentText("Fetching today's notifications...")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .build();
        try {
            // guardian notification
            connection.getDbUser().child(savedData.getValue("uid")).child("hardNotification")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                message.append(dataSnapshot1.getKey()+"\n");
                                Notification notification=new NotificationCompat.Builder(getBaseContext(),CHANNEL_1_ID)
                                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                        .setContentTitle(savedData.getValue("schoolName"))
                                        .setContentText(message)
                                        .setContentIntent(pendingIntent)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                        .build();
                                managerCompat.notify(2,notification);
                            }

                                connection.getDbUser().child(savedData.getValue("uid")).child("softNotification")
                                        .setValue(dataSnapshot.getValue());
                            connection.getDbUser().child(savedData.getValue("uid")).child("hardNotification")
                                    .removeValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }catch (Exception e){
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}