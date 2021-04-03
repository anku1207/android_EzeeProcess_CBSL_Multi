package in.cbslgroup.ezeepeafinal.ui.activity.viewer;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class ImageViewerActivity extends AppCompatActivity {

    // ImageView ivImageViewer;
    private static final String IMAGE_URL = ApiUrl.BASE_URL;
    String filepath, filename, fileType, fileSize, fileDate, docid, ftpFilePath,fromWorkflow;

    TextView tvName, tvSize, tvDate;

    LinearLayout llPanel, llNoFileFound;
    ProgressBar progressBar;

    ProgressDialog mProgressDialog;

    Toolbar toolbar;

    Glide glide;

    DownloadManager downloadManager;
    PhotoView photoView;

    AlertDialog alertDialog;

    SessionManager sessionManager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        delFileFromTempPath(ftpFilePath);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);


        initLocale();

        sessionManager = new SessionManager(this);


        Toolbar toolbar = findViewById(R.id.toolbar_image_viewer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                // delFileFromTempPath(ftpFilePath);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        // ivImageViewer = findViewById(R.id.iv_image_viewer);
        photoView = findViewById(R.id.iv_image_viewer);

        Intent intent = getIntent();
        // filepath = intent.getStringExtra("filepath");
        //filepath="images/1501762341_sample-pan-card-front.jpg";
        filename = intent.getStringExtra("name");
        filepath = intent.getStringExtra("path");
        fileType = intent.getStringExtra("type");
        fileDate = intent.getStringExtra("date");
        fileSize = intent.getStringExtra("size");
        docid = intent.getStringExtra("docid");

        Log.e("filepath image ", filepath);

        tvName = findViewById(R.id.tv_imageviewer_name);
        tvDate = findViewById(R.id.tv_imageviewer_date);
        tvSize = findViewById(R.id.tv_imageviewer_size);

        tvName.setText(filename);
        tvSize.setText(fileSize);
        tvDate.setText(fileDate);

        progressBar = findViewById(R.id.progressBar_Glide);
        llPanel = findViewById(R.id.ll_image_viewer_panel);
        llNoFileFound = findViewById(R.id.ll_image_viewer_no_file_found);

        progressBar.setVisibility(View.VISIBLE);
        //progressBar.setLayoutParams(new LinearLayout.LayoutParams());

        // getFtpFilePath(docid, MainActivity.userid);

        if (intent.getStringExtra("workflow")!=null) {

            fromWorkflow = intent.getStringExtra("workflow");

            if(fromWorkflow.equals("true")){

                getFtpFilePath(docid, sessionManager.getUserId());//means workflow or attachment

            }


        }


      else  if (Initializer.globalDynamicSlid!=null && !Initializer.globalDynamicSlid.contains("_")//means workflow or attachment
        ) {

            checkFileLocked(Initializer.globalDynamicSlid);

        } else {

            //  new RetrivePDFstream().execute(PDF_Url + filepath);
            getFtpFilePath(docid, MainActivity.userid);
        }

       // checkFileLocked(Initializer.globalDynamicSlid);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.imageviewer_menu, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_image_viewer_download) {

            mProgressDialog = new ProgressDialog(ImageViewerActivity.this);
            mProgressDialog.setMessage(getString(R.string.file_is_downloading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);


            DownloadFile(IMAGE_URL + ftpFilePath);


            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    // Do Something
                    mProgressDialog.dismiss();

                }
            };

            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


            return true;

        }

        return super.onOptionsItemSelected(item);
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

                        Log.e("glide_path", IMAGE_URL + ftpFilePath);

                        if (fileType.equals("gif")) {

                            Glide.with(ImageViewerActivity.this)
                                    .load(IMAGE_URL + ftpFilePath)
                                    .thumbnail(0.5f).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                    progressBar.setVisibility(View.GONE);
                                    llPanel.setVisibility(View.GONE);
                                    llNoFileFound.setVisibility(View.VISIBLE);


//
//                                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
//
//                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
//
//                                            }


                                    Log.e("Glide exception ", String.valueOf(e));
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    llNoFileFound.setVisibility(View.GONE);
                                    llPanel.setVisibility(View.VISIBLE);

//
//                                            if(toolbar.getMenu()!=null){
//
//                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
//
//                                            }


                                    return false;
                                }
                            }).into(photoView);


                        } else if (fileType.equalsIgnoreCase("bmp")) {

                            Glide.with(ImageViewerActivity.this)
                                    .load(IMAGE_URL + ftpFilePath)
                                    .thumbnail(0.5f).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                    progressBar.setVisibility(View.GONE);
                                    llPanel.setVisibility(View.GONE);
                                    llNoFileFound.setVisibility(View.VISIBLE);


//
//                                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
//
//                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
//
//                                            }


                                    Log.e("Glide exception ", String.valueOf(e));
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    llNoFileFound.setVisibility(View.GONE);
                                    llPanel.setVisibility(View.VISIBLE);

//
//                                            if(toolbar.getMenu()!=null){
//
//                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
//
//                                            }


                                    return false;
                                }
                            }).into(photoView);


