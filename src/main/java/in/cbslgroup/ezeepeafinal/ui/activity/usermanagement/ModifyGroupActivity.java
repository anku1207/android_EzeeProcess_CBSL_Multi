package in.cbslgroup.ezeepeafinal.ui.activity.usermanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.chip.FilterAdapter;
import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.chip.UsernameCompletionView;
import in.cbslgroup.ezeepeafinal.R;

public class ModifyGroupActivity extends AppCompatActivity {

    List<User> groupmemberListwithSlid = new ArrayList<>();
    List<String> groupMemberSelectedUseridList = new ArrayList<>();
    List<User> usernamelist = new ArrayList<>();
    TextView tvheading, tvUseringrp;
    EditText etGrpName;
    LinearLayout llRoot;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    private UsernameCompletionView autoCompleteTextView;
    private FilterAdapter filterAdapter;
    private ArrayList<String> shareFilesUseridList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group);

//        Toolbar toolbar = findViewById(R.id.toolbar_modifyGroup);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ModifyGroupActivity.this, GroupManagerActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                finish();
//            }
//        });
//
//        progressBar = findViewById(R.id.progressBarModifyGroup);
//        llRoot = findViewById(R.id.ll_root_modify_group);
//
//        //session userid
//        final String userid = MainActivity.userid;
//
//        //getting intents values
//        Intent intent = getIntent();
//        final String groupid = intent.getStringExtra("grpid");
//        final String groupname = intent.getStringExtra("grpname");
//
//        toolbar.setSubtitle(groupname);
//
//
//        etGrpName = findViewById(R.id.et_modifygroupactivity_group_name);
//        etGrpName.setText(groupname);
//
//        tvUseringrp = findViewById(R.id.tv_modifygroupactivity_users_in_grp);
//        tvUseringrp.setText(" Users in group : " + groupname);
//
//        autoCompleteTextView = findViewById(R.id.autocomplete_textview_modifygroupactivity);
//        autoCompleteTextView.allowDuplicates(false);
//
//        //Set the listener that will be notified of changes in the Tokenlist
//        autoCompleteTextView.setTokenListener(new TokenCompleteTextView.TokenListener<User>() {
//            @Override
//            public void onTokenAdded(User token) {
//
//                Log.e("Chip", "Added");
//
//            }
//
//            @Override
//            public void onTokenRemoved(User token) {
//
//                Log.e("Chip", "Added");
//            }
//        });
//
//        //Set the action to be taken when a Token is clicked
//        autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
//
//       /* progressBar.setVisibility(View.VISIBLE);
//        llRoot.setVisibility(View.GONE);*/
//
//        //gettijg the selected members list
//        getGroupMemberList(userid, groupid);
//
//        btnCancel = findViewById(R.id.btn_modifygroupactivity_close);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ModifyGroupActivity.this, GroupManagerActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                finish();
//
//
//            }
//        });
//
//        btnSave = findViewById(R.id.btn_modifygroupactivity_save);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                shareFilesUseridList.clear();
//
//
//                List<User> tokens = autoCompleteTextView.getObjects();
//                StringBuilder content = new StringBuilder();
//                for (int i = 0; i < tokens.size(); i++) {
//                    String slid = tokens.get(i).getSlid();
//                    content.append(slid).append(" ");
//                    shareFilesUseridList.add(slid);
//                }
//                shareFilesUseridList.add("1");
//                shareFilesUseridList.add(userid);
//                //  Toast.makeText(ModifyGroupActivity.this, content, Toast.LENGTH_SHORT).show();
//
//                final JSONArray jsonArray = new JSONArray();
//
//                for (int i = 0; i < shareFilesUseridList.size(); i++) {
//
//                    jsonArray.put(shareFilesUseridList.get(i));
//
//                }
//
//
//                if(etGrpName.getText().toString().equals("")||etGrpName.getText().toString().isEmpty()){
//
//
//                    etGrpName.setError("Please enter group name");
//                    etGrpName.requestFocus();
//                }
//
//                else{
//
//                    etGrpName.setError(null);
//
//                    AlertDialog alertDialog = new AlertDialog.Builder(ModifyGroupActivity.this).create();
//                    alertDialog.setTitle("Modifying Group");
//                    alertDialog.setIcon(R.drawable.ic_group_grey_24dp);
//                    alertDialog.setMessage("Are you sure you want to modify group " + groupname + " ?");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            String useridlist = jsonArray.toString();
//                            String userid = MainActivity.userid;
//                            String ip = MainActivity.ip;
//                            String username = MainActivity.username;
//                            String groupnameNew = String.valueOf(etGrpName.getText());
//
//                            modifyGroup(useridlist, userid, username, ip, groupid, groupnameNew);
//
//
//
//
//
//
//
//                        }
//                    });
//                    alertDialog.show();
//
//
//                }
//
//
//
//
//
//                //  Toast.makeText(ModifyGroupActivity.this,jsonArray.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        });


    }

