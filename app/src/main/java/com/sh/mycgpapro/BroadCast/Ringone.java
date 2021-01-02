package com.sh.mycgpapro.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

    public class Ringone extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if(AlarmReceiver.ringtone!=null)
            AlarmReceiver.ringtone.stop();
        }catch (Exception e){

        }

        sharedPreferences =context.getSharedPreferences("ring_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("requestCodeValue1", "false");
        editor.apply();
    }
}
