package in.cbslgroup.ezeepeafinal.ui.activity.visitorpass;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.adapters.list.VisitorPassAdapter;
import in.cbslgroup.ezeepeafinal.model.VisitorPass;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class VisitorPassActivity extends AppCompatActivity {

    RecyclerView rvMain;
    VisitorPassAdapter visitorPassAdapter;
    List<VisitorPass> visitorPassList = new ArrayList<>();

    Toolbar toolbar;

    ProgressBar pbVis;
    TextView tvNoResultFound,tvCount;

    // for pagination
    Boolean isLoading = false;
    int pageNumber=1;
    String searchQuery ="";

    SearchView searchView;

  //  SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistor_pass);


        initToolbar();
        initViews();
        initRecyclerviews();

        //intialized when searchview is created
        //searchquery is blank in first
        getVisitorList(new SessionManager(this).getUserId(), searchQuery);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                getVisitorList(new SessionManager(VisitorPassActivity.this).getUserId(), searchQuery);
//
//            }
//        });


    }

    private void getVisitorList(String userid, String searchText) {

        visitorPassList.clear();
        pbVis.setVisibility(View.VISIBLE);
        tvNoResultFound.setVisibility(View.GONE);
        rvMain.setVisibility(View.GONE);
        pageNumber = 1;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VISITOR_PASS
                , response -> {

            Log.e("vis_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                //String msg = jsonObject.getString("msg");
                String error = jsonObject.getString("error");
                String totalResult = jsonObject.getString("total_result");

                if (error.equalsIgnoreCase("false")) {


                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = jsonArray.getJSONObject(i).getString("Id");
                        String passno = jsonArray.getJSONObject(i).getString("passno");
                        String visitor_name = jsonArray.getJSONObject(i).getString("visitor_name");
                        String mobileno = jsonArray.getJSONObject(i).getString("mobileno");
                        String companyname = jsonArray.getJSONObject(i).getString("companyname");
                        String deptId = jsonArray.getJSONObject(i).getString("deptId");
                        String meetingwith = jsonArray.getJSONObject(i).getString("meetingwith");
                        String noofvisitors = jsonArray.getJSONObject(i).getString("noofvisitors");
                        String visit_purpose = jsonArray.getJSONObject(i).getString("visit_purpose");
                        String visit_date = jsonArray.getJSONObject(i).getString("visit_date");
                        String intime = jsonArray.getJSONObject(i).getString("intime");
                        String outtime = jsonArray.getJSONObject(i).getString("outtime");
                        String remark = jsonArray.getJSONObject(i).getString("remark");
                        String pic = jsonArray.getJSONObject(i).getString("pic");
                        String actionby = jsonArray.getJSONObject(i).getString("actionby");
                        String department_name = jsonArray.getJSONObject(i).getString("department_name");
                        String employeename = jsonArray.getJSONObject(i).getString("employeename");
                        String update_meeting_status = jsonArray.getJSONObject(i).getString("update_meeting_status");
                        String addOutTime = jsonArray.getJSONObject(i).getString("addOutTime");
                        String status = jsonArray.getJSONObject(i).getString("status");

                        visitorPassList.add(new VisitorPass(visit_purpose, intime, actionby, department_name, deptId, outtime, meetingwith, remark, mobileno, pic, visitor_name, noofvisitors, companyname, addOutTime, passno, id, visit_date, employeename, update_meeting_status, status));

                    }

                    //pageNumber++;

//                    if(!swipeRefreshLayout.isRefreshing()){
//
//
//                    }


                   // swipeRefreshLayout.setRefreshing(false);

                    visitorPassAdapter = new VisitorPassAdapter(visitorPassList, this);
                    rvMain.setAdapter(visitorPassAdapter);
                    pageNumber++;
                    isLoading = false;
                    tvCount.setText(visitorPassAdapter.getItemCount()+" "+getString(R.string.out_of)+" "+totalResult+" "+getString(R.string.results));

                    pbVis.setVisibility(View.GONE);
                    tvNoResultFound.setVisibility(View.GONE);
                    rvMain.setVisibility(View.VISIBLE);
                    tvCount.setVisibility(View.VISIBLE);


                } else {
                  //  swipeRefreshLayout.setRefreshing(false);
                    pbVis.setVisibility(View.GONE);
                    tvNoResultFound.setVisibility(View.VISIBLE);
                    rvMain.setVisibility(View.GONE);
                    tvCount.setVisibility(View.GONE);
                }


            } catch (JSONException e) {
               // swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                pbVis.setVisibility(View.GONE);
                tvNoResultFound.setVisibility(View.VISIBLE);
                rvMain.setVisibility(View.GONE);
                tvCount.setVisibility(View.GONE);

                e.printStackTrace();
            }


        }, error -> {
            //swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.server_error, Toast.LENGTH_SHORT).show();
            pbVis.setVisibility(View.GONE);
            tvNoResultFound.setVisibility(View.VISIBLE);
            rvMain.setVisibility(View.GONE);
            tvCount.setVisibility(View.GONE);
            Log.e("vis_res_err", error.toString());


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("action", "getVisitorList");

                if (!searchText.isEmpty()) {


                    params.put("searchtext", searchText);
                    searchQuery = searchText;
                    pageNumber = 1; //reseting the page number as it

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setSubtitle("Searched Query : " +searchQuery);
                        }
                    });



                }
                params.put("page", String.valueOf(pageNumber));

                Log.e("vis_params",params.toString());
                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getVisitorListLoadMore(String userid, String searchText) {

        visitorPassList.add(null);
        visitorPassAdapter.notifyItemInserted(visitorPassList.size() - 1);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VISITOR_PASS
                , response -> {

            Log.e("vis_res_mre", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String error = jsonObject.getString("error");
                String totalResult = jsonObject.getString("total_result");

                if (error.equalsIgnoreCase("false")) {

                    visitorPassList.remove(visitorPassList.size() - 1);
                    int scrollPosition = visitorPassList.size();
                    visitorPassAdapter.notifyItemRemoved(scrollPosition);

                    JSONArray jsonArray = jsonObject.getJSONArray("list");



                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = jsonArray.getJSONObject(i).getString("Id");
                        String passno = jsonArray.getJSONObject(i).getString("passno");
                        String visitor_name = jsonArray.getJSONObject(i).getString("visitor_name");
                        String mobileno = jsonArray.getJSONObject(i).getString("mobileno");
                        String companyname = jsonArray.getJSONObject(i).getString("companyname");
                        String deptId = jsonArray.getJSONObject(i).getString("deptId");
                        String meetingwith = jsonArray.getJSONObject(i).getString("meetingwith");
                        String noofvisitors = jsonArray.getJSONObject(i).getString("noofvisitors");
                        String visit_purpose = jsonArray.getJSONObject(i).getString("visit_purpose");
                        String visit_date = jsonArray.getJSONObject(i).getString("visit_date");
                        String intime = jsonArray.getJSONObject(i).getString("intime");
                        String outtime = jsonArray.getJSONObject(i).getString("outtime");
                        String remark = jsonArray.getJSONObject(i).getString("remark");
                        String pic = jsonArray.getJSONObject(i).getString("pic");
                        String actionby = jsonArray.getJSONObject(i).getString("actionby");
                        String department_name = jsonArray.getJSONObject(i).getString("department_name");
                        String employeename = jsonArray.getJSONObject(i).getString("employeename");
                        String update_meeting_status = jsonArray.getJSONObject(i).getString("update_meeting_status");
                        String addOutTime = jsonArray.getJSONObject(i).getString("addOutTime");
                        String status = jsonArray.getJSONObject(i).getString("status");

                        visitorPassList.add(new VisitorPass(visit_purpose, intime, actionby, department_name, deptId, outtime, meetingwith, remark, mobileno, pic, visitor_name, noofvisitors, companyname, addOutTime, passno, id, visit_date, employeename, update_meeting_status, status));

                    }



                    tvCount.setText(visitorPassAdapter.getItemCount()+" "+R.string.out_of+" "+totalResult+" "+getString(R.string.results));
                    visitorPassAdapter.notifyDataSetChanged();
                    isLoading = false;

                    pageNumber++;

                } else {
                    //swipeRefreshLayout.setRefreshing(false);
                    visitorPassList.remove(visitorPassList.size() - 1);
                    int scrollPosition = visitorPassList.size();
                    visitorPassAdapter.notifyItemRemoved(scrollPosition);

                  //  Toast.makeText(this, R.string.no_more_data_found, Toast.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {
               // swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                visitorPassList.remove(visitorPassList.size() - 1);
                int scrollPosition = visitorPassList.size();
                visitorPassAdapter.notifyItemRemoved(scrollPosition);
                e.printStackTrace();
            }


        }, error -> {
            //swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.server_error, Toast.LENGTH_SHORT).show();
            visitorPassList.remove(visitorPassList.size() - 1);
            int scrollPosition = visitorPassList.size();
            visitorPassAdapter.notifyItemRemoved(scrollPosition);
            Log.e("vis_res_err_mre", error.toString());


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                if (!searchText.isEmpty()) {

                    params.put("searchtext", searchText);
                    searchQuery = searchText;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setSubtitle(getString(R.string.searched_query)+" " +searchQuery);
                        }
                    });
                }

                params.put("page", String.valueOf(pageNumber));
                params.put("action", "getVisitorList");

                Log.e("vis_params_more",params.toString());

                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    void initToolbar() {

        toolbar = findViewById(R.id.toolbar_visitor_pass);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });

    }

    void initViews() {

        pbVis = findViewById(R.id.pb_visitor_pass_main);
        tvNoResultFound = findViewById(R.id.tv_visitor_pass_no_result_found);
        tvCount = findViewById(R.id.tv_visitor_pass_count);
       // swipeRefreshLayout = findViewById(R.id.srl_visitor_pass_entry);


    }

    void initRecyclerviews() {

        rvMain = findViewById(R.id.rv_visitor_pass_main);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();


//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int totalItemCount = linearLayoutManager.getItemCount();
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (!isLoading) {
//                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                            && firstVisibleItemPosition >= 0) {
//
//                        getVisitorListLoadMore(new SessionManager(VisitorPassActivity.this).getUserId(),searchQuery);
//                        isLoading = true;
//
//                    }
//                }


                if (!isLoading) {

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == visitorPassList.size() - 1) {
                        //bottom of list!
                        getVisitorListLoadMore(new SessionManager(VisitorPassActivity.this).getUserId(),searchQuery);
                        isLoading = true;
                    }
                    else{

                        Log.e("ankit","not at the end");

                    }
                }
                else{

                    Log.e("ankit", String.valueOf(isLoading));
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_visitor, menu);
        MenuItem searchItem = menu.findItem(R.id.action_main_visitor_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_visitor_from_mobile_no_or_name));
        searchView.setMaxWidth(Integer.MAX_VALUE);

//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                searchView.setIconified(true);
//                return false;
//            }
//        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("search query",query);

                getVisitorList(new SessionManager(VisitorPassActivity.this).getUserId(), query);
                //searchView.setQuery("", false);
                //searchView.setIconified(true);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchView.setIconifiedByDefault(true);



        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_main_visitor_reset){

            searchQuery = "";
            toolbar.setSubtitle("");
            getVisitorList(new SessionManager(this).getUserId(), "");

        }


        return super.onOptionsItemSelected(item);



    }
}
