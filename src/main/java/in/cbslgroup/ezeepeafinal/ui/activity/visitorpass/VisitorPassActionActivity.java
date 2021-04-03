package in.cbslgroup.ezeepeafinal.ui.activity.visitorpass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cbslgroup.ezeepeafinal.adapters.list.CommentListAdapter;
import in.cbslgroup.ezeepeafinal.model.CommentTaskApproveReject;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class VisitorPassActionActivity extends AppCompatActivity {

    List<CommentTaskApproveReject> commentList = new ArrayList<>();
    CommentListAdapter commentListAdapter;
    RecyclerView rvComments;
    TextView tvMeetingAction, tvAddOutime, tvStatus, tvActionBanner, tvNoCommentsFound;

    TextInputLayout tilPassNo, tilVisName, tilCompanyName, tilMeetingWith, tilDate, tilMobileNo, tilDeptName, tilInTime, tilOutTime, tilPurpose, tilNoOfVisitors;
    TextInputEditText tiePassNo, tieVisName, tieCompanyName, tieMeetingWith, tieDate, tieMobileNo, tieDeptName, tieInTime, tieOutTime, tiePurpose, tieNoOfVisitors;

    String meetingStatus = "0";

    String visid, actionby, outtime="";

    ProgressBar pbComments;

    CircleImageView civProfile;

    Toolbar toolbar;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_pass_action);


        initToolbar();
        initViews();
        initListeners();
        initRecyclerviews();
        setUserDetails();


    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar_visitor_pass_detail);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });

    }

    private void setUserDetails() {

        Intent intent = getIntent();

        visid = intent.getStringExtra("Id");
       // outtime = intent.getStringExtra("outtime");
        actionby = intent.getStringExtra("actionby");

//        String remark = intent.getStringExtra("remark");
//        String pic = intent.getStringExtra("pic");
//        String departmentName = intent.getStringExtra("department_name");
//        String employeename = intent.getStringExtra("employeename");
//        String updateMeetingStatus = intent.getStringExtra("update_meeting_status");
//        String addOutTime = intent.getStringExtra("addOutTime");
//        String status = intent.getStringExtra("status");
//        String passno = intent.getStringExtra("passno");
//        String visitorName = intent.getStringExtra("visitor_name");
//        String mobileno = intent.getStringExtra("mobileno");
//        String companyname = intent.getStringExtra("companyname");
//        String deptId = intent.getStringExtra("deptId");
//        String meetingwith = intent.getStringExtra("meetingwith");
//        String noofvisitors = intent.getStringExtra("noofvisitors");
//        String visitPurpose = intent.getStringExtra("visit_purpose");
//        String visitDate = intent.getStringExtra("visit_date");
//        String intime = intent.getStringExtra("intime");

        getSpecificAppointment(visid);

        getComments(visid);


//
//        if (tvMeetingAction.getVisibility() == View.GONE && tvAddOutime.getVisibility() == View.GONE) {
//
//            tvActionBanner.setVisibility(View.GONE);
//        }


    }

    void initViews() {

        civProfile = findViewById(R.id.civ_visitor_detail_image);

        pbComments = findViewById(R.id.pb_visitor_comments);

        tvMeetingAction = findViewById(R.id.tv_action_visitor_meeting_action);
        tvStatus = findViewById(R.id.tv_visitor_detail_status);
        tvAddOutime = findViewById(R.id.tv_action_visitor_meeting_add_outtime);
        tvActionBanner = findViewById(R.id.tv_action_banner);
        tvNoCommentsFound = findViewById(R.id.tv_visitor_pass_detail_no_comments_found);


        //textinputEditexts
        tieCompanyName = findViewById(R.id.tie_visitor_detail_company_name);
        tieDate = findViewById(R.id.tie_visitor_detail_date);
        tieDeptName = findViewById(R.id.tie_visitor_detail_dept_name);
        tieInTime = findViewById(R.id.tie_visitor_detail_in_time);
        tieMeetingWith = findViewById(R.id.tie_visitor_detail_meeting_with);
        tieMobileNo = findViewById(R.id.tie_visitor_detail_mobile_no);
        tieOutTime = findViewById(R.id.tie_visitor_detail_out_time);
        tiePassNo = findViewById(R.id.tie_visitor_detail_pass_no);
        tiePurpose = findViewById(R.id.tie_visitor_detail_purpose);
        tieVisName = findViewById(R.id.tie_visitor_detail_vis_name);
        tieNoOfVisitors = findViewById(R.id.tie_visitor_detail_num_of_visitors);


    }

    void initListeners() {

        tvMeetingAction.setOnClickListener(view -> showActionAlertDialog());
        tvAddOutime.setOnClickListener(view -> showAddOutimeDialog());


    }

    void initRecyclerviews() {

        rvComments = findViewById(R.id.rv_visitor_pass_detail_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

    }

    void setProfilePic(String path) {

        Glide.with(VisitorPassActionActivity.this)
                .load(ApiUrl.BASE_URL_ROOT + path)
                .placeholder(R.drawable.ic_perm_identity_white_24dp)
                .error(R.drawable.ic_perm_identity_white_24dp)
                .thumbnail(0.5f).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                //civProfile.setImageResource(R.drawable.ic_perm_identity_white_24dp);
                Log.e("Glide exception ", String.valueOf(e));
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                return false;
            }
        }).into(civProfile);


    }

    //other methods
    private void changeStatus(String status) {

        if (tvMeetingAction.getVisibility() == View.GONE && tvAddOutime.getVisibility() == View.GONE) {

            tvActionBanner.setVisibility(View.GONE);

        }

        if(status.equalsIgnoreCase("Done")){

            ((GradientDrawable) tvStatus.getBackground()).setColor(getResources().getColor(R.color.md_green_600));
            tvStatus.setText(getString(R.string.done));

        }

       else if(status.equalsIgnoreCase("Awaited")){
            ((GradientDrawable) tvStatus.getBackground()).setColor(getResources().getColor(R.color.md_blue_500));
            tvStatus.setText(getString(R.string.awaited));
        }

        else if(status.equalsIgnoreCase("cancel")){

            ((GradientDrawable) tvStatus.getBackground()).setColor(getResources().getColor(R.color.md_red_600));
            tvStatus.setText(getString(R.string.cancel));
        }

        else if(status.equalsIgnoreCase("Allow")){

            ((GradientDrawable) tvStatus.getBackground()).setColor(getResources().getColor(R.color.md_green_600));
            tvStatus.setText(getString(R.string.allow));
        }


        else if(status.equalsIgnoreCase("Pending")){

            ((GradientDrawable) tvStatus.getBackground()).setColor(getResources().getColor(R.color.md_orange_500));
            tvStatus.setText(getString(R.string.pending));
        }


    }

    //AlertDialog
    private void showAddOutimeDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_add_outime, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        TextInputEditText tieDate = dialogView.findViewById(R.id.tie_add_outime);
        // TODO Auto-generated method stub

        if (outtime.equals("-") || outtime.length() == 0 || outtime == null) {

            Calendar mcurrentTime = Calendar.getInstance();
            tieDate.setText(new SimpleDateFormat("hh:mm a").format(mcurrentTime.getTime()));

        } else {

            tieDate.setText(outtime);
        }


        tieDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(VisitorPassActionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);


