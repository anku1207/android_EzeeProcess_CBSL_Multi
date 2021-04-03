package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import in.cbslgroup.ezeepeafinal.model.MetaBottomSheet;
import in.cbslgroup.ezeepeafinal.R;

public class MetaViewBottomSheetAdapter extends RecyclerView.Adapter<MetaViewBottomSheetAdapter.ViewHolder> {

    List<MetaBottomSheet> metaBottomSheetList;
    Context context;


    public MetaViewBottomSheetAdapter(List<MetaBottomSheet> metaBottomSheetList, Context context) {
        this.metaBottomSheetList = metaBottomSheetList;
        this.context = context;
    }

    @Override
    public MetaViewBottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomsheet_metaview_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MetaViewBottomSheetAdapter.ViewHolder holder, int position) {

        holder.tvMetaname.setText(metaBottomSheetList.get(position).getMetaname());


    }

    @Override
    public int getItemCount() {
        return metaBottomSheetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMetaname;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMetaname = itemView.findViewById(R.id.bottom_sheet_tv_metadataname);
        }
    }
}
