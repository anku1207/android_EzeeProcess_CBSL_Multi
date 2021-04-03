package in.cbslgroup.ezeepeafinal.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.cbslgroup.ezeepeafinal.BuildConfig;


public class NotificationActions {


    public static String INTENT_ACTION = BuildConfig.APPLICATION_ID + ".notification.action";



    protected static final String PARAM_ACTION = "action";
    protected static final String PARAM_UPLOAD_ID = "uploadId";

    protected static final String ACTION_CANCEL_UPLOAD = "cancelUpload";

    private NotificationActions() {

    }

    public static PendingIntent getCancelUploadAction(final Context context,
                                                      final int requestCode,
                                                      final String uploadID) {
        Intent intent = new Intent(INTENT_ACTION);
        Log.e("intent",INTENT_ACTION);

        intent.putExtra(PARAM_ACTION, ACTION_CANCEL_UPLOAD);
        intent.putExtra(PARAM_UPLOAD_ID, uploadID);

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
