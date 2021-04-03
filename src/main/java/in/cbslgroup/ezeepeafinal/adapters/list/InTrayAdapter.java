package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.TaskInProcessActivity;
import in.cbslgroup.ezeepeafinal.model.DocumentListWorkflow;
import in.cbslgroup.ezeepeafinal.model.InTray;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class InTrayAdapter extends RecyclerView.Adapter<InTrayAdapter.ViewHolder> {

    List<InTray> inTrayList;
    Context context;


    BottomSheetDialog bottomSheetDialog;
    RecyclerView rvBottomSheetDocumentView;
    List<DocumentListWorkflow> documentListWorkFlow = new ArrayList<>();
    WorkflowDocListAdapter workFlowDoclistAdapter;

    LinearLayout llNofileFound;
    ProgressBar progressBar;
    TextView tvBottomSheetHeading;

    SessionManager sessionManager;

    public InTrayAdapter(List<InTray> inTrayList, Context context) {
        this.inTrayList = inTrayList;
        this.context = context;

        sessionManager = new SessionManager(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_tray_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        InTray item = inTrayList.get(position);
        holder.tvTaskname.setText(item.getTaskname());
        holder.tvDeadline.setText(item.getDeadLine());
        holder.tvAssignedDate.setText(item.getAssignDate());
        holder.tvPriority.setText(item.getPriority());
        holder.tvStatus.setText(item.getStatus());
        holder.tvStartedBy.setText(item.getStartedBy());
        holder.tvDocid.setText(item.getDocid());
        holder.tvTaskid.setText(item.getTaskid());
        holder.tvWarning.setText(item.getWarning());

        String warning = holder.tvWarning.getText().toString();
        String deadline = holder.tvDeadline.getText().toString();
        String docid = holder.tvDocid.getText().toString();

        Log.e("doc id in tray 1", docid);

        String lang = sessionManager.getLanguage();

        if (deadline.equalsIgnoreCase("0 Secounds")) {

              if(!lang.equalsIgnoreCase("en")){

                holder.tvDeadline.setText("0 "+context.getString(R.string.secounds));

            }
            
            holder.tvDeadline.setTextColor(Color.RED);

        } else {

            holder.tvDeadline.setTextColor(Color.BLACK);
        }


        if (warning.equalsIgnoreCase("null")) {

            holder.llWarning.setVisibility(View.GONE);

        } else {

            holder.llWarning.setVisibility(View.VISIBLE);
        }


        holder.btnViewDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheetDialog(holder.tvDocid.getText().toString());


            }
        });


        String taskstatus = holder.tvStatus.getText().toString().trim();
        String priority = holder.tvPriority.getText().toString().trim();




        //priortiy
        if (priority.equalsIgnoreCase("No Task Priority")) {

              if(!lang.equalsIgnoreCase("en")){

                holder.tvPriority.setText(context.getString(R.string.no_task_priority));

            }

            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.red));


        }

        if (priority.equalsIgnoreCase("Normal")) {

              if(!lang.equalsIgnoreCase("en")){

                holder.tvPriority.setText(context.getString(R.string.normal));

            }

            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.colorPrimary));

        } else if (priority.equalsIgnoreCase("Urgent")) {


            if(!lang.equalsIgnoreCase("en")){

                holder.tvPriority.setText(context.getString(R.string.urgent));

            }

            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.red));


        } else if (priority.equalsIgnoreCase("Medium")) {

            if(!lang.equalsIgnoreCase("en")){

                holder.tvPriority.setText(context.getString(R.string.medium));

            }

            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.light_yellow));


        }


        //taskstatus
        if (taskstatus.equalsIgnoreCase("Approved")) {

            if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.approved));

            }

            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        } else if (taskstatus.equalsIgnoreCase("Rejected")) {

              if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.rejected));

            }

            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.red));


        } else if (taskstatus.equalsIgnoreCase("Aborted")) {

            if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.aborted));

            }

            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.red));


        } else if (taskstatus.equalsIgnoreCase("Processed")) {

            if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.processed));

            }

            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));


        } else if (taskstatus.equalsIgnoreCase("Complete")) {

            if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.complete));

            }


            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));


        } else if (taskstatus.equalsIgnoreCase("Done")) {

              if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.done));

            }

            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));


        } else if (taskstatus.equalsIgnoreCase("Pending")) {


              if(!lang.equalsIgnoreCase("en")){

                holder.tvStatus.setText(context.getString(R.string.pending));

            }


            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.light_yellow));


        }


        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("approv_1", String.valueOf(item.getWarning().equalsIgnoreCase("null") || item.getWarning() == null));


                if (taskstatus.equalsIgnoreCase("Approved") && !(item.getWarning().equalsIgnoreCase("null") || item.getWarning() == null)) {

                    //Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, TaskInProcessActivity.class);
                    intent.putExtra("taskid", holder.tvTaskid.getText().toString());
                    intent.putExtra("taskname", holder.tvTaskname.getText().toString());
                    intent.putExtra("taskStatus", holder.tvStatus.getText().toString());
                    context.startActivity(intent);


                } else if (taskstatus.equalsIgnoreCase("Approved")) {

                    Toast.makeText(context, "Cant perform action on Approved Tasks", Toast.LENGTH_SHORT).show();


                } else {

                    //Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, TaskInProcessActivity.class);
                    intent.putExtra("taskid", holder.tvTaskid.getText().toString());
                    intent.putExtra("taskname", holder.tvTaskname.getText().toString());
                    intent.putExtra("taskStatus", holder.tvStatus.getText().toString());
                    context.startActivity(intent);


                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return inTrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWarning, tvTaskname, tvDeadline, tvPriority, tvStatus, tvAssignedDate, tvStartedBy, tvDocid, tvTaskid;
        ImageView ivAction;
        Button btnViewDoc;
        LinearLayout llWarning;

        public ViewHolder(View itemView) {
            super(itemView);

            llWarning = itemView.findViewById(R.id.ll_in_tray_item_warning);
            tvWarning = itemView.findViewById(R.id.tv_in_tray_item_warning);

            tvTaskname = itemView.findViewById(R.id.tv_in_tray_item_taskname);
            tvDeadline = itemView.findViewById(R.id.tv_in_tray_item_deadline);
            tvAssignedDate = itemView.findViewById(R.id.tv_in_tray_item_assigned_date);
            tvPriority = itemView.findViewById(R.id.tv_in_tray_item_priority);
            tvStatus = itemView.findViewById(R.id.tv_in_tray_item_status);
            tvStartedBy = itemView.findViewById(R.id.tv_in_tray_item_started_by);
            tvDocid = itemView.findViewById(R.id.tv_in_tray_item_docid);
            tvTaskid = itemView.findViewById(R.id.tv_in_tray_item_taskid);

            ivAction = itemView.findViewById(R.id.iv_in_tray_item_action);

            btnViewDoc = itemView.findViewById(R.id.btn_in_tray_viewdocument);


        }
    }

    public void showBottomSheetDialog(String docid) {


        // View view = context.getLayoutInflater().inflate(R.layout.bottom_sheet_workflowdoclist, null);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet_workflowdoclist, null);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        rvBottomSheetDocumentView = bottomSheetDialog.findViewById(R.id.rv_bottom_sheet_workflow_doc_list);
        rvBottomSheetDocumentView.setLayoutManager(new LinearLayoutManager(context));
        rvBottomSheetDocumentView.setHasFixedSize(true);
        rvBottomSheetDocumentView.setItemViewCacheSize(documentListWorkFlow.size());

        llNofileFound = bottomSheetDialog.findViewById(R.id.ll_workflow_bottom_sheet_doclist_nofilefound);
        progressBar = bottomSheetDialog.findViewById(R.id.progressBar_workflowlist_doclist);
        tvBottomSheetHeading = bottomSheetDialog.findViewById(R.id.tv_bottom_sheet_workflow_heading);
        tvBottomSheetHeading.setText(context.getString(R.string.document) + " / " + context.getString(R.string.desciption));

        getDoc(docid);


        bottomSheetDialog.show();


    }

    void getDoc(final String docid) {

        llNofileFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        rvBottomSheetDocumentView.setVisibility(View.GONE);

        Log.e("docid in tray", docid);

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
                        bottomSheetDialog.show();
                        //Toast.makeText(context, "No Document Found", Toast.LENGTH_SHORT).show();

                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String docname = jsonArray.getJSONObject(i).getString("old_doc_name");
                            String docid = jsonArray.getJSONObject(i).getString("doc_id");
                            String path = jsonArray.getJSONObject(i).getString("doc_path");
                            String extension = jsonArray.getJSONObject(i).getString("doc_extn");
                            String slid = jsonArray.getJSONObject(i).getString("slid");

                            documentListWorkFlow.add(new DocumentListWorkflow(docname, docid, path, extension, slid));

                        }

                        workFlowDoclistAdapter = new WorkflowDocListAdapter(documentListWorkFlow, context);
                        rvBottomSheetDocumentView.setAdapter(workFlowDoclistAdapter);
                        llNofileFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        rvBottomSheetDocumentView.setVisibility(View.VISIBLE);


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
                params.put("docid", docid);
                params.put("userId", MainActivity.userid);
                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }


}
