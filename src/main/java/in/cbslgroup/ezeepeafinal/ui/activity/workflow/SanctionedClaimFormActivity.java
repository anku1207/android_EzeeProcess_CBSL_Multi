package in.cbslgroup.ezeepeafinal.ui.activity.workflow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.adapters.list.AttachmentAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.ClaimBoardingAndLodgingExpensesAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.ClaimLocalConveyanceAndOutstationVisitAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.DetailsOfTravelingExpensesAdapter;
import in.cbslgroup.ezeepeafinal.interfaces.ActivityCommanMethods;
import in.cbslgroup.ezeepeafinal.model.Attachments;
import in.cbslgroup.ezeepeafinal.model.City;
import in.cbslgroup.ezeepeafinal.model.ClaimBoardingAndLodging;
import in.cbslgroup.ezeepeafinal.model.response.DefaultResponse;
import in.cbslgroup.ezeepeafinal.model.DetailsOfTravelExpenses;
import in.cbslgroup.ezeepeafinal.model.LocalConveyanceAndOnOutstationVisit;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.BLAttachItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.BLItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.DOTAttachItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.DOTItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.LCAttItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.LCItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.MiscAttachItem;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.SanctionClaimResponse;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.databinding.ActivitySanctionedClaimFormBinding;
import in.cbslgroup.ezeepeafinal.network.RetrofitSingelton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SanctionedClaimFormActivity extends AppCompatActivity implements ActivityCommanMethods {

    SanctionClaimResponse sanctionClaimResponse;


    ActivitySanctionedClaimFormBinding binding;

    //Adapters
    DetailsOfTravelingExpensesAdapter detailsOfTravelingExpensesAdapter;
    ClaimBoardingAndLodgingExpensesAdapter claimBoardingAndLodgingExpensesAdapter;
    ClaimLocalConveyanceAndOutstationVisitAdapter claimLocalConveyanceAndOutstationVisitAdapter;

    //Adapters for attachments
    AttachmentAdapter attachDetailsAdapter;
    AttachmentAdapter attachbordingLoadgingAdapter;
    AttachmentAdapter attachmiscAdapter;
    AttachmentAdapter attachlocalConveyanceAdapter;
    AttachmentAdapter attachMainAdapter;

    //Attachment Lists
    List<Attachments> detailAttachmentsList = new ArrayList();
    List<Attachments> bordingLoadgingAttachmentsList = new ArrayList();
    List<Attachments> miscAttachmentsList = new ArrayList();
    List<Attachments> localConveyanceAttachmentsList = new ArrayList();
    List<Attachments> mainAttachmentsList = new ArrayList();

    //Lists
    List<DetailsOfTravelExpenses> detailsOfTravelList = new ArrayList<>();
    List<City> cityList = new ArrayList<>();

    //List Boarding and Lodging
    List<ClaimBoardingAndLodging> claimBoardingAndLodgingList = new ArrayList<>();

    //LocalConveyanceAndOnOutstationVisit
    List<LocalConveyanceAndOnOutstationVisit> localConveyanceAndOnOutstationVisitList = new ArrayList<>();


    String ticketId,claimId,sanctionedAction;

    SessionManager sessionManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initLocale();

        try{
            init(this);

            getSanctionedData("getSanctionClaimFormDetails",ticketId,claimId);

            //starting all expanded
            onToggleDetailsOfTravel();
            onToggleBoardingAndLodging();
            onToggleLocalConveyance();
            onToggleMiscExp();
            onToggleBasicInfo();


        }
        catch (Exception e){

            e.printStackTrace();
        }



    }


    private void init(Context context) {

        ActivityCommanMethods methods = (ActivityCommanMethods) context;
        methods.initBindingAndViewModels();
        methods.initToolbar();
        methods.initRecyclerViews();
        methods.initObservers();
        methods.initListeners();
        methods.setData();
        methods.initSessionManager();

    }


    @Override
    public void initBindingAndViewModels() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sanctioned_claim_form);

    }

    @Override
    public void initToolbar() {

        setSupportActionBar(binding.toolbarSanctionedClaimFormTravelExpense);
        binding.toolbarSanctionedClaimFormTravelExpense.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();

            }
        });


    }

    @Override
    public void initRecyclerViews() {


        //attachment rvs for details
        binding.rvAttachmentDetailsOfTravel.setLayoutManager(new LinearLayoutManager(this));
        attachDetailsAdapter = new AttachmentAdapter(detailAttachmentsList, this);
        attachDetailsAdapter.setListener(new AttachmentAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {


            }
        });
        binding.rvAttachmentDetailsOfTravel.setAdapter(attachDetailsAdapter);


        //attachment rvs for boarding
        binding.rvAttachmentBoardingLoading.setLayoutManager(new LinearLayoutManager(this));
        attachbordingLoadgingAdapter = new AttachmentAdapter(bordingLoadgingAttachmentsList, this);
        attachbordingLoadgingAdapter.setListener(new AttachmentAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                bordingLoadgingAttachmentsList.remove(pos);
                attachbordingLoadgingAdapter.notifyDataSetChanged();

            }
        });
        binding.rvAttachmentBoardingLoading.setAdapter(attachbordingLoadgingAdapter);

        //misc
        //attachment rvs for misc
        binding.rvAttachmentMisc.setLayoutManager(new LinearLayoutManager(this));
        attachmiscAdapter = new AttachmentAdapter(miscAttachmentsList, this);
        attachmiscAdapter.setListener(new AttachmentAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                miscAttachmentsList.remove(pos);
                attachmiscAdapter.notifyDataSetChanged();

            }
        });
        binding.rvAttachmentMisc.setAdapter(attachmiscAdapter);


        //local convey
        //attachment rvs for local convey
        binding.rvAttachmentLocalConvey.setLayoutManager(new LinearLayoutManager(this));
        attachlocalConveyanceAdapter = new AttachmentAdapter(localConveyanceAttachmentsList, this);
        attachlocalConveyanceAdapter.setListener(new AttachmentAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                localConveyanceAttachmentsList.remove(pos);
                attachlocalConveyanceAdapter.notifyDataSetChanged();

            }
        });
        binding.rvAttachmentLocalConvey.setAdapter(attachlocalConveyanceAdapter);


        //1
        detailsOfTravelingExpensesAdapter = new DetailsOfTravelingExpensesAdapter(detailsOfTravelList, cityList, this);
        detailsOfTravelingExpensesAdapter.setListener(new DetailsOfTravelingExpensesAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                detailsOfTravelingExpensesAdapter.removeItem(pos);
            }

            @Override
            public void getTotalSanctionedAmt(int total) {

                binding.tieSanctionedClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalSanctioned.setText(String.valueOf(total));
                setTotalSanctionedAmount();


            }

            @Override
            public void getTotalClaimAmt(int total) {

                binding.tieSanctionedClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalSanctionedClaimed.setText(String.valueOf(total));
                //totalClaim_1_2_3_4_Amount();

            }
        });



        binding.rvSanctionedClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.rvSanctionedClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvSanctionedClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.setAdapter(detailsOfTravelingExpensesAdapter);
       // detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses("", "", "", "", "0", "0", "", "", "", "", "", "",true), 0);


        //2
        claimBoardingAndLodgingExpensesAdapter = new ClaimBoardingAndLodgingExpensesAdapter(claimBoardingAndLodgingList, this);
        claimBoardingAndLodgingExpensesAdapter.setListener(new ClaimBoardingAndLodgingExpensesAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {




            }

            @Override
            public void onMinusButtonClicked(int pos) {


            }

            @Override
            public void getTotalSanctionedAmt(int total) {

                binding.tieSanctionedClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalSanctioned.setText(String.valueOf(total));
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();

                setTotalSanctionedAmount();


            }

            @Override
            public void getTotalClaimAmt(int total) {

                binding.tieSanctionedClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalSanctionedClaimed.setText(String.valueOf(total));
                //totalClaim_1_2_3_4_Amount();
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();
            }
        });


        binding.rvSanctionedClaimBoardingAndLodging.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.rvSanctionedClaimBoardingAndLodging.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvSanctionedClaimBoardingAndLodging.setAdapter(claimBoardingAndLodgingExpensesAdapter);
        //claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging("", "", "0", "0", "", "0", "0"), 0);


        ///4
        claimLocalConveyanceAndOutstationVisitAdapter = new ClaimLocalConveyanceAndOutstationVisitAdapter(localConveyanceAndOnOutstationVisitList, this);
        claimLocalConveyanceAndOutstationVisitAdapter.setListener(new ClaimLocalConveyanceAndOutstationVisitAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                claimLocalConveyanceAndOutstationVisitAdapter.removeItem(pos);
            }

            @Override
            public void getTotalSanctionedAmt(int total) {

                binding.tieSanctionedClaimLocalConveyTotalSancAmt.setText(String.valueOf(total));
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();
                setTotalSanctionedAmount();


            }

            @Override
            public void getTotalClaimAmt(int total) {

                binding.tieSanctionedClaimLocalConveyTotalSanctionedClaimAmt.setText(String.valueOf(total));
                //totalClaim_1_2_3_4_Amount();
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();
            }
        });
        binding.rvSanctionedClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.rvSanctionedClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvSanctionedClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.setAdapter(claimLocalConveyanceAndOutstationVisitAdapter);
        //claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit("", "", "", "", "", "", "", "", "0", "0"), 0);



    }

    @Override
    public void initObservers() {

    }

    @Override
    public void initListeners() {


        //Toggles
        binding.llSanctionedClaimBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleBasicInfo();

            }
        });

        binding.llSanctionedClaimDetailsOfTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleDetailsOfTravel();

            }
        });

        binding.llSanctionedClaimBoardingAndLodging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleBoardingAndLodging();

            }
        });

        binding.llSanctionedClaimLocalConveyance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleLocalConveyance();

            }
        });

        binding.llSanctionedClaimMiscExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleMiscExp();
            }
        });
        //Toggles end

        binding.btnSanctionedClaimReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SanctionedClaimFormActivity.this,SanctionedClaimFormActivity.class);
                intent.putExtra("ticketId",ticketId);
                intent.putExtra("wfid","");
                intent.putExtra("claimId",claimId);
                startActivity(intent);
                finish();

            }
        });


        binding.btnSanctionedClaimSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitSanctionedForm();

            }
        });

        binding.btnSanctionedClaimEntitlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEntitlement(sanctionClaimResponse.getEntitlement());


            }
        });




        binding.tieSanctionedClaimMiscTelSanctionedClaimAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setSanctionedTotalMisc();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tieSanctionedClaimMiscInternetSancAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setSanctionedTotalMisc();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tieSanctionedClaimMiscAgentChrgTicketBookingSancAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setSanctionedTotalMisc();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tieSanctionedClaimMiscOtherExpSancAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setSanctionedTotalMisc();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    @Override
    public void setData() {

        Intent intent = getIntent();
        ticketId = intent.getStringExtra("ticketId");
         //wfid =  intent.getStringExtra("wfid");
        claimId =  intent.getStringExtra("claimId");
        sanctionedAction =  intent.getStringExtra("sa");

    }

    @Override
    public void initSessionManager() {


         sessionManager = new SessionManager(this);

    }

    /**
     * Get All Sanctioned Data
     */

    void getSanctionedData(String action, String ticketid ,String claimid){


        try {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading Claim Sanctioned Form Details");
            progressDialog.setMessage("please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            RetrofitSingelton.getClient().getSanctionedClaimDetails(action,ticketid,claimid).enqueue(new Callback<SanctionClaimResponse>() {
                @Override
                public void onResponse(Call<SanctionClaimResponse> call, Response<SanctionClaimResponse> response) {

                    if(response.isSuccessful()) {

                        if (response != null) {


                            sanctionClaimResponse = response.body();

                            if(sanctionClaimResponse.getError().equalsIgnoreCase("false")){

                                String sanc = sanctionClaimResponse.getSanctionedChk();

                                binding.tieSanctionedClaimCompany.setText(sanctionClaimResponse.getCompanyName());
                                binding.tieSanctionedClaimCompany.setEnabled(false);

                                binding.tieSanctionedClaimDivision.setText(sanctionClaimResponse.getDevisionName());
                                binding.tieSanctionedClaimDivision.setEnabled(false);

                                binding.tieSanctionedClaimDept.setText(sanctionClaimResponse.getDepartmentName());
                                binding.tieSanctionedClaimDept.setEnabled(false);

                                binding.tieSanctionedClaimSite.setText(sanctionClaimResponse.getSiteName());
                                binding.tieSanctionedClaimSite.setEnabled(false);

                                binding.tieSanctionedClaimEmpName.setText(sanctionClaimResponse.getName());
                                binding.tieSanctionedClaimEmpName.setEnabled(false);

                                binding.tieSanctionedClaimDesignation.setText(sanctionClaimResponse.getDesignation());
                                binding.tieSanctionedClaimDesignation.setEnabled(false);

                                binding.tieSanctionedClaimGrade.setText(sanctionClaimResponse.getGrade());
                                binding.tieSanctionedClaimGrade.setEnabled(false);

                                binding.tieSanctionedClaimEmpId.setText(sanctionClaimResponse.getEmpId());
                                binding.tieSanctionedClaimEmpId.setEnabled(false);

                                binding.tieSanctionedClaimTourid.setText(sanctionClaimResponse.getTourTicketId());
                                binding.tieSanctionedClaimTourid.setEnabled(false);

                                binding.tieSanctionedClaimFrom.setText(sanctionClaimResponse.getFromDate());
                                binding.tieSanctionedClaimFrom.setEnabled(false);

                                binding.tieSanctionedClaimTo.setText(sanctionClaimResponse.getToDate());
                                binding.tieSanctionedClaimTo.setEnabled(false);

                                binding.tieSanctionedClaimPurposeOfTour.setText(sanctionClaimResponse.getPurposeTour());
                                binding.tieSanctionedClaimPurposeOfTour.setEnabled(false);


                                if (sanctionClaimResponse.getTaskComplete().equalsIgnoreCase("Yes")) {

                                    binding.rbAssignedJobCompletedYes.setChecked(true);
                                    binding.rbAssignedJobCompletedYes.setEnabled(false);
                                    binding.rbAssignedJobCompletedNo.setEnabled(false);


                                } else {

                                    binding.rbAssignedJobCompletedNo.setChecked(true);
                                    binding.rbAssignedJobCompletedNo.setEnabled(false);
                                    binding.rbAssignedJobCompletedYes.setEnabled(false);
                                }




                                //Misc
                                //tel
                                binding.tieSanctionedClaimMiscTelSanctionedClaimAmt.setText(sanctionClaimResponse.getMisc().getTelephoneClaim());
                                binding.tieSanctionedClaimMiscTelSanctionedClaimAmt.setEnabled(false);


                                binding.tieSanctionedClaimMiscTelRemarks.setText(sanctionClaimResponse.getMisc().getTelephoneRemarks());
                                binding.tieSanctionedClaimMiscTelRemarks.setEnabled(false);

                                //sanc
                                String sancTel = sanc.equals("1")?sanctionClaimResponse.getMisc().getTelephoneSantioned() :sanctionClaimResponse.getMisc().getTelephoneClaim();
                                binding.tieSanctionedClaimMiscTelSancAmt.setText(sancTel);
                                binding.tieSanctionedClaimMiscTelSancAmt.setEnabled(true);

                                //Internet

                                binding.tieSanctionedClaimMiscInternetSanctionedClaimAmt.setText(sanctionClaimResponse.getMisc().getInternetClaim());
                                binding.tieSanctionedClaimMiscInternetSanctionedClaimAmt.setEnabled(false);

                                //sanc
                                String sancInternet = sanc.equals("1")?sanctionClaimResponse.getMisc().getInternetSantioned() :sanctionClaimResponse.getMisc().getInternetClaim();
                                binding.tieSanctionedClaimMiscInternetSancAmt.setText(sancInternet);
                                binding.tieSanctionedClaimMiscInternetSancAmt.setEnabled(true);


                                binding.tieSanctionedClaimMiscInternetRemarks.setText(sanctionClaimResponse.getMisc().getInternetRemarks());
                                binding.tieSanctionedClaimMiscInternetRemarks.setEnabled(false);


                                //Agent charges and Ticket booking
                                binding.tieSanctionedClaimMiscAgentChrgTicketBookingSanctionedClaimAmt.setText(sanctionClaimResponse.getMisc().getTicketClaim());
                                binding.tieSanctionedClaimMiscAgentChrgTicketBookingSanctionedClaimAmt.setEnabled(false);

                                //sanc
                                String sancAgent = sanc.equals("1")?sanctionClaimResponse.getMisc().getTicketSantioned() :sanctionClaimResponse.getMisc().getTicketClaim();
                                binding.tieSanctionedClaimMiscAgentChrgTicketBookingSancAmt.setText(sancAgent);
                                binding.tieSanctionedClaimMiscAgentChrgTicketBookingSancAmt.setEnabled(true);

                                binding.tieSanctionedClaimMiscAgentChrgTicketBookingRemarks.setText(sanctionClaimResponse.getMisc().getAgentRemarks());
                                binding.tieSanctionedClaimMiscAgentChrgTicketBookingRemarks.setEnabled(false);


                                //Other Expenses
                                binding.tieSanctionedClaimMiscOtherExpSanctionedClaimAmt.setText(sanctionClaimResponse.getMisc().getOtherExpenseClaim());
                                binding.tieSanctionedClaimMiscOtherExpSanctionedClaimAmt.setEnabled(false);

                                //sanc
                                String sancExpense = sanc.equals("1")?sanctionClaimResponse.getMisc().getOtherExpenseSantioned() :sanctionClaimResponse.getMisc().getOtherExpenseClaim();
                                binding.tieSanctionedClaimMiscOtherExpSancAmt.setText(sancExpense);
                                binding.tieSanctionedClaimMiscOtherExpSancAmt.setEnabled(true);

                                binding.tieSanctionedClaimMiscOtherExpRemarks.setText(sanctionClaimResponse.getMisc().getOtherRemarks());
                                binding.tieSanctionedClaimMiscOtherExpRemarks.setEnabled(false);


                                //Set total misc
                                setSanctionedTotalMisc();


                                //Misc Attachments
                                for (int j = 0; j <sanctionClaimResponse.getMiscAttach().size() ; j++) {

                                    MiscAttachItem at = sanctionClaimResponse.getMiscAttach().get(j);
                                    miscAttachmentsList.add(new Attachments(at.getOldDocName(),"",at.getDocExtn(),at.getDocId(),true));
                                }
                                attachmiscAdapter.notifyDataSetChanged();


                                //DOT
                                for (int i = 0; i <sanctionClaimResponse.getDOT().size() ; i++) {

                                    DOTItem dotItem = sanctionClaimResponse.getDOT().get(i);
                                    String sancDOT = sanc.equalsIgnoreCase("1")?dotItem.getSantionedAmt():dotItem.getClaimAmt();
                                    detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses(dotItem.getSno(), dotItem.getMode(), dotItem.getJsonMemberClass(), dotItem.getTravelingRemarks(), dotItem.getClaimAmt(),sancDOT, dotItem.getDepartureDate(), dotItem.getDeparturePlace(), dotItem.getDepartureTime(), dotItem.getArrivalDate(), dotItem.getArrivalPlace(), dotItem.getArrivalTime(),true,dotItem.getTravelId()), 0);

                                }

                                //DOT Attachments
                                for (int j = 0; j <sanctionClaimResponse.getDOTAttach().size() ; j++) {

                                    DOTAttachItem at = sanctionClaimResponse.getDOTAttach().get(j);
                                    detailAttachmentsList.add(new Attachments(at.getOldDocName(),"",at.getDocExtn(),at.getDocId(),true));
                                }
                                attachDetailsAdapter.notifyDataSetChanged();



                                //BL
                                for (int k = 0; k <sanctionClaimResponse.getBL().size() ; k++) {

                                    BLItem item = sanctionClaimResponse.getBL().get(k);

                                    String sancBL = sanc.equalsIgnoreCase("1")?item.getLbSAmt():item.getLbCAmt();
                                    claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging(item.getLbSno(), item.getLbDate(), item.getBoarding(),item.getLodging(),item.getBoardingRemarks(),item.getLbCAmt(),sancBL,true,item.getBlId()),k);
                                }

                                //BL Attachments
                                for (int j = 0; j <sanctionClaimResponse.getBLAttach().size() ; j++) {

                                    BLAttachItem at = sanctionClaimResponse.getBLAttach().get(j);
                                    bordingLoadgingAttachmentsList.add(new Attachments(at.getOldDocName(),"",at.getDocExtn(),at.getDocId(),true));
                                }
                                attachbordingLoadgingAdapter.notifyDataSetChanged();


                                //LC
                                for (int k = 0; k <sanctionClaimResponse.getLC().size() ; k++) {

                                    LCItem item = sanctionClaimResponse.getLC().get(k);
                                    String sancLC = sanc.equalsIgnoreCase("1")?item.getLcSanamt():item.getLcCamt();
                                    claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit(item.getLcSno(), item.getLcDate(), item.getLcTime(),item.getLcFrom(),item.getLcTo(),item.getLcMode(),"",item.getLocalRemarks(),item.getLcCamt(),sancLC,true,item.getLcId()),k);
                                }

                                //BL Attachments
                                for (int j = 0; j <sanctionClaimResponse.getLCAtt().size() ; j++) {

                                    LCAttItem at = sanctionClaimResponse.getLCAtt().get(j);
                                    localConveyanceAttachmentsList.add(new Attachments(at.getOldDocName(),"",at.getDocExtn(),at.getDocId(),true));
                                }
                                attachlocalConveyanceAdapter.notifyDataSetChanged();


                                //setting total
                                setTotalSanctionedAmount();
                                setTotalClaimAmount();

                                binding.tieSanctionedClaimTotalSanctionedClaimNetPay.setText(sanctionClaimResponse.getNetPay());
                                binding.tieSanctionedClaimOtherExpensesLessAdvance.setText(sanctionClaimResponse.getAdvance());

                                progressDialog.dismiss();



                            }
                            else
                            {
                                Toast.makeText(SanctionedClaimFormActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }

                        }
                        else{

                            Toast.makeText(SanctionedClaimFormActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }


                    }
                    else{

                        Toast.makeText(SanctionedClaimFormActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<SanctionClaimResponse> call, Throwable t) {

                    Toast.makeText(SanctionedClaimFormActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }catch (Exception e){

            e.printStackTrace();

        }



    }

    void submitSanctionedForm(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.initiating_claim_sanction_form));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        //DOT
        JsonArray sno = new JsonArray();
        JsonArray sanamt = new JsonArray();
        JsonArray travel_id = new JsonArray();

        for (int i = 0; i <detailsOfTravelingExpensesAdapter.getList().size(); i++) {

            DetailsOfTravelExpenses item = detailsOfTravelingExpensesAdapter.getList().get(i);
            sno.add(item.getSno());
            sanamt.add(item.getSancAmt());
            travel_id.add(item.getTravelId());

        }

        //BL
        JsonArray sno2 = new JsonArray();
        JsonArray sanamt2 = new JsonArray();
        JsonArray bl_id = new JsonArray();

        for (int i = 0; i <claimBoardingAndLodgingExpensesAdapter.getList().size(); i++) {

            ClaimBoardingAndLodging item = claimBoardingAndLodgingExpensesAdapter.getList().get(i);
            sno2.add(item.getSno());
            sanamt2.add(item.getSancAmt());
            bl_id.add(item.getBlid());

        }

        //LC
        String telSanc = checkNullOrEmpty(binding.tieSanctionedClaimMiscTelSancAmt.getText().toString());
        String interSanc = checkNullOrEmpty(binding.tieSanctionedClaimMiscInternetSancAmt.getText().toString());
        String agentSanc = checkNullOrEmpty(binding.tieSanctionedClaimMiscAgentChrgTicketBookingSancAmt.getText().toString());
        String otherSanc = checkNullOrEmpty(binding.tieSanctionedClaimMiscOtherExpSancAmt.getText().toString());

        JsonArray lcno = new JsonArray();
        JsonArray lcsantionamt = new JsonArray();
        JsonArray lc_id = new JsonArray();

        for (int i = 0; i <claimLocalConveyanceAndOutstationVisitAdapter.getList().size(); i++) {

            LocalConveyanceAndOnOutstationVisit item = claimLocalConveyanceAndOutstationVisitAdapter.getList().get(i);
            lcno.add(item.getSno());
            lcsantionamt.add(item.getSancAmt());
            lc_id.add(item.getLcId());

        }

        RetrofitSingelton.getClient().sumbitSanctionedClaimForm("submit_claim_sanc",ticketId,claimId,claimId,sanctionedAction,sanctionClaimResponse.getName(),sanctionClaimResponse.getGrade(),sanctionClaimResponse.getDesignation(),sanctionClaimResponse.getEmpId(),sno.toString(),sanamt.toString(),travel_id.toString(),sno2.toString(),sanamt2.toString(),bl_id.toString(),telSanc,interSanc,agentSanc,otherSanc,lcno.toString(),lcsantionamt.toString(),lc_id.toString(),sessionManager.getUserId()
        ).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(response.isSuccessful()){

                    if(response!=null){

                        DefaultResponse res = response.body();
                        if(res.getError().equalsIgnoreCase("false")){

                            Toast.makeText(SanctionedClaimFormActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();

                        }
                        else{

                            Toast.makeText(SanctionedClaimFormActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            //finish();


                        }
                    }
                    else{

                        Toast.makeText(SanctionedClaimFormActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //finish();

                    }

                }
                else{

                    Toast.makeText(SanctionedClaimFormActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //finish();
                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                Toast.makeText(SanctionedClaimFormActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });


    }

    /**
     * Helper Methods for calculation
     */

    String checkNullOrEmpty(String val){

        String value = val;
        if(val.isEmpty()){
            value = "0";
        }

        return value;
    }

    void setSanctionedTotalMisc(){

        int telClaim = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscTelSanctionedClaimAmt.getText().toString()));
        int telSanc = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscTelSancAmt.getText().toString()));

        int internetClaim = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscInternetSanctionedClaimAmt.getText().toString()));
        int interSanc = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscInternetSancAmt.getText().toString()));

        int agentClaim = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscAgentChrgTicketBookingSanctionedClaimAmt.getText().toString()));
        int agentSanc = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscAgentChrgTicketBookingSancAmt.getText().toString()));

        int otherClaim = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscOtherExpSanctionedClaimAmt.getText().toString()));
        int otherSanc = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscOtherExpSancAmt.getText().toString()));

        int miscClaimTotal = telClaim +internetClaim+agentClaim+otherClaim;
        int miscSancTotal = telSanc +interSanc+agentSanc+otherSanc;

        Log.e("total_misc",miscSancTotal+"");

        binding.tieSanctionedClaimMiscTotalSanctionedClaimAmt.setText(miscClaimTotal+"");
        binding.tieSanctionedClaimMiscTotalSancAmt.setText(miscSancTotal+"");

        setTotalSanctionedAmount();

    }

    void setTotalSanctionedAmount(){

        int totalDetailsTravel = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalSanctioned.getText().toString()));
        int totalBoardingAndLoading = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalSanctioned.getText().toString()));
        int totalLocalConveyance = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimLocalConveyTotalSancAmt.getText().toString()));
        int totalMiscSanc = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscTotalSancAmt.getText().toString()));

        int total = totalDetailsTravel+totalBoardingAndLoading+totalLocalConveyance+totalMiscSanc;

        binding.tieSanctionedClaimTotalSanctionedSanc.setText(total+"");


    }

    void setTotalClaimAmount(){

        int totalDetailsTravel = detailsOfTravelingExpensesAdapter.getTotalClaimAmt();
        int totalBoardingAndLoading = claimBoardingAndLodgingExpensesAdapter.getTotalClaimAmount();
        int totalLocalConveyance = claimLocalConveyanceAndOutstationVisitAdapter.getTotalClaimAmount();
        int totalMisc = Integer.parseInt(checkNullOrEmpty(binding.tieSanctionedClaimMiscTotalSanctionedClaimAmt.getText().toString()));


        int total = totalDetailsTravel+totalBoardingAndLoading+totalLocalConveyance+totalMisc;

        Log.e("total_claim",total+""+totalDetailsTravel+","+totalBoardingAndLoading+","+totalLocalConveyance+","+totalMisc);

        binding.tieSanctionedClaimTotalSanctionedClaimAmount.setText(total+"");

    }

    /**
     * Alert dialogs
     * @param message
     */

    void showEntitlement(String message) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater =this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_task_desc, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();

        TextView tvHeading = dialogView.findViewById(R.id.tv_alert_dialog_task_title);
        tvHeading.setText(R.string.entitlement);

        WebView webview = dialogView.findViewById(R.id.webview_task_desc);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL("", message, "text/html", "UTF-8", "");

        Button cancel = dialogView.findViewById(R.id.btn_task_desc_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        TextView tvdesc = dialogView.findViewById(R.id.tv_alert_dialog_task_desc);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvdesc.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvdesc.setText(Html.fromHtml(message));
        }

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    /**
     * Toggle animation
     */

    private void onToggleMiscExp() {

        if (binding.elSanctionedClaimMiscExp.isExpanded()) {

            binding.elSanctionedClaimMiscExp.toggle();
            binding.ivSanctionedClaimMiscExp.animate().rotation(0f).start();

        } else {

            binding.elSanctionedClaimMiscExp.toggle();
            binding.ivSanctionedClaimMiscExp.animate().rotation(90f).start();
        }


    }

    private void onToggleDetailsOfTravel() {

        if (binding.elSanctionedClaimDetailsOfTravel.isExpanded()) {

            binding.elSanctionedClaimDetailsOfTravel.toggle();
            binding.ivSanctionedClaimDetailsOfTravel.animate().rotation(0f).start();

        } else {

            binding.elSanctionedClaimDetailsOfTravel.toggle();
            binding.ivSanctionedClaimDetailsOfTravel.animate().rotation(90f).start();
        }

    }

    private void onToggleBoardingAndLodging() {

        if (binding.elSanctionedClaimBoardingAndLodging.isExpanded()) {

            binding.elSanctionedClaimBoardingAndLodging.toggle();
            binding.ivSanctionedClaimBoardingAndLodging.animate().rotation(0f).start();

        } else {

            binding.elSanctionedClaimBoardingAndLodging.toggle();
            binding.ivSanctionedClaimBoardingAndLodging.animate().rotation(90f).start();
        }

    }

    private void onToggleLocalConveyance() {

        if (binding.elSanctionedClaimLocalConveyance.isExpanded()) {

            binding.elSanctionedClaimLocalConveyance.toggle();
            binding.ivSanctionedClaimLocalConveyance.animate().rotation(0f).start();

        } else {

            binding.elSanctionedClaimLocalConveyance.toggle();
            binding.ivSanctionedClaimLocalConveyance.animate().rotation(90f).start();
        }

    }

    private void onToggleBasicInfo() {
        if (binding.elBasicInfo.isExpanded()) {

            binding.elBasicInfo.toggle();
            binding.ivSanctionedClaimBasicInfo.animate().rotation(0f).start();

        } else {

            binding.elBasicInfo.toggle();
            binding.ivSanctionedClaimBasicInfo.animate().rotation(90f).start();
        }
    }

}
