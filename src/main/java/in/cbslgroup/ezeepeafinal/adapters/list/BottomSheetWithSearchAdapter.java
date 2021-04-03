package in.cbslgroup.ezeepeafinal.adapters.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.model.BottomSheetWithSearch;
import in.cbslgroup.ezeepeafinal.R;

public class BottomSheetWithSearchAdapter extends RecyclerView.Adapter<BottomSheetWithSearchAdapter.Viewholder> implements Filterable {

    public BottomSheetWithSearchAdapter(List<BottomSheetWithSearch> list) {
        this.list = list;
        this.filteredlist = list;
    }

    List<BottomSheetWithSearch> list;
    List<BottomSheetWithSearch> filteredlist;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    OnClickListener listener;

    public interface OnClickListener {

        void onItemClickListerner(BottomSheetWithSearch obj);

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredlist = list;
                } else {

                    List<BottomSheetWithSearch> filteredList = new ArrayList<>();
                    for (BottomSheetWithSearch row : list) {

                        if (row.getValue().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredlist = filteredList;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filteredlist = (ArrayList<BottomSheetWithSearch>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomsheet_with_search_item, parent, false);
        return new Viewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.tvItem.setText(filteredlist.get(position).getValue());

    }

    @Override
    public int getItemCount() {
        return filteredlist.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tv_bs_with_search_item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClickListerner(filteredlist.get(getAdapterPosition()));


                }
            });


        }
    }

    void clearFilteredList(){

        filteredlist.clear();
        notifyDataSetChanged();

    }

    void clearMainList(){

        list.clear();
        notifyDataSetChanged();
    }
}