//                         String hourString = "";
//                         if(selectedHour < 12) {
//                             hourString = selectedHour < 10 ? "0"+selectedHour : ""+selectedHour;
//                         } else {
//                             hourString = (selectedHour - 12) < 10 ? "0"+(selectedHour - 12) : ""+(selectedHour - 12);
//                         }
//                         String minuteString = selectedMinute < 10 ? "0"+selectedMinute : ""+selectedMinute;

                        // tieDate.setText( hourString + ":" + minuteString+" "+(selectedHour>11?"PM":"AM"));
                        tieDate.setText(new SimpleDateFormat("hh:mm a").format(calendar.getTime()));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        Button btnSave = dialogView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                updateOuttime(tieDate.getText().toString());


            }
        });

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });


        alertDialog.show();

    }

    private void showActionAlertDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.bottomsheet_visitor_action, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        RadioGroup radioGroup = dialogView.findViewById(R.id.rg_update_meeting_status);
        meetingStatus = "4"; //as allow is deafult thats y
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    //Toast.makeText(VisitorPassActionActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    String s = rb.getText().toString();
                    if (getString(R.string.done).equalsIgnoreCase(s)) {
                        meetingStatus = "1";
                    } else if (getString(R.string.wait).equalsIgnoreCase(s)) {
                        meetingStatus = "2";
                    } else if (getString(R.string.cancel).equalsIgnoreCase(s)) {
                        meetingStatus = "3";
                    } else if (getString(R.string.allow).equalsIgnoreCase(s)) {
                        meetingStatus = "4";
                    } else {
                        meetingStatus = "0";
                    }

                }

            }
        });

      //  radioGroup.getCheckedRadioButtonId()

        TextInputEditText tieComment = dialogView.findViewById(R.id.tie_comments);


        Button btnSave = dialogView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String status = meetingStatus;
                String comment = tieComment.getText().toString();

                updateMeetingStatus(visid, comment, status);

                alertDialog.dismiss();


            }
        });

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });


        alertDialog.show();


    }


    //web methods
    private void getComments(String visitorId) {

        pbComments.setVisibility(View.VISIBLE);
        rvComments.setVisibility(View.GONE);
        tvNoCommentsFound.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VISITOR_PASS
                , response -> {

            Log.e("vis_comment_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                //String msg = jsonObject.getString("msg");
                String error = jsonObject.getString("error");

                if (error.equalsIgnoreCase("false")) {


                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String comment = jsonArray.getJSONObject(i).getString("comment");
                        String fullname = jsonArray.getJSONObject(i).getString("fullname");
                        String date = jsonArray.getJSONObject(i).getString("date");
                        String pic = jsonArray.getJSONObject(i).getString("pic");

                        commentList.add(new CommentTaskApproveReject(fullname, "", comment, date, pic));

                    }

                    commentListAdapter = new CommentListAdapter(commentList, this);
                    rvComments.setAdapter(commentListAdapter);
                    Log.e("ankit", "here");

                    pbComments.setVisibility(View.GONE);
                    rvComments.setVisibility(View.VISIBLE);
                    tvNoCommentsFound.setVisibility(View.GONE);


                } else {

                    pbComments.setVisibility(View.GONE);
                    rvComments.setVisibility(View.GONE);
                    tvNoCommentsFound.setVisibility(View.VISIBLE);

                }


            } catch (JSONException e) {
                pbComments.setVisibility(View.GONE);
                rvComments.setVisibility(View.GONE);
                tvNoCommentsFound.setVisibility(View.VISIBLE);

                Log.e("vis_res_err_json", e.toString());


            }


        }, error -> {

            pbComments.setVisibility(View.GONE);
            rvComments.setVisibility(View.GONE);
            tvNoCommentsFound.setVisibility(View.VISIBLE);

            Log.e("vis_res_err", error.toString());


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("visitorId", visitorId);
                params.put("action", "getComments");

                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void updateMeetingStatus(String visitorId, String remark, String status) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Meeting Status");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VISITOR_PASS
                , response -> {

            Log.e("vis_status_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String error = jsonObject.getString("error");

                if (error.equalsIgnoreCase("false")) {

                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                    //if successfully updated status
                    tvMeetingAction.setVisibility(View.GONE);

                    getSpecificAppointment(visid);
                    progressDialog.dismiss();

                } else {

                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            } catch (JSONException e) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                Log.e("vis_status_res_json", e.toString());
                progressDialog.dismiss();


            }


        }, error -> {

            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
            Log.e("vis_status_res_err", error.toString());
            progressDialog.dismiss();


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("visitId", visitorId);
                params.put("optradio", status);
                params.put("userid", new SessionManager(VisitorPassActionActivity.this).getUserId());

                if (!remark.isEmpty()) {

                    params.put("remark", remark);
                    params.put("rId", actionby);
                }


                params.put("action", "updateStatus");

                Log.e("update_Stattus_param", params.toString());


                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void updateOuttime(String outtime) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.updating_out_time_status));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VISITOR_PASS
                , response -> {

            Log.e("vis_outtime_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String error = jsonObject.getString("error");

                if (error.equalsIgnoreCase("false")) {

                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                    //if successfully updated status
                    tvAddOutime.setVisibility(View.GONE);

                    getSpecificAppointment(visid);

//                    changeStatus("");
                    progressDialog.dismiss();

                } else {

                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            } catch (JSONException e) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                Log.e("vis_outtime_res_json", e.toString());
                progressDialog.dismiss();


            }


        }, error -> {

            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
            Log.e("vis_outtime_res_err", error.toString());
            progressDialog.dismiss();


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("outtime", outtime);
                params.put("recordId", visid);
                params.put("action", "updateOutime");

                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getSpecificAppointment(String vId) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.fetching_apt));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VISITOR_PASS
                , response -> {

            Log.e("vis_specific_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String error = jsonObject.getString("error");

                if (error.equalsIgnoreCase("false")) {

                    String passno = jsonObject.getString("passno").equals("null") ? "-" : jsonObject.getString("passno");
                    String visitor_name = jsonObject.getString("visitor_name").equals("null") ? "-" : jsonObject.getString("visitor_name");
                    String mobileno = jsonObject.getString("mobileno").equals("null") ? "-" : jsonObject.getString("mobileno");
                    String companyname = jsonObject.getString("companyname").equals("null") ? "-" : jsonObject.getString("companyname");
                    String meetingwith = jsonObject.getString("meetingwith").equals("null") ? "-" : jsonObject.getString("meetingwith");
                    String noofvisitors = jsonObject.getString("noofvisitors").equals("null") ? "-" : jsonObject.getString("noofvisitors");
                    String visit_purpose = jsonObject.getString("visit_purpose").equals("null") ? "-" : jsonObject.getString("visit_purpose");
                    String visit_date = jsonObject.getString("visit_date").equals("null") ? "-" : jsonObject.getString("visit_date");
                    String intime = jsonObject.getString("intime").equals("null") ? "-" : jsonObject.getString("intime");
                    String outtime = jsonObject.getString("outtime").equals("null") ? "-" : jsonObject.getString("outtime");
                    String pic = jsonObject.getString("pic");
                    String department_name = jsonObject.getString("department_name").equals("null") ? "-" : jsonObject.getString("department_name");
                    String status = jsonObject.getString("status").equals("null") ? "-" : jsonObject.getString("status");
                    String update_meeting_status = jsonObject.getString("update_meeting_status");


                    this.outtime = outtime;

                    //outime visibilty
                    tvAddOutime.setVisibility(outtime.equals("-") ? View.VISIBLE : View.GONE);
                    //hiding according to permission
                    tvMeetingAction.setVisibility(update_meeting_status.equals("1") ? View.VISIBLE : View.GONE);


                    //set profile pic
                    setProfilePic(pic);

                    //setting the status of the meeting
                    changeStatus(status);

                    tieVisName.setText(visitor_name);
                    tiePassNo.setText(passno);
                    tieCompanyName.setText(companyname);
                    tieMeetingWith.setText(meetingwith);
                    tieDate.setText(visit_date);
                    tieMobileNo.setText(mobileno);
                    tieDeptName.setText(department_name);
                    tieInTime.setText(intime);
                    tieOutTime.setText(outtime);
                    tiePurpose.setText(visit_purpose);
                    tieNoOfVisitors.setText(noofvisitors);

                    progressDialog.dismiss();


                } else {

                    Toast.makeText(this, "Error fetching details", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }


            } catch (JSONException e) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                Log.e("vis_specific_res_json", e.toString());
                progressDialog.dismiss();


            }


        }, error -> {

            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
            Log.e("vis_specific_res_err", error.toString());
            progressDialog.dismiss();


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("vId", vId);
                params.put("userid", new SessionManager(VisitorPassActionActivity.this).getUserId());
                params.put("action", "getSpecificAppointment");
                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }


}
