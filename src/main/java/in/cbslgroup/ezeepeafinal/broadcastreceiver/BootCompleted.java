package in.cbslgroup.ezeepeafinal.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.cbslgroup.ezeepeafinal.utils.FCMHandler;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

            FCMHandler.enableFCM();
            Log.e("fcm_init","broadcast_init");

        }

    }

}
