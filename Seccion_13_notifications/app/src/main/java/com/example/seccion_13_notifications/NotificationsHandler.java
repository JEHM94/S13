package com.example.seccion_13_notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class NotificationsHandler extends ContextWrapper {

    private NotificationManager manager;

    public static final String CHANNEL_HIGH_ID = "1";
    private final String CHANNEL_HIGH_NAME = "HIGH CHANNEL";
    public static final String CHANNEL_LOW_ID = "2";
    private final String CHANNEL_LOW_NAME = "LOW CHANNEL";
    private final int SUMMARY_GROUP_ID = 1001;
    private final String SUMMARY_GROUP_NAME = "GROUPING_NOTIFICATION";

    public NotificationsHandler(Context context) {
        super(context);
        CreateChannels();
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    private void CreateChannels() {
        // Compatibilidad con API 26
        if (Build.VERSION.SDK_INT >= 26) {
            //Creating HIGHT CHANNEL
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);

            // Extra config.....
            //Luz led de la notificacion if compatible
            highChannel.enableLights(true);
            highChannel.setLightColor(Color.YELLOW);
            //Circulito de notificacion en el icono de la app
            highChannel.setShowBadge(true);
            //Vibracion
            highChannel.enableVibration(true);
            //highChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //Sonido de la notificacion
            //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //highChannel.setSound(defaultSoundUri, null);
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            //Creating LOW CHANNEL
            NotificationChannel lowChannel = new NotificationChannel(
                    CHANNEL_LOW_ID, CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);
            lowChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(lowChannel);
        }
    }

    public Notification.Builder createNotification(String title, String message, boolean isHighImportance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isHighImportance) {
                return this.createNotificationWithChannel(title, message, CHANNEL_HIGH_ID);
            }
            return this.createNotificationWithChannel(title, message, CHANNEL_LOW_ID);
        }
        return createNotificationWithoutChannel(title, message);
    }

    private Notification.Builder createNotificationWithChannel(String title, String message, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("tittle", title);
            intent.putExtra("message", message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Action action =
                    new Notification.Action.Builder(
                            Icon.createWithResource(this, android.R.drawable.ic_menu_send),
                            "See Details",
                            pendingIntent).build();

            return new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    //.setContentIntent(pendingIntent)
                    .addAction(action)
                    .setColor(getColor(R.color.purple_200))
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setGroup(SUMMARY_GROUP_NAME)
                    //Para que el usuario pueda cerrarla
                    .setAutoCancel(true);
        }
        return null;
    }

    private Notification.Builder createNotificationWithoutChannel(String title, String message) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                //Para que el usuario pueda cerrarla
                .setAutoCancel(true);
    }

    public void publishNotificationSummaryGroup(boolean isHighImportance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = (isHighImportance) ? CHANNEL_HIGH_ID : CHANNEL_LOW_ID;
            Notification summaryNotification = new Notification.Builder(getApplicationContext(), channelID)
                    .setSmallIcon(android.R.drawable.stat_notify_call_mute)
                    .setGroup(SUMMARY_GROUP_NAME)
                    .setGroupSummary(true)
                    .build();
            getManager().notify(SUMMARY_GROUP_ID, summaryNotification);
        }
    }
}
