package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

import in.cbslgroup.ezeepeafinal.model.ClaimBoardingAndLodging;
import in.cbslgroup.ezeepeafinal.R;

public class ClaimBoardingAndLodgingExpensesAdapter extends RecyclerView.Adapter<ClaimBoardingAndLodgingExpensesAdapter.Viewholder> {


    List<ClaimBoardingAndLodging> claimBoardingAndLodgingList;
    Context context;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    private OnClickListener listener;

    public interface OnClickListener {

        void onAddButtonClicked(int pos);

        void onMinusButtonClicked(int pos);

        void getTotalSanctionedAmt(int total);

        void getTotalClaimAmt(int total);


    }

    public ClaimBoardingAndLodgingExpensesAdapter(List<ClaimBoardingAndLodging> claimBoardingAndLodgingList, Context context) {
        this.claimBoardingAndLodgingList = claimBoardingAndLodgingList;
        this.context = context;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_form_travel_expense_boarding_and_lodging_add_more_layout, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        ClaimBoardingAndLodging item = claimBoardingAndLodgingList.get(position);
        Log.e("isSanctioned",item.getSanctioned()+"");



            if (position == 0) {

                holder.ivAdd.setVisibility(View.VISIBLE);
                holder.ivMinus.setVisibility(View.GONE);

            } else {

                holder.ivAdd.setVisibility(View.GONE);
                holder.ivMinus.setVisibility(View.VISIBLE);

            }

            holder.ivMinus.setOnClickListener(

                    view -> listener.onMinusButtonClicked(position)
            );


            holder.ivAdd.setOnClickListener(

                    view -> listener.onAddButtonClicked(position)
            );

            holder.tieSno.setText(String.valueOf(position + 1));
            holder.tieDate.setText(item.getDate());
            holder.tieBoarding.setText(item.getBoarding());
            holder.tieLoadging.setText(item.getLoadging());
            holder.tieClaimAmt.setText(item.getClaimAmt());
            holder.tieSancAmt.setText(item.getSancAmt());
            holder.tieRemarks.setText(item.getRemarks());

            holder.tieBoarding.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    claimBoardingAndLodgingList.get(position).setBoarding(holder.tieBoarding.getText().toString());

                    int boarding = Integer.parseInt(claimBoardingAndLodgingList.get(position).getBoarding().equals("") ? "0" : claimBoardingAndLodgingList.get(position).getBoarding());
                    int lodging = Integer.parseInt(claimBoardingAndLodgingList.get(position).getLoadging().equals("") ? "0" : claimBoardingAndLodgingList.get(position).getLoadging());
                    int claimAmt = boarding + lodging;

                    claimBoardingAndLodgingList.get(position).setClaimAmt(String.valueOf(claimAmt));
                    holder.tieClaimAmt.setText(String.valueOf(claimAmt));

                    listener.getTotalClaimAmt(getTotalClaimAmount());


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            holder.tieLoadging.addTextChangedListener(new TextWatcher() {
                @Override

                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    claimBoardingAndLodgingList.get(position).setLoadging(holder.tieLoadging.getText().toString());

                    int boarding = Integer.parseInt(claimBoardingAndLodgingList.get(position).getBoarding().equals("") ? "0" : claimBoardingAndLodgingList.get(position).getBoarding());
                    int lodging = Integer.parseInt(claimBoardingAndLodgingList.get(position).getLoadging().equals("") ? "0" : claimBoardingAndLodgingList.get(position).getLoadging());
                    int claimAmt = boarding + lodging;

                    claimBoardingAndLodgingList.get(position).setClaimAmt(String.valueOf(claimAmt));
                    holder.tieClaimAmt.setText(String.valueOf(claimAmt));

                    listener.getTotalClaimAmt(getTotalClaimAmount());


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



        if(item.getSanctioned()!=null && item.getSanctioned()==true){

            holder.ivAdd.setVisibility(View.GONE);
            holder.ivMinus.setVisibility(View.GONE);

            holder.tieSno.setEnabled(false);
            holder.tieDate.setEnabled(false);
            holder.tieBoarding.setEnabled(false);
            holder.tieLoadging.setEnabled(false);
            holder.tieClaimAmt.setEnabled(false);
            holder.tieRemarks.setEnabled(false);

            holder.tieSancAmt.setEnabled(true);

        }





    }

    @Override
    public int getItemCount() {

        return claimBoardingAndLodgingList.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView ivAdd, ivMinus;

        TextInputLayout tilSno, tilBoarding, tilLoadging, tilDate, tilRemarks, tilClaimAmt, tilSancAmt;
        TextInputEditText tieSno, tieBoarding, tieLoadging, tieDate, tieRemarks, tieClaimAmt, tieSancAmt;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ivAdd = itemView.findViewById(R.id.iv_claim_boarding_lodging_add);
            ivMinus = itemView.findViewById(R.id.iv_claim_boarding_lodging_minus);

            tieSno = itemView.findViewById(R.id.tie_claim_boarding_lodging_sno);
            tieBoarding = itemView.findViewById(R.id.tie_claim_boarding_lodging_boarding);
            tieLoadging = itemView.findViewById(R.id.tie_claim_boarding_lodging_loadging);
            tieDate = itemView.findViewById(R.id.tie_claim_boarding_lodging_date_of_travel);
            tieClaimAmt = itemView.findViewById(R.id.tie_claim_boarding_lodging_total_claim_amt);
            tieSancAmt = itemView.findViewById(R.id.tie_claim_boarding_lodging_total_sanc_amt);
            tieRemarks = itemView.findViewById(R.id.tie_claim_boarding_lodging_remarks);


            tilSno = itemView.findViewById(R.id.til_claim_boarding_lodging_sno);
            tilBoarding = itemView.findViewById(R.id.til_claim_boarding_lodging_boarding);
            tilLoadging = itemView.findViewById(R.id.til_claim_boarding_lodging_loadging);
            tilDate = itemView.findViewById(R.id.til_claim_boarding_lodging_date_of_travel);
            tilClaimAmt = itemView.findViewById(R.id.til_claim_boarding_lodging_total_claim_amt);
            tilSancAmt = itemView.findViewById(R.id.til_claim_boarding_lodging_total_sanc_amt);
            tilRemarks = itemView.findViewById(R.id.til_claim_boarding_lodging_remarks);


            tieDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                           // String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (month < 10 ? "0" + month : month) + "-" + year;
                            String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;

                            claimBoardingAndLodgingList.get(getAdapterPosition()).setDate(dt);
                            notifyDataSetChanged();

                        }
                    }, mYear, mMonth, mDay).show();
                }
            });


            tieSno.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    claimBoardingAndLodgingList.get(getAdapterPosition()).setSno(tieSno.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

//            tieFrom.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//                    claimBoardingAndLodgingList.get(getAdapterPosition()).setBoarding(tieFrom.getText().toString());
//                    listener.getTotalClaimAmt(getTotalClaimAmount());
//
//                    //      int claimAmt = Integer.parseInt(claimBoardingAndLodgingList.get(getAdapterPosition()).getBoarding()) +
////                Integer.parseInt(claimBoardingAndLodgingList.get(getAdapterPosition()).getLoadging());
////
////                    claimBoardingAndLodgingList.get(getAdapterPosition()).setClaimAmt(String.valueOf(claimAmt));
//
//                    // notifyDataSetChanged();
//
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });


//            tieTo.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//                    claimBoardingAndLodgingList.get(getAdapterPosition()).setLoadging(tieTo.getText().toString());
//
//                    int claimAmt = Integer.parseInt(claimBoardingAndLodgingList.get(getAdapterPosition()).getBoarding()) +
//                            Integer.parseInt(claimBoardingAndLodgingList.get(getAdapterPosition()).getLoadging());
//
//                    claimBoardingAndLodgingList.get(getAdapterPosition()).setClaimAmt(String.valueOf(claimAmt));
//
//                    listener.getTotalClaimAmt(getTotalClaimAmount());
//
//
//                    // notifyDataSetChanged();
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });

            tieRemarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    claimBoardingAndLodgingList.get(getAdapterPosition()).setRemarks(tieRemarks.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieClaimAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    claimBoardingAndLodgingList.get(getAdapterPosition()).setClaimAmt(tieClaimAmt.getText().toString());
                    listener.getTotalClaimAmt(getTotalClaimAmount());
                    //notifyDataSetChanged();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieSancAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    claimBoardingAndLodgingList.get(getAdapterPosition()).setSancAmt(tieSancAmt.getText().toString());
                    listener.getTotalSanctionedAmt(getTotalSanctioned());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }
    }

    public List<ClaimBoardingAndLodging> getList(){

        return claimBoardingAndLodgingList;
    }

    public void addItem(ClaimBoardingAndLodging claimBoardingAndLodging, int pos) {

        claimBoardingAndLodgingList.add(claimBoardingAndLodging);
        notifyDataSetChanged();
        Log.e("list_size_ad", claimBoardingAndLodgingList.size() + "");
    }

    public void removeItem(int pos) {

        claimBoardingAndLodgingList.remove(pos);
        notifyDataSetChanged();
        Log.e("list_size_rm", claimBoardingAndLodgingList.size() + "");

    }

    public int getTotalClaimAmount() {

        int sum = 0;

        for (ClaimBoardingAndLodging d :
                claimBoardingAndLodgingList) {

            sum += Integer.parseInt(d.getClaimAmt().equals("") ? "0" : d.getClaimAmt());

        }
        return sum;

    }


    public int getTotalSanctioned() {

        int sum = 0;

        for (ClaimBoardingAndLodging d :
                claimBoardingAndLodgingList) {

            sum += Integer.parseInt(d.getSancAmt().equals("") ? "0" : d.getSancAmt());

        }
        return sum;

    }


    public void removeAll(){

        claimBoardingAndLodgingList.clear();
        notifyDataSetChanged();

    }
}
