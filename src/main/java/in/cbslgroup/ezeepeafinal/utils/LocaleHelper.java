package in.cbslgroup.ezeepeafinal.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

public class LocaleHelper {

    public static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context){

        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);

    }

    public static Context onAttach(Context context,String defaultLanguage){

        String lang = getPersistedData(context,defaultLanguage);
        Log.e("test_locale",lang);
        return setLocale(context, lang);

    }

    public static Context setLocale(Context context, String lang) {

        persist(context,lang);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){

            return updateResources(context,lang);
        }

        return updateResourceLegacy(context,lang);

    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Log.e("test_update_res",lang);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);

    }

    private static Context updateResourceLegacy(Context context, String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){

            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        return context;

    }


    public static void persist(Context context, String lang) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SELECTED_LANGUAGE,lang);
        editor.apply();

    }

    public static String getPersistedData(Context context, String language) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  sharedPreferences.getString(SELECTED_LANGUAGE,language);
    }


}
