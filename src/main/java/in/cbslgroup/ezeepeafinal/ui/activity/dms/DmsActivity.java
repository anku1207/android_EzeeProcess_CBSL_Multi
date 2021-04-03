package in.cbslgroup.ezeepeafinal.ui.activity.dms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.FileViewAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.FileViewHorizontalAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.MetaViewBottomSheetAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.SharedFileStatusAdapter;
import in.cbslgroup.ezeepeafinal.chip.FilterAdapter;
import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.chip.UsernameCompletionView;
import in.cbslgroup.ezeepeafinal.interfaces.CustomItemClickListener;
import in.cbslgroup.ezeepeafinal.model.FileViewList;
import in.cbslgroup.ezeepeafinal.model.Foldername;
import in.cbslgroup.ezeepeafinal.model.MetaBottomSheet;
import in.cbslgroup.ezeepeafinal.model.SharedFileStatus;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.ConnectivityReceiver;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

import static in.cbslgroup.ezeepeafinal.utils.ApiUrl.STORAGE_URL;


public class DmsActivity extends AppCompatActivity implements View.OnLongClickListener, ConnectivityReceiver.ConnectivityReceiverListener  {

    public static String dynamicFileSlid;
    public static String foldernameDyanamic;
    public static String slid_Session, userid;
    public static String roleid;
    public static String copy_storage;
    public static String move_storage;
    public static String ip;
    public static String lockfolder;
    public static String isProtected;
    public static String shareFolder;

    AlertDialog alertDialog, alertDialogSuccess;

    RecyclerView rvHierarcy, rvHorizontal, rvFileView;

    String rootStorage1;

    ProgressBar progressBar, progressBar2;
    SessionManager session;


    TextView tvTotalFolders, tvTotalFiles, tvTotalFileSize, tvHeading;

    LinearLayout llDmsRoot, llNoFileFound, llDmsNavLeft;

    CardView cvBtnMetadata;

    DrawerLayout drawer;
    View navView;
    ImageButton btnAddMetaForm, btnRemoveMetaForm;
    Button btnMetaSearch;
    List<String> spinnerConditionlist = new ArrayList<>();


    EditText etRootMetadataText;
    String rootStorage;


    ArrayAdapter<String> dynamicSpinnerAdapter;
    FileViewHorizontalAdapter fileViewHorizontalAdapter;
    ArrayList<String> spinnerMetaList = new ArrayList<>();

    String rootSpinnerMetadata, rootSpinnerCondition, rootEditTextMetaDataText;
    CardView cvMetaFormStatic;
    TextView tvNoMetaData;
    JSONArray jsonArrayMetadata;
    int counterAddButton = 1;

    String dynamicFoldername;


    ArrayList<String> metadata = new ArrayList<>();
    ArrayList<String> condition = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();


    CardView cvNavLeftMetaFormDyanamic;
    View rowView;
    int count;
    ImageView ivMetaFolder, ivLockFolder;
    String session_userName = null, session_userEmail = null, session_designation = null, session_contact = null;
    RelativeLayout relativeLayout;
    LinearLayout llnointernetfound;
    ScrollView scrollViewmain;
    Toolbar toolbar;


    NavigationView navigationView;
    ImageView ivCloseDrawer;
    private int lastPosition = -1;

    private List<Foldername> horizontalist = new ArrayList<>();
    private List<FileViewList> fileViewList = new ArrayList<>();
    private List<Foldername> foldernames = new ArrayList<>();
    private List<Foldername> onclickfoldernames = new ArrayList<>();
    private List<View> myLayouts = new ArrayList<>();


    private Spinner staticSpinnerMetaData, staticSpinnerCondition;
    private FileViewAdapter fileViewAdapter;

    List<MetaBottomSheet> metaDataViewBottomsheetList = new ArrayList<>();

    BottomSheetDialog bottomSheetDialog;
    RecyclerView rvBottomSheetMetaview;

    ArrayAdapter<String> conditionListAdapter;
    Intent intentMultiMeta;
    Boolean jumptoMetaSearch = false;

    //User list
    List<User> userList = new ArrayList<>();

    OnToolbarLayedFinished onToolbarLayedFinished;

    private void setOnToolbarLayedFinished(OnToolbarLayedFinished onToolbarLayedFinished) {
        this.onToolbarLayedFinished = onToolbarLayedFinished;
    }

    interface OnToolbarLayedFinished{

        void onFinished();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dms);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initLocale();
        initSessionManager();
        getIntentData();
        initViews();
        initToolbar();

