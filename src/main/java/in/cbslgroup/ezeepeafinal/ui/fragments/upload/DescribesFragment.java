package in.cbslgroup.ezeepeafinal.ui.fragments.upload;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.UploadActivity;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.CustomDateTimePicker;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescribesFragment extends Fragment {

    public static final String KEY_META_LABEL = "meta_label";
    public static final String KEY_META_ENTERED = "meta_entered";
    public static ArrayList<String> metalabellist;
    public static ArrayList<String> metaEnteredlist;
    public Context context;
    LinearLayout llDescribe;
    String metadata;
    Button btn;
    int count;
    TextView tv_star;
    ImageView ivDatePicker;
    ArrayList<String> metalist = new ArrayList<>();
    String dynamicslid = DmsActivity.dynamicFileSlid;

    EditText etMeta;
    DatePickerDialog datePickerDialog;

    Button btnBack, btnNxt;

    TextView tvNoMetaFound;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private List<View> myLayouts = new ArrayList<>();

    private int year, month, day;

    private int mYear, mMonth, mDay, mHour, mMinute;

    Boolean error = true;

    //custom datetimepicker
    CustomDateTimePicker dateTimePicker;

    //metalist entered

    Map<String, String> metaEnteredListHash = new HashMap<>();

    ProgressDialog progressDialog;

    public DescribesFragment() {
        // Required empty public constructor


    }

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(getActivity(), null);
        if (lang == null) {

            LocaleHelper.persist(getActivity(), "en");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(LocaleHelper.onAttach(context, "en"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_describes, container, false);

        initLocale();

        llDescribe = v.findViewById(R.id.ll_fragment_describes);
        // btn= v.findViewById(R.id.addbtn_describes);
        btnBack = v.findViewById(R.id.back);
        btnBack.setVisibility(View.INVISIBLE);

        tvNoMetaFound = v.findViewById(R.id.tv_upload_frag_describe_nometafound);

        // sharedPreferences = getActivity().getSharedPreferences("jsonobject", 0);
        //  editor = sharedPreferences.edit();

        metalabellist = new ArrayList<>();
        metaEnteredlist = new ArrayList<>();

        btnNxt = v.findViewById(R.id.next);
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metaEnteredlist.clear();
                metalabellist.clear();


                if (tvNoMetaFound.getVisibility() == View.VISIBLE) {


                    UploadActivity.stepView.done(true);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                    ft.replace(R.id.fl_upload_root, new UploadFileFragment());
                    ft.commit();

                    UploadActivity.stepView.go(1, true);

                } else {

                    Log.e("working", "1");
                    for (int i = 0; i < myLayouts.size(); i++) {


                        TextView tvMetaLabel = myLayouts.get(i).findViewById(R.id.tv_upload_describe_metaname);
                        EditText etMetadata = myLayouts.get(i).findViewById(R.id.et_upload_describe);
                        TextView tv_star = myLayouts.get(i).findViewById(R.id.tv_upload_describes_mandatory);
                        TextView dataType = myLayouts.get(i).findViewById(R.id.tv_upload_describe_datatype);
                        //etMetadata.setText(list.get(i));


                        String metadata = String.valueOf(etMetadata.getText());
                        String metaname = String.valueOf(tvMetaLabel.getText());
                        String datatype = String.valueOf(dataType.getText());

                        metaEnteredListHash.put(metaname, metadata);

                        Log.e("datatype", datatype);

                        if (datatype.equalsIgnoreCase("datetime") && TextUtils.isEmpty(metadata)) {

                            if (tv_star.getVisibility() == View.VISIBLE) {

                                metadata = "";

                            } else {
                                metadata = "0000-00-00";
                            }


                            metalabellist.add(metaname);
                            metaEnteredlist.add(metadata);

                        } else if (datatype.equalsIgnoreCase("bit") && TextUtils.isEmpty(metadata)) {

                            if (tv_star.getVisibility() == View.VISIBLE) {

                                metadata = "";

                            } else {

                                metadata = "0";
                            }

                            metalabellist.add(metaname);
                            metaEnteredlist.add(metadata);

                        } else {

                            metalabellist.add(metaname);
                            metaEnteredlist.add(metadata);

                        }

                        Log.e("metadata", metadata);
                        Log.e("metaname", metaname);


                        Log.e("working", "2");

                        if (tv_star.getVisibility() == View.VISIBLE) {

                            Log.e("error flag star", String.valueOf(error));

                            if (metadata.equals("") || metadata.isEmpty() || metadata.length() == 0) {

                                Log.e("error flag star no b", String.valueOf(error));
                                etMetadata.setError(getString(R.string.please_fill_these_are_mandatory));

                                //shake where is error
//                                TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
//                                shake.setDuration(50);
//                                shake.setInterpolator(new CycleInterpolator(7));
//                                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
//                                etMetadata.startAnimation(animation);

                                error = true;
                                Toast.makeText(getActivity(), getString(R.string.please_fill_these_are_mandatory), Toast.LENGTH_SHORT).show();


                                Log.e("error flag star no af", String.valueOf(error));
                                break;

                            } else {

                                error = false;

                                Log.e("error flag star n01", String.valueOf(error));

                            }

                        } else {

                            error = false;

                        }
                    }

                    JSONObject j = new JSONObject(metaEnteredListHash);
                    Log.e("metalist json", j.toString());


                    if (!error) {

                        UploadActivity.stepView.done(true);
                       /* Bundle bundle = new Bundle();
                        bundle.putStringArrayList("values",metaEnteredlist);
*/
                        UploadFileFragment fragment = new UploadFileFragment();
                        //fragment.setArguments(bundle);

                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                        ft.replace(R.id.fl_upload_root, fragment);
                        ft.commit();

                        UploadActivity.stepView.go(1, true);


                    }


                }


                // count = metalabellist.size();
                //Log.e("count", String.valueOf(count));
                //   Log.e("metalabel",Arrays.toString(new ArrayList[]{metalabellist}));
                //  Log.e("metaname",Arrays.toString(new ArrayList[]{metaEnteredlist}));


                // editor.putString(KEY_META_LABEL,Arrays.toString(new ArrayList[]{metalabellist}) );
                //editor.putString(KEY_META_ENTERED,Arrays.toString(new ArrayList[]{metaEnteredlist}));


            }


        });

     /*   btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<myLayouts.size();i++){

                    EditText etMetadata=myLayouts.get(i).findViewById(R.id.et_upload_describe);
                    TextView tv_star = myLayouts.get(i).findViewById(R.id.tv_upload_describes_mandatory);

                    String metadata = etMetadata.getText().toString();

                    if(tv_star.getVisibility()==View.VISIBLE){

                        if(metadata.equals("")||metadata.isEmpty()){


                            etMetadata.setError("Please fill these are mandatory");

                        }

                        else{

                            etMetadata.setError(null);


                        }

                        }

                        else{


                        Log.e("working","fy9");

                    }


                    Log.e("mylayouts in fragment","position  "+i+" metadata "+metadata);


                }



            }
        });
*/


        // getMetadata(dynamicslid);
        checkMandatory(dynamicslid);

        return v;
    }


    private void createDynamicView(String metaname, String length, String mandatory, String datatype) {


        int etMetaDatePos = 0;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
        View rowView = inflater.inflate(R.layout.upload_describes_dynamic_tv_et, null);

        myLayouts.add(rowView);

        TextView textView = rowView.findViewById(R.id.tv_upload_describe_metaname);
        textView.setText(metaname);

        TextView tvDatatype = rowView.findViewById(R.id.tv_upload_describe_datatype);
        tvDatatype.setText(datatype);

        //RelativeLayout rlMain = rowView.findViewById(R.id.rl_upload_describe);
        ivDatePicker = rowView.findViewById(R.id.date_picker_describe);
        etMeta = rowView.findViewById(R.id.et_upload_describe);

        Log.e("metaname", metaname);


         /*   JSONObject j = new JSONObject(metaEnteredListHash);
            Iterator<String> iter = j.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object metalabel = j.get(key);



                    if(metaname.equalsIgnoreCase(String.valueOf(metalabel))){

                        etMeta.setText(j.getString(String.valueOf(metalabel)));

                    };



                } catch (JSONException e) {
                    // Something went wrong!
                }


        }
*/


        if (datatype.equals("varchar")) {

            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
            etMeta.setInputType(InputType.TYPE_CLASS_TEXT);
            if (length.equals("")) {


            } else {

                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});

        } else if (datatype.equals("bit")) {

            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
            etMeta.setHint("Data should " + length + " only");

            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});

        } else if (datatype.equals("float")) {

            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


            if (length.equals("")) {


            } else {

                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
        } else if (datatype.equals("Int")) {

            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (length.equals("")) {


            } else {

                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
        } else if (datatype.equals("BigInt")) {

            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (length.equals("")) {


            } else {

                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }

        } else if (datatype.equals("TEXT")) {

            etMeta.setHint("");

        } else if (datatype.equals("char")) {

            ivDatePicker.setVisibility(View.GONE);
            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
            //  etMeta.setInputType(InputType.);
            if (length.equals("")) {


            } else {

                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }


        } else if (datatype.equals("datetime")) {


            // get Position
            String strPosition = (String) etMeta.getTag();
            Log.e("EtMeta"," datetime getTag : >"+strPosition);
            // getId
            int strId = etMeta.getId();
            Log.e("EtMeta"," datetime getId : >"+strId);


            int childpos = llDescribe.indexOfChild(etMeta.getRootView());
            Log.e("EtMeta"," datetime childpos : >"+childpos);

            etMeta.setTag(etMetaDatePos);
            etMetaDatePos++;

            //ivDatePicker.setVisibility(View.VISIBLE);
            etMeta.setHint("yyyy-mm-dd");
            // etMeta.setKeyListener(null);
            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
            etMeta.setFocusable(false);
            etMeta.setFocusableInTouchMode(false);
            etMeta.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
            etMeta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    dateTimePicker = new CustomDateTimePicker((AppCompatActivity) getActivity(), new CustomDateTimePicker.ICustomDateTimeListener() {
                        @Override
                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {

                            etMeta.setText("");

                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));

                            String Month = "";

                            if (monthNumber + 1 <= 9) {

                                Month = "0" + (monthNumber + 1);

                            } else {

                                Month = String.valueOf(monthNumber + 1);
                            }

                            String secounds = String.valueOf(sec);
                            String minutes = String.valueOf(min);

                            if(min<10)
                            {
                                minutes = "0"+minutes;

                            }

                            if(sec<10){

                                secounds = "0"+secounds;
                            }

                            EditText et = view.findViewWithTag(view.getTag());
                            Log.e("etMeta getTag()", String.valueOf(view.getTag()));
                            et.setText(year
                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                    + " " + hour12 + ":" + minutes + ":" + secounds
                            );



                        }

                        @Override
                        public void onCancel() {

                            dateTimePicker.dismissDialog();

                        }
                    });

                    dateTimePicker.set24HourFormat(true);
                    dateTimePicker.setDate(Calendar.getInstance());
                    dateTimePicker.showDialog();


//                    // Get Current Date
//                    final Calendar c = Calendar.getInstance();
//                    mYear = c.get(Calendar.YEAR);
//                    mMonth = c.get(Calendar.MONTH);
//                    mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                    new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                            Log.e("date_picker", "year" + year + "\n" + "month" + (month + 1) + "\n" + "day" + dayOfMonth);
//
//                            if (month + 1 <= 10) {
//
//                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
//
//
//                            } else {
//
//                                etMeta.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
//
//                            }
//
//                            if (dayOfMonth <= 10) {
//
//                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + "0" + dayOfMonth);
//
//                            } else {
//
//                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
//                            }
//
//                        }
//                    }, mYear, mMonth, mDay).show();


                }
            });
          /* if(etMeta.getText().toString().isEmpty()||etMeta.getText().toString().equalsIgnoreCase("")){

               etMeta.setText("0000-00-00");
           }*/

        } else if (datatype.equals("DATETIME")) {

            // ivDatePicker.setVisibility(View.VISIBLE);
            etMeta.setHint("yyyy-mm-dd");
            etMeta.setFocusable(false);

            // get Position
            String strPosition = (String) etMeta.getTag();
            Log.e("EtMeta"," DATETIME getTag : >"+strPosition);
            // getId
            int strId = etMeta.getId();
            Log.e("EtMeta"," DATETIME getId : >"+strId);

            int childpos = llDescribe.indexOfChild(etMeta.getRootView());
            Log.e("EtMeta"," DATETIME childpos : >"+childpos);

            // etMeta.setTag(etMeta.getId());


            etMeta.setTag(etMetaDatePos);
            etMetaDatePos++;

            etMeta.setFocusableInTouchMode(false);
            etMeta.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
            // etMeta.setKeyListener(null);
            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
            etMeta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dateTimePicker = new CustomDateTimePicker((AppCompatActivity) getActivity(), new CustomDateTimePicker.ICustomDateTimeListener() {
                        @Override
                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {

                            etMeta.setText("");

                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));

                            String Month = "";

                            if (monthNumber + 1 <= 9) {

                                Month = "0" + (monthNumber + 1);

                            } else {

                                Month = String.valueOf(monthNumber + 1);
                            }

                            String secounds = String.valueOf(sec);
                            String minutes = String.valueOf(min);

                            if(min<10)
                            {
                                minutes = "0"+minutes;

                            }

                            if(sec<10){

                                secounds = "0"+secounds;
                            }

                            EditText et = view.findViewWithTag(view.getTag());
                            Log.e("etMeta getTag()", String.valueOf(view.getTag()));
                            et.setText(year
                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                    + " " + hour12 + ":" + minutes + ":" + secounds
                            );



                        }

                        @Override
                        public void onCancel() {

                            dateTimePicker.dismissDialog();

                        }
                    });

                    dateTimePicker.set24HourFormat(true);
                    dateTimePicker.setDate(Calendar.getInstance());
                    dateTimePicker.showDialog();


