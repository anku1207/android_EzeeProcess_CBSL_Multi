package in.cbslgroup.ezeepeafinal.ui.activity.workflow;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.adapters.list.StorageAllotedAdapter;
import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.AttachmentAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.BottomSheetWithSearchAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.ClaimBoardingAndLodgingExpensesAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.ClaimLocalConveyanceAndOutstationVisitAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.DetailsOfTravelingExpensesAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.FileViewHorizontalAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.MoveStorageListAdapter;
import in.cbslgroup.ezeepeafinal.interfaces.CustomItemClickListener;
import in.cbslgroup.ezeepeafinal.interfaces.MoveStorageListListener;
import in.cbslgroup.ezeepeafinal.model.Attachments;
import in.cbslgroup.ezeepeafinal.model.BottomSheetWithSearch;
import in.cbslgroup.ezeepeafinal.model.City;
import in.cbslgroup.ezeepeafinal.model.ClaimBoardingAndLodging;
import in.cbslgroup.ezeepeafinal.model.DetailsOfTravelExpenses;
import in.cbslgroup.ezeepeafinal.model.Foldername;
import in.cbslgroup.ezeepeafinal.model.LocalConveyanceAndOnOutstationVisit;
import in.cbslgroup.ezeepeafinal.model.MoveStorage;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.DateUtil;
import in.cbslgroup.ezeepeafinal.utils.ExceptionHandler;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;
import in.cbslgroup.ezeepeafinal.databinding.ActivityClaimFormTravelExpenseBinding;

import static in.cbslgroup.ezeepeafinal.ui.activity.MainActivity.storageAllotedList;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;

public class ClaimFormTravelExpenseActivity extends AppCompatActivity {

    String workflowId,workflowName;

    UploadNotificationConfig uploadNotificationConfig;

    //alert
    AlertDialog alertDialogProcessing;

    public static final String SAVE_DRAFT = "save_draft";
    public static final String SUBMIT = "submit";
    public boolean IS_DRAFT = false;

    String detailType="";
    String claimId="";

    //Binding and ViewModels
    ActivityClaimFormTravelExpenseBinding binding;

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
    List<BottomSheetWithSearch> companyList = new ArrayList<>();
    List<BottomSheetWithSearch> divisonList = new ArrayList<>();
    List<BottomSheetWithSearch> deptList = new ArrayList<>();
    List<BottomSheetWithSearch> siteList = new ArrayList<>();
    List<BottomSheetWithSearch> projectList = new ArrayList<>();
    List<BottomSheetWithSearch> touridList = new ArrayList<>();

    //Lists
    List<DetailsOfTravelExpenses> detailsOfTravelList = new ArrayList<>();
    List<City> cityList = new ArrayList<>();

    //List Boarding and Lodging
    List<ClaimBoardingAndLodging> claimBoardingAndLodgingList = new ArrayList<>();

    //LocalConveyanceAndOnOutstationVisit
    List<LocalConveyanceAndOnOutstationVisit> localConveyanceAndOnOutstationVisitList = new ArrayList<>();

    //Session manager
    SessionManager sessionManager;

    //filepath
    String detailFilePath, boardingLoadingFilePath, miscFilePath, localConveyanceFilePath;


    //arrays

    //Details
    ArrayList<String> detailSno = new ArrayList<>();
    ArrayList<String> detailtmode = new ArrayList<>();
    ArrayList<String> detailArrivalPlace = new ArrayList<>();
    ArrayList<String> detailArrivalDay = new ArrayList<>();
    ArrayList<String> detailArrivalTime = new ArrayList<>();

    ArrayList<String> detailDeptPlace = new ArrayList<>();
    ArrayList<String> detailDeptDay = new ArrayList<>();
    ArrayList<String> detailDeptTime = new ArrayList<>();

    ArrayList<String> detailclass = new ArrayList<>();
    ArrayList<String> detailremarks = new ArrayList<>();
    ArrayList<String> detailcamt = new ArrayList<>();

    /*
     * Boarding and lodging expense
     */

    ArrayList<String> blSno = new ArrayList<>();
    ArrayList<String> blDate = new ArrayList<>();
    ArrayList<String> blBoard = new ArrayList<>();
    ArrayList<String> blLodg = new ArrayList<>();
    ArrayList<String> blClaim = new ArrayList<>();
    ArrayList<String> blRemarks = new ArrayList<>();

    /*
     * Localconvyance Expense
     *
     */

    ArrayList<String> lcsno = new ArrayList<>();
    ArrayList<String> lcdate = new ArrayList<>();
    ArrayList<String> lctime = new ArrayList<>();
    ArrayList<String> lcfrom = new ArrayList<>();
    ArrayList<String> lcto = new ArrayList<>();
    ArrayList<String> lcmode = new ArrayList<>();
    ArrayList<String> lcclaimamt = new ArrayList<>();
    ArrayList<String> lcRemarks = new ArrayList<>();


    //ids of dropdowns
    String companyId = "";
    String divId = "";
    String deptId = "";
    String projectId = "";
    String siteId = "";

    //for dms folder select
    List<MoveStorage> moveStorageList = new ArrayList<>();
    BottomSheetDialog bottomSheetSelectStorageDialog;
    LinearLayout llnodirectoryfound;
    Button btnSelectStorage;
    ArrayList<Foldername> horilist = new ArrayList<>();
    RecyclerView rvMain, rvHori;
    ProgressBar progressBar;
    FileViewHorizontalAdapter fileViewHorizontalAdapter;
    MoveStorageListAdapter moveStorageListAdapter;
    String storagename, slidStr;

    //Selected Storage Slid
    String selectedSlid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {

            requestPermission();
            initViewModelsAndBinding();
            initLocale();
            initSessionManager();
            setData();
            initToolbar();
            initRecyclerViews();
            initListeners();
            registerBroadCastReceivers();
            initToogle();

            //web methods intialize
            getTourId();
            getCity();
            getUserDetails();



        }