        setOnToolbarLayedFinished(new OnToolbarLayedFinished() {
            @Override
            public void onFinished() {

                initSpinners();
                initRecyclerViews();
                initListeners();

                if (isNetworkAvailable()) {

                    llnointernetfound.setVisibility(View.GONE);


                    //getting role_id
                    getUserRole(userid);
                    getGroupMemberList(userid);

                    //ipaddress
                    ip = session.getIP();


                } else {


                    llnointernetfound.setVisibility(View.VISIBLE);
                    scrollViewmain.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.GONE);
                    Toast.makeText(DmsActivity.this, R.string.no_internet_connection_found, Toast.LENGTH_LONG).show();

                }

            }
        });
        //userid


    }

    private void getIntentData() {

       Intent intent =  getIntent();
       slid_Session = intent.getStringExtra("slid");
       //it is used in recurison of file hierraccy
       dynamicFileSlid = slid_Session;

//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
//        slid_Session = sharedPreferences.getString("userSlid", null);
        //giving slid to adapter when file is clicked

    }

    private void initListeners() {

        ivMetaFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showBottomSheetDialog();

                //assignedMetaData(dynamicFileSlid);


            }
        });
        ivCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Load the animation like this
                Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                        android.R.anim.fade_out);

                // Start the animation like this
                navigationView.startAnimation(animSlide);

                navigationView.setVisibility(View.GONE);


            }
        });
        //ScrollView scrollViewnavleft = navView.findViewById(R.id.scrollview_dms_nav_left);
        btnAddMetaForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (jsonArrayMetadata.length() > counterAddButton) {

                    addMetaForm();
                    counterAddButton++;
                    Log.e("Counteraddbutton", String.valueOf(counterAddButton));
                } else {

                    Toast.makeText(DmsActivity.this, "No more metadata", Toast.LENGTH_LONG).show();
                }


            }
        });
        btnMetaSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metadata.clear();
                condition.clear();
                text.clear();


                rootSpinnerMetadata = staticSpinnerMetaData.getSelectedItem().toString();
                rootSpinnerCondition = staticSpinnerCondition.getSelectedItem().toString();


                if (counterAddButton == 1) {

                    if (rootSpinnerMetadata.equals("Select Metadata")) {


                        ((TextView) staticSpinnerMetaData.getSelectedView()).setError(getString(R.string.please_select_metadata));
                        (staticSpinnerMetaData.getSelectedView()).requestFocus();

                        Toast.makeText(DmsActivity.this, "Select Metadata", Toast.LENGTH_SHORT).show();


                    } else if (rootSpinnerCondition.equals("Select Condition")) {

                        ((TextView) staticSpinnerCondition.getSelectedView()).setError(getString(R.string.please_select__condition));
                        (staticSpinnerCondition.getSelectedView()).requestFocus();

                        Toast.makeText(DmsActivity.this, "Select Condition", Toast.LENGTH_SHORT).show();


                    } else if (etRootMetadataText.getText().toString().equals("") || etRootMetadataText.getText().toString().isEmpty()) {


                        etRootMetadataText.setError(getString(R.string.please_enter_text_for_search));
                        etRootMetadataText.clearFocus();


                        Toast.makeText(DmsActivity.this, getString(R.string.please_enter_text_for_search), Toast.LENGTH_SHORT).show();


                    } else {

                        ((TextView) staticSpinnerCondition.getSelectedView()).setError(null);
                        ((TextView) staticSpinnerMetaData.getSelectedView()).setError(null);
                        etRootMetadataText.setError(null);

                        rootSpinnerMetadata = staticSpinnerMetaData.getSelectedItem().toString();
                        rootSpinnerCondition = staticSpinnerCondition.getSelectedItem().toString();
                        rootEditTextMetaDataText = String.valueOf(etRootMetadataText.getText());

                        metadata.add(rootSpinnerMetadata);
                        condition.add(rootSpinnerCondition);
                        text.add(rootEditTextMetaDataText);

                 /*       metadata.add("ankit");
                        condition.add("Equal");
                        text.add("g");*/


                        count = metadata.size();
                        Log.e("metadata size", String.valueOf(count));


                        try {

                            JSONObject jsonObject = makeJsonObject(metadata, condition, text, count);

                            Log.e("jsonobject", String.valueOf(jsonObject));

                            Intent intent = new Intent(DmsActivity.this, MetaSearchFileViewActivity.class);
                            intent.putExtra("slid", dynamicFileSlid);
                            intent.putExtra("metadata", String.valueOf(jsonObject));
                            intent.putExtra("foldername", dynamicFoldername);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            // multiMetaSearch(String.valueOf(jsonObject),"329");

                            // Log.e("jsonobject", String.valueOf(jsonObject));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                } else if (counterAddButton > 1) {


                    EditText et = null;


                    if (rootSpinnerMetadata.equals("Select Metadata")) {


                        ((TextView) staticSpinnerMetaData.getSelectedView()).setError("please select metadata");
                        (staticSpinnerMetaData.getSelectedView()).requestFocus();
                        jumptoMetaSearch = false;
                        Toast.makeText(DmsActivity.this, R.string.select_metadata, Toast.LENGTH_SHORT).show();


                    } else if (rootSpinnerCondition.equals("Select Condition")) {

                        ((TextView) staticSpinnerCondition.getSelectedView()).setError("please select condition");
                        (staticSpinnerCondition.getSelectedView()).requestFocus();

                        Toast.makeText(DmsActivity.this, R.string.select_condition, Toast.LENGTH_SHORT).show();


                    } else if (etRootMetadataText.getText().toString().equals("") || etRootMetadataText.getText().toString().isEmpty()) {


                        etRootMetadataText.setError(getString(R.string.please_enter_text_for_search));
                        etRootMetadataText.clearFocus();


                        Toast.makeText(DmsActivity.this, getString(R.string.please_enter_text_for_search), Toast.LENGTH_SHORT).show();


                    } else {

                        etRootMetadataText.setError(null);
                        rootEditTextMetaDataText = String.valueOf(etRootMetadataText.getText());

                        metadata.add(rootSpinnerMetadata);
                        condition.add(rootSpinnerCondition);
                        text.add(rootEditTextMetaDataText);


                        for (int i = 0; i < myLayouts.size(); i++) {

                            et = myLayouts.get(i).findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);

                            Spinner s1 = myLayouts.get(i).findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
                            Spinner s2 = myLayouts.get(i).findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);

                            String metadatatext = s1.getSelectedItem().toString();
                            String conditiontext = s2.getSelectedItem().toString();
                            String textTxt = null;


                            if (metadatatext.equals("Select Metadata")) {


                                ((TextView) s1.getSelectedView()).setError(getString(R.string.please_select_metadata));
                                (s1.getSelectedView()).requestFocus();
                                jumptoMetaSearch = false;
                                Toast.makeText(DmsActivity.this, getString(R.string.select_metadata), Toast.LENGTH_SHORT).show();


                            } else if (conditiontext.equals("Select Condition")) {

                                ((TextView) s2.getSelectedView()).setError(getString(R.string.please_select__condition));
                                (s2.getSelectedView()).requestFocus();
                                jumptoMetaSearch = false;
                                Toast.makeText(DmsActivity.this, getString(R.string.select_condition), Toast.LENGTH_SHORT).show();


                            } else if (et.getText().toString().equals("") || et.getText().toString().isEmpty()) {


                                et.setError("Please enter text for search");
                                et.clearFocus();

                                jumptoMetaSearch = false;
                                Toast.makeText(DmsActivity.this, getString(R.string.please_enter_text_for_search), Toast.LENGTH_SHORT).show();


                            } else {

                                et.setError(null);
                                textTxt = String.valueOf(et.getText());

                                metadata.add(metadatatext);
                                condition.add(conditiontext);
                                text.add(textTxt);


                                count = metadata.size();
                                Log.e("metadata 2 size", String.valueOf(count));


                                try {

                                    JSONObject jsonObject = makeJsonObject(metadata, condition, text, count);

                                    Log.e("jsonobject", String.valueOf(jsonObject));

                                    intentMultiMeta = new Intent(DmsActivity.this, MetaSearchFileViewActivity.class);
                                    intentMultiMeta.putExtra("slid", dynamicFileSlid);
                                    intentMultiMeta.putExtra("metadata", String.valueOf(jsonObject));
                                    intentMultiMeta.putExtra("foldername", dynamicFoldername);

                                    jumptoMetaSearch = true;

                                    // multiMetaSearch(String.valueOf(jsonObject),"329");

                                    // Log.e("jsonobject", String.valueOf(jsonObject));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        if (jumptoMetaSearch) {

                            startActivity(intentMultiMeta);
                        }

                       /* count = metadata.size();
                        Log.e("metadata 2 size", String.valueOf(count));


                        try {

                            JSONObject jsonObject = makeJsonObject(metadata, condition, text, count);

                            Log.e("jsonobject", String.valueOf(jsonObject));

                            Intent intent = new Intent(DmsActivity.this, MetaSearchFileViewActivity.class);
                            intent.putExtra("slid", dynamicFileSlid);
                            intent.putExtra("metadata", String.valueOf(jsonObject));
                            intent.putExtra("foldername", dynamicFoldername);
                            startActivity(intent);

                            // multiMetaSearch(String.valueOf(jsonObject),"329");

                            // Log.e("jsonobject", String.valueOf(jsonObject));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/

                        // Toast.makeText(DmsActivity.this, textTxt + " " + metadatatext + " " + textTxt, Toast.LENGTH_LONG).show();

                    }

                }


            }


            // multiMetaSearch(rootSpinnerMetadata, rootSpinnerCondition, rootEditTextMetaDataText, "329");


            // Toast.makeText(DmsActivity.this, rootSpinnerMetadata + " " + rootSpinnerCondition + " " + rootEditTextMetaDataText, Toast.LENGTH_LONG).show();


        });
    }

    private void initSpinners() {


        spinnerConditionlist.add("Equal");
        spinnerConditionlist.add("Contains");
        spinnerConditionlist.add("Like");
        spinnerConditionlist.add("Not Like");
        spinnerConditionlist.add("Select Condition");


        if (staticSpinnerCondition != null) {

            conditionListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
                @Override
                public int getCount() {
                    // don't display last item. It is used as hint.
                    int count = super.getCount();
                    return count > 0 ? count - 1 : count;
                }
            };

            conditionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            staticSpinnerCondition.setAdapter(conditionListAdapter);
            staticSpinnerCondition.setSelection(conditionListAdapter.getCount());
            //conditionListAdapter.notifyDataSetChanged();


        } else {

            Log.e("staticSpinnerCondition", "null");
        }

        if (staticSpinnerMetaData != null) {

            dynamicSpinnerAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerMetaList) {
                @Override
                public int getCount() {
                    // don't display last item. It is used as hint.
                    int count = super.getCount();
                    return count > 0 ? count - 1 : count;
                }
            };

        } else {


            Log.e("staticSpinnerMetadata", "null");
        }
    }

    private void initRecyclerViews() {
        //recyclerview Hierarcy (gallery view)
        rvHierarcy = findViewById(R.id.rv_storage_management);
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        // rvHierarcy.setLayoutManager(gridLayoutManager);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvHierarcy.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            rvHierarcy.setLayoutManager(new GridLayoutManager(this, 5));
        }
        //rvHierarcy.setHasFixedSize(true);

        //recyclerview horizontal list (listview horizontal)
        rvHorizontal = findViewById(R.id.rv_storage_management_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHorizontal.setLayoutManager(linearLayoutManager);
        rvHorizontal.setHasFixedSize(true);

        fileViewAdapter = new FileViewAdapter(DmsActivity.this, foldernames, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String slid, String fullFolderName) {

            }
        });


        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horizontalist, DmsActivity.this, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String slid, String fullFolderName) {

                counterAddButton = 1;
                onclickfoldernames.clear();
                foldernames.clear();
                //  llDmsRoot.removeAllViews();

                CardView cardView = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);
                if (cardView != null) {
                    llDmsNavLeft.removeView(cardView);

                } else {

                    Log.e("cardview", "null");
                }


                if (slid == slid_Session) {

                    horizontalist.clear();
                    String id = rootStorage + "&&" + slid_Session;
                    Foldername foldername1 = new Foldername();
                    foldername1.setFoldername(id);
                    horizontalist.add(foldername1);
                    fileViewHorizontalAdapter.notifyDataSetChanged();


                } else {

                    horizontalist.subList(position + 1, horizontalist.size()).clear();
                    fileViewHorizontalAdapter.notifyDataSetChanged();
                    // fileViewHorizontalAdapter.deleteItem(position+1);
                    recursiveGetFolder(slid);
                    getMetadata(slid);

                }

                setAnimation(v, position);

            }
        });
        rvHorizontal.setAdapter(fileViewHorizontalAdapter);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_storage_management);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });
    }

    private void initViews() {

        tvTotalFiles = findViewById(R.id.tv_dms_heading_total_files);
        tvTotalFileSize = findViewById(R.id.tv_dms_heading_total_size);
        tvTotalFolders = findViewById(R.id.tv_dms_heading_total_folder);
        tvHeading = findViewById(R.id.tv_dms_heading);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        ivMetaFolder = findViewById(R.id.iv_meta_icon);
        ivLockFolder = findViewById(R.id.iv_lock_icon);
        relativeLayout = findViewById(R.id.rl_dms_main);
        llnointernetfound = findViewById(R.id.ll_dmsactivity_nointernetfound);
        scrollViewmain = findViewById(R.id.dms_scrollview_main);
        llDmsRoot = findViewById(R.id.ll_root_dmsActivity);
        llNoFileFound = findViewById(R.id.ll_dmsactivity_nofilefound);


        drawer = findViewById(R.id.dmsactivity_drawer_layout_left);
        navigationView = findViewById(R.id.dmsactivity_nav_view_left);
        navView = navigationView.getHeaderView(0);
        llDmsNavLeft = navView.findViewById(R.id.ll_dms_nav_left_drawer);
        btnAddMetaForm = navView.findViewById(R.id.iv_nav_left_add_btn);
        staticSpinnerMetaData = navView.findViewById(R.id.spinner_metadata_nav_left_dmsactvity);
        staticSpinnerCondition = navView.findViewById(R.id.spinner_condition_nav_left_dmsactvity);
        cvBtnMetadata = navView.findViewById(R.id.cv_nav_left_form_metadatasearch_button);
        cvMetaFormStatic = navView.findViewById(R.id.cv_dmsactivity_nav_left);
        cvNavLeftMetaFormDyanamic = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);

        ivCloseDrawer = navView.findViewById(R.id.iv_close_drawer_nav_drawer_dms);

        etRootMetadataText = navView.findViewById(R.id.et_static__text_search_nav_left_form);
        tvNoMetaData = navView.findViewById(R.id.tv_noMetaDataFound);
        btnMetaSearch = navView.findViewById(R.id.btn_metadatasearch_nav_left);

    }

    private void initSessionManager() {
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // userid
        userid = user.get(SessionManager.KEY_USERID);
        session_userName = user.get(SessionManager.KEY_NAME);
        session_userEmail = user.get(SessionManager.KEY_EMAIL);
        session_designation = user.get(SessionManager.KEY_DESIGNATION);
        session_contact = user.get(SessionManager.KEY_CONTACT);



        //  Log.e("username",session_userName+" "+session_userEmail);
    }

    /* * **********************************************************
     *                   Alertdialog and Bottomsheets             *
     * *********************************************************** */

    private void shareFolderDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog_share_folder, null);

        UsernameCompletionView autoCompleteTextView = dialogView.findViewById(R.id.autocomplete_textview);
        TextView tvHeading = dialogView.findViewById(R.id.alert_dialog_share_folder_folder_name);
        tvHeading.setText(foldernameDyanamic);

        //Set the listener that will be notified of changes in the Tokenlist
        autoCompleteTextView.setTokenListener(new TokenCompleteTextView.TokenListener<User>() {
            @Override
            public void onTokenAdded(User token) {

                Log.e("Chip", "Added");

            }

            @Override
            public void onTokenRemoved(User token) {

                Log.e("Chip", "Added");
            }
        });

        //Set the action to be taken when a Token is clicked
        autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                autoCompleteTextView.showDropDown();

            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                autoCompleteTextView.showDropDown();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                autoCompleteTextView.showDropDown();

            }

            @Override
            public void afterTextChanged(Editable s) {


                autoCompleteTextView.showDropDown();
            }
        });


        Button btn_share = dialogView.findViewById(R.id.btn_share_popup);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<User> selectedUser = autoCompleteTextView.getObjects();

                if (selectedUser.size() != 0) {


                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < selectedUser.size(); i++) {

                        jsonArray.put(selectedUser.get(i).getUserid());

                    }

                    shareFolder(session.getUserId(), jsonArray.toString(), dynamicFileSlid);
                    alertDialog.dismiss();

                } else {

                    autoCompleteTextView.setError(getString(R.string.select_one_person_to_share));
                    autoCompleteTextView.requestFocus();
                }

            }
        });

        Button btn_close = dialogView.findViewById(R.id.btn_close_share_popup);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code here for close dialog button

                alertDialog.dismiss();

            }
        });

        //Initializing and attaching adapter for AutocompleteTextView
        FilterAdapter filterAdapter = new FilterAdapter(this, R.layout.item_user, userList);
        autoCompleteTextView.setAdapter(filterAdapter);
        autoCompleteTextView.allowDuplicates(false);
        autoCompleteTextView.setShowAlways(true);

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    //Create new Child method
    private void createNewChildPopup() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);

        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_create_new_child, null);
        TextView tvFname = dialogView.findViewById(R.id.tv_create_new_child_docname);

        String lang =  LocaleHelper.getPersistedData(this,"en");
        if(lang.equalsIgnoreCase("en")){

            tvFname.setText(getString(R.string.add_sub_folder_to)+""+ foldernameDyanamic);

        }

        else if(lang.equalsIgnoreCase("hi")){

            tvFname.setText(foldernameDyanamic + getString(R.string.add_sub_folder_to));
        }




        final EditText etName = dialogView.findViewById(R.id.et_child_name);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_create_new_child_cancel);
        Button btn_createChild = dialogView.findViewById(R.id.btn_create_new_child_add);

        btn_createChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String childName = etName.getText().toString();

                if (childName.isEmpty() || childName == null) {

                    etName.setError("Enter Child Name");
                } else {

                    etName.setError(null);
                    addNewChild(dynamicFileSlid, childName);
                    alertDialog.dismiss();

                }
                //alertDialog.dismiss();

            }
        });

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    //Change stoarge name method
    private void modifyStorageNamePopup() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.modify_storage_popup, null);

        final EditText etName = dialogView.findViewById(R.id.et_modify_storage_name);
        etName.setText(foldernameDyanamic);
        etName.setSelection(etName.getText().length());
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_modify_storage_cancel);
        Button btn_save_changes = dialogView.findViewById(R.id.btn_modify_storage_savechanges);

        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String storageName = etName.getText().toString();

                if (storageName.isEmpty() || storageName == null) {

                    etName.setError(getString(R.string.enter_storage_name));
                } else {

                    etName.setError(null);
                    modifyStorage(dynamicFileSlid, storageName);
                    alertDialog.dismiss();

                }


            }
        });

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    private void showBottomSheetDialog() {


        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_assignedmetadata, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        rvBottomSheetMetaview = bottomSheetDialog.findViewById(R.id.bottom_sheet_rv_metaview);
        rvBottomSheetMetaview.setLayoutManager(new LinearLayoutManager(this));
        rvBottomSheetMetaview.setHasFixedSize(true);
        rvBottomSheetMetaview.setItemViewCacheSize(metaDataViewBottomsheetList.size());

        assignedMetaData(dynamicFileSlid);


    }

    private void resetMultiDialog() {

        spinnerConditionlist.clear();

        spinnerConditionlist.add("Equal");
        spinnerConditionlist.add("Contains");
        spinnerConditionlist.add("Like");
        spinnerConditionlist.add("Not Like");
        spinnerConditionlist.add("Select Condition");


        if (staticSpinnerCondition != null) {

            conditionListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
                @Override
                public int getCount() {
                    // don't display last item. It is used as hint.
                    int count = super.getCount();
                    return count > 0 ? count - 1 : count;
                }
            };

            conditionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            staticSpinnerCondition.setAdapter(conditionListAdapter);

            staticSpinnerCondition.setSelection(conditionListAdapter.getCount());
            //conditionListAdapter.notifyDataSetChanged();


        } else {

            Log.e("staticSpinnerCondition", "null");
        }

        // conditionListAdapter.notifyDataSetChanged();


        etRootMetadataText.setText("");

    }

    private void showFolderLockUnlockDialog(Context context, String selectedFolder, String folderSlid, String mode) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog_file_lock_password, null);

        TextInputEditText tiePwd = dialogView.findViewById(R.id.tie_alert_dialog_lock_pwd_enter_pwd);
        TextInputLayout tilPwd = dialogView.findViewById(R.id.til_alert_dialog_lock_pwd_enter_pwd);

        TextView tvBanner = dialogView.findViewById(R.id.tv_alert_dialog_lock_pwd_banner_enter_pwd);
        tvBanner.setVisibility(View.VISIBLE);


        if (mode.equalsIgnoreCase("Lock")) {

            tilPwd.setHint(getString(R.string.enter_pwd));
            tvBanner.setText(R.string.lock_folder);

        } else if (mode.equalsIgnoreCase("Unlock")) {

            tilPwd.setHint(getString(R.string.enter_old_pwd));
            tvBanner.setText(R.string.unlock_folder);

        }


        TextInputLayout tilSelectFolder = dialogView.findViewById(R.id.til_alert_dialog_lock_pwd_select_folder);

        TextInputEditText tieSelectFolder = dialogView.findViewById(R.id.tie_alert_dialog_lock_pwd_select_folder);
        tieSelectFolder.setText(selectedFolder);
        tilSelectFolder.setVisibility(View.VISIBLE);


        Button btnCancel = dialogView.findViewById(R.id.btn_alert_dialog_lock_pwd_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alertDialog.dismiss();

            }
        });


        Button btnSubmit = dialogView.findViewById(R.id.btn_alert_dialog_lock_pwd_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tilPwd.setError(null);
                tilPwd.setErrorEnabled(false);

                String text = tiePwd.getText().toString().trim();
                if (text.length() == 0) {

                    tilPwd.setError(getString(R.string.please_enter_password));

                } else {

                    tilPwd.setError(null);
                    tilPwd.setErrorEnabled(false);


                    String userPwd = tiePwd.getText().toString().trim();
                    Log.e("pwd", userPwd + "_____" + dynamicFileSlid);

                    if (mode.equalsIgnoreCase("lock")) {

                        LockFolder(userPwd, folderSlid);

                    } else if (mode.equalsIgnoreCase("unlock")) {

                        UnLockFolder(userPwd, folderSlid);
                    }


                }


            }
        });


        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    private void forgotPassWordDialog(Context context, String foldername) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog_forgot_lock_password, null);

        Button btnClose = dialogView.findViewById(R.id.btn_alert_dialog_f_lock_close);
        Button btnConfirm = dialogView.findViewById(R.id.btn_alert_dialog_f_lock_confirm);

        String msg = getString(R.string.are_you_sure_that_you_want_to_reset_pwd_for) + foldername + getString(R.string.storage)+"?";

        TextView tvMsg = dialogView.findViewById(R.id.tv_alert_dialog_f_lock_forgot_msg);
        tvMsg.setText(msg);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgotPasswordLockUnlock();

            }
        });


        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }

    private void resetPassWordDialog(Context context, String Otp) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog_folder_lock_reset_password, null);

        TextInputLayout tilOtp = dialogView.findViewById(R.id.til_alert_dialog_folder_reset_otp);
        TextInputLayout tilNewPwd = dialogView.findViewById(R.id.til_alert_dialog_folder_reset_new_pwd);
        TextInputLayout tilConfirm = dialogView.findViewById(R.id.til_alert_dialog_folder_reset_confirm_pwd);

        TextInputEditText tieOtp = dialogView.findViewById(R.id.tie_alert_dialog_folder_reset_otp);
        TextInputEditText tieNewPwd = dialogView.findViewById(R.id.tie_alert_dialog_folder_reset_new_pwd);
        TextInputEditText tieConfirm = dialogView.findViewById(R.id.tie_alert_dialog_folder_reset_confirm_pwd);


        Button btnClose = dialogView.findViewById(R.id.btn_alert_dialog_folder_reset_close);
        Button btnConfirm = dialogView.findViewById(R.id.btn_alert_dialog_folder_reset_confirm);
        btnConfirm.setEnabled(false);

        tieOtp.requestFocus();
        tieOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String enteredOtp = charSequence.toString();
                if (enteredOtp.equalsIgnoreCase(Otp)) {

                    tilOtp.setError(null);
                    tilOtp.setErrorEnabled(false);

                    tieNewPwd.setEnabled(true);
                    tieConfirm.setEnabled(true);


                } else {

                    tilOtp.setError(null);
                    tilOtp.setErrorEnabled(false);

                    tilOtp.setError(getString(R.string.enter_valid_otp));

                    tieNewPwd.setEnabled(false);
                    tieConfirm.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tieConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String newPwd = tieNewPwd.getText().toString();
                if (newPwd.equalsIgnoreCase(charSequence.toString())) {

                    btnConfirm.setEnabled(true);
                    tilConfirm.setError(null);
                    tilConfirm.setErrorEnabled(false);

                } else {

                    btnConfirm.setEnabled(false);

                    tilConfirm.setError(null);
                    tilConfirm.setErrorEnabled(false);

                    tilConfirm.setError(getString(R.string.new_pwd_confirm_pwd_doesnt_match));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                resetPassword(tieConfirm.getText().toString());


            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }

    /* * **********************************************************
     *                   Activity Methods                         *
     * *********************************************************** */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dms_settings, menu);
        onToolbarLayedFinished.onFinished();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_dms_grid_switch) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            supportInvalidateOptionsMenu();
            boolean isSwitched = fileViewAdapter.toggleItemViewType();
            rvHierarcy.setLayoutManager(isSwitched ? new LinearLayoutManager(DmsActivity.this) : new GridLayoutManager(DmsActivity.this, 3));
            rvHierarcy.setHasFixedSize(true);
       /*     if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                rvHierarcy.setLayoutManager(new GridLayoutManager(this, 3));
            }
            else{
                rvHierarcy.setLayoutManager(new GridLayoutManager(this, 5));
            }*/
            fileViewAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_dms_list_switch) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            supportInvalidateOptionsMenu();
            boolean isSwitched = fileViewAdapter.toggleItemViewType();
            rvHierarcy.setLayoutManager(isSwitched ? new LinearLayoutManager(this) : new GridLayoutManager(this, 3));
            fileViewAdapter.notifyDataSetChanged();


            return true;
        } else if (id == R.id.action_edit_profile) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();


            return true;
        } else if (id == R.id.action_create_new_child) {

            createNewChildPopup();

            return true;
        } else if (id == R.id.action_modify_storage) {

            modifyStorageNamePopup();

            return true;
        } else if (id == R.id.action_move_storage) {


            String foldernameNslid = String.valueOf(horizontalist.get(horizontalist.size() - 1).getFoldername());

            Intent intent = new Intent(DmsActivity.this, MoveCopyStorageActivity.class);
            intent.putExtra("foldername&&slid", foldernameNslid);
            startActivity(intent);

            Log.e("slid", foldernameNslid);

            return true;
        } else if (id == R.id.action_upload_documents) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(DmsActivity.this, UploadActivity.class);
            startActivityForResult(intent, 102);


            return true;
        } else if (id == R.id.action_dms_search) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            // Load the animation like this
            Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                    android.R.anim.fade_in);

            // Start the animation like this
            navigationView.startAnimation(animSlide);


            navigationView.setVisibility(View.VISIBLE);



         /*   if (drawer.isDrawerOpen(GravityCompat.END)) {

                drawer.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.END);
            } else {

                drawer.setVisibility(View.GONE);
                drawer.openDrawer(GravityCompat.END);

            }*/


            return true;
        } else if (id == R.id.action_assign_metadata) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(DmsActivity.this, AssignMetadataActivity.class);
            intent.putExtra("fSlid", horizontalist.get(horizontalist.size() - 1).getFoldername());
            startActivityForResult(intent, 103);


            return true;
        } else if (id == R.id.action_lock_folder) {

            showFolderLockUnlockDialog(this, foldernameDyanamic, dynamicFileSlid, "Lock");

        } else if (id == R.id.action_unlock_folder) {

            showFolderLockUnlockDialog(this, foldernameDyanamic, dynamicFileSlid, "Unlock");

        } else if (id == R.id.action_forgot_pwd) {


            forgotPassWordDialog(this, foldernameDyanamic);

        } else if (id == R.id.action_share_folder) {


            shareFolderDialog();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {

            if (resultCode == RESULT_OK) {

                String slid = data.getStringExtra("slid_dms");
                //Toast.makeText(this, slid, Toast.LENGTH_SHORT).show();
                recursiveGetFolder(slid);

            }

        }

        if (requestCode == 102) {

            if (resultCode == RESULT_OK) {

                String slid = data.getStringExtra("slid_dms");
                //Toast.makeText(this, slid, Toast.LENGTH_SHORT).show();
                recursiveGetFolder(slid);

            }

        }

        if (requestCode == 103) {

            if (resultCode == RESULT_OK) {

                String slid = data.getStringExtra("slid_dms");
                //Toast.makeText(this, slid, Toast.LENGTH_SHORT).show();
                recursiveGetFolder(slid);
                getMetadata(slid);


            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        toolbar = findViewById(R.id.toolbar_storage_management);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        // register connection status listener
        Initializer.getInstance().setConnectivityListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (isNetworkAvailable()) {

            if (horizontalist.size() == 1) {

                Intent intent = new Intent(DmsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            } else {
                String slidPrevious;

                if (horizontalist.size() == 0) {

                    //  Toast.makeText(DmsActivity.this,"Network error",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(DmsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    slidPrevious = horizontalist.get(horizontalist.size() - 2).getFoldername();// 0 index slid
                    String slid = slidPrevious.substring(slidPrevious.indexOf("&&") + 2);
                    //  Log.e("Slid previous",slid);
                    fileViewList.clear();
                    onclickfoldernames.clear();

                    if (slid.isEmpty() || slidPrevious.isEmpty() || slidPrevious.equals("")) {

                        Toast.makeText(DmsActivity.this, "Network error", Toast.LENGTH_LONG).show();

                    } else {

                        recursiveGetFolder(slid);
                        getMetadata(slid);
                        horizontalist.remove(horizontalist.size() - 1);
                        fileViewHorizontalAdapter.notifyDataSetChanged();

                    }
                }


            }
        } else {

            Intent intent = new Intent(DmsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        }


    }


    /* * **********************************************************
     *  Web Methods For the Apis                                 *                                       *
     * *********************************************************** */

    private void getFolder(final String slid, final String userid) {

         /*rvHierarcy.setVisibility(View.GONE);
         progressBar2.setVisibility(View.VISIBLE);*/
        //resetMultiDialog();

        //clearing the metasearch spinner list and textfield
        // spinnerConditionlist.clear();
        // etRootMetadataText.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, STORAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("folderlist", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);


                    rootStorage = jsonObject.getString("storagename");
                    tvHeading.setText(rootStorage);
                    foldernameDyanamic = rootStorage;

                    String id = rootStorage + "&&" + slid_Session;

                    Foldername foldername1 = new Foldername();
                    foldername1.setFoldername(id);
                    horizontalist.add(foldername1);


                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");
                    JSONArray blankfolderArray = jsonObject.getJSONArray("blankFolder");
                    JSONArray folderLockArray = jsonObject.getJSONArray("lockFolder");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.e("getresponse in loop ", jsonArray.getString(i));
                        String jsondataloop = jsonArray.getString(i);
                        String blankfolderloop = blankfolderArray.getString(i);
                        String lockFolderloop = folderLockArray.getString(i);
                        Foldername foldername = new Foldername();
                        foldername.setFoldername(jsondataloop);
                        foldername.setBlankfolder(blankfolderloop);
                        foldername.setLockfolder(lockFolderloop);
                        foldernames.add(foldername);

                    }

                    String totalfolders = jsonObject.getString("totalfolder");
                    String totalfile = jsonObject.getString("totalfile");
                    String totalsize = jsonObject.getString("size");
                    String currentstoragefiles = jsonObject.getString("currentstoragefile");
                    isProtected = jsonObject.getString("is_protected");
                    String lockedBy = jsonObject.getString("locked_by_userid");


                    if (isProtected.equals("0")) {

                        ivLockFolder.setVisibility(View.GONE);

                    } else {

                        ivLockFolder.setVisibility(View.VISIBLE);
                    }


                    final Foldername foldername = new Foldername();
                    foldername.setFoldername(currentstoragefiles);
                    foldername.setRootlockfolder(isProtected);
                    foldernames.add(foldername);


                    tvTotalFileSize.setText(totalsize);
                    tvTotalFiles.setText(totalfile);
                    tvTotalFolders.setText(totalfolders);

                    progressBar.setVisibility(View.GONE);
                    llDmsRoot.setVisibility(View.VISIBLE);


                    rvHierarcy.setAdapter(new FileViewAdapter(DmsActivity.this, foldernames, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, final String slid, String fullFolderName) {

                            //  llDmsRoot.removeAllViews();
                            counterAddButton = 1;
                            CardView cardView = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);
                            if (cardView != null) {
                                llDmsNavLeft.removeView(cardView);
                            } else {
                                Log.e("cardview", "null");
                            }


                            recursiveGetFolder(slid);
                            getMetadata(slid);

                            Foldername foldername = new Foldername();
                            foldername.setFoldername(fullFolderName);
                            horizontalist.add(foldername);

                            String folderName = fullFolderName.substring(0, fullFolderName.indexOf("&&"));
                            tvHeading.setText(folderName);
                            foldernameDyanamic = folderName;

                        }
                    }));

                    rvHierarcy.setItemViewCacheSize(foldernames.size());
                    toggleLockPasswordMenuItems(lockedBy, isProtected);

                   /* rvHierarcy.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.GONE);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getFolder(slid, userid);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameter = new HashMap<String, String>();
                Log.e("Map userid slid", userid + " " + slid_Session);
                parameter.put("userid", userid);
                parameter.put("slid", slid);//root slid
                parameter.put("la", session.getLanguage());

                return parameter;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);
        VolleySingelton.getInstance(DmsActivity.this).getRequestQueue().getCache().remove(STORAGE_URL);

    }

    private void recursiveGetFolder(final String slid) {

        dynamicFileSlid = slid;

        onclickfoldernames.clear();
        rvHierarcy.setVisibility(View.GONE);
        progressBar2.setVisibility(View.VISIBLE);
        llNoFileFound.setVisibility(View.GONE);

        //clearing the multimeta form
        //spinnerConditionlist.clear();
        // etRootMetadataText.setText("");

        resetMultiDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, STORAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("folderlist_recur", response);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");
                    JSONArray blankfolderArray = jsonObject.getJSONArray("blankFolder");
                    JSONArray folderLockArray = jsonObject.getJSONArray("lockFolder");

                    rootStorage1 = jsonObject.getString("storagename");
                    tvHeading.setText(rootStorage1);
                    foldernameDyanamic = rootStorage1;


                    for (int i = 0; i < jsonArray.length(); i++) {

                        String jsondataloop = jsonArray.getString(i);
                        String blankStorage = blankfolderArray.getString(i);
                        String lockFolderloop = folderLockArray.getString(i);
                        Foldername foldername = new Foldername();
                        foldername.setFoldername(jsondataloop);
                        foldername.setBlankfolder(blankStorage);
                        foldername.setLockfolder(lockFolderloop);

                        onclickfoldernames.add(foldername);

                    }

                    String totalfolders = jsonObject.getString("totalfolder");
                    String totalfile = jsonObject.getString("totalfile");
                    String totalsize = jsonObject.getString("size");
                    String currentstoragefiles = jsonObject.getString("currentstoragefile");
                    isProtected = jsonObject.getString("is_protected");
                    String lockedBy = jsonObject.getString("locked_by_userid");

                    toggleLockPasswordMenuItems(lockedBy, isProtected);

                    if (isProtected.equals("0")) {

                        ivLockFolder.setVisibility(View.GONE);

                    } else {

                        ivLockFolder.setVisibility(View.VISIBLE);
                    }

                    //   Log.e("totalfiles", totalfile);

                    if (totalfile.equals("0")) {

                        Log.e("total files found", "0");

                    } else {

                        Foldername foldername = new Foldername();
                        foldername.setFoldername(currentstoragefiles);
                        foldername.setRootlockfolder(isProtected);
                        onclickfoldernames.add(foldername);

                    }

                    if (totalfolders.equals("0") && totalfile.equals("0") && currentstoragefiles.equals("0")) {

                        progressBar2.setVisibility(View.GONE);

                        llNoFileFound.setVisibility(View.VISIBLE);
                        rvHierarcy.setVisibility(View.GONE);

                    } else {
                        progressBar2.setVisibility(View.GONE);

                        llNoFileFound.setVisibility(View.GONE);
                        rvHierarcy.setVisibility(View.VISIBLE);

                    }


                    tvTotalFileSize.setText(totalsize);
                    tvTotalFiles.setText(totalfile);
                    tvTotalFolders.setText(totalfolders);


                    //progressBar2.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //code here
                FileViewAdapter fileViewAdapter = new FileViewAdapter(DmsActivity.this, onclickfoldernames, new CustomItemClickListener() {

                    @Override
                    public void onItemClick(View v, int position, String slid, String fullFolderName) {

                        counterAddButton = 1;
                        CardView cardView = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);
                        if (cardView != null) {
                            llDmsNavLeft.removeView(cardView);
                        } else {
                            Log.e("cardview", "null");
                        }


                        onclickfoldernames.clear();

                        if (slid == slid_Session) {

                            horizontalist.clear();
                            String id = rootStorage1 + "&&" + slid_Session;
                            Foldername foldername1 = new Foldername();
                            foldername1.setFoldername(id);
                            horizontalist.add(foldername1);
                            fileViewHorizontalAdapter.notifyDataSetChanged();


                        } else {

                            recursiveGetFolder(slid);
                            getMetadata(slid);
                            Foldername foldername = new Foldername();
                            foldername.setFoldername(fullFolderName);
                            horizontalist.add(foldername);
                            fileViewHorizontalAdapter.notifyDataSetChanged();


                        }
                    }
                });

                rvHierarcy.setAdapter(fileViewAdapter);
                rvHierarcy.setItemViewCacheSize(onclickfoldernames.size());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                recursiveGetFolder(slid);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("slid", slid);
                params.put("ln", session.getLanguage());

                return params;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);
        VolleySingelton.getInstance(DmsActivity.this).getRequestQueue().getCache().remove(STORAGE_URL);
    }

    //getting the list of metadata in dropdown of each indiviual folder dynamically
    private void getMetadata(final String slid) {

        spinnerMetaList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("metadata", response);
                try {

                    jsonArrayMetadata = new JSONArray(response);

                    if (jsonArrayMetadata.length() == 0 || jsonArrayMetadata == null) {

                        tvNoMetaData.setVisibility(View.VISIBLE);
                        cvMetaFormStatic.setVisibility(View.GONE);
                        cvBtnMetadata.setVisibility(View.GONE);
                        ivMetaFolder.setVisibility(View.INVISIBLE);

                        if (rowView != null) {

                            if (rowView.getVisibility() == View.VISIBLE) {

                                LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
                                linearLayout.removeView(rowView);

                            }

                        } else {

                            Log.e("dynamicmetaForm", "null");

                        }


                    } else {


                        cvMetaFormStatic.setVisibility(View.VISIBLE);
                        tvNoMetaData.setVisibility(View.GONE);
                        cvBtnMetadata.setVisibility(View.VISIBLE);
                        ivMetaFolder.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArrayMetadata.length(); i++) {

                            String array = jsonArrayMetadata.get(i).toString();
                            spinnerMetaList.add(array);
                            dynamicSpinnerAdapter.notifyDataSetChanged();

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinnerMetaList.add("FileName");
                spinnerMetaList.add("No Of Pages");
                spinnerMetaList.add("Select Metadata");
                dynamicSpinnerAdapter.notifyDataSetChanged();
                dynamicSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                staticSpinnerMetaData.setAdapter(dynamicSpinnerAdapter);
                staticSpinnerMetaData.setSelection(dynamicSpinnerAdapter.getCount());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getMetadata(slid);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("slid", slid);

                return params;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);
    }

    private void addNewChild(final String parentSlid, final String foldername) {


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CREATE_NEW_CHILD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create new child", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                        tv_error_heading.setText(R.string.success);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialogSuccess.dismiss();
                                alertDialog.dismiss();
                                recursiveGetFolder(dynamicFileSlid);


                            }
                        });

                        dialogBuilder.setView(dialogView);
                        alertDialogSuccess = dialogBuilder.create();
                        alertDialogSuccess.setCancelable(false);
                        alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialogSuccess.show();


                    }

                    if (error.equals("true")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText(R.string.error);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


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

                Map<String, String> params = new HashMap<String, String>();
                params.put("add_child", parentSlid);
                params.put("create_child", foldername);
                params.put("name", session_userName);
                params.put("ip", ip);
                params.put("userid", userid);
                params.put("ln", session.getLanguage());

                Util.printParams(params,"child_params");

                return params;

            }


        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }

    private void modifyStorage(final String parentSlid, final String foldername) {


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MODIFY_STORAGE_NAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("modify Storage", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                        tv_error_heading.setText(R.string.success);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialogSuccess.dismiss();
                                alertDialog.dismiss();
                                recursiveGetFolder(dynamicFileSlid);


                            }
                        });

                        dialogBuilder.setView(dialogView);
                        alertDialogSuccess = dialogBuilder.create();
                        alertDialogSuccess.setCancelable(false);
                        alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialogSuccess.show();


                    }

                    if (error.equals("true")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText(R.string.error);
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


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

                Map<String, String> params = new HashMap<String, String>();
                params.put("modi", parentSlid);
                params.put("modify_slname", foldername);
                params.put("name", session_userName);
                params.put("ip",ip);
                params.put("userid", userid);
                params.put("ln", session.getLanguage());

                return params;

            }


        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }

    private void getUserRole(final String userid) {


        progressBar.setVisibility(View.VISIBLE);
        llDmsRoot.setVisibility(View.GONE);
        foldernames.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    //String userrole = jsonObject.getString("user_role");
                    String userroleid = jsonObject.getString("role_id");
                    String assign_metadata = jsonObject.getString("assign_metadata");
                    String create_storage = jsonObject.getString("create_storage");

                    String move_storage_level = jsonObject.getString("move_storage_level");
                    String copy_storage_level = jsonObject.getString("copy_storage_level");

                    String upload_doc_storage = jsonObject.getString("upload_doc_storage");
                    String metadata_quick_search = jsonObject.getString("metadata_quick_search");
                    String lock_folder = jsonObject.getString("lock_folder");
                    String shareFolder = jsonObject.getString("share_folder");

                    lockfolder = lock_folder;


//                    if (lock_folder.equalsIgnoreCase("1")) {
//
//                        toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(true);
//
//                    } else {
//
//                        toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(false);
//
//                    }


                    if (metadata_quick_search.equalsIgnoreCase("1")) {

                        toolbar.getMenu().findItem(R.id.action_dms_search).setVisible(true);

                    } else {

                        toolbar.getMenu().findItem(R.id.action_dms_search).setVisible(false);

                    }

                    if (shareFolder.equalsIgnoreCase("1")) {

                        toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(true);

                    } else {

                        toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(false);

                    }

                    if (move_storage_level.equalsIgnoreCase("0")) {

                        move_storage = "0";

                    } else {

                        move_storage = "1";
                    }

                    if (copy_storage_level.equalsIgnoreCase("0")) {

                        copy_storage = "0";

                    } else {

                        copy_storage = "1";
                    }


                    if (upload_doc_storage.equalsIgnoreCase("0")) {

                        toolbar.getMenu().findItem(R.id.action_upload_documents).setVisible(false);
                    } else {

                        toolbar.getMenu().findItem(R.id.action_upload_documents).setVisible(true);
                    }


                    if (assign_metadata.equalsIgnoreCase("0")) {

                        toolbar.getMenu().findItem(R.id.action_assign_metadata).setVisible(false);
                    } else {

                        toolbar.getMenu().findItem(R.id.action_assign_metadata).setVisible(true);
                    }

                    if (create_storage.equalsIgnoreCase("0")) {

                        toolbar.getMenu().findItem(R.id.action_create_new_child).setVisible(false);
                    } else {

                        toolbar.getMenu().findItem(R.id.action_create_new_child).setVisible(true);
                    }


                    roleid = userroleid;

                    //getfoldernames and attached slid
                    getFolder(slid_Session, userid);//here
                    //getting metadata
                    getMetadata(slid_Session);


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

                params.put("userid", userid);
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");
                params.put("ln", session.getLanguage());

                return params;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }

    private void assignedMetaData(final String slid) {

        metaDataViewBottomsheetList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ASSIGN_METADATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("assignmetadata", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        String metaname = jsonArray.getJSONObject(i).getString("field_name");
                        String metaid = jsonArray.getJSONObject(i).getString("id");

                        metaDataViewBottomsheetList.add(new MetaBottomSheet(metaname));


                    }

                    if (rvBottomSheetMetaview != null) {

                        rvBottomSheetMetaview.setAdapter(new MetaViewBottomSheetAdapter(metaDataViewBottomsheetList, DmsActivity.this));
                        bottomSheetDialog.show();

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
                params.put("slid", slid);
                params.put("ln", session.getLanguage());

                return params;
            }
        };


        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }

    private void LockFolder(String pwd, String folderslid) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.locking_folder));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOCK_FOLDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Folder_lock", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");


                    if (error.equalsIgnoreCase("false")) {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        recursiveGetFolder(folderslid);

                    } else if (error.equalsIgnoreCase("true")) {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                progressDialog.dismiss();
                Toast.makeText(DmsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();


            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "lockFolder");
                params.put("lockfolderPwd", pwd);
                params.put("lockfolderSlid", folderslid);
                params.put("userid", userid);
                params.put("ln", session.getLanguage());
                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void UnLockFolder(String pwd, String folderslid) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.unlocking_folder));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOCK_FOLDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("UnLock_Folder", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");


                    if (error.equalsIgnoreCase("false")) {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        recursiveGetFolder(folderslid);

                    } else if (error.equalsIgnoreCase("true")) {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(DmsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                progressDialog.dismiss();
                Toast.makeText(DmsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();


            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "unLockFolder");
                params.put("unlockfolderPwd", pwd);
                params.put("unlockfolderSlid", folderslid);
                params.put("userid", userid);
                params.put("ln", session.getLanguage());

                Util.printParams(params,"unlock");

                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void forgotPasswordLockUnlock() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.forgot_pwd));
        progressDialog.setMessage(getString(R.string.sending_otp_on_mail));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOCK_FOLDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("forgot_pass_res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Log.e("otp", jsonObject.getString("otp"));

                        alertDialog.dismiss();

                        resetPassWordDialog(DmsActivity.this, Util.base64Decode(jsonObject.getString("otp")));

                    } else if (error.equalsIgnoreCase("true")) {

                        alertDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();

                    } else {
                        alertDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                alertDialog.dismiss();
                progressDialog.dismiss();
                Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();


            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "forgotPassword");
                params.put("userid", userid);
                params.put("ln", session.getLanguage());
                return params;
            }


        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void resetPassword(String password) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.reseting_pwd));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOCK_FOLDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("reset_pass_res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        alertDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();


                    } else if (error.equalsIgnoreCase("true")) {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, msg, Toast.LENGTH_SHORT).show();

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    progressDialog.dismiss();
                    Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(DmsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();


            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "resetPassword");
                params.put("slid", dynamicFileSlid);
                params.put("fname", foldernameDyanamic);
                params.put("pwd", password);
                params.put("userid", userid);
                params.put("toEmail", session_userEmail);
                params.put("ln", session.getLanguage());

                Util.printParams(params, "reset_pwd_params");

                return params;
            }


        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getGroupMemberList(final String userid) {

        userList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response in getmembrs", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String full = jsonArray.getString(i);
                            String username = full.substring(0, full.indexOf("&"));
                            String userid = full.substring(full.indexOf("&") + 2);

                            User user = new User();
                            user.setUsername(username);
                            user.setUserid(userid);
                            user.setDrawableId(R.drawable.ic_person_blue_20dp);
                            userList.add(user);

                            Log.e("group_member", "username-->" + username + "\n" + "userid-->" + userid);

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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);
                parameters.put("la", session.getLanguage());
                return parameters;
            }


        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }

    private void shareFolder(String userId, String shareUserids, String dynamicFileSlid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FOLDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("share_folder_res", response);
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    List<SharedFileStatus> sharedFileStatusList = new ArrayList<>();


                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String msg = jsonObject.getString("msg");
                            String error = jsonObject.getString("error");
                            String username = jsonObject.getString("username");


                            if (error.equals("null") || error.equals("true")) {


                                String status = getString(R.string.folder_shared_failed_with_username) + " " + username;

                                if(session.getLanguage().equalsIgnoreCase("hi")){

                                    status = username +" "+ getString(R.string.folder_shared_failed_with_username);
                                }

                                sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_close_black_24dp));

                            } else if (error.equals("false")) {

                                String status = getString(R.string.folder_shared_successfully_with_username) + " " + username;


                                if(session.getLanguage().equalsIgnoreCase("hi")){

                                    status = username + getString(R.string.folder_shared_successfully_with_username);

                                }

                                sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_check_green_24dp));

                            }

                        }


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);


                        TextView tvHeading = dialogView.findViewById(R.id.alert_shared_multi_banner);
                        tvHeading.setText(R.string.shared_folder_status);

                        RecyclerView rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                        LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(DmsActivity.this);
                        rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);

                        SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, DmsActivity.this);
                        rvSharedFileStatus.setAdapter(sharedFileStatusAdapter);


                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_no_sharedstatus_popup);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


                    } else {

                        Toast.makeText(DmsActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(DmsActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DmsActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userId);
                parameters.put("slid", dynamicFileSlid);
                parameters.put("action", "shareFolder");
                parameters.put("shareUserids", shareUserids);
                parameters.put("ip",session.getIP());
                parameters.put("la", session.getLanguage());
                return parameters;

            }
        };


        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }


    /* * **********************************************************
     *              Helper Methods                                *
     * *********************************************************** */

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private String setRoles() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("role_id").append(",")
                .append("assign_metadata").append(",")
                .append("create_storage").append(",")
                .append("move_storage_level").append(",")
                .append("copy_storage_level").append(",")
                .append("upload_doc_storage").append(",")
                .append("lock_folder").append(",")
                .append("share_folder").append(",")
                .append("metadata_quick_search");


        return stringBuilder.toString();
    }

    private void toggleLockPasswordMenuItems(String lockedBy, String isProtected) {

        Log.e("lock_folder_main", lockfolder);
        Log.e("lock_folder_by", lockedBy);
        Log.e("lock_folder_is_protectd", isProtected);
        Log.e("lockfolder_userid", session.getUserDetails().get(SessionManager.KEY_USERID));

        if (lockfolder.equals("1")) {

            //this means this is the user who have locked the folder
            if (lockedBy.equals(session.getUserDetails().get(SessionManager.KEY_USERID)) && isProtected.equals("2")) {

                Log.e("here", "1");
                toolbar.getMenu().findItem(R.id.action_forgot_pwd).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_unlock_folder).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(false);


            }


            else if (isProtected.equals("1") && lockedBy.equals(session.getUserDetails().get(SessionManager.KEY_USERID))) {

                Log.e("here", "2");
                toolbar.getMenu().findItem(R.id.action_forgot_pwd).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_unlock_folder).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(false);

            } else if (isProtected.equals("0") || isProtected.equalsIgnoreCase("null")) {
                Log.e("here", "3");
                toolbar.getMenu().findItem(R.id.action_forgot_pwd).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_unlock_folder).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(true);

            } else if((isProtected.equals("1") || isProtected.equals("2")) && !lockedBy.equals(session.getUserDetails().get(SessionManager.KEY_USERID))) {
                Log.e("here", "4");
//                toolbar.getMenu().findItem(R.id.action_forgot_pwd).setVisible(false);
//                toolbar.getMenu().findItem(R.id.action_unlock_folder).setVisible(false);
//                toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(true);

                toolbar.getMenu().findItem(R.id.action_forgot_pwd).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_unlock_folder).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(false);
            }

        } else {
            Log.e("here", "5");
            toolbar.getMenu().findItem(R.id.action_forgot_pwd).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_unlock_folder).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_lock_folder).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_share_folder).setVisible(false);
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showNoInternetLayout(isConnected);

    }

    void showNoInternetLayout(Boolean isconnected) {


        if (!isconnected) {

            llnointernetfound.setVisibility(View.VISIBLE);
            scrollViewmain.setVisibility(View.GONE);


        } else {

            llnointernetfound.setVisibility(View.GONE);
            scrollViewmain.setVisibility(View.VISIBLE);


        }

    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private JSONObject makeJsonObject(ArrayList<String> metaData, ArrayList<String> condition, ArrayList<String> text, int formcount) throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();

        for (int i = 0; i < formcount; i++) {
            obj = new JSONObject();
            try {
                obj.put("metadata", metaData.get(i));
                obj.put("cond", condition.get(i));
                obj.put("searchText", text.get(i));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("multiMetaSearch", jsonArray1);
        return finalobject;
    }


    /* * **********************************************************
     *              Add Form Dynamic Side Panel Methods           *
     * *********************************************************** */

    private void addMetaForm() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
        rowView = inflater.inflate(R.layout.dms_nav_left_meta_form, null);

        myLayouts.add(rowView);

        Spinner spinnerdynamicCondition = rowView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdynamicCondition.setAdapter(adapter);
        spinnerdynamicCondition.setSelection(adapter.getCount());


        Spinner spinnerdynamicMetadata = rowView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerMetaList) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdynamicMetadata.setAdapter(adapter1);
        spinnerdynamicMetadata.setSelection(adapter1.getCount());


        btnRemoveMetaForm = rowView.findViewById(R.id.iv_nav_left_del_btn);
        btnRemoveMetaForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Animation animation = AnimationUtils.loadAnimation(DmsActivity.this, android.R.anim.slide_out_right);
                animation.setDuration(500);
                rowView.startAnimation(animation);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (counterAddButton == 0) {

                            Log.e("counter addbutton zero", "true");
                            handler.removeCallbacksAndMessages(null);
                        } else {


                            counterAddButton -= 1;
                            llDmsNavLeft.removeViewAt(llDmsNavLeft.getChildCount() - 2);
                            myLayouts.remove(myLayouts.size() - 1);

                            //scrollViewnavleft.removeView(rowView);

                            Log.e("counteraddButton", String.valueOf(counterAddButton));

                            Animation animation = AnimationUtils.loadAnimation(DmsActivity.this, android.R.anim.slide_in_left);
                            animation.setDuration(500);
                            cvBtnMetadata.startAnimation(animation);
                            handler.removeCallbacksAndMessages(null);

                        }


                    }
                }, 500);


            }
        });

        Animation animation = AnimationUtils.loadAnimation(DmsActivity.this, android.R.anim.slide_in_left);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                llDmsNavLeft.addView(rowView, llDmsNavLeft.getChildCount() - 1);


            }
        }, 500);


    }

}
