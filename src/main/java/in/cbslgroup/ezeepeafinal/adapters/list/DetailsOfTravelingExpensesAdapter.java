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
import in.cbslgroup.ezeepeafinal.model.DetailsOfTravelExpenses;
import in.cbslgroup.ezeepeafinal.model.ModeClass;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class DetailsOfTravelingExpensesAdapter extends RecyclerView.Adapter<DetailsOfTravelingExpensesAdapter.ViewHolder> {

    public DetailsOfTravelingExpensesAdapter(List<DetailsOfTravelExpenses> detailsOfTravelExpenses, List<City> cityList, Context context) {
        this.detailsOfTravelExpenses = detailsOfTravelExpenses;
        this.cityList = cityList;
        this.context = context;
    }


    List<DetailsOfTravelExpenses> detailsOfTravelExpenses;
    List<City> cityList;
    List<ModeClass> modeClassList = new ArrayList();
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_form_travel_expense_details_of_travel_add_more_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DetailsOfTravelExpenses item = detailsOfTravelExpenses.get(position);

        if (position == 0) {

            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.ivMinus.setVisibility(View.GONE);

        } else {

            holder.ivAdd.setVisibility(View.GONE);
            holder.ivMinus.setVisibility(View.VISIBLE);

        }

        holder.ivMinus.setOnClickListener(view -> listener.onMinusButtonClicked(position));
        holder.ivAdd.setOnClickListener(view -> listener.onAddButtonClicked(position));


        holder.tieSno.setText(String.valueOf(position + 1));
        holder.tieDeptDateOfTravel.setText(item.getDeptDateOfTravel());
        holder.tieDeptTime.setText(item.getDeptTime());
        holder.tieDeptPlace.setText(item.getDeptPlace());

        holder.tieArrvDateOfTravel.setText(item.getArrvDateOfTravel());
        holder.tieArrvTime.setText(item.getArrvTime());
        holder.tieArrvPlace.setText(item.getArrvPlace());

        holder.tieClass.setText(item.getmClass());
        holder.tieMode.setText(item.getMode());
        holder.tieRemarks.setText(item.getRemarks());


        holder.tieSanc.setText(item.getSancAmt());
        holder.tieClaim.setText(item.getClaimAmt());

        //if value is coming from selecting tourid
        if(item.getMode().isEmpty()){

           Log.e("getModeClass","yes");
           holder.tieClass.setEnabled(false);
        }
        else{

            holder.tieClass.setEnabled(true);
        }

        if(item.isSanctioned()){

            holder.ivAdd.setVisibility(View.GONE);
            holder.ivMinus.setVisibility(View.GONE);

            holder.tieSno.setEnabled(false);
            holder.tieDeptDateOfTravel.setEnabled(false);
            holder.tieDeptTime.setEnabled(false);
            holder.tieDeptPlace.setEnabled(false);

            holder.tieArrvDateOfTravel.setEnabled(false);
            holder.tieArrvTime.setEnabled(false);
            holder.tieArrvPlace.setEnabled(false);

            holder.tieClass.setEnabled(false);
            holder.tieMode.setEnabled(false);
            holder.tieRemarks.setEnabled(false);
            holder.tieClaim.setEnabled(false);


            holder.tieSanc.setEnabled(true);

        }

    }

    @Override
    public int getItemCount() {
        return detailsOfTravelExpenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAdd, ivMinus;
        TextInputLayout tilSno, tilMode, tilDeptDateOfTravel, tilDeptTime, tilDeptPlace, tilArrvDateOfTravel, tilArrvTime, tilArrvPlace, tilClass, tilRemarks, tilSanc, tilClaim;
        TextInputEditText tieSno, tieMode, tieDeptDateOfTravel, tieDeptTime, tieDeptPlace, tieArrvDateOfTravel, tieArrvTime, tieArrvPlace, tieClass, tieRemarks, tieSanc, tieClaim;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAdd = itemView.findViewById(R.id.iv_details_of_travel_exp_add);
            ivMinus = itemView.findViewById(R.id.iv_details_of_travel_exp_minus);

            tieSno =itemView.findViewById(R.id.tie_details_of_travel_exp_sno);
            tieMode =itemView.findViewById(R.id.tie_details_of_travel_exp_mode);
            tieDeptDateOfTravel =itemView.findViewById(R.id.tie_details_of_travel_exp_dept_date_of_travel);
            tieDeptTime =itemView.findViewById(R.id.tie_details_of_travel_exp_dept_time);
            tieDeptPlace =itemView.findViewById(R.id.tie_details_of_travel_exp_dept_place);
            tieArrvDateOfTravel =itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_date_of_travel);
            tieArrvTime =itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_time);
            tieArrvPlace =itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_place);
            tieClass =itemView.findViewById(R.id.tie_details_of_travel_exp_class);
            tieRemarks =itemView.findViewById(R.id.tie_details_of_travel_exp_remarks);
            tieSanc =itemView.findViewById(R.id.tie_details_of_travel_exp_sanc_amt);
            tieClaim =itemView.findViewById(R.id.tie_details_of_travel_exp_claim_amt);

            tilSno =itemView.findViewById(R.id.til_details_of_travel_exp_sno);
            tilMode =itemView.findViewById(R.id.til_details_of_travel_exp_mode);
            tilDeptDateOfTravel =itemView.findViewById(R.id.til_details_of_travel_exp_dept_date_of_travel);
            tilDeptTime =itemView.findViewById(R.id.til_details_of_travel_exp_dept_time);
            tilDeptPlace =itemView.findViewById(R.id.til_details_of_travel_exp_dept_place);
            tilArrvDateOfTravel =itemView.findViewById(R.id.til_details_of_travel_exp_arrv_date_of_travel);
            tilArrvTime =itemView.findViewById(R.id.til_details_of_travel_exp_arrv_time);
            tilArrvPlace =itemView.findViewById(R.id.til_details_of_travel_exp_arrv_place);
            tilClass =itemView.findViewById(R.id.til_details_of_travel_exp_class);
            tilRemarks =itemView.findViewById(R.id.til_details_of_travel_exp_remarks);
            tilSanc =itemView.findViewById(R.id.til_details_of_travel_exp_sanc_amt);
            tilClaim =itemView.findViewById(R.id.til_details_of_travel_exp_claim_amt);

            tieClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!detailsOfTravelExpenses.get(getAdapterPosition()).getMode().equals("")) {

                        if (modeClassList.size() > 0) {

                            List<BottomSheetWithSearch> list = new ArrayList<>();
                            BottomSheetWithSearchAdapter bt = new BottomSheetWithSearchAdapter(list);
                            bt.clearFilteredList();
                            bt.clearMainList();

                            Log.e("mode_class_list_size_br", cityList.size() + "");

                            for (ModeClass c : modeClassList) {

                                list.add(new BottomSheetWithSearch(c.getTravelBy(), c.getmClass()));

                            }

                            Log.e("mode_class_list_size", list.size() + "");

                            showBottomSheetDialog("Class",tieClass, bt, getAdapterPosition(), "class", false);

                        }

                        else if (modeClassList.size()==0 && !detailsOfTravelExpenses.get(getAdapterPosition()).getmClass().equals("")){


                            getModeClass(detailsOfTravelExpenses.get(getAdapterPosition()).getMode(),tieClass,getAdapterPosition(),detailsOfTravelExpenses.get(getAdapterPosition()).getmClass());

                        }

                        else {

                            Toast.makeText(context, "There is no class for the selected mode of travel", Toast.LENGTH_SHORT).show();
                            detailsOfTravelExpenses.get(getAdapterPosition()).setmClass("");
                            notifyDataSetChanged();


                        }

                    } else {


                        Toast.makeText(context, "Please Select Mode of Travel First", Toast.LENGTH_SHORT).show();
                        detailsOfTravelExpenses.get(getAdapterPosition()).setmClass("");
                        notifyDataSetChanged();

                    }
                }
            });



            tieRemarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    detailsOfTravelExpenses.get(getAdapterPosition()).setRemarks(tieRemarks.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieSanc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    detailsOfTravelExpenses.get(getAdapterPosition()).setSancAmt(tieSanc.getText().toString());
                    listener.getTotalSanctionedAmt(getTotalSancAmt());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieClaim.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    detailsOfTravelExpenses.get(getAdapterPosition()).setClaimAmt(tieClaim.getText().toString());
                    listener.getTotalClaimAmt(getTotalClaimAmt());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieDeptPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<BottomSheetWithSearch> list = new ArrayList<>();

                    Log.e("city_list_size_br", cityList.size() + "");

                    for (City c : cityList) {

                        list.add(new BottomSheetWithSearch(c.getCityName(), c.getCityId()));

                    }

                    Log.e("city_list_size", list.size() + "");

                    showBottomSheetDialog("Departure Place ",tieDeptPlace, new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "dept_place", true);

                }
            });

            tieArrvPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<BottomSheetWithSearch> list = new ArrayList<>();

                    Log.e("city_list_size_br", cityList.size() + "");

                    for (City c : cityList) {

                        list.add(new BottomSheetWithSearch(c.getCityName(), c.getCityId()));

                    }

                    Log.e("city_list_size", list.size() + "");

                    showBottomSheetDialog("Arrival Place ",tieArrvPlace, new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "arrv_place", true);

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

                    showBottomSheetDialog("Mode of Travel",tieClass, new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "mode", false);

                }
            });

