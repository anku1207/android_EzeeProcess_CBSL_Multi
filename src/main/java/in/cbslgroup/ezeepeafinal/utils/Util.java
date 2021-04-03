package in.cbslgroup.ezeepeafinal.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.R;


public class Util {

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getStringByLocal(Activity context, int id, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(id);
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    public static  void logLargeString(String data, String tag) {
        final int CHUNK_SIZE = 4076;  // Typical max logcat payload.
        int offset = 0;
        while (offset + CHUNK_SIZE <= data.length()) {
            Log.e(tag, data.substring(offset, offset += CHUNK_SIZE));
        }
        if (offset < data.length()) {
            Log.e(tag, data.substring(offset));
        }
    }

    public static void startPowerSaverIntent(Context context) {
        SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        boolean skipMessage = settings.getBoolean("skipProtectedAppCheck", false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            boolean foundCorrectIntent = false;
            for (Intent intent : Constants.POWERMANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;
                    final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                    dontShowAgain.setText("Do not show again");
                    dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            editor.putBoolean("skipProtectedAppCheck", isChecked);
                            editor.apply();
                        }
                    });

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                            .setView(dontShowAgain)
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    break;
                }
            }
            if (!foundCorrectIntent) {
                editor.putBoolean("skipProtectedAppCheck", true);
                editor.apply();
            }
        }
    }

    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void setSearchviewTextSize(SearchView searchView, int fontSize, String text) {
        try {
            AutoCompleteTextView autoCompleteTextViewSearch = searchView.findViewById(searchView.getContext().getResources().getIdentifier("app:id/search_src_text", null, null));

            if (autoCompleteTextViewSearch != null) {
                autoCompleteTextViewSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                autoCompleteTextViewSearch.setBackgroundColor(Color.TRANSPARENT);
                autoCompleteTextViewSearch.setHint(text);
            } else {
                LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
                LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
                LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
                AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
                autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                //manoj shakya 02-04-2021
                autoComplete.setBackgroundColor(Color.TRANSPARENT);
                autoComplete.setHint(text);
            }
        } catch (Exception e) {
            LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
            autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            autoComplete.setBackgroundColor(Color.TRANSPARENT);
            autoComplete.setHint(text);
        }
    }

    static AlertDialog alertDialog;

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static boolean isNullOrEmpty(String string) {

        return TextUtils.isEmpty(string) && string.equals("") && string.equalsIgnoreCase("") && string.trim().length() == 0;

    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                File f = new File(appDir, s);
                if (deleteDir(f))
                    Log.i("del", String.format("**************** DELETED -> (%s) *******************", f.getAbsolutePath()));
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //Dynamically converting bytes to kb mb gb etc
    public static String formatFileSize(long size) {

        String hrSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    /*   public void downloadInstall(String apkurl){
        try {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading");


            URL url = new URL(apkurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "softhris.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new    File(Environment.getExternalStorageDirectory() + "/download/" + "softhris.apk")), "application/vnd.android.package-archive");
            startActivity(intent);

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "download error!", Toast.LENGTH_LONG).show();
        }
    }
*/

    //Alert dialog succcess

    public static void alertError(String message, Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
        tv_error_heading.setText("Error");
        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
        tv_error_subheading.setText(message);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public static void alertProcessing(String onOff, Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_alert, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        if (onOff.equalsIgnoreCase("on")) {

            alertDialog.show();
        } else if (onOff.equalsIgnoreCase("off")) {

            alertDialog.dismiss();

        }


    }

    public static String getPathFromURI(Uri uri, Context context) {
        String realPath = "";
// SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            String[] proj = {MediaStore.Images.Media.DATA};
            @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = 0;
            String result = "";
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                realPath = cursor.getString(column_index);
            }
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(context, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                realPath = cursor.getString(column_index);
            }
        }
        // SDK > 19 (Android 4.4)
        else {


            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
            int columnIndex = 0;
            if (cursor != null) {
                columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    realPath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }
        return realPath;
    }

    public static void alertSuccess(String message, Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
        tv_error_heading.setText("Success");
        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
        tv_error_subheading.setText(message);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
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

    public static void checkPermission(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                ActivityCompat.requestPermissions(activity, permissions, 101);

            }


        } else {
            // write your logic here
        }

    }

    public static void printParams(Map params, String logname) {

        JSONObject jsonObject = new JSONObject(params);
        Log.e(logname, jsonObject.toString());

    }

    public static String sha1Encypt(String text) {


        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }

    }

    public static String base64Encode(String text) {

        byte[] encrpt = new byte[0];
        encrpt = text.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);
        return base64;
    }

    public static String base64Decode(String base64) {

        byte[] decrypt = Base64.decode(base64, Base64.DEFAULT);
        String decoded = null;
        decoded = new String(decrypt, StandardCharsets.UTF_8);
        return decoded;

    }

    public static Boolean isSlidPresent(ArrayList<String> arrayList, String slid) {

        return arrayList.contains(slid);
    }


}
