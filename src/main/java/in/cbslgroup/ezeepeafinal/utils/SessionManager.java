package in.cbslgroup.ezeepeafinal.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import in.cbslgroup.ezeepeafinal.ui.activity.LoginActivity;


public class SessionManager {

    // (make variable public to access from outside)
    public static final String KEY_NAME = "user_name";
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_CONTACT = "user_contact";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_DESIGNATION = "user_designation";
    public static final String UNLOCKED_SLID = "unlocked_slid";
    public static final String KEY_IP = "user_ip";
    public static final String KEY_LANG = "user_lang";

    // Sharedpref file name
    private static final String PREF_NAME = "UserDetails";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;


    public SessionManager(Context context) {

        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
        this.context = context;

    }


    public void createLoginSession(String userid, String name, String email, String contact, String designation) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        //Storing User Details

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CONTACT, contact);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_DESIGNATION, designation);


        // commit changes
        editor.commit();
    }


    public void setLanguage(String lang){
        editor.putString(KEY_LANG,lang);
        editor.commit();
    }

    public String getLanguage(){

        return sharedPreferences.getString(KEY_LANG, "en");
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USERID, null);
    }


    //methods used in lock folder
    public void storeUnlocked(String slid) {


        if (getStoredUnlockedSlids() != null) {


            try {

                JSONArray jsonArray = new JSONArray(getStoredUnlockedSlids());

                if (!jsonArray.toString().contains(slid)) {
                    jsonArray.put(slid);
                    editor.putString(UNLOCKED_SLID, jsonArray.toString());
                    editor.commit();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(slid);
            editor.putString(UNLOCKED_SLID, jsonArray.toString());
            editor.commit();

        }


    }

    public String getStoredUnlockedSlids() {

        return sharedPreferences.getString(UNLOCKED_SLID, null);
    }

    public boolean isSlidPresent(String slid) {

        if (sharedPreferences.getString(UNLOCKED_SLID, null) != null) {

            try {
                if (new JSONArray(getStoredUnlockedSlids()).toString().contains(slid)) {

                    return true;


                } else {

                    return false;

                }


            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            }

        }

        return false;

    }

    public void resetStoredSlid(){

        editor.putString(UNLOCKED_SLID, null);
        editor.commit();

    }

    public void setIP(String ip){

        editor.putString(KEY_IP, ip);
        editor.commit();
    }

    public String getIP(){

        return sharedPreferences.getString(KEY_IP, "192.168.1.1");
    }


    /**
     * Get stored session data
     */

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));
        user.put(KEY_CONTACT, sharedPreferences.getString(KEY_CONTACT, null));
        user.put(KEY_USERID, sharedPreferences.getString(KEY_USERID, null));
        user.put(KEY_DESIGNATION, sharedPreferences.getString(KEY_DESIGNATION, null));
        // return user
        return user;
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

        }


    }


    /**
     * Clear session details
     */

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);



    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }


}