//            tieMode.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    String txt = editable.toString();
//
//                    if(!txt.isEmpty()){
//
//                        getModeClass(txt,tieClass,getAdapterPosition(),tieClass.getText().toString());
//
//                    }
//
//                }
//            });

            tieDeptDateOfTravel.setOnClickListener(new View.OnClickListener() {
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

                            String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                            detailsOfTravelExpenses.get(getAdapterPosition()).setDeptDateOfTravel(dt);
                            notifyDataSetChanged();

                        }
                    }, mYear, mMonth, mDay).show();

                }
            });

            tieArrvDateOfTravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                 DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                            detailsOfTravelExpenses.get(getAdapterPosition()).setArrvDateOfTravel(dt);
                            notifyDataSetChanged();

                        }
                    }, mYear, mMonth, mDay);
                  //  dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
                    dpd.show();



                }
            });

            tieArrvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                   DatePickerDialog dpd =  new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                           // String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + (month < 10 ? "0" + month : month) + "-" + year;


                            String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                            detailsOfTravelExpenses.get(getAdapterPosition()).setArrvDateOfTravel(dt);
                            notifyDataSetChanged();

                        }
                    }, mYear, mMonth, mDay);
                  //  dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
                    dpd.show();

                }
            });


            tieDeptTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog timePicker;

                    timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            detailsOfTravelExpenses.get(getAdapterPosition()).setDeptTime(selectedHour + ":" + selectedMinute);
                            notifyDataSetChanged();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    timePicker.show();

                }
            });


            tieArrvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog timePicker;

                    timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            detailsOfTravelExpenses.get(getAdapterPosition()).setArrvTime(selectedHour + ":" + selectedMinute);
                            notifyDataSetChanged();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    timePicker.show();

                }
            });






        }
    }


    //methods helper
    private void showBottomSheetDialog(String title,TextInputEditText tie,BottomSheetWithSearchAdapter bottomSheetWithSearchAdapter, int pos, String module, Boolean wantSearchFilter) {

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

                if (module.equals("dept_place")) {

                    detailsOfTravelExpenses.get(pos).setDeptPlace(obj.getValue());


                } else if (module.equals("arrv_place")) {

                    detailsOfTravelExpenses.get(pos).setArrvPlace(obj.getValue());


                } else if (module.equals("mode")) {

                    detailsOfTravelExpenses.get(pos).setMode(obj.getValue());

                    getModeClass(obj.getValue(),tie,pos,"");

                } else if (module.equals("class")) {

                    detailsOfTravelExpenses.get(pos).setmClass(obj.getValue());

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


    private void getModeClass(String mode,TextInputEditText tie,int pos,String mclass) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                modeClassList.clear();
                Log.e("mode_class",response);

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
                        detailsOfTravelExpenses.get(pos).setmClass("");
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

    public int getTotalSancAmt() {

        int sum = 0;

        for (DetailsOfTravelExpenses d :
                detailsOfTravelExpenses) {

            sum += Integer.parseInt(d.getSancAmt().equals("")?"0":d.getSancAmt());

        }
        return sum;

    }

    public int getTotalClaimAmt() {

        int sum = 0;

        for (DetailsOfTravelExpenses d :
                detailsOfTravelExpenses) {

            sum += Integer.parseInt(d.getClaimAmt().equals("")?"0":d.getClaimAmt());

        }
        return sum;

    }


    public void addItem(DetailsOfTravelExpenses detailsOfTravel, int pos) {

        detailsOfTravelExpenses.add(detailsOfTravel);
        notifyDataSetChanged();
        Log.e("list_size_ad", detailsOfTravelExpenses.size() + "");

//        notifyItemInserted(detailsOfTravelList.size() - 1);
//        notifyItemRangeChanged(pos + 1, detailsOfTravelList.size());
    }

    public void removeItem(int pos) {

        detailsOfTravelExpenses.remove(pos);
        notifyDataSetChanged();
        Log.e("list_size_rm", detailsOfTravelExpenses.size() + "");

//        notifyItemRemoved(pos);
//        notifyItemRangeChanged(pos, detailsOfTravelList.size());
    }

    public void removeAll(){

        detailsOfTravelExpenses.clear();
        notifyDataSetChanged();

    }

    public List<DetailsOfTravelExpenses>getList(){

        return detailsOfTravelExpenses;

    }
}
