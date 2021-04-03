package in.cbslgroup.ezeepeafinal.ui.fragments.upload;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.UploadActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.SearchableSpinnerAdapter;
import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.model.StepList;
import in.cbslgroup.ezeepeafinal.model.TaskList;
import in.cbslgroup.ezeepeafinal.model.WorkFlowList;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.NotificationActions;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

import static android.app.Activity.RESULT_OK;
import static in.cbslgroup.ezeepeafinal.utils.ApiUrl.UPLOAD_DOC_IN_WORKFLOW;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadInWorkflowFragment extends Fragment {


    LinearLayout llSelectWork, llSelectStep, llSelectTask;
    Spinner spSelectStep, spSelectTask;
//spSelectWork

    ArrayAdapter<String> spSelectWorkAdapter;
    ArrayAdapter<String> spSelectStepAdapter;
    ArrayAdapter<String> spSelectTaskAdapter;


    List<String> workFlowNameList = new ArrayList<>();
    List<String> stepNameList = new ArrayList<>();
    List<String> taskNameList = new ArrayList<>();

    List<WorkFlowList> workFlowList = new ArrayList<>();
    List<StepList> stepList = new ArrayList<>();
    List<TaskList> taskList = new ArrayList<>();


    Button btnBack, btnSubmit;

    JSONObject jsonObject;

    String filePath = UploadActivity.filePath;
    MultipartUploadRequest multipartUploadRequest;
    AlertDialog alertDialogProcessing, alertDialogError, alertDialogSuccess;


    List<User> userlist = new ArrayList<>();
    SearchableSpinnerAdapter searchableSpinnerAdapter;
    AlertDialog alertDialog;

    TextView tvWorkflow;

    ProgressDialog pd, progressDialog;

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(getActivity(), null);
        if (lang == null) {

            LocaleHelper.persist(getActivity(), "en");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(LocaleHelper.onAttach(context, "en"));
    }


    public UploadInWorkflowFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_upload_in_workflow, container, false);

        //LinearLAyouts
        llSelectWork = v.findViewById(R.id.ll_upload_in_workflow_select_workflow);
        llSelectWork.setVisibility(View.VISIBLE);
        llSelectStep = v.findViewById(R.id.ll_upload_in_workflow_select_step);
        llSelectStep.setVisibility(View.GONE);
        llSelectTask = v.findViewById(R.id.ll_upload_in_workflow_select_task);
        llSelectTask.setVisibility(View.GONE);

        initLocale();

        //spinners
        //spSelectWork = v.findViewById(R.id.sp_upload_in_workflow_select_workflow);
        spSelectStep = v.findViewById(R.id.sp_upload_in_workflow_select_step);
        spSelectTask = v.findViewById(R.id.sp_upload_in_workflow_select_task);

        //textview Select workflow
        tvWorkflow = v.findViewById(R.id.tv_upload_in_workflow_select_workflow);

        btnBack = v.findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UploadActivity.stepView.done(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, new VerifyAndCompleteFragment());
                ft.commit();

                UploadActivity.stepView.go(2, true);

            }
        });
        btnSubmit = v.findViewById(R.id.submit_workflow_doc_upload);
        btnSubmit.setEnabled(false);

        pd = new ProgressDialog(getActivity());
        pd.setTitle(getString(R.string.file_upload_in_workflow));
        pd.setMessage(getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.setButton(getString(R.string.run_in_background), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = getActivity().getIntent();
                intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = DescribesFragment.metalabellist.size();

                jsonObject = null;

                try {

                    jsonObject = makeJsonObject(DescribesFragment.metalabellist, DescribesFragment.metaEnteredlist, count);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("json", String.valueOf(jsonObject));


                String workflowname = tvWorkflow.getText().toString();
                String stepname = spSelectStep.getSelectedItem().toString();
                String taskname = spSelectTask.getSelectedItem().toString();
                String slid = DmsActivity.dynamicFileSlid;

                String workflowid = "";


                for (User user : userlist) {

                    if (user.getUsername().equalsIgnoreCase(workflowname)) {

                        workflowid = user.getUserid();

                    }

                }


//                for(WorkFlowList workFlowList : workFlowList) {
//
//                    if (workFlowList.getWorkFlowName().contains(workflowname)) {
//
//                        workflowid = workFlowList.getWorkFlowId();
//
//                    }
//
//                }

                String stepid = "";

                for (StepList stepList : stepList) {

                    if (stepList.getStepName().contains(stepname)) {

                        stepid = stepList.getStepId();
                        Log.e("stpID", workflowid);

                    }

                }

                String taskid = "";

                for (TaskList taskList : taskList) {

                    Log.e("tskname", taskname);
                    if (taskList.getTaskName().equalsIgnoreCase(taskname)) {

                        taskid = taskList.getTaskId();
                        Log.e("tskID", taskid);

                    }

                }

                byte[] data = new byte[0];
                try {

                    data = slid.getBytes("UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                String slidBase64 = Base64.encodeToString(data, Base64.DEFAULT);

                //getting workflowid

                Log.e("------", "--------");
                Log.e("widBase64", workflowid);
                Log.e("slid", DmsActivity.dynamicFileSlid);
                Log.e("userid", MainActivity.userid);
                Log.e("jsonobject", String.valueOf(jsonObject));
                Log.e("stepid", stepid);
                Log.e("taskid", taskid);
                Log.e("pagecount", VerifyAndCompleteFragment.pagecount);
                Log.e("------", "--------");

                // alertProcessing("On", getActivity());


                UploadDoc(slidBase64, MainActivity.userid, String.valueOf(jsonObject), workflowid, stepid, taskid, VerifyAndCompleteFragment.pagecount, "");

               /* Intent intent = new Intent(getActivity(),UploadActivity.class);
                startActivity(intent);*/
                //UploadDoc("0","9","8","8","9","9","5");


            }
        });

        tvWorkflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getWorkFlow(MainActivity.userid);

            }
        });


        // Inflate the layout for this fragment
        return v;
    }


    public void getWorkFlow(final String userid) {

        userlist.clear();
        btnSubmit.setEnabled(false);
        workFlowNameList.clear();
        workFlowList.clear();
        llSelectTask.setVisibility(View.GONE);
        llSelectStep.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading Workflow List");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_DOC_IN_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response in getmembrs", response);


                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String msg = jsonObject.getString("msg");
