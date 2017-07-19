package com.example.uottawa.cali;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Quang-Tri on 13/07/2017.
 */

public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String assignment = intent.getExtras().getString("Assignment");
        boolean isUpcomingNotificationOn = intent.getExtras().getBoolean("isUpcomingNotificationOn", true);
        boolean isDailyNotificationOn = intent.getExtras().getBoolean("isDailyNotificationOn", true);

        Intent newIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(isUpcomingNotificationOn || isDailyNotificationOn) {
            // build notification
            // the addAction re-use the same intent to keep the example short
            Notification n = new Notification.Builder(context)
                    .setContentTitle("Assignments due!")
                /*.setContentText("An assignment is due in 2 days!")*/
                    .setContentText(assignment)
                    .setSmallIcon(R.drawable.ic_at_study)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        }

    }
}
