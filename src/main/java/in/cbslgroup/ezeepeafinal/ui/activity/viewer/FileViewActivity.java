package in.cbslgroup.ezeepeafinal.ui.activity.viewer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.adapters.list.FileViewListAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.SharedFileStatusAdapter;
import in.cbslgroup.ezeepeafinal.chip.TokenFilterAdapter;
import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.chip.UsernameCompletionView;
import in.cbslgroup.ezeepeafinal.interfaces.onFileClick;


import in.cbslgroup.ezeepeafinal.model.FileViewList;
import in.cbslgroup.ezeepeafinal.model.SharedFileStatus;
import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.CheckInActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.MoveCopyStorageActivity;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;


public class FileViewActivity extends AppCompatActivity implements View.OnLongClickListener {

    public static String mode = "";
    public static String slidDynamic, movefile, copyfile, checkinCheckout, fileVersion, fileDelete, shareFile, viewMetadata, pdfView, imageView, delversion, lockfolder, isProtected;
    public static RecyclerView rvFileView;
    public static ProgressBar progressBar;

    public static String ip;
    public static String doclistArray;
    public static Boolean in_action_mode = false;

    FileViewListAdapter fileViewListAdapter;

    String slid, foldername;
    boolean onlongclick = false;
    AlertDialog alertDialog;
    String userid;

    List<FileViewList> fileViewList = new ArrayList<>();
    ArrayList<User> usernamelist = new ArrayList<>();
    ArrayList<String> SelectionList = new ArrayList<>();
    ArrayList<String> docidList = new ArrayList<>();
    ArrayList<String> useridList = new ArrayList<>();
    ArrayList<String> shareuseridsList = new ArrayList<>();
    ArrayList<SharedFileStatus> sharedFileStatusList = new ArrayList<>();
    List<String> sNoList = new ArrayList<>();

    Toolbar toolbar;

    RecyclerView rvSharedFileStatus;
    LinearLayoutManager linearLayoutManagerFileView;

    TextView tvNoOfItem;

    RelativeLayout rlRecyclerView;

    LinearLayout llFileViewMain;
    LinearLayout llnofilefound;

    ProgressDialog pdMail;
    //admin or not
    Boolean isScrolling = false;
    Boolean checkAdmin = false;

    int counter = 0;
    int page_number = 1;
    int totalfiles = 0;
    int pageCount = 0;
    int itemStart = 1, itemLast = 25;

    ProgressBar pgRv;
    TextView tvHeader;
    LinearLayout llHeader;

    private UsernameCompletionView autoCompleteTextView;
    private TokenFilterAdapter filterAdapter;

    SessionManager sessionManager;

