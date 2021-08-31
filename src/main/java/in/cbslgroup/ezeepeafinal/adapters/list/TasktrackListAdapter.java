package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.IntiateWorkFlowActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.TaskTrackingActivity;
import in.cbslgroup.ezeepeafinal.model.DocumentListWorkflow;
import in.cbslgroup.ezeepeafinal.model.TaskTrackList;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;



class ListViewHolder extends RecyclerView.ViewHolder {

    TextView tvTicketId, tvTicketDate, tvTaskStatus, tvAction, tvDocid;
    ImageView ivAction, ivViewDoc;
    Button btnViewDoc, btnReintiate;
    private CoordinatorLayout mParent;


    public ListViewHolder(View itemView) {

        super(itemView);

        tvTicketId = itemView.findViewById(R.id.tv_TicketId);
        tvTicketDate = itemView.findViewById(R.id.tv_TicketDate);
        tvTaskStatus = itemView.findViewById(R.id.tv_TaskStatus);
        //tvAction = itemView.findViewById(R.id.tv_Action);
        ivAction = itemView.findViewById(R.id.iv_task_track_action);
        tvDocid = itemView.findViewById(R.id.tv_task_track_docid);
        // ivViewDoc = itemView.findViewById(R.id.iv_task_track_viewdocument);
        btnViewDoc = itemView.findViewById(R.id.btn_task_track_viewdocument);
        btnReintiate = itemView.findViewById(R.id.btn_task_track_reinitiatework);


    }


}


