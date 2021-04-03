package in.cbslgroup.ezeepeafinal.ui.activity.workflow;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.AuditWorkFlowAdapter;
import in.cbslgroup.ezeepeafinal.model.AuditTrailWorkflow;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class AuditTrailWorkFlowActivity extends AppCompatActivity {

    List<AuditTrailWorkflow> auditTrailWorkflowList = new ArrayList<>();
    RecyclerView rvAudit;
    TextView tvTotalLogs, tvNoResultFound, tvCountPanel;
    View navView;
    Spinner spMembers;
    List<String> spinnerMemberList = new ArrayList<>();
    Button btnSearch, btnScrollupdown,btnReset;
    DrawerLayout drawer;
    String spinnerUsername;
    Boolean isScrolling;
    AuditWorkFlowAdapter auditWorkFlowAdapter;
    SearchView searchView;
    ProgressBar progressBar;
    LinearLayout ll_workflow_audit_main, ll_user_nooflogs, llNoFileFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_trail_work_flow);

        initLocale();

        Toolbar toolbar = findViewById(R.id.toolbar_audit_workflow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });


        rvAudit = findViewById(R.id.rv_workflow_audit);
        rvAudit.setHasFixedSize(true);
        rvAudit.setLayoutManager(new LinearLayoutManager(this));
        rvAudit.setItemViewCacheSize(auditTrailWorkflowList.size());
        rvAudit.setItemAnimator(new DefaultItemAnimator());

        auditWorkFlowAdapter = new AuditWorkFlowAdapter(auditTrailWorkflowList, AuditTrailWorkFlowActivity.this);

        btnScrollupdown = findViewById(R.id.btn_audit_workflow_hover_scrollupdown);

        tvTotalLogs = findViewById(R.id.tv_workflowaudit_resultcount);
        tvNoResultFound = findViewById(R.id.rv_tv_audit_workflow_no_result_found);
        tvCountPanel = findViewById(R.id.tv_workflowaudit_resultcount);
        progressBar = findViewById(R.id.progressBarWorkflowAudit);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        ll_workflow_audit_main = findViewById(R.id.ll_audit_trail_workflow_main);
        ll_user_nooflogs = findViewById(R.id.ll_audit_workflow_nooflogs);
        llNoFileFound = findViewById(R.id.ll_audit_workflow_no_data_found);

        getAuditTrailWorkFlowList(MainActivity.userid);

        groupmembersList(MainActivity.userid);

        drawer = findViewById(R.id.drawer_layout_audit_trail_workflow);

       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = findViewById(R.id.nav_view_audit_trail_workflow);
        // navigationView.setNavigationItemSelectedListener(this);
        navView = navigationView.getHeaderView(0);

        spMembers = navView.findViewById(R.id.spinner_audit_workflow_members);

        btnSearch = navView.findViewById(R.id.btn_audit_workflow_search);

        btnReset = navView.findViewById(R.id.btn_audit_workflow_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAuditTrailWorkFlowList(MainActivity.userid);
                groupmembersList(MainActivity.userid);


            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //manoj shakya 02-04-2021
                if (spMembers.getSelectedItem()!=null && !spMembers.getSelectedItem().toString().equals(getString(R.string.select_member))) {
                    spinnerUsername = spMembers.getSelectedItem().toString();

                  if (drawer.isDrawerOpen(GravityCompat.END)) {
                        drawer.closeDrawer(GravityCompat.END);
                    } else {
                        drawer.openDrawer(GravityCompat.END);
                    }
                    // Toast.makeText(AuditTrailUserActivity.this, spinnerUserid, Toast.LENGTH_SHORT).show();
                    getPersonSpecificAuditList(spinnerUsername);


                } else {

                    Toast.makeText(AuditTrailWorkFlowActivity.this, R.string.please_select_member, Toast.LENGTH_SHORT).show();

                }


            }
        });


        searchView = findViewById(R.id.searchview_audit_workflow);
        Util.setSearchviewTextSize(searchView, 12, getString(R.string.search_by_username_action_performed_etc));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                List<AuditTrailWorkflow> filteredList = new ArrayList<>();

                for (int i = 0; i < auditTrailWorkflowList.size(); i++) {

                    final String username = auditTrailWorkflowList.get(i).getUserName().toLowerCase();
                    final String ip = auditTrailWorkflowList.get(i).getIp().toLowerCase();
                    //final String startdate = auditTrailList.get(i).getActionStartDate().toLowerCase();
                    //final String enddate = auditTrailList.get(i).getActionEndDate().toLowerCase();
                    final String actionperformed = auditTrailWorkflowList.get(i).getActionPerformed().toLowerCase();

                    final String sno = auditTrailWorkflowList.get(i).getSno().toLowerCase();
                    final String serialNum = sno.substring(sno.lastIndexOf(" ") + 1);
                    Log.e("serialNum", serialNum);

                    if (serialNum.contains(newText) || actionperformed.contains(newText) || username.contains(newText) || ip.contains(newText)) {

                        filteredList.add(auditTrailWorkflowList.get(i));

                    }
                }

                rvAudit.setLayoutManager(new LinearLayoutManager(AuditTrailWorkFlowActivity.this));
                auditWorkFlowAdapter = new AuditWorkFlowAdapter(filteredList, AuditTrailWorkFlowActivity.this);

                if (filteredList.size() == 0) {

                    Log.e("audittrailistsize", String.valueOf(auditTrailWorkflowList.size()));
                    tvNoResultFound.setVisibility(View.VISIBLE);

                    rvAudit.setAdapter(auditWorkFlowAdapter);
                    auditWorkFlowAdapter.notifyDataSetChanged();


                } else {


                    tvNoResultFound.setVisibility(View.GONE);
                    rvAudit.setAdapter(auditWorkFlowAdapter);
                    auditWorkFlowAdapter.notifyDataSetChanged();


                }

                // auditListAdapter.setDisplayCount(totalLogs);

                if (auditWorkFlowAdapter.getItemCount() == 0) {

                    //tvCountPanel.setText("Showing 0 to 0 of 0 entries");
                    tvCountPanel.setText(getString(R.string.zero_results_found));
                } else {

                    // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries");
                    tvCountPanel.setText(auditWorkFlowAdapter.getItemCount()+" "+ getString(R.string.results_found));


                }
                return false;
            }
        });


        rvAudit.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isScrolling = false;
                        btnScrollupdown.setVisibility(View.GONE);

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isScrolling = true;
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //isScrolling=false;
                        //btnScrollupdown.setVisibility(View.GONE);
                        break;

                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy > 0) {


                    //scroll upwards
                    btnScrollupdown.setVisibility(View.VISIBLE);
                    btnScrollupdown.setText(getString(R.string.move_to_last));
                    btnScrollupdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward_white_24dp, 0, 0, 0);


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
/*
                            Animation animation = AnimationUtils.loadAnimation(AuditTrailUserActivity.this, android.R.anim.fade_in);
                            animation.setDuration(200);
                            btnScrollupdown.startAnimation(animation);*/


                            btnScrollupdown.setVisibility(View.GONE);

                        }
                    }, 5000);


                    btnScrollupdown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // vibrator.vibrate(100);
                            rvAudit.scrollToPosition(auditWorkFlowAdapter.getItemCount() - 1);

                        }
                    });


                } else {

                    //scroll downwards

                    btnScrollupdown.setVisibility(View.VISIBLE);
                    btnScrollupdown.setText(getString(R.string.move_to_first));
                    btnScrollupdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward_white_24dp, 0, 0, 0);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                       /*     Animation animation = AnimationUtils.loadAnimation(AuditTrailUserActivity.this, android.R.anim.fade_in);
                            animation.setDuration(200);
                            btnScrollupdown.startAnimation(animation);*/

                            //vibrator.vibrate(100);
                            btnScrollupdown.setVisibility(View.GONE);

                        }
                    }, 5000);


                    btnScrollupdown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            rvAudit.scrollToPosition(0);

                        }
                    });


                }

            }
        });


    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_audit_trail_workflow);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audit_trail_work_flow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_workflow_audit) {

            //Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void getAuditTrailWorkFlowList(final String userid) {

        auditTrailWorkflowList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ll_workflow_audit_main.setVisibility(View.GONE);
        ll_user_nooflogs.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_AUDIT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("workflowaudit", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {


                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);

                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String username = jsonArray.getJSONObject(i).getString("user_name");
                            Log.e("username",username);
                            String ip = jsonArray.getJSONObject(i).getString("system_ip");
                            Log.e("ip",ip);
                            String userid = jsonArray.getJSONObject(i).getString("user_id");
                            Log.e("userid",userid);
                            String actionname = jsonArray.getJSONObject(i).getString("action_name");
                            Log.e("actionname",actionname);
                            String startdate = jsonArray.getJSONObject(i).getString("start_date");
                            Log.e("startdate",startdate);
                            auditTrailWorkflowList.add(new AuditTrailWorkflow("S.No." + (i + 1), username, actionname, startdate, ip, userid));

                        }

                        auditWorkFlowAdapter = new AuditWorkFlowAdapter(auditTrailWorkflowList, AuditTrailWorkFlowActivity.this);
                        rvAudit.setAdapter(auditWorkFlowAdapter);
                        tvTotalLogs.setText(jsonArray.length()+" " +getString(R.string.records_found));

                        progressBar.setVisibility(View.GONE);
                        ll_workflow_audit_main.setVisibility(View.VISIBLE);
                        llNoFileFound.setVisibility(View.GONE);
                        ll_user_nooflogs.setVisibility(View.VISIBLE);


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

        VolleySingelton.getInstance(AuditTrailWorkFlowActivity.this).addToRequestQueue(stringRequest);


    }

    void groupmembersList(final String userid) {

        spinnerMemberList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_AUDIT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("audit grp member list", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String username = jsonArray.getJSONObject(i).getString("user_name");

                        spinnerMemberList.add(username);


                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            AuditTrailWorkFlowActivity.this, android.R.layout.simple_spinner_item, spinnerMemberList) {
                        @Override
                        public int getCount() {
                            // don't display last item. It is used as hint.
                            int count = super.getCount();
                            return count > 0 ? count - 1 : count;
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMemberList.add(getString(R.string.select_member));
                    spMembers.setAdapter(adapter);
                    spMembers.setSelection(adapter.getCount());

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

                Map<String, String> params = new HashMap<>();
                params.put("UserID", userid);
                return params;
            }
        };

        VolleySingelton.getInstance(AuditTrailWorkFlowActivity.this).addToRequestQueue(stringRequest);


    }

    void getPersonSpecificAuditList(final String username) {

        auditTrailWorkflowList.clear();

        llNoFileFound.setVisibility(View.GONE);
        ll_workflow_audit_main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ll_user_nooflogs.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_AUDIT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {

                        ll_workflow_audit_main.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);

                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            String username = jsonArray.getJSONObject(i).getString("user_name");
                            String actionname = jsonArray.getJSONObject(i).getString("action_name");
                            String date = jsonArray.getJSONObject(i).getString("start_date");
                            String ip = jsonArray.getJSONObject(i).getString("system_ip");
                            String userid = jsonArray.getJSONObject(i).getString("user_id");

                            auditTrailWorkflowList.add(new AuditTrailWorkflow("S.no " + (i + 1), username, actionname, date, ip, userid));


                        }

                        auditWorkFlowAdapter = new AuditWorkFlowAdapter(auditTrailWorkflowList, AuditTrailWorkFlowActivity.this);
                        rvAudit.setAdapter(auditWorkFlowAdapter);
                        tvTotalLogs.setText(jsonArray.length() +" "+getString(R.string.records_found));

                        progressBar.setVisibility(View.GONE);
                        ll_workflow_audit_main.setVisibility(View.VISIBLE);
                        ll_user_nooflogs.setVisibility(View.VISIBLE);


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

                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }


        };


        VolleySingelton.getInstance(AuditTrailWorkFlowActivity.this).addToRequestQueue(stringRequest);


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

}