    private void initLocale()
    {
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
        setContentView(R.layout.activity_file_view);

        initLocale();



        try {

            initSessionManager();
            initToolbar();
            getIntentData();
            initViews();
            initRecylerView();

            //checked item list
            SelectionList.clear();

            getUserRole(userid);
            groupMemberList(userid);
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private void initViews() {

        llnofilefound = findViewById(R.id.ll_fileview_no_file_found);
        llFileViewMain = findViewById(R.id.ll_fileview_main);
        progressBar = findViewById(R.id.progressBar_storage_fileviewlist);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        pdMail = new ProgressDialog(this);
        tvHeader = findViewById(R.id.tv_fileview_totalfiles_header);
        llHeader = findViewById(R.id.ll_fileview_header);
        //String slidnew = slid.substring(0,slid.indexOf("&&"));
        tvNoOfItem = findViewById(R.id.fileview_nooffile);
        rlRecyclerView = findViewById(R.id.rl_fileview_rv_tvnooffile);
        pgRv = findViewById(R.id.pg_rv_dms_fileview);
    }

    private void initRecylerView() {
        rvFileView = findViewById(R.id.rv_dms_fileview);
        linearLayoutManagerFileView = new LinearLayoutManager(this);
        rvFileView.setLayoutManager(linearLayoutManagerFileView);
        rvFileView.setHasFixedSize(true);


        //rvFileView.setItemViewCacheSize(-1);
        rvFileView.setItemAnimator(new DefaultItemAnimator());
        rvFileView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    isScrolling = true;

                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int currentItems = linearLayoutManagerFileView.getChildCount();
                int totalItems = fileViewListAdapter.getItemCount();
                int scrolledoutitems = linearLayoutManagerFileView.findFirstVisibleItemPosition();

                Log.e("currentitems", String.valueOf(currentItems));
                Log.e("totalItems", String.valueOf(totalItems));
                Log.e("scrolledoutitems", String.valueOf(scrolledoutitems));

                Log.e("isScrolling", String.valueOf(isScrolling));
                Log.e("logic", String.valueOf(isScrolling && (currentItems + totalItems == scrolledoutitems)));

                Log.e("totalfiles", String.valueOf(totalfiles));

                if (!in_action_mode) {


                    if (isScrolling && (currentItems + scrolledoutitems == totalItems)) {

                        Log.e("fileview.size()", String.valueOf(fileViewList.size()));
                        if (fileViewList.size() != totalfiles) {

                            isScrolling = false;

                            fileViewList.add(null);
                            fileViewListAdapter.notifyItemInserted(fileViewList.size() - 1);
                            fileViewList.remove(fileViewList.size() - 1);
                            fileViewListAdapter.notifyItemRemoved(fileViewList.size());

                            page_number = page_number + 1;

                            Log.e("pagenumber", String.valueOf(page_number));

                            loadMore(slidDynamic, String.valueOf(page_number));

                        } else {

                            Log.e("end", "reached the end");

                        }
                    }
                }

            }
        });
    }

    private void getIntentData() {

        slid = getIntent().getStringExtra("slid");
        foldername = getIntent().getStringExtra("foldername");
        mode = getIntent().getStringExtra("mode");
        slidDynamic = slid;
        Initializer.globalDynamicSlid = slid;
        Log.e("slid in fileview", slid);


    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_storage_management_fileview);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });
    }

    private void initSessionManager() {
        sessionManager = new SessionManager(this);
        //user role
        userid = new SessionManager(this).getUserDetails().get(SessionManager.KEY_USERID);
        ip = sessionManager.getIP();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 420) {

            String foldername = data.getStringExtra("fname");
            toolbar.setSubtitle(foldername);
            getUserRole(MainActivity.userid);


        } else if (requestCode == 1003) {

            getUserRole(MainActivity.userid);

        } else if (requestCode == 1004) {

            getUserRole(MainActivity.userid);

        } else if (requestCode == 1001) {

            getUserRole(MainActivity.userid);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fileview_share) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            if (counter != 0) {


                showMultiShareDialog();

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < FileViewListAdapter.checkedlist.size(); i++) {

                    sb.append(FileViewListAdapter.checkedlist.get(i)).append(",");

                }

                // Toast.makeText(FileViewActivity.this, sb, Toast.LENGTH_LONG).show();

                SelectionList.clear();
                // docidList.clear();


            } else {

                Toast.makeText(FileViewActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }


            return true;
        } else if (id == R.id.action_fileview_delete) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            if (counter != 0) {


                // docidList.clear();

                showMultiDelDialog();

                for (String selection : SelectionList) {

                    Log.e("selection", selection);

                }


                SelectionList.clear();

                //docidList.clear();

            } else {

                Toast.makeText(FileViewActivity.this, R.string.plese_select_atleast_one_file, Toast.LENGTH_LONG).show();

            }


            return true;
        } else if (id == R.id.action_fileview_move) {

            if (counter != 0) {


                // docidList.clear();

                showMultiMoveDialog();


                SelectionList.clear();

                //docidList.clear();

            } else {

                Toast.makeText(FileViewActivity.this, R.string.plese_select_atleast_one_file, Toast.LENGTH_LONG).show();

            }
            return true;
        } else if (id == R.id.action_fileview_copy) {

            if (counter != 0) {

                // docidList.clear();
                showMultiCopyDialog();
                SelectionList.clear();
                //docidList.clear();

            } else {

                Toast.makeText(FileViewActivity.this, R.string.plese_select_atleast_one_file, Toast.LENGTH_LONG).show();

            }
            return true;
        } else if (id == R.id.action_fileview_file_mail) {

            if (docidList.size() != 0) {

                showMultiSendMailDialog();

            } else {

                Toast.makeText(FileViewActivity.this, R.string.please_select_atleast_one_document, Toast.LENGTH_SHORT).show();

            }


        }

        return super.onOptionsItemSelected(item);
    }

    public void getFileListData(final String slid, final String page) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("List response", response);

                try {


                    JSONObject jsonObject2 = new JSONObject(response);
                    totalfiles = Integer.parseInt(jsonObject2.getString("totalfiles"));
                    pageCount = Integer.parseInt(jsonObject2.getString("pageCount"));
                    isProtected = jsonObject2.getString("is_protected");

                    Log.e("pageCount", String.valueOf(pageCount));

                    JSONArray jsonArray = jsonObject2.getJSONArray("list");

                    if (jsonArray.length() == 0) {

                        Log.e("deleted", "ok");

                        progressBar.setVisibility(View.GONE);
                        llFileViewMain.setVisibility(View.GONE);
                        llnofilefound.setVisibility(View.VISIBLE);


                    } else {


                        String metadata;
                        StringBuilder sb = new StringBuilder();
                        String meta = null;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            // JSONObject jsonObject = jsonArray.getJSONObject(i);


                            //  Toast.makeText(FileViewActivity.this, "ok", Toast.LENGTH_SHORT).show();

                            String fileName = jsonArray.getJSONObject(i).getString("old_doc_name");
                            Log.e("old_doc_name", fileName);
                            String noOfPages = jsonArray.getJSONObject(i).getString("noofpages");
                            Log.e("noofpages", noOfPages);

                            String fileSize = jsonArray.getJSONObject(i).getString("doc_size");
                            Log.e("fisize_raw", fileSize);

                            String serialNum = jsonArray.getJSONObject(i).getString("s.no");

                            Log.e("doc_size", fileSize);

                            String filetype = jsonArray.getJSONObject(i).getString("doc_extn");
                            Log.e("doc_extn", filetype);

                            String docid = jsonArray.getJSONObject(i).getString("doc_id");
                            Log.e("doc_id", docid);


                            //String versioniconShowHide = jsonArray.getJSONObject(i).getString("version_status");
                            String versioniconShowHide = "1";
                            Log.e("version_status", versioniconShowHide);

                            String checkin_out = jsonArray.getJSONObject(i).getString("checkin_status");
                            Log.e("checkin_status", checkin_out);

        /*                    JSONArray jsonArray1 = jsonObject.getJSONArray("metadata");

                            if (jsonArray1.length() == 0) {


                                meta = "No Metadata Found";

                            } else {

                                //clearing the stringbuilder string which is dynamically building
                                sb.setLength(0);


                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    metadata = jsonArray1.get(j).toString();//{"AadharNumber":"1003"}
                                    String a = metadata.substring(metadata.indexOf("{") + 1, metadata.indexOf("}"));//"AadharNumber":"1003"
                                    String b = a.substring(0, a.indexOf(":"));//"AadharNumber"
                                    String label = b.substring(1, b.length() - 1);//label = AadharNumber
                                    String c = a.substring(a.indexOf(":") + 1, a.length());//"1003"

                                    if (c.equals("null")) {

                                        c = " ";
                                        sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");
                                    } else {

                                        c = c.substring(1, c.length() - 1);
                                        sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");

                                    }

                                }

                                meta = sb.toString();

                                Log.e("meta in fileview", meta);

                            }

                            String uploadedBy = null;

                            JSONArray jsonArray2 = jsonObject.getJSONArray("name");
                            Log.e("array name length", String.valueOf(jsonArray2.length()));

                            if (jsonArray2.length() == 0) {

                                uploadedBy = "-";
                            } else {

                                JSONObject jObjName = jsonArray2.getJSONObject(0);
                                uploadedBy = jObjName.getString("fullname");
                            }


                            Log.e("uploadedby", uploadedBy);*/

                            Log.e("checkin", checkin_out);
                            //Log.e("version",versioniconShowHide);

                            Double fileSizeDouble = Double.parseDouble(fileSize) / 1000000;
                            // Double fileSizeDouble = Double.valueOf(fileSize);
                            Log.e("File size ", String.valueOf(fileSizeDouble));


                            //String fileSizeInMb = String.valueOf(Math.round(fileSizeDouble * 100D) / 100D);
                            String fileSizeInMb = String.valueOf(Math.round(fileSizeDouble * 100D) / 100D);
                            Log.e("File size in mb ", fileSizeInMb);

                            //  String uploadedBy = jsonArray.getJSONObject(i).getString("uploaded_by");
                            String uploadedDate = jsonArray.getJSONObject(i).getString("dateposted");
                            String fullpath = jsonArray.getJSONObject(i).getString("doc_path");

                            String uploadedBy = jsonArray.getJSONObject(i).getString("name");
                            if (uploadedBy.length() == 0) {

                                uploadedBy = "-";

                            }

                            Log.e("uploadedby", uploadedBy);

                            String docname = jsonArray.getJSONObject(i).getString("doc_name");
                            Log.e("doc_name", docname);

                            fileViewList.add(new FileViewList(fileName, fileSizeInMb + " " + "mb", noOfPages, uploadedBy, uploadedDate, fullpath, filetype, docid, versioniconShowHide, checkin_out, false, docname));

                            sNoList.add(serialNum);


                        }


                        fileViewListAdapter = new FileViewListAdapter(fileViewList, FileViewActivity.this, new onFileClick() {
                            @Override
                            public void onFileClick(View v, int position) {


                            }

                            @Override
                            public void onCheckInBtnClick(View v, int position, final String docid, String docname) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                tvHeading.setText(R.string.check_in);
                                tvHeading.setBackgroundColor(getResources().getColor(R.color.green_dark));


                                TextView tvSubheading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                tvSubheading.setText(getString(R.string.are_you_sure_want_to_check_in_doc) + " " + docname + " ?");

                                Button btn_cancel = dialogView.findViewById(R.id.btn_no_pDel_popup);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();
                                    }
                                });

                                Button btn_ok = dialogView.findViewById(R.id.btn_yes_pDel_popup);
                                btn_ok.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(FileViewActivity.this, CheckInActivity.class);


                                        if (mode.equalsIgnoreCase("share_folder")) {

                                            intent.putExtra("docid", docid);
                                            intent.putExtra("slid", slid);
                                            intent.putExtra("fname", foldername);
                                            intent.putExtra("mode", "share_folder");
                                            startActivityForResult(intent, 1001);

                                        } else {

                                            intent.putExtra("docid", docid);
                                            intent.putExtra("slid", DmsActivity.dynamicFileSlid);
                                            intent.putExtra("fname", DmsActivity.foldernameDyanamic);
                                            intent.putExtra("mode", "fileviewActivity");
                                            startActivityForResult(intent, 420);
                                            // finish();
                                        }


                                        // checkIn(docid);


                                        alertDialog.dismiss();
                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.show();


                            }

                            @Override
                            public void onDelVersionBtnClick(View v, int position, final String docid) {


                            }

                            @Override
                            public void onCheckOutBtnClick(View v, int position, final String docid, String docname) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                tvHeading.setText(R.string.check_out);
                                //tvHeading.setBackgroundColor(Color.GREEN);


                                TextView tvSubheading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                tvSubheading.setText(getString(R.string.are_you_sure_want_to_check_out) + " " + docname + " ?");


                                Button btn_cancel = dialogView.findViewById(R.id.btn_no_pDel_popup);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        alertDialog.dismiss();
                                    }
                                });

                                Button btn_ok = dialogView.findViewById(R.id.btn_yes_pDel_popup);
                                btn_ok.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        checkOut(docid);


                                        alertDialog.dismiss();
                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.show();

                            }

                            @Override
                            public void onDeleteBtnClick(View v, final int position, final String docid, final String fullname, final String filename) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);

                                Button delPer = dialogView.findViewById(R.id.btn_per_delete_popup);

                                if (checkAdmin) {

                                    delPer.setVisibility(View.VISIBLE);
                                } else {

                                    delPer.setVisibility(View.GONE);

                                }


                                delPer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        // String fullname = String.valueOf(holder.tvUploadedBy.getText());
                                        String roleid = DmsActivity.roleid;
                                        String ip = DmsActivity.ip;
                                        String userid = DmsActivity.userid;
                                        //String filename = String.valueOf(holder.tvFileName.getText());
                                        // String docid = String.valueOf(holder.tvDocid.getText());

                                        //delete doc volley request

                                        deleteDoc(docid, fullname, "Yes", roleid, ip, userid, filename, position);

                                        alertDialog.dismiss();


                                    }
                                });

                                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_delete_popup);
                                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        // String fullname = String.valueOf(holder.tvUploadedBy.getText());
                                        String roleid = DmsActivity.roleid;
                                        String ip = DmsActivity.ip;
                                        String userid = DmsActivity.userid;
                                        //String filename = String.valueOf(holder.tvFileName.getText());
                                        // String docid = String.valueOf(holder.tvDocid.getText());

                                        //delete doc volley request

                                        deleteDoc(docid, fullname, "No", roleid, ip, userid, filename, position);

                                        alertDialog.dismiss();

                                    }
                                });

                                Button btn_cancel = dialogView.findViewById(R.id.btn_close_delete_popup);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        alertDialog.dismiss();
                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.show();
                            }
                        });


                        Log.e("sno", "starting : " + itemStart + "\n" + "last : " + itemLast);
                        //in_action_mode = false;
                        // tvNoOfItem.setText( sNoList.get(0)+" to "+sNoList.get(sNoList.size()-1)+" out of total files : "+totalfiles);

                        tvNoOfItem.setText(totalfiles + " " + getString(R.string.files_found));
                        //subtitle.append("Folder Name : "+foldername).append("\n");
                        //  subtitle.append("Total Files : "+totalfiles);

                        // llHeader.setVisibility(View.VISIBLE);
                        // tvHeader.setText(subtitle);

                        toolbar.setSubtitle(foldername);
                        rvFileView.setAdapter(fileViewListAdapter);
                        fileViewListAdapter.notifyDataSetChanged();
                        //rvFileView.setItemViewCacheSize(-1);
                        progressBar.setVisibility(View.GONE);
                        llnofilefound.setVisibility(View.GONE);
                        llFileViewMain.setVisibility(View.VISIBLE);

                        //Toast.makeText(FileViewActivity.this, "ok2", Toast.LENGTH_SHORT).show();


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

                params.put("id", slid);
                params.put("page", page);
                params.put("userId", new SessionManager(FileViewActivity.this).getUserDetails().get(SessionManager.KEY_USERID));
                params.put("ln", sessionManager.getLanguage());
                return params;
            }
        };


       /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    public void loadMore(final String slid, final String page) {

        pgRv.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Load MOre response", response);

                try {


                    JSONObject jsonObject2 = new JSONObject(response);
                    totalfiles = Integer.parseInt(jsonObject2.getString("totalfiles"));
                    pageCount = Integer.parseInt(jsonObject2.getString("pageCount"));
                    isProtected = jsonObject2.getString("is_protected");


                    Log.e("pageCount", String.valueOf(pageCount));


                    JSONArray jsonArray = jsonObject2.getJSONArray("list");

                    if (jsonArray.length() == 0) {


                    } else {

                        String metadata;
                        StringBuilder sb = new StringBuilder();
                        String meta = null;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fileName = jsonArray.getJSONObject(i).getString("old_doc_name");
                            Log.e("old_doc_name", fileName);
                            String noOfPages = jsonArray.getJSONObject(i).getString("noofpages");
                            Log.e("noofpages", noOfPages);

                            String fileSize = jsonArray.getJSONObject(i).getString("doc_size");

                            Log.e("doc_size", fileSize);

                            String filetype = jsonArray.getJSONObject(i).getString("doc_extn");
                            Log.e("doc_extn", filetype);

                            String docid = jsonArray.getJSONObject(i).getString("doc_id");
                            Log.e("doc_id", docid);


                            //String versioniconShowHide = jsonArray.getJSONObject(i).getString("version_status");
                            String versioniconShowHide = "1";
                            Log.e("version_status", versioniconShowHide);

                            String checkin_out = jsonArray.getJSONObject(i).getString("checkin_status");
                            Log.e("checkin_status", checkin_out);

                     /*       JSONArray jsonArray1 = jsonObject.getJSONArray("metadata");

                            if (jsonArray1.length() == 0) {


                                meta = "No Metadata Found";

                            } else {

                                //clearing the stringbuilder string which is dynamically building
                                sb.setLength(0);


                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    metadata = jsonArray1.get(j).toString();//{"AadharNumber":"1003"}
                                    String a = metadata.substring(metadata.indexOf("{") + 1, metadata.indexOf("}"));//"AadharNumber":"1003"
                                    String b = a.substring(0, a.indexOf(":"));//"AadharNumber"
                                    String label = b.substring(1, b.length() - 1);//label = AadharNumber
                                    String c = a.substring(a.indexOf(":") + 1, a.length());//"1003"

                                    if (c.equals("null")) {

                                        c = " ";
                                        sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");
                                    } else {

                                        c = c.substring(1, c.length() - 1);
                                        sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");

                                    }

                                }

                                meta = sb.toString();

                                Log.e("meta in fileview", meta);

                            }

                            String uploadedBy = null;

                            JSONArray jsonArray2 = jsonObject.getJSONArray("name");
                            Log.e("array name length", String.valueOf(jsonArray2.length()));

                            if (jsonArray2.length() == 0) {

                                uploadedBy = "-";
                            } else {

                                JSONObject jObjName = jsonArray2.getJSONObject(0);
                                uploadedBy = jObjName.getString("fullname");
                            }*/


                            //  String uploadedBy = jsonArray.getJSONObject(i).getString("uploaded_by");


                            Log.e("checkin", checkin_out);
                            //Log.e("version",versioniconShowHide);

                            Double fileSizeDouble = Double.parseDouble(fileSize) / 1000000;
                            Log.e("File size ", String.valueOf(fileSizeDouble));

                            String fileSizeInMb = String.valueOf(Math.round(fileSizeDouble * 100D) / 100D);
                            Log.e("File size in mb ", fileSizeInMb);

                            String uploadedDate = jsonArray.getJSONObject(i).getString("dateposted");
                            String fullpath = jsonArray.getJSONObject(i).getString("doc_path");

                            String uploadedBy = jsonArray.getJSONObject(i).getString("name");
                            if (uploadedBy.length() == 0) {

                                uploadedBy = "-";

                            }

                            Log.e("uploadedby", uploadedBy);

                            String docname = jsonArray.getJSONObject(i).getString("doc_name");
                            Log.e("doc_name", docname);

                            fileViewList.add(new FileViewList(fileName, fileSizeInMb + " " + "mb", noOfPages, uploadedBy, uploadedDate, fullpath, filetype, docid, versioniconShowHide, checkin_out, false, docname));


                        }


                    }

                    // rvFileView.setAdapter(fileViewListAdapter);
                    // in_action_mode = false;


                    //rvFileView.setItemViewCacheSize(totalfiles);


                    fileViewListAdapter.notifyDataSetChanged();
                    //rvFileView.invalidate();
                    //rvFileView.setItemViewCacheSize(-1);
                    pgRv.setVisibility(View.GONE);
                    tvNoOfItem.setText(totalfiles + " files found");


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

                params.put("id", slid);
                params.put("page", page);
                params.put("userId", MainActivity.userid);
                params.put("ln", sessionManager.getLanguage());
                return params;
            }
        };


        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    private void checkIn(final String docid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECKIN_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // [{"message":"Check Out successful","error":"false"}]

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String message = jsonArray.getJSONObject(0).getString("message");
                    String error = jsonArray.getJSONObject(0).getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        // getFileListData(slidDynamic, "1");

                        getUserRole(MainActivity.userid);

                        Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_SHORT).show();

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

                Map<String, String> params = new HashMap<>();
                params.put("checkin", docid);
                params.put("ln", sessionManager.getLanguage());

                return params;
            }
        };

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);

    }

    private void checkOut(final String docid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECKIN_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // [{"message":"Check Out successful","error":"false"}]

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String message = jsonArray.getJSONObject(0).getString("message");
                    String error = jsonArray.getJSONObject(0).getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        /*    getFileListData(slidDynamic, "1");
                         */


                        getUserRole(MainActivity.userid);


                        Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_SHORT).show();

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

                Map<String, String> params = new HashMap<>();
                params.put("checkout", docid);
                params.put("ln", sessionManager.getLanguage());
                return params;
            }
        };

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {


        if (in_action_mode) {

            counter = 0;
            toolbar.getMenu().clear();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle(R.string.storage_management);
            //llHeader.setVisibility(View.VISIBLE);
            toolbar.setSubtitle(foldername);
            in_action_mode = false;
            onlongclick = false;
            fileViewListAdapter.checkboxUnselectAll();
            fileViewListAdapter.notifyDataSetChanged();

            sessionManager.resetStoredSlid();


        } else {


            //Toast.makeText(this, "back working", Toast.LENGTH_SHORT).show() ;

            if (mode != null && mode.equalsIgnoreCase("share_folder")) {

                sessionManager.resetStoredSlid();
                finish();
            }


            Intent intent = getIntent();
            Log.e("fileviewonbackslid", slid);
            intent.putExtra("slid_dms", slid);
            setResult(RESULT_OK, intent);
            sessionManager.resetStoredSlid();
            finish();

            //super.onBackPressed();

        }

    }

    //Activating the multiselect mode
    @Override
    public boolean onLongClick(View v) {


        try {

            if (!onlongclick) {


                toolbar.setBackgroundColor(getResources().getColor(R.color.black_blur));

                toolbar.setTitle(R.string.zero_item_selected);
                toolbar.inflateMenu(R.menu.fileview_multiselect_menu);


                if (lockfolder.equals("1")) {

                    if (isProtected.equals("0")) {

                        toolbar.getMenu().findItem(R.id.action_fileview_move).setVisible(movefile.equalsIgnoreCase("1"));
                        toolbar.getMenu().findItem(R.id.action_fileview_copy).setVisible(copyfile.equalsIgnoreCase("1"));
                        toolbar.getMenu().findItem(R.id.action_fileview_share).setVisible(shareFile.equalsIgnoreCase("1"));

                    } else {

                        toolbar.getMenu().findItem(R.id.action_fileview_move).setVisible(false);
                        toolbar.getMenu().findItem(R.id.action_fileview_share).setVisible(false);
                        toolbar.getMenu().findItem(R.id.action_fileview_copy).setVisible(false);


                    }


                } else {

                    toolbar.getMenu().findItem(R.id.action_fileview_move).setVisible(true);
                    toolbar.getMenu().findItem(R.id.action_fileview_share).setVisible(true);
                    toolbar.getMenu().findItem(R.id.action_fileview_copy).setVisible(true);


                }

                toolbar.getMenu().findItem(R.id.action_fileview_delete).setVisible(fileDelete.equalsIgnoreCase("1"));


                toolbar.setSubtitle("");
                in_action_mode = true;

                fileViewListAdapter.notifyDataSetChanged();
                //llHeader.setVisibility(View.GONE);
                onlongclick = true;

            } else {

                Log.e("onclick", "false");

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return true;
    }

    public void prepareSelection(View v, int position, ArrayList<String> docidlist) {


        docidList = docidlist;


        if (((CheckBox) v).isChecked()) {

            SelectionList.add("" + position);
            counter++;
            updateCounter(counter);

        } else {

            SelectionList.remove("" + position);
            counter--;
            updateCounter(counter);
        }


    }

    // Updating the counter number of the selected item
    // How many items are selected in the multislect mode
    public void updateCounter(int counter) {

        if (counter == 0) {

            toolbar.setTitle("0 item selected");


        } else {

            toolbar.setTitle(counter + " item selected");
        }

    }

    public void groupMemberList(final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response membrs flview", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {


                        String names = jsonArray.getString(i);

                        String namesOnly = names.substring(0, names.indexOf("&&"));

                        if (namesOnly.isEmpty() || namesOnly.equals("")) {

                            Log.e("names", "null");


                        } else {

                            // spinnermemberfullwithidList.add(names);

                            // usernamelist.add(new User(names,R.drawable.ic_user));


                            String username = names.substring(0, names.indexOf("&&"));
                            usernamelist.add(new User(username, names, R.drawable.ic_user));


                            // usernamelistwithSlid.add(new User(username,names,R.drawable.ic_user));
                            //  usernamelist.add(new User(username,names,R.drawable.ic_user));

                            // String userid  = names.substring(names.indexOf("&&"),names.length());


                            // spinnermemberlist.add(username);

                        }

                    }


                    //Initializing and attaching adapter for AutocompleteTextView


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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);
                parameters.put("ln", sessionManager.getLanguage());


                return parameters;
            }


        };


        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    private void deleteDoc(final String docid, final String fullname, final String permission, final String roleid, final String ip, final String userid, final String filename, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DELETE_DOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response deldoc", response);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equals("true")) {

                        Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                        String slid = FileViewActivity.slidDynamic;


                    } else if (error.equals("false")) {


                        Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();
                        /*            getFileListData(slid, "1");*/
                        getUserRole(MainActivity.userid);

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
                params.put("docid", docid);
                params.put("fullname", fullname);
                params.put("ip", ip);
                params.put("permission", permission);
                params.put("roleid", roleid);
                params.put("filename", filename);
                params.put("ln", sessionManager.getLanguage());


                return params;
            }
        };

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    private void showMultiShareDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_share_files, null, false);

        autoCompleteTextView = dialogView.findViewById(R.id.autocomplete_textview);

        //Set the action to be taken when a Token is clicked
        autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.allowDuplicates(false);
        //autoCompleteTextView.setShowAlways(true);
        //autoCompleteTextView.showDropDown();
