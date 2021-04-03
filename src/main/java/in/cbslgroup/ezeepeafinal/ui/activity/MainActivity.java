package in.cbslgroup.ezeepeafinal.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import in.cbslgroup.ezeepeafinal.ui.activity.audittrail.AuditTrailStorageActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.audittrail.AuditTrailUserActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.search.FrequentlyQueriesActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.search.MetaDataSearchActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.search.QuickSearchActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.sharedfiles.SharedFilesActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.sharedfiles.SharedWithMeActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.visitorpass.VisitorPassActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.AuditTrailWorkFlowActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.InTrayActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.IntiateFileActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.sharedfiles.ShareFolderActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.TaskTrackStatusActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.DashboardAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.StorageAllotedAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.WorkFlowListAdapter;
import in.cbslgroup.ezeepeafinal.model.DashBoard;
import in.cbslgroup.ezeepeafinal.model.StorageAlloted;
import in.cbslgroup.ezeepeafinal.model.WorkFlowList;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.ConnectivityReceiver;
import in.cbslgroup.ezeepeafinal.utils.FCMHandler;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.PermissionManager;
import in.cbslgroup.ezeepeafinal.utils.PrefManager;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    static public String lang,roleId, username, userid, ip, slid_session, designation, tasktrackstatus, intray, multiStorage, shareByMe, shareWithMe;

    RecyclerView rvDashboard;

    DashboardAdapter dashboardAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    List<DashBoard> dashBoardList = new ArrayList<>();
    SessionManager session;
    PermissionManager permissionManager;

    String session_userId = null, session_userName = null, session_userEmail = null, session_designation = null, session_contact = null, session_api_key;

    TextView emailInTray, fullnameInTray;
    View navView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AlertDialog alertDialog;
    CircleImageView circleImageView;
    NavigationView navigationView;

    //no internetfound layout
    LinearLayout llnointernetfound;
    NestedScrollView scrollViewdashboard;

    BottomSheetDialog bottomSheetDialog;
    MaterialButton btnEditProfile;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout llAudtrailMain, llSearchMain, llStorageManegmentMain, llStorageMangerMain, llWorkflowManagementMain, llIntiateWorkMain, lltodoMain, llAppointMain;
    //linear layouts of navbar
    LinearLayout llAppointmentLevel0, llTodoLevel0, llWorkFlowManagementLevel0, llWorkFlowManagementLevel1, llStorageManagementLevel0, llStorageManagementLevel1, llSearchLevel0, llAuditTrailLevel0, llAuthlevel0, llUserMangerlevel0, llUserManagementlevel0;

    //recycler view workflow list
    RecyclerView rvWorkFLowlist;
    List<WorkFlowList> workFlowList = new ArrayList<>();
    WorkFlowListAdapter workFlowListAdapter;

    //recycler view storage alloted list
    RecyclerView rvStorageAlloted;
    public static List<StorageAlloted> storageAllotedList = new ArrayList<>();
    StorageAllotedAdapter storageAllotedAdapter;

    ProgressBar progressBar;
    ProgressDialog pDLogout;

    ImageView ivIntiateWorkFlowArrow, ivWorkFlowManagementArrow, ivStorageManagementArrow, ivSearcArrow, ivAuditTrailArrow, ivStorageManagerArrow, ivAuthArrow, ivUserManagerArrow, ivUserManagementArrow;

    TextView tvSharefFolder, tvVisitorPass, tvInTray, tvNoWfFoundNoWfFound, tvFreqQueries, tvAbout,
            tvIntiateFile, tvWorkflowAudit, tvTaskTrackStatus, tvUserProfile, tvSupport, tvGroupManager, tvShareFilesWithMe,
            tvShareFiles, tvUserAudit, tvStorageAudit, tvQuickSearch, tvMetaSearch, tvRecycleBin, tvLogout, tvDashBoard,
            tvStorageAlloted, tvAddTodo, tvTodayTodo, tvTommorowTodo, tvThisWeekTodo, tvAllTodo, tvAddAppointment, tvViewAppointment;


    PrefManager prefManager;
    ActionBarDrawerToggle toggle;

    //permissions
    MaterialShowcaseSequence sequence;

    Toolbar toolbar;

    //Notification badge
    TextView tvNotiBadgeCount;
    int mNotiCount = 10;
    //EditText etQuickSearch;
    SearchView searchViewQuickSearch;
    ProgressBar pbDmsReport, pbWorkflowStatus, pbWorkflowPriority, pbMemoryUsage, pbUserReport;

    //cardviews of graphs
    CardView cvUserReportGraph, cvDmsReport, cvWorkflowStatusGraph, cvWfPriorGraph, cvMemoryUsageGraph, cvTodoDash, cvAptDash;
    ConstraintLayout clCalenderDash;
    private PieChart dmsReportChart, workflowStatusChart, workflowPriorityChart;
    private LineChart memoryStatusChart;
    private BarChart userReportGraph;
    private CalendarView calendarView;

    EditText etFilterWfList;
    HorizontalScrollView horizontalScrollView;

    DrawerLayout drawer;
    LinearLayout llHeaderMain;

    @Override
    public void onBackPressed() {

        finishAffinity();
        //super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

       // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_main);


        requestPermission();
        Util.startPowerSaverIntent(this);
        // startActivity(new Intent(this, ShareFolderActivity.class));
        checkNewRelease();
        initToolbar();
        initSessionManager();
        initPermissionManager();
        checkFirebaseInstanceId();

        //Check wheather the person is logged in or not
        session.checkLogin();
        Log.e("userid sent on mail", userid);


        //intializing the views
        initViews();
        initLocale();
        //loading json requests
        loadJson();


        //language
        lang = session.getLanguage();


    }

    private void initPermissionManager() {

        permissionManager = new PermissionManager(getApplicationContext());

    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void checkNewRelease() {

        try {

            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String versionName = String.valueOf(info.versionName);
            Log.e("version_name", versionName);

            isThereNewRelease(versionName);


        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }
    }

    private void initSessionManager() {
        // Session Manager
        session = new SessionManager(getApplicationContext());

        // permission Manager
        //Getting userdata from session
        HashMap<String, String> user = session.getUserDetails();
        session_userId = user.get(SessionManager.KEY_USERID);
        session_userName = user.get(SessionManager.KEY_NAME);
        session_userEmail = user.get(SessionManager.KEY_EMAIL);
        session_designation = user.get(SessionManager.KEY_DESIGNATION);
        session_contact = user.get(SessionManager.KEY_CONTACT);

        //making these variable global for easy usage
        userid = session_userId;
        designation = session_designation;
        username = session_userName;
        ip = getDeviceIpAddress();
        session.setIP(ip);

    }

    private void checkFirebaseInstanceId() {
        //String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.e("token", instanceIdResult.getToken());
                updateToken(session_userId, instanceIdResult.getToken());
            }
        });
    }

    private void initLocale() {

        String lang = LocaleHelper.getPersistedData(this, null);
        Log.e("lang_test",lang);

        if (lang == null) {

            LocaleHelper.persist(this, "en");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));

    }

    void initViews() {

        //Graphs cardviews
        cvDmsReport = findViewById(R.id.cv_dms_report_graph);
        cvWorkflowStatusGraph = findViewById(R.id.cv_workflow_status_graph);
        cvWfPriorGraph = findViewById(R.id.cv_workflow_priority_graph);
        cvMemoryUsageGraph = findViewById(R.id.cv_memory_usage_chart);
        cvUserReportGraph = findViewById(R.id.cv_user_report_chart);


        horizontalScrollView = findViewById(R.id.hori_scroll_view_dashboard);
        rvDashboard = findViewById(R.id.rv_dashboard);
        progressBar = findViewById(R.id.progressBar_mainactivity);


        searchViewQuickSearch = findViewById(R.id.sv_quick_search_in_list);
        scrollViewdashboard = findViewById(R.id.scrollview_main_dashboard);
        //        scrollViewdashboard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//
//
//
//
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//
//
//                }
//                else{
//
//
//                }
//
//
//                return false;
//            }
//        });

        llnointernetfound = findViewById(R.id.ll_mainactivity_nointernetfound);
        //llDashBoard = findViewById(R.id.ll_mainactivity_dashboard);
        swipeRefreshLayout = findViewById(R.id.swipelayout_main_acitivity);


        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                // inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
//                InputMethodManager inputMethodManager = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }


        };
        drawer.addDrawerListener(toggle);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navView = navigationView.getHeaderView(0);

        circleImageView = navView.findViewById(R.id.iv_navheader_profile);
        fullnameInTray = navView.findViewById(R.id.tv_navheader_username);
        emailInTray = navView.findViewById(R.id.tv_navheader_email);
        btnEditProfile = navView.findViewById(R.id.btn_navheader_dashboard_edit_profile);

        llHeaderMain = navView.findViewById(R.id.ll_header_main);
        llHeaderMain.setVisibility(View.GONE);

        //nav drawer views
        //imageviews
        ivStorageManagementArrow = navView.findViewById(R.id.iv_nav_drawer_storage_management_arrow);
        ivSearcArrow = navView.findViewById(R.id.iv_nav_drawer_search_arrow);
        ivAuditTrailArrow = navView.findViewById(R.id.iv_nav_drawer_audit_trail_arrow);
        ivStorageManagerArrow = navView.findViewById(R.id.iv_nav_drawer_storage_manager_arrow);
        ivAuthArrow = navView.findViewById(R.id.iv_nav_drawer_authorization_arrow);
        ivUserManagerArrow = navView.findViewById(R.id.iv_nav_drawer_user_manager_arrow);
        ivUserManagementArrow = navView.findViewById(R.id.iv_nav_drawer_user_management_arrow);
        ivWorkFlowManagementArrow = navView.findViewById(R.id.iv_nav_drawer_workflow_management_arrow);
        ivIntiateWorkFlowArrow = navView.findViewById(R.id.iv_nav_drawer_intiate_workflow_arrow);

        //linearlayouts

        llAudtrailMain = navView.findViewById(R.id.ll_Audit_trail_panel);
        llSearchMain = navView.findViewById(R.id.ll_Searchmodule_main);
        llStorageManegmentMain = navView.findViewById(R.id.ll_nav_bar_storage_management_main);
        llStorageMangerMain = navView.findViewById(R.id.ll_nav_bar_storage_manager_main);
        llWorkflowManagementMain = navView.findViewById(R.id.ll_nav_bar_workflow_management_main);
        llIntiateWorkMain = navView.findViewById(R.id.ll_nav_bar_intiate_workflow_main);


        llStorageManagementLevel0 = navView.findViewById(R.id.ll_nav_bar_storage_management_level_0);
        llSearchLevel0 = navView.findViewById(R.id.ll_nav_bar_search_level_0);
        llAuditTrailLevel0 = navView.findViewById(R.id.ll_nav_bar_Audit_trail_level_0);
        llAuthlevel0 = navView.findViewById(R.id.ll_nav_bar_Authorization_level_0);
        llStorageManagementLevel1 = navView.findViewById(R.id.ll_nav_bar_storage_management_level_1);
        llUserMangerlevel0 = navView.findViewById(R.id.ll_nav_bar_user_manager_level_0);
        llUserManagementlevel0 = navView.findViewById(R.id.ll_nav_bar_user_management_level_0);
        llWorkFlowManagementLevel0 = navView.findViewById(R.id.ll_nav_bar_workflow_management_level_0);
        llWorkFlowManagementLevel1 = navView.findViewById(R.id.ll_nav_bar_workflow_management_level_1);

        //recyclerview workflow list
        rvWorkFLowlist = navView.findViewById(R.id.rv_workflow_list_nav_bar);
        rvWorkFLowlist.setHasFixedSize(true);
        rvWorkFLowlist.setItemViewCacheSize(workFlowList.size());
        rvWorkFLowlist.setLayoutManager(new LinearLayoutManager(this));
        rvWorkFLowlist.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        rvStorageAlloted = navView.findViewById(R.id.rv_nav_bar_storage_manager_storage_assigned);
        rvStorageAlloted.setLayoutManager(new LinearLayoutManager(this));
        rvStorageAlloted.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        //textview in nav drawer
        tvStorageAlloted = navView.findViewById(R.id.tv_nav_bar_storage_manager_storage_assigned);


        tvWorkflowAudit = navView.findViewById(R.id.tv_nav_bar_workflow_audit);
        tvAbout = navView.findViewById(R.id.tv_nav_bar_about);
        tvIntiateFile = navView.findViewById(R.id.tv_nav_bar_intiate_file);
        tvTaskTrackStatus = navView.findViewById(R.id.tv_nav_bar_task_track_status);
        tvInTray = navView.findViewById(R.id.tv_nav_bar_in_tray);
        tvUserProfile = navView.findViewById(R.id.tv_nav_bar__Authorization_user_profile);
        tvGroupManager = navView.findViewById(R.id.tv_nav_bar_group_manager);
        tvSupport = navView.findViewById(R.id.tv_nav_bar_support);


        tvUserAudit = navView.findViewById(R.id.tv_nav_bar_user_audit);
        tvStorageAudit = navView.findViewById(R.id.tv_nav_bar_storage_audit);
        tvQuickSearch = navView.findViewById(R.id.tv_nav_bar_quick_search);
        tvFreqQueries = navView.findViewById(R.id.tv_nav_bar_freq_queries);
        tvShareFiles = navView.findViewById(R.id.tv_nav_bar_shared_files);
        tvShareFilesWithMe = navView.findViewById(R.id.tv_nav_bar_shared_files_withme);
        tvMetaSearch = navView.findViewById(R.id.tv_nav_bar_meta_search);
        tvRecycleBin = navView.findViewById(R.id.tv_nav_bar_recyclebin);
        tvDashBoard = navView.findViewById(R.id.tv_nav_bar_dashboard);
        tvLogout = navView.findViewById(R.id.tv_nav_bar_logoout);

        tvVisitorPass = navView.findViewById(R.id.tv_nav_bar_visitor_pass);
        tvSharefFolder = navView.findViewById(R.id.tv_nav_bar_shared_folder);


        tvNoWfFoundNoWfFound = navView.findViewById(R.id.tv_wf_list_no_workflow_found);
        etFilterWfList = navView.findViewById(R.id.et_nav_bar_workflow_list_filter);
        rvWorkFLowlist.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        etFilterWfList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String newText = s.toString();
                newText = newText.toLowerCase();
                final List<WorkFlowList> filteredList = new ArrayList<>();

                for (int i = 0; i < workFlowList.size(); i++) {

                    final String username = workFlowList.get(i).getWorkFlowName().toLowerCase();
                    Log.e("newText", newText);


                    if (username.contains(newText)) {

                        filteredList.add(workFlowList.get(i));
                    }


                }


                workFlowListAdapter = new WorkFlowListAdapter(filteredList, MainActivity.this);

                if (filteredList.size() == 0) {

                    //Log.e("audittrailistsize", String.valueOf(auditTrailList.size()));
                    tvNoWfFoundNoWfFound.setVisibility(View.VISIBLE);
                    rvWorkFLowlist.setAdapter(workFlowListAdapter);
                    workFlowListAdapter.notifyDataSetChanged();


                } else {


                    tvNoWfFoundNoWfFound.setVisibility(View.GONE);
                    rvWorkFLowlist.setAdapter(workFlowListAdapter);
                    workFlowListAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //piechartview

        dmsReportChart = findViewById(R.id.dms_report_chart);
        workflowStatusChart = findViewById(R.id.workflow_status_chart);
        workflowPriorityChart = findViewById(R.id.workflow_priority_chart);
        memoryStatusChart = findViewById(R.id.memory_status_chart);
        userReportGraph = findViewById(R.id.user_report_chart);


        //progrss bars for charts
        pbDmsReport = findViewById(R.id.pb_dms_report_chart);
        pbWorkflowStatus = findViewById(R.id.pb_workflow_status_chart);
        pbWorkflowPriority = findViewById(R.id.pb_workflow_priority_chart);
        pbMemoryUsage = findViewById(R.id.pb_memory_status_chart);
        pbUserReport = findViewById(R.id.pb_user_report_chart);

        // test();

        //for dashborad rv

//        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
//        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
//        rvDashboard.addOnScrollListener(new CenterScrollListener());
//        rvDashboard.setLayoutManager(layoutManager);


        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvDashboard.setLayoutManager(staggeredGridLayoutManager);
        rvDashboard.setLayoutFrozen(true);
        rvDashboard.setNestedScrollingEnabled(false);
//        rvDashboard.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                // Stop only scrolling.
//                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
//            }
//        });


        fullnameInTray.setText(session_userName);
        emailInTray.setText(session_userEmail);


        Util.setSearchviewTextSize(searchViewQuickSearch, 12, getString(R.string.quick_search_hint));

        searchViewQuickSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainActivity.this, QuickSearchActivity.class);
                intent.putExtra("text", query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red), getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(() -> {

           /*   //Getting dashboard data
            if (session_userId == null || session_userId.isEmpty()) {

                Log.e("session userid ", "null");

            } else

            {
                getSlidId(session_userId);
            }*/

            loadJson();


        });


        scrollViewdashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollViewdashboard.getScrollY(); //for verticalScrollView
                if (scrollY == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });

        tvSharefFolder.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, ShareFolderActivity.class);
            startActivity(intent);

        });

        tvVisitorPass.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, VisitorPassActivity.class);
            startActivity(intent);

        });

        //onclicks
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });


        tvWorkflowAudit.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AuditTrailWorkFlowActivity.class);
            startActivity(intent);

        });


        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);

            }
        });


        tvIntiateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, IntiateFileActivity.class);
                startActivity(intent);

            }
        });


        tvTaskTrackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, TaskTrackStatusActivity.class);
                startActivity(intent);


            }
        });

        tvInTray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                startActivity(intent);
            }
        });

        tvFreqQueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FrequentlyQueriesActivity.class);
                startActivity(intent);

            }
        });


        tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FaqActivity.class);
                startActivity(intent);

            }
        });


        tvUserAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AuditTrailUserActivity.class);
                startActivity(intent);


            }
        });


        tvStorageAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AuditTrailStorageActivity.class);
                startActivity(intent);

            }
        });


        tvQuickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, QuickSearchActivity.class);
                intent.putExtra("text", "");
                startActivity(intent);

            }
        });


        tvShareFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SharedFilesActivity.class);
                startActivity(intent);
            }
        });


        tvShareFilesWithMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SharedWithMeActivity.class);
                startActivity(intent);
            }
        });


        tvMetaSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MetaDataSearchActivity.class);
                startActivity(intent);

            }
        });


        tvRecycleBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RecycleBinActivity.class);
                startActivity(intent);

            }
        });


        tvDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //logout();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_logout, null);

                Button btn_yes = dialogView.findViewById(R.id.btn_yes_logout_popup);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isNetworkAvailable()) {

                            logout();

                        } else {

                            Toast.makeText(MainActivity.this, R.string.no_internet_connection_found, Toast.LENGTH_SHORT).show();

                        }


                    }
                });

                Button btn_no = dialogView.findViewById(R.id.btn_no_logout_popup);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

            }
        });


        //Arrows

        //workflow

        //ivIntiateWorkFlowArrow
        // llIntiateWorkMain

        llIntiateWorkMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llWorkFlowManagementLevel1.getVisibility() == View.VISIBLE) {

                  /*  Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel1.startAnimation(animation);
*/
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llWorkFlowManagementLevel1.setVisibility(View.GONE);
                            ivIntiateWorkFlowArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);

                        }
                    }, 300);


                } else {


                   /* Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel1.startAnimation(animation);*/


                    llWorkFlowManagementLevel1.setVisibility(View.VISIBLE);
                    ivIntiateWorkFlowArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });

        //ivWorkFlowManagementArrow
        // llWorkflowManagementMain
        llWorkflowManagementMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llWorkFlowManagementLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llWorkFlowManagementLevel0.setVisibility(View.GONE);
                            ivWorkFlowManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel0.startAnimation(animation);


                    llWorkFlowManagementLevel0.setVisibility(View.VISIBLE);
                    ivWorkFlowManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });


        //workflow end
        ivUserManagementArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llUserManagementlevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llUserManagementlevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llUserManagementlevel0.setVisibility(View.GONE);
                            ivUserManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llUserManagementlevel0.startAnimation(animation);


                    llUserManagementlevel0.setVisibility(View.VISIBLE);
                    ivUserManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        //llStorageManegmentMain
        //ivStorageManagementArrow

        llStorageManegmentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llStorageManagementLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llStorageManagementLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llStorageManagementLevel0.setVisibility(View.GONE);
                            ivStorageManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llStorageManagementLevel0.startAnimation(animation);


                    llStorageManagementLevel0.setVisibility(View.VISIBLE);
                    ivStorageManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });

        ivUserManagerArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llUserMangerlevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llUserMangerlevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llUserMangerlevel0.setVisibility(View.GONE);
                            ivUserManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llUserMangerlevel0.startAnimation(animation);


                    llUserMangerlevel0.setVisibility(View.VISIBLE);
                    ivUserManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        ivAuthArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (llAuthlevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llAuthlevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llAuthlevel0.setVisibility(View.GONE);
                            ivAuthArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llAuthlevel0.startAnimation(animation);


                    llAuthlevel0.setVisibility(View.VISIBLE);
                    ivAuthArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });


        //llSearchMain
        //ivSearcArrow

        llSearchMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llSearchLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llSearchLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llSearchLevel0.setVisibility(View.GONE);
                            ivSearcArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llSearchLevel0.startAnimation(animation);


                    llSearchLevel0.setVisibility(View.VISIBLE);
                    ivSearcArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });

        //ivAuditTrailArrow
        //llAudtrailMain

        llAudtrailMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llAuditTrailLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llAuditTrailLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llAuditTrailLevel0.setVisibility(View.GONE);
                            ivAuditTrailArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llAuditTrailLevel0.startAnimation(animation);


                    llAuditTrailLevel0.setVisibility(View.VISIBLE);
                    ivAuditTrailArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });

        //ivStorageManagerArrow
        //llStorageMangerMain
        llStorageMangerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llStorageManagementLevel1.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llStorageManagementLevel1.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llStorageManagementLevel1.setVisibility(View.GONE);
                            ivStorageManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llStorageManagementLevel1.startAnimation(animation);


                    llStorageManagementLevel1.setVisibility(View.VISIBLE);
                    ivStorageManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        navigationView.setNavigationItemSelectedListener(this);


    }

    void loadJson() {

        llHeaderMain.setVisibility(View.GONE);
        // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //getting the workflow list
        getWorkFlowList(session_userId);

        getSlidId(session_userId);

        getProfilePic(session_userId);

        //pie charts methods
        getUserReport(session_userId);
        getWorkflowStatus(session_userId);
        getWorkflowPriority(session_userId);


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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showNoInternetLayout(isConnected);

    }

    void showNoInternetLayout(Boolean isconnected) {


        if (!isconnected) {

            llnointernetfound.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);


        } else {

            llnointernetfound.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        // register connection status listener
        Initializer.getInstance().setConnectivityListener(this);
    }


    private void showNewReleasePopup(Context context, String message, String version) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_new_release, null);
        TextView tv_error_heading = dialogView.findViewById(R.id.tv_new_release_message);
        tv_error_heading.setText(message);
        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_new_release_version);
        tv_error_subheading.setText(getString(R.string.version) + " : " + version);
        ImageView btn_cancel_ok = dialogView.findViewById(R.id.iv_new_release_version_close_popup);

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        Button btn_update = dialogView.findViewById(R.id.btn_new_release_version_confirm);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    //web methods
    public void updateToken(final String u, final String t) {

        Log.e("blabla_main", "ok");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPDATE_FIREBASE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updatefbtoken service", response);


         /*       sPfbToken = getSharedPreferences("fb_token", MODE_PRIVATE);
                editor = sPfbToken.edit();
                editor.putString("tokenid",t);

                editor.commit();
*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userid", u);
                params.put("tokenid", t);
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    void isThereNewRelease(String version) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.NEW_VERSION_RELEASE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");


                    if (error.equalsIgnoreCase("false")) {

                        String version = jsonObject.getString("version");
                        showNewReleasePopup(MainActivity.this, msg, version);

                    } else {

                        Log.e("update", "no update found");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("update", e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("update", error.getMessage() + "");

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("version", version);
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void logout() {

        alertDialog.dismiss();
        pDLogout = new ProgressDialog(this);
        pDLogout.setTitle(getString(R.string.logging_out));
        pDLogout.setMessage(getString(R.string.please_wait));
        pDLogout.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGOUT, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {

                Log.e("logout", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("false")) {

                        session.logoutUser();


                        FCMHandler.disableFCM();


                        //Util.clearApplicationData(MainActivity.this);


                        // FirebaseMessagingService.clearToken(MainActivity.this);
                        //stopService(inTrayServiceIntent);
                        finishAffinity();

                        pDLogout.dismiss();


                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        pDLogout.dismiss();

                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                pDLogout.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", session_userId);

                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }//
    //getting the work flow list

    void getWorkFlowList(final String userid) {

        workFlowList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("workflowlist res", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("workflow_list");
                        if (jsonArray.length() == 0) {

                            etFilterWfList.setVisibility(View.GONE);
                            rvWorkFLowlist.setVisibility(View.GONE);
                            tvNoWfFoundNoWfFound.setVisibility(View.VISIBLE);


                        } else {


                            for (int i = 0; i < jsonArray.length(); i++) {


                                String workflowid = jsonArray.getJSONObject(i).getString("workflow_id");
                                String workflowname = jsonArray.getJSONObject(i).getString("workflow_name");
                                String arf = jsonArray.getJSONObject(i).getString("arf");
                                String cte = jsonArray.getJSONObject(i).getString("cte");
                                String wftype = jsonArray.getJSONObject(i).getString("wftype");


                                //manoj shakya 01-04-21
                                workFlowList.add(new WorkFlowList(workflowname, workflowid, arf, cte,wftype));


                            }

                            workFlowListAdapter = new WorkFlowListAdapter(workFlowList, MainActivity.this);
                            workFlowListAdapter.notifyDataSetChanged();
                            rvWorkFLowlist.setAdapter(workFlowListAdapter);

                            etFilterWfList.setVisibility(View.VISIBLE);
                            rvWorkFLowlist.setVisibility(View.VISIBLE);
                            tvNoWfFoundNoWfFound.setVisibility(View.GONE);

                        }

                    } else if (jsonObject.getString("error").equalsIgnoreCase("true")) {

                        Toast.makeText(MainActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                params.put("userid", userid);

                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }//

    public void getSlidId(String userid) {

        scrollViewdashboard.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        storageAllotedList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dashboard response", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jobj = jsonObject.getJSONObject("dashboard_data");
                    String sid = jobj.getString("slid");
                    if(sid==null || sid == "null"){


                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(MainActivity.this);
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        builder.setMessage("Please assign atleast one storage. Contact administration for storage allotment")
                                .setTitle("No Storage Assigned")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Warning");
                        alert.show();

                    }

                    else{

                        String totalfolder = jobj.getString("totalfolder");
                        String totalfile = jobj.getString("totalfile");
                        String size = jobj.getString("size");
                        String slId = jobj.getString("slid");
                        String inTray = jobj.getString("in_tray");
                        String numpages = jobj.getString("numpages");
                        String viscount = jobj.getString("visitor_count");

                        slid_session = slId;

                        Log.e("sess id", slId);

                        //String storagealloted = jobj.getString("storagealloted");

                        JSONArray jsonArray = jobj.getJSONArray("storagealloted");

                        rvStorageAlloted.setVisibility(View.VISIBLE);
                        tvStorageAlloted.setVisibility(View.GONE);

                        if (jsonArray.length() == 0) {

                            rvStorageAlloted.setVisibility(View.GONE);
                            tvStorageAlloted.setVisibility(View.VISIBLE);
                            tvStorageAlloted.setText(R.string.no_storage_alloted);

                        } else {

                            if (jsonArray.length() > 1) {

                                multiStorage = "yes";

                            } else {

                                multiStorage = "no";
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {

                                String storagename = jsonArray.getJSONObject(i).getString("storage_name");
                                String storageid = jsonArray.getJSONObject(i).getString("storage_id");

                                storageAllotedList.add(new StorageAlloted(storagename, storageid));

                            }

                            rvStorageAlloted.setVisibility(View.VISIBLE);
                            tvStorageAlloted.setVisibility(View.GONE);


                        }

                        StorageAllotedAdapter storageAllotedAdapter = new StorageAllotedAdapter(storageAllotedList, MainActivity.this);
                        rvStorageAlloted.setAdapter(storageAllotedAdapter);
                        storageAllotedAdapter.setOnClickListener(new StorageAllotedAdapter.OnClickListener() {
                            @Override
                            public void onStorageClicked(String slid, String storagename) {

                                Intent intent = new Intent(MainActivity.this, DmsActivity.class);
                                intent.putExtra("slid", slid);
                                startActivity(intent);

                            }
                        });


//                    if (storagealloted != null) {
//
//                        tvStorageAlloted.setText(storagealloted);
//
//                    } else {
//                        Log.e("storage alloacted  ", "null");
//
//                    }


//                    tvStorageAlloted.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//
//                            Intent intent = new Intent(MainActivity.this, DmsActivity.class);
//                            startActivity(intent);
//
//
//                        }
//                    });

                        Log.e("slid in json loop ", slId);
                        Log.e("total file json loop", totalfile);
                        Log.e("total size loop ", size);
                        Log.e("total folder json loop", totalfolder);

                        sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("userSlid", slId);
                        editor.apply();


                        //pie piechart
                        generateReportChart(dmsReportChart, totalfile, totalfolder, numpages);

                        scrollViewdashboard.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                        getUserPermission(userid, inTray, viscount, totalfolder, size);
                        getMemoryUsed(session_userId, size);

                    }




                    // generateUserReportGraph(userReportGraph, barEntries);
                    //generateCalender();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("a_test", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);

                // getSlidId(userid);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                Log.e("userid before response", userid);
                parameters.put("userid", userid);
                parameters.put("action", "getDashBoardData");


                return parameters;
            }
        };


        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }//

    //grphs method
    void getUserReport(final String userid) {

        pbUserReport.setVisibility(View.VISIBLE);
        //userReportGraph.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getUserReport", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray reportArray = jsonObject.getJSONArray("user_report_data");
                        ArrayList<BarEntry> barEntries = new ArrayList<>();

                        for (int i = 0; i < reportArray.length(); i++) {
                            float val = Float.parseFloat(reportArray.get(i).toString());
                            barEntries.add(new BarEntry(i, val));
                        }

                        generateUserReportGraph(userReportGraph, barEntries);

                    } else {


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
                params.put("userid", userid);
                params.put("action", "getUserReport");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getWorkflowStatus(final String userid) {

        pbWorkflowStatus.setVisibility(View.VISIBLE);
        workflowStatusChart.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getWorkflowStatus", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject workStatusObj = jsonObject.getJSONObject("workflow_status");
                        String completed = workStatusObj.getString("completed");
                        String processed = workStatusObj.getString("processed");
                        String pending = workStatusObj.getString("pending");
                        String approved = workStatusObj.getString("done");
                        String done = workStatusObj.getString("pending");
                        String rejected = workStatusObj.getString("rejected");

                        generateWorkflowStatusChart(workflowStatusChart, pending, processed, completed, done, approved, rejected);


                    } else {


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
                params.put("userid", userid);
                params.put("action", "getWorkflowStatus");

                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getMemoryUsed(final String userid, String totalmemory) {

        memoryStatusChart.setVisibility(View.GONE);
        pbMemoryUsage.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getMemoryUsed", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    // String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject jObj = jsonObject.getJSONObject("memory_used");

                        JSONArray umArray = jObj.getJSONArray("upload_memory");
                        ArrayList<Entry> umXaxis = new ArrayList<>();
                        Log.e("umArray", String.valueOf(umArray.length()));
                        for (int i = 0; i < umArray.length(); i++) {

                            float val = Float.parseFloat(umArray.get(i).toString());
                            Log.e("xaxis", String.valueOf(val));
                            umXaxis.add(new Entry(i + 1, val));

                        }

                        JSONArray dmArray = jObj.getJSONArray("download_memory");
                        ArrayList<Entry> dmYaxis = new ArrayList<>();
                        Log.e("dmArray", String.valueOf(dmArray.length()));
                        for (int j = 0; j < dmArray.length(); j++) {

                            float val = Float.parseFloat(dmArray.get(j).toString());
                            Log.e("yaxis", String.valueOf(val));
                            dmYaxis.add(new Entry(j + 1, val));

                        }

                        generateMemoryUsageChart(memoryStatusChart, umXaxis, dmYaxis, totalmemory);


                    } else {

                        memoryStatusChart.setVisibility(View.VISIBLE);
                        pbMemoryUsage.setVisibility(View.GONE);

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
                params.put("userid", userid);
                params.put("action", "getMemoryUsed");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getWorkflowPriority(final String userid) {

        pbWorkflowPriority.setVisibility(View.VISIBLE);
        workflowPriorityChart.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("workflow_priority_res", response);

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject workPriorObj = jsonObject.getJSONObject("workflow_priority");
                        String urgent = workPriorObj.getString("up");
                        String medium = workPriorObj.getString("mp");
                        String normal = workPriorObj.getString("np");


                        generateWorkflowPriorityChart(workflowPriorityChart, urgent, medium, normal);


                    } else {


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
                params.put("userid", userid);
                params.put("action", "getWorkflowPriority");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    //grphs method end
    void getProfilePic(final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    //JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = new JSONObject(response);

                    String profilepic = jsonObject.getString("profilepic");

                    if (profilepic.isEmpty() || profilepic.equals("")) {

                        circleImageView.setImageResource(R.drawable.ic_avatar);

                    } else {
                        byte[] decodedString = Base64.decode(profilepic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        circleImageView.setImageBitmap(decodedByte);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getProfilePic(userid);
                System.out.println("i am here: " + new Exception().getStackTrace()[0]);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);


                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                JSONObject js = new JSONObject(params);
                Log.e("profile params", js.toString());


                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }//

    void getUserPermission(final String userid, String intrayNum, String viscount, String numoffolders, String memoryused) {

        String SHOWCASE_ID = String.valueOf(new Random().nextInt(60000));

        dashBoardList.clear();

        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setMaskColor(getResources().getColor(R.color.black_blur));
        //config.setShapePadding(96);
        config.setDelay(200); // half second between each showcase view

        sequence = new MaterialShowcaseSequence(MainActivity.this, SHOWCASE_ID);
        sequence.setConfig(config);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getUserPermission", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    //  "metadata_search": "1",
                    //        "metadata_quick_search": "1",

                    //String userrole = jsonObject.getString("user_role");
                    String roleID = jsonObject.getString("role_id");
                    String recyclebin = jsonObject.getString("view_recycle_bin");
                    String auditTrailUserAudit = jsonObject.getString("view_user_audit");
                    String auditTrailStorageAudit = jsonObject.getString("view_storage_audit");
                    String sharefileswithme = jsonObject.getString("share_with_me");
                    String sharedfiles = jsonObject.getString("shared_file");
                    String metadatasearch = jsonObject.getString("metadata_search");
                    String metadataquicksearch = jsonObject.getString("metadata_quick_search");
                    String saveQuery = jsonObject.getString("save_query");


                    //String view_workflow_list = jsonObject.getString("view_workflow_list");
                    String dashboard_edit_profile = jsonObject.getString("dashboard_edit_profile");
                    String dashboard_mydms = jsonObject.getString("dashboard_mydms");
                    String dashboard_mytask = jsonObject.getString("dashboard_mytask");

                    String num_of_folder = jsonObject.getString("num_of_folder");
                    String num_of_file = jsonObject.getString("num_of_file");
                    String memory_used = jsonObject.getString("memory_used");


                    //workflow management
                    String workflow_audit = jsonObject.getString("workflow_audit");
                    String workflow_task_track = jsonObject.getString("workflow_task_track");
                    // String view_workflow_list = jsonObject.getString("view_workflow_list");
                    String workflow_initiate_file = jsonObject.getString("workflow_initiate_file");


                    //Graphs
                    String workflow_status_chart = jsonObject.getString("status_wf");
                    String workflow_prior_chart = jsonObject.getString("priority_wf");
                    String workflow_user_report_chart = jsonObject.getString("user_graph");

                    //visitor pass
                    String view_visitor = jsonObject.getString("view_visitor");

                    //sharefolder
                    String sharedFolder = jsonObject.getString("share_folder");
                    shareByMe = jsonObject.getString("view_shared_folder");
                    shareWithMe = jsonObject.getString("shared_folder_with_me");


                    roleId = roleID;
                    // shareByMe = shareByMe


                    permissionManager.setInTrayPerm(dashboard_mytask);
                    permissionManager.setTaskTrackPerm(workflow_task_track);
                    permissionManager.setVisitorPassPerm(view_visitor);


                    //visitor pass

                    if (num_of_file.equalsIgnoreCase("1") || num_of_folder.equalsIgnoreCase("1")) {

                        cvDmsReport.setVisibility(View.VISIBLE);

                    } else {

                        cvDmsReport.setVisibility(View.GONE);

                    }


                    if (workflow_user_report_chart.equalsIgnoreCase("1")) {

                        // cvDmsReport.setVisibility(View.VISIBLE);
                        cvUserReportGraph.setVisibility(View.VISIBLE);


                    } else {

                        // cvDmsReport.setVisibility(View.GONE);
                        cvUserReportGraph.setVisibility(View.GONE);

                    }


                    if (workflow_status_chart.equalsIgnoreCase("1")) {

                        cvWorkflowStatusGraph.setVisibility(View.VISIBLE);

                    } else {

                        cvWorkflowStatusGraph.setVisibility(View.GONE);

                    }


                    if (workflow_prior_chart.equalsIgnoreCase("1")) {

                        cvWfPriorGraph.setVisibility(View.VISIBLE);

                    } else {

                        cvWfPriorGraph.setVisibility(View.GONE);

                    }


                    if (dashboard_mydms.equalsIgnoreCase("0")) {

                        llStorageMangerMain.setVisibility(View.GONE);

                    } else {

                        dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_layers), getString(R.string.my_dms), getString(R.string.explore_the_dms)));
                        llStorageMangerMain.setVisibility(View.VISIBLE);


                    }


                    if (dashboard_mytask.equalsIgnoreCase("0")) {


                        intray = "0";
                        tvInTray.setVisibility(View.GONE);

                    } else {

                        intray = "1";
                        tvInTray.setVisibility(View.VISIBLE);
                        dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_intray), getString(R.string.in_tray), intrayNum + " " + getString(R.string.tasks)));

                    }


                    if (num_of_folder.equalsIgnoreCase("1") || num_of_file.equalsIgnoreCase("1")) {

                        cvDmsReport.setVisibility(View.VISIBLE);

                    } else {

                        cvDmsReport.setVisibility(View.GONE);

                        //dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_no_of_folders), "No. of Folders", numoffolders + " Folders"));
                        //sequence.addSequenceItem(ivNoOfFolders,"Total number of folder alloted to the user according to the rights given. ", "Next");


                    }


                    if (memory_used.equalsIgnoreCase("1")) {


                        cvMemoryUsageGraph.setVisibility(View.VISIBLE);

                    } else {

                        cvMemoryUsageGraph.setVisibility(View.GONE);


                    }


                    if (dashboard_edit_profile.equalsIgnoreCase("0")) {

                        btnEditProfile.setVisibility(View.GONE);

                    } else {

                        btnEditProfile.setVisibility(View.VISIBLE);
                    }

                    //search modules
                    if (metadataquicksearch.equalsIgnoreCase("1") || saveQuery.equalsIgnoreCase("1") || metadatasearch.equalsIgnoreCase("1")) {

                        llSearchMain.setVisibility(View.VISIBLE);

                        if (saveQuery.equalsIgnoreCase("1")) {

                            tvFreqQueries.setVisibility(View.VISIBLE);
                        } else {

                            tvFreqQueries.setVisibility(View.GONE);

                        }

                        if (metadataquicksearch.equals("0") && metadatasearch.equals("0")) {

                            llSearchMain.setVisibility(View.GONE);

                        } else {

                            llSearchMain.setVisibility(View.VISIBLE);

                        }

                        if (metadataquicksearch.equals("0")) {

                            tvQuickSearch.setVisibility(View.GONE);


                        } else {

                            tvQuickSearch.setVisibility(View.VISIBLE);

                        }

                        if (metadatasearch.equals("0")) {

                            tvMetaSearch.setVisibility(View.GONE);

                        } else {

                            tvMetaSearch.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.meta_search), getString(R.string.metadata_search), getString(R.string.search_file_using_metadata)));

                        }


                    } else {

                        llSearchMain.setVisibility(View.GONE);

                    }


                    //storage mangement
                    if (sharedfiles.equalsIgnoreCase("1")
                            || sharefileswithme.equalsIgnoreCase("1")
                            || recyclebin.equalsIgnoreCase("1") || dashboard_mydms.equalsIgnoreCase("1")) {

                        llStorageMangerMain.setVisibility(View.VISIBLE);

                        if (dashboard_mydms.equalsIgnoreCase("1")) {

                            llStorageMangerMain.setVisibility(View.VISIBLE);
                            rvStorageAlloted.setVisibility(View.VISIBLE);
                            tvStorageAlloted.setVisibility(View.GONE);


                        } else {

                            llStorageMangerMain.setVisibility(View.GONE);
                            rvStorageAlloted.setVisibility(View.GONE);
                            tvStorageAlloted.setVisibility(View.VISIBLE);


                        }


                        if (sharefileswithme.equals("0")) {

                            tvShareFilesWithMe.setVisibility(View.GONE);

                        } else {

                            tvShareFilesWithMe.setVisibility(View.VISIBLE);
                        }

                        if (sharedfiles.equals("0")) {

                            tvShareFiles.setVisibility(View.GONE);

                        } else {

                            tvShareFiles.setVisibility(View.VISIBLE);

                        }

                        if (recyclebin.equals("0")) {

                            tvRecycleBin.setVisibility(View.GONE);


                        } else {

                            tvRecycleBin.setVisibility(View.VISIBLE);

                        }


                    } else {

                        llStorageMangerMain.setVisibility(View.GONE);

                    }


                    //Audit trail
                    if (auditTrailUserAudit.equals("1") || auditTrailStorageAudit.equals("1")) {

                        llAudtrailMain.setVisibility(View.VISIBLE);

                        if (auditTrailUserAudit.equals("0")) {

                            tvUserAudit.setVisibility(View.GONE);

                        } else {

                            tvUserAudit.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_user), getString(R.string.user_report), getString(R.string.user_wise_audit_trail)));


                        }

                        if (auditTrailStorageAudit.equals("0")) {

                            tvStorageAudit.setVisibility(View.GONE);

                        } else {

                            tvStorageAudit.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_storage_blue), getString(R.string.storage_report), getString(R.string.storagewise_audit_trail)));

                        }


                    } else {

                        llAudtrailMain.setVisibility(View.GONE);
                    }


                    //workflow
                    //here it should be and operator && instead of or ||

                    if (workflow_task_track.equals("1") || workflow_initiate_file.equals("1") || workflow_audit.equals("1")) {

                        llWorkflowManagementMain.setVisibility(View.VISIBLE);

                        if (workflow_task_track.equals("0")) {


                            tasktrackstatus = "0";
                            tvTaskTrackStatus.setVisibility(View.GONE);


                        } else {

                            tasktrackstatus = "1";
                            tvTaskTrackStatus.setVisibility(View.VISIBLE);

                        }


                        if (workflow_initiate_file.equals("0")) {

                            llIntiateWorkMain.setVisibility(View.GONE);

                        } else {

                            llIntiateWorkMain.setVisibility(View.VISIBLE);

                        }


                        if (workflow_audit.equals("0")) {

                            tvWorkflowAudit.setVisibility(View.GONE);


                        } else {

                            tvWorkflowAudit.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.workflow_report), getString(R.string.workflow_audit), getString(R.string.worlkflow_audit_trail)));

                        }


                    } else {

                        llWorkflowManagementMain.setVisibility(View.GONE);


                    }

                    if (view_visitor.equals("1")) {

                        tvVisitorPass.setVisibility(View.VISIBLE);
                        dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_user), getString(R.string.visitor_pass), viscount + " " + getString(R.string.visitors)));
                    } else {

                        tvVisitorPass.setVisibility(View.GONE);
                    }

                    if (sharedFolder.equalsIgnoreCase("1")) {

                        if (shareByMe.equalsIgnoreCase("1") || shareWithMe.equalsIgnoreCase("1")) {


                            tvSharefFolder.setVisibility(View.VISIBLE);

                        } else {

                            tvSharefFolder.setVisibility(View.GONE);
                        }
                    } else {

                        tvSharefFolder.setVisibility(View.GONE);
                    }

                    Log.e("dlist_size", String.valueOf(dashBoardList.size()));
                    dashboardAdapter = new DashboardAdapter(dashBoardList, MainActivity.this);
                    dashboardAdapter.setOnItemClickListener(new DashboardAdapter.OnItemClickListener() {
                        @Override
                        public void onDmsClicked() {

                            if(multiStorage.equalsIgnoreCase("yes")){

                                showMultistorageDialog(MainActivity.this);

                            }

                            else{

                                Intent intent = new Intent(MainActivity.this, DmsActivity.class);
                                intent.putExtra("slid", MainActivity.slid_session);
                                startActivity(intent);

                            }



                        }

                        @Override
                        public void onInTrayClicked() {

                            Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onAuditTrailUserClicked() {

                            Intent intent = new Intent(MainActivity.this, AuditTrailUserActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onAuditTrailStorageClicked() {

                            Intent intent = new Intent(MainActivity.this, AuditTrailStorageActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onAuditTrailWorkflowClicked() {


                            Intent intent = new Intent(MainActivity.this, AuditTrailWorkFlowActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onMetaDataSearchClicked() {

                            Intent intent = new Intent(MainActivity.this, MetaDataSearchActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onVisitorPassClicked() {

                            Intent intent = new Intent(MainActivity.this, VisitorPassActivity.class);
                            startActivity(intent);

                        }
                    });
                    rvDashboard.setAdapter(dashboardAdapter);


                    //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    llHeaderMain.setVisibility(View.VISIBLE);

                    final int speedScroll = 5000;
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        int count = 0;
                        boolean flag = true;

                        @Override
                        public void run() {
                            if (count < dashboardAdapter.getItemCount()) {
                                if (count == dashboardAdapter.getItemCount() - 1) {
                                    flag = false;
                                } else if (count == 0) {
                                    flag = true;
                                }
                                if (flag) count++;
                                else count--;

                                rvDashboard.smoothScrollToPosition(count);
                                handler.postDelayed(this, speedScroll);
                            }
                        }
                    };

                    handler.postDelayed(runnable, speedScroll);

                } catch (JSONException e) {
                    //e.printStackTrace();

                    Log.e("json_ex_dash", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                // getUserPermission(userid);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");


                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                JSONObject js = new JSONObject(params);
                Log.e("getuserper params", js.toString());

                return params;
            }
        };

        swipeRefreshLayout.setRefreshing(false);

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }//

    String setRoles() {


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("role_id").append(",")
                .append("view_recycle_bin").append(",")
                .append("view_user_audit").append(",")
                .append("view_storage_audit").append(",")
                .append("share_with_me").append(",")
                .append("shared_file").append(",")
                .append("metadata_search").append(",")
                .append("metadata_quick_search").append(",")
                .append("dashboard_edit_profile").append(",")
                .append("dashboard_mydms").append(",")
                .append("dashboard_mytask").append(",")
                .append("num_of_folder").append(",")
                .append("num_of_file").append(",")
                .append("memory_used").append(",")
                .append("workflow_audit").append(",")
                .append("workflow_task_track").append(",")
                .append("workflow_initiate_file").append(",")
                .append("status_wf").append(",")
                .append("priority_wf").append(",")
                .append("save_query").append(",")
                .append("user_graph").append(",")
                .append("share_folder").append(",")
                .append("view_shared_folder").append(",")
                .append("shared_folder_with_me").append(",")
                .append("view_visitor")
        ;

        // .append("view_workflow_list").append(",")
        return stringBuilder.toString();


    }//

    //graphs method
    void generateReportChart(PieChart chart, String numfiles, String numfolder, String numpages) {

        pbDmsReport.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);


        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 5, 5, 10);
        chart.setExtraOffsets(5, 5, 5, 10);

        // chart.setDragDecelerationFrictionCoef(0.95f);

        // chart.setCenterTextTypeface(tfLight);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(80);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(70f);
        //chart.setTouchEnabled(false);
        chart.setDrawCenterText(true);
        /*chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL",pe.getLabel());


            }

            @Override
            public void onNothingSelected() {

            }
        });*/
 /*       chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(3000, Easing.EaseInOutQuad);*/


        LegendEntry folder = new LegendEntry();
        folder.label = numfolder +" "+getString(R.string.folders);
        folder.formColor = getResources().getColor(R.color.colorPrimary);

        LegendEntry files = new LegendEntry();
        files.label = numfiles +" "+getString(R.string.files);
        files.formColor = getResources().getColor(R.color.purple);

        LegendEntry pages = new LegendEntry();
        pages.label = numpages +" "+getString(R.string.pages);
        pages.formColor = getResources().getColor(R.color.yellow_dark);


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(5f);
        l.setTextSize(13f);
        l.setCustom(Arrays.asList(folder, files, pages));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(10f);


        float nfld = Float.parseFloat(numfolder);
        float nfiles = Float.parseFloat(numfiles);
        float npages = Float.parseFloat(numpages);
        float total = nfld + nfiles + npages;


        if (total != 0) {

            SpannableString s = new SpannableString(getString(R.string.dms_report));
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 10, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, 0);
            chart.setCenterText(s);

        } else {

            SpannableString s = new SpannableString(getString(R.string.no_data_found));
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 13, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, 0);
            chart.setCenterText(s);
        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();


        if (nfld != 0) {
            entries.add(new PieEntry((nfld / total) * 100, getString(R.string.folders)));
            colors.add(getResources().getColor(R.color.colorPrimary));

        }


        if (nfiles != 0) {
            entries.add(new PieEntry((nfiles / total) * 100, getString(R.string.files)));
            colors.add(getResources().getColor(R.color.purple));
        }

        if (npages != 0) {
            entries.add(new PieEntry((npages / total) * 100, getString(R.string.Pages)));
            colors.add(getResources().getColor(R.color.orange));


        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //data.setValueTypeface(tfLight);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
//        chart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    }
//                    case MotionEvent.EDGE_RIGHT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//                    case MotionEvent.EDGE_LEFT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//
//
//                    //case MotionEvent.ACTION_CANCEL:
//
//                    case MotionEvent.ACTION_UP: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                    }
//                }
//
//                return false;
//            }
//        });

        chart.setRotationEnabled(false);
        chart.invalidate();


        pbDmsReport.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);


    }

    void generateWorkflowStatusChart(PieChart chart, String pending, String processed, String complete, String done, String approved, String rejected) {


        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 5, 5, 10);
        // chart.setDragDecelerationFrictionCoef(0.95f);

        // chart.setCenterTextTypeface(tfLight);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(80);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(70f);

        chart.setTouchEnabled(true);

        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL", pe.getLabel());

                String label = pe.getLabel();

                if (label.equalsIgnoreCase("Pending")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskStatus", "Pending");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Processed")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskStatus", "Processed");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Complete")) {

                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskStatus", "Complete");
                    startActivity(intent);

                }


            }

            @Override
            public void onNothingSelected() {

            }
        });


        LegendEntry folder = new LegendEntry();
        folder.label = pending + " " + getString(R.string.pending);
        folder.formColor = getResources().getColor(R.color.red);

        LegendEntry files = new LegendEntry();
        files.label = processed + " " + getString(R.string.processed);
        files.formColor = getResources().getColor(R.color.yellow);

        LegendEntry pages = new LegendEntry();
        pages.label = complete + " " + getString(R.string.complete);
        pages.formColor = getResources().getColor(R.color.green);

       /* LegendEntry approv = new LegendEntry();
        approv.label = complete + " Appr.";
        approv.formColor = getResources().getColor(R.color.green_dark);

        LegendEntry rejec = new LegendEntry();
        rejec.label = complete + " Appr.";
        rejec.formColor = getResources().getColor(R.color.red);

        LegendEntry dne = new LegendEntry();
        dne.label = complete + " Done.";
        dne.formColor = getResources().getColor(R.color.colorPrimary);*/


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(5f);
        l.setTextSize(13f);
        l.setCustom(Arrays.asList(folder, files, pages));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(10f);


        float nPen = Float.parseFloat(pending);
        float nPros = Float.parseFloat(processed);
        float nCmpl = Float.parseFloat(complete);
        float nApr = Float.parseFloat(approved);
        float nDone = Float.parseFloat(done);
        float nRej = Float.parseFloat(rejected);

