package in.cbslgroup.ezeepeafinal.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class ProfileActivity extends AppCompatActivity {

    SessionManager session;
    TextView tvUsernameHeading, tvDesignationHeading, tvUsername, tvDesignation, tvContact, tvEmail, tvUserRole;

    TextInputEditText tieFirstname, tieLastname, tieContact, tieEmail, tieDesignation, tiePassword, tieRetypePassword;
    TextInputLayout tilFirstname, tilLastname, tilContact, tilEmail, tilDesignation, tilPassword, tilRetypePassword;

    ImageView ivLogo;

    View dialogView;
    String ipadress;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> names1 = new ArrayList<>();
    ArrayList<String> names2 = new ArrayList<>();

    AlertDialog alertDialog, alertDialogSuccess;
    ProgressBar progressbar;
    LinearLayout ll_profile_main;
    private String username, designation, contact, email, firstname, lastname, userid;

    private String newPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        //intializing the views

        initViews();
        initLocale();

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        username = user.get(SessionManager.KEY_NAME);
        // email
        email = user.get(SessionManager.KEY_EMAIL);
        // contact
        contact = user.get(SessionManager.KEY_CONTACT);
        // designation
        designation = user.get(SessionManager.KEY_DESIGNATION);

        //userid

        userid = user.get(SessionManager.KEY_USERID);


        firstname = username.substring(0, username.indexOf(" "));
        lastname = username.substring(username.indexOf(" "), username.length());
        tvUsername.setText(username);
        tvContact.setText(contact);
        tvEmail.setText(email);
        tvDesignation.setText(designation);
        tvUsernameHeading.setText(username);
        tvDesignationHeading.setText(designation);

        getUserRole(userid);



    }

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(this, null);
        if (lang == null) {

            LocaleHelper.persist(this, "en");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }



    void initViews() {

        tvDesignationHeading = findViewById(R.id.tv_profile_heading_designation);
        tvUsernameHeading = findViewById(R.id.tv_profile_heading_username);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvContact = findViewById(R.id.tv_profile_contact);
        tvDesignation = findViewById(R.id.tv_profile_designation);
        tvUsername = findViewById(R.id.tv_profile_username);
        ivLogo = findViewById(R.id.iv_profile_pic);
        tvUserRole = findViewById(R.id.tv_profile_userrole);
        progressbar = findViewById(R.id.progressBar_profile);
        progressbar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        ll_profile_main = findViewById(R.id.ll_profile_main);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            editProfileForm();


            return true;
        }

        if (id == R.id.action_change_password) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            changePasswordForm();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void editProfileForm() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alertdialog_edit_profile, null);

        tieFirstname = dialogView.findViewById(R.id.tie_edit_profile_firstname);
        tieLastname = dialogView.findViewById(R.id.tie_edit_profile_lastname);
        tieContact = dialogView.findViewById(R.id.tie_edit_profile_contact);
        tieDesignation = dialogView.findViewById(R.id.tie_edit_profile_designation);
        tieEmail = dialogView.findViewById(R.id.tie_edit_profile_email);


        tilFirstname = dialogView.findViewById(R.id.til_edit_profile_firstname);
        tilLastname = dialogView.findViewById(R.id.til_edit_profile_lastname);
        tilContact = dialogView.findViewById(R.id.til_edit_profile_contact);
        tilEmail = dialogView.findViewById(R.id.til_edit_profile_email);
        tilDesignation = dialogView.findViewById(R.id.til_edit_profile_designation);


        String username = tvUsername.getText().toString();
        String firstname = username.substring(0, username.lastIndexOf(" "));
        String lastname = username.substring(username.lastIndexOf(" ") + 1);

        tieEmail.setText(tvEmail.getText().toString());
        tieDesignation.setText(tvDesignation.getText().toString());
        tieContact.setText(tvContact.getText().toString());
        tieFirstname.setText(firstname);
        tieLastname.setText(lastname);


        Button btn_cancel = dialogView.findViewById(R.id.btn_edit_profile_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        Button btn_save = dialogView.findViewById(R.id.btn_edit_profile_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tilFirstname.setError(null);
                tilLastname.setError(null);
                tilContact.setError(null);
                tilDesignation.setError(null);
                tilEmail.setError(null);
                tilEmail.setErrorEnabled(false);
                tilLastname.setErrorEnabled(false);
                tilContact.setErrorEnabled(false);
                tilDesignation.setErrorEnabled(false);
                tilEmail.setErrorEnabled(false);


                if (TextUtils.isEmpty(tieFirstname.getText().toString().trim()) || tieFirstname.getText().toString().trim().equals("")) {


                    tilFirstname.setError(getString(R.string.enter_first_name));


                } else if (TextUtils.isEmpty(tieLastname.getText().toString().trim()) || tieLastname.getText().toString().trim().equals("")) {

                    tilLastname.setError(getString(R.string.enter_last_name));


                } else if (TextUtils.isEmpty(tieContact.getText().toString().trim()) || tieContact.getText().toString().trim().equals("")) {

                    tilContact.setError(getString(R.string.enter_contact));

                }

                else if (tieContact.getText().toString().trim().length() != 10) {

                    tilContact.setError(getString(R.string.enter_a_valid_contact_number));


                } else if (TextUtils.isEmpty(tieEmail.getText().toString().trim()) || tieEmail.getText().toString().trim().equals("")) {

                    tilEmail.setError(getString(R.string.enter_email));


                } else if (TextUtils.isEmpty(tieDesignation.getText().toString().trim()) || tieDesignation.getText().toString().trim().equals("")) {

                    tilDesignation.setError(getString(R.string.enter_designation));


                } else {


                    tilFirstname.setError(null);
                    tilLastname.setError(null);
                    tilContact.setError(null);
                    tilDesignation.setError(null);
                    tilEmail.setError(null);
                    tilEmail.setErrorEnabled(false);
                    tilLastname.setErrorEnabled(false);
                    tilContact.setErrorEnabled(false);
                    tilDesignation.setErrorEnabled(false);
                    tilEmail.setErrorEnabled(false);

                    String fname = tieFirstname.getText().toString().trim();
                    String lname = tieLastname.getText().toString().trim();
                    String phn = tieContact.getText().toString().trim();
                    String desg = tieDesignation.getText().toString().trim();
                    String email = tieEmail.getText().toString().trim();
                    String ip = MainActivity.ip;
                    String fullname = fname + " " + lname;


                    String log = checkUpdateProfile(fullname, email, phn, desg);

                    updateProfile(userid, fname, lname, email, phn, desg, ip, log);


                    alertDialog.dismiss();


                  /*  AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                    LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
                    TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                    tv_error_heading.setText("Success");
                    TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                    tv_error_subheading.setText("Working");
                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialogSuccess.dismiss();
                            alertDialog.dismiss();




                        }
                    });

                    dialogBuilder.setView(dialogView);
                    alertDialogSuccess = dialogBuilder.create();
                    alertDialogSuccess.setCancelable(false);
                    alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialogSuccess.show();*/


                }


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }


    void changePasswordForm() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alertdialog_change_password, null);

        tiePassword = dialogView.findViewById(R.id.tie_edit_profile_new_password);
        tieRetypePassword = dialogView.findViewById(R.id.tie_edit_profile_retype_password);

        tilPassword = dialogView.findViewById(R.id.til_edit_profile_new_password);
        tilRetypePassword = dialogView.findViewById(R.id.til_edit_profile_retype_password);


        Button btn_save = dialogView.findViewById(R.id.btn_change_password_save);


        Button btn_cancel = dialogView.findViewById(R.id.btn_change_password_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        tiePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Pattern specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Pattern UpperCasePattern = Pattern.compile("[A-Z ]");
                Pattern lowerCasePattern = Pattern.compile("[a-z ]");
                Pattern digitCasePattern = Pattern.compile("[0-9 ]");

                newPassword = s.toString();

                tilPassword.setError(null);
                tilPassword.setErrorEnabled(false);

                if (newPassword.equalsIgnoreCase("") || newPassword.isEmpty()) {

                    tilPassword.setError(getString(R.string.please_enter_new_password));
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);

                }

               else  if (newPassword.length() < 8){

                    tilPassword.setError(getString(R.string.password_eight_charcter_error));
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);

                }

               else  if(!specialCharPattern.matcher(newPassword).find()){

                    tilPassword.setError(getString(R.string.pwd_one_special_character_error));
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);

                }


                else if (!UpperCasePattern.matcher(newPassword).find()) {

                    tilPassword.setError(getString(R.string.pwd_one_uppercase_error));
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);

                }

               else if (!lowerCasePattern.matcher(newPassword).find()) {

                    tilPassword.setError(getString(R.string.pwd_one_lowercase_error));
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);


                }

               else if (!digitCasePattern.matcher(newPassword).find()) {

                    tilPassword.setError(getString(R.string.pwd_have_one_digit_error));
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);


                }


                else{

                    tilPassword.setErrorEnabled(false);
                    tilPassword.setError(null);

                    btn_save.setEnabled(true);
                    tieRetypePassword.setEnabled(true);
                    tieRetypePassword.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                    //changePasswordRequest(userid, confirmPassword, name, getDeviceIpAddress());

                }