        catch (Exception ex){

            Log.e("error",ex.getMessage());
            //ex.printStackTrace();

        }




    }

    private void initToogle() {

         //Expand all first
        onToggleBankDetails();
        onToggleBasicInfo();
        onToggleMiscExp();
        onToggleBoardingAndLodging();
        onToggleDetailsOfTravel();
        onToggleLocalConveyance();

    }

    private void getSaveInDraftInfo(String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_draft",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    String error = jsonObject.getString("error");
                   //
                    //String isDraft = jsDetails.getString("is_draft");
                    /**
                     * This means that the form is in draft
                     */
                    if(error.equals("false")){

                        IS_DRAFT = true;

                        JSONObject jsDetails = jsonObject.getJSONObject("details");

                        claimId = jsDetails.getString("claim_id");

                        String compnyIdDraft = jsDetails.getString("company_Id");
                        String deptIdDraft = jsDetails.getString("department_id");
                        String devisionIdDraft = jsDetails.getString("devision_id");
                        String projectIdDraft = jsDetails.getString("project_id");
                        String siteIdDraft = jsDetails.getString("site_id");

                        String jobCompletedOrNot = jsDetails.getString("site_id");
                        String fromDate = jsDetails.getString("from_date").equalsIgnoreCase("1970-01-01")?"":jsDetails.getString("from_date");
                        String toDate = jsDetails.getString("to_date").equalsIgnoreCase("1970-01-01")?"":jsDetails.getString("to_date");
                        String tourPurpose = jsDetails.getString("purpose_tour");
                        String noofdays = jsDetails.getString("noofdays");
                        String tourId = jsDetails.getString("tour_ticket_id");

                        String totalClaim = jsDetails.getString("total_claim");
                        String advance = jsDetails.getString("advance");
                        String netPay = jsDetails.getString("net_pay");


                        //card and bank details
                        String recieveType = jsDetails.getString("receivetype");

                        //bank
                        String bankname = jsDetails.getString("bankname");
                        String branchname = jsDetails.getString("branchname");
                        String ifsccode = jsDetails.getString("ifsccode");
                        String accountno = jsDetails.getString("accountno");

                        //cards
                        String cardbankname = jsDetails.getString("cardbankname");
                        String cardno = jsDetails.getString("cardno");

                        if(recieveType.equalsIgnoreCase("0")){

                            binding.tieClaimBankBankDetailsBankName.setText(bankname);
                            binding.tieClaimBankBankDetailsBranchName.setText(branchname);
                            binding.tieClaimBankBankDetailsIfscCode.setText(ifsccode);
                            binding.tieClaimBankBankDetailsAccNo.setText(accountno);
                            binding.rgClaimBankDetails.check(binding.rbClaimBankDetailsBank.getId());


                            if(binding.elClaimBankCardDetails.isExpanded()){

                                binding.elClaimBankCardDetails.collapse();
                                binding.elClaimBankBankDetails.expand();

                            }

                        }
                        else{

                            binding.rgClaimBankDetails.check(binding.rbClaimBankDetailsCard.getId());

                            binding.tieClaimCardDetailsBankName.setText(cardbankname);
                            binding.tieClaimCardDetailsCardNo.setText(cardno);

                            if(binding.elClaimBankBankDetails.isExpanded()){

                                binding.elClaimBankCardDetails.expand();
                                binding.elClaimBankBankDetails.collapse();

                            }

                        }


                        //some id is which are being assignd
                        companyId = compnyIdDraft;
                        deptId = deptIdDraft;
                        divId = devisionIdDraft;
                        projectId = projectIdDraft;
                        siteId = siteIdDraft;


                        binding.tieClaimFrom.setText(fromDate);
                        binding.tieClaimTo.setText(toDate);
                        binding.tieClaimPurposeOfTour.setText(tourPurpose);


                        try {
                            binding.tieClaimNoOfDays.setText(DateUtil.daysBetween2Dates(fromDate,toDate));

                        } catch (Exception e) {
                            e.printStackTrace();

                            binding.tieClaimNoOfDays.setText(noofdays);
                        }

                        binding.tieClaimTotalClaimAmount.setText(totalClaim);
                        binding.tieClaimOtherExpensesLessAdvance.setText(advance);
                        binding.tieClaimTotalClaimNetPay.setText(netPay);

                        //misc

                        /**
                         * "telephone_claim": "6556",
                         *         "telephone_santioned": "0",
                         *         "internet_claim": "444",
                         *         "internet_santioned": "0",
                         *         "ticket_claim": "44",
                         *         "ticket_santioned": "0",
                         *         "other_expense_claim": "44",
                         *         "other_expense_santioned": "0",
                         *         "telephone_remarks": "123",
                         *         "internet_remarks": "123",
                         *         "agent_remarks": "123",
                         *         "other_remarks": "124"
                         */


                        JSONObject jsMisc = jsonObject.getJSONObject("ME");

                        String telephone_claim = jsMisc.getString("telephone_claim");
                        String internet_claim = jsMisc.getString("internet_claim");
                        String ticket_claim = jsMisc.getString("ticket_claim");
                        String other_expense_claim = jsMisc.getString("other_expense_claim");


                        String telephone_remarks = jsMisc.getString("telephone_remarks");
                        String internet_remarks = jsMisc.getString("internet_remarks");
                        String agent_remarks = jsMisc.getString("agent_remarks");
                        String other_remarks = jsMisc.getString("other_remarks");

                        binding.tieClaimMiscTelClaimAmt.setText(telephone_claim);
                        binding.tieClaimMiscTelRemarks.setText(telephone_remarks);

                        binding.tieClaimMiscInternetClaimAmt.setText(internet_claim);
                        binding.tieClaimMiscInternetRemarks.setText(internet_remarks);

                        binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.setText(ticket_claim);
                        binding.tieClaimMiscAgentChrgTicketBookingRemarks.setText(agent_remarks);

                        binding.tieClaimMiscOtherExpClaimAmt.setText(other_expense_claim);
                        binding.tieClaimMiscOtherExpRemarks.setText(other_remarks);


                        /**
                         * After the value have been placed then we calculate the final misc amount
                         */
                        getTotalMiscExpense();


                        //Details Of Traveling Expenses

                        //clearing all the data then adding from draft saved
                        detailsOfTravelingExpensesAdapter.removeAll();
                        JSONArray jsDOT = jsonObject.getJSONArray("DOTE");
                        if(jsDOT.length() != 0){

                            for(int i=0;i<jsDOT.length();i++){

                                JSONObject j1 = jsDOT.getJSONObject(i);

                                String sno = j1.getString("sno");
                                String mode = j1.getString("mode");
                                String departure_place = j1.getString("departure_place");
                                String departure_date = j1.getString("departure_date").equals("1970-01-01")?"":j1.getString("departure_date");
                                String departure_time = j1.getString("departure_time");
                                String arrival_date = j1.getString("arrival_date").equals("1970-01-01")?"":j1.getString("arrival_date");
                                String arrival_time = j1.getString("arrival_time");
                                String arrival_place = j1.getString("arrival_place");
                                String mClass = j1.getString("class");
                                String claim_amt = j1.getString("claim_amt");
                                String sanc_amt = j1.getString("santioned_amt");
                                String traveling_remarks = j1.getString("traveling_remarks");

                                detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses(sno,mode,mClass,traveling_remarks,claim_amt,sanc_amt,departure_date,departure_place,departure_time,arrival_date,arrival_place,arrival_time,false),i);

                            }

                        }

                        else{

                            detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses("", "", "", "", "0", "0", "", "", "", "", "", "",false), 0);


                        }




                        // Boarding And Lodging Expenses

                        //clearing all the data then adding from draft saved
                        claimBoardingAndLodgingExpensesAdapter.removeAll();
                        JSONArray jsBALE = jsonObject.getJSONArray("BALE");
                        if(jsBALE.length()!=0){

                            for(int i=0;i<jsBALE.length();i++){

                                JSONObject j3 = jsBALE.getJSONObject(i);
                                String sno = j3.getString("lb_sno");
                                String lb_date = j3.getString("lb_date").equals("1970-01-01")?"":j3.getString("lb_date");
                                String boarding = j3.getString("boarding");
                                String lodging = j3.getString("lodging");
                                String lb_c_amt = j3.getString("lb_c_amt");
                                String lb_s_amt = j3.getString("lb_s_amt");
                                String boarding_remarks = j3.getString("boarding_remarks");

                                claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging(sno,lb_date,boarding,lodging,boarding_remarks,lb_c_amt,lb_s_amt),i);

                            }

                        }

                       else{

                            claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging("", "", "0", "0", "", "0", "0"), 0);

                        }

                        /**
                         * Local Conveyance and on Outstation Visit
                         */

                        //clearing all the data then adding from draft saved
                        claimLocalConveyanceAndOutstationVisitAdapter.removeAll();
                        JSONArray jsLCOV = jsonObject.getJSONArray("LCOV");
                        if(jsLCOV.length()!=0){

                            for(int i=0;i<jsLCOV.length();i++){

                                JSONObject j3 = jsLCOV.getJSONObject(i);

                                /**
                                 * "lc_sno": "1",
                                 *             "lc_date": "2020-05-20",
                                 *             "lc_time": "0",
                                 *             "lc_from": "sant nagar",
                                 *             "lc_to": "burari",
                                 *             "lc_mode": "Air",
                                 *             "lc_camt": "3400",
                                 *             "lc_sanamt": "0",
                                 *             "local_remarks": "134"
                                 */

                                String lc_sno = j3.getString("lc_sno");
                                String lc_date = j3.getString("lc_date").equalsIgnoreCase("1970-01-01")?"":j3.getString("lc_date");
                                String lc_time = j3.getString("lc_time");
                                String lc_from = j3.getString("lc_from");
                                String lc_to = j3.getString("lc_to");
                                String lc_mode = j3.getString("lc_mode");
                                String lc_camt = j3.getString("lc_camt");
                                String lc_sanamt = j3.getString("lc_sanamt");
                                String local_remarks = j3.getString("local_remarks");

                                claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit(lc_sno,lc_date,lc_time,lc_from,lc_to,lc_mode,"",local_remarks,lc_camt,lc_sanamt),i);


                            }

                        }
                        else{

                            claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit("", "", "", "", "", "", "", "", "0", "0"), 0);


                        }



                   if(jobCompletedOrNot.equalsIgnoreCase("Yes")){

                            binding.rbAssignedJobCompletedYes.setSelected(true);
                            binding.rbAssignedJobCompletedNo.setSelected(false);
                        }

                        else{

                            binding.rbAssignedJobCompletedNo.setSelected(true);
                            binding.rbAssignedJobCompletedYes.setSelected(false);

                        }


                        /**
                         * These 3 methods are interconnnected
                         * @method getCompanyListInDraft
                         * @method getDivisionInDraft
                         * @method getProjectListInDraft
                         */

                        getCompanyListInDraft(compnyIdDraft);
                        getDivisionInDraft(compnyIdDraft,devisionIdDraft);
                        getProjectInDraft(devisionIdDraft,projectIdDraft);


                        getDepartmentInDraft(deptIdDraft);
                        getSiteInDraft(siteIdDraft);
                        getTourIdInDraft(tourId);


                    }
                    /**
                     * Normal flow will go on from here like a fresh form
                     */
                    else{

                        getCompanyList();
                        getDepartment();
                        getSite();

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
                map.put("action", "getSaveInDraftInfo");
                map.put("user_id", userid);
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void initRecyclerViews() {

        //attachment rvs for details
        binding.rvAttachmentDetailsOfTravel.setLayoutManager(new LinearLayoutManager(this));
        attachDetailsAdapter = new AttachmentAdapter(detailAttachmentsList, this);
        attachDetailsAdapter.setListener(new AttachmentAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                detailAttachmentsList.remove(pos);
                attachDetailsAdapter.notifyDataSetChanged();

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


        //main
        binding.rvAttachmentClaimMain.setLayoutManager(new LinearLayoutManager(this));
        attachMainAdapter = new AttachmentAdapter(mainAttachmentsList, this);
        attachMainAdapter.setListener(new AttachmentAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                mainAttachmentsList.remove(pos);
                attachMainAdapter.notifyDataSetChanged();

                if (mainAttachmentsList.isEmpty()) {

                    binding.llClaimSelectStorage.setVisibility(View.GONE);

                } else {

                    binding.llClaimSelectStorage.setVisibility(View.VISIBLE);
                }


            }
        });
        binding.rvAttachmentClaimMain.setAdapter(attachMainAdapter);

        //1
        detailsOfTravelingExpensesAdapter = new DetailsOfTravelingExpensesAdapter(detailsOfTravelList, cityList, this);
        detailsOfTravelingExpensesAdapter.setListener(new DetailsOfTravelingExpensesAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {


                if (detailOfTravelValidation()) {

                    detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses("", "", "", "", "", "", "", "", "", "", "", "",false), pos);
                    binding.rvClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.smoothScrollToPosition(detailsOfTravelList.size() - 1);
                }

            }

            @Override
            public void onMinusButtonClicked(int pos) {

                detailsOfTravelingExpensesAdapter.removeItem(pos);
            }

            @Override
            public void getTotalSanctionedAmt(int total) {

                binding.tieClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalSanctioned.setText(String.valueOf(total));


            }

            @Override
            public void getTotalClaimAmt(int total) {

                binding.tieClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalClaimed.setText(String.valueOf(total));
                totalClaim_1_2_3_4_Amount();

            }
        });
        binding.rvClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.rvClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.setAdapter(detailsOfTravelingExpensesAdapter);
        detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses("", "", "", "", "0", "0", "", "", "", "", "", "",false), 0);


        //2
        claimBoardingAndLodgingExpensesAdapter = new ClaimBoardingAndLodgingExpensesAdapter(claimBoardingAndLodgingList, this);
        claimBoardingAndLodgingExpensesAdapter.setListener(new ClaimBoardingAndLodgingExpensesAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {

                if (boardingAndLodgingValidation()) {

                    claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging("", "", "0", "0", "", "0", "0"), pos);
                    binding.rvClaimBoardingAndLodging.smoothScrollToPosition(claimBoardingAndLodgingList.size() - 1);
                }


            }

            @Override
            public void onMinusButtonClicked(int pos) {

                claimBoardingAndLodgingExpensesAdapter.removeItem(pos);
            }

            @Override
            public void getTotalSanctionedAmt(int total) {

                binding.tieClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalSanctioned.setText(String.valueOf(total));
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();


            }

            @Override
            public void getTotalClaimAmt(int total) {

                binding.tieClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalClaimed.setText(String.valueOf(total));

                try {
                    totalClaim_1_2_3_4_Amount();
                }
                catch (Exception e){

                    Log.e("b_l_exp",e.getMessage());
                }

                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();
            }
        });
        binding.rvClaimBoardingAndLodging.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.rvClaimBoardingAndLodging.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvClaimBoardingAndLodging.setAdapter(claimBoardingAndLodgingExpensesAdapter);
        claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging("", "", "0", "0", "", "0", "0"), 0);


        ///4
        claimLocalConveyanceAndOutstationVisitAdapter = new ClaimLocalConveyanceAndOutstationVisitAdapter(localConveyanceAndOnOutstationVisitList, this);
        claimLocalConveyanceAndOutstationVisitAdapter.setListener(new ClaimLocalConveyanceAndOutstationVisitAdapter.OnClickListener() {
            @Override
            public void onAddButtonClicked(int pos) {

                claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit("", "", "", "", "", "", "", "", "0", "0"), pos);
                binding.rvClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.smoothScrollToPosition(localConveyanceAndOnOutstationVisitList.size() - 1);
            }

            @Override
            public void onMinusButtonClicked(int pos) {

                claimLocalConveyanceAndOutstationVisitAdapter.removeItem(pos);
            }

            @Override
            public void getTotalSanctionedAmt(int total) {

                binding.tieClaimLocalConveyTotalSancAmt.setText(String.valueOf(total));
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();


            }

            @Override
            public void getTotalClaimAmt(int total) {

                binding.tieClaimLocalConveyTotalClaimAmt.setText(String.valueOf(total));
                totalClaim_1_2_3_4_Amount();
                //claimBoardingAndLodgingExpensesAdapter.notifyDataSetChanged();
            }
        });
        binding.rvClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.rvClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.setAdapter(claimLocalConveyanceAndOutstationVisitAdapter);
        claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit("", "", "", "", "", "", "", "", "0", "0"), 0);

    }

    private void registerBroadCastReceivers() {



    }

    private void initListeners() {


        binding.tieClaimCardDetailsCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tilClaimCardDetailsCardNo.setError(null);
                binding.tilClaimCardDetailsCardNo.setErrorEnabled(false);

                if(s.toString().length()==0){

                    binding.tilClaimCardDetailsCardNo.setError(getString(R.string.field_is_required));
                }
                else if(s.toString().length()<16){

                    binding.tilClaimCardDetailsCardNo.setError(getString(R.string.this_value_length_is_invalid_it_should_be_between_16_and_16_characters_long));

                }

                else {

                    binding.tilClaimCardDetailsCardNo.setError(null);
                    binding.tilClaimCardDetailsCardNo.setErrorEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.llClaimBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleBasicInfo();
            }
        });

        binding.llClaimBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleBankDetails();
            }
        });

        binding.llClaimDetailsOfTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleDetailsOfTravel();


            }
        });


        binding.llClaimBoardingAndLodging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleBoardingAndLodging();
            }
        });


        binding.llClaimMiscExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleMiscExp();
            }
        });

        binding.llClaimLocalConveyance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onToggleLocalConveyance();
            }
        });




        binding.tvClaimFormTravelSelectStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.multiStorage.equalsIgnoreCase("yes")){

                    showMultistorageDialog(ClaimFormTravelExpenseActivity.this);

                }
                else{

                    selectStoragePopUp(MainActivity.slid_session);

                }

            }
        });

        binding.btnClaimSaveAsDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (formValidationWhileSaveAsDraft()) {

                    //Toast.makeText(ClaimFormTravelExpenseActivity.this, "All Field Are Filled", Toast.LENGTH_SHORT).show();
                     submitForm(SAVE_DRAFT);

                } else {

                    Toast.makeText(ClaimFormTravelExpenseActivity.this, "Atleast Fill First Section", Toast.LENGTH_SHORT).show();

                }



            }
        });


        binding.btnClaimReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClaimFormTravelExpenseActivity.this, ClaimFormTravelExpenseActivity.class);
                intent.putExtra("workflowName",workflowName);
                intent.putExtra("workflowID",workflowId);
                intent.putExtra("isReset",true);
                startActivity(intent);
                finish();

            }
        });

        //buttons footer
        binding.btnClaimSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (formValidation()) {

                   // Toast.makeText(ClaimFormTravelExpenseActivity.this, "All Field Are Filled", Toast.LENGTH_SHORT).show();
                    submitForm(SUBMIT);

                } else {

                    Toast.makeText(ClaimFormTravelExpenseActivity.this, R.string.empty_fields_are_there, Toast.LENGTH_SHORT).show();

                }

            }
        });

        //buttons footer ends here


        //expandable layout


        //expandable layout ends here


        //uploading buttons

        binding.btnClaimFormChooseFileMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(ClaimFormTravelExpenseActivity.this)
                        .withRequestCode(1004)
                        //.withRootPath("/storage/emulated/")
                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

            }
        });

        binding.btnClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(ClaimFormTravelExpenseActivity.this)
                        .withRequestCode(1000)
                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

            }
        });

        binding.btnClaimFormTravelExpenseBoardingLodgingAddMoreLayoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(ClaimFormTravelExpenseActivity.this)
                        .withRequestCode(1001)
                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

            }
        });

        binding.btnClaimMiscUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(ClaimFormTravelExpenseActivity.this)
                        .withRequestCode(1002)
                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();


            }
        });

        binding.btnClaimLocalConveyUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(ClaimFormTravelExpenseActivity.this)
                        .withRequestCode(1003)
                        .withCloseMenu(true)
                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });
        //uploading btn ends here

        binding.tieClaimTourid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!touridList.isEmpty()) {

                    showBottomSheetDialog(ClaimFormTravelExpenseActivity.this, "Select Tour Id", binding.tieClaimTourid, new BottomSheetWithSearchAdapter(touridList), 0, "tour_id", true);

                } else {

                    Toast.makeText(ClaimFormTravelExpenseActivity.this, "No Tour Id Found", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //Misc starts
        binding.tieClaimMiscTelClaimAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                totalClaim_1_2_3_4_Amount();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tieClaimMiscInternetClaimAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                totalClaim_1_2_3_4_Amount();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                totalClaim_1_2_3_4_Amount();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tieClaimMiscOtherExpClaimAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                totalClaim_1_2_3_4_Amount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Misc Ends
        binding.tieClaimFrom.setOnClickListener(view -> {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                    binding.tieClaimFrom.setText(dt);

                }
            }, mYear, mMonth, mDay);
           // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
            dpd.show();

        });

        binding.tieClaimTo.setOnClickListener(view -> {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

          DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String dt = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((month+1) < 10 ? "0" + (month+1) : (month+1)) + "-" + year;
                    binding.tieClaimTo.setText(dt);


                    if (!binding.tieClaimFrom.getText().toString().equals("")) {

                        String d1 = binding.tieClaimFrom.getText().toString();
                        String d2 = binding.tieClaimTo.getText().toString();


                        try {

                            binding.tieClaimNoOfDays.setText(String.valueOf(DateUtil.daysBetween2Dates(d1, d2)));

                        } catch (Exception e) {

                            Log.e("exp_date", e.toString());
                        }

                    }

                }
            }, mYear, mMonth, mDay);
           // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)); //5 days before only
            dpd.show();


        });

        binding.tieClaimCompany.setOnClickListener(view -> {

            showBottomSheetDialog(this, "Select Company", binding.tieClaimCompany, new BottomSheetWithSearchAdapter(companyList), 0, "Company", true);


        });

        binding.tieClaimDivision.setOnClickListener(view -> {


            if(binding.tieClaimCompany.getText().length()!=0){

                showBottomSheetDialog(this, "Select Division", binding.tieClaimDivision, new BottomSheetWithSearchAdapter(divisonList), 0, "division", true);

            }

            else{

                binding.tilClaimDivision.setError("Select Comapany First");
            }

        });

        binding.tieClaimSite.setOnClickListener(view -> {

            showBottomSheetDialog(this, "Select Site", binding.tieClaimSite, new BottomSheetWithSearchAdapter(siteList), 0, "Site", true);


        });

        binding.tieClaimDept.setOnClickListener(view -> {

            showBottomSheetDialog(this, "Select Department", binding.tieClaimDept, new BottomSheetWithSearchAdapter(deptList), 0, "Dept", true);


        });

        binding.tieClaimProject.setOnClickListener(view -> {

            showBottomSheetDialog(this, "Select Project", binding.tieClaimProject, new BottomSheetWithSearchAdapter(projectList), 0, "Project", true);


        });

        //default set the radiobuttton job assigned completed to NO
        binding.rgAssignedJobCompleted.check(binding.rbAssignedJobCompletedNo.getId());

        //default set the radiobuttton bank/card to BANK
        binding.rgClaimBankDetails.check(binding.rbClaimBankDetailsBank.getId());
        binding.rgClaimBankDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == binding.rbClaimBankDetailsBank.getId()) {

                    binding.elClaimBankBankDetails.toggle();
                    binding.elClaimBankCardDetails.toggle();

                   detailType = "bank";

                } else if (i == binding.rbClaimBankDetailsCard.getId()) {

                    binding.elClaimBankBankDetails.toggle();
                    binding.elClaimBankCardDetails.toggle();

                    detailType = "card";

                }

            }
        });


    }

    private void initToolbar() {

        setSupportActionBar(binding.toolbarClaimFormTravelExpense);
        binding.toolbarClaimFormTravelExpense.setSubtitle(workflowName);
        binding.toolbarClaimFormTravelExpense.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();

            }
        });


    }

    private void setData() {

        Intent intent = getIntent();
        workflowId = intent.getStringExtra("workflowID");
        workflowName = intent.getStringExtra("workflowName");


    }

    private void initSessionManager() {

        sessionManager = new SessionManager(this);

    }

    private void initViewModelsAndBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_claim_form_travel_expense);

    }

    //calculation methods
    private void getTotalMiscExpense() {

        int telClaimTotal = Integer.parseInt(binding.tieClaimMiscTelClaimAmt.getText().toString().equals("") ? "0" : binding.tieClaimMiscTelClaimAmt.getText().toString());
        int internetClaimTotal = Integer.parseInt(binding.tieClaimMiscInternetClaimAmt.getText().toString().equals("") ? "0" : binding.tieClaimMiscInternetClaimAmt.getText().toString());
        int agntClaimTotal = Integer.parseInt(binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.getText().toString().equals("") ? "0" : binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.getText().toString());
        int otherExpClaimTotal = Integer.parseInt(binding.tieClaimMiscOtherExpClaimAmt.getText().toString().equals("") ? "0" : binding.tieClaimMiscOtherExpClaimAmt.getText().toString());

        int subTotal = telClaimTotal + internetClaimTotal + agntClaimTotal + otherExpClaimTotal;

        binding.tieClaimMiscTotalClaimAmt.setText(String.valueOf(subTotal));

    }

    //web methods
    private void getTourId() {

        touridList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_get_tourid", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsUser = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsUser.length(); i++) {

                            String tourId = jsUser.getString(i);
                            touridList.add(new BottomSheetWithSearch(tourId, tourId));

                        }


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
                map.put("action", "getTourId");
                map.put("user_id", sessionManager.getUserId());
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getTourIdInDraft(String tourIdDraft) {

        touridList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_get_tourid", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsUser = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsUser.length(); i++) {

                            String tourId = jsUser.getString(i);
                            touridList.add(new BottomSheetWithSearch(tourId, tourId));

                            if(tourIdDraft.equals(tourId)){

                             binding.tieClaimTourid.setText(tourId);

                            }
                        }

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
                map.put("action", "getTourId");
                map.put("user_id", sessionManager.getUserId());
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getOtherDetailsByTourId(String tourid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("resOtherDetailsByTourId", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String fromDate = jsonObject.isNull("fromdate") ? "" : jsonObject.getString("fromdate");
                    String todate = jsonObject.isNull("todate") ? "" : jsonObject.getString("todate");
                    String purposeoftour = jsonObject.isNull("purposeoftour") ? "" : jsonObject.getString("purposeoftour");
                    String noofdays = jsonObject.isNull("noofdays") ? "" : jsonObject.getString("noofdays");
                    String totalamount = jsonObject.isNull("totalamount") ? "" : jsonObject.getString("totalamount");


                    binding.tieClaimFrom.setText(fromDate);
                    binding.tieClaimTo.setText(todate);
                    try {
                        binding.tieClaimNoOfDays.setText(DateUtil.daysBetween2Dates(fromDate,todate)+"");

                    } catch (Exception e) {
                        e.printStackTrace();

                        binding.tieClaimNoOfDays.setText(noofdays);
                    }
                    binding.tieClaimOtherExpensesLessAdvance.setText(totalamount);
                    binding.tieClaimPurposeOfTour.setText(purposeoftour);


                } catch (Exception e) {
                    Log.e("resOther_err", e.toString());
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
                map.put("action", "getOtherDetailsByTourId");
                map.put("tour_id", tourid);
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getCity() {

        cityList.clear();


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



                        /**
                         * If this user has save in draft
                         */

                        //if user presses reset button the  the form will be blank
                        boolean isReset =  getIntent().getBooleanExtra("isReset",false);

                        if(!isReset){

                            getSaveInDraftInfo(sessionManager.getUserId());

                        }

                        else{

                            getCompanyList();
                            getDepartment();
                            getSite();

                        }


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

    private void getCompanyList() {

        companyList.clear();

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

    //this function for save in draft
    private void getCompanyListInDraft(String companyIdDraft) {

        companyList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ADV_REQ_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String cId = jsonArray.getJSONObject(i).getString("c_id");
                            String cName = jsonArray.getJSONObject(i).getString("c_name");
                            companyList.add(new BottomSheetWithSearch(cId, cName));

                            //This is for selecting comapany dynamically from the list
                            if(companyIdDraft.equals(cId)){

                                binding.tieClaimCompany.setText(cName);

                            }

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

    private void getDivision(String companyId) {

        divisonList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("div", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("division");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String divId = jsonArray.getJSONObject(i).getString("div_id");
                            String divName = jsonArray.getJSONObject(i).getString("division_name");

                            divisonList.add(new BottomSheetWithSearch(divId, divName));

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
                map.put("action", "getDivision");
                map.put("companyId", companyId);
                map.put("user_id", sessionManager.getUserId());
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getDivisionInDraft(String companyIdDraft,String divIdDraft) {

        divisonList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("div", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("division");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String divId = jsonArray.getJSONObject(i).getString("div_id");
                            String divName = jsonArray.getJSONObject(i).getString("division_name");

                            divisonList.add(new BottomSheetWithSearch(divId, divName));

                            if(divIdDraft.equals(divId)){

                                binding.tieClaimDivision.setText(divName);
                            }

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
                map.put("action", "getDivision");
                map.put("companyId", companyIdDraft);
                map.put("user_id", sessionManager.getUserId());
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getDepartment() {

        deptList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("department_list");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String divId = jsonArray.getJSONObject(i).getString("dept_id");
                            String divName = jsonArray.getJSONObject(i).getString("dept_name");

                            deptList.add(new BottomSheetWithSearch(divId, divName));

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
                map.put("action", "getDepartment");
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    //for save draft
    private void getDepartmentInDraft(String deptIdDraft) {

        deptList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("department_list");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String deptId = jsonArray.getJSONObject(i).getString("dept_id");
                            String deptName = jsonArray.getJSONObject(i).getString("dept_name");

                            deptList.add(new BottomSheetWithSearch(deptId, deptName));

                            //This is for selecting comapany dynamically from the list
                            if(deptIdDraft.equals(deptId)){

                                binding.tieClaimDept.setText(deptName);

                            }

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
                map.put("action", "getDepartment");
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getProject(String divisionId) {

        projectList.clear();
        binding.tieClaimProject.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("project", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("options");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String projectId = jsonArray.getJSONObject(i).getString("project_id");
                            String projectName = jsonArray.getJSONObject(i).getString("project_name");

                            projectList.add(new BottomSheetWithSearch(projectId, projectName));

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
                map.put("action", "getProject");
                map.put("div_id", divisionId);
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getProjectInDraft(String divisionId, String projectIdInDraft) {

        projectList.clear();
        binding.tieClaimProject.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("project", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("options");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String projectId = jsonArray.getJSONObject(i).getString("project_id");
                            String projectName = jsonArray.getJSONObject(i).getString("project_name");

                            projectList.add(new BottomSheetWithSearch(projectId, projectName));

                            if(projectIdInDraft.equals(projectId)){

                                binding.tieClaimProject.setText(projectName);
                            }

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
                map.put("action", "getProject");
                map.put("div_id", divisionId);
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getSite() {

        siteList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("site_res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("site");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String divId = jsonArray.getJSONObject(i).getString("loc_id");
                            String divName = jsonArray.getJSONObject(i).getString("location_name");

                            siteList.add(new BottomSheetWithSearch(divId, divName));

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
                map.put("action", "getSite");
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void getSiteInDraft(String siteIdDraft) {

        siteList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("site_res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("site");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String siteId = jsonArray.getJSONObject(i).getString("loc_id");
                            String siteName = jsonArray.getJSONObject(i).getString("location_name");

                            siteList.add(new BottomSheetWithSearch(siteId,siteName));

                            if(siteIdDraft.equals(siteId)){

                                binding.tieClaimSite.setText(siteName);

                            }

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
                map.put("action", "getSite");
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


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

                        binding.tieClaimDesignation.setText(designation);
                        binding.tieClaimEmpName.setText(fullname);
                        binding.tieClaimEmpId.setText(empid);
                        binding.tieClaimGrade.setText(grade);

                        boolean isThere = jsonObject.isNull("bank_details");
                        if(!isThere)
                        {

                            JSONObject jbBank  = jsonObject.getJSONObject("bank_details");
                            String bank_name = jbBank.getString("bank_name");
                            String ifcs_code = jbBank.getString("ifcs_code");
                            String account_no = jbBank.getString("account_no");

                            binding.tieClaimBankBankDetailsBankName.setText(bank_name);
                            binding.tieClaimBankBankDetailsIfscCode.setText(ifcs_code);
                            binding.tieClaimBankBankDetailsAccNo.setText(account_no);
                        }



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

    private void submitForm(String mode) {

        alertProcessing(this);

        MultipartUploadRequest multipartUploadRequest = null;
        try {

            multipartUploadRequest = new MultipartUploadRequest(ClaimFormTravelExpenseActivity.this, ApiUrl.CLAIM_FORM);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

        if (detailAttachmentsList.size() > 0 || bordingLoadgingAttachmentsList.size() > 0 || miscAttachmentsList.size() > 0 || localConveyanceAttachmentsList.size() > 0 || mainAttachmentsList.size() > 0 && !mode.equals(SAVE_DRAFT)) {

            uploadNotificationConfig = new UploadNotificationConfig();
            multipartUploadRequest.setNotificationConfig(

                             uploadNotificationConfig
                            .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                            .setClearOnActionForAllStatuses(true)
            );


            // uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(IntiateWorkFlowActivity.this, 1, uploadId)));

            uploadNotificationConfig.getCompleted().message = getString(R.string.upload_complete_successfully_in)+" "+ ELAPSED_TIME;
            // uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;


            uploadNotificationConfig.getError().message = getString(R.string.error_while_uploading);
            uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
            //uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


            uploadNotificationConfig.getCancelled().message = getString(R.string.upload_has_been_cancelled);
            //uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;
        }
        else {


            uploadNotificationConfig = new UploadNotificationConfig();
            multipartUploadRequest.setNotificationConfig(
                    uploadNotificationConfig
                            .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                            .setClearOnActionForAllStatuses(true)
            );



            if(mode.equals(SAVE_DRAFT)){

                uploadNotificationConfig.setTitleForAllStatuses(getString(R.string.drafting_workflow));
                uploadNotificationConfig.getProgress().message = getString(R.string.drafting_workflow_please_wait);
                uploadNotificationConfig.setIconForAllStatuses(R.drawable.ic_logo_notification);

                // uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(IntiateWorkFlowActivity.this, 1, uploadId)));
                uploadNotificationConfig.getCompleted().message = getString(R.string.workflow_claim_form_travel_drafted_successfully);
                // uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = R.drawable.ic_logo_notification;


                uploadNotificationConfig.getError().message = getString(R.string.error_drafting_workflow_claim_form_travel);
                uploadNotificationConfig.getError().iconResourceID = R.drawable.ic_logo_notification;
                //uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


                uploadNotificationConfig.getCancelled().message = getString(R.string.workflow_claim_form_travel_has_been_cancelled);
                //uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = R.drawable.ic_logo_notification;

            }

            else{

                uploadNotificationConfig.setTitleForAllStatuses(getString(R.string.intiate_workflow));
                uploadNotificationConfig.getProgress().message = getString(R.string.intiating_workflow_please_wait);
                uploadNotificationConfig.setIconForAllStatuses(R.drawable.ic_logo_notification);


                // uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(IntiateWorkFlowActivity.this, 1, uploadId)));
                uploadNotificationConfig.getCompleted().message = getString(R.string.workflow_claim_form_travel_intiated_successfully);
                // uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = R.drawable.ic_logo_notification;


                uploadNotificationConfig.getError().message = getString(R.string.error_intiating_workflow_claim_form_travel);
                uploadNotificationConfig.getError().iconResourceID = R.drawable.ic_logo_notification;
                //uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


                uploadNotificationConfig.getCancelled().message = getString(R.string.workflow_claim_form_travel_has_been_cancelled);
                //uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = R.drawable.ic_logo_notification;

            }


        }

        //file attached
        if(mode.equals(SUBMIT))
        {
            multipartUploadRequest.addParameter("action", "submit_claim");

        if(IS_DRAFT){

            multipartUploadRequest.addParameter("preclaimid",claimId);

        }


        }

        else if(mode.equals(SAVE_DRAFT)){

            multipartUploadRequest.addParameter("action", "save_claim");

            if(IS_DRAFT){

                multipartUploadRequest.addParameter("preclaimid",claimId);

            }


        }


        /**
         * Getting all the values from the dynamic form
         */
        getDetailOfTravelValues();
        getBoardingAndLodgingValues();
        getLocalConveyanceAndVisitFormValues();


        multipartUploadRequest.addParameter("wfid", workflowId);

        multipartUploadRequest.addParameter("company", companyId);
        multipartUploadRequest.addParameter("dept", deptId);
        multipartUploadRequest.addParameter("division", divId);
        multipartUploadRequest.addParameter("site", siteId);
        multipartUploadRequest.addParameter("project", projectId);
        multipartUploadRequest.addParameter("cdes_user_id", sessionManager.getUserId());
        multipartUploadRequest.addParameter("fullname", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));


        multipartUploadRequest.addParameter("employeeid", binding.tieClaimEmpId.getText().toString());
        multipartUploadRequest.addParameter("grade", binding.tieClaimGrade.getText().toString());
        multipartUploadRequest.addParameter("designation", binding.tieClaimDesignation.getText().toString());
        multipartUploadRequest.addParameter("to", binding.tieClaimTo.getText().toString());
        multipartUploadRequest.addParameter("from", binding.tieClaimFrom.getText().toString());

        if (binding.rgAssignedJobCompleted.getCheckedRadioButtonId() == binding.rbAssignedJobCompletedYes.getId()) {

            multipartUploadRequest.addParameter("jobcomplete", "Yes");

        } else {

            multipartUploadRequest.addParameter("jobcomplete", "No");
        }


        //bank or card details
        //multipartUploadRequest.addParameter("receivetype", detailType.equalsIgnoreCase("bank")?"1":"0");


//        //bank
//        multipartUploadRequest.addParameter("bankname", binding.tieClaimBankBankDetailsBankName.getText().toString());
//        multipartUploadRequest.addParameter("branchname", binding.tieClaimBankBankDetailsBranchName.getText().toString());
//        multipartUploadRequest.addParameter("ifsccode", binding.tieClaimBankBankDetailsIfscCode.getText().toString());
//        multipartUploadRequest.addParameter("accountno", binding.tieClaimBankBankDetailsAccNo.getText().toString());
//
//        //card
//        multipartUploadRequest.addParameter("cardbankname", binding.tieClaimCardDetailsBankName.getText().toString());
//        multipartUploadRequest.addParameter("cardno", binding.tieClaimCardDetailsCardNo.getText().toString());


        multipartUploadRequest.addParameter("purpose", binding.tieClaimPurposeOfTour.getText().toString());
        multipartUploadRequest.addParameter("totalsum", binding.tieClaimTotalClaimAmount.getText().toString());
        multipartUploadRequest.addParameter("advancers", binding.tieClaimOtherExpensesLessAdvance.getText().toString());
        multipartUploadRequest.addParameter("refundablers", binding.tieClaimTotalClaimNetPay.getText().toString());

        multipartUploadRequest.addParameter("lastMoveId", selectedSlid);
        multipartUploadRequest.addParameter("tourid", binding.tieClaimTourid.getText().toString());

        /*
         * Details of travel expense
         */
        multipartUploadRequest.addParameter("sno", new JSONArray(detailSno).toString());
        multipartUploadRequest.addParameter("tmode1", new JSONArray(detailtmode).toString());

        multipartUploadRequest.addParameter("afrom", new JSONArray(detailArrivalPlace).toString());
        multipartUploadRequest.addParameter("adoftravel", new JSONArray(detailArrivalDay).toString());
        multipartUploadRequest.addParameter("atoftravel", new JSONArray(detailArrivalTime).toString());

        multipartUploadRequest.addParameter("dfrom", new JSONArray(detailDeptPlace).toString());
        multipartUploadRequest.addParameter("ddoftravel", new JSONArray(detailDeptDay).toString());
        multipartUploadRequest.addParameter("timeoftravel", new JSONArray(detailDeptTime).toString());

        multipartUploadRequest.addParameter("class", new JSONArray(detailclass).toString());
        multipartUploadRequest.addParameter("camt", new JSONArray(detailcamt).toString());



        /**
         *                     $bankname = mysqli_escape_string($con, $_POST['bankname']);
         *                     $branchname = mysqli_escape_string($con, $_POST['branchname']);
         *                     $ifsccode = $_POST['ifsccode'];
         *                     $accountno = $_POST['accountno'];
         *                     $receivetype = $_POST['receivetype'];
         *                     $cardbankname = $_POST['cardbankname'];
         *                     $cardno = $_POST['cardno'];
         *
         *                     $noofdays = $_POST['noofdays'];
         *                     $travelingremarks = $_POST['traveling_remarks'];
         *                     $boardingremarks = $_POST['boarding_remarks'];
         *
         *                     $telephone_remarks = $_POST['telephone_remarks'];
         *                     $internet_remarks = $_POST['internet_remarks'];
         *                     $agent_remarks = $_POST['agent_remarks'];
         *                     $other_remarks = $_POST['other_remarks'];
         *                     $localremarks = $_POST['local_remarks'];
         */


        if (binding.rgClaimBankDetails.getCheckedRadioButtonId() == binding.rbClaimBankDetailsBank.getId()) {

            multipartUploadRequest.addParameter("receivetype", "0");

        } else {

            multipartUploadRequest.addParameter("receivetype", "1");

        }

        //bank details
        multipartUploadRequest.addParameter("bankname", binding.tieClaimBankBankDetailsBankName.getText().toString());
        multipartUploadRequest.addParameter("branchname", binding.tieClaimBankBankDetailsBranchName.getText().toString());
        multipartUploadRequest.addParameter("ifsccode", binding.tieClaimBankBankDetailsIfscCode.getText().toString());
        multipartUploadRequest.addParameter("accountno", binding.tieClaimBankBankDetailsAccNo.getText().toString());

        multipartUploadRequest.addParameter("cardbankname", binding.tieClaimCardDetailsBankName.getText().toString());
        multipartUploadRequest.addParameter("cardno", binding.tieClaimCardDetailsCardNo.getText().toString());

        multipartUploadRequest.addParameter("noofdays", binding.tieClaimNoOfDays.getText().toString());

        //remarks
        multipartUploadRequest.addParameter("telephone_remarks", binding.tieClaimMiscTelRemarks.getText().toString());
        multipartUploadRequest.addParameter("internet_remarks", binding.tieClaimMiscInternetRemarks.getText().toString());
        multipartUploadRequest.addParameter("agent_remarks", binding.tieClaimMiscAgentChrgTicketBookingRemarks.getText().toString());
        multipartUploadRequest.addParameter("other_remarks", binding.tieClaimMiscOtherExpRemarks.getText().toString());

        multipartUploadRequest.addParameter("local_remarks", new JSONArray(lcRemarks).toString());
        multipartUploadRequest.addParameter("traveling_remarks", new JSONArray(detailremarks).toString());
        multipartUploadRequest.addParameter("boarding_remarks", new JSONArray(blRemarks).toString());


        if(!mode.equals(SAVE_DRAFT)){

            for (Attachments a : detailAttachmentsList) {

                try {

                    multipartUploadRequest.addFileToUpload(a.getFilePath(), "travelingexp[]");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }


        /*
         * Boarding and lodging expense
         */

        multipartUploadRequest.addParameter("sno2", new JSONArray(blSno).toString());
        multipartUploadRequest.addParameter("datebl", new JSONArray(blDate).toString());
        multipartUploadRequest.addParameter("board", new JSONArray(blBoard).toString());
        multipartUploadRequest.addParameter("lodg", new JSONArray(blLodg).toString());
        multipartUploadRequest.addParameter("clamt2", new JSONArray(blClaim).toString());

        for (Attachments a : bordingLoadgingAttachmentsList) {

            try {

                multipartUploadRequest.addFileToUpload(a.getFilePath(), "boardinglogexp[]");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


        /*
         * Misc Expense
         */

        multipartUploadRequest.addParameter("mcltele", binding.tieClaimMiscTelClaimAmt.getText().toString());
        multipartUploadRequest.addParameter("mclint", binding.tieClaimMiscInternetClaimAmt.getText().toString());
        multipartUploadRequest.addParameter("mcltkt", binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.getText().toString());
        multipartUploadRequest.addParameter("mcloexp", binding.tieClaimMiscOtherExpClaimAmt.getText().toString());


        if(!mode.equals(SAVE_DRAFT)){

            for (Attachments a : miscAttachmentsList) {

                try {

                    multipartUploadRequest.addFileToUpload(a.getFilePath(), "miscexp[]");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }


        }



        /*
         * Localconvyance Expense
         */

        multipartUploadRequest.addParameter("lcsno", new JSONArray(lcsno).toString());
        multipartUploadRequest.addParameter("lcdate", new JSONArray(lcdate).toString());
        multipartUploadRequest.addParameter("lctime", new JSONArray(lctime).toString());
        multipartUploadRequest.addParameter("lcfrom", new JSONArray(lcfrom).toString());
        multipartUploadRequest.addParameter("lcto", new JSONArray(lcto).toString());
        multipartUploadRequest.addParameter("tmode", new JSONArray(lcmode).toString());
        multipartUploadRequest.addParameter("lcclaimamt", new JSONArray(lcclaimamt).toString());

        if(!mode.equals(SAVE_DRAFT)){

            for (Attachments a : localConveyanceAttachmentsList) {

                try {

                    multipartUploadRequest.addFileToUpload(a.getFilePath(), "locanconvey[]");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }

        if(!mode.equals(SAVE_DRAFT)){

            for (Attachments a : mainAttachmentsList) {

                try {

                    multipartUploadRequest.addFileToUpload(a.getFilePath(), "fileName[]");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }



        multipartUploadRequest.setMaxRetries(0);
        multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
            @Override
            public void onProgress(Context context, UploadInfo uploadInfo) {

            }

            @Override
            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                Log.e("onError", serverResponse.getBodyAsString());
                alertDialogProcessing.dismiss();
            }

            @Override
            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                Util.logLargeString(serverResponse.getBodyAsString(),"onCompleted");
                //Log.e("onCompleted", serverResponse.getBodyAsString());

                try {
                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                    String msg = jsonObject.getString("msg");
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        alertDialogProcessing.dismiss();
                        finish();
                    }
                    else{
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        alertDialogProcessing.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                    alertDialogProcessing.dismiss();
                }

            }

            @Override
            public void onCancelled(Context context, UploadInfo uploadInfo) {

                Log.e("onCancelled", uploadInfo.toString());
                alertDialogProcessing.dismiss();
            }
        });

        //alertProcessing("off",IntiateWorkFlowActivity.this);
        multipartUploadRequest.startUpload();


    }

    private void selectStoragePopUp(String slid) {

        View view = getLayoutInflater().inflate(R.layout.bottomsheet_select_storage, null);
        bottomSheetSelectStorageDialog = new BottomSheetDialog(this);
        bottomSheetSelectStorageDialog.setContentView(view);

        llnodirectoryfound = bottomSheetSelectStorageDialog.findViewById(R.id.ll_selectstorage_nofilefound);
        btnSelectStorage = bottomSheetSelectStorageDialog.findViewById(R.id.btn_select_storage);
        btnSelectStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fNameSlid = horilist.get(horilist.size() - 1).getFoldername();
                bottomSheetSelectStorageDialog.dismiss();

                String storagename = fNameSlid.substring(0, fNameSlid.indexOf("&&"));
                String slid = fNameSlid.substring(fNameSlid.indexOf("&&") + 2);

                binding.tvClaimFormTravelSelectStorage.setText(storagename);
                selectedSlid = slid;
                binding.tvClaimFormTravelSelectStorage.setError(null);

                Log.e("fnameslid", fNameSlid);

            }
        });


        progressBar = bottomSheetSelectStorageDialog.findViewById(R.id.progressBar_select_storage);

        rvMain = bottomSheetSelectStorageDialog.findViewById(R.id.rv_select_storage);

        rvHori = bottomSheetSelectStorageDialog.findViewById(R.id.rv_select_storage_hori);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setItemViewCacheSize(moveStorageList.size());
        rvMain.setHasFixedSize(true);


        rvHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHori.setItemViewCacheSize(moveStorageList.size());
        rvHori.setHasFixedSize(true);

        moveStorageList.clear();
        horilist.clear();


        getFoldername(slid);


    }

    private void getFoldername(final String slid) {

        moveStorageList.clear();
        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMain.setVisibility(View.GONE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("getfolname", response);

                //{"storagename":"Test-1&&731","foldername":[]}


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String str = jsonObject.getString("storagename");

                    storagename = str.substring(0, str.indexOf("&&"));
                    slidStr = str.substring(str.indexOf("&&") + 2);


                    //329

                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");

                    if (jsonArray.length() == 0) {

                        // llnodirectoryfound.setVisibility(View.VISIBLE);
                        rvMain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);


                        horilist.clear();
                        String id = storagename + "&&" + slid;
                        Foldername foldername1 = new Foldername();
                        foldername1.setFoldername(id);
                        horilist.add(foldername1);

                        //fileViewHorizontalAdapter.notifyDataSetChanged();

                        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horilist, ClaimFormTravelExpenseActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String slid, String fullFolderName) {


                            }
                        });

                        rvHori.setAdapter(fileViewHorizontalAdapter);
                        rvHori.scrollToPosition(horilist.size() - 1);
                        bottomSheetSelectStorageDialog.show();


                    } else {

                        Log.e("test", "ok 1");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2);

                            moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));

                            // spinnerlist.add(fname);

                        }

                        Log.e("test", "ok 2");

                        String id = storagename + "&&" + slid;
                        Foldername foldername1 = new Foldername();
                        foldername1.setFoldername(id);
                        horilist.add(foldername1);

                        Log.e("test", "ok 3");

                        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horilist, ClaimFormTravelExpenseActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String Slid, String fullFolderName) {


                                if (Slid == slid) {

                                    horilist.clear();
                                    String id = storagename + "&&" + MainActivity.slid_session;
                                    Foldername foldername1 = new Foldername();
                                    foldername1.setFoldername(id);
                                    horilist.add(foldername1);
                                    fileViewHorizontalAdapter.notifyDataSetChanged();
                                    rvHori.scrollToPosition(horilist.size() - 1);


                                } else {

                                    horilist.subList(position + 1, horilist.size()).clear();
                                    fileViewHorizontalAdapter.notifyDataSetChanged();

                                    getChildFoldername(Slid);

                                }


                            }
                        });

                        rvHori.setAdapter(fileViewHorizontalAdapter);
                        Log.e("test", "ok 4");


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, ClaimFormTravelExpenseActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {


                                // tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);

                                fileViewHorizontalAdapter.notifyDataSetChanged();

                           /* tvpreviousFname.setText(foldername);
                            tvpreviousSlid.setText(slid);*/


                                getChildFoldername(slid);


                            }
                        });
                        rvMain.setAdapter(moveStorageListAdapter);
                        moveStorageListAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        rvMain.setVisibility(View.VISIBLE);
                        bottomSheetSelectStorageDialog.show();

                        Log.e("test", "ok 5");


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
                Map<String, String> params = new HashMap<>();
                params.put("slid", slid);


                return params;
            }
        };


        VolleySingelton.getInstance(ClaimFormTravelExpenseActivity.this).addToRequestQueue(stringRequest);


    }

    //cash voucher
    private void getChildFoldername(final String slid) {

        // spinnerchildlist.clear();

        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMain.setVisibility(View.GONE);


        moveStorageList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getchildfoldernames", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");


                    if (jsonArray.length() == 0) {

                        progressBar.setVisibility(View.GONE);
                        rvMain.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.VISIBLE);


                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2);

                            moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));


                         /*   arrayListchild.add(fname);


                            mySpinnerChildlist.add(arrayListchild);
*/
                        }

                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, ClaimFormTravelExpenseActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {

                                //tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);
                                fileViewHorizontalAdapter.notifyDataSetChanged();
                                rvHori.scrollToPosition(horilist.size() - 1);

                                getChildFoldername(slid);
                            }
                        });

                        rvMain.setAdapter(moveStorageListAdapter);


                        moveStorageListAdapter.notifyDataSetChanged();


                        progressBar.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.GONE);
                        rvMain.setVisibility(View.VISIBLE);


                    }




                    /*  addSpinner();*/

                    /*    mySpinnerChildlistCounter++;
                     */


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

                Map<String, String> params = new HashMap<>();
                params.put("slid", slid);


                return params;
            }
        };

        VolleySingelton.getInstance(ClaimFormTravelExpenseActivity.this).addToRequestQueue(stringRequest);


    }

    //dynamic form
    //if tourid is present
    private void getTourDetails(String tourid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CLAIM_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_tour_details", response);


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject jb = jsonObject.getJSONObject("details");
                        String receivetype = jb.getString("receivetype");

                        //card details
                        JSONObject card_details = jb.getJSONObject("card_details");
                        String cardbankname = card_details.getString("cardbankname");
                        String cardno = card_details.getString("cardno");


                        //bank details
                        JSONObject bank_details = jb.getJSONObject("bank_details");
                        String bankname = bank_details.getString("bankname");
                        String branchname = bank_details.getString("branchname");
                        String ifsccode = bank_details.getString("ifsccode");
                        String accountno = bank_details.getString("accountno");

                        if (receivetype.equalsIgnoreCase("1"))
                        //means it is a card
                        {

                            binding.tieClaimCardDetailsBankName.setText(cardbankname);
                            binding.tieClaimCardDetailsCardNo.setText(cardno);

                            selectBankOrCredit("card");


                        } else {

                            binding.tieClaimBankBankDetailsBankName.setText(bankname);
                            binding.tieClaimBankBankDetailsBranchName.setText(branchname);
                            binding.tieClaimBankBankDetailsIfscCode.setText(ifsccode);
                            binding.tieClaimBankBankDetailsAccNo.setText(accountno);

                            selectBankOrCredit("bank");
                        }

                        try {

                            try{

                                //Details of traveling
                                detailsOfTravelList.clear();
                                JSONArray jsDot = jb.getJSONArray("details_of_travel");
                                for (int i = 0; i < jsDot.length(); i++) {

                                    String mode = jsDot.getJSONObject(i).getString("mode");
                                    String date = jsDot.getJSONObject(i).getString("date");
                                    String time = jsDot.getJSONObject(i).getString("time");
                                    String dept_place = jsDot.getJSONObject(i).getString("dept_place");
                                    String arrival_place = jsDot.getJSONObject(i).getString("arrival_place");
                                    String mclass = jsDot.getJSONObject(i).getString("class");
                                    String remarks = jsDot.getJSONObject(i).getString("remarks");
                                    String claim_amt = jsDot.getJSONObject(i).getString("claim_amt");

                                    detailsOfTravelingExpensesAdapter.addItem(new DetailsOfTravelExpenses("", mode, mclass, remarks, claim_amt, "", date, dept_place, time, date, arrival_place, time,false), i);

                                }

                            }

                            catch (Exception e){

                                e.printStackTrace();

                            }


                            try {
                                //boarding_Lodging
                                claimBoardingAndLodgingList.clear();
                                JSONArray jsBL = jb.getJSONArray("boarding_Lodging");
                                for (int i = 0; i < jsBL.length(); i++) {

                                    String date = jsBL.getJSONObject(i).getString("date");
                                    String boarding_amt = jsBL.getJSONObject(i).getString("boarding_amt");
                                    String loadging_amt = jsBL.getJSONObject(i).getString("loadging_amt");
                                    String claim_amt = jsBL.getJSONObject(i).getString("claim_amt");

                                    claimBoardingAndLodgingExpensesAdapter.addItem(new ClaimBoardingAndLodging("", date, boarding_amt, loadging_amt, "", claim_amt, "0"), i);

                                }
                            }

                            catch (Exception e){

                                e.printStackTrace();
                            }


                            try{

                                //misc
                                JSONObject misc = jb.getJSONObject("misc");
                                String tel_charge_claim = misc.getString("tel_charge_claim");
                                // String internet_claim = misc.getString("internet_claim");
                                //String agent_charges_for_ticket_booking_claim = misc.getString("agent_charges_for_ticket_booking_claim");
                                String other_expense_claim = misc.getString("other_expense_claim");


                                binding.tieClaimMiscTelClaimAmt.setText(tel_charge_claim);
                                //binding.tieClaimMiscInternetClaimAmt.setText(internet_claim);
                                //binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.setText(agent_charges_for_ticket_booking_claim);
                                binding.tieClaimMiscOtherExpClaimAmt.setText(other_expense_claim);
                                localConveyanceAndOnOutstationVisitList.clear();
                                //localconveyance
                                JSONObject lcl = jb.getJSONObject("local_conveyance");
                                String local_conveyance = lcl.getString("local_conveyance");
                                String time = lcl.getString("time");

                                claimLocalConveyanceAndOutstationVisitAdapter.addItem(new LocalConveyanceAndOnOutstationVisit("", "", time, "", "", "", "", "", local_conveyance, ""), 0);



                            }
                            catch (Exception e){

                             e.printStackTrace();

                            }


                        }
                        catch(Exception e){

                            e.printStackTrace();

                        }

                    } else {

                        Toast.makeText(ClaimFormTravelExpenseActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
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
                map.put("action", "getTourDetails");
                map.put("tour_id", tourid);
                return map;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    //uploading form and submit


    //calculation methods

    //    private void getTotalMiscClaimAmt(){
//
//        int telAmnt = Integer.parseInt(binding.tieClaimMiscTelClaimAmt.getText().toString().equals("")?"0":binding.tieClaimMiscTelClaimAmt.getText().toString());
//        int internetAmnt = Integer.parseInt(binding.tieClaimMiscInternetClaimAmt.getText().toString().equals("")?"0":binding.tieClaimMiscInternetClaimAmt.getText().toString());
//        int agentAmnt = Integer.parseInt(binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.getText().toString().equals("")?"0":binding.tieClaimMiscAgentChrgTicketBookingClaimAmt.getText().toString());
//        int otherExpAmnt = Integer.parseInt(binding.tieClaimMiscOtherExpClaimAmt.getText().toString().equals("")?"0":binding.tieClaimMiscOtherExpClaimAmt.getText().toString());
//        int total = telAmnt+internetAmnt+agentAmnt+otherExpAmnt;
//        binding.tieClaimMiscTotalClaimAmt.setText(String.valueOf(total));
//
//    }

    private void totalClaim_1_2_3_4_Amount() {

        getTotalMiscExpense();

        int totalDetailsClaim = Integer.parseInt(binding.tieClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalClaimed.getText().toString().equals("") ? "0" : binding.tieClaimFormTravelExpenseDetailsOfTravelAddMoreLayoutTotalClaimed.getText().toString());
        int totalBoardingClaim = Integer.parseInt(binding.tieClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalClaimed.getText().toString().equals("") ? "0" : binding.tieClaimFormTravelExpenseLodgingBoardingAddMoreLayoutTotalClaimed.getText().toString());
        int totalMiscClaim = Integer.parseInt(binding.tieClaimMiscTotalClaimAmt.getText().toString().equals("") ? "0" : binding.tieClaimMiscTotalClaimAmt.getText().toString());
        int totalLocalConveyClaim = Integer.parseInt(binding.tieClaimLocalConveyTotalClaimAmt.getText().toString().equals("") ? "0" : binding.tieClaimLocalConveyTotalClaimAmt.getText().toString());
        int total = totalDetailsClaim + totalBoardingClaim + totalMiscClaim + totalLocalConveyClaim;
        binding.tieClaimTotalClaimAmount.setText(String.valueOf(total));

        netPayableAmt();


    }

    private void netPayableAmt() {

        int lessAdvance = Integer.parseInt(binding.tieClaimOtherExpensesLessAdvance.getText().toString().equals("")?"0":binding.tieClaimOtherExpensesLessAdvance.getText().toString());
        int totalClaim = Integer.parseInt(binding.tieClaimTotalClaimAmount.getText().toString().equals("")?"0":binding.tieClaimTotalClaimAmount.getText().toString());
        int netPayable = totalClaim - lessAdvance;
        binding.tieClaimTotalClaimNetPay.setText(String.valueOf(netPayable));

    }

    //bottomsheets
    private void showBottomSheetDialog(Context context, String title, TextInputEditText tie, BottomSheetWithSearchAdapter bottomSheetWithSearchAdapter, int pos, String module, Boolean wantSearchFilter) {

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

                Log.e("ItemObj", "key-->" + obj.getKey() + "\nValue-->" + obj.getValue());


                if (module.equalsIgnoreCase("company")) {

                    tie.setText(obj.getValue());
                    companyId = obj.getKey();
                    getDivision(companyId);

                    binding.tilClaimCompany.setErrorEnabled(false);
                    binding.tilClaimCompany.setError(null);


                } else if (module.equalsIgnoreCase("division")) {

                    tie.setText(obj.getValue());
                    divId = obj.getKey();
                    getProject(obj.getKey());

                    binding.tilClaimDivision.setErrorEnabled(false);
                    binding.tilClaimDivision.setError(null);

                } else if (module.equalsIgnoreCase("site")) {

                    tie.setText(obj.getValue());
                    siteId = obj.getKey();

                    binding.tilClaimSite.setErrorEnabled(false);
                    binding.tilClaimSite.setError(null);
                    //getProject(obj.getKey());

                } else if (module.equalsIgnoreCase("dept")) {

                    tie.setText(obj.getValue());
                    deptId = obj.getKey();

                    binding.tilClaimDept.setErrorEnabled(false);
                    binding.tilClaimDept.setError(null);

                    //getProject(obj.getKey());

                } else if (module.equalsIgnoreCase("project")) {

                    tie.setText(obj.getValue());
                    projectId = obj.getKey();

                    binding.tilClaimProject.setErrorEnabled(false);
                    binding.tilClaimProject.setError(null);

                    //getProject(obj.getKey());

                } else if (module.equalsIgnoreCase("tour_id")) {

                    tie.setText(obj.getValue());

                    try{

                        getOtherDetailsByTourId(obj.getValue());
                        getTourDetails(obj.getValue());
                        //getProject(obj.getKey());

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }


                }
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

    //helpers ui methods
    private void selectBankOrCredit(String mode) {

        if (mode.equals("card")) {

            binding.rgClaimBankDetails.check(binding.rbClaimBankDetailsCard.getId());
            binding.elClaimBankBankDetails.collapse();
            binding.elClaimBankCardDetails.expand();


        } else {

            binding.rgClaimBankDetails.check(binding.rbClaimBankDetailsBank.getId());
            binding.elClaimBankCardDetails.collapse();
            binding.elClaimBankBankDetails.expand();

        }

    }

    private boolean formValidation() {

        boolean isAllFieldFilled = true;
        String msg =getString(R.string.field_is_required);

        if (setErrorMsg(msg, binding.tilClaimCompany, binding.tieClaimCompany)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimDivision, binding.tieClaimDivision)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimDept, binding.tieClaimDept)) {

            isAllFieldFilled = false;

        }


        if (setErrorMsg(msg, binding.tilClaimProject, binding.tieClaimProject)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimSite, binding.tieClaimSite)) {

            isAllFieldFilled = false;

        }


        if (setErrorMsg(msg, binding.tilClaimPurposeOfTour, binding.tieClaimPurposeOfTour)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimNoOfDays, binding.tieClaimNoOfDays)) {

            isAllFieldFilled = false;

        }

        //means if bank is selected in radiobutton
        if (binding.rgClaimBankDetails.getCheckedRadioButtonId() == binding.rbClaimBankDetailsBank.getId()) {

            if (setErrorMsg(msg, binding.tilClaimBankBankDetailsBankName, binding.tieClaimBankBankDetailsBankName)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, binding.tilClaimBankBankDetailsBranchName, binding.tieClaimBankBankDetailsBranchName)) {

                isAllFieldFilled = false;

            }


            if (setErrorMsg(msg, binding.tilClaimBankBankDetailsIfscCode, binding.tieClaimBankBankDetailsIfscCode)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, binding.tilClaimBankBankDetailsAccNo, binding.tieClaimBankBankDetailsAccNo)) {

                isAllFieldFilled = false;

            }


        } else {

            if (setErrorMsg(msg, binding.tilClaimCardDetailsBankName, binding.tieClaimCardDetailsBankName)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, binding.tilClaimCardDetailsCardNo, binding.tieClaimCardDetailsCardNo)) {

                isAllFieldFilled = false;

            }

            if (binding.tieClaimCardDetailsCardNo.getText().toString().length()<16) {


                binding.tilClaimCardDetailsCardNo.setError(getString(R.string.this_value_length_is_invalid_it_should_be_between_16_and_16_characters_long));
                isAllFieldFilled = false;

            }
            else{

                binding.tilClaimCardDetailsCardNo.setError(null);
                binding.tilClaimCardDetailsCardNo.setErrorEnabled(false);

            }

        }

        if (!detailOfTravelValidation()) {

            isAllFieldFilled = false;

        }

        if (!boardingAndLodgingValidation()) {

            isAllFieldFilled = false;

        }

        if (binding.llClaimSelectStorage.getVisibility() == View.VISIBLE) {

            if (binding.tvClaimFormTravelSelectStorage.getText().toString().isEmpty()) {

                binding.tvClaimFormTravelSelectStorage.setError(msg);
                isAllFieldFilled = false;
            } else {

                binding.tvClaimFormTravelSelectStorage.setError(null);
            }

        }

        getLocalConveyanceAndVisitFormValues();

        return isAllFieldFilled;

    }


    boolean formValidationWhileSaveAsDraft(){

        boolean isAllFieldFilled = true;
        String msg = getString(R.string.field_is_required);

        if (setErrorMsg(msg, binding.tilClaimCompany, binding.tieClaimCompany)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimDivision, binding.tieClaimDivision)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimDept, binding.tieClaimDept)) {

            isAllFieldFilled = false;

        }


        if (setErrorMsg(msg, binding.tilClaimProject, binding.tieClaimProject)) {

            isAllFieldFilled = false;

        }

        if (setErrorMsg(msg, binding.tilClaimSite, binding.tieClaimSite)) {

            isAllFieldFilled = false;

        }

        return isAllFieldFilled;
    }

    private boolean boardingAndLodgingValidation() {

        boolean isAllFieldFilled = true;
        String msg = "Field is Required";

        blSno.clear();
        blBoard.clear();
        blLodg.clear();
        blDate.clear();
        blClaim.clear();
        blRemarks.clear();


        //validating boarding and lodging 2
        for (int i = 0; i < claimBoardingAndLodgingExpensesAdapter.getItemCount(); i++) {

            try {

                View itemView = binding.rvClaimBoardingAndLodging.getLayoutManager().findViewByPosition(i);

                TextInputLayout tilSno, tilBoarding, tilLoadging, tilDate, tilRemarks, tilClaimAmt, tilSancAmt;
                TextInputEditText tieSno, tieBoarding, tieLoadging, tieDate, tieRemarks, tieClaimAmt, tieSancAmt;

                tieSno = itemView.findViewById(R.id.tie_claim_boarding_lodging_sno);
                tieBoarding = itemView.findViewById(R.id.tie_claim_boarding_lodging_boarding);
                tieLoadging = itemView.findViewById(R.id.tie_claim_boarding_lodging_loadging);
                tieDate = itemView.findViewById(R.id.tie_claim_boarding_lodging_date_of_travel);
                tieClaimAmt = itemView.findViewById(R.id.tie_claim_boarding_lodging_total_claim_amt);
                tieRemarks = itemView.findViewById(R.id.tie_claim_boarding_lodging_remarks);

                tilSno = itemView.findViewById(R.id.til_claim_boarding_lodging_sno);
                tilBoarding = itemView.findViewById(R.id.til_claim_boarding_lodging_boarding);
                tilLoadging = itemView.findViewById(R.id.til_claim_boarding_lodging_loadging);
                tilDate = itemView.findViewById(R.id.til_claim_boarding_lodging_date_of_travel);
                tilClaimAmt = itemView.findViewById(R.id.til_claim_boarding_lodging_total_claim_amt);


                blSno.add(tieSno.getText().toString());
                blBoard.add(tieBoarding.getText().toString());
                blLodg.add(tieLoadging.getText().toString());
                blDate.add(tieDate.getText().toString());
                blClaim.add(tieClaimAmt.getText().toString());
                blRemarks.add(tieRemarks.getText().toString());


                if (setErrorMsg(msg, tilSno, tieSno)) {

                    isAllFieldFilled = false;

                }

                if (setErrorMsg(msg, tilBoarding, tieBoarding)) {

                    isAllFieldFilled = false;

                }

                if (setErrorMsg(msg, tilLoadging, tieLoadging)) {

                    isAllFieldFilled = false;

                }
                if (setErrorMsg(msg, tilDate, tieDate)) {

                    isAllFieldFilled = false;

                }

                if (setErrorMsg(msg, tilClaimAmt, tieClaimAmt)) {

                    isAllFieldFilled = false;

                }
            } catch (Exception e) {
                Log.e("error_det", e.toString() + "pos->" + i);
                e.getStackTrace();
            }

        }

        return isAllFieldFilled;
    }

    private boolean detailOfTravelValidation() {

        boolean isAllFieldFilled = true;
        String msg = "Field is required";

        for (int i = 0; i < detailsOfTravelingExpensesAdapter.getItemCount(); i++) {

            View itemView = binding.rvClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.getLayoutManager().findViewByPosition(i);

            TextInputLayout tilSno, tilMode, tilDeptDateOfTravel, tilDeptTime, tilDeptPlace, tilArrvDateOfTravel, tilArrvTime, tilArrvPlace, tilClass, tilRemarks, tilSanc, tilClaim;
            TextInputEditText tieSno, tieMode, tieDeptDateOfTravel, tieDeptTime, tieDeptPlace, tieArrvDateOfTravel, tieArrvTime, tieArrvPlace, tieClass, tieRemarks, tieSanc, tieClaim;

            tieSno = itemView.findViewById(R.id.tie_details_of_travel_exp_sno);
            tieMode = itemView.findViewById(R.id.tie_details_of_travel_exp_mode);
            tieDeptDateOfTravel = itemView.findViewById(R.id.tie_details_of_travel_exp_dept_date_of_travel);
            tieDeptTime = itemView.findViewById(R.id.tie_details_of_travel_exp_dept_time);
            tieDeptPlace = itemView.findViewById(R.id.tie_details_of_travel_exp_dept_place);
            tieArrvDateOfTravel = itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_date_of_travel);
            tieArrvTime = itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_time);
            tieArrvPlace = itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_place);
            tieClass = itemView.findViewById(R.id.tie_details_of_travel_exp_class);
            tieRemarks = itemView.findViewById(R.id.tie_details_of_travel_exp_remarks);