//                    String error = jsonObject.getString("error");
//                    String totalMembers = jsonObject.getString("totalMemberCount");


                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            String workflowid = jsonArray.getJSONObject(i).getString("workflow_id");
                            String workflowname = jsonArray.getJSONObject(i).getString("workflow_name");

//                        workFlowList.add(new WorkFlowList(workflowname,workflowid));
//                        workFlowNameList.add(workflowname);


                            userlist.add(new User(workflowname, R.drawable.workflow_icon_round, workflowid));

                        }


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.searchable_spinner_layout, null);

                        TextView tvHeading = dialogView.findViewById(R.id.tv_searchable_alert_dialog_heading);
                        tvHeading.setText("Select Workflow");

                        RecyclerView rvMember = dialogView.findViewById(R.id.rv_searchable_spinner_alert_dialog);
                        rvMember.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvMember.setNestedScrollingEnabled(true);

                        ProgressBar pb = dialogView.findViewById(R.id.pb_searchable_spinner);
                        LinearLayout llRv = dialogView.findViewById(R.id.ll_searchable_alert_dialog_rv);
                        LinearLayout llNoMemFound = dialogView.findViewById(R.id.ll_searchable_spinner_no_member_found);

                        TextView tvNoMemberFound = dialogView.findViewById(R.id.no_member_found);
                        tvNoMemberFound.setText("No Workflow Found");


                        EditText etFilter = dialogView.findViewById(R.id.et_searchable_alert_dialog_search_member);
                        etFilter.setHint("Search Workflow");


                        pb.setVisibility(View.VISIBLE);
                        llRv.setVisibility(View.GONE);
                        llNoMemFound.setVisibility(View.GONE);

                        if (jsonArray.length() > 0) {
//
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String username = jsonArray.getJSONObject(i).getString("user_name");
//                            String userid = jsonArray.getJSONObject(i).getString("user_id");
//                            userlist.add(new User(username, userid));
//
//                        }

                            searchableSpinnerAdapter = new SearchableSpinnerAdapter(getActivity(), userlist);
                            rvMember.setAdapter(searchableSpinnerAdapter);
                            searchableSpinnerAdapter.setOnItemClickListener(new SearchableSpinnerAdapter.OnItemClickListener() {
                                @Override
                                public void onMemberClickListener(String wfname, String wfid) {

                                    tvWorkflow.setText(wfname);
                                    //tvDialogUserid.setText(userid);

                                    getStep(wfid);
                                    alertDialog.dismiss();


                                }
                            });

                            etFilter.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    String newText = charSequence.toString();
                                    List<User> filteredList = new ArrayList<>();

                                    for (int j = 0; j < userlist.size(); j++) {

                                        final String username = userlist.get(j).getUsername().toLowerCase();

                                        if (username.contains(newText)) {

                                            filteredList.add(userlist.get(j));
                                        }

                                    }

                                    //rvMember.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    searchableSpinnerAdapter = new SearchableSpinnerAdapter(getActivity(), filteredList);
                                    searchableSpinnerAdapter.setOnItemClickListener(new SearchableSpinnerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onMemberClickListener(String wfname, String wfid) {

                                            tvWorkflow.setText(wfname);
                                            //tvDialogUserid.setText(userid);

                                            getStep(wfid);
                                            alertDialog.dismiss();


                                        }
                                    });

                                    if (filteredList.size() == 0) {

                                        llNoMemFound.setVisibility(View.VISIBLE);
                                        rvMember.setVisibility(View.GONE);
                                        rvMember.setAdapter(searchableSpinnerAdapter);
                                        searchableSpinnerAdapter.notifyDataSetChanged();


                                    } else {


                                        rvMember.setVisibility(View.VISIBLE);
                                        llNoMemFound.setVisibility(View.GONE);
                                        rvMember.setAdapter(searchableSpinnerAdapter);
                                        searchableSpinnerAdapter.notifyDataSetChanged();

                                    }


                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            pb.setVisibility(View.GONE);
                            llRv.setVisibility(View.VISIBLE);


                        } else if (jsonArray.length() == 0) {

                            pb.setVisibility(View.GONE);
                            llRv.setVisibility(View.GONE);
                            llNoMemFound.setVisibility(View.VISIBLE);


                        } else {

                            pb.setVisibility(View.GONE);
                            llRv.setVisibility(View.GONE);
                            llNoMemFound.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

                        }


                        Button btnCancel = dialogView.findViewById(R.id.btn_searchable_alert_dialog_cancel);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                alertDialog.dismiss();

                            }
                        });


                        progressDialog.dismiss();
                        dialogBuilder.setView(dialogView);
                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        alertDialog.show();


                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "No Workflow Found", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Error Fetching workflow list", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error Fetching workflow list", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("UseridWork", userid);
                return params;
            }

        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }


