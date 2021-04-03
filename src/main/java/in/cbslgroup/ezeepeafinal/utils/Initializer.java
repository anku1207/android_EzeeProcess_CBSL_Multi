package in.cbslgroup.ezeepeafinal.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.rohitss.uceh.UCEHandler;
import net.gotev.uploadservice.UploadService;
import in.cbslgroup.ezeepeafinal.BuildConfig;


public class Initializer extends Application {

    private static Initializer mInstance;
    public static String globalDynamicSlid;

    public final static String ADMIN_CHANNEL_ID = "EzeeProcess";
    public static CharSequence adminChannelName = "EzeeProcess Channel";
    public static String adminChannelDescription = "EzeeProcess Channel Desc";

    @Override
    public void onCreate() {
        super.onCreate();

        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Initialize UCE_Handler Library
        new UCEHandler.Builder(this).build();
        // Or, you can define it manually.
        /* UploadService.NAMESPACE = "com.yourcompany.yourapp";*/

        //init firebase
        FCMHandler.enableFCM();

        mInstance = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }


      // initLocale();


    }

//
//    private void initLocale() {
//        String lang = LocaleHelper.getPersistedData(this, null);
//        if (lang == null) {
//
//            LocaleHelper.persist(this, "en");
//        }
//    }
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
//    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }


    }


    public static synchronized Initializer getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }



}
