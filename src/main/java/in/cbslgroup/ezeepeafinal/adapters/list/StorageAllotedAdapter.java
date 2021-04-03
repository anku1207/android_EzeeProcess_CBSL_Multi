package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.model.StorageAlloted;
import in.cbslgroup.ezeepeafinal.R;

public class StorageAllotedAdapter extends RecyclerView.Adapter<StorageAllotedAdapter.Viewholder> {



    List<StorageAlloted> storageAllotedList;
    Context context;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    OnClickListener onClickListener;

    public interface OnClickListener{

        void onStorageClicked(String slid, String storagename);

    }


    public StorageAllotedAdapter(List<StorageAlloted> storageAllotedList, Context context) {
        this.storageAllotedList = storageAllotedList;
        this.context = context;
    }


    @NonNull
    @Override
    public StorageAllotedAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.storage_alloted_layout,viewGroup,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageAllotedAdapter.Viewholder viewholder, int i) {

        viewholder.tvStorageAlloted.setText(storageAllotedList.get(i).getSlName());
        viewholder.tvStorageAlloted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                onClickListener.onStorageClicked(storageAllotedList.get(i).getSlid(),storageAllotedList.get(i).getSlName());
                Log.e("info->","slid:"+storageAllotedList.get(i).getSlid()+"\n slname:"+storageAllotedList.get(i).getSlName());


            }
        });

    }

    @Override
    public int getItemCount() {
        return storageAllotedList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView tvStorageAlloted;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvStorageAlloted = itemView.findViewById(R.id.tv_storage_alloted);
        }
    }
}