//        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                autoCompleteTextView.showDropDown();
//                autoCompleteTextView.deleteText();
//
//            }
//        });
//
//        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                autoCompleteTextView.showDropDown();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                autoCompleteTextView.showDropDown();
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                autoCompleteTextView.showDropDown();
//            }
//        });

        //usernamelist.clear();
        filterAdapter = new TokenFilterAdapter(FileViewActivity.this, R.layout.item_user, usernamelist) {
            @Override
            protected boolean keepObject(User obj, String mask) {

                mask = mask.toLowerCase();
                return obj.getUsername().toLowerCase().contains(mask);
            }
        };
        autoCompleteTextView.setAdapter(filterAdapter);


        Button btn_share = dialogView.findViewById(R.id.btn_share_popup);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<User> t = autoCompleteTextView.getObjects();
                if (t.size() != 0) {

                    StringBuilder sb = new StringBuilder();
                    for (String ids : docidList) {

                        sb.append(ids).append(",");
                        Log.e("ids", ids);

                    }

                    List<User> tokens = autoCompleteTextView.getObjects();
                    StringBuilder content = new StringBuilder();
                    for (int i = 0; i < tokens.size(); i++) {

                        String slid = tokens.get(i).getSlid();
                        content.append(slid.substring(slid.indexOf("&&") + 2)).append(" ");
                        shareuseridsList.add(slid.substring(slid.indexOf("&&") + 2));
                    }
                    //shareduserids = 1

                    useridList.add(userid);

                    JSONArray jsonArray = new JSONArray();
                    JSONArray jsonArray1 = new JSONArray();


                    for (int i = 0; i < docidList.size(); i++) {

                        jsonArray.put(docidList.get(i));

                    }

                    String jsonarraystringDocids = jsonArray.toString();

                    Log.e("doclist json array", jsonarraystringDocids);

                    for (int i = 0; i < shareuseridsList.size(); i++) {

                        jsonArray1.put(shareuseridsList.get(i));

                    }

                    String jsonarraystringUserids = jsonArray1.toString();

                    Log.e("useridlist json array", jsonarraystringUserids);


                    String username = MainActivity.username;

                    //   Toast.makeText(FileViewActivity.this,sb,Toast.LENGTH_LONG).show();


                    if (shareuseridsList.size() != 0) {

                        autoCompleteTextView.deleteText();

                        //posting request
                        ShareMultipeFiles(userid, jsonarraystringUserids, jsonarraystringDocids, ip, username);

                        //normalMode();

                        fileViewListAdapter.clearCheckedList();

                    } else {

                        Toast.makeText(FileViewActivity.this, R.string.select_one_person_to_share, Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(FileViewActivity.this, R.string.select_one_person_to_share, Toast.LENGTH_SHORT).show();
                }

            }
        });


        Button btn_close = dialogView.findViewById(R.id.btn_close_share_popup);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                normalMode();


