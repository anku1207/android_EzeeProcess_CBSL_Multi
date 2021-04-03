package in.cbslgroup.ezeepeafinal.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.ui.activity.visitorpass.VisitorPassActionActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.TaskInProcessActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.TaskTrackingActivity;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.PermissionManager;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private final static String ADMIN_CHANNEL_ID = "007";

    NotificationManager notificationManager;
    Intent intent;
    PendingIntent pendingIntent;

    SessionManager sessionManager;
    PermissionManager permissionManager;

    public static String getToken(Context context) {

        return context.getSharedPreferences("fb_token", MODE_PRIVATE).getString("tokenid", "empty");
    }

    public static void clearToken(Context context) {

        context.getSharedPreferences("fb_token", MODE_PRIVATE).edit().clear().apply();

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e("newToken_fb", s);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {

            Log.e("userid in fb", sessionManager.getUserDetails().get("user_id"));
            Log.e("tokenid fb", s);

            getSharedPreferences("fb_token", MODE_PRIVATE).edit().putString("tokenid", s).apply();
            updateToken(sessionManager.getUserDetails().get("user_id"), s);

                 /*    sPfbToken = getSharedPreferences("fb_token", MODE_PRIVATE);
        editor = sPfbToken.edit();
        editor.putString("tokenid",s);

        editor.commit();*/


        }


        // getSharedPreferences("fb_token", MODE_PRIVATE).edit().commit();


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("onMessageRecieved", remoteMessage.getData().toString());


        sessionManager = new SessionManager(getApplicationContext());
        permissionManager = new PermissionManager(getApplicationContext());

        //if the user is logged in
        if (sessionManager.isLoggedIn()) {

            sendNotification(remoteMessage, permissionManager);


//            if(MainActivity.tasktrackstatus.equalsIgnoreCase("1")){
//
//
//            }
//            else if (MainActivity.tasktrackstatus.equalsIgnoreCase("0")){
//
//                Log.e("permission fb","no permisson");
//            }


        }


    }

    public void updateToken(final String u, final String t) {

        Log.e("blabla", "ok");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPDATE_FIREBASE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updatefbtoken service", response);


         /*       sPfbToken = getSharedPreferences("fb_token", MODE_PRIVATE);
                editor = sPfbToken.edit();
                editor.putString("tokenid",t);

                editor.commit();
*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userid", u);
                params.put("tokenid", t);
                return params;
            }
        };

        VolleySingelton.getInstance(FirebaseMessagingService.this).addToRequestQueue(stringRequest);


    }

    void sendNotification(RemoteMessage remoteMessage, PermissionManager permissionManager) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = new Random().nextInt(60000);

        String title = remoteMessage.getData().get("title");

        if (title.equalsIgnoreCase("logout")) {

            Log.e("fc", "here");
            logout();

        } else if (title.equalsIgnoreCase("Visitor Pass") && permissionManager.getPermissions().get(PermissionManager.KEY_VISITOR_PASS).equals("1")) {

            String path = remoteMessage.getData().get("pic_path");
            String msg = remoteMessage.getData().get("message");
            String visId = remoteMessage.getData().get("visid");
            String actionby = remoteMessage.getData().get("actionby");

            intent = new Intent(this, VisitorPassActionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Id", visId);
            intent.putExtra("actionby", actionby);
            pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Initializer.ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ezeeiconmain))
                    .setContentTitle(title)
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setColor(this.getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(pendingIntent)
                    .setContentText(msg);


            try {

                Bitmap bitmap = Glide.with(this)
                        .asBitmap()
                        .load(ApiUrl.BASE_URL_ROOT + path)
                        .submit()
                        .get();


                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setBigContentTitle(title));


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        } else {

            String taskid = remoteMessage.getData().get("id");
            String taskname = remoteMessage.getData().get("taskname");
            String msg = remoteMessage.getData().get("message");

            // String actionBy = remoteMessage.getData().get("actionBy");
            //String datetime = remoteMessage.getData().get("datetime");

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON_OBJECT_noti", object.toString());

            Log.e("getFrom", remoteMessage.getFrom());


       /* Spannable sb = new SpannableString(remoteMessage.getData().get("title"));
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/

            if (title.equalsIgnoreCase("In Tray") &&
                    permissionManager.getPermissions().get(PermissionManager.KEY_IN_TRAY).equals("1")) {

                intent = new Intent(this, TaskInProcessActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("taskid", taskid);
                intent.putExtra("taskname", taskname);
                intent.putExtra("taskStatus", "Pending");
                pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                        PendingIntent.FLAG_ONE_SHOT);

            } else if (title.equalsIgnoreCase("Task Track") && permissionManager.getPermissions().get(PermissionManager.KEY_TASK_TRACk).equals("1")) {


                intent = new Intent(this, TaskTrackingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ticket_id", taskid);
                pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                        PendingIntent.FLAG_ONE_SHOT);

            }


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Initializer.ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo_notification)  //a resource for your custom small icon
                    .setContentTitle(Html
                            .fromHtml("<b>" + title + "</b>")) //the "title" value you sent in your notification
                    .setContentText(msg) //ditto
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    //.setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.pending_icon))
                    .setColor(this.getResources().getColor(R.color.colorPrimary));

            // Create a BigTextStyle object.
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

    /*    if (title.equalsIgnoreCase("Task Track")) {

            bigTextStyle.bigText(
                    Html.fromHtml(msg + "<br><br><b>Action By : </b>" + actionBy + "\n" + "<br><br><b>Assigned Date : </b>" + datetime));

        } else if (title.equalsIgnoreCase("In Tray")) {

            bigTextStyle.bigText(Html
                    .fromHtml(msg + "<br><br><b>Assigned Date : </b>" + datetime));

        }*/

            bigTextStyle.setBigContentTitle(msg);
            bigTextStyle.setBigContentTitle(Html
                    .fromHtml("<b>" + title + "</b>"));

            // Set big text style.
            notificationBuilder.setStyle(bigTextStyle);

           /* //Vibration
            notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //LED
            notificationBuilder.setLights(Color.RED, 3000, 3000);

            //Ton
            notificationBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
*/


            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());


        }


    }

    void logout() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("logout", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        String msg = jsonObject.getString("message");

                        int notificationId = new Random().nextInt(60000);

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Initializer.ADMIN_CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_logo_notification)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ezeeiconmain))
                                .setContentTitle("Password Changed")
                                .setAutoCancel(true)
                                .setColor(getResources().getColor(R.color.colorPrimary))
                                .setContentText("Logged out due to password reseted");

                        notificationManager.notify(notificationId, notificationBuilder.build());

                        sessionManager.logoutUser();
                        // FCMHandler.disableFCM();


                    } else if (error.equalsIgnoreCase("true")) {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", sessionManager.getUserId());

                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }


}