//    void getWorkFlowOld(String userid){
//
//         btnSubmit.setEnabled(false);
//         workFlowNameList.clear();
//         workFlowList.clear();
//         llSelectTask.setVisibility(View.GONE);
//         llSelectStep.setVisibility(View.GONE);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_DOC_IN_WORKFLOW, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("workflow response",response);
//                try {
//                    JSONArray jsonArray  = new JSONArray(response);
//
//                    for(int i = 0 ; i<jsonArray.length();i++){
//
//
//                        String workflowid = jsonArray.getJSONObject(i).getString("workflow_id");
//                        String workflowname = jsonArray.getJSONObject(i).getString("workflow_name");
//
//                        workFlowList.add(new WorkFlowList(workflowname,workflowid));
//                        workFlowNameList.add(workflowname);
//
//
//                    }
//
//
//                    spSelectWorkAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, workFlowNameList) {
//
//
//                        @Override
//                        public int getCount() {
//                            // don't display last item. It is used as hint.
//                            int count = super.getCount();
//                            return count > 0 ? count - 1 : count;
//                        }
//
//
//                    };
//
//                    workFlowNameList.add("Select Workflow");
//
//
//                    spSelectWorkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spSelectWork.setAdapter(spSelectWorkAdapter);
//                    spSelectWork.setSelection(spSelectWorkAdapter.getCount());
//                    spSelectWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//
//
//                            String workflowname = spSelectWork.getSelectedItem().toString();
//
//                            if(workflowname.equalsIgnoreCase("Select Workflow")){
//
//                                //no code
//
//                            }
//
//                            else{
//
//                                String workflowid = "";
//
//                                for(WorkFlowList workFlowList : workFlowList){
//
//                                    if(workFlowList.getWorkFlowName().contains(workflowname)){
//
//                                        workflowid = workFlowList.getWorkFlowId();
//
//                                    }
//
//                                }
//
//                                getStep(workflowid);
//
//                            }
//
//
//
//
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//
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
//        }){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String,String> params = new HashMap<>();
//
//                params.put("UseridWork",userid);
//                return params;
//            }
//        };
//
//
//        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);
//
//
//
//    }

    private void getStep(String workflowid) {
        btnSubmit.setEnabled(false);
        llSelectStep.setVisibility(View.GONE);
        llSelectTask.setVisibility(View.GONE);

        stepNameList.clear();
        stepList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_DOC_IN_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("step response", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {

                        llSelectStep.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No Step Found", Toast.LENGTH_SHORT).show();

                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {


                            String stepid = jsonArray.getJSONObject(i).getString("step_id");
                            String stepName = jsonArray.getJSONObject(i).getString("step_name");
                            String workflowid = jsonArray.getJSONObject(i).getString("workflow_id");

                            stepList.add(new StepList(stepName, stepid, workflowid));
                            stepNameList.add(stepName);


                        }


                        spSelectStepAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stepNameList) {


                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }


                        };

                        stepNameList.add("Select Step");

                        spSelectStepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSelectStep.setAdapter(spSelectStepAdapter);
                        spSelectStep.setSelection(spSelectStepAdapter.getCount());

                        llSelectStep.setVisibility(View.VISIBLE);

                        spSelectStep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                btnSubmit.setEnabled(false);

                                String stepName = spSelectStep.getSelectedItem().toString();

                                if (stepName.equalsIgnoreCase("Select Step")) {

                                    //no code


                                } else {

                                    String workflowid = "";
                                    for (StepList stepList : stepList) {

                                        if (stepList.getStepName().contains(stepName)) {

                                            workflowid = stepList.getWorkflowId();

                                        }

                                    }

                                    Log.e("workflow id step ", workflowid);

                                    getTask(workflowid);

                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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

                params.put("workflowidStep", workflowid);
                return params;
            }
        };


        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void getTask(String workflowid) {

        btnSubmit.setEnabled(false);
        taskNameList.clear();
        taskList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_DOC_IN_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("task response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {

                        llSelectTask.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "No Task Found", Toast.LENGTH_SHORT).show();

                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {


                            String taskId = jsonArray.getJSONObject(i).getString("task_id");
                            String taskName = jsonArray.getJSONObject(i).getString("task_name");
                            //String workflowid =  jsonArray.getJSONObject(i).getString("workflow_id");

                            taskList.add(new TaskList(taskName, taskId));
                            taskNameList.add(taskName);


                        }


                        spSelectTaskAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, taskNameList) {


                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }


                        };

                        taskNameList.add("Select Task");
                        spSelectTaskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSelectTask.setAdapter(spSelectTaskAdapter);
                        spSelectTask.setSelection(spSelectTaskAdapter.getCount());
                        spSelectTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (!spSelectTask.getSelectedItem().toString().equalsIgnoreCase("Select Task")){

                                    btnSubmit.setEnabled(true);

                                }

                                else{

                                    btnSubmit.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        llSelectTask.setVisibility(View.VISIBLE);

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

                params.put("workflowidTask", workflowid);
                return params;
            }
        };


        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void UploadDoc(String slid, String userid, String metaname, String workflowid, String stepid, String taskid, String pagecount, String taskremark) {


        //getting the actual path of the image
        String path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filePath;
        }

        if (path == null) {

            Toast.makeText(getActivity(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code


            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);
                final ProgressDialog progress = new ProgressDialog(getActivity());

                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(getActivity(), uploadId, ApiUrl.UPLOAD_DOC_IN_WORKFLOW);
                multipartUploadRequest.addFileToUpload(path, "fileName");


                Log.e("Upload_rate", UPLOAD_RATE.toString());

          /*      uploadNotificationConfig.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                        + " at " + UPLOAD_RATE + " - " + PROGRESS;*/

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(getActivity(), 1, uploadId)));

                //uploadNotificationConfig.getProgress().iconResourceID = R.drawable.ic_upload;
                //uploadNotificationConfig.getProgress().iconColorResourceID = Color.BLUE;

                uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;

                //converting the drawable to bitmap
                // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_upload_blue_dark_24dp);
                //uploadNotificationConfig.getCompleted().largeIcon = bm;
                //uploadNotificationConfig.getCompleted().iconColorResourceID = Color.GREEN;

                uploadNotificationConfig.getError().message = "Error while uploading";
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                // uploadNotificationConfig.getError().iconColorResourceID = Color.RED;

                uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
                uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;
                // uploadNotificationConfig.getCancelled().iconColorResourceID = Color.YELLOW;


                Log.e("----upload--", "--------");
                Log.e("widBase64", workflowid);
                Log.e("slid", slid);
                Log.e("userid", userid);
                Log.e("jsonobject", String.valueOf(jsonObject));
                Log.e("stepid", stepid);
                Log.e("taskid", taskid);
                Log.e("------", "--------");


                //Adding file
                multipartUploadRequest

                        .addParameter("metaName", metaname)
                        .addParameter("storageId", slid)
                        .addParameter("pageCount", pagecount)
                        .addParameter("userId", userid)
                        .addParameter("wtsk", taskid)
                        .addParameter("wstp", stepid)
                        .addParameter("wfid", workflowid)
                        .addParameter("taskRemark", taskremark)
                        .setNotificationConfig(

                                uploadNotificationConfig
                                        .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                        .setClearOnActionForAllStatuses(true)
                        )

                        .setMaxRetries(0)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {


                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(getActivity(), "Upload Error", Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                //progress.dismiss();

                                String response = serverResponse.getBodyAsString();
                                // alertProcessing("off",getActivity());

                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    String error = jsonObject.getString("error");

                                    if (error.equalsIgnoreCase("false")) {

                                        pd.dismiss();
                                        if (getActivity() != null) {

                                            Intent intent = getActivity().getIntent();
                                            intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                                            getActivity().setResult(RESULT_OK, intent);
                                            getActivity().finish();


                                        }
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                                        // alertSuccess(msg,getActivity());

                                        // alertProcessing("off",getActivity());

                                    } else if (error.equalsIgnoreCase("true")) {

                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    } else {

                                        pd.dismiss();
                                        Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pd.dismiss();

                                    Toast.makeText(context, "Server Error while Uploading", Toast.LENGTH_SHORT).show();
                                }


                                Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {
                                Toast.makeText(context, "Upload Canceled", Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadId);
                                pd.dismiss();
                            }
                        });


                if (isNetworkAvailable()) {

                    multipartUploadRequest.startUpload();
                    pd.show();

                    Toast.makeText(getActivity(), "Upload Started..", Toast.LENGTH_SHORT).show();


                } else {

                    pd.dismiss();
                    Toast.makeText(getActivity(), "No Internet connection found", Toast.LENGTH_SHORT).show();

                }


            } catch (Exception exc) {
                pd.dismiss();
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private JSONObject makeJsonObject(ArrayList<String> label, ArrayList<String> value,
                                      int count)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();


        for (int i = 0; i < count; i++) {

            obj = new JSONObject();

            try {
                obj.put("metaLabel", label.get(i));
                obj.put("metaEntered", value.get(i));


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("meta", jsonArray1);
        return finalobject;
    }

    void alertProcessing(String onOff, Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_alert, null);
        dialogBuilder.setView(dialogView);

        alertDialogProcessing = dialogBuilder.create();
        alertDialogProcessing.setCancelable(true);
        alertDialogProcessing.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        if (onOff.equalsIgnoreCase("on")) {

            alertDialogProcessing.show();
        } else if (onOff.equalsIgnoreCase("off")) {

            alertDialogProcessing.dismiss();

        }


    }

    void alertError(String message, final Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
        tv_error_heading.setText("Error");
        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
        tv_error_subheading.setText(message);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogError.dismiss();


                Intent intent = new Intent(context, UploadActivity.class);
                startActivity(intent);
                requireActivity().finish();


            }
        });

        dialogBuilder.setView(dialogView);

        alertDialogError = dialogBuilder.create();
        alertDialogError.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogError.show();
    }

    void alertSuccess(String message, final Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
        tv_error_heading.setText("Success");
        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
        tv_error_subheading.setText(message);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialogSuccess.dismiss();


                Intent intent = new Intent(context, UploadActivity.class);
                startActivity(intent);
                getActivity().finish();


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialogSuccess = dialogBuilder.create();
        alertDialogSuccess.setCancelable(false);
        alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogSuccess.show();
    }


}



