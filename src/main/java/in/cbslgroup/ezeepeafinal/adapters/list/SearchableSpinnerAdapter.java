package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.R;


public class SearchableSpinnerAdapter extends RecyclerView.Adapter<SearchableSpinnerAdapter.ViewHolder> {

    Context context;
    List<User> userList;
    OnItemClickListener onItemClickListener;



    public SearchableSpinnerAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;


    }

    public interface OnItemClickListener {

        void onMemberClickListener(String username, String userid);


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }




    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == userList.size() - 1) {

            holder.vDecorator.setVisibility(View.GONE);
        }

        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               onItemClickListener.onMemberClickListener(user.getUsername(),user.getUserid());

                //Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();

            }
        });

        if(user.getDrawableId()!=0){

            holder.imageView.setImageResource(user.getDrawableId());

        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        View vDecorator;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_user_image);
            tvUsername = itemView.findViewById(R.id.tv_member_username);
            vDecorator = itemView.findViewById(R.id.view_member_decorator);
        }
    }



}
