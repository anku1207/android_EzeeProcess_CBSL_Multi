package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.model.BottomSheetWithSearch;
import in.cbslgroup.ezeepeafinal.model.City;
import in.cbslgroup.ezeepeafinal.model.LocalConveyanceAndOnOutstationVisit;
import in.cbslgroup.ezeepeafinal.model.ModeClass;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class ClaimLocalConveyanceAndOutstationVisitAdapter extends RecyclerView.Adapter<ClaimLocalConveyanceAndOutstationVisitAdapter.ViewHolder> {

    List<City> cityList;
    List<ModeClass> modeClassList = new ArrayList();
    List<LocalConveyanceAndOnOutstationVisit> localConveyanceAndOnOutstationVisitList;
    Context context;

    public ClaimLocalConveyanceAndOutstationVisitAdapter(List<LocalConveyanceAndOnOutstationVisit> localConveyanceAndOnOutstationVisitList, Context context) {
        this.localConveyanceAndOnOutstationVisitList = localConveyanceAndOnOutstationVisitList;
        this.context = context;
    }


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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_form_travel_expense_local_conveyance_and_on_outstation_visit_add_more_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LocalConveyanceAndOnOutstationVisit item = localConveyanceAndOnOutstationVisitList.get(position);

        Log.e("from_"+position+"-->",item.getFrom());
        Log.e("to_"+position+"-->",item.getTo());

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
            holder.tieClaimAmt.setText(item.getClaimAmt());
            holder.tieSancAmt.setText(item.getSancAmt());
            holder.tieRemarks.setText(item.getRemarks());
            holder.tieMode.setText(item.getMode());


            holder.tieFrom.setText(item.getFrom());
            holder.tieTo.setText(item.getTo());


            holder.tieTime.setText(item.getTime());
            holder.tieClass.setText(item.getmClass());


            if (item.getSanctioned() != null && item.getSanctioned()) {

                holder.ivAdd.setVisibility(View.GONE);
                holder.ivMinus.setVisibility(View.GONE);
                holder.tilClass.setVisibility(View.GONE);

                holder.tieSno.setEnabled(false);
                holder.tieDate.setEnabled(false);
                holder.tieClaimAmt.setEnabled(false);

                holder.tieRemarks.setEnabled(false);
                holder.tieMode.setEnabled(false);
                holder.tieFrom.setEnabled(false);
                holder.tieTo.setEnabled(false);
                holder.tieTime.setEnabled(false);


                holder.tieClass.setEnabled(false);

                holder.tieSancAmt.setEnabled(true);


            }





    }

    @Override
    public int getItemCount() {
        return localConveyanceAndOnOutstationVisitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAdd, ivMinus;
        TextInputLayout tilSno, tilTime, tilMode, tilFrom, tilTo, tilDate, tilRemarks, tilClaimAmt, tilSancAmt, tilClass;
        TextInputEditText tieSno, tieTime, tieMode, tieFrom, tieTo, tieDate, tieRemarks, tieClaimAmt, tieSancAmt, tieClass;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAdd = itemView.findViewById(R.id.iv_claim_local_conveyance_add);
            ivMinus = itemView.findViewById(R.id.iv_claim_local_conveyance_minus);

            tieSno = itemView.findViewById(R.id.tie_claim_local_conveyance_sno);
            tieDate = itemView.findViewById(R.id.tie_claim_local_conveyance_date_of_travel);
            tieClaimAmt = itemView.findViewById(R.id.tie_claim_local_conveyance_claim_amt);
            tieSancAmt = itemView.findViewById(R.id.tie_claim_local_conveyance_sanc_amt);
            tieRemarks = itemView.findViewById(R.id.tie_claim_local_conveyance_remarks);
            tieMode = itemView.findViewById(R.id.tie_claim_local_conveyance_mode);
            tieFrom = itemView.findViewById(R.id.tie_claim_local_conveyance_dest_from);
            tieTo = itemView.findViewById(R.id.tie_claim_local_conveyance_dest_to);
            tieTime = itemView.findViewById(R.id.tie_claim_local_conveyance_time);
            tieClass = itemView.findViewById(R.id.tie_claim_local_conveyance_class);

            tilSno = itemView.findViewById(R.id.til_claim_local_conveyance_sno);
            tilDate = itemView.findViewById(R.id.til_claim_local_conveyance_date_of_travel);
            tilClaimAmt = itemView.findViewById(R.id.til_claim_local_conveyance_claim_amt);
            tilSancAmt = itemView.findViewById(R.id.til_claim_local_conveyance_sanc_amt);
            tilRemarks = itemView.findViewById(R.id.til_claim_local_conveyance_remarks);
            tilMode = itemView.findViewById(R.id.til_claim_local_conveyance_mode);
            tilFrom = itemView.findViewById(R.id.til_claim_local_conveyance_dest_from);
            tilTo = itemView.findViewById(R.id.til_claim_local_conveyance_dest_to);
            tilTime = itemView.findViewById(R.id.til_claim_local_conveyance_time);
            tilClass = itemView.findViewById(R.id.til_claim_local_conveyance_class);


            try {

                tieClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).getMode().equals("")) {

                            if (modeClassList.size() > 0) {

                                List<BottomSheetWithSearch> list = new ArrayList<>();
                                BottomSheetWithSearchAdapter bt = new BottomSheetWithSearchAdapter(list);
                                bt.clearFilteredList();
                                bt.clearMainList();

                                // Log.e("mode_class_list_size_br", cityList.size() + "");

                                for (ModeClass c : modeClassList) {

                                    list.add(new BottomSheetWithSearch(c.getTravelBy(), c.getmClass()));

                                }

                                Log.e("mode_class_list_size", list.size() + "");

                                showBottomSheetDialog("Class", tieClass, bt, getAdapterPosition(), "class", false);

                            } else {

                                Toast.makeText(context, R.string.there_is_no_class_for_the_selected_mode_of_travel, Toast.LENGTH_SHORT).show();
                                localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setmClass("");
                                notifyDataSetChanged();


                            }

                        } else {


                            Toast.makeText(context, R.string.please_select_mode_of_travel_first, Toast.LENGTH_SHORT).show();
                            localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setmClass("");
                            notifyDataSetChanged();

                        }


                    }
                });

                tieMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        List<BottomSheetWithSearch> list = new ArrayList<>();
                        list.add(new BottomSheetWithSearch("0", "Air"));
                        list.add(new BottomSheetWithSearch("1", "Auto"));
                        list.add(new BottomSheetWithSearch("2", "Bus"));
                        list.add(new BottomSheetWithSearch("3", "Rickshaw"));
                        list.add(new BottomSheetWithSearch("4", "Taxi"));
                        list.add(new BottomSheetWithSearch("5", "Train"));

                        showBottomSheetDialog("Mode of Travel", tieClass, new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "mode", false);


                    }
                });

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

                                //String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (month < 10 ? "0" + month : month) + "-" + year;
                                String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
                                localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setDate(dt);
                                notifyDataSetChanged();

                            }
                        }, mYear, mMonth, mDay).show();
                    }


                });



                tieFrom.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setFrom(tieFrom.getText().toString());

                    }


                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


                tieTo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setTo(tieTo.getText().toString());

                    }


                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


                tieTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog timePicker;

                        timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                                try {

                                    localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setTime(selectedHour + ":" + selectedMinute);
                                     notifyDataSetChanged();
                                    // tieTime.setText( selectedHour + ":" + selectedMinute);
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }


                            }
                        }, hour, minute, true);//Yes 24 hour time
                        timePicker.show();

                    }
                });

                tieClaimAmt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setClaimAmt(tieClaimAmt.getText().toString());
                        listener.getTotalClaimAmt(getTotalClaimAmount());

                    }


                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                tieRemarks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setRemarks(tieRemarks.getText().toString());

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

                        localConveyanceAndOnOutstationVisitList.get(getAdapterPosition()).setSancAmt(tieSancAmt.getText().toString());
                        listener.getTotalSanctionedAmt(getTotalSanctioned());

                    }


                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });



            } catch (Exception e) {

                e.printStackTrace();
            }


        }
    }

    public List<LocalConveyanceAndOnOutstationVisit> getList() {

        return localConveyanceAndOnOutstationVisitList;
    }

    public void addItem(LocalConveyanceAndOnOutstationVisit localConveyanceAndOnOutstationVisit, int pos) {

        localConveyanceAndOnOutstationVisitList.add(localConveyanceAndOnOutstationVisit);
        notifyDataSetChanged();
        Log.e("list_size_ad", localConveyanceAndOnOutstationVisitList.size() + "");

        Log.e("from_"+pos+"-->",localConveyanceAndOnOutstationVisitList.get(pos).getFrom());
        Log.e("to_"+pos+"-->",localConveyanceAndOnOutstationVisitList.get(pos).getTo());
    }

    public void removeItem(int pos) {

        localConveyanceAndOnOutstationVisitList.remove(pos);
        notifyDataSetChanged();
        Log.e("list_size_rm", localConveyanceAndOnOutstationVisitList.size() + "");

    }

    public int getTotalClaimAmount() {

        int sum = 0;

        for (LocalConveyanceAndOnOutstationVisit d :
                localConveyanceAndOnOutstationVisitList) {

            sum += Integer.parseInt(d.getClaimAmt().equals("") ? "0" : d.getClaimAmt());

        }
        return sum;

    }

    public int getTotalSanctioned() {

        int sum = 0;

        for (LocalConveyanceAndOnOutstationVisit d :
                localConveyanceAndOnOutstationVisitList) {

            sum += Integer.parseInt(d.getSancAmt().equals("") ? "0" : d.getSancAmt());

        }
        return sum;

    }

    private void showBottomSheetDialog(String title, TextInputEditText tie, BottomSheetWithSearchAdapter bottomSheetWithSearchAdapter, int pos, String module, Boolean wantSearchFilter) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottomsheet_with_search, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        RecyclerView rvBottomSheetDocumentView = bottomSheetDialog.findViewById(R.id.rv_bs_with_search);
        rvBottomSheetDocumentView.setLayoutManager(new LinearLayoutManager(context));
        rvBottomSheetDocumentView.setAdapter(bottomSheetWithSearchAdapter);

        bottomSheetWithSearchAdapter.setListener(new BottomSheetWithSearchAdapter.OnClickListener() {
            @Override
            public void onItemClickListerner(BottomSheetWithSearch obj) {

                if (module.equals("mode")) {

                    localConveyanceAndOnOutstationVisitList.get(pos).setMode(obj.getValue());

                    getModeClass(obj.getValue(), tie, pos, "");

                } else if (module.equals("class")) {

                    localConveyanceAndOnOutstationVisitList.get(pos).setmClass(obj.getValue());

                }

                bottomSheetDialog.dismiss();
                notifyDataSetChanged();

            }
        });

        EditText etSearh = bottomSheetDialog.findViewById(R.id.et_bs_with_search_search);
        etSearh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                bottomSheetWithSearchAdapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etSearh.setVisibility(wantSearchFilter ? View.VISIBLE : View.GONE);


        TextView tvBottomSheetHeading = bottomSheetDialog.findViewById(R.id.tv_bs_with_search_heading);
        tvBottomSheetHeading.setText(title);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(((View) view.getParent()));
        // bottomSheetBehavior.setPeekHeight(Integer.MAX_VALUE);

//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int i) {
//                bottomSheetBehavior.setPeekHeight(Integer.MAX_VALUE);
//            }
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });


        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    private void getModeClass(String mode, TextInputEditText tie, int pos, String mclass) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                modeClassList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    if (error.equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String travelBy = jsonArray.getJSONObject(i).getString("travel_by");
                            String mClass = jsonArray.getJSONObject(i).getString("class");
                            modeClassList.add(new ModeClass(travelBy, mClass));
                        }


                        tie.setText(mclass);
                        tie.setEnabled(true);
                        notifyDataSetChanged();

                    } else {

                        modeClassList.clear();
                        tie.setEnabled(false);
                        tie.setText("");
                        localConveyanceAndOnOutstationVisitList.get(pos).setmClass("");
                        notifyDataSetChanged();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "getClass");
                params.put("mode", mode);
                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }


    public void removeAll() {

        localConveyanceAndOnOutstationVisitList.clear();
        notifyDataSetChanged();

    }


}