//    void getGroupMemberList(final String userid, final String grpUserid) {
//
//        llRoot.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("grouplist", response);
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        String userid = jsonArray.getJSONObject(i).getString("userid");
//                        String username = jsonArray.getJSONObject(i).getString("name");
//
//                        // groupmemberListwithSlid.add(new User(username, userid, R.drawable.ic_user));
//                        // usernamelist.add(new User(username,userid,R.drawable.ic_user));
//                        autoCompleteTextView.addObject(new User(username, userid, R.drawable.ic_user));
//
//                    }
//
//
//                    getAllMemberList(userid);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("grpUserid", grpUserid);
//                params.put("sUserid", userid);
//                return params;
//            }
//        };
//
//        VolleySingelton.getInstance(ModifyGroupActivity.this).addToRequestQueue(stringRequest);
//
//
//    }
//
//    public void getAllMemberList(final String userid) {
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("grouplist all mem", response);
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        String userid = jsonArray.getJSONObject(i).getString("user_id");
//                        String username = jsonArray.getJSONObject(i).getString("name");
//
//                        groupmemberListwithSlid.add(new User(username, userid, R.drawable.ic_user));
//                        usernamelist.add(new User(username, userid, R.drawable.ic_user));
//
//
//                    }
//
//
//                    //Initializing and attaching adapter for AutocompleteTextView
//                    filterAdapter = new FilterAdapter(ModifyGroupActivity.this, R.layout.item_user, usernamelist);
//
//                    autoCompleteTextView.setAdapter(filterAdapter);
//
//                    autoCompleteTextView.allowDuplicates(false);
//                    autoCompleteTextView.setShowAlways(true);
//
//                    progressBar.setVisibility(View.GONE);
//                    llRoot.setVisibility(View.VISIBLE);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//
//                params.put("id", userid);
//                return params;
//            }
//        };
//
//        VolleySingelton.getInstance(ModifyGroupActivity.this).addToRequestQueue(stringRequest);
//
//
//    }
//
//    void modifyGroup(final String useridlist, final String userid, final String username, final String ip, final String groupId, final String groupname) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                Log.e("modify", response);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String error = jsonObject.getString("error");
//                    String message = jsonObject.getString("message");
//
//                    if (error.equals("false")) {
//
//                        Toast.makeText(ModifyGroupActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(ModifyGroupActivity.this, GroupManagerActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                        finish();
//
//                    } else if (error.equals("true")) {
//
//                        Toast.makeText(ModifyGroupActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//
//                params.put("gId", groupId);
//                params.put("userID", userid);
//                params.put("iP", ip);
//                params.put("grpName", groupname);
//                params.put("userIDS", useridlist);
//                params.put("userName", username);
//
//                return params;
//            }
//
//
//        };
//
//        VolleySingelton.getInstance(ModifyGroupActivity.this).addToRequestQueue(stringRequest);
//
//
//    }
//

}
