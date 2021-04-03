package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;

import java.util.List;

import in.cbslgroup.ezeepeafinal.interfaces.AddMoreCashVoucherListener;
import in.cbslgroup.ezeepeafinal.model.CashVocherAddMore;
import in.cbslgroup.ezeepeafinal.R;



public class CashVocherAddMoreAdapter extends RecyclerView.Adapter<CashVocherAddMoreAdapter.ViewHolder> {

    List<CashVocherAddMore> cashVocherAddMoreList;
    Context context;
    AddMoreCashVoucherListener listener;

    boolean fillAllFields = false;


    public CashVocherAddMoreAdapter(List<CashVocherAddMore> cashVocherAddMoreList, Context context, AddMoreCashVoucherListener listener) {
        this.cashVocherAddMoreList = cashVocherAddMoreList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_voucher_add_more_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(position>0){

            holder.ibAdd.setVisibility(View.GONE);
            holder.ibRemove.setVisibility(View.VISIBLE);
        }

        else{

            holder.ibAdd.setVisibility(View.VISIBLE);
            holder.ibRemove.setVisibility(View.GONE);

        }


        holder.etDesc.setText(cashVocherAddMoreList.get(position).getDescription());
        holder.etRupee.setText(cashVocherAddMoreList.get(position).getRupee());
        holder.etPaisa.setText(cashVocherAddMoreList.get(position).getPaisa());

        holder.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if(s.toString().length()==0){
//
//                    holder.etDesc.setError("Please Enter Description");
//                    fillAllFields = false;
//
//                }
//
//                else{
//
//                    holder.etDesc.setError(null);
//                    fillAllFields = true;
//
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etRupee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                listener.onRuppeChanged();

//                if(s.toString().length()==0){
//
//                    holder.etRupee.setError("Please Enter Ruppe");
//                    fillAllFields = false;
//
//                }
//
//                else{
//
//                    holder.etRupee.setError(null);
//                    fillAllFields = true;
//
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etPaisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


//                if(s.toString().length()==0){
//
//                    holder.etPaisa.setError("Please Enter Paisa");
//                    fillAllFields = false;
//
//                }
//
//                else{
//
//                    holder.etPaisa.setError(null);
//                    fillAllFields = true;
//
//                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onAddClick(v,position);
            }
        });

        holder.ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onRemoveClick(v,position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cashVocherAddMoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageButton ibAdd,ibRemove;
        EditText etRupee,etPaisa,etDesc;



        public ViewHolder(View itemView) {
            super(itemView);

            ibAdd = itemView.findViewById(R.id.ib_intiateworkflow_add);
            ibRemove = itemView.findViewById(R.id.ib_intiateworkflow_remove);


            etRupee = itemView.findViewById(R.id.et_intiateworkflow_rupee_dynamic);
            etPaisa = itemView.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
            etDesc = itemView.findViewById(R.id.et_intiateworkflow_desc_dynamic);


            etDesc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    cashVocherAddMoreList.get(getAdapterPosition()).setDescription(etDesc.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            etRupee.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(s.toString().length() == 0){

                        etRupee.setText("0");

                    }

                    cashVocherAddMoreList.get(getAdapterPosition()).setRupee(etRupee.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            etPaisa.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    if(s.toString().length() == 0){

                        etPaisa.setText("0");

                    }

                    cashVocherAddMoreList.get(getAdapterPosition()).setPaisa(etPaisa.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });




        }
    }


    public JSONArray getAllDescription(){

        JSONArray jrDesc = new JSONArray();

        for (CashVocherAddMore item : cashVocherAddMoreList) {

            jrDesc.put(item.getDescription());

        }

        return jrDesc;

    }


    public String getTotalRupees(){

        int total = 0;

        for (CashVocherAddMore item:
                cashVocherAddMoreList) {

            total =  total + Integer.parseInt(item.getRupee());
        }

        return String.valueOf(total);
    }


    public String getTotalPaise(){

        int total = 0;

        for (CashVocherAddMore item:
                cashVocherAddMoreList) {

            total =  total + Integer.parseInt(item.getPaisa());
        }

        return String.valueOf(total);
    }


    boolean getValidation(){

        return fillAllFields;

    }


//
//    //get all list data
//   public List<CashVocherAddMore> getValues(){
//
//        List<CashVocherAddMore> list = new ArrayList<>();
//
//        for(int i =0 ; i<enteredDataList.size();i++){
//
//            list.add(new CashVocherAddMore(enteredDataList.get(i).getRupee(),enteredDataList.get(i).getPaisa()));
//
//        }
//
//        return list;
//    }


}