public class TasktrackListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    Boolean noDoc = false;

    List<TaskTrackList> taskTrackList;


    BottomSheetDialog bottomSheetDialog;
    RecyclerView rvBottomSheetDocumentView;
    List<DocumentListWorkflow> documentListWorkFlow = new ArrayList<>();
    WorkflowDocListAdapter workFlowDoclistAdapter;

    LinearLayout llNofileFound;
    ProgressBar progressBar;
    TextView tvBottomSheetHeading;
    Activity activity;

    public TasktrackListAdapter(List<TaskTrackList> taskTrackList, RecyclerView recyclerView, Activity activity) {


        this.taskTrackList = taskTrackList;
        this.activity = activity;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_track_status_list_item, parent, false);
        return new ListViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        final ListViewHolder listVH = (ListViewHolder) holder;
        listVH.tvTicketId.setText(taskTrackList.get(position).getTicketId());
        listVH.tvTaskStatus.setText(taskTrackList.get(position).getTaskStatus());
        listVH.tvTicketDate.setText(taskTrackList.get(position).getTicketDate());
        listVH.tvDocid.setText(taskTrackList.get(position).getDocid());



        // manoj shakya 31-08-21
        if(taskTrackList.get(position).getDocid().equals("0")){
            listVH.btnViewDoc.setVisibility(View.GONE);
        }

        listVH.btnViewDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //manoj shakya 31-03-21
                showBottomSheetDialog(listVH.tvDocid.getText().toString(),listVH.btnViewDoc ,taskTrackList.get(position).getTaskId());


            }
        });


        listVH.btnReintiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWorkflowId(((ListViewHolder) holder).tvTicketId.getText().toString());

            }
        });


        String taskstatus = listVH.tvTaskStatus.getText().toString().trim();

        if (taskstatus.equalsIgnoreCase("Approved")) {


            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.green_normal));
           // listVH.btnReintiate.setVisibility(View.GONE);
        } else if (taskstatus.equalsIgnoreCase("Rejected")) {


            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.red));
            //listVH.btnReintiate.setVisibility(View.VISIBLE);

        } else if (taskstatus.equalsIgnoreCase("Aborted")) {


            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.red));
          //  listVH.btnReintiate.setVisibility(View.GONE);

        } else if (taskstatus.equalsIgnoreCase("Processed")) {


            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.green_normal));
           // listVH.btnReintiate.setVisibility(View.GONE);

        } else if (taskstatus.equalsIgnoreCase("Complete")) {

            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.green_normal));
          //  listVH.btnReintiate.setVisibility(View.GONE);


        } else if (taskstatus.equalsIgnoreCase("Done")) {


            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.green_normal));
           // listVH.btnReintiate.setVisibility(View.GONE);

        } else if (taskstatus.equalsIgnoreCase("Pending")) {


            ((GradientDrawable) listVH.tvTaskStatus.getBackground()).setColor(activity.getResources().getColor(R.color.light_yellow));
           // listVH.btnReintiate.setVisibility(View.GONE);

        }


        listVH.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, TaskTrackingActivity.class);
                intent.putExtra("ticket_id", listVH.tvTicketId.getText().toString().trim());
                activity.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {

        return taskTrackList.size();
    }


    //manoj shakya 01-04-21
    public void showBottomSheetDialog(String docid ,Button btnDoc , String taskId) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet_workflowdoclist, null);

        bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(view);

        rvBottomSheetDocumentView = bottomSheetDialog.findViewById(R.id.rv_bottom_sheet_workflow_doc_list);
        rvBottomSheetDocumentView.setLayoutManager(new LinearLayoutManager(activity));
        rvBottomSheetDocumentView.setHasFixedSize(true);
        rvBottomSheetDocumentView.setItemViewCacheSize(documentListWorkFlow.size());

        llNofileFound = bottomSheetDialog.findViewById(R.id.ll_workflow_bottom_sheet_doclist_nofilefound);
        progressBar = bottomSheetDialog.findViewById(R.id.progressBar_workflowlist_doclist);
        tvBottomSheetHeading = bottomSheetDialog.findViewById(R.id.tv_bottom_sheet_workflow_heading);
        tvBottomSheetHeading.setText(activity.getString(R.string.document) + " / " + activity.getString(R.string.desciption));

        //manoj shakya 31-03-21
        getDoc(docid,btnDoc ,taskId);


    }

    //manoj shakya 01-04-21
    void getDoc(final String docid,Button btnDoc , String taskId) {

        llNofileFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        rvBottomSheetDocumentView.setVisibility(View.GONE);
        btnDoc.setEnabled(false);
        documentListWorkFlow.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TASK_TRACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("response tracklist", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);


                    if (jsonArray.length() == 0) {

                        progressBar.setVisibility(View.GONE);
                        rvBottomSheetDocumentView.setVisibility(View.GONE);
                        llNofileFound.setVisibility(View.VISIBLE);
                        btnDoc.setEnabled(true);


                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String docname = jsonArray.getJSONObject(i).getString("old_doc_name");
                            String docid = jsonArray.getJSONObject(i).getString("doc_id");
                            String path = jsonArray.getJSONObject(i).getString("doc_path");
                            String extension = jsonArray.getJSONObject(i).getString("doc_extn");
                            String slid = jsonArray.getJSONObject(i).getString("slid");

                            documentListWorkFlow.add(new DocumentListWorkflow(docname, docid, path, extension,slid));

                        }

                        workFlowDoclistAdapter = new WorkflowDocListAdapter(documentListWorkFlow, activity);
                        rvBottomSheetDocumentView.setAdapter(workFlowDoclistAdapter);
                        llNofileFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        rvBottomSheetDocumentView.setVisibility(View.VISIBLE);
                        btnDoc.setEnabled(true);

                    }


                    bottomSheetDialog.show();

                } catch (JSONException e) {
                    e.printStackTrace();

                    btnDoc.setEnabled(true);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                btnDoc.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("docid", docid);
                params.put("userId", MainActivity.userid);

                //manoj shakya 31-03-21
                params.put("taskId",taskId);
                return params;
            }
        };

        VolleySingelton.getInstance(activity).addToRequestQueue(stringRequest);

    }

    void getWorkflowId(String ticketid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.INITIATE_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response tracklist", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.length() == 0) {


                    } else {

                        String wId = jsonObject.getString("workflow_id");
                        String wName = jsonObject.getString("workflow_name");

                        Intent intent = new Intent(activity, IntiateWorkFlowActivity.class);
                        intent.putExtra("workflowID", wId);
                        intent.putExtra("workflowName", wName);

                      /*  workflowId = intent.getStringExtra("workflowID");
                        workflowName = intent.getStringExtra("workflowName");*/

                        activity.startActivity(intent);

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
                params.put("tktid", ticketid);
                return params;
            }
        };

        VolleySingelton.getInstance(activity).addToRequestQueue(stringRequest);


    }



}