//                if (in_action_mode) {
//
//                    counter = 0;
//                    toolbar.getMenu().clear();
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    toolbar.setTitle("Storage Management");
//                    toolbar.setSubtitle(foldername);
//                    in_action_mode = false;
//                    onlongclick = false;
//                    fileViewListAdapter.notifyDataSetChanged();
//
//                    //code here for close dialog button
//                    alertDialog.dismiss();
//
//                }

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }

    private void showMultiDelDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);


        TextView tvMessage = dialogView.findViewById(R.id.tv_alert_delete_message);
        tvMessage.setText(getString(R.string.are_you_sure_want_to_delete) + " " + counter + getString(R.string.filles) + " ?");

        Button delPer = dialogView.findViewById(R.id.btn_per_delete_popup);

        Button btn_cancel = dialogView.findViewById(R.id.btn_close_delete_popup);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });


        if (checkAdmin) {

            delPer.setVisibility(View.VISIBLE);
        } else {

            delPer.setVisibility(View.GONE);

        }

        delPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("delMultidocids", jsonArrayDocids.toString());
                Log.e("delMultidocids", DmsActivity.dynamicFileSlid);


                multiDel(userid, MainActivity.username, ip, jsonArrayDocids.toString(), "Yes", DmsActivity.dynamicFileSlid);

                // docidList.clear();

                normalMode();

                fileViewListAdapter.clearCheckedList();


                alertDialog.dismiss();


            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_delete_popup);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String fullname = String.valueOf(holder.tvUploadedBy.getText());
                String roleid = DmsActivity.roleid;
                String ip = MainActivity.ip;
                String userid = DmsActivity.userid;
                String username = MainActivity.username;

                //String filename = String.valueOf(holder.tvFileName.getText());
                // String docid = String.valueOf(holder.tvDocid.getText());

                //delete doc volley request
                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("delMultidocids", jsonArrayDocids.toString());
                Log.e("delMultidocids", DmsActivity.dynamicFileSlid);


                multiDel(userid, username, ip, jsonArrayDocids.toString(), "No", DmsActivity.dynamicFileSlid);

                // docidList.clear();

                normalMode();

                fileViewListAdapter.clearCheckedList();


                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();
    }

    private void showMultiMoveDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);


        TextView tvHeading = dialogView.findViewById(R.id.tv_alert_restore_heading);
        tvHeading.setText(R.string.move_files);

        TextView tvMessage = dialogView.findViewById(R.id.tv_recyclebin_restore_message);
        String str = getString(R.string.are_you_sure_want_to_move, counter + "");
        tvMessage.setText(str);

        Button btn_cancel = dialogView.findViewById(R.id.btn_no_restore_popup);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();


            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_yes_restore_popup);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String fullname = String.valueOf(holder.tvUploadedBy.getText());
                String roleid = DmsActivity.roleid;
                String ip = MainActivity.ip;
                String userid = DmsActivity.userid;
                String username = MainActivity.username;

                //String filename = String.valueOf(holder.tvFileName.getText());
                // String docid = String.valueOf(holder.tvDocid.getText());

                //delete doc volley request
                //  docidList.clear();

                // fileViewListAdapter.clearCheckedList();

                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("moveMultidocids", jsonArrayDocids.toString());
                // Log.e("delMultidocids",DmsActivity.dynamicFileSlid);

                doclistArray = jsonArrayDocids.toString();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
                String slid_Session = sharedPreferences.getString("userSlid", null);

                //multiMove(username,userid,ip,slid_Session,);

                Intent intent = new Intent(FileViewActivity.this, MoveCopyStorageActivity.class);
                intent.putExtra("mode", "movefiles");
                intent.putExtra("foldername&&slid", DmsActivity.foldernameDyanamic + "&&" + DmsActivity.dynamicFileSlid);
                intent.putExtra("viewer", "file");
                intent.putExtra("slid", slidDynamic);
                //intent.putExtra("filecount",counter);

                startActivityForResult(intent, 1003);

                normalMode();

                fileViewListAdapter.clearCheckedList();

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }

    private void showMultiCopyDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);


        TextView tvHeading = dialogView.findViewById(R.id.tv_alert_restore_heading);
        tvHeading.setText(R.string.copy_files);

        TextView tvMessage = dialogView.findViewById(R.id.tv_recyclebin_restore_message);
        tvMessage.setText(getString(R.string.are_you_sure_you_want_to_copy_these_files, counter + ""));

        Button btn_cancel = dialogView.findViewById(R.id.btn_no_restore_popup);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();


            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_yes_restore_popup);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("copyMultidocids", jsonArrayDocids.toString());
                // Log.e("delMultidocids",DmsActivity.dynamicFileSlid);

                doclistArray = jsonArrayDocids.toString();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
                String slid_Session = sharedPreferences.getString("userSlid", null);

                //multiMove(username,userid,ip,slid_Session,);

                Intent intent = new Intent(FileViewActivity.this, MoveCopyStorageActivity.class);
                intent.putExtra("mode", "copyfiles");
                intent.putExtra("foldername&&slid", DmsActivity.foldernameDyanamic + "&&" + DmsActivity.dynamicFileSlid);
                intent.putExtra("viewer", "file");
                intent.putExtra("slid", DmsActivity.dynamicFileSlid);
                //intent.putExtra("filecount",counter);

                startActivityForResult(intent, 1004);
                normalMode();

                fileViewListAdapter.clearCheckedList();

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }

    private void showMultiSendMailDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_share_files_mail, null);

        TextInputEditText tieSubject = dialogView.findViewById(R.id.ad_tie_share_files_mail_enter_subject);
        TextInputEditText tieEmail = dialogView.findViewById(R.id.ad_tie_share_files_mail_enter_email_id);
        EditText etMessage = dialogView.findViewById(R.id.ad_et_share_files_mail_enter_msg);

        Button btnClose = dialogView.findViewById(R.id.ad_btn_share_files_mail_close);
        Button btnSend = dialogView.findViewById(R.id.ad_btn_share_files_mail_send);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String subj = tieSubject.getText().toString();
                String mailid = tieEmail.getText().toString();
                String msg = etMessage.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Pattern pattern = Pattern.compile(emailPattern);


                if (mailid.length() == 0 || TextUtils.isEmpty(mailid)) {

                    Toast.makeText(FileViewActivity.this, R.string.enter_email_id, Toast.LENGTH_SHORT).show();
                } /*else if (!pattern.matcher(mailid).matches()) {

                    Toast.makeText(FileViewActivity.this, "Enter valid email id", Toast.LENGTH_SHORT).show();

                }*/ else if (subj.length() == 0 || TextUtils.isEmpty(subj)) {

                    Toast.makeText(FileViewActivity.this, R.string.enter_subject, Toast.LENGTH_SHORT).show();

                } else if (msg.length() == 0 || TextUtils.isEmpty(msg)) {

                    Toast.makeText(FileViewActivity.this, R.string.enter_message, Toast.LENGTH_SHORT).show();

                } else {


                    String list = Arrays.toString(docidList.toArray());
                    String docids = list.substring(list.indexOf("[") + 1, list.lastIndexOf("]"));
                    sendDocOnMail(mailid, msg, subj, docids, MainActivity.userid, DmsActivity.dynamicFileSlid);

                }


            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    private void normalMode() {

        if (in_action_mode) {

            counter = 0;
            toolbar.getMenu().clear();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle("Storage Management");
            toolbar.setSubtitle(foldername);
            in_action_mode = false;
            onlongclick = false;
            fileViewListAdapter.checkboxUnselectAll();
            fileViewListAdapter.notifyDataSetChanged();

            //code here for close dialog button
            alertDialog.cancel();

        }


    }

    private void multiMove(final String username, final String userid, final String ip, final String movetoParentid, final String moveFromSlid, final String movetoSlid, final String docids) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        getUserRole(MainActivity.userid);
                        /*  getFileListData(DmsActivity.dynamicFileSlid, "1");*/
                        normalMode();

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

                Map<String, String> params = new HashMap<>();
                params.put("mulMove_ip", ip);
                params.put("mulMove_username", username);
                params.put("mulMove_sl_id_move_multi", moveFromSlid);
                params.put("mulMove_lastMoveId", movetoSlid);
                params.put("mulMove_userid", userid);
                params.put("mulMove_moveToParentId", movetoParentid);
                params.put("mulMove_doc_id_smove_multi", docids);
                params.put("ln", sessionManager.getLanguage());


                return params;
            }


        };


        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    private void ShareMultipeFiles(final String userid, final String userids, final String docids, final String ip, final String username) {

        sharedFileStatusList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("reponse sharefiles", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        String toUsername = jsonObject.getString("toUsername");
                        String docname = jsonObject.getString("docname");

                        if (error.equals("null") || error.equals("true")) {

                            String status = docname + "\n" +
                                    getString(R.string.to) + " : " + toUsername + "\n" +
                                    getString(R.string.status) + " : " + message;
                            // String status = docname + " " + "to" + " " + toUsername + " " + message;

                            if (sessionManager.getLanguage().equals("hi")) {


                            }

                            sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_close_black_24dp));
                        } else if (error.equals("false")) {

                            String status = docname + "\n" +
                                    getString(R.string.to) + " : " + toUsername + "\n" +
                                    getString(R.string.status) + " : " + message;
                            sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_check_green_24dp));

                        }


                    }


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FileViewActivity.this);

                    LayoutInflater inflater = FileViewActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);

                    rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                    LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(FileViewActivity.this);
                    rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);
                    rvSharedFileStatus.setHasFixedSize(true);
                    rvSharedFileStatus.setItemViewCacheSize(sharedFileStatusList.size());

                    SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, FileViewActivity.this);
                    rvSharedFileStatus.setAdapter(sharedFileStatusAdapter);


                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_no_sharedstatus_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });

                    dialogBuilder.setView(dialogView);

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                /* String error= jsonArray.getJSONArray(0).getJSONObject(0).getString("error");
                 String message = jsonArray.getJSONArray(0).getJSONObject(0).getString("message");
*/
                   /* switch (error) {
                        case "null":

                            Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                            break;
                        case "true":

                            Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                            break;
                        case "false":

                            Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                            break;
                    }*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Userid", userid);
                parameters.put("userids", userids);
                parameters.put("docids", docids);
                parameters.put("ip", ip);
                parameters.put("username", username);
                parameters.put("ln", sessionManager.getLanguage());


                return parameters;
            }

        };


        FileViewListAdapter.docidlist.clear();
        FileViewListAdapter.checkedlist.clear();
        docidList.clear();
        shareuseridsList.clear();

        //switching to normal mode from multiselct mode
        normalMode();


        alertDialog.dismiss();

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    void multiDel(final String userid, final String username, final String ip, final String delMultiDocids, final String delPermission, final String slid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("muldel", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //docidList.clear();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        getUserRole(MainActivity.userid);
                        // getFileListData(DmsActivity.dynamicFileSlid, "1");
                        normalMode();
                        //docidList.clear();

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

                Map<String, String> params = new HashMap<>();
                params.put("delIp", ip);
                params.put("delUsername", username);
                params.put("delMultiple", delPermission);
                params.put("delSlid", slid);
                params.put("delUserid", userid);
                params.put("delFileDocids", delMultiDocids);
                params.put("ln", sessionManager.getLanguage());


                return params;
            }
        };


        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    void getUserRole(final String userid) {

        fileViewList.clear();
        //sNoList.clear();

        progressBar.setVisibility(View.VISIBLE);
        llFileViewMain.setVisibility(View.GONE);
        llnofilefound.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile file", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String role_id = jsonObject.getString("role_id");
                    String checkin_checkout = jsonObject.getString("checkin_checkout");
                    String move_file = jsonObject.getString("move_file");
                    String copy_file = jsonObject.getString("copy_file");
                    String file_delete = jsonObject.getString("file_delete");
                    String file_version = jsonObject.getString("file_version");
                    String share_file = jsonObject.getString("share_file");
                    String view_metadata = jsonObject.getString("view_metadata");
                    //String file_view = jsonObject.getString("file_view");
                    String pdf_file = jsonObject.getString("pdf_file");
                    String image_file = jsonObject.getString("image_file");
                    String delete_version = jsonObject.getString("delete_version");
                    String lock_folder = jsonObject.getString("lock_folder");

                    lockfolder = lock_folder;


                    if (delete_version.equalsIgnoreCase("1")) {

                        delversion = "1";

                    } else {

                        delversion = "0";
                    }

                    if (pdf_file.equalsIgnoreCase("1")) {

                        pdfView = "1";
                        Log.e("ok", "ok");

                    } else {

                        pdfView = "0";
                        Log.e("ok", "ok 0");

                    }


                    if (image_file.equalsIgnoreCase("1")) {

                        imageView = "1";
                        Log.e("ok", "ok");

                    } else {

                        imageView = "0";
                        Log.e("ok", "ok 0");

                    }

                    if (share_file.equalsIgnoreCase("1")) {

                        shareFile = "1";

                    } else {

                        shareFile = "0";

                    }

                    if (view_metadata.equalsIgnoreCase("1")) {

                        viewMetadata = "1";

                    } else {

                        viewMetadata = "0";

                    }


                    if (file_version.equalsIgnoreCase("1")) {

                        fileVersion = "1";

                    } else {

                        fileVersion = "0";

                    }

                    if (file_delete.equalsIgnoreCase("1")) {

                        fileDelete = "1";

                    } else {

                        fileDelete = "0";

                    }

                    if (checkin_checkout.equalsIgnoreCase("1")) {

                        checkinCheckout = "1";

                    } else {

                        checkinCheckout = "0";

                    }
                    if (move_file.equalsIgnoreCase("1")) {


                        movefile = "1";
                    } else {


                        movefile = "0";
                    }
                    if (copy_file.equalsIgnoreCase("1")) {

                        copyfile = "1";

                    } else {

                        copyfile = "0";
                    }


                    //||userrole.equals("17")
                    checkAdmin = role_id.equals("1");


                    getFileListData(slid, String.valueOf(page_number));


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
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");
                params.put("ln", sessionManager.getLanguage());


                return params;
            }
        };

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);


    }

    void sendDocOnMail(String mailto, String mailbody, String subject, String mailFile, String userid, String slid) {

        pdMail.setTitle(getString(R.string.sending_doc_on_mail));
        pdMail.setMessage(getString(R.string.please_wait));
        pdMail.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("send doc on mail", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        normalMode();
                        alertDialog.dismiss();
                        pdMail.dismiss();

                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(FileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        pdMail.dismiss();

                    } else {

                        Toast.makeText(FileViewActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        pdMail.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pdMail.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mailto", mailto);
                params.put("subject", subject);
                params.put("mailbody", mailbody);
                params.put("mailFile", mailFile);
                params.put("userid", userid);
                params.put("slid", slid);
                params.put("projectName", "EzeeProcess");
                params.put("ip", MainActivity.ip);
                params.put("ln", sessionManager.getLanguage());

                Util.printParams(params, "send on mail");

                return params;

            }
        };

        VolleySingelton.getInstance(FileViewActivity.this).addToRequestQueue(stringRequest);

    }

    private String setRoles() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("move_file").append(",")
                .append("copy_file").append(",")
                .append("share_file").append(",")
                .append("checkin_checkout").append(",")
                .append("file_delete").append(",")
                .append("file_version").append(",")
                .append("view_metadata").append(",")
                .append("pdf_file").append(",")
                .append("role_id").append(",")
                .append("image_file").append(",")
                .append("lock_folder").append(",")
                .append("delete_version");

        return stringBuilder.toString();
    }

}