//+nApr+nDone+nRej
        float total = nPen + nPros + nCmpl;

        //&& nApr == 0 && nDone == 0 && nRej == 0

        if (nPen == 0 && nPros == 0 && nCmpl == 0) {

            SpannableString s = new SpannableString(getString(R.string.no_data_found));
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 13, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, 0);
            chart.setCenterText(s);

        } else {

            SpannableString s = new SpannableString(getString(R.string.workflow_status));
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 15, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, 0);
            chart.setCenterText(s);

        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();


        if (nPen != 0) {

            entries.add(new PieEntry((nPen / total) * 100, getString(R.string.pending), 0));
            colors.add(getResources().getColor(R.color.red));
        }

        if (nPros != 0) {


            entries.add(new PieEntry((nPros / total) * 100, getString(R.string.processed), 1));
            colors.add(getResources().getColor(R.color.yellow));
        }

        if (nCmpl != 0) {

            entries.add(new PieEntry((nCmpl / total) * 100, getString(R.string.complete), 2));
            colors.add(getResources().getColor(R.color.green));
        }

       /* if (nApr != 0) {

            entries.add(new PieEntry((nApr / total) * 100, "Approved", 3));
            colors.add(getResources().getColor(R.color.green_dark));
        }  if (nDone != 0) {

            entries.add(new PieEntry((nDone / total) * 100, "Done", 4));
            colors.add(getResources().getColor(R.color.colorPrimary));
        }
        if (nRej != 0) {

            entries.add(new PieEntry((nRej / total) * 100, "Rejected", 5));
            colors.add(getResources().getColor(R.color.red));
        }*/

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //data.setValueTypeface(tfLight);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
//        chart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    }
//                    case MotionEvent.EDGE_RIGHT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//                    case MotionEvent.EDGE_LEFT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//
//
//                    //case MotionEvent.ACTION_CANCEL:
//
//                    case MotionEvent.ACTION_UP: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                    }
//                }
//
//                return false;
//            }
//        });

        chart.setRotationEnabled(false);
        chart.invalidate();

        pbWorkflowStatus.setVisibility(View.GONE);
        workflowStatusChart.setVisibility(View.VISIBLE);


    }

    void generateWorkflowPriorityChart(PieChart chart, String Urgent, String Medium, String Normal) {


        chart.setUsePercentValues(false);
        // chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 5, 5, 10);

        // chart.setDragDecelerationFrictionCoef(0.95f);

        // chart.setCenterTextTypeface(tfLight);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(80);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(70f);

        chart.setTouchEnabled(true);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        // no description text
        chart.getDescription().setEnabled(false);

        chart.setDrawCenterText(true);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL", pe.getLabel());

                String label = pe.getLabel();

                if (label.equalsIgnoreCase("Urgent")) {

                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskPrior", "1");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Medium")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskPrior", "2");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Normal")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskPrior", "3");
                    startActivity(intent);


                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        //chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        //chart.setRotationEnabled(true);
        // chart.setHighlightPerTapEnabled(true);

        // chart.animateY(1400, Easing.EaseInOutQuad);


        LegendEntry folder = new LegendEntry();
        folder.label = Urgent + " " + getString(R.string.urgent);
        folder.formColor = Color.RED;

        LegendEntry files = new LegendEntry();
        files.label = Medium + " " + getString(R.string.medium);
        files.formColor = getResources().getColor(R.color.yellow);

        LegendEntry pages = new LegendEntry();
        pages.label = Normal + " " + getString(R.string.normal);
        pages.formColor = getResources().getColor(R.color.green);


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(5f);
        l.setTextSize(13f);
        l.setCustom(Arrays.asList(folder, files, pages));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(10f);

        float nUrg = Float.parseFloat(Urgent);
        float nMed = Float.parseFloat(Medium);
        float nNrml = Float.parseFloat(Normal);
        float total = nUrg + nMed + nNrml;


        if (nUrg == 0 && nMed == 0 && nNrml == 0) {

            SpannableString s = new SpannableString(getString(R.string.no_data_found));
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 13, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, 0);
            chart.setCenterText(s);

        } else {


            SpannableString s = new SpannableString(getString(R.string.workflow_priority));
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 17, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 17, 0);
            chart.setCenterText(s);

        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();


        if (nUrg != 0) {

            entries.add(new PieEntry((nUrg / total) * 100, getString(R.string.urgent)));
            colors.add(Color.RED);
        }


        if (nMed != 0) {

            entries.add(new PieEntry((nMed / total) * 100, getString(R.string.medium)));
            colors.add(getResources().getColor(R.color.yellow));

        }
        if (nNrml != 0) {

            entries.add(new PieEntry((nNrml / total) * 100, getString(R.string.normal)));
            colors.add(getResources().getColor(R.color.green));

        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //data.setValueTypeface(tfLight);
        chart.setData(data);
        chart.setRotationEnabled(false);
        // undo all highlights
        chart.highlightValues(null);
//        chart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    }
//                    case MotionEvent.EDGE_RIGHT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//                    case MotionEvent.EDGE_LEFT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//
//
//                    //case MotionEvent.ACTION_CANCEL:
//
//                    case MotionEvent.ACTION_UP: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                    }
//                }
//
//                return false;
//            }
//        });


        chart.invalidate();

        pbWorkflowPriority.setVisibility(View.GONE);
        workflowPriorityChart.setVisibility(View.VISIBLE);


    }

    void generateMemoryUsageChart(LineChart chart, ArrayList<Entry> values1, ArrayList<Entry> values2, String totalMemory) {

        Log.e("error", "1");

      /*  values1.add(new Entry(1,(float) 0));
        values1.add(new Entry(2,(float) 0));
        values1.add(new Entry(3,(float) 0));
        values1.add(new Entry(4,(float) 0));
        values1.add(new Entry(5,(float) 0));
        values1.add(new Entry(6, (float) 2.08));
        values1.add(new Entry(7,(float) 0.01));
        values1.add(new Entry(8,(float) 0));
        values1.add(new Entry(9,(float) 0));
        values1.add(new Entry(10,(float) 0));
        values1.add(new Entry(11,(float) 0));
        values1.add(new Entry(12,(float) 0));

        values2.add(new Entry(1,(float) 0));
        values2.add(new Entry(2,(float) 0));
        values2.add(new Entry(3,(float) 0));
        values2.add(new Entry(4,(float) 0));
        values2.add(new Entry(5,(float) 0));
        values2.add(new Entry(6, (float)0));
        values2.add(new Entry(7,(float) 10.02));
        values2.add(new Entry(8,(float) 0));
        values2.add(new Entry(9,(float) 0.49));
        values2.add(new Entry(10,(float) 2.08));
        values2.add(new Entry(11,(float) 6.07));
        values2.add(new Entry(12,(float) 0));*/

        TextView tvTotal = findViewById(R.id.tv_heading_memory_status_chart);
        tvTotal.setText(getString(R.string.memory_usage_in) + "( " + totalMemory + " )");

        LineDataSet set1, set2;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

            Log.e("error", "2");

            pbMemoryUsage.setVisibility(View.GONE);
            memoryStatusChart.setVisibility(View.VISIBLE);

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values1, getString(R.string.upload_memory_in_mb));

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.graph_blue));
            set1.setCircleColor(getResources().getColor(R.color.graph_blue));
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(getResources().getColor(R.color.graph_blue));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(true);
            set1.setDrawFilled(true);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = new LineDataSet(values2, getString(R.string.download_memory_in_mb));
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(getResources().getColor(R.color.graph_pink));
            set2.setCircleColor(getResources().getColor(R.color.graph_pink));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(getResources().getColor(R.color.graph_pink));
            set2.setDrawCircleHole(true);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawFilled(true);
            //set2.setFillFormatter(new MyFillFormatter(900f));

            // create a data object with the data sets
            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);


            // set data
            chart.getDescription().setEnabled(false);
            chart.animate();
            //chart.setMaxVisibleValueCount(100);
            chart.setData(data);

