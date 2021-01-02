package com.sh.mycgpapro.BroadCast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.sh.mycgpapro.Notification.NotificationHelper;
import java.util.Calendar;
import java.util.UUID;
import static android.content.Context.MODE_PRIVATE;


public class AlarmReceiver extends BroadcastReceiver {

    public static Ringtone ringtone;
    NotificationHelper helper;
    String requestCode1="false";
    SharedPreferences sharedPreferences;
    int uu;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String titleName  = intent.getStringExtra("titleName");
        String beforeDate  = intent.getStringExtra("beforeDate");
        int id  = intent.getIntExtra("id",1);
        String flag = intent.getStringExtra("flag");
        String ring  = intent.getStringExtra("ring");

        uu= UUID.randomUUID().hashCode();

        sharedPreferences =context.getSharedPreferences("ring_info", MODE_PRIVATE);
        requestCode1 = sharedPreferences.getString("requestCodeValue1", "false");

        helper = new NotificationHelper(context);

        if(ring.equals("true")){

            if(requestCode1.equals("false")){
                Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

                ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
                ringtone.play();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("requestCodeValue1", "true");
                editor.apply();

            }

            Calendar cal = Calendar.getInstance();
            Intent intent_stop=new Intent(context,Ringone.class);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            PendingIntent pending = PendingIntent.getBroadcast(context, UUID.randomUUID().hashCode(), intent_stop, PendingIntent.FLAG_CANCEL_CURRENT);
            if (alarmManager != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+10000, pending);
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+10000, pending);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+10000, pending);
            }

        }


        if ("true".equals(flag)){


            if(beforeDate!=null){
                if(Integer.parseInt(beforeDate) == 0){

                    showNotification(titleName,
                            "The Schedule Time is now. Tap for Details or swipe to skip.",id);
                }

               else if(Integer.parseInt(beforeDate) == 1){

                    showNotification(titleName,
                            "The Schedule Time is Tomorrow. Tap for Details or swipe to skip.",id);
                }
                else if(Integer.parseInt(beforeDate) == 2){

                    showNotification(titleName,
                            "The Schedule Time is 2 days later. Tap for Details or swipe to skip.",id);
                }

                else if(Integer.parseInt(beforeDate) == 3){

                    showNotification(titleName,
                            "The Schedule Time is 3 days later. Tap for Details or swipe to skip.",id);
                }

                else if(Integer.parseInt(beforeDate) == 4){

                    showNotification(titleName,
                            "The Schedule Time is 4 days later. Tap for Details or swipe to skip.",id);
                }

                else if(Integer.parseInt(beforeDate) == 5){

                    showNotification(titleName,
                            "The Schedule Time is 5 days later. Tap for Details or swipe to skip.",id);
                }

                else if(Integer.parseInt(beforeDate) == 7){

                    showNotification(titleName,
                            "The Schedule Time is 7 days later. Tap for Details or swipe to skip.",id);
                }

                else if(Integer.parseInt(beforeDate) == 14){

                    showNotification(titleName,
                            "The Schedule Time is two weeks later. Tap for Details or swipe to skip.",id);
                }

            }

        }

    }

    private void showNotification(String title, String message,int id) {

        NotificationCompat.Builder notification = helper.getChannelNotification(title,message,id,uu);
        helper.getManager().notify(uu, notification.build());

    }

}

