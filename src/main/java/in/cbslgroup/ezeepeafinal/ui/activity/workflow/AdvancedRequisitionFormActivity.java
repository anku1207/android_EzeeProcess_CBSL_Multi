package in.cbslgroup.ezeepeafinal.ui.activity.workflow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.adapters.list.BottomSheetWithSearchAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.DetailsOfTravelingAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.DetailsOfTravelingPreviewAdapter;
import in.cbslgroup.ezeepeafinal.model.BottomSheetWithSearch;
import in.cbslgroup.ezeepeafinal.model.City;
import in.cbslgroup.ezeepeafinal.model.DetailsOfTravel;
import in.cbslgroup.ezeepeafinal.services.IntiateWorkflowService;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.DateUtil;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;
import in.cbslgroup.ezeepeafinal.databinding.ActivityAdvancedRequisitionFormBinding;
import in.cbslgroup.ezeepeafinal.databinding.AlertdialogAdvanceReqPreviewBinding;



public class AdvancedRequisitionFormActivity extends AppCompatActivity{

    private BroadcastReceiver broadcastReceiverClosePB;
    private AlertDialog alertDialogProcessing;

    private ActivityAdvancedRequisitionFormBinding binding;
    private SessionManager sessionManager;

    //Lists
    List<DetailsOfTravel> detailsOfTravelList = new ArrayList<>();
    List<City> cityList = new ArrayList<>();
    List<BottomSheetWithSearch> companyList = new ArrayList<>();

    //Adapters
    DetailsOfTravelingAdapter detailsOfTravelingAdapter;


    //variables
    String lodgingCharges = "0";
    String selectedCompanyId = "null";
    String detailType = "bank";
    String workflowId,workflowName;


    //arrays
    ArrayList<String> sno = new ArrayList<>();
    ArrayList<String> tmode = new ArrayList<>();
    ArrayList<String> dofTravel = new ArrayList<>();
    ArrayList<String> toDest = new ArrayList<>();
    ArrayList<String> fromDest = new ArrayList<>();
    ArrayList<String> mClass = new ArrayList<>();
    ArrayList<String> purpose = new ArrayList<>();
    ArrayList<String> arequired = new ArrayList<>();
    ArrayList<String> company = new ArrayList<>();