//            chart.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN: {
//                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                            break;
//                        }
//                        case MotionEvent.EDGE_RIGHT: {
//                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                            break;
//
//                        }
//                        case MotionEvent.EDGE_LEFT: {
//                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                            break;
//
//                        }
//
//
//                        //case MotionEvent.ACTION_CANCEL:
//
//                        case MotionEvent.ACTION_UP: {
//                            horizontalScrollView.requestDisallowInterceptTouchEvent(false);
//                            break;
//                        }
//                    }
//
//                    return false;
//                }
//            });

            Log.e("error", "2");

            pbMemoryUsage.setVisibility(View.GONE);
            memoryStatusChart.setVisibility(View.VISIBLE);
        }
    }

    void generateUserReportGraph(BarChart barChart, ArrayList<BarEntry> entries) {

       /* List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));
*/
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Jan");
        xAxisLabel.add("Feb");
        xAxisLabel.add("Mar");
        xAxisLabel.add("Apr");
        xAxisLabel.add("May");
        xAxisLabel.add("Jun");
        xAxisLabel.add("Jul");
        xAxisLabel.add("Aug");
        xAxisLabel.add("Sept");
        xAxisLabel.add("Oct");
        xAxisLabel.add("Nov");
        xAxisLabel.add("Dec");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int) value);
            }
        });


        int year = Calendar.getInstance().get(Calendar.YEAR);
        BarDataSet set = new BarDataSet(entries, getString(R.string.active_user_analytics) + " " + year);
        //set.setColor(getResources().getColor(R.color.graph_blue));
        //set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate();
