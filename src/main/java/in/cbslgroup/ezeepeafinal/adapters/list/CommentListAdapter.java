package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.model.CommentTaskApproveReject;
import in.cbslgroup.ezeepeafinal.R;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.Viewholder> {

    List<CommentTaskApproveReject> commentList;
    Context context;

    public CommentListAdapter(List<CommentTaskApproveReject> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commment_approve_reject_item, parent, false);
        return new Viewholder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.taskname.setText(commentList.get(position).getTaskname());
        holder.date.setText(commentList.get(position).getDate());
        holder.comment.setText(commentList.get(position).getComment());
        holder.username.setText(commentList.get(position).getName());
        holder.pic.setText(commentList.get(position).getPic());

        String comment = holder.comment.getText().toString();

        if(TextUtils.isEmpty(commentList.get(position).getTaskname())|| commentList.get(position).getTaskname().equalsIgnoreCase("")){


            holder.llTask.setVisibility(View.GONE);


        }

        else{


            holder.llTask.setVisibility(View.VISIBLE);

        }


        if(TextUtils.isEmpty(comment)|| comment.equalsIgnoreCase("")){


            holder.llComment.setVisibility(View.GONE);


        }

        else{


            holder.llComment.setVisibility(View.VISIBLE);

        }



        if(!commentList.get(position).getPic().isEmpty()&&commentList.get(position).getPic()!=null &&!commentList.get(position).getPic().equals("null"))

        {


            byte[] decodedString = Base64.decode(holder.pic.getText().toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.profilepic.setImageBitmap(decodedByte);
            Log.e("mayank","here");

        }



    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {


        TextView username,comment,date,taskname,pic;
        ImageView profilepic;

        LinearLayout llComment,llTask;


        public Viewholder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_comment_task_approve_reject_name);
            comment = itemView.findViewById(R.id.tv_comment_task_approve_reject_comment);
            date = itemView.findViewById(R.id.tv_comment_task_approve_reject_date);
            taskname = itemView.findViewById(R.id.tv_comment_task_approve_reject_taskname);
            pic = itemView.findViewById(R.id.tv_comment_task_approve_reject_pic);

            profilepic = itemView.findViewById(R.id.iv_comment_approve_reject_profilepic);
            llComment = itemView.findViewById(R.id.ll_comment_approved_reject_comment);
            llTask = itemView.findViewById(R.id.ll_comment_approved_reject_taskname);



        }



    }
}
