package in.cbslgroup.ezeepeafinal.ui.activity.workflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.adapters.list.DetailsOfTravelingAdapter;
import in.cbslgroup.ezeepeafinal.interfaces.ActivityCommanMethods;
import in.cbslgroup.ezeepeafinal.model.City;
import in.cbslgroup.ezeepeafinal.model.DetailsOfTravel;
import in.cbslgroup.ezeepeafinal.model.response.AdvanceSanctionResponse;
import in.cbslgroup.ezeepeafinal.model.response.AdvanceSanctionSubmitResponse;
import in.cbslgroup.ezeepeafinal.model.response.DOTAdvanceSanction;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.databinding.ActivityAdvanceSanctionedBinding;
import in.cbslgroup.ezeepeafinal.network.RetrofitSingelton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvanceSanctionedActivity extends AppCompatActivity implements ActivityCommanMethods {

    private ActivityAdvanceSanctionedBinding binding;
    private SessionManager session;
    private DetailsOfTravelingAdapter detailsOfTravelingAdapter;

    List<DetailsOfTravel> detailsOfTravelList= new ArrayList<>();
    List<City> cityList= new ArrayList<>();

    AdvanceSanctionResponse advanceSanctionResponse;

    String ticketId;
    String wfid ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intializing the activity comman methods
        init(this);

        //expand all at first
        onToggleBasicInfo();
        onToggleBankDetails();
        onToggleDetailsOfTravel();

        try {

            getSanctionedAdvanceDetails(ticketId);
        }

        catch (Exception e){

            e.printStackTrace();
        }



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

    private void init(Context context) {

        ActivityCommanMethods methods = (ActivityCommanMethods) context;
        methods.initBindingAndViewModels();
        methods.initSessionManager();
        methods.initToolbar();
        methods.initRecyclerViews();
        methods.initObservers();
        methods.initListeners();
        methods.setData();

        initLocale();


    }

    @Override
    public void initBindingAndViewModels() {

        Log.e(ActivityCommanMethods.TAG,"initBindingAndViewModels");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advance_sanctioned);

    }

    @Override
    public void initToolbar() {

        setSupportActionBar(binding.toolbarAdvanaceSancReq);
        binding.toolbarAdvanaceSancReq.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();

            }
        });

    }

    @Override
    public void initRecyclerViews() {

        binding.rvDetailsOfTravel.setLayoutManager(new LinearLayoutManager(this));
        detailsOfTravelingAdapter = new DetailsOfTravelingAdapter(this, detailsOfTravelList, cityList);
        detailsOfTravelingAdapter.setListener(new DetailsOfTravelingAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {


            }

            @Override
            public void onToPlaceSelected(int pos, String toPlace) {

            }

            @Override
            public void getTotalAdvance(int total) {

            }

            @Override
            public void getTotalSanctioned(int total) {

                setTotalSanctionedAmount();
                binding.tieAdvanceSancToAndFroFare.setText(total+"");

            }
        });
        binding.rvDetailsOfTravel.setAdapter(detailsOfTravelingAdapter);


    }

    @Override
    public void initObservers() {



    }

    @Override
    public void initListeners() {




        binding.btnAdvanceSancSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sumbitSanctionedFormSubmit();

            }
        });

        binding.btnAdvanceSancReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(AdvanceSanctionedActivity.this,AdvanceSanctionedActivity.class);
                intent.putExtra("ticketId",ticketId);
                intent.putExtra("wfid","");
                startActivity(intent);
                finish();


            }
        });


        binding.llAdvanceSancBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleBankDetails();
            }
        });

        binding.llAdvanceSancDetailsOfTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleDetailsOfTravel();
            }
        });

        binding.llAdvanceSancBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleBasicInfo();
            }
        });


        binding.tieAdvanceSancConyeanceSancAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setTotalSanctionedAmount();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tieAdvanceSancTelphoneSancAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setTotalSanctionedAmount();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tieAdvanceSancOtherSancAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setTotalSanctionedAmount();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.rgAdvanceSancBankDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == binding.rbAdvanceSancBankBankDetails.getId()) {

                    binding.elAdvanceSancBankBankDetails.toggle();
                    binding.elAdvanceSancBankCardDetails.toggle();

                    //detailType = "bank";

                } else if (i == binding.rbAdvanceSancBankCardDetails.getId()) {

                    binding.elAdvanceSancBankBankDetails.toggle();
                    binding.elAdvanceSancBankCardDetails.toggle();

                   // detailType = "card";

                }

            }
        });


    }

    @Override
    public void setData() {


        Intent intent = getIntent();
        ticketId = intent.getStringExtra("ticketId");
        wfid =  intent.getStringExtra("wfid");

    }

    @Override
    public void initSessionManager() {

        session = new SessionManager(this);

    }


    /**
     * Getting the Sanctioned Advance form details
     */

    void getSanctionedAdvanceDetails(String ticketid){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.loading_advance_sanctioned_form_details));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        RetrofitSingelton.getClient().getAdvanceSanctionedDetails("getSanctionedDetails",session.getUserId(),ticketid).enqueue(new Callback<AdvanceSanctionResponse>() {
            @Override
            public void onResponse(Call<AdvanceSanctionResponse> call, Response<AdvanceSanctionResponse> response) {

                if(response.isSuccessful()){

                    advanceSanctionResponse = response.body();

                    try{

                        if(advanceSanctionResponse !=null){

                            if(advanceSanctionResponse.getError().equals("false")){



                                binding.tieAdvanceSancCompany.setText(advanceSanctionResponse.getCompanyName());
                                binding.tieAdvanceSancDate.setText(advanceSanctionResponse.getDate());
                                binding.tieAdvanceSancNameOfThePersonTraveling.setText(advanceSanctionResponse.getFullname());
                                binding.tieAdvanceSancDesignation.setText(advanceSanctionResponse.getDesignation());
                                binding.tieAdvanceSancGrade.setText(advanceSanctionResponse.getGrade());
                                binding.tieAdvanceSancEmpId.setText(advanceSanctionResponse.getEmpid());
                                binding.tieAdvanceSancDateOfTravel.setText(advanceSanctionResponse.getDateOfTravel().equalsIgnoreCase("1970-01-01")?"":advanceSanctionResponse.getDateOfTravel());
                                binding.tieAdvanceSancDateOfReturn.setText(advanceSanctionResponse.getExpectedDateOfReturn().equalsIgnoreCase("1970-01-01")?"":advanceSanctionResponse.getExpectedDateOfReturn());
                                binding.tieAdvanceSancNameOfClientEventProject.setText(advanceSanctionResponse.getClientNameEvent());
                                binding.tieAdvanceSancNoOfDays.setText(advanceSanctionResponse.getNoofdays());

                                try {

                                    if(advanceSanctionResponse.getReceivetype().equalsIgnoreCase("1")) // means it is card
                                    {
                                        binding.rgAdvanceSancBankDetails.check(binding.rbAdvanceSancBankCardDetails.getId());
                                        binding.tieAdvanceSancCardDetailsBankName.setText(advanceSanctionResponse.getCardbankname());
                                        binding.tieAdvanceSancCardDetailsCardNo.setText(advanceSanctionResponse.getCardno());

                                        if(binding.elAdvanceSancBankBankDetails.isExpanded()){

                                            binding.elAdvanceSancBankBankDetails.collapse();
                                            binding.elAdvanceSancBankCardDetails.expand();
                                        }


                                    }

                                    else{


                                        binding.rgAdvanceSancBankDetails.check(binding.rbAdvanceSancBankBankDetails.getId());
                                        binding.tieAdvanceSancBankBankDetailsAccNo.setText(advanceSanctionResponse.getAccountno());
                                        binding.tieAdvanceSancBankBankDetailsBankName.setText(advanceSanctionResponse.getBankName());
                                        binding.tieAdvanceSancBankBankDetailsBranchName.setText(advanceSanctionResponse.getBranchName());
                                        binding.tieAdvanceSancBankBankDetailsIfscCode.setText(advanceSanctionResponse.getIfsccode());

                                        if(binding.elAdvanceSancBankCardDetails.isExpanded()){

                                            binding.elAdvanceSancBankBankDetails.expand();
                                            binding.elAdvanceSancBankCardDetails.collapse();
                                        }

                                    }


                                }
                                catch (Exception e){

                                    e.printStackTrace();

                                }



                                binding.cbAdvanceSancCheckCompanyHouseAvailable.setChecked(advanceSanctionResponse.getGuesthouseAvailable().equals("1"));

                                binding.tieAdvanceSancToAndFroFare.setText(advanceSanctionResponse.getToAndFroFare());

                                binding.tieAdvanceSancLodgingCharges.setText(advanceSanctionResponse.getLCharge());
                                binding.tieAdvanceSancLodgingChargesPayDayFor.setText(advanceSanctionResponse.getLPerDay());



                                String lChargeTotal = String.valueOf(Integer.parseInt(advanceSanctionResponse.getLCharge())*Integer.parseInt(advanceSanctionResponse.getLPerDay()));
                                binding.tieAdvanceSancLodgingChargesPayDayForTotal.setText(lChargeTotal);

                                binding.tieAdvanceSancFoodCharges.setText(advanceSanctionResponse.getFCharge());
                                binding.tieAdvanceSancFoodChargesPayDayFor.setText(advanceSanctionResponse.getFPerDay());

                                String fChargeTotal = String.valueOf(Integer.parseInt(advanceSanctionResponse.getFCharge())*Integer.parseInt(advanceSanctionResponse.getFPerDay()));
                                binding.tieAdvanceSancFoodChargesPayDayForTotal.setText(fChargeTotal);




                                binding.tieAdvanceSancLocalConyeance.setText(advanceSanctionResponse.getLConveyance());
                                binding.tieAdvanceSancConyeanceSancAmt.setText(advanceSanctionResponse.getLConveyance());

                                try{


                                    binding.tieAdvanceSancTelephone.setText(advanceSanctionResponse.getTelephoneCharge());
                                    binding.tieAdvanceSancTelphoneSancAmt.setText(advanceSanctionResponse.getTelephoneCharge());
                                }

                                catch (Exception e){


                                }


                                binding.tieAdvanceSancOtherExpenses.setText(advanceSanctionResponse.getOtherExpense());
                                binding.tieAdvanceSancOtherSancAmt.setText(advanceSanctionResponse.getOtherExpense());
                                binding.tieAdvanceSancOtherRemarks.setText(advanceSanctionResponse.getOtherexpenseremark());

                                binding.tieAdvanceSancTotalAdvance.setText(advanceSanctionResponse.getTotalAdvReq());
                                binding.tieAdvanceSancTotalSanctioned.setText(advanceSanctionResponse.getTotalAdvReq());

                                for (int i = 0; i < advanceSanctionResponse.getDOT().size() ; i++) {

                                    DOTAdvanceSanction item = advanceSanctionResponse.getDOT().get(i);
                                    detailsOfTravelingAdapter.addItem(new DetailsOfTravel(item.getSNo(),item.getDOT(),item.getMode(),item.getSource(),item.getDestination(),item.getJsonMemberClass(),item.getPurpose(),item.getAdvanceReq(),item.getTravelId(),item.getAdvanceReq(),true),i);

                                }
                                detailsOfTravelingAdapter.notifyDataSetChanged();

                                progressDialog.dismiss();

                            }

                            else{

                                progressDialog.dismiss();
                                Toast.makeText(AdvanceSanctionedActivity.this,advanceSanctionResponse.getMsg(),Toast.LENGTH_LONG).show();

                            }

                        }

                    }

                    catch(Exception e){

                        e.printStackTrace();

                    }


                }

                else{

                    progressDialog.dismiss();
                    Toast.makeText(AdvanceSanctionedActivity.this,R.string.something_went_wrong,Toast.LENGTH_LONG).show();


                }

            }

            @Override
            public void onFailure(Call<AdvanceSanctionResponse> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(AdvanceSanctionedActivity.this,R.string.something_went_wrong,Toast.LENGTH_LONG).show();


            }
        });


    }


    void sumbitSanctionedFormSubmit(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.intiate_advance_sanction_form));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();


        String toFareCharges = binding.tieAdvanceSancToAndFroFare.getText().toString();
        String telSancCharge = binding.tieAdvanceSancTelphoneSancAmt.getText().toString();
        String locCinyenaceSancCharge = binding.tieAdvanceSancConyeanceSancAmt.getText().toString();
        String otherSancCharge = binding.tieAdvanceSancOtherSancAmt.getText().toString();
        String lChargeTotal = String.valueOf(Integer.parseInt(advanceSanctionResponse.getLCharge())*Integer.parseInt(advanceSanctionResponse.getLPerDay()));
        String fChargeTotal = String.valueOf(Integer.parseInt(advanceSanctionResponse.getFCharge())*Integer.parseInt(advanceSanctionResponse.getFPerDay()));
        String totalSancAmt = binding.tieAdvanceSancTotalSanctioned.getText().toString();

        JsonArray sno = new JsonArray();
        JsonArray travelId = new JsonArray();
        JsonArray doftravel = new JsonArray();
        JsonArray purpose = new JsonArray();
        JsonArray arequired = new JsonArray();
        JsonArray sancAmt = new JsonArray();

        try {

            List<DetailsOfTravel> list = detailsOfTravelingAdapter.getList();
            for (int i = 0; i < list.size(); i++) {

                sno.add(list.get(i).getSno());
                travelId.add(list.get(i).getTravelId());
                doftravel.add(list.get(i).getDateOfTravel());
                purpose.add(list.get(i).getRemarks());
                arequired.add(list.get(i).getSancAmt());
                //sancAmt.add(list.get(i).getSancAmt());

            }


        }catch (Exception ex){

            ex.printStackTrace();

        }

        RetrofitSingelton.getClient().submitAdvanceSanctionForm("arf_submit_sanc",ticketId
                ,advanceSanctionResponse.getArfId()
                ,advanceSanctionResponse.getCompanyId()
                ,advanceSanctionResponse.getFullname()
                ,advanceSanctionResponse.getGrade()
                ,advanceSanctionResponse.getDateOfTravel()
                ,advanceSanctionResponse.getClientNameEvent()
                ,advanceSanctionResponse.getDate()
                ,advanceSanctionResponse.getDesignation()
                ,advanceSanctionResponse.getEmpid()
                ,advanceSanctionResponse.getExpectedDateOfReturn()
                ,advanceSanctionResponse.getNoofdays()
                ,advanceSanctionResponse.getBankName()
                ,advanceSanctionResponse.getBranchName()
                ,advanceSanctionResponse.getIfsccode()
                ,advanceSanctionResponse.getAccountno()
                ,advanceSanctionResponse.getGuesthouseAvailable()
                ,toFareCharges,
                advanceSanctionResponse.getLCharge(),
                advanceSanctionResponse.getLPerDay(),
                lChargeTotal,
                advanceSanctionResponse.getLCharge(),
                advanceSanctionResponse.getLPerDay(),
                fChargeTotal,
                advanceSanctionResponse.getLConveyance(),
                locCinyenaceSancCharge,
                advanceSanctionResponse.getTelephoneCharge(),
                telSancCharge,
                advanceSanctionResponse.getOtherExpense(),
                advanceSanctionResponse.getOtherexpenseremark(),
                otherSancCharge,
                advanceSanctionResponse.getTotalAdvReq(),
                totalSancAmt,session.getUserId(),
                sno.toString(),purpose.toString(),
                arequired.toString(),
                travelId.toString(),
                doftravel.toString()
                ).enqueue(new Callback<AdvanceSanctionSubmitResponse>() {
            @Override
            public void onResponse(Call<AdvanceSanctionSubmitResponse> call, Response<AdvanceSanctionSubmitResponse> response) {


                if(response.isSuccessful()){

                    if(response!=null){

                        AdvanceSanctionSubmitResponse res = response.body();
                        if(res.getError().equals("false")){

                            Toast.makeText(AdvanceSanctionedActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            setResult(120);
                            finish();

                        }
                        else{

                            progressDialog.dismiss();
                            Toast.makeText(AdvanceSanctionedActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    else{

                        progressDialog.dismiss();
                        Toast.makeText(AdvanceSanctionedActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

                    }


                }

                else{

                    progressDialog.dismiss();
                    Toast.makeText(AdvanceSanctionedActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<AdvanceSanctionSubmitResponse> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(AdvanceSanctionedActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

            }
        });

    }


    /**
     * Setting the total sanction amount
     */


    void setTotalSanctionedAmount(){

        String localConveyanceSancAmnt =  binding.tieAdvanceSancConyeanceSancAmt.getText().toString().trim();
        String telSancAmnt =  binding.tieAdvanceSancTelphoneSancAmt.getText().toString().trim();
        String otherSancAmnt =  binding.tieAdvanceSancOtherSancAmt.getText().toString().trim();
        String loadgingCharges =  binding.tieAdvanceSancLodgingChargesPayDayForTotal.getText().toString().trim();
        String foodCharges =  binding.tieAdvanceSancFoodChargesPayDayForTotal.getText().toString().trim();


        int lcSancAmt = Integer.parseInt(localConveyanceSancAmnt.equals("")?"0":localConveyanceSancAmnt);
        int telSancAmt = Integer.parseInt(telSancAmnt.equals("")?"0":telSancAmnt);
        int otherSancAmt = Integer.parseInt(otherSancAmnt.equals("")?"0":otherSancAmnt);
        int loadgingSancAmt = Integer.parseInt(loadgingCharges.equals("")?"0":loadgingCharges);
        int foodSancAmt = Integer.parseInt(foodCharges.equals("")?"0":foodCharges);

        int totalDetailsOfTravelSancAmt = detailsOfTravelingAdapter.getTotalSanctioned();
        int totalBrdingLodign = loadgingSancAmt+foodSancAmt;

        String total = String.valueOf(lcSancAmt+telSancAmt+otherSancAmt+totalDetailsOfTravelSancAmt+totalBrdingLodign);
        Log.e("total_sanc","total-->"+total+"\n"+
                "lcSancAmt-->"+lcSancAmt+"\n"+
                "telSancAmt-->"+telSancAmt+"\n"+
                "otherSancAmt-->"+ otherSancAmt+"\n"+
                "totalDetailsOfTravelSancAmt-->"+ totalDetailsOfTravelSancAmt+"\n"+
                "totalBrdingLodign-->"+ totalBrdingLodign);

        binding.tieAdvanceSancTotalSanctioned.setText(total);

    }


    /**
     * Toggles on ui expanding animation
     */

    private void onToggleBasicInfo() {
        if (binding.elBasicInfo.isExpanded()) {

            binding.elBasicInfo.toggle();
            binding.ivAdvanceSancBasicInfo.animate().rotation(0f).start();

        } else {

            binding.elBasicInfo.toggle();
            binding.ivAdvanceSancBasicInfo.animate().rotation(90f).start();
        }
    }

    private void onToggleDetailsOfTravel() {
        if (binding.elAdvanceSancDetailsOfTravel.isExpanded()) {

            binding.elAdvanceSancDetailsOfTravel.toggle();
            binding.ivAdvanceSancDetailsOfTravel.animate().rotation(0f).start();

        } else {

            binding.elAdvanceSancDetailsOfTravel.toggle();
            binding.ivAdvanceSancDetailsOfTravel.animate().rotation(90f).start();
        }
    }

    private void onToggleBankDetails() {
        if (binding.elAdvanceSancBankDetails.isExpanded()) {

            binding.elAdvanceSancBankDetails.toggle();
            binding.ivAdvanceSancBankDetails.animate().rotation(0f).start();

        } else {

            binding.elAdvanceSancBankDetails.toggle();
            binding.ivAdvanceSancBankDetails.animate().rotation(90f).start();
        }
    }




}