//        barChart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    }
//                    case MotionEvent.EDGE_RIGHT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//                    case MotionEvent.EDGE_LEFT: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    }
//
//
//                    //case MotionEvent.ACTION_CANCEL:
//
//                    case MotionEvent.ACTION_UP: {
//                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                    }
//                }
//
//                return false;
//            }
//        });


        // refresh

        pbUserReport.setVisibility(View.GONE);
        userReportGraph.setVisibility(View.VISIBLE);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    //for permisiiosn
    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_occured, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.need_permisions);
        builder.setMessage(R.string.grant_permission_message);
        builder.setPositiveButton(R.string.goto_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_main_choose_language) {

            showLanguageChooseBottomSheet();

        }


        return super.onOptionsItemSelected(item);

    }

    private void showLanguageChooseBottomSheet() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottomsheet_choose_language, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        RadioGroup radioGroup = view.findViewById(R.id.rg_lang_en);


        RadioButton rbEng = view.findViewById(R.id.rb_lang_en);
        RadioButton rbHindi = view.findViewById(R.id.rb_lang_hi);


        radioGroup.clearCheck();
        switch (LocaleHelper.getPersistedData(this, null)) {

            case "en": {

                rbEng.setChecked(true);
            }
            break;

            case "hi": {

                rbHindi.setChecked(true);
            }

            break;

            default: {

                rbEng.setChecked(true);

            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = view.findViewById(checkedId);
                String label = radioButton.getText().toString().trim();

                if (label.equalsIgnoreCase("english")) {

                    LocaleHelper.persist(MainActivity.this, "en");
                    updateView(LocaleHelper.getPersistedData(MainActivity.this, null));
                    session.setLanguage("en");
                    bottomSheetDialog.dismiss();

                } else if (label.equalsIgnoreCase("hindi")) {

                    LocaleHelper.persist(MainActivity.this, "hi");
                    updateView(LocaleHelper.getPersistedData(MainActivity.this, null));
                    session.setLanguage("hi");
                    bottomSheetDialog.dismiss();
                }


            }
        });


        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    private void updateView(String lang) {

        Context context = LocaleHelper.setLocale(this, lang);
        //recreate();
        //tvLang.setText(context.getResources().getString(R.string.hello));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showMultistorageDialog(Context context){


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_multi_storage, null);

        RecyclerView rvMain = dialogView.findViewById(R.id.rv_select_multi_storage);
        rvMain.setLayoutManager(new LinearLayoutManager(this));

        StorageAllotedAdapter storageAllotedAdapter = new StorageAllotedAdapter(storageAllotedList, context);
        rvMain.setAdapter(storageAllotedAdapter);
        storageAllotedAdapter.setOnClickListener(new StorageAllotedAdapter.OnClickListener() {
            @Override
            public void onStorageClicked(String slid, String storagename) {

                Intent intent = new Intent(context, DmsActivity.class);
                intent.putExtra("slid", slid);
                startActivity(intent);

            }
        });


        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }


}