//                            Glide.with(ImageViewerActivity.this)
//                                    .load(IMAGE_URL + ftpFilePath)
//                                    .asBitmap()
//                                    .thumbnail(0.5f)
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .listener(new RequestListener<String, Bitmap>() {
//                                        @Override
//                                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                                            progressBar.setVisibility(View.GONE);
//                                            llPanel.setVisibility(View.GONE);
//                                            llNoFileFound.setVisibility(View.VISIBLE);
//
////                                            if(toolbar.getMenu()!=null){
////
////                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
////
////                                            }
//
//
//                                            Log.e("Glide exception ", String.valueOf(e));
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                            progressBar.setVisibility(View.GONE);
//                                            llNoFileFound.setVisibility(View.GONE);
//                                            llPanel.setVisibility(View.VISIBLE);
////
////
////                                            if(toolbar.getMenu()!=null){
////
////                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
////
////                                            }
//
//
//
//
//                                            return false;
//                                        }
//                                    })
//                                    .into(photoView)
//                            ;


                        } else {


                            Glide.with(ImageViewerActivity.this)
                                    .load(IMAGE_URL + ftpFilePath)
                                    .thumbnail(0.5f).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                    progressBar.setVisibility(View.GONE);
                                    llPanel.setVisibility(View.GONE);
                                    llNoFileFound.setVisibility(View.VISIBLE);


//
//                                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
//
//                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
//
//                                            }


                                    Log.e("Glide exception ", String.valueOf(e));
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    llNoFileFound.setVisibility(View.GONE);
                                    llPanel.setVisibility(View.VISIBLE);

//
//                                            if(toolbar.getMenu()!=null){
//
//                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
//
//                                            }


                                    return false;
                                }
                            }).into(photoView);


//                            Glide.with(ImageViewerActivity.this)
//                                    .load(IMAGE_URL + ftpFilePath)
//                                    .thumbnail(0.5f)
//                                    .crossFade()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .listener(new RequestListener<String, GlideDrawable>() {
//                                        @Override
//                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//
//                                            progressBar.setVisibility(View.GONE);
//                                            llPanel.setVisibility(View.GONE);
//                                            llNoFileFound.setVisibility(View.VISIBLE);
//
////                                            if(toolbar.getMenu()!=null){
////
////                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
////
////                                            }
//
//                                            Log.e("Glide exception ", String.valueOf(e));
//                                            return false;
//
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                            progressBar.setVisibility(View.GONE);
//                                            llNoFileFound.setVisibility(View.GONE);
//                                            llPanel.setVisibility(View.VISIBLE);
//
////                                            if(toolbar.getMenu()!=null){
////
////                                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
////
////                                            }
//
//                                            return false;
//                                        }
//                                    })
//                                    .into(photoView);

                        }


                    } else {

                        Toast.makeText(ImageViewerActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        finish();

                    }


                } catch (JSONException e) {
                    //e.printStackTrace();

                    Log.e("image_err", e.toString());
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
                return params;
            }

        };

        VolleySingelton.getInstance(ImageViewerActivity.this).addToRequestQueue(stringRequest);

    }

    //Downloading file
    public void DownloadFile(String url) {


        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


//        File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeProcess Downloads");
//
//        if (!myDirectory.exists()) {
//            myDirectory.mkdirs();
//        }

      //  String PATH = "/EzeeProcess Downloads/";
        String PATH = Environment.DIRECTORY_DOWNLOADS;
        request.setDestinationInExternalPublicDir(PATH, uri.getLastPathSegment());
        downloadManager.enqueue(request);


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

        VolleySingelton.getInstance(ImageViewerActivity.this).addToRequestQueue(stringRequest);

    }

    void checkFileLocked(String slid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOCK_FOLDER,
                response -> {

                    Log.e("pdf_res_lock", response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String isProtected = jsonObject.getString("is_protected");
                        String oldPwd = jsonObject.getString("password");

                        if (isProtected.equalsIgnoreCase("0") || sessionManager.isSlidPresent(Initializer.globalDynamicSlid)) {

                            //  new RetrivePDFstream().execute(PDF_Url + filepath);
                            getFtpFilePath(docid, sessionManager.getUserDetails().get(SessionManager.KEY_USERID));


                        } else {

                            showFolderLockDialog(this, oldPwd);

                        }


                    }
                    catch (JSONException e) {

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


                    tilPwd.setError("Please enter password");

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

                        // Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();


                    } else {

                        tilPwd.setError(null);
                        tilPwd.setErrorEnabled(false);
                        tilPwd.setError("Incorrect Password");

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

}