    //set mindate for date of travel
    public static  final long fiveDaysBefore = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000); //i.e 5 days before;
    public static  final long currentDate = System.currentTimeMillis() - 1000; //i.e 5 days before;
    long minDateForDOT = fiveDaysBefore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initViewModelsAndBinding();
        initLocale();
        initSessionManager();
        setData();
        initToolbar();
        initListeners();
        registerBroadCastReceivers();


        //init webmethods
        getUserDetails();
        getCity();
        getCompanyList();

        //expandAll
        onToggleBasicInfo();
        onToggleBankDetails();
        onToggleDetailsOfTravel();


    }

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(this, null);
        if (lang == null) {

            LocaleHelper.persist(this, "en");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    private void initToolbar() {

        setSupportActionBar(binding.toolbarAdvanaceReq);
        binding.toolbarAdvanaceReq.setSubtitle(workflowName);
        binding.toolbarAdvanaceReq.setNavigationOnClickListener(view ->

                onBackPressed()

        );

    }

    private void registerBroadCastReceivers() {

        broadcastReceiverClosePB = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                if ((getPackageName() + ".CLOSE_INITIATE_PB").equalsIgnoreCase(arg1.getAction())) {

                    String msg = arg1.getStringExtra("msg");
                    String error = arg1.getStringExtra("error");


                    if (error.equalsIgnoreCase("false")) {


                        alertProcessing(AdvancedRequisitionFormActivity.this);

                        Toast.makeText(arg0,msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(arg0, AdvancedRequisitionFormActivity.class);
                        intent.putExtra("workflowID", workflowId);
                        intent.putExtra("workflowName", workflowName);
                        startActivity(intent);

                        finish();


                    } else if (error.equalsIgnoreCase("true")) {


                        alertDialogProcessing.dismiss();
                        Toast.makeText(arg0, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }


                }

            }
        };

        registerReceiver(broadcastReceiverClosePB, new IntentFilter(getPackageName() + ".CLOSE_INITIATE_PB"));
    }

    private void ClearDetailsOfTravelArrayLists() {

        sno.clear();
        tmode.clear();
        dofTravel.clear();
        toDest.clear();
        fromDest.clear();
        mClass.clear();
        purpose.clear();
        arequired.clear();
        company.clear();

    }

    private void initRecyclerviews() {

        //adding the first
        // detailsOfTravelList.add(new DetailsOfTravel("","","","","","","",""));

        detailsOfTravelingAdapter = new DetailsOfTravelingAdapter(this, detailsOfTravelList, cityList);
        detailsOfTravelingAdapter.setListener(new DetailsOfTravelingAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {

                if (checkValidationDetailsOfTravelRecyclerView()) {

                    detailsOfTravelingAdapter.addItem(new DetailsOfTravel("", "", "Select Mode", "", "", "Select Class", "", ""), 0);
                   // detailsOfTravelingAdapter.addItem(new DetailsOfTravel("", "", "", "", "", "", "", ""), pos);
                    binding.rvDetailsOfTravel.smoothScrollToPosition(detailsOfTravelingAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onMinusButtonClicked(int pos) {

                detailsOfTravelingAdapter.removeItem(pos);

            }

            @Override
            public void onToPlaceSelected(int pos, String toPlace) {

                if (pos == 0) {

                    getLoadingAndFoodCharges(toPlace);

                }

            }

            @Override
            public void getTotalAdvance(int total) {

                binding.tieAdvanceToAndFroFare.setText(String.valueOf(total));
                totalAdvanceRequired();

            }

            @Override
            public void getTotalSanctioned(int total) {

            }
        });

        binding.rvDetailsOfTravel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvDetailsOfTravel.setAdapter(detailsOfTravelingAdapter);
        detailsOfTravelingAdapter.addItem(new DetailsOfTravel("", "", "Select Mode", "", "", "Select Class", "", ""), 0);

    }

    private void initListeners() {

        binding.tieAdvanceDate.setText(DateUtil.getDate());

        binding.tieAdvanceCardDetailsCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tilAdvanceCardDetailsCardNo.setError(null);
                binding.tilAdvanceCardDetailsCardNo.setErrorEnabled(false);

                if(s.toString().length()==0){

                    binding.tilAdvanceCardDetailsCardNo.setError(getString(R.string.field_is_required));
                }
                else if(s.toString().length()<16){

                    binding.tilAdvanceCardDetailsCardNo.setError(getString(R.string.card_detail_error_msg));

                }

                else {

                    binding.tilAdvanceCardDetailsCardNo.setError(null);
                    binding.tilAdvanceCardDetailsCardNo.setErrorEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.tieAdvanceTourId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tilAdvanceTourId.setErrorEnabled(false);
                binding.tilAdvanceTourId.setError(null);


                String text = binding.tieAdvanceTourId.getText().toString();
                if(text.length()!=0){

                    getDetailsFromTourId(binding.tieAdvanceTourId.getText().toString());

                }
                else{

                    minDateForDOT = fiveDaysBefore;
                    binding.tilAdvanceTourId.setErrorEnabled(true);
                    binding.tilAdvanceTourId.setError("Enter Tour Id");


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

//                String text = String.valueOf(s);
//                if(text.length()!=0){
//
//                    getDetailsFromTourId(text);
//
//                }
//                else{
//
//
//
//                }

            }
        });


//        binding.btnAdvanceTourId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                binding.tilAdvanceTourId.setErrorEnabled(false);
//                binding.tilAdvanceTourId.setError(null);
//
//
//                String text = binding.tieAdvanceTourId.getText().toString();
//                if(text.length()!=0){
//
//                    getDetailsFromTourId(binding.tieAdvanceTourId.getText().toString());
//                }
//                else{
//
//                    binding.tilAdvanceTourId.setErrorEnabled(true);
//                    binding.tilAdvanceTourId.setError("Enter Tour Id");
//
//                }
//
//            }
//        });

//        binding.tieAdvanceTourId.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    if(event.getRawX() >= ( binding.tieAdvanceTourId.getRight() -  binding.tieAdvanceTourId.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        // your action here
//
//
//
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        binding.btnAdvanceReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdvancedRequisitionFormActivity.this,AdvancedRequisitionFormActivity.class);
                workflowId = intent.getStringExtra("workflowID");
                workflowName = intent.getStringExtra("workflowName");
                startActivity(intent);
                finish();

            }
        });

        binding.cbAdvanceCheckCompanyHouseAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ifChecked) {


                if (ifChecked) {

                    binding.tieAdvanceLodgingCharges.setText("0");
                    // totalLoadingCharges();
                    totalAdvanceRequired();

                } else {

                    binding.tieAdvanceLodgingCharges.setText(lodgingCharges);
                    // totalLoadingCharges();
                    totalAdvanceRequired();
                }
            }
        });

        binding.btnAdvancePreview.setOnClickListener(view -> {

            if (checkWholeFormValidation()) {

                // get selected radio button from radioGroup
                int selectedId = binding.rgAdvanceBankDetails.getCheckedRadioButtonId();

                // find the radio button by returned id
                RadioButton radioButton = findViewById(selectedId);

                alertDialogPreview(radioButton.getText().toString());

            }

            else{

                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            }


        });


        binding.btnAdvanceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkWholeFormValidation()) {

                    if(!binding.tieAdvanceTourId.getText().toString().isEmpty()){

                      checkTourId(binding.tieAdvanceTourId.getText().toString());

                    }
                    else{

                        submitForm();
                    }

                }

            }
        });

        binding.tieAdvanceDate.setOnClickListener(view -> {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                    binding.tieAdvanceDate.setText(dt);

                }
            }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
            dpd.show();


        });

        binding.tieAdvanceDateOfTravel.setOnClickListener(view -> {

            binding.tilAdvanceDateOfReturn.setError(null);
            binding.tilAdvanceDateOfReturn.setErrorEnabled(false);

            binding.tilAdvanceDateOfTravel.setErrorEnabled(false);
            binding.tilAdvanceDateOfTravel.setError(null);

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;

                    binding.tieAdvanceDateOfTravel.setText(dt);

                    if (!binding.tieAdvanceDateOfReturn.getText().toString().equals("")) {

                        String d1 = binding.tieAdvanceDateOfTravel.getText().toString();
                        String d2 = binding.tieAdvanceDateOfReturn.getText().toString();

                        try {

                            binding.tieAdvanceNoOfDays.setText(String.valueOf(DateUtil.daysBetween2Dates(d1, d2)));

                        } catch (Exception e) {

                            Log.e("exp_date", e.toString());
                        }

                    }

                }
            }, mYear, mMonth, mDay);
            // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dpd.getDatePicker().setMinDate(minDateForDOT); //5 days before only
            //dpd.getDatePicker().setMinDate(minDate); //5 days before only
            dpd.show();



        });

        binding.tieAdvanceDateOfReturn.setOnClickListener(view -> {

           String dot = binding.tieAdvanceDateOfTravel.getText().toString();
           if(!dot.isEmpty()){

               // Get Current Date
               final Calendar c = Calendar.getInstance();
               int mYear = c.get(Calendar.YEAR);
               int mMonth = c.get(Calendar.MONTH);
               int mDay = c.get(Calendar.DAY_OF_MONTH);

               DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                       String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                       binding.tieAdvanceDateOfReturn.setText(dt);

                       if (!binding.tieAdvanceDateOfTravel.getText().toString().equals("")) {

                           String d1 = binding.tieAdvanceDateOfTravel.getText().toString();
                           String d2 = binding.tieAdvanceDateOfReturn.getText().toString();

                           try {

                               binding.tieAdvanceNoOfDays.setText(String.valueOf(DateUtil.daysBetween2Dates(d1, d2)));

                           }

                           catch (Exception e) {

                               Log.e("exp_date", e.toString());
                           }

                       }

                   }
               }, mYear, mMonth, mDay);

               //getting date selected in from date
               Date date = DateUtil.stringToDate( binding.tieAdvanceDateOfTravel.getText().toString());
               // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
               dpd.getDatePicker().setMinDate(date.getTime());
               dpd.show();

               binding.tilAdvanceDateOfReturn.setError(null);
               binding.tilAdvanceDateOfReturn.setErrorEnabled(false);

               binding.tilAdvanceDateOfTravel.setErrorEnabled(false);
               binding.tilAdvanceDateOfTravel.setError(null);

           }
           else{

               binding.tilAdvanceDateOfReturn.setError(getString(R.string.date_of_travel_field_is_empty));
               binding.tilAdvanceDateOfTravel.requestFocus();
               binding.tilAdvanceDateOfTravel.setError(getString(R.string.field_is_required));
           }



        });

        binding.tieAdvanceCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomSheetDialog(AdvancedRequisitionFormActivity.this, "Select Company", new BottomSheetWithSearchAdapter(companyList), true);

            }
        });

        binding.tieAdvanceLocalConyeance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                totalAdvanceRequired();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tieAdvanceOtherExpenses.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                totalAdvanceRequired();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tieAdvanceOtherExpenses.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                totalAdvanceRequired();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tieAdvanceNoOfDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tieAdvanceLodgingChargesPayDayFor.setText(charSequence.toString());
                binding.tieAdvanceFoodChargesPayDayFor.setText(charSequence.toString());

                //totalBoardingCharges();
                //totalLoadingCharges();

                totalAdvanceRequired();


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        binding.llAdvanceDetailsOfTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleDetailsOfTravel();

            }
        });

        binding.llAdvanceBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleBankDetails();
            }
        });

        binding.llAdvanceBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               onToggleBasicInfo();
            }
        });

        binding.rgAdvanceBankDetails.check(binding.rbAdvanceBankBankDetails.getId());

        binding.rgAdvanceBankDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == binding.rbAdvanceBankBankDetails.getId()) {

                    binding.elAdvanceBankBankDetails.toggle();
                    binding.elAdvanceBankCardDetails.toggle();

                    detailType = "bank";

                } else if (i == binding.rbAdvanceBankCardDetails.getId()) {

                    binding.elAdvanceBankBankDetails.toggle();
                    binding.elAdvanceBankCardDetails.toggle();

                    detailType = "card";

                }

            }
        });


    }



    private void onToggleBasicInfo() {
        if (binding.elBasicInfo.isExpanded()) {

            binding.elBasicInfo.toggle();
            binding.ivAdvanceBasicInfo.animate().rotation(0f).start();

        } else {

            binding.elBasicInfo.toggle();
            binding.ivAdvanceBasicInfo.animate().rotation(90f).start();
        }
    }

    private void onToggleBankDetails() {
        if (binding.elAdvanceBankDetails.isExpanded()) {

            binding.elAdvanceBankDetails.toggle();
            binding.ivAdvanceBankDetails.animate().rotation(0f).start();

        } else {

            binding.elAdvanceBankDetails.toggle();
            binding.ivAdvanceBankDetails.animate().rotation(90f).start();
        }
    }

    private void onToggleDetailsOfTravel() {

        if (binding.elAdvanceDetailsOfTravel.isExpanded()) {

            binding.elAdvanceDetailsOfTravel.toggle();
            binding.ivAdvanceDetailsOfTravel.animate().rotation(0f).start();

        } else {

            binding.elAdvanceDetailsOfTravel.toggle();
            binding.ivAdvanceDetailsOfTravel.animate().rotation(90f).start();
        }
    }

    private void initSessionManager() {

        sessionManager = new SessionManager(this);

    }

    private void setData() {

        Intent intent = getIntent();
        workflowId = intent.getStringExtra("workflowID");
        workflowName = intent.getStringExtra("workflowName");

        // binding.tieAdvanceDesignation.setText(sessionManager.getUserDetails().get(SessionManager.KEY_DESIGNATION));
        //binding.tieAdvanceNameOfThePersonTraveling.setText(sessionManager.getUserDetails().get(SessionManager.KEY_NAME));

    }

    private void initViewModelsAndBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_advanced_requisition_form);

    }


    //calculation methods
    int totalLoadingCharges() {

        int lodgingChrges = Integer.parseInt(binding.tieAdvanceLodgingCharges.getText().toString().equals("") ? "0" : binding.tieAdvanceLodgingCharges.getText().toString());
        int payDayFor = Integer.parseInt(binding.tieAdvanceLodgingChargesPayDayFor.getText().toString().equals("") ? "0" : binding.tieAdvanceLodgingChargesPayDayFor.getText().toString());
        String total = String.valueOf(lodgingChrges * payDayFor);
        binding.tieAdvanceLodgingChargesPayDayForTotal.setText(total);
        return Integer.parseInt(total);

    }

    int totalBoardingCharges() {

        int boardingCharges = Integer.parseInt(binding.tieAdvanceFoodCharges.getText().toString().equals("") ? "0" : binding.tieAdvanceFoodCharges.getText().toString());
        int payDayFor = Integer.parseInt(binding.tieAdvanceFoodChargesPayDayFor.getText().toString().equals("") ? "0" : binding.tieAdvanceFoodChargesPayDayFor.getText().toString());
        String total = String.valueOf(boardingCharges * payDayFor);
        binding.tieAdvanceFoodChargesPayDayForTotal.setText(total);
        return Integer.parseInt(total);
    }

    void totalAdvanceRequired() {

        totalLoadingCharges();
        totalBoardingCharges();

        int localConynce = Integer.parseInt(binding.tieAdvanceLocalConyeance.getText().toString().equals("") ? "0" : binding.tieAdvanceLocalConyeance.getText().toString());
        int tel = Integer.parseInt(binding.tieAdvanceTelephone.getText().toString().equals("") ? "0" : binding.tieAdvanceTelephone.getText().toString());
        int otherExp = Integer.parseInt(binding.tieAdvanceOtherExpenses.getText().toString().equals("") ? "0" : binding.tieAdvanceOtherExpenses.getText().toString());
        int totalAdvancedRequired = localConynce + tel + otherExp + totalBoardingCharges() + totalLoadingCharges() + detailsOfTravelingAdapter.getTotalAdvacnce();

        binding.tieAdvanceTotalAdvance.setText(String.valueOf(totalAdvancedRequired));

    }


    //bottomsheets and alert dialogs
    void alertDialogPreview(String bankDetailMode) {

        AlertdialogAdvanceReqPreviewBinding reqPreviewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.alertdialog_advance_req_preview, null, false);
        Dialog alertDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //, android.R.style.Theme_Black_NoTitleBar_Fullscreen
        //Buttons
        reqPreviewBinding.btnAdvReqPreviewCancel.setOnClickListener(view -> {

            alertDialog.dismiss();

        });



        //recyclerviews
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reqPreviewBinding.rvDetailsOfTravelPreview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        reqPreviewBinding.rvDetailsOfTravelPreview.addItemDecoration(dividerItemDecoration);


        Log.e("size_of_details", String.valueOf(detailsOfTravelingAdapter.getList().size()));

        for (DetailsOfTravel d : detailsOfTravelingAdapter.getList()) {

            Log.e("details_travel", d.toString());

        }

        DetailsOfTravelingPreviewAdapter detailsOfTravelingPreviewAdapter = new DetailsOfTravelingPreviewAdapter(detailsOfTravelingAdapter.getList());

        reqPreviewBinding.btnAdvReqPreviewSumbit.setOnClickListener(view -> {

            if(!binding.tieAdvanceTourId.getText().toString().isEmpty()){

                checkTourId(binding.tieAdvanceTourId.getText().toString());

            }
            else{

                submitForm();
            }

        });

        reqPreviewBinding.btnAdvReqPreviewCancel.setOnClickListener(view -> {

            alertDialog.dismiss();

        });

        reqPreviewBinding.rvDetailsOfTravelPreview.setAdapter(detailsOfTravelingPreviewAdapter);

        //either card or bank
        if (bankDetailMode.equalsIgnoreCase(getString(R.string.card_details))) {


            reqPreviewBinding.llAdvReqPreviewBankBankDetails.setVisibility(View.GONE);
            reqPreviewBinding.llAdvReqPreviewCardBankDetails.setVisibility(View.VISIBLE);


        } else {

            reqPreviewBinding.llAdvReqPreviewBankBankDetails.setVisibility(View.VISIBLE);
            reqPreviewBinding.llAdvReqPreviewCardBankDetails.setVisibility(View.GONE);
        }

        //Details
        reqPreviewBinding.tieAdvReqPreviewDate.setText(binding.tieAdvanceDate.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewDateOfTravel.setText(binding.tieAdvanceDateOfTravel.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewNameOfThePersonTraveling.setText(binding.tieAdvanceNameOfThePersonTraveling.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewGrade.setText(binding.tieAdvanceGrade.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewNameOfTheClientEvent.setText(binding.tieAdvanceNameOfClientEventProject.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewEmpId.setText(binding.tieAdvanceEmpId.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewNoOfDays.setText(binding.tieAdvanceNoOfDays.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewDesig.setText(binding.tieAdvanceDesignation.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewExpDateOfReturn.setText(binding.tieAdvanceDateOfReturn.getText().toString());

        //Bank details -->bank
        reqPreviewBinding.tieAdvReqPreviewBankName.setText(binding.tieAdvanceBankBankDetailsBankName.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewBranchName.setText(binding.tieAdvanceBankBankDetailsBranchName.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewIfsc.setText(binding.tieAdvanceBankBankDetailsIfscCode.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewAccNo.setText(binding.tieAdvanceBankBankDetailsAccNo.getText().toString());

        //Bank details -->card
        reqPreviewBinding.tieAdvReqPreviewCardBankName.setText(binding.tieAdvanceCardDetailsBankName.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewCardNo.setText(binding.tieAdvanceCardDetailsCardNo.getText().toString());

        //calculation of estimate cost
        reqPreviewBinding.tieAdvReqPreviewToAndForFare.setText(binding.tieAdvanceToAndFroFare.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewLocalConveyance.setText(binding.tieAdvanceLocalConyeance.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewTelCharges.setText(binding.tieAdvanceTelephone.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewOtherExpenses.setText(binding.tieAdvanceOtherExpenses.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewRemarks.setText(binding.tieAdvanceOtherRemarks.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewTotalAdvReq.setText(binding.tieAdvanceTotalAdvance.getText().toString());

        reqPreviewBinding.tieAdvReqPreviewLodgingCharges.setText(binding.tieAdvanceLodgingCharges.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewLodgingChargesPayDayFor.setText(binding.tieAdvanceLodgingChargesPayDayFor.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewLodgingChargesPayDayForTotal.setText(binding.tieAdvanceLodgingChargesPayDayForTotal.getText().toString());

        reqPreviewBinding.tieAdvReqPreviewFoodingCharges.setText(binding.tieAdvanceFoodCharges.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewFoodingChargesPayDayFor.setText(binding.tieAdvanceFoodChargesPayDayFor.getText().toString());
        reqPreviewBinding.tieAdvReqPreviewFoodingChargesTotal.setText(binding.tieAdvanceFoodChargesPayDayForTotal.getText().toString());

        //dialogBuilder.setView(reqPreviewBinding.getRoot());

        alertDialog.setContentView(reqPreviewBinding.getRoot());
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alertDialog.show();


    }

    //methods helper
    void alertProcessing(Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_alert, null);
        dialogBuilder.setView(dialogView);

        Button btnRunInBg = dialogView.findViewById(R.id.progress_alert_run_in_bg);
        btnRunInBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                if (alertDialogProcessing.isShowing()) {
                    alertDialogProcessing.dismiss();

                }

            }
        });

        alertDialogProcessing = dialogBuilder.create();
        alertDialogProcessing.setCancelable(false);
        alertDialogProcessing.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogProcessing.show();


    }

    private void showBottomSheetDialog(Context context, String title, BottomSheetWithSearchAdapter bottomSheetWithSearchAdapter, Boolean wantSearchFilter) {

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

                binding.tieAdvanceCompany.setText(obj.getValue());
                selectedCompanyId = obj.getKey();
                bottomSheetDialog.dismiss();


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

    //validation methods

    private boolean checkValidationDetailsOfTravelRecyclerView() {

        boolean isAllFieldFilled = true;

        ClearDetailsOfTravelArrayLists();


        if (!binding.elAdvanceDetailsOfTravel.isExpanded()) {

            binding.elAdvanceDetailsOfTravel.toggle();
            binding.ivAdvanceDetailsOfTravel.animate().rotation(90f).start();

        }



        for (int i = 0; i < detailsOfTravelingAdapter.getItemCount(); i++) {

            View itemView = binding.rvDetailsOfTravel.getLayoutManager().findViewByPosition(i);

            TextInputEditText tieSno = itemView.findViewById(R.id.tie_details_of_travel_sno);
            TextInputEditText tieDateOfTravel = itemView.findViewById(R.id.tie_details_of_travel_date_of_travel);
            TextInputEditText tieAdvReq = itemView.findViewById(R.id.tie_details_of_travel_adv_req);
            TextInputEditText tieDestFrom = itemView.findViewById(R.id.tie_details_of_travel_dest_from);
            TextInputEditText tieDestTo = itemView.findViewById(R.id.tie_details_of_travel_dest_to);
            TextInputEditText tieMode = itemView.findViewById(R.id.tie_details_of_travel_mode);
            TextInputEditText tieClass = itemView.findViewById(R.id.tie_details_of_travel_class);
            TextInputEditText tieRemarks = itemView.findViewById(R.id.tie_details_of_travel_remarks);

            TextInputLayout tilDateOfTravel = itemView.findViewById(R.id.til_details_of_travel_date_of_travel);
            TextInputLayout tilAdvReq = itemView.findViewById(R.id.til_details_of_travel_adv_req);
            TextInputLayout tilDestFrom = itemView.findViewById(R.id.til_details_of_travel_dest_from);
            TextInputLayout tilDestTo = itemView.findViewById(R.id.til_details_of_travel_dest_to);
            TextInputLayout tilMode = itemView.findViewById(R.id.til_details_of_travel_mode);
            TextInputLayout tilClass = itemView.findViewById(R.id.til_details_of_travel_class);

//            if(detailsOfTravelingAdapter.getModeClassList().size()<0){
//
//                tilClass.setError(getString(R.string.field_is_required));
//                isAllFieldFilled=false;
//
//            }
//            else{
//
//                tilClass.setErrorEnabled(false);
//                tilClass.setError(null);
//
//            }

            //adding  arraylist for dynamic arrays
            sno.add(tieSno.getText().toString());
            dofTravel.add(tieDateOfTravel.getText().toString());
            arequired.add(tieAdvReq.getText().toString());
            fromDest.add(tieDestFrom.getText().toString());
            toDest.add(tieDestTo.getText().toString());
            tmode.add(tieMode.getText().toString());
            mClass.add(tieClass.getText().toString());
            purpose.add(tieRemarks.getText().toString());



            if (tieDateOfTravel.getText().toString().trim().isEmpty()) {

                tilDateOfTravel.setError(getString(R.string.field_is_required));
                isAllFieldFilled = false;

            } else {

                tilDateOfTravel.setErrorEnabled(false);
                tilDateOfTravel.setError(null);
            }

            if (tieMode.getText().toString().trim().isEmpty()) {

                tilMode.setError(getString(R.string.field_is_required));
                isAllFieldFilled = false;

            } else {

                tilMode.setErrorEnabled(false);
                tilMode.setError(null);
            }


            if (tieAdvReq.getText().toString().trim().isEmpty()) {

                tilAdvReq.setError(getString(R.string.field_is_required));
                isAllFieldFilled = false;

            } else {

                tilAdvReq.setErrorEnabled(false);
                tilAdvReq.setError(null);
            }

            if (tieDestFrom.getText().toString().trim().isEmpty()) {

                tilDestFrom.setError(getString(R.string.field_is_required));
                isAllFieldFilled = false;

            } else {

                tilDestFrom.setErrorEnabled(false);
                tilDestFrom.setError(null);

            }

            if (tieDestTo.getText().toString().trim().isEmpty()) {

                tilDestTo.setError(getString(R.string.field_is_required));
                isAllFieldFilled = false;

            } else {

                tilDestTo.setErrorEnabled(false);
                tilDestTo.setError(null);
            }

            if(tieMode.getText().toString().equalsIgnoreCase("Select Mode")){

                tilMode.setError(getString(R.string.field_is_required));
                tilClass.setError("Select mode first");
                isAllFieldFilled = false;

            }
            else{


                tilMode.setErrorEnabled(false);
                tilMode.setError(null);


                if(!tieClass.isEnabled()){

                    tilClass.setErrorEnabled(false);
                    tilClass.setError(null);
                }

                else if (tieClass.getText().toString().trim().isEmpty()) {

                    tilClass.setError(getString(R.string.field_is_required));
                    isAllFieldFilled = false;

                } else {

                    tilClass.setErrorEnabled(false);
                    tilClass.setError(null);
                }
            }


        }

        return isAllFieldFilled;

    }

    private boolean checkValidationOfStaticFields() {

        boolean fillAllFields = true;

        if (!binding.elBasicInfo.isExpanded()) {

            binding.elBasicInfo.toggle();
            binding.ivAdvanceBasicInfo.animate().rotation(90f).start();

        }

        if (!binding.elAdvanceBankDetails.isExpanded()) {

            binding.elAdvanceBankDetails.toggle();
            binding.ivAdvanceBankDetails.animate().rotation(90f).start();

        }

        if (binding.tieAdvanceCompany.getText().toString().trim().isEmpty()) {

            binding.tilAdvanceCompany.setError(getString(R.string.field_is_required));
            fillAllFields = false;

        } else {

            binding.tilAdvanceCompany.setErrorEnabled(false);
            binding.tilAdvanceCompany.setError(null);
        }

        if (binding.tieAdvanceDate.getText().toString().trim().isEmpty()) {

            binding.tilAdvanceDate.setError(getString(R.string.field_is_required));
            fillAllFields = false;
        } else {

            binding.tilAdvanceDate.setErrorEnabled(false);
            binding.tilAdvanceDate.setError(null);
        }

        if (binding.tieAdvanceNameOfThePersonTraveling.getText().toString().trim().isEmpty()) {

            binding.tilAdvanceNameOfThePersonTraveling.setError(getString(R.string.field_is_required));
            fillAllFields = false;
        } else {

            binding.tilAdvanceNameOfThePersonTraveling.setErrorEnabled(false);
            binding.tilAdvanceNameOfThePersonTraveling.setError(null);
        }

        if (binding.tieAdvanceDateOfTravel.getText().toString().trim().isEmpty()) {

            binding.tilAdvanceDateOfTravel.setError(getString(R.string.field_is_required));
            fillAllFields = false;
        } else {

            binding.tilAdvanceDateOfTravel.setErrorEnabled(false);
            binding.tilAdvanceDateOfTravel.setError(null);
        }

        if (binding.tieAdvanceDateOfReturn.getText().toString().trim().isEmpty()) {

            binding.tilAdvanceDateOfReturn.setError(getString(R.string.field_is_required));
            fillAllFields = false;
        } else {

            binding.tilAdvanceDateOfReturn.setErrorEnabled(false);
            binding.tilAdvanceDateOfReturn.setError(null);
        }

        if (binding.tieAdvanceNameOfClientEventProject.getText().toString().trim().isEmpty()) {

            binding.tilAdvanceNameOfClientEventProject.setError(getString(R.string.field_is_required));
            fillAllFields = false;
        } else {

            binding.tilAdvanceNameOfClientEventProject.setErrorEnabled(false);
            binding.tilAdvanceNameOfClientEventProject.setError(null);
        }

        // get selected radio button from radioGroup
        int selectedId = binding.rgAdvanceBankDetails.getCheckedRadioButtonId();
        // find the radio button by returned id
        RadioButton radioButton = findViewById(selectedId);

        if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.card_details))) {

            if (binding.tieAdvanceCardDetailsBankName.getText().toString().trim().isEmpty()) {

                binding.tilAdvanceCardDetailsBankName.setError(getString(R.string.field_is_required));
                fillAllFields = false;

            } else {

                binding.tilAdvanceCardDetailsBankName.setErrorEnabled(false);
                binding.tilAdvanceCardDetailsBankName.setError(null);
            }


            if (binding.tieAdvanceCardDetailsCardNo.getText().toString().trim().isEmpty()) {

                binding.tilAdvanceCardDetailsCardNo.setError(getString(R.string.field_is_required));
                fillAllFields = false;


            } else {

                binding.tilAdvanceCardDetailsCardNo.setErrorEnabled(false);
                binding.tilAdvanceCardDetailsCardNo.setError(null);
            }

            if (binding.tieAdvanceCardDetailsCardNo.getText().toString().length() == 0) {

                binding.tilAdvanceCardDetailsCardNo.setError(getString(R.string.card_detail_error_msg));
                fillAllFields = false;


            } else {

                binding.tilAdvanceCardDetailsCardNo.setErrorEnabled(false);
                binding.tilAdvanceCardDetailsCardNo.setError(null);
            }



        } else {

            if (binding.tieAdvanceBankBankDetailsBankName.getText().toString().trim().isEmpty()) {

                binding.tilAdvanceBankBankDetailsBankName.setError(getString(R.string.field_is_required));
                fillAllFields = false;
            } else {

                binding.tilAdvanceBankBankDetailsBankName.setErrorEnabled(false);
                binding.tilAdvanceBankBankDetailsBankName.setError(null);
            }


            if (binding.tieAdvanceBankBankDetailsBranchName.getText().toString().trim().isEmpty()) {

                binding.tilAdvanceBankBankDetailsBranchName.setError(getString(R.string.field_is_required));
                fillAllFields = false;
            } else {

                binding.tilAdvanceBankBankDetailsBranchName.setErrorEnabled(false);
                binding.tilAdvanceBankBankDetailsBranchName.setError(null);
            }


            if (binding.tieAdvanceBankBankDetailsAccNo.getText().toString().trim().isEmpty()) {

                binding.tilAdvanceBankBankDetailsAccNo.setError(getString(R.string.field_is_required));
                fillAllFields = false;
            } else {

                binding.tilAdvanceBankBankDetailsAccNo.setErrorEnabled(false);
                binding.tilAdvanceBankBankDetailsAccNo.setError(null);
            }


            if (binding.tieAdvanceBankBankDetailsIfscCode.getText().toString().trim().isEmpty()) {

                binding.tilAdvanceBankBankDetailsIfscCode.setError(getString(R.string.field_is_required));
                fillAllFields = false;
            } else {

                binding.tilAdvanceBankBankDetailsIfscCode.setErrorEnabled(false);
                binding.tilAdvanceBankBankDetailsIfscCode.setError(null);
            }

        }


        return fillAllFields;


    }

    boolean checkWholeFormValidation(){

        boolean isAllFieldsFilled = true;

        if(!checkValidationOfStaticFields()){

            isAllFieldsFilled = false;
        }

        if(!checkValidationDetailsOfTravelRecyclerView()){

            isAllFieldsFilled = false;
        }

        return isAllFieldsFilled;
    }

    //web methods

    private void submitForm() {

        HashMap<String, String> map = new HashMap<>();
        map.put("action", "arf_submit");
        map.put("wfName", workflowName);
        map.put("previewChk", "1");
        map.put("tdate", binding.tieAdvanceDate.getText().toString());
        map.put("fullname", binding.tieAdvanceNameOfThePersonTraveling.getText().toString());
        map.put("employeeid", binding.tieAdvanceEmpId.getText().toString());
        map.put("cdes_user_id", sessionManager.getUserId());
        map.put("grade", binding.tieAdvanceGrade.getText().toString());
        map.put("designation", binding.tieAdvanceDesignation.getText().toString());
        map.put("traveldate", binding.tieAdvanceDateOfTravel.getText().toString());
        map.put("returndate", binding.tieAdvanceDateOfReturn.getText().toString());
        map.put("nclient", binding.tieAdvanceNameOfClientEventProject.getText().toString());

        //bank nor credit card
        map.put("bankname", binding.tieAdvanceBankBankDetailsBankName.getText().toString());
        map.put("branchname", binding.tieAdvanceBankBankDetailsBranchName.getText().toString());
        map.put("ifsccode", binding.tieAdvanceBankBankDetailsIfscCode.getText().toString());
        map.put("accountno", binding.tieAdvanceBankBankDetailsAccNo.getText().toString());
        map.put("receivetype", detailType.equalsIgnoreCase("bank") ? "0" : "1"); //bank selected is 0 or credit is 1
        map.put("cardbankname", binding.tieAdvanceCardDetailsBankName.getText().toString());
        map.put("cardno", binding.tieAdvanceCardDetailsCardNo.getText().toString());

        //dynamic array starts
        map.put("sno", new JSONArray(sno).toString());
        map.put("tmode", new JSONArray(tmode).toString());
        map.put("doftravel", new JSONArray(dofTravel).toString());
        map.put("to", new JSONArray(toDest).toString());
        map.put("from", new JSONArray(fromDest).toString());
        map.put("class",new JSONArray(mClass).toString());
        map.put("purpose",new JSONArray(purpose).toString());
        map.put("arequired",new JSONArray(arequired).toString());
        //dynamic array ends

        map.put("otherexpenseremark", binding.tieAdvanceOtherRemarks.getText().toString());
        map.put("noofdays", binding.tieAdvanceNoOfDays.getText().toString());

        map.put("guesthouse", binding.cbAdvanceCheckCompanyHouseAvailable.isChecked() ? "1" : "0");

        map.put("company", selectedCompanyId);
        map.put("wfid", workflowId);
        map.put("tourid", binding.tieAdvanceTourId.getText().toString());

        map.put("to_fro_fare", binding.tieAdvanceToAndFroFare.getText().toString());
        map.put("lodging_charge", binding.tieAdvanceLodgingCharges.getText().toString());
        map.put("per_day_for", binding.tieAdvanceLodgingChargesPayDayFor.getText().toString());
        map.put("food_charge", binding.tieAdvanceFoodCharges.getText().toString());
        map.put("per_day_for_food", binding.tieAdvanceFoodChargesPayDayFor.getText().toString());
        map.put("local_convyance", binding.tieAdvanceLocalConyeance.getText().toString());
        map.put("telephone", binding.tieAdvanceTelephone.getText().toString());
        map.put("otherexpense", binding.tieAdvanceOtherExpenses.getText().toString());
        map.put("Total", binding.tieAdvanceTotalAdvance.getText().toString());
        map.put("advance_required", binding.tieAdvanceTotalAdvance.getText().toString());

        Intent i = new Intent(this, IntiateWorkflowService.class);
        i.putExtra("method","arf_form");
        i.putExtra("wfName",workflowName);
        i.putExtra("params",map);
        startService(i);

        alertProcessing(this);

    }

    private void getUserDetails() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_user_details", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    if (error.equalsIgnoreCase("false")) {

                        JSONObject jbUser = jsonObject.getJSONObject("user_details");

                        String fullname = jbUser.getString("fullname");
                        String designation = jbUser.getString("designation");
                        String empid = jbUser.getString("empid");
                        String grade = jbUser.getString("grade");

                        binding.tieAdvanceDesignation.setText(designation);
                        binding.tieAdvanceNameOfThePersonTraveling.setText(fullname);
                        binding.tieAdvanceEmpId.setText(empid);
                        binding.tieAdvanceGrade.setText(grade);

                        JSONObject jbBank = jsonObject.getJSONObject("bank_details");

                        String bank_name = jbBank.isNull("bank_name")?"":jbBank.getString("bank_name");
                        String ifcs_code = jbBank.isNull("ifcs_code")?"":jbBank.getString("ifcs_code");
                        String account_no =jbBank.isNull("account_no")?"": jbBank.getString("account_no");
                        String branch_name = jbBank.isNull("branch")?"":jbBank.getString("branch");



                        binding.tieAdvanceBankBankDetailsBankName.setText(bank_name);
                        binding.tieAdvanceBankBankDetailsBranchName.setText(branch_name);
                        binding.tieAdvanceBankBankDetailsIfscCode.setText(ifcs_code);
                        binding.tieAdvanceBankBankDetailsAccNo.setText(account_no);


                    } else {


                    }


                } catch (JSONException e) {
                    Log.e("response_user_details", e.toString());
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
                Map<String, String> map = new HashMap<>();
                map.put("action", "getUserDetails");
                map.put("userid", sessionManager.getUserId());
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getCity() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_get_city", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsUser = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsUser.length(); i++) {

                            String cityName = jsUser.getJSONObject(i).getString("city_id");
                            String cityId = jsUser.getJSONObject(i).getString("city_name");

                            cityList.add(new City(cityName, cityId));

                        }

                        initRecyclerviews();


                    } else {


                    }


                } catch (JSONException e) {
                    Log.e("response_get_city", e.toString());
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
                Map<String, String> map = new HashMap<>();
                map.put("action", "getCity");
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getLoadingAndFoodCharges(String toPlace) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String err = jsonObject.getString("error");
                    if (err.equals("false")) {


                        String loadging = jsonObject.getString("lodging");
                        String boarding = jsonObject.getString("boarding");//fooding

                        //need in chckbox previious loding charges
                        lodgingCharges = loadging;

                        binding.tieAdvanceLodgingCharges.setText(loadging);
                        binding.tieAdvanceFoodCharges.setText(boarding);

                        totalAdvanceRequired();


                    } else {

                        binding.tieAdvanceLodgingCharges.setText("0");
                        binding.tieAdvanceFoodCharges.setText("0");
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
                Map<String, String> map = new HashMap<>();
                map.put("action", "getLodgingFooding");
                map.put("toPlace", toPlace);
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getCompanyList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String cityid = jsonArray.getJSONObject(i).getString("c_id");
                            String cityname = jsonArray.getJSONObject(i).getString("c_name");

                            companyList.add(new BottomSheetWithSearch(cityid, cityname));

                        }

                    } else {


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
                Map<String, String> map = new HashMap<>();
                map.put("action", "getCompanyList");
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void checkTourId(String tourid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if(error.equalsIgnoreCase("false")){

                        submitForm();

                    }
                    else{

                        Toast.makeText(AdvancedRequisitionFormActivity.this,msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", "getAdvanceReqDetailsByTourId");
                map.put("tourid", tourid);
                return map;
            }

        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getDetailsFromTourId(String tourid){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.getting_data_from_tour_id));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        //progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getAdvanceDetails",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if(error.equalsIgnoreCase("false")){

                              /*  "name_cep": "2344",
                                "mode": "Air",
                                "dot": "2020-06-24",
                                "source": "Abhayapuri",
                                "destination": "Abhayapuri",
                                "class": "Business",
                                "purpose": "356",
                                "dor": "24-06-2020"*/


                        String mode = jsonObject.getString("mode");
                        String dot = jsonObject.getString("dot");
                        String source = jsonObject.getString("source");
                        String destination = jsonObject.getString("destination");
                        String mclass = jsonObject.getString("class");
                        //String purpose = jsonObject.getString("purpose");
                       // String dor = jsonObject.getString("dor");
                        String name_ecp = jsonObject.getString("name_cep");


//                        try {
//
//
//
//                        }
//                        catch (Exception e){
//
//                            e.printStackTrace();
//
//                        }

                        binding.tieAdvanceDateOfTravel.setText("");
                        binding.tieAdvanceDateOfReturn.setText("");
                        binding.tieAdvanceNoOfDays.setText("0");
                        binding.tieAdvanceNameOfClientEventProject.setText(name_ecp);

                        detailsOfTravelingAdapter.clearAll();
                        detailsOfTravelingAdapter.addItem(new DetailsOfTravel("1",dot,mode,source,destination,mclass,"","","","",false),0);


                        progressDialog.cancel();
                        binding.tilAdvanceTourId.setErrorEnabled(false);
                        binding.tilAdvanceTourId.setError(null);

                        Toast.makeText(AdvancedRequisitionFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                       // binding.tilAdvanceTourId.setErrorTextColor(ColorStateList.valueOf(Color.BLUE));


                        minDateForDOT = currentDate;

                        String l_charge = jsonObject.getString("l_charge");
                        String l_per_day = jsonObject.getString("l_per_day");
                        String f_charge = jsonObject.getString("f_charge");
                        String f_per_day = jsonObject.getString("f_per_day");

                        binding.tieAdvanceFoodCharges.setText(f_charge);
                        binding.tieAdvanceFoodChargesPayDayFor.setText(f_per_day);

                        binding.tieAdvanceLodgingCharges.setText(l_charge);
                        binding.tieAdvanceLodgingChargesPayDayFor.setText(l_per_day);

                        int fTotal = Integer.parseInt(f_charge)*Integer.parseInt(f_per_day);
                        int lTotal = Integer.parseInt(l_charge)*Integer.parseInt(l_per_day);

                        binding.tieAdvanceLodgingChargesPayDayForTotal.setText(lTotal+"");
                        binding.tieAdvanceFoodChargesPayDayForTotal.setText(fTotal+"");


                    }


                    else{

                       // Toast.makeText(AdvancedRequisitionFormActivity.this,msg, Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        binding.tilAdvanceTourId.setError(msg);

                        minDateForDOT = fiveDaysBefore;



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.cancel();
                    minDateForDOT = fiveDaysBefore;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                minDateForDOT = fiveDaysBefore;
                progressDialog.cancel();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", "getAdvanceReqDetailsByTourId");
                map.put("tourid", tourid);

                Util.printParams(map,"getTouridDetails");
                return map;
            }

        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);
        progressDialog.setButton("cancel",(dialog, which) -> stringRequest.cancel());
       // progressDialog.show();


    }



}