//                else {
//
//                    tilPassword.setError(null);
//                    tilPassword.setErrorEnabled(false);
//
//                    if (newPassword.length() < 8) {
//
//
//                        tilPassword.setError(getString(R.string.password_eight_charcter_error));
//                        btn_save.setEnabled(false);
//                        tieRetypePassword.setEnabled(false);
//
//
//                    } else {
//
//                        if (!specialCharPattern.matcher(newPassword).find()) {
//
//                            tilPassword.setError(getString(R.string.pwd_one_special_character_error));
//                            btn_save.setEnabled(false);
//                            tieRetypePassword.setEnabled(false);
//
//                        } else {
//
//                            tilPassword.setErrorEnabled(false);
//                            tilPassword.setError(null);
//
//                            if (!UpperCasePattern.matcher(newPassword).find()) {
//
//
//                                tilPassword.setError(getString(R.string.pwd_one_uppercase_error));
//                                btn_save.setEnabled(false);
//                                tieRetypePassword.setEnabled(false);
//
//                            } else {
//
//                                tilPassword.setErrorEnabled(false);
//                                tilPassword.setError(null);
//
//                                if (!lowerCasePattern.matcher(newPassword).find()) {
//
//                                    tilPassword.setError(getString(R.string.pwd_one_lowercase_error));
//                                    btn_save.setEnabled(false);
//                                    tieRetypePassword.setEnabled(false);
//
//
//                                } else {
//
//                                    tilPassword.setErrorEnabled(false);
//                                    tilPassword.setError(null);
//
//                                    if (!digitCasePattern.matcher(newPassword).find()) {
//
//                                        tilPassword.setError(getString(R.string.pwd_have_one_digit_error));
//                                        btn_save.setEnabled(false);
//                                        tieRetypePassword.setEnabled(false);
//
//
//                                    } else {
//
//                                        tilPassword.setErrorEnabled(false);
//                                        tilPassword.setError(null);
//
//                                        btn_save.setEnabled(true);
//                                        tieRetypePassword.setEnabled(true);
//                                        tieRetypePassword.setHintTextColor(getResources().getColor(R.color.colorPrimary));
//                                        //changePasswordRequest(userid, confirmPassword, name, getDeviceIpAddress());
//
//
//                                    }
//
//
//                                }
//
//
//                            }
//
//
//                        }
//
//
//                    }
//
//                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tieRetypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                confirmPassword = s.toString();


                tilRetypePassword.setError(null);
                tilRetypePassword.setErrorEnabled(false);


                if(confirmPassword.length() == 0)
                {

                    tilRetypePassword.setError(getString(R.string.plese_enter_confirm_password));
                }


                else if (!confirmPassword.equals(newPassword)) {

                    tilRetypePassword.setError(getString(R.string.new_pwd_confirm_pwd_doesnt_match));




                } else {

                    tilRetypePassword.setError(null);
                    tilRetypePassword.setErrorEnabled(false);


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newPassword = tiePassword.getText().toString().trim();
                confirmPassword = tieRetypePassword.getText().toString().trim();

                tilPassword.setError(null);
                tilRetypePassword.setError(null);
                tilPassword.setErrorEnabled(false);
                tilRetypePassword.setErrorEnabled(false);


                if(newPassword.isEmpty()||newPassword.equals("")){


                    tilPassword.setError(getString(R.string.enter_new_pwd));

                }

                else if (confirmPassword.isEmpty()||confirmPassword.equals("")){

                    tilRetypePassword.setError(getString(R.string.plese_enter_confirm_password));

                }

                else if(!confirmPassword.equals(newPassword))
                {

                    tilRetypePassword.setError(getString(R.string.new_pwd_confirm_pwd_doesnt_match));

                }

                else if(!newPassword.equals(confirmPassword))
                {

                    tilRetypePassword.setError(getString(R.string.new_pwd_confirm_pwd_doesnt_match));

                }

                else{

                    tilPassword.setError(null);
                    tilRetypePassword.setError(null);
                    tilPassword.setErrorEnabled(false);
                    tilRetypePassword.setErrorEnabled(false);


                    String name = tvUsername.getText().toString().trim();

                    Log.e("confirmpass",confirmPassword);

                    changePasswordRequest(userid, confirmPassword, name, MainActivity.ip);

                }


            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();


    }


    void getUserRole(final String userid) {

        ll_profile_main.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String userrole = jsonObject.getString("user_role");
                    tvUserRole.setText(userrole);

                    getProfilePic(userid);


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

                params.put("userid", userid);
                params.put("roles", "user_role");
                params.put("action", "getSpecificRoles");


                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);


    }

    void getProfilePic(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    //JSONArray jsonObject = new JSONArray(response);
                    JSONObject jsonObject = new JSONObject(response);

                    String profilepic = jsonObject.getString("profilepic");

                    if (profilepic.isEmpty() || profilepic.equals("")) {

                        ivLogo.setImageResource(R.drawable.ic_avatar);

                    } else {
                        byte[] decodedString = Base64.decode(profilepic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ivLogo.setImageBitmap(decodedByte);
                    }


                    progressbar.setVisibility(View.GONE);
                    ll_profile_main.setVisibility(View.VISIBLE);


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

                params.put("userid", userid);

                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
        ;


    }

    void updateProfile(final String userid, final String firstname, final String lastname, final String email, final String phone, final String designation, final String ip, final String log) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile edit", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equals("false")) {


                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();

                        setUserDetails(userid);


                    } else {

                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();


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

                params.put("userid", userid);
                params.put("fname", firstname);
                params.put("lname", lastname);
                params.put("ip", ip);
                params.put("email", email);
                params.put("phn", phone);
                params.put("desg", designation);
                params.put("log", log);

                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
        ;


    }

    void changePasswordRequest(final String userid, final String password, final String name, final String ip) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response changepwd ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equals("false")) {


                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();


                    } else {

                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();


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

                params.put("userid", userid);
                params.put("pwd", password);
                params.put("name", name);
                params.put("ip", ip);


                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
        ;


    }

    void setUserDetails(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("userdetails profileAct", response);

                try {
                   // JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = new JSONObject(response);

                    String firstname = jsonObject.getString("firstname");
                    String lastname = jsonObject.getString("lastname");
                    String email = jsonObject.getString("email");
                    String designation = jsonObject.getString("designation");
                    String contact = jsonObject.getString("contact");

                    tvUsername.setText(firstname + " " + lastname);
                    tvContact.setText(contact);
                    tvDesignation.setText(designation);
                    tvDesignationHeading.setText(designation);
                    tvUsernameHeading.setText(firstname + " " + lastname);
                    tvEmail.setText(email);


                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    session.createLoginSession(userid, firstname + " " + lastname, email, contact, designation);

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

                params.put("userid", userid);

                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
        ;


    }

    String checkUpdateProfile(String name, String eml, String phone, String desig) {


        StringBuilder log = new StringBuilder();


     /*   if(firstname.equals(fname.trim())){

            Log.e("firstname","has not been updated");


        }
        else{

            Log.e("firstname","has been updated");

        }


        if(lastname.equals(lname.trim())){

            Log.e("lastname","has not been updated");


        }*/
      /*  else{

            Log.e("lastname","has been updated");

         }*/
        Log.e("previous details", tvUsername.getText().toString() + " " + tvContact.getText().toString() + " " + tvContact.getText().toString() + " " + " " + tvDesignation.getText().toString());
        Log.e("after details", name + " " + eml + " " + phone + " " + " " + desig);
        if (tvUsername.getText().toString().equals(name)) {

            Log.e("Username", "has not been updated");


        } else {

            Log.e("Username", "has been updated");
            log.append("Username has been updated ");

        }

        if (tvEmail.getText().toString().equals(eml.trim())) {

            Log.e("email", "has not been updated");


        } else {

            Log.e("email", "has been updated");
            log.append("Email has been updated ");
        }

        if (tvContact.getText().toString().equals(phone.trim())) {

            Log.e("contact", "has not been updated");


        } else {

            Log.e("contact", "has been updated");
            log.append("Contact has been updated ");

        }

        if (tvDesignation.getText().toString().equals(desig.trim())) {

            Log.e("designation", "has not been updated");


        } else {

            Log.e("designation", "has been updated");
            log.append("Designation has been updated ");

        }


        Log.e("log profile updated", log.toString());

        if (log.toString().equals("")) {

            return "Nothing is updated ";
        } else {

            return log.toString();
        }

    }

}
