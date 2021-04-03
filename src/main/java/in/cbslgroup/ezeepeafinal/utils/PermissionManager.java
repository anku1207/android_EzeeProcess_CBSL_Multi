package in.cbslgroup.ezeepeafinal.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PermissionManager {

    // (make variable public to access from outside)
    public static final String KEY_VISITOR_PASS = "visitor_pass";
    public static final String KEY_IN_TRAY = "in_tray";
    public static final String KEY_TASK_TRACk = "task_track";


    // Sharedpref file name
    private static final String PREF_NAME = "UserPermissions";
    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;

    public PermissionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
        this.context = context;

    }

    public void setVisitorPassPerm(String value){

        editor.putString(KEY_VISITOR_PASS, value);
        editor.commit();

    }

    public void setInTrayPerm(String value){

        editor.putString(KEY_IN_TRAY, value);
        editor.commit();

    }

    public void setTaskTrackPerm(String value){

        editor.putString(KEY_TASK_TRACk, value);
        editor.commit();

    }


    public HashMap<String, String> getPermissions() {

        HashMap<String, String> perm = new HashMap<String, String>();
        // user name
        perm.put(KEY_VISITOR_PASS, sharedPreferences.getString(KEY_VISITOR_PASS, "0"));
        perm.put(KEY_IN_TRAY, sharedPreferences.getString(KEY_IN_TRAY, "0"));
        perm.put(KEY_TASK_TRACk, sharedPreferences.getString(KEY_TASK_TRACk, "0"));

        return perm;
    }

}