//            tieSanc =itemView.findViewById(R.id.tie_details_of_travel_exp_sanc_amt);
            tieClaim = itemView.findViewById(R.id.tie_details_of_travel_exp_claim_amt);

            tilSno = itemView.findViewById(R.id.til_details_of_travel_exp_sno);
            tilMode = itemView.findViewById(R.id.til_details_of_travel_exp_mode);
            tilDeptDateOfTravel = itemView.findViewById(R.id.til_details_of_travel_exp_dept_date_of_travel);
            tilDeptTime = itemView.findViewById(R.id.til_details_of_travel_exp_dept_time);
            tilDeptPlace = itemView.findViewById(R.id.til_details_of_travel_exp_dept_place);
            tilArrvDateOfTravel = itemView.findViewById(R.id.til_details_of_travel_exp_arrv_date_of_travel);
            tilArrvTime = itemView.findViewById(R.id.til_details_of_travel_exp_arrv_time);
            tilArrvPlace = itemView.findViewById(R.id.til_details_of_travel_exp_arrv_place);
//            tilClass =itemView.findViewById(R.id.til_details_of_travel_exp_class);
//            tilRemarks =itemView.findViewById(R.id.til_details_of_travel_exp_remarks);
//            tilSanc =itemView.findViewById(R.id.til_details_of_travel_exp_sanc_amt);
            tilClaim = itemView.findViewById(R.id.til_details_of_travel_exp_claim_amt);


            if (setErrorMsg(msg, tilSno, tieSno)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilMode, tieMode)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilDeptPlace, tieDeptPlace)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilArrvTime, tieArrvTime)) {

                isAllFieldFilled = false;

            }
            if (setErrorMsg(msg, tilDeptDateOfTravel, tieDeptDateOfTravel)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilDeptTime, tieDeptTime)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilArrvDateOfTravel, tieArrvDateOfTravel)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilArrvTime, tieArrvTime)) {

                isAllFieldFilled = false;

            }
            if (setErrorMsg(msg, tilArrvPlace, tieArrvPlace)) {

                isAllFieldFilled = false;

            }

            if (setErrorMsg(msg, tilClaim, tieClaim)) {

                isAllFieldFilled = false;

            }


        }

        return isAllFieldFilled;
    }

    private void getBoardingAndLodgingValues() {


        blSno.clear();
        blBoard.clear();
        blLodg.clear();
        blDate.clear();
        blClaim.clear();
        blRemarks.clear();


        //validating boarding and lodging 2
        for (int i = 0; i < claimBoardingAndLodgingExpensesAdapter.getItemCount(); i++) {

            try {

                View itemView = binding.rvClaimBoardingAndLodging.getLayoutManager().findViewByPosition(i);

                TextInputLayout tilSno, tilBoarding, tilLoadging, tilDate, tilRemarks, tilClaimAmt, tilSancAmt;
                TextInputEditText tieSno, tieBoarding, tieLoadging, tieDate, tieRemarks, tieClaimAmt, tieSancAmt;

                tieSno = itemView.findViewById(R.id.tie_claim_boarding_lodging_sno);
                tieBoarding = itemView.findViewById(R.id.tie_claim_boarding_lodging_boarding);
                tieLoadging = itemView.findViewById(R.id.tie_claim_boarding_lodging_loadging);
                tieDate = itemView.findViewById(R.id.tie_claim_boarding_lodging_date_of_travel);
                tieClaimAmt = itemView.findViewById(R.id.tie_claim_boarding_lodging_total_claim_amt);
                tieRemarks = itemView.findViewById(R.id.tie_claim_boarding_lodging_remarks);

                tilSno = itemView.findViewById(R.id.til_claim_boarding_lodging_sno);
                tilBoarding = itemView.findViewById(R.id.til_claim_boarding_lodging_boarding);
                tilLoadging = itemView.findViewById(R.id.til_claim_boarding_lodging_loadging);
                tilDate = itemView.findViewById(R.id.til_claim_boarding_lodging_date_of_travel);
                tilClaimAmt = itemView.findViewById(R.id.til_claim_boarding_lodging_total_claim_amt);


                blSno.add(tieSno.getText().toString());
                blBoard.add(tieBoarding.getText().toString());
                blLodg.add(tieLoadging.getText().toString());
                blDate.add(tieDate.getText().toString());
                blClaim.add(tieClaimAmt.getText().toString());
                blRemarks.add(tieRemarks.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void getDetailOfTravelValues() {


        detailSno.clear();
        detailtmode.clear();
        detailDeptDay.clear();
        detailDeptTime.clear();
        detailDeptPlace.clear();
        detailArrivalPlace.clear();
        detailArrivalDay.clear();
        detailArrivalTime.clear();
        detailclass.clear();
        detailcamt.clear();
        detailremarks.clear();

        for (int i = 0; i < detailsOfTravelingExpensesAdapter.getItemCount(); i++) {

            View itemView = binding.rvClaimFormTravelExpenseDetailsOfTravelAddMoreLayout.getLayoutManager().findViewByPosition(i);

            TextInputLayout tilSno, tilMode, tilDeptDateOfTravel, tilDeptTime, tilDeptPlace, tilArrvDateOfTravel, tilArrvTime, tilArrvPlace, tilClass, tilRemarks, tilSanc, tilClaim;
            TextInputEditText tieSno, tieMode, tieDeptDateOfTravel, tieDeptTime, tieDeptPlace, tieArrvDateOfTravel, tieArrvTime, tieArrvPlace, tieClass, tieRemarks, tieSanc, tieClaim;

            tieSno = itemView.findViewById(R.id.tie_details_of_travel_exp_sno);
            tieMode = itemView.findViewById(R.id.tie_details_of_travel_exp_mode);
            tieDeptDateOfTravel = itemView.findViewById(R.id.tie_details_of_travel_exp_dept_date_of_travel);
            tieDeptTime = itemView.findViewById(R.id.tie_details_of_travel_exp_dept_time);
            tieDeptPlace = itemView.findViewById(R.id.tie_details_of_travel_exp_dept_place);
            tieArrvDateOfTravel = itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_date_of_travel);
            tieArrvTime = itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_time);
            tieArrvPlace = itemView.findViewById(R.id.tie_details_of_travel_exp_arrv_place);
            tieClass = itemView.findViewById(R.id.tie_details_of_travel_exp_class);
            tieRemarks = itemView.findViewById(R.id.tie_details_of_travel_exp_remarks);
//            tieSanc =itemView.findViewById(R.id.tie_details_of_travel_exp_sanc_amt);
            tieClaim = itemView.findViewById(R.id.tie_details_of_travel_exp_claim_amt);

            tilSno = itemView.findViewById(R.id.til_details_of_travel_exp_sno);
            tilMode = itemView.findViewById(R.id.til_details_of_travel_exp_mode);
            tilDeptDateOfTravel = itemView.findViewById(R.id.til_details_of_travel_exp_dept_date_of_travel);
            tilDeptTime = itemView.findViewById(R.id.til_details_of_travel_exp_dept_time);
            tilDeptPlace = itemView.findViewById(R.id.til_details_of_travel_exp_dept_place);
            tilArrvDateOfTravel = itemView.findViewById(R.id.til_details_of_travel_exp_arrv_date_of_travel);
            tilArrvTime = itemView.findViewById(R.id.til_details_of_travel_exp_arrv_time);
            tilArrvPlace = itemView.findViewById(R.id.til_details_of_travel_exp_arrv_place);
//            tilClass =itemView.findViewById(R.id.til_details_of_travel_exp_class);
//            tilRemarks =itemView.findViewById(R.id.til_details_of_travel_exp_remarks);
//            tilSanc =itemView.findViewById(R.id.til_details_of_travel_exp_sanc_amt);
            tilClaim = itemView.findViewById(R.id.til_details_of_travel_exp_claim_amt);


            detailSno.add(tieSno.getText().toString());
            detailtmode.add(tieMode.getText().toString());
            detailDeptDay.add(tieDeptDateOfTravel.getText().toString());
            detailDeptTime.add(tieDeptTime.getText().toString());
            detailDeptPlace.add(tieDeptPlace.getText().toString());
            detailArrivalPlace.add(tieArrvPlace.getText().toString());
            detailArrivalDay.add(tieArrvDateOfTravel.getText().toString());
            detailArrivalTime.add(tieArrvTime.getText().toString());
            detailclass.add(tieClass.getText().toString());
            detailcamt.add(tieClaim.getText().toString());
            detailremarks.add(tieRemarks.getText().toString());
        }
    }

    private void getLocalConveyanceAndVisitFormValues() {


        /*
         * Localconvyance Expense
         *
         */

        lcsno.clear();
        lcdate.clear();
        lctime.clear();
        lcfrom.clear();
        lcto.clear();
        lcmode.clear();
        lcclaimamt.clear();
        lcRemarks.clear();


        for (int i = 0; i < claimLocalConveyanceAndOutstationVisitAdapter.getItemCount(); i++) {

            View itemView = binding.rvClaimFormTravelExpenseLocalConveyanceAndOnOutstationVisitAddMoreLayout.getLayoutManager().findViewByPosition(i);

            TextInputLayout tilSno, tilTime, tilMode, tilFrom, tilTo, tilDate, tilRemarks, tilClaimAmt, tilSancAmt, tilClass;
            TextInputEditText tieSno, tieTime, tieMode, tieFrom, tieTo, tieDate, tieRemarks, tieClaimAmt, tieSancAmt, tieClass;

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


            lcsno.add(tieSno.getText().toString());
            lcdate.add(tieDate.getText().toString());
            lctime.add(tieTime.getText().toString());
            lcfrom.add(tieFrom.getText().toString());
            lcto.add(tieTo.getText().toString());
            lcmode.add(tieMode.getText().toString());
            lcclaimamt.add(tieClaimAmt.getText().toString());
            lcRemarks.add(tieRemarks.getText().toString());


        }

    }

    private boolean setErrorMsg(String msg, TextInputLayout til, TextInputEditText tie) {

        if (tie.getText().toString().isEmpty()) {

            til.setError(msg);
            return true;
        } else {

            til.setErrorEnabled(false);
            til.setError(null);
            return false;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {

            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            detailFilePath = path;
            Log.e("1000", path);
            String filename = path.substring(path.lastIndexOf("/") + 1);
            Log.e("1000", filename);
            String filetype = filename.substring(filename.lastIndexOf(".") + 1);

            //Adding attachments in list of  details
            detailAttachmentsList.add(new Attachments(filename, path, filetype));
            attachDetailsAdapter.notifyDataSetChanged();

            //binding.tvClaimFormTravelExpenseDetailsOfTravelFileName.setText(path.substring(path.lastIndexOf("/")+1));


        } else if (requestCode == 1001 && resultCode == RESULT_OK) {

            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            boardingLoadingFilePath = path;
            // binding.tvClaimFormTravelExpenseBoardingLodgingFileName.setText(path.substring(path.lastIndexOf("/")+1));
            Log.e("1001", path);

            String filename = path.substring(path.lastIndexOf("/") + 1);
            Log.e("1001", filename);
            String filetype = filename.substring(filename.lastIndexOf(".") + 1);

            //Adding attachments in list of  details
            bordingLoadgingAttachmentsList.add(new Attachments(filename, path, filetype));
            attachbordingLoadgingAdapter.notifyDataSetChanged();


        } else if (requestCode == 1002 && resultCode == RESULT_OK) {

            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            miscFilePath = path;
            //binding.tvClaimFormTravelExpenseMiscFileName.setText(path.substring(path.lastIndexOf("/")+1));
            Log.e("1002", path);

            String filename = path.substring(path.lastIndexOf("/") + 1);
            Log.e("1002", filename);
            String filetype = filename.substring(filename.lastIndexOf(".") + 1);

            //Adding attachments in list of  details
            miscAttachmentsList.add(new Attachments(filename, path, filetype));
            attachmiscAdapter.notifyDataSetChanged();

        } else if (requestCode == 1003 && resultCode == RESULT_OK) {

            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            //localConveyanceFilePath=path;
            //  binding.tvClaimFormTravelExpenseLocalConveyFileName.setText(path.substring(path.lastIndexOf("/")+1));
            Log.e("1003", path);

            String filename = path.substring(path.lastIndexOf("/") + 1);
            Log.e("1003", filename);
            String filetype = filename.substring(filename.lastIndexOf(".") + 1);

            //Adding attachments in list of  details
            localConveyanceAttachmentsList.add(new Attachments(filename, path, filetype));
            attachlocalConveyanceAdapter.notifyDataSetChanged();

        } else if (requestCode == 1004 && resultCode == RESULT_OK) {

            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            //localConveyanceFilePath=path;
            //  binding.tvClaimFormTravelExpenseLocalConveyFileName.setText(path.substring(path.lastIndexOf("/")+1));
            Log.e("1004", path);

            String filename = path.substring(path.lastIndexOf("/") + 1);
            Log.e("1004", filename);
            String filetype = filename.substring(filename.lastIndexOf(".") + 1);

            //Adding attachments in list of  details
            mainAttachmentsList.add(new Attachments(filename, path, filetype));
            attachMainAdapter.notifyDataSetChanged();

            binding.llClaimSelectStorage.setVisibility(View.VISIBLE);

        }

    }


    //Toggle
    private void onToggleBasicInfo() {
        if (binding.elBasicInfo.isExpanded()) {

            binding.elBasicInfo.toggle();
            binding.ivClaimBasicInfo.animate().rotation(0f).start();

        } else {

            binding.elBasicInfo.toggle();
            binding.ivClaimBasicInfo.animate().rotation(90f).start();
        }
    }

    private void onToggleBankDetails(){

        if (binding.elClaimBankDetails.isExpanded()) {

            binding.elClaimBankDetails.toggle();
            binding.ivClaimBankDetails.animate().rotation(0f).start();

        } else {

            binding.elClaimBankDetails.toggle();
            binding.ivClaimBankDetails.animate().rotation(90f).start();
        }

    }

    private void onToggleMiscExp() {

        if (binding.elClaimMiscExp.isExpanded()) {

            binding.elClaimMiscExp.toggle();
            binding.ivClaimMiscExp.animate().rotation(0f).start();

        } else {

            binding.elClaimMiscExp.toggle();
            binding.ivClaimMiscExp.animate().rotation(90f).start();
        }


    }

    private void onToggleDetailsOfTravel() {

        if (binding.elClaimDetailsOfTravel.isExpanded()) {

            binding.elClaimDetailsOfTravel.toggle();
            binding.ivClaimDetailsOfTravel.animate().rotation(0f).start();

        } else {

            binding.elClaimDetailsOfTravel.toggle();
            binding.ivClaimDetailsOfTravel.animate().rotation(90f).start();
        }

    }

    private void onToggleBoardingAndLodging() {

        if (binding.elClaimBoardingAndLodging.isExpanded()) {

            binding.elClaimBoardingAndLodging.toggle();
            binding.ivClaimBoardingAndLodging.animate().rotation(0f).start();

        } else {

            binding.elClaimBoardingAndLodging.toggle();
            binding.ivClaimBoardingAndLodging.animate().rotation(90f).start();
        }

    }

    private void onToggleLocalConveyance() {

        if (binding.elClaimLocalConveyance.isExpanded()) {

            binding.elClaimLocalConveyance.toggle();
            binding.ivClaimLocalConveyance.animate().rotation(0f).start();

        } else {

            binding.elClaimLocalConveyance.toggle();
            binding.ivClaimLocalConveyance.animate().rotation(90f).start();
        }

    }

    private void alertProcessing(Context context) {

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


    //for permisiiosn
    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClaimFormTravelExpenseActivity.this);
        builder.setTitle(getString(R.string.need_permisions));
        builder.setMessage(getString(R.string.grant_permission_message));
        builder.setPositiveButton(getString(R.string.goto_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void showMultistorageDialog(Context context){


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater =this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_multi_storage, null);

        RecyclerView rvMain = dialogView.findViewById(R.id.rv_select_multi_storage);
        rvMain.setLayoutManager(new LinearLayoutManager(this));

        StorageAllotedAdapter storageAllotedAdapter = new StorageAllotedAdapter(storageAllotedList, context);
        rvMain.setAdapter(storageAllotedAdapter);
        storageAllotedAdapter.setOnClickListener(new StorageAllotedAdapter.OnClickListener() {
            @Override
            public void onStorageClicked(String slid, String storagename) {

                selectStoragePopUp(slid);//bottomsheet

            }
        });


        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


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

}
