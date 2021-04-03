package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import in.cbslgroup.ezeepeafinal.model.DetailsOfTravel;
import in.cbslgroup.ezeepeafinal.model.ModeClass;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class DetailsOfTravelingAdapter extends RecyclerView.Adapter<DetailsOfTravelingAdapter.ViewHolder> {

    List<DetailsOfTravel> detailsOfTravelList;
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

        void onToPlaceSelected(int pos, String toPlace);

        void getTotalAdvance(int total);

        void getTotalSanctioned(int total);


    }

    public DetailsOfTravelingAdapter(Context context, List<DetailsOfTravel> detailsOfTravelList, List<City> cityList) {
        this.detailsOfTravelList = detailsOfTravelList;
        this.cityList = cityList;
        this.context = context;
    }


    @NonNull
    @Override
    public DetailsOfTravelingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_req_details_of_travel_add_more_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsOfTravelingAdapter.ViewHolder holder, int position) {

        DetailsOfTravel item = detailsOfTravelList.get(position);

        if (position == 0) {

            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.ivMinus.setVisibility(View.GONE);

        } else {

            holder.ivAdd.setVisibility(View.GONE);
            holder.ivMinus.setVisibility(View.VISIBLE);

        }

        holder.ivMinus.setOnClickListener(
                view ->
                listener.onMinusButtonClicked(position)
        );


        holder.ivAdd.setOnClickListener(
                view ->
                listener.onAddButtonClicked(position)
        );

        holder.tieSno.setText(String.valueOf(position + 1));
        holder.tieAdvReq.setText(item.getAdvReq());
        holder.tieClass.setText(item.getmClass());
        holder.tieDateOfTravel.setText(item.getDateOfTravel());
        holder.tieDestFrom.setText(item.getDestFrom());
        holder.tieDestTo.setText(item.getDestTo());
        holder.tieMode.setText(item.getMode());
        holder.tieRemarks.setText(item.getRemarks());

        //if santionced form
        if(item.getSanctioned()){

            holder.ivAdd.setVisibility(View.GONE);
            holder.ivMinus.setVisibility(View.GONE);

            holder.tieSno.setEnabled(false);
            holder.tieAdvReq.setEnabled(false);
            holder.tieClass.setEnabled(false);
            holder.tieDateOfTravel.setEnabled(false);
            holder.tieDestFrom.setEnabled(false);
            holder.tieDestTo.setEnabled(false);
            holder.tieMode.setEnabled(false);
            holder.tieRemarks.setEnabled(false);

            holder.tilSanctAmt.setVisibility(View.VISIBLE);
            holder.tieSanctAmt.setText(item.getSancAmt());


        }

    }

    @Override
    public int getItemCount() {
        return detailsOfTravelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAdd, ivMinus;

        TextInputLayout tilSno, tilDateOfTravel, tilMode, tilDestTo, tilDestFrom, tilClass, tilRemarks, tilAdvReq,tilSanctAmt;
        TextInputEditText tieSno, tieDateOfTravel, tieMode, tieDestTo, tieDestFrom, tieClass, tieRemarks, tieAdvReq,tieSanctAmt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAdd = itemView.findViewById(R.id.iv_details_of_travel_add);
            ivMinus = itemView.findViewById(R.id.iv_details_of_travel_minus);

            tieSno = itemView.findViewById(R.id.tie_details_of_travel_sno);
            tieDateOfTravel = itemView.findViewById(R.id.tie_details_of_travel_date_of_travel);
            tieAdvReq = itemView.findViewById(R.id.tie_details_of_travel_adv_req);
            tieSanctAmt = itemView.findViewById(R.id.tie_details_of_travel_sanc_req);
            tieClass = itemView.findViewById(R.id.tie_details_of_travel_class);
            tieDestFrom = itemView.findViewById(R.id.tie_details_of_travel_dest_from);
            tieDestTo = itemView.findViewById(R.id.tie_details_of_travel_dest_to);
            tieMode = itemView.findViewById(R.id.tie_details_of_travel_mode);
            tieRemarks = itemView.findViewById(R.id.tie_details_of_travel_remarks);

            tilSno = itemView.findViewById(R.id.til_details_of_travel_sno);
            tilDateOfTravel = itemView.findViewById(R.id.til_details_of_travel_date_of_travel);
            tilAdvReq = itemView.findViewById(R.id.til_details_of_travel_adv_req);
            tilClass = itemView.findViewById(R.id.til_details_of_travel_class);
            tilDestFrom = itemView.findViewById(R.id.til_details_of_travel_dest_from);
            tilDestTo = itemView.findViewById(R.id.til_details_of_travel_dest_to);
            tilMode = itemView.findViewById(R.id.til_details_of_travel_mode);
            tilRemarks = itemView.findViewById(R.id.til_details_of_travel_remarks);
            tilSanctAmt = itemView.findViewById(R.id.til_details_of_travel_sanc_req);


            tieSanctAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    detailsOfTravelList.get(getAdapterPosition()).setSancAmt(tieSanctAmt.getText().toString().replaceAll("[^a-zA-Z0-9]", ""));
                    listener.getTotalSanctioned(getTotalSanctioned());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            tieSno.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    detailsOfTravelList.get(getAdapterPosition()).setSno(tieSno.getText().toString().replaceAll("[^a-zA-Z0-9]", ""));

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieDestFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<BottomSheetWithSearch> list = new ArrayList<>();

                    Log.e("city_list_size_br", cityList.size() + "");

                    for (City c : cityList) {

                        list.add(new BottomSheetWithSearch(c.getCityName(), c.getCityId()));

                    }

                    Log.e("city_list_size", list.size() + "");

                    showBottomSheetDialog("Destination From",tieDestFrom,new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "from", true);

                }
            });

            tieDestTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<BottomSheetWithSearch> list = new ArrayList<>();

                    Log.e("city_list_size_br", cityList.size() + "");

                    for (City c : cityList) {

                        list.add(new BottomSheetWithSearch(c.getCityName(), c.getCityId()));

                    }

                    Log.e("city_list_size", list.size() + "");

                    showBottomSheetDialog("Destination To",tieDestTo, new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "to", true);

                }
            });

            tieDateOfTravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dpd = new DatePickerDialog(context, (view1, year, month, dayOfMonth) -> {

                        String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                        detailsOfTravelList.get(getAdapterPosition()).setDateOfTravel(dt);
                        notifyDataSetChanged();

                    }, mYear, mMonth, mDay);

                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
                    dpd.show();

                }
            });

            tieClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!detailsOfTravelList.get(getAdapterPosition()).getMode().equals("")) {

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

                        } else {

                            Toast.makeText(context, "There is no class for the selected mode of travel", Toast.LENGTH_SHORT).show();
                            detailsOfTravelList.get(getAdapterPosition()).setmClass("");
                            notifyDataSetChanged();


                        }

                    } else {


                        Toast.makeText(context, "Please Select Mode of Travel First", Toast.LENGTH_SHORT).show();
                        detailsOfTravelList.get(getAdapterPosition()).setmClass("");
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

                    showBottomSheetDialog("Mode of Travel",tieClass, new BottomSheetWithSearchAdapter(list), getAdapterPosition(), "mode", false);


                }
            });

            tieRemarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    detailsOfTravelList.get(getAdapterPosition()).setRemarks(tieRemarks.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tieAdvReq.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    detailsOfTravelList.get(getAdapterPosition())
                            .setAdvReq(tieAdvReq.getText().toString()
                            .replaceAll("[^a-zA-Z0-9]", "")
                    );
                    listener.getTotalAdvance(getTotalAdvacnce());

                }

                @Override
                public void afterTextChanged(Editable editable) {

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

                if (module.equals("to")) {

                    detailsOfTravelList.get(pos).setDestTo(obj.getValue());
                    listener.onToPlaceSelected(pos, obj.getValue());

                } else if (module.equals("from")) {

                    detailsOfTravelList.get(pos).setDestFrom(obj.getValue());


                } else if (module.equals("mode")) {

                    detailsOfTravelList.get(pos).setMode(obj.getValue());

                    getModeClass(obj.getValue(),tie,pos);

                } else if (module.equals("class")) {

                    detailsOfTravelList.get(pos).setmClass(obj.getValue());

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

    private void getModeClass(String mode,TextInputEditText tie,int pos) {

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

                        tie.setEnabled(true);
                        notifyDataSetChanged();

                    } else {

                        modeClassList.clear();
                        tie.setEnabled(false);
                        tie.setText("");
                        detailsOfTravelList.get(pos).setmClass("");
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


    public void addItem(DetailsOfTravel detailsOfTravel, int pos) {

        detailsOfTravelList.add(detailsOfTravel);
        notifyDataSetChanged();
        Log.e("list_size_ad", detailsOfTravelList.size() + "");
//        notifyItemInserted(detailsOfTravelList.size() - 1);
//        notifyItemRangeChanged(pos + 1, detailsOfTravelList.size());
    }

    public void removeItem(int pos) {

        detailsOfTravelList.remove(pos);
        notifyDataSetChanged();
        Log.e("list_size_rm", detailsOfTravelList.size() + "");

//        notifyItemRemoved(pos);
//        notifyItemRangeChanged(pos, detailsOfTravelList.size());
    }

    public int getTotalAdvacnce() {

        int sum = 0;

        for (DetailsOfTravel d :
                detailsOfTravelList) {

            sum += Integer.parseInt(d.getAdvReq().equals("")?"0":d.getAdvReq());

        }
        return sum;

    }


    public int getTotalSanctioned() {

        int sum = 0;

        for (DetailsOfTravel d :
                detailsOfTravelList) {

            sum += Integer.parseInt(d.getSancAmt().equals("")?"0":d.getSancAmt());

        }
        return sum;

    }

    public void clearAll(){

        detailsOfTravelList.clear();
        notifyDataSetChanged();
    }

    public List<DetailsOfTravel> getList(){

        return detailsOfTravelList;
    }

    public List<ModeClass> getModeClassList(){

        return modeClassList;
    }

}
