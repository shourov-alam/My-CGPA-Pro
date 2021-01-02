package com.sh.mycgpapro.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.sh.mycgpapro.Activity.Schedule_Record;
import com.sh.mycgpapro.BroadCast.NotificationDismissedReceiver;
import com.sh.mycgpapro.R;
import java.util.UUID;

public class NotificationHelper extends ContextWrapper {
    public static final String channelId = "channel ID";
    public static final String channelName = "channel Name";
    public Context context;

    private NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        this.context=base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {

        NotificationChannel channel1 = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.green);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);

    }

    public NotificationManager getManager(){

        if (manager == null){

            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }

        return manager;
    }

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);

        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message, int id,int codes){

        Intent skipIntent = new Intent(context, NotificationDismissedReceiver.class);

        skipIntent.putExtra("val",codes);
        PendingIntent skipPendingIntent = PendingIntent.getBroadcast (context, codes, skipIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        Intent intent1 =new Intent(this,Schedule_Record.class);
        intent1.putExtra(Schedule_Record.TAG_ACTIVITY_FROM,"notify");
        intent1.putExtra("idn",id);
        intent1.putExtra("co",codes);
        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent1=PendingIntent.getActivity(this,codes,intent1,PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent1)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .addAction(R.drawable.ic_info_black_24dp, "Details", pendingIntent1)
                .addAction(R.drawable.ic_cancel_black_24dp, "Skip", skipPendingIntent)
                .setDeleteIntent(createOnDismissedIntent(context, UUID.randomUUID().hashCode()))
                .setVibrate(new long[] {2000, 2000,2000,2000,2000})
                .setAutoCancel(true);
    }

}
