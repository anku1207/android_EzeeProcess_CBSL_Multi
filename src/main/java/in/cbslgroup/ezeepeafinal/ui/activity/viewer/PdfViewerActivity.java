package in.cbslgroup.ezeepeafinal.ui.activity.viewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class PdfViewerActivity extends AppCompatActivity {

    PDFView pdfView;
    TextView pdftextview;
    Integer pageNumber = 0;
    ProgressDialog progressDialog;

    ProgressBar progressBar = null;
    String pdfName, filepath, docid,fromWorkflow;

    ProgressDialog mProgressDialog;

    ImageView ivDownloadbtn, ivBack;
    TextView tvpdfname;
    private PowerManager.WakeLock wakeLock;

    DownloadManager downloadManager;

    String ftpFilePath;

    private static final String PDF_Url = ApiUrl.BASE_URL;

    AlertDialog alertDialog;

    SessionManager sessionManager;
    String hasSlid = "false";

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        delFileFromTempPath(ftpFilePath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        AllowRunTimePermission();
        initLocale();

        sessionManager = new SessionManager(this);


        ivBack = findViewById(R.id.iv_pdfview_backbutton);
        ivDownloadbtn = findViewById(R.id.iv_pdfview_downloadbutton);
        tvpdfname = findViewById(R.id.tv_pdfview_filename);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        ivDownloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog alertDialog = new AlertDialog.Builder(PdfViewerActivity.this).create();
                alertDialog.setTitle(getString(R.string.download_pdf));
                alertDialog.setIcon(R.drawable.pdf);
                alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_download) + " " + tvpdfname.getText() + " " + "?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.download),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Downloadpdf(ftpFilePath);

                            }
                        });
                alertDialog.show();


            }
        });

        Intent intent = getIntent();
        pdfName = intent.getStringExtra("filename");
        filepath = intent.getStringExtra("filepath");
        docid = intent.getStringExtra("docid");

        tvpdfname.setText(pdfName);

        pdfView = findViewById(R.id.pdfView);
        pdfView.isBestQuality();
        pdfView.enableAnnotationRendering(true);
        pdftextview = findViewById(R.id.pdftextview);



        if (intent.getStringExtra("workflow")!=null) {

            fromWorkflow = intent.getStringExtra("workflow");

            if(fromWorkflow.equals("true")){

                getFtpFilePath(docid, sessionManager.getUserId());//means workflow or attachment

            }


        }

       else if (Initializer.globalDynamicSlid!=null && !Initializer.globalDynamicSlid.contains("_")) {

            checkFileLocked(Initializer.globalDynamicSlid);

        } else {

            //  new RetrivePDFstream().execute(PDF_Url + filepath);
            getFtpFilePath(docid, sessionManager.getUserId());//means workflow or attachment
        }



    }

    public void delFileFromTempPath(String tempPath) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.FTP_UTIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");

                    if (jsonObject.getString("error").equalsIgnoreCase("false")) {

                        Log.e("ftp_del", msg);
                    } else {

                        Log.e("ftp_del", msg);

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
                params.put("temp_file_path", tempPath);
                params.put("action", "delFtpFile");
                return params;

            }
        };

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);

    }

    void getFtpFilePath(String docid, String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.FTP_UTIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ftpRes", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equalsIgnoreCase("false")) {

                        String filePath = jsonObject.getString("path");
                        ftpFilePath = filePath;

                        new RetrivePDFstream().execute(PDF_Url + ftpFilePath);

                    } else {

                        Toast.makeText(PdfViewerActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        finish();

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
                params.put("userid", userid);
                params.put("doc_id", docid);
                params.put("action", "getFtpPath");

                Util.printParams(params,"ftpparams");

                return params;
            }

        };

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);

    }

    class RetrivePDFstream extends AsyncTask<String, Integer, InputStream> {

//        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar = findViewById(R.id.progressbarpdf);
            progressBar.setVisibility(View.VISIBLE);


        }


        @SuppressLint("WrongThread")
        @Override
        protected InputStream doInBackground(String... strings) {
            progressBar.setVisibility(View.VISIBLE);
            InputStream inputStream = null;


            try {

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                int fileLength = urlConnection.getContentLength();


                if (urlConnection.getResponseCode() == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }


            } catch (IOException e) {

                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(final InputStream inputStream) {


            pdfView.fromStream(inputStream)
                    .spacing(5) // in dp
                    .scrollHandle(new DefaultScrollHandle(PdfViewerActivity.this))
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            pageNumber = page;

                            int pageplusone = page + 1;

                            pdftextview.setText(pageplusone + "/" + pageCount);

                        }
                    })
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {

                            Toast.makeText(PdfViewerActivity.this, R.string.no_file_found, Toast.LENGTH_SHORT).show();

                            onBackPressed();
                        }
                    })

                    .onPageError(new OnPageErrorListener() {
                        @Override
                        public void onPageError(int page, Throwable t) {
                            Toast.makeText(PdfViewerActivity.this, t.getMessage() + "pg", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {

                            progressBar.setVisibility(View.INVISIBLE);

                            getUserRole(MainActivity.userid);

                            //  ivDownloadbtn.setVisibility(View.VISIBLE);


                        }
                    })

                    .load();


        }


    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(PdfViewerActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    //Toast.makeText(PdfViewerActivity.this, "Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(PdfViewerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            //Toast.makeText(PdfViewerActivity.this, "WRITE_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(PdfViewerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }

    public void Downloadpdf(String path) {

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(PDF_Url + path);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


//        File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeProcess Downloads");
//
//        if (!myDirectory.exists()) {
//            myDirectory.mkdirs();
//        }

       // String PATH = "/EzeeProcess Downloads/";


        String PATH = Environment.DIRECTORY_DOWNLOADS;
        request.setDestinationInExternalPublicDir(PATH, uri.getLastPathSegment());
        downloadManager.enqueue(request);


    }

    private void getUserRole(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("user_perm",response);

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.getString("pdf_download").equalsIgnoreCase("0")) {


                        ivDownloadbtn.setVisibility(View.GONE);

                    } else {

                        ivDownloadbtn.setVisibility(View.VISIBLE);

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
                params.put("action", "getSpecificRoles");
                params.put("roles", "pdf_download");

                return params;
            }


        };

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);

    }

    private void checkFileLocked(String slid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOCK_FOLDER,
                response -> {

                    Log.e("pdf_res_lock", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String isProtected = jsonObject.getString("is_protected");
                        String oldPwd = jsonObject.getString("password");


                        Log.e("saved_slids", "-->" + sessionManager.getStoredUnlockedSlids());


//                        if (sessionManager.getStoredUnlockedSlids() == null) {
//
//                            hasSlid = "false";
//
//                        } else {
//
//                            if (new JSONArray(sessionManager.getStoredUnlockedSlids()).toString().contains(Initializer.globalDynamicSlid)) {
//
//                                hasSlid = "true";
//                            }
//                            else{
//
//                                hasSlid = "false";
//                            }
//
//                        }
//
//                        Log.e("hasSlid",hasSlid +"slid"+Initializer.globalDynamicSlid);
//                        Log.e("hasSlid2", String.valueOf(new JSONArray(sessionManager.getStoredUnlockedSlids()).toString().contains(Initializer.globalDynamicSlid)));

                        if (isProtected.equalsIgnoreCase("0") || sessionManager.isSlidPresent(Initializer.globalDynamicSlid)) {

                            //  new RetrivePDFstream().execute(PDF_Url + filepath);
                            getFtpFilePath(docid, MainActivity.userid);


                        } else {

                            showFolderLockDialog(this, oldPwd);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {

                    Log.e("pdf_res_lock_err", error.toString());
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "getLockStatus");
                params.put("slid", slid);
                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void showFolderLockDialog(Context context, String oldPwd) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog_file_lock_password, null);

        TextInputEditText tiePwd = dialogView.findViewById(R.id.tie_alert_dialog_lock_pwd_enter_pwd);
        TextInputLayout tilPwd = dialogView.findViewById(R.id.til_alert_dialog_lock_pwd_enter_pwd);

        TextView tvBanner = dialogView.findViewById(R.id.tv_alert_dialog_lock_pwd_banner_enter_pwd);
        tvBanner.setVisibility(View.VISIBLE);

        TextInputLayout tilSelectFolder = dialogView.findViewById(R.id.til_alert_dialog_lock_pwd_select_folder);
        tilSelectFolder.setVisibility(View.GONE);


        Button btnCancel = dialogView.findViewById(R.id.btn_alert_dialog_lock_pwd_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                alertDialog.dismiss();

            }
        });


        Button btnSubmit = dialogView.findViewById(R.id.btn_alert_dialog_lock_pwd_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tilPwd.setError(null);
                tilPwd.setErrorEnabled(false);

                String text = tiePwd.getText().toString().trim();
                if (text.length() == 0) {


                    tilPwd.setError(getString(R.string.please_enter_pwd));

                } else {

                    tilPwd.setError(null);
                    tilPwd.setErrorEnabled(false);


                    String userPwd = Util.sha1Encypt(tiePwd.getText().toString().trim());
                    Log.e("sha1Encypt", userPwd);
                    if (userPwd.equals(oldPwd)) {

                        tilPwd.setError(null);
                        tilPwd.setErrorEnabled(false);

                        alertDialog.dismiss();


                        //  new RetrivePDFstream().execute(PDF_Url + filepath);
                        getFtpFilePath(docid, MainActivity.userid);

                        //Storing slid folder lock
                        sessionManager.storeUnlocked(Initializer.globalDynamicSlid);


                        Log.e("saved_slids", "2-->" + sessionManager.getStoredUnlockedSlids());

                        // Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();


                    } else {

                        tilPwd.setError(null);
                        tilPwd.setErrorEnabled(false);
                        tilPwd.setError(getString(R.string.incorrect_pwd));


                        // Toast.makeText(context, "Not ok", Toast.LENGTH_SHORT).show();

                    }

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


//    //Download file from server
//    public class DownloadFile extends AsyncTask<String, Integer, String> {
//
//        private Context context;
//        private PowerManager.WakeLock mWakeLock;
//
//        NotificationManager mNotifyManager;
//        NotificationCompat.Builder mBuilder;
//
//        public DownloadFile(Context context) {
//            this.context = context;
//        }
//        @Override
//        protected String doInBackground(String... sUrl) {
//            InputStream input = null;
//            OutputStream output = null;
//            HttpURLConnection connection = null;
//            try {
//                URL url = new URL(sUrl[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                // expect HTTP 200 OK, so we don't mistakenly save error report
//                // instead of the file
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return "Server returned HTTP " + connection.getResponseCode()
//                            + " " + connection.getResponseMessage();
//                }
//
//                // this will be useful to display download percentage
//                // might be -1: server did not report the length
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                input = connection.getInputStream();
//
//                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeFile Downloads");
//
//                if(!myDirectory.exists()) {
//                    myDirectory.mkdirs();
//
//                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+pdfName+".pdf");
//                }
//                else{
//                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+pdfName+".pdf");
//                }
//
//
//                byte data[] = new byte[4096];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    // allow canceling with back button
//                    if (isCancelled()) {
//                        input.close();
//                        return null;
//                    }
//                    total += count;
//                    // publishing the progress....
//                    if (fileLength > 0) // only if total length is known
//                        publishProgress((int) (total * 100 / fileLength));
//                    output.write(data, 0, count);
//                }
//            } catch (Exception e) {
//                return e.toString();
//            } finally {
//                try {
//                    if (output != null)
//                        output.close();
//                    if (input != null)
//                        input.close();
//                } catch (IOException ignored) {
//                }
//
//                if (connection != null)
//                    connection.disconnect();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // take CPU lock to prevent CPU from going off if the user
//            // presses the power button during download
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                    getClass().getName());
//            mWakeLock.acquire();
//
//          /*  mNotifyManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mBuilder = new NotificationCompat.Builder(context);
//
//            mBuilder.setContentTitle("File Download")
//                    .setContentText("Download in progress")
//                    .setColor(getResources().getColor(R.color.colorPrimary))
//                    .setSmallIcon(R.drawable.ic_file_download_white_24dp)
//                     ;*/
//
//
//            //Toast.makeText(context, "Downloading the file... The download progress is on notification bar.", Toast.LENGTH_LONG).show();
//
//             mProgressDialog.show();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            // if we get here, length is known, now set indeterminate to false
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(progress[0]);
//
//          /*  mBuilder.setProgress(100, progress[0], false);
//            // Displays the progress bar on notification
//            mNotifyManager.notify(0, mBuilder.build());*/
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            mWakeLock.release();
//            mProgressDialog.dismiss();
//            if (result != null)
//                Toast.makeText(context,"Download error", Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(context,"File downloaded Succesfully", Toast.LENGTH_SHORT).show();
//           /* mBuilder.setContentTitle("File Download Succesfully")
//                     .setContentText("")
//                    .setColor(getResources().getColor(R.color.colorPrimary))
//                    .setSmallIcon(R.drawable.ic_file_download_white_24dp);
//
//             mNotifyManager.notify(0, mBuilder.build());
//*/
//
//        }
//    }
//
//    public static class DownloadService extends IntentService {
//        public static final int UPDATE_PROGRESS = 8344;
//
//
//        public DownloadService(){
//            super("DownloadService");
//        }
//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            String urlToDownload = intent.getStringExtra("url");
//            String pdfName = intent.getStringExtra("pdfName");
//            ResultReceiver receiver = intent.getParcelableExtra("receiver");
//            try {
//                OutputStream output;
//
//                URL url = new URL(urlToDownload);
//                URLConnection connection = url.openConnection();
//                connection.connect();
//
//                // this will be useful so that you can show a typical 0-100% progress bar
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                InputStream input = new BufferedInputStream(connection.getInputStream());
//
//                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeFile Downloads");
//
//                if(!myDirectory.exists()) {
//                    myDirectory.mkdirs();
//
//                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+pdfName+".pdf");
//                }
//                else{
//                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+pdfName+".pdf");
//                }
//
//
//                /*OutputStream output = new FileOutputStream("/sdcard/BarcodeScanner-debug.apk");*/
//
//                byte data[] = new byte[1024];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    Bundle resultData = new Bundle();
//                    resultData.putInt("progress" ,(int) (total * 100 / fileLength));
//                    receiver.send(UPDATE_PROGRESS, resultData);
//                    output.write(data, 0, count);
//                }
//
//                output.flush();
//                output.close();
//                input.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Bundle resultData = new Bundle();
//            resultData.putInt("progress" ,100);
//            receiver.send(UPDATE_PROGRESS, resultData);
//        }
//    }
//
//    private  class DownloadReceiver extends ResultReceiver{
//
//        NotificationManager mNotifyManager;
//        NotificationCompat.Builder mBuilder;
//
//        public DownloadReceiver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//            super.onReceiveResult(resultCode, resultData);
//            if (resultCode == DownloadService.UPDATE_PROGRESS) {
//                int progress = resultData.getInt("progress");
//
//
//                mNotifyManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                mBuilder = new NotificationCompat.Builder(PdfViewerActivity.this);
//
//                mBuilder.setContentTitle("File Download")
//                        .setContentText("Download in progress")
//                        .setColor(getResources().getColor(R.color.colorPrimary))
//                        .setSmallIcon(android.R.drawable.stat_sys_download)
//                         .setProgress(100,progress,false)
//                .setAutoCancel(true)
//
//                ;
//                mNotifyManager.notify(0, mBuilder.build());
//
//                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                 wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                        getClass().getName());
//                wakeLock.acquire();
//
//                //mProgressDialog.setProgress(progress);
//                if (progress == 100) {
//                    Log.e("wakelock","working 1");
//                    wakeLock.release();
//
//                    Toast.makeText(PdfViewerActivity.this, "File Download Successfully", Toast.LENGTH_SHORT).show();
//                     mNotifyManager.cancel(0);
//            /*        mBuilder.setContentTitle("File Download Successfully")
//                            .setContentText("")
//                            .setColor(getResources().getColor(R.color.colorPrimary))
//                            .setSmallIcon(R.drawable.ic_file_download_white_24dp)
//                            //.setProgress(0,0,false)
//                    ;
//                    mNotifyManager.notify(0, mBuilder.build());*/
//
//                    Log.e("wakelock","working 2");
//
//                    //mProgressDialog.dismiss();
//
//
//                }
//            }
//        }
//    }


}