//                    // Get Current Date
//                    final Calendar c = Calendar.getInstance();
//                    mYear = c.get(Calendar.YEAR);
//                    mMonth = c.get(Calendar.MONTH);
//                    mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                    new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                            Log.e("date_picker", "year" + year + "\n" + "month" + (month + 1) + "\n" + "day" + dayOfMonth);
//
//                            if (month + 1 <= 10) {
//
//                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
//
//
//                            } else {
//
//                                etMeta.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
//
//                            }
//
//                            if (dayOfMonth <= 10) {
//
//                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + "0" + dayOfMonth);
//
//                            } else {
//
//                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
//                            }
//
//
//                        }
//                    }, mYear, mMonth, mDay).show();


                }
            });



          /* if(etMeta.getText().toString().isEmpty()||etMeta.getText().toString().equalsIgnoreCase("")){

               etMeta.setText("0000-00-00");
           }*/

        } else {

            ivDatePicker.setVisibility(View.GONE);
            etMeta.setHint(getString(R.string.data_length_shouldnt_exceed_from) + " " + length + " " + getString(R.string.characters));
            if (length.equals("")) {


            } else {

                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
        }


        tv_star = rowView.findViewById(R.id.tv_upload_describes_mandatory);
        if (mandatory.equals("Yes")) {


            tv_star.setVisibility(View.VISIBLE);


        } else {
            tv_star.setVisibility(View.GONE);

        }

//        ivDatePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
////                // Get Current Date
////                final Calendar c = Calendar.getInstance();
////                mYear = c.get(Calendar.YEAR);
////                mMonth = c.get(Calendar.MONTH);
////                mDay = c.get(Calendar.DAY_OF_MONTH);
////
////
////                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
////                    @Override
////                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
////
////
////                        if (month <= 10) {
////
////                            etMeta.setText(year + "-" + "0" + month + "-" + dayOfMonth);
////
////
////                        } else {
////
////                            etMeta.setText(year + "-" + month + "-" + dayOfMonth);
////
////                        }
////
////                        if (dayOfMonth <= 10) {
////
////                            etMeta.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
////
////                        } else {
////
////                            etMeta.setText(year + "-" + "0" + month + "-" + dayOfMonth);
////                        }
////
////
////                    }
////                }, mYear, mMonth, mDay).show();
//
//
//            }
//        });

        llDescribe.addView(rowView, llDescribe.getChildCount() - 1);
        progressDialog.dismiss();
        btnNxt.setEnabled(true);


    }

    //getting the list of metadata in dropdown of each indiviual folder dynamically
    private void checkMandatory(final String slid) {

        btnNxt.setEnabled(false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.loading_metadata));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECK_MANDATORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("mandatory", response);
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {

                        tvNoMetaFound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        btnNxt.setEnabled(true);

                    } else {

                        tvNoMetaFound.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fieldname = jsonArray.getJSONObject(i).getString("field_name");
                            String size = jsonArray.getJSONObject(i).getString("length_data");
                            String mandatorystatus = jsonArray.getJSONObject(i).getString("mandatory");
                            String datatype = jsonArray.getJSONObject(i).getString("data_type");

                            //5 times
                            createDynamicView(fieldname, size, mandatorystatus, datatype);


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("slid", slid);
                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private void getMetadata(final String slid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("metadata", response);
                try {

                    JSONArray jsonArrayMetadata = new JSONArray(response);

                    for (int i = 0; i < jsonArrayMetadata.length(); i++) {

                        String array = jsonArrayMetadata.get(i).toString();
                        metalist.add(array);

                    }

                    for (int i = 0; i < metalist.size(); i++) {

                        //createDynamicView(metalist.get(i), "255", "Yes",);
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
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("slid", slid);


                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


}
