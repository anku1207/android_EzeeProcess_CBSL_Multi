package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.workflow.AdvancedRequisitionFormActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.ClaimFormTravelExpenseActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.IntiateWorkFlowActivity;
import in.cbslgroup.ezeepeafinal.model.WorkFlowList;
import in.cbslgroup.ezeepeafinal.R;

public class WorkFlowListAdapter extends RecyclerView.Adapter<WorkFlowListAdapter.ViewHolder> {

    List<WorkFlowList> workFlowList = new ArrayList<>();
    Context context;


    public WorkFlowListAdapter(List<WorkFlowList> workFlowList, Context context) {
        this.workFlowList = workFlowList;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkFlowListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workflow_list_items, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull final WorkFlowListAdapter.ViewHolder holder, int position) {

        WorkFlowList item = workFlowList.get(position);

        holder.tvWorkFLowId.setText(item.getWorkFlowId());
        holder.tvWorkFlowName.setText(item.getWorkFlowName());
        holder.llWorkFlowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, holder.tvWorkFlowName.getText(),Toast.LENGTH_SHORT).show();

                if(item.getArf().equals("1")){

                    Intent intent = new Intent(context, AdvancedRequisitionFormActivity.class);
                    intent.putExtra("workflowName",holder.tvWorkFlowName.getText().toString().trim());
                    intent.putExtra("workflowID",holder.tvWorkFLowId.getText().toString().trim());
                    context.startActivity(intent);


                }

                else if (item.getCte().equals("1")){

                    Intent intent = new Intent(context, ClaimFormTravelExpenseActivity.class);
                    intent.putExtra("workflowName",holder.tvWorkFlowName.getText().toString().trim());
                    intent.putExtra("workflowID",holder.tvWorkFLowId.getText().toString().trim());
                    intent.putExtra("isReset",false);
                    context.startActivity(intent);


                }

                else{

                    Intent intent = new Intent(context, IntiateWorkFlowActivity.class);
                    intent.putExtra("workflowName",holder.tvWorkFlowName.getText().toString().trim());
                    intent.putExtra("workflowID",holder.tvWorkFLowId.getText().toString().trim());
                    //manoj shakya 01-04-21
                    intent.putExtra(IntiateWorkFlowActivity.EXTRAS_WORK_FLOW_TYPE,item.getWftype());

                    context.startActivity(intent);


                }



            }
        });






    }

    @Override
    public int getItemCount() {
        return workFlowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWorkFlowName, tvWorkFLowId;
        LinearLayout llWorkFlowItem;

        public ViewHolder(View itemView) {
            super(itemView);

            llWorkFlowItem = itemView.findViewById(R.id.ll_workflow_item_main);
            tvWorkFlowName = itemView.findViewById(R.id.tv_workflow_name);
            tvWorkFLowId = itemView.findViewById(R.id.tv_workflow_id);


        }
    }
}
