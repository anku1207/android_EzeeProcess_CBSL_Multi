package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cbslgroup.ezeepeafinal.ui.activity.visitorpass.VisitorPassActionActivity;
import in.cbslgroup.ezeepeafinal.model.VisitorPass;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;

public class VisitorPassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<VisitorPass> visitorPassList;
    Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public VisitorPassAdapter(List<VisitorPass> visitorPassList, Context context) {
        this.visitorPassList = visitorPassList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM){

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor_item, parent, false);
            return new ItemViewHolder(v);

        }

        else{

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder rvholder, int position) {

       if(rvholder instanceof ItemViewHolder){

           ItemViewHolder holder = (ItemViewHolder) rvholder;

           VisitorPass item = visitorPassList.get(position);
           holder.tvUsername.setText(item.getVisitorName());

           holder.tvDateAndTime.setText(item.getIntime() + " • " + item.getVisitDate());

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   Intent intent = new Intent(context, VisitorPassActionActivity.class);

                   intent.putExtra("Id", item.getId());
                   intent.putExtra("passno", item.getPassno().equals("") || item.getPassno().equals("null") ? "-" : item.getPassno());
                   intent.putExtra("visitor_name", item.getVisitorName().equals("") || item.getVisitorName().equals("null") ? "-" : item.getVisitorName());
                   intent.putExtra("mobileno", item.getMobileno().equals("") || item.getMobileno().equals("null") ? "-" : item.getMobileno());
                   intent.putExtra("companyname", item.getCompanyname().equals("") || item.getCompanyname().equals("null") ? "-" : item.getCompanyname());
                   intent.putExtra("deptId", item.getDeptId());
                   intent.putExtra("meetingwith", item.getMeetingwith().equals("") || item.getMeetingwith().equals("null") ? "-" : item.getMeetingwith());
                   intent.putExtra("noofvisitors", item.getNoofvisitors().equals("") || item.getNoofvisitors().equals("null") ? "-" : item.getNoofvisitors());
                   intent.putExtra("visit_purpose", item.getVisitPurpose().equals("") || item.getVisitPurpose().equals("null") ? "-" : item.getVisitPurpose());
                   intent.putExtra("visit_date", item.getVisitDate());
                   intent.putExtra("intime", item.getIntime().equals("") || item.getIntime().equals("null") ? "-" : item.getIntime());
                   intent.putExtra("outtime", item.getOuttime().equals("") || item.getOuttime().equals("null") ? "-" : item.getOuttime());
                   intent.putExtra("remark", item.getRemark());
                   intent.putExtra("pic", item.getPic());
                   intent.putExtra("actionby", item.getActionby());
                   intent.putExtra("department_name", item.getDepartmentName().equals("") || item.getDepartmentName().equals("null") ? "-" : item.getDepartmentName());
                   intent.putExtra("employeename", item.getEmployeename());
                   intent.putExtra("update_meeting_status", item.getUpdateMeetingStatus());
                   intent.putExtra("addOutTime", item.getAddOutTime());
                   intent.putExtra("status", item.getStatus());

                   context.startActivity(intent);

               }
           });


           switch (item.getStatus()) {

               case "Done":
                   ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.md_green_600));

                   holder.tvStatus.setText(context.getString(R.string.done));

                   break;

               case "Awaited":
                   ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.md_blue_500));

                   holder.tvStatus.setText(context.getString(R.string.awaited));
                   break;

               case "Cancel":
                   ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.md_red_600));

                   holder.tvStatus.setText(context.getString(R.string.cancel));
                   break;


               case "Allow":
                   ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.md_green_600));

                   holder.tvStatus.setText(context.getString(R.string.allow));
                   break;

               case "Pending":
                   ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.md_orange_500));

                   holder.tvStatus.setText(context.getString(R.string.pending));
                   break;
           }

           if(item.getPic().trim().equals("null")||item.getPic()==null||item.getPic().trim().isEmpty()){

               holder.civPic.setImageResource(R.drawable.default_user);
           }

           else{

               Glide.with(context)
                       .load(ApiUrl.BASE_URL_ROOT + item.getPic())
                       .placeholder(R.drawable.default_user)
                       .error(R.drawable.default_user)
                       .thumbnail(0.5f).listener(new RequestListener<Drawable>() {
                   @Override
                   public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                       //holder.civPic.setImageResource(R.drawable.ic_perm_identity_white_24dp);
                       Log.e("Glide exception ", String.valueOf(e));
                       return false;
                   }

                   @Override
                   public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                       return false;
                   }
               }).into( holder.civPic);

           }

       }

       else if(rvholder instanceof  ProgressViewHolder){

           //show progress bar

       }




    }

    @Override
    public int getItemViewType(int position) {
        return visitorPassList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return visitorPassList == null ? 0 : visitorPassList.size();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {



        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvDateAndTime, tvStatus;
        CircleImageView civPic;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_visitor_name);
            tvStatus = itemView.findViewById(R.id.tv_visitor_status);
            tvDateAndTime = itemView.findViewById(R.id.tv_visitor_datetime);
            civPic = itemView.findViewById(R.id.civ_visitor_image);


        }
    }
}
