package com.sh.mycgpapro.BroadCast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sh.mycgpapro.Notification.NotificationHelper;

  public class NotificationDismissedReceiver extends BroadcastReceiver {

    NotificationHelper helper;
    NotificationManager manager;
    int code;

    @Override
    public void onReceive(Context context, Intent intent) {

        helper = new NotificationHelper(context);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        code=intent.getIntExtra("val",-1);

        try {

            AlarmReceiver.ringtone.stop();

        }catch (Exception e){

        }
      //  AlarmReceiver.ringtone.stop();
        manager.cancel(code);
    }

}
