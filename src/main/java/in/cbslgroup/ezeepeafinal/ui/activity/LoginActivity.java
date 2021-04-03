package in.cbslgroup.ezeepeafinal.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.services.FirebaseMessagingService;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.FCMHandler;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class LoginActivity extends AppCompatActivity {

    private String username, password;
    private AlertDialog alertDialog;
    private SessionManager session;
    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText tieUsername, tiePassword;
    private Button btnLogin;
    private ProgressDialog progress;

        private void initLocale() {
        String lang = LocaleHelper.getPersistedData(this, null);
        if (lang == null) {

            LocaleHelper.persist(this, "en");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //intializing Views
        initViews();

        // Session Manager
        initSessionManager();
        initListeners();
        initLocale();

        if (session.isLoggedIn()){


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        username = String.valueOf(tieUsername.getText());
        password = String.valueOf(tiePassword.getText());

        //recreate();

//        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//        startActivity(intent);


    }

    private void initSessionManager() {

        session = new SessionManager(getApplicationContext());
    }

    private void initListeners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {

                    username = String.valueOf(tieUsername.getText());
                    password = String.valueOf(tiePassword.getText());


                    if (TextUtils.isEmpty(username)) {


                        tilUsername.setError(getString(R.string.enter_username));

                    } else if (TextUtils.isEmpty(password)) {

                        tilPassword.setError(getString(R.string.enter_password));

                    } else {


                        tilPassword.setErrorEnabled(false);
                        tilUsername.setErrorEnabled(false);
                        tilUsername.setError(null);
                        tilPassword.setError(null);

                        Log.e("username", username);
                        Log.e("password", password);



                        //Login with username and password
                        login(username, password);

                      /*  FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String token = instanceIdResult.getToken();
                                Log.e("FCM_TOKEN",token);



                            }
                        });*/

                    }

                } else {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                    LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_error, null);

                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                    TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                    tv_error_heading.setText(getString(R.string.error));
                    TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                    tv_error_subheading.setText(getString(R.string.no_internet_connection_found));
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });

                    dialogBuilder.setView(dialogView);

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();


                }

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }


    void initViews() {

        //Textinputlayouts
        tilUsername = findViewById(R.id.til_login_username);
        tilPassword = findViewById(R.id.til_login_password);

        //TextInputEditexts
        tieUsername = findViewById(R.id.tie_login_username);
        tiePassword = findViewById(R.id.tie_login_password);

        btnLogin = findViewById(R.id.btn_login);

        progress = new ProgressDialog(this);


    }

    public void login(final String username, final String password) {

        progress.setTitle(getString(R.string.logging_in));
        progress.setMessage(getString(R.string.please_wait));
        progress.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Login response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");
                    String action = jsonObject.getString("action");


                    if (error.equalsIgnoreCase("false")) {


                        String firstname = jsonObject.getString("FirstName");
                        String lastname = jsonObject.getString("LastName");
                        String email = jsonObject.getString("Email");
                        String userid = jsonObject.getString("userID");
                        String designation = jsonObject.getString("Designation");
                        String phonenumber = jsonObject.getString("Phone");


                        // String fullname = firstname + " " + lastname;

                        Log.e("------", "------");
                        Log.e("firstname", firstname);
                        Log.e("lastname", lastname);
                        Log.e("email", email);
                        Log.e("userid", userid);
                        Log.e("designation", designation);
                        Log.e("phonenumber", phonenumber);


                        //session.createLoginSession(userid, fullname, email, phonenumber, designation);


                        //enabling the fcm
                        FCMHandler.enableFCM();
                        String fullname = firstname + " " + lastname;
                        session.createLoginSession(userid, fullname, email, phonenumber, designation);

                        //Redirecting  to DrawerActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                        progress.dismiss();



//                        //Redirecting  to DrawerActivity
//                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
//                        intent.putExtra("firstname", firstname);
//                        intent.putExtra("lastname", lastname);
//                        intent.putExtra("email", email);
//                        intent.putExtra("userid", userid);
//                        intent.putExtra("designation", designation);
//                        intent.putExtra("phonenumber", phonenumber);
//                        startActivity(intent);
//                        finish();
//
//
//                        progress.dismiss();


                    } else if (error.equalsIgnoreCase("true") && action.equalsIgnoreCase("active_deactivate")) {


                        progress.dismiss();

                        //Toast.makeText(LoginActivity.this, "Username password dont match", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText(getString(R.string.login_failed));
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(msg);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


                    } else if (error.equalsIgnoreCase("true") && action.equalsIgnoreCase("login")) {

                        progress.dismiss();

                        String login_attempts = jsonObject.getString("login_attempts");

                        //int leftAttempts = 5 - (counterLoginAttempts);

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);

                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText(R.string.login_failed);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(getString(R.string.you_have)+" "+ login_attempts +" "+ getString(R.string.login_attempts_left_otherwise_account_will_be_deactivated));
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                        // counterLoginAttempts++;

//                        if (counterLoginAttempts > 4) {
//
//                            //userdeactivate method
//                            deactivateUser(username);
//
//
//
//                        } else {
//
//
//
//
//
//                        }


                    } else if (error.equalsIgnoreCase("true") && action.equalsIgnoreCase("invalid_email")) {

                        progress.dismiss();

                        //Toast.makeText(LoginActivity.this, "Username password dont match", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText(R.string.login_failed);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(msg);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                    }

                    else {

                        Toast.makeText(LoginActivity.this,  R.string.server_error, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                String tokenid = FirebaseMessagingService.getToken(LoginActivity.this);
                Log.e("token id in login", tokenid);

                params.put("name", username);
                params.put("pwd", password);
                params.put("ip", getDeviceIpAddress());
                params.put("ln", session.getLanguage());


                return params;
            }

        };


        /*  RequestQueue queue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
        queue.add(stringRequest);*/

        VolleySingelton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);


    }

    @Deprecated
    public void loginOld(final String username, final String password) {

        progress.setTitle("Logging In");
        progress.setMessage("Please wait");
        progress.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Login response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");


                    if (status.equals("True")) {


                        //enabling the fcm
                        FCMHandler.enableFCM();

                        String firstname = jsonObject.getString("FirstName");
                        String lastname = jsonObject.getString("LastName");
                        String email = jsonObject.getString("Email");
                        String userid = jsonObject.getString("userID");
                        String designation = jsonObject.getString("Designation");
                        String phonenumber = jsonObject.getString("Phone");
                        String fullname = firstname + " " + lastname;

                        session.createLoginSession(userid, fullname, email, phonenumber, designation);

                        //Redirecting  to DrawerActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                        progress.dismiss();


                    } else if (status.equalsIgnoreCase("False")) {

                        progress.dismiss();

                        //Toast.makeText(LoginActivity.this, "Username password dont match", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText( R.string.error);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(R.string.username_and_password_didnt_match);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

              /*  String tokenid = FirebaseMessagingService.getToken(LoginActivity.this);
                Log.e("token id in login",tokenid);*/

                params.put("name", username);
                params.put("pwd", password);
                params.put("ip", getDeviceIpAddress());
                params.put("ln", session.getLanguage());


                return params;
            }

        };


        VolleySingelton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onBackPressed() {

        //bug on back goes to mainactivity
        // super.onBackPressed();
        finishAffinity();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @NonNull
    private String getDeviceIpAddress() {
        String actualConnectedToNetwork = null;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                actualConnectedToNetwork = getWifiIp();
            }
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = "127.0.0.1";
        }
        return actualConnectedToNetwork;
    }

    @Nullable
    private String getWifiIp() {
        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            int ip = mWifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                    + ((ip >> 24) & 0xFF);
        }
        return null;
    }

    @Nullable
    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();


                        /* Log.e("Hostname",host);*/
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }

    void updateToken(String userid, String tokenid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPDATE_FIREBASE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updatefbtoken", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("tokenid", tokenid);
                params.put("ln", session.getLanguage());


                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }


}
