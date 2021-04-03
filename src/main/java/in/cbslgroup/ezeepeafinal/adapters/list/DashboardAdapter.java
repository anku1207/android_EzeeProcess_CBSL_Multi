package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.audittrail.AuditTrailStorageActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.audittrail.AuditTrailUserActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.search.MetaDataSearchActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.visitorpass.VisitorPassActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.AuditTrailWorkFlowActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.workflow.InTrayActivity;
import in.cbslgroup.ezeepeafinal.model.DashBoard;
import in.cbslgroup.ezeepeafinal.R;


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    List<DashBoard> dashBoardList;
    Context context;
    private int lastPosition = -1;


   public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{

        void onDmsClicked();
        void onInTrayClicked();
        void onAuditTrailUserClicked();
        void onAuditTrailStorageClicked();
        void onAuditTrailWorkflowClicked();
        void onMetaDataSearchClicked();
        void onVisitorPassClicked();

    };

    public DashboardAdapter(List<DashBoard> dashBoardList, Context context) {
        this.dashBoardList = dashBoardList;
        this.context = context;
    }



    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_card_layout, viewGroup, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder viewHolder, int i) {

        viewHolder.civIcon.setImageDrawable(dashBoardList.get(i).getImage());
        viewHolder.tvHeading.setText(dashBoardList.get(i).getHeading());
        viewHolder.tvSubHeading.setText(dashBoardList.get(i).getSubheading());

        String labelName = viewHolder.tvHeading.getText().toString();


        // Here you apply the animation when the view is bound
        setAnimation(viewHolder.itemView, i);


        viewHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(labelName.equalsIgnoreCase(context.getString(R.string.my_dms)))
                {
//                    Intent intent = new Intent(context, DmsActivity.class);
//                    intent.putExtra("slid", MainActivity.slid_session);
//                    context.startActivity(intent);

                   onItemClickListener.onDmsClicked();

                }

                else if(labelName.equalsIgnoreCase(context.getString(R.string.in_tray)))
                {


//                    Intent intent = new Intent(context, InTrayActivity.class);
//                    context.startActivity(intent);

                    onItemClickListener.onInTrayClicked();

                } else if(labelName.equalsIgnoreCase(context.getString(R.string.user_report)))
                {


//                    Intent intent = new Intent(context, AuditTrailUserActivity.class);
//                    context.startActivity(intent);

                    onItemClickListener.onAuditTrailUserClicked();


                } else if(labelName.equalsIgnoreCase (context.getString(R.string.storage_report)))
                {

//
//                    Intent intent = new Intent(context, AuditTrailStorageActivity.class);
//                    context.startActivity(intent);

                    onItemClickListener.onAuditTrailStorageClicked();

                } else if(labelName.equalsIgnoreCase(context.getString(R.string.workflow_audit)))
                {


//                    Intent intent = new Intent(context, AuditTrailWorkFlowActivity.class);
//                    context.startActivity(intent);

                    onItemClickListener.onAuditTrailWorkflowClicked();

                } else if(labelName.equalsIgnoreCase(context.getString(R.string.metadata_search)))
                {


//                    Intent intent = new Intent(context, MetaDataSearchActivity.class);
//                    context.startActivity(intent);

                    onItemClickListener.onMetaDataSearchClicked();

                }
                else if(labelName.equalsIgnoreCase(context.getString(R.string.visitor_pass)))
                {


//                    Intent intent = new Intent(context, VisitorPassActivity.class);
//                    context.startActivity(intent);

                    onItemClickListener.onVisitorPassClicked();

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return dashBoardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeading,tvSubHeading;
        CircleImageView civIcon;
        CardView cvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvHeading =  itemView.findViewById(R.id.tv_dashboard_heading);
            tvSubHeading =  itemView.findViewById(R.id.tv_dashboard_subheading);
            civIcon =  itemView.findViewById(R.id.civ_dashboard_icon);
            cvItem = itemView.findViewById(R.id.cv_dashboard_item);


        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
