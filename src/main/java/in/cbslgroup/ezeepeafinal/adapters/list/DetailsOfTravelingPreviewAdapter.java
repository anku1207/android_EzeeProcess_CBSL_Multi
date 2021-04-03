package in.cbslgroup.ezeepeafinal.adapters.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import in.cbslgroup.ezeepeafinal.model.DetailsOfTravel;
import in.cbslgroup.ezeepeafinal.R;

public class DetailsOfTravelingPreviewAdapter  extends RecyclerView.Adapter<DetailsOfTravelingPreviewAdapter.ViewHolder> {

    List<DetailsOfTravel> detailsOfTravelList;

    public DetailsOfTravelingPreviewAdapter(List<DetailsOfTravel> detailsOfTravelList) {
        this.detailsOfTravelList = detailsOfTravelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_of_travel_preview_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DetailsOfTravel item = detailsOfTravelList.get(position);
        holder.tieSno.setText((position+1)+"");
        holder.tieMode.setText(item.getMode());
        holder.tieDateOfTravel.setText(item.getDateOfTravel());
        holder.tieDest.setText(item.getDestFrom()+"-"+item.getDestTo());
        holder.tieClass.setText(item.getmClass());
        holder.tieRemarks.setText(item.getRemarks());
        holder.tieAdvReq.setText(item.getAdvReq());

    }

    @Override
    public int getItemCount() {

        return detailsOfTravelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

       // TextInputLayout tilSno, tilDateOfTravel, tilMode, tilDestTo, tilDestFrom, tilClass, tilRemarks, tilAdvReq;
        TextInputEditText tieSno, tieDateOfTravel, tieMode, tieDest,tieClass, tieRemarks, tieAdvReq;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tieSno = itemView.findViewById(R.id.tie_details_preview_sno);
            tieDateOfTravel = itemView.findViewById(R.id.tie_details_preview_date_of_travel);
            tieAdvReq = itemView.findViewById(R.id.tie_details_preview__adv_req);
            tieClass = itemView.findViewById(R.id.tie_details_preview_class);
            tieDest = itemView.findViewById(R.id.tie_details_preview_destination);
            tieMode = itemView.findViewById(R.id.tie_details_preview_mode);
            tieRemarks = itemView.findViewById(R.id.tie_details_preview_remarks);

        }
    }
}
