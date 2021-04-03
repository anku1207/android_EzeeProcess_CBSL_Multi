package in.cbslgroup.ezeepeafinal.ui.activity.usermanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.chip.FilterAdapter;
import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.chip.UsernameCompletionView;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class AddGroupActivity extends AppCompatActivity {

    LinearLayout llRoot;
    ProgressBar progressBar;
    Button btnCancel, btnSave;
    private UsernameCompletionView autoCompleteTextView;
    private FilterAdapter filterAdapter;
    private ArrayList<String> shareFilesUseridList = new ArrayList<>();
    EditText etGrpName;
    List<User> groupmemberListwithSlid = new ArrayList<>();
    List<User> usernamelist = new ArrayList<>();
    String session_userid,session_ip,session_username;
    TextView tvGroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        Toolbar toolbar = findViewById(R.id.toolbar_addGroup);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGroupActivity.this, GroupManagerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        session_userid = MainActivity.userid;
        session_ip = MainActivity.ip;
        session_username = MainActivity.username;



        progressBar = findViewById(R.id.progressBaraddGroup);
        llRoot = findViewById(R.id.ll_root_add_group);
        etGrpName = findViewById(R.id.et_addgroupactivity_group_name);
        tvGroupName = findViewById(R.id.tv_add_group_activity_group_name);

        autoCompleteTextView = findViewById(R.id.autocomplete_textview_addgroupactivity);
        autoCompleteTextView.allowDuplicates(false);

        //Set the listener that will be notified of changes in the Tokenlist
        autoCompleteTextView.setTokenListener(new TokenCompleteTextView.TokenListener<User>() {
            @Override
            public void onTokenAdded(User token) {

                Log.e("Chip", "Added");

            }

            @Override
            public void onTokenRemoved(User token) {

                Log.e("Chip", "Added");
            }
        });

        //Set the action to be taken when a Token is clicked
        autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
       // autoCompleteTextView.setDropDownVerticalOffset(1);

        getAllMemberList(session_userid);

      /*  final View activityRootView = findViewById(R.id.ll_root_add_group);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = activityRootView.getRootView()
                                .getHeight() - activityRootView.getHeight();
                        if (heightDiff > 100) { // if more than 100 pixels, its
                            // probably a keyboard...
                            if (autoCompleteTextView.isFocused()) {
                                //keyboard is shown

                                tvGroupName.setVisibility(View.GONE);
                                etGrpName.setVisibility(View.GONE);


                            }
                        } else {
                            if (autoCompleteTextView.isFocused()) {
                                //Keyboard is hidden.
                                tvGroupName.setVisibility(View.VISIBLE);
                                etGrpName.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });*/

        btnCancel = findViewById(R.id.btn_addgroupactivity_close);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddGroupActivity.this, GroupManagerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();


            }
        });

        btnSave = findViewById(R.id.btn_addgroupactivity_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareFilesUseridList.clear();

                List<User> tokens = autoCompleteTextView.getObjects();
                StringBuilder content = new StringBuilder();
                for (int i = 0; i < tokens.size(); i++) {
                    String slid = tokens.get(i).getSlid();
                    content.append(slid).append(" ");
                    shareFilesUseridList.add(slid);
                }
                shareFilesUseridList.add("1");
                shareFilesUseridList.add(session_userid);
                //  Toast.makeText(ModifyGroupActivity.this, content, Toast.LENGTH_SHORT).show();

                final JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < shareFilesUseridList.size(); i++) {

                    jsonArray.put(shareFilesUseridList.get(i));

                }

                if(etGrpName.getText().toString().equals("")||etGrpName.getText().toString().isEmpty()){


                    etGrpName.setError("Please enter group name");
                    etGrpName.requestFocus();
                }

                else{

                    AlertDialog alertDialog = new AlertDialog.Builder(AddGroupActivity.this).create();
                    alertDialog.setTitle("Adding Group");
                    alertDialog.setIcon(R.drawable.ic_group_add_grey_24dp);
                    alertDialog.setMessage("Are you sure you want to add group " + etGrpName.getText() + " ?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String useridlist = jsonArray.toString();
                            String userid = MainActivity.userid;
                            String ip = MainActivity.ip;
                            String username = MainActivity.username;
                            String groupnameNew = String.valueOf(etGrpName.getText());

                            addGroup(useridlist,userid,groupnameNew,username,ip);


                        }
                    });
                    alertDialog.show();

                }




                //  Toast.makeText(ModifyGroupActivity.this,jsonArray.toString(), Toast.LENGTH_SHORT).show();


            }
        });


    }

    void addGroup(final String useridlist , final String userid, final String groupname , final String username, final String ip){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("addgroup",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        Toast.makeText(AddGroupActivity.this, message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddGroupActivity.this, GroupManagerActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        finish();

                    } else if (error.equals("true")) {

                        Toast.makeText(AddGroupActivity.this, message, Toast.LENGTH_SHORT).show();

                    }

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
                params.put("addUsername",username);
                params.put("addUserid",userid);
                params.put("addUserids",useridlist);
                params.put("addGrpName",groupname);
                params.put("addIp",ip);
                return params;
            }
        };

        VolleySingelton.getInstance(AddGroupActivity.this).addToRequestQueue(stringRequest);


    }


    public void getAllMemberList(final String userid) {

        progressBar.setVisibility(View.VISIBLE);
        llRoot.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("grouplist add all mem", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        String userid = jsonArray.getJSONObject(i).getString("user_id");
                        String username = jsonArray.getJSONObject(i).getString("name");

                        groupmemberListwithSlid.add(new User(username, userid, R.drawable.ic_user));
                        usernamelist.add(new User(username, userid, R.drawable.ic_user));


                    }


                    //Initializing and attaching adapter for AutocompleteTextView
                    filterAdapter = new FilterAdapter(AddGroupActivity.this, R.layout.item_user, usernamelist);

                    autoCompleteTextView.setAdapter(filterAdapter);

                    autoCompleteTextView.allowDuplicates(false);
                    autoCompleteTextView.setShowAlways(true);

                    progressBar.setVisibility(View.GONE);
                    llRoot.setVisibility(View.VISIBLE);


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

                params.put("id", userid);
                return params;
            }
        };

        VolleySingelton.getInstance(AddGroupActivity.this).addToRequestQueue(stringRequest);


    }

}
