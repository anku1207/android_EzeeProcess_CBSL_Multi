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

import in.cbslgroup.ezeepeafinal.model.SharedFileStatus;
import in.cbslgroup.ezeepeafinal.R;

public class SharedFileStatusAdapter extends RecyclerView.Adapter<SharedFileStatusAdapter.ViewHolder> {

    List<SharedFileStatus> sharedFileStatusList;
    Context context;

    public SharedFileStatusAdapter(List<SharedFileStatus> sharedFileStatusList, Context context) {
        this.sharedFileStatusList = sharedFileStatusList;
        this.context = context;
    }

    @NonNull
    @Override
    public SharedFileStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_files_status_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedFileStatusAdapter.ViewHolder holder, int position) {

        holder.tvStatus.setText(sharedFileStatusList.get(position).getText());
        holder.ivStatus.setImageResource(sharedFileStatusList.get(position).getStatusImage());

    }

    @Override
    public int getItemCount() {
        return sharedFileStatusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvStatus;
        ImageView ivStatus;

        public ViewHolder(View itemView) {

            super(itemView);

            tvStatus = itemView.findViewById(R.id.tv_shared_files_status);
            ivStatus = itemView.findViewById(R.id.iv_shared_files_status);


        }
    }
}
