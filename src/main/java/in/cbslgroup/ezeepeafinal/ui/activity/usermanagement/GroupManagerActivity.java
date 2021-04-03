package in.cbslgroup.ezeepeafinal.ui.activity.usermanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.GroupManagerListAdapter;
import in.cbslgroup.ezeepeafinal.interfaces.GroupManagerListInterface;
import in.cbslgroup.ezeepeafinal.model.GroupManager;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class GroupManagerActivity extends AppCompatActivity{


    RecyclerView rvGrouplist;
    List<GroupManager> groupManagerList = new ArrayList<>();
    GroupManagerListAdapter groupManagerListAdapter;

    String session_userid;

    ProgressBar progressBar;
    LinearLayout llNoListFound;
    AlertDialog alertDialog;
    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);

        Toolbar toolbar = findViewById(R.id.toolbar_group_manager);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        progressBar = findViewById(R.id.progressbar_group_manager_list);
        llNoListFound = findViewById(R.id.ll_group_manager_no_list_found);


        rvGrouplist = findViewById(R.id.rv_group_manager);
        rvGrouplist.setLayoutManager(new LinearLayoutManager(this));
        rvGrouplist.setHasFixedSize(true);
        rvGrouplist.setItemViewCacheSize(groupManagerList.size());

        session_userid = MainActivity.userid;

        getGroupList(session_userid);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_manager_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search_group_manager);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                s = s.toLowerCase();
                final List<GroupManager> filteredList = new ArrayList<>();

                for (int i = 0; i < groupManagerList.size(); i++) {

                    final String text = groupManagerList.get(i).getGroupName();
                    if (text.contains(s)) {

                        filteredList.add(groupManagerList.get(i));
                    }

                   rvGrouplist.setLayoutManager(new LinearLayoutManager(GroupManagerActivity.this));
                   groupManagerListAdapter = new GroupManagerListAdapter(GroupManagerActivity.this, filteredList, new GroupManagerListInterface() {
                       @Override
                       public void onDeleteButtonClick(View v, int position, String groupid, String groupname) {

                       }
                   });

                   if(filteredList.size()==0){

                       llNoListFound.setVisibility(View.VISIBLE);
                       rvGrouplist.setVisibility(View.GONE);
                       rvGrouplist.setAdapter(groupManagerListAdapter);
                       groupManagerListAdapter.notifyDataSetChanged();

                   }
                   else{
                       llNoListFound.setVisibility(View.GONE);
                       rvGrouplist.setVisibility(View.VISIBLE);
                       rvGrouplist.setAdapter(groupManagerListAdapter);
                       groupManagerListAdapter.notifyDataSetChanged();

                   }

                }



                return false;
            }
        });
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_add_new_group) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(GroupManagerActivity.this,AddGroupActivity.class);
            startActivity(intent);

            return true;
        }



        if (id == R.id.action_search_group_manager) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

         /*   newText = newText.toLowerCase();
            final List<AuditTrail> filteredList = new ArrayList<>();

            for (int i = 0; i < auditTrailList.size(); i++) {

                final String text = auditTrailList.get(i).getUsername().toLowerCase();
                if (text.contains(newText)) {

                    filteredList.add(auditTrailList.get(i));
                }

                rvAudit.setLayoutManager(new LinearLayoutManager(AuditTrailStorageActivity.this));
                auditListAdapter = new AuditListAdapter(AuditTrailStorageActivity.this, filteredList);
                rvAudit.setAdapter(auditListAdapter);
                auditListAdapter.notifyDataSetChanged();
            }
            return false;
*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void getGroupList(final String userid){

        progressBar.setVisibility(View.VISIBLE);
        llNoListFound.setVisibility(View.GONE);
        rvGrouplist.setVisibility(View.GONE);
        groupManagerList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("grouplist",response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0){

                        progressBar.setVisibility(View.GONE);
                        llNoListFound.setVisibility(View.VISIBLE);
                        rvGrouplist.setVisibility(View.GONE);


                    }
                    else{

                        for(int i =0 ; i<jsonArray.length();i++){


                            String groupname = jsonArray.getJSONObject(i).getString("groupname");
                            String groupid = jsonArray.getJSONObject(i).getString("groupid");

                            Log.e("groupnameid",groupname+" "+groupid);


                            groupManagerList.add(new GroupManager(groupname,groupid));




                        }

                        groupManagerListAdapter = new GroupManagerListAdapter(GroupManagerActivity.this, groupManagerList, new GroupManagerListInterface() {
                            @Override
                            public void onDeleteButtonClick(View v, final int position, final String groupid, String groupname) {



                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupManagerActivity.this);

                                LayoutInflater inflater = (LayoutInflater)GroupManagerActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                tvHeading.setText("Delete Group");

                                TextView tvSubHeading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                tvSubHeading.setText("Are you sure you want delete this group " + "?");


                                Button btn_yes = dialogView.findViewById(R.id.btn_yes_pDel_popup);

                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        String userid = MainActivity.userid;
                                        String ip = MainActivity.ip;
                                        String username = MainActivity.username;


                                        delGroup(userid, groupid, ip, username,position);


                                        alertDialog.dismiss();

                                    }
                                });

                                Button btn_no = dialogView.findViewById(R.id.btn_no_pDel_popup);

                                btn_no.setOnClickListener(new View.OnClickListener() {
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
                        });
                        rvGrouplist.setAdapter(groupManagerListAdapter);


                    }

                    progressBar.setVisibility(View.GONE);
                    llNoListFound.setVisibility(View.GONE);
                    rvGrouplist.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("userid",userid);

                return params;
            }
        };


        VolleySingelton.getInstance(GroupManagerActivity.this).addToRequestQueue(stringRequest);



    }

    void delGroup(final String userid, final String groupid, final String ip, final String username, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("delGrp", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        Toast.makeText(GroupManagerActivity.this, message, Toast.LENGTH_SHORT).show();
                        groupManagerList.remove(position);

                        getGroupList(userid);

                        alertDialog.dismiss();

                    } else if (error.equals("true")) {

                        Toast.makeText(GroupManagerActivity.this, message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

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
                params.put("delUserid", userid);
                params.put("delip", ip);
                params.put("delUserName", username);
                params.put("delGid", groupid);
                return params;
            }
        };

        VolleySingelton.getInstance(GroupManagerActivity.this).addToRequestQueue(stringRequest);


    }







}
