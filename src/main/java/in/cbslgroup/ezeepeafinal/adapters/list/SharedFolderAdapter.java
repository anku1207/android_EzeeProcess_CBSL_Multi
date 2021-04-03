package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.model.response.sharedfolder.SharedFolderListItem;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.FileViewActivity;

public class SharedFolderAdapter extends RecyclerView.Adapter<SharedFolderAdapter.ViewHolder> {

    List<SharedFolderListItem> sharedFolderListItemList;
    Context context;
    String mode;

    public SharedFolderAdapter(List<SharedFolderListItem> sharedFolderListItemList, Context context,String mode) {
        this.sharedFolderListItemList = sharedFolderListItemList;
        this.context = context;
        this.mode = mode;
    }


    OnViewMoreClickListener onViewMoreClickListener;

    public void setOnViewMoreClickListener(OnViewMoreClickListener onViewMoreClickListener) {
        this.onViewMoreClickListener = onViewMoreClickListener;
    }


    public interface OnViewMoreClickListener{

        void onUndoClickListener(int pos,String id, String slName, String slid, String userId);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.shared_folder_item_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedFolderListItem listitem = sharedFolderListItemList.get(position);

        holder.tvFolderName.setText(listitem.getSlName());
        holder.tvInfo.setText(listitem.getFirstName()+" "+listitem.getLastName()+" • "+listitem.getSharedDate());
        holder.ivMore.setVisibility(View.GONE);

        if(mode.equalsIgnoreCase("share_folder")){

            holder.ivMore.setVisibility(View.VISIBLE);

            holder.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(context, holder.ivMore);
                    popup.inflate(R.menu.shared_folder_menu);

                    //for show hide items
                    // popup.menu.findItem(R.id.recycle_bin_restore).setVisible(false)
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId() == R.id.action_shared_folder_undo){

                                onViewMoreClickListener.onUndoClickListener(position,listitem.getId(),listitem.getSlName(),listitem.getSlId(),listitem.getShareWith());

                            }

                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }

        else{

            holder.ivMore.setVisibility(View.GONE);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, FileViewActivity.class);
                intent.putExtra("slid", listitem.getSlId());
                intent.putExtra("foldername", listitem.getSlName());
                intent.putExtra("mode", "share_folder");
                ((Activity) context).startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return sharedFolderListItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFolderName,tvInfo;
        ImageView ivMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvFolderName = itemView.findViewById(R.id.tv_share_folder_name);
            tvInfo = itemView.findViewById(R.id.tv_share_folder_info);
            ivMore = itemView.findViewById(R.id.iv_share_folder_view_more);

        }
    }

    /***************************************************************
     *       Helper Methods For the Share Folder Adapter           *
     ***************************************************************/

    public void removeItem(int pos){

         if(!sharedFolderListItemList.isEmpty()){
             sharedFolderListItemList.remove(pos);
             notifyItemRemoved(pos);
         }


    }

}
