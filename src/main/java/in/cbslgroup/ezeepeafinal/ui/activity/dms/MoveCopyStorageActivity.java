package in.cbslgroup.ezeepeafinal.ui.activity.dms;

import  android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.FileViewActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeepeafinal.adapters.list.FileViewHorizontalAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.MoveStorageListAdapter;
import in.cbslgroup.ezeepeafinal.adapters.list.SharedFileStatusAdapter;
import in.cbslgroup.ezeepeafinal.interfaces.CustomItemClickListener;
import in.cbslgroup.ezeepeafinal.interfaces.MoveStorageListListener;
import in.cbslgroup.ezeepeafinal.model.Foldername;
import in.cbslgroup.ezeepeafinal.model.MoveStorage;
import in.cbslgroup.ezeepeafinal.model.SharedFileStatus;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.SessionManager;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

import static in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity.slid_Session;

public class MoveCopyStorageActivity extends AppCompatActivity {

    JSONArray jsonArray;

    RecyclerView rvMoveStorage, rvHori;

    List<MoveStorage> moveStorageList = new ArrayList<>();
    ArrayList<Foldername> horilist = new ArrayList<>();

    TextView tvpreviousSlid, tvpreviousFname, tvFoldername;

    MoveStorageListAdapter moveStorageListAdapter;

    FileViewHorizontalAdapter fileViewHorizontalAdapter;

    LinearLayout llnodirectoryfound;

    ProgressBar progressBar;

    Toolbar toolbar;

    String previousslid, storagename, slidStr, mode, docids, viewer, rootMoveFoldername, rootMoveFolderSlid;

    AlertDialog alertDialog;

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
        setContentView(R.layout.activity_move_storage);

        initLocale();
        initSessionManager();

        toolbar = findViewById(R.id.toolbar_storage_management_movestorage);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(view -> {

            onBackPressed();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        });


        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        docids = intent.getStringExtra("docids");
        viewer = intent.getStringExtra("viewer");


        String foldernameNslid = intent.getStringExtra("foldername&&slid");
        Log.e("foldernameprevious", foldernameNslid);


        rootMoveFoldername = foldernameNslid.substring(0, foldernameNslid.indexOf("&&"));
        rootMoveFolderSlid = foldernameNslid.substring(foldernameNslid.indexOf("&&") + 2, foldernameNslid.length());


        Log.e("rootmovename", rootMoveFoldername + "___" + rootMoveFolderSlid);


        toolbar.setSubtitle(rootMoveFoldername);

        rvMoveStorage = findViewById(R.id.rv_move_storage);
        rvMoveStorage.setLayoutManager(new LinearLayoutManager(this));
        rvMoveStorage.setItemViewCacheSize(moveStorageList.size());
        rvMoveStorage.setHasFixedSize(true);

        rvHori = findViewById(R.id.rv_move_storage_hori);
        rvHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHori.setItemViewCacheSize(moveStorageList.size());
        rvHori.setHasFixedSize(true);


        previousslid = MainActivity.slid_session;

        tvpreviousFname = findViewById(R.id.tv_move_storage_fname_previous);
        tvpreviousSlid = findViewById(R.id.tv_move_storage_slid_previous);
        tvFoldername = findViewById(R.id.tv_move_storage_foldername);

        llnodirectoryfound = findViewById(R.id.ll_movestorage_nofilefound);
        progressBar = findViewById(R.id.progressBar_move_storage);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));


        getFoldername(MainActivity.slid_session);

    }

    private void initSessionManager() {

        sessionManager = new SessionManager(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.move_storage_menu, menu);

        if (mode.equals("copy")) {

            toolbar.setTitle(R.string.copy_storage);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(false);


        } else if (mode.equals("move")) {

            toolbar.setTitle(R.string.move_storage);

            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(false);

        } else if (mode.equals("movefiles")) {

            toolbar.setTitle(R.string.move_storage_files);

            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(false);


        } else if (mode.equals("copyfiles")) {

            toolbar.setTitle(R.string.copy_storage_files);


            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(false);


        }


        return true;
    }

    @Override
    public void onBackPressed() {


        // getFoldername(slidlist.get(slidlist.size()-1));
        //slidlist.remove(slidlist.size()-1);

        super.onBackPressed();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_move_storage_copyhere) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

            AlertDialog alertDialog = new AlertDialog.Builder(MoveCopyStorageActivity.this).create();
            alertDialog.setTitle(getString(R.string.copy_storage));
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_copy) + rootMoveFoldername + " " + getString(R.string.to) + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.copy_here),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

                            copyStorage(dSlid, rootMoveFolderSlid, rootMoveFoldername, MainActivity.ip, MainActivity.username, MainActivity.userid);

                            dialog.dismiss();
                        }
                    });

            alertDialog.show();

            return true;
        }


        if (id == R.id.action_move_storage_movehere) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());


            AlertDialog alertDialog = new AlertDialog.Builder(MoveCopyStorageActivity.this).create();
            alertDialog.setTitle(getString(R.string.move_storage));
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_move) + rootMoveFoldername + " " + getString(R.string.to) + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.move_here),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

                            moveStorage(rootMoveFolderSlid, dSlid, MainActivity.ip, MainActivity.username, MainActivity.userid);

                            dialog.dismiss();
                        }
                    });

            alertDialog.show();

            return true;
        }


        if (id == R.id.action_move_storage_movefiles) {


            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

            AlertDialog alertDialog = new AlertDialog.Builder(MoveCopyStorageActivity.this).create();
            alertDialog.setTitle(getString(R.string.move_storage_files));
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage(getString(R.string.Are_you_sure_you_want_to_move_files_from) +" "+ rootMoveFoldername + " " + getString(R.string.to) + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.move_files_here),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

                            String doclistArray = FileViewActivity.doclistArray;

                            //multiMove("Ankit Roy ","54","100.89.117.173","329","329","899","[19824]");

                            if (viewer.equalsIgnoreCase("file")) {

                                multiMove(MainActivity.username, MainActivity.userid, MainActivity.ip, slid_Session, rootMoveFolderSlid, dSlid, FileViewActivity.doclistArray);

                            }

                            else if (viewer.equalsIgnoreCase("meta")) {


                                multiMove(MainActivity.username, MainActivity.userid, MainActivity.ip, slid_Session, rootMoveFolderSlid, dSlid, MetaSearchFileViewActivity.doclistArrayMeta);


                            }


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();


            return true;
        }


        if (id == R.id.action_move_storage_copyfiles) {

            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

            AlertDialog alertDialog = new AlertDialog.Builder(MoveCopyStorageActivity.this).create();
            alertDialog.setTitle(getString(R.string.copy_storage_files));
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_copy_files_from)+" "+ rootMoveFoldername + " " + getString(R.string.to) + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,  getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.copy_files_here),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

                            String doclistArray = FileViewActivity.doclistArray;

                            //multiMove("Ankit Roy ","54","100.89.117.173","329","329","899","[19824]");

                            if (viewer.equalsIgnoreCase("file")) {

                                multiCopy(MainActivity.username, MainActivity.userid, MainActivity.ip, slid_Session, rootMoveFolderSlid, dSlid, FileViewActivity.doclistArray);
                            } else if (viewer.equalsIgnoreCase("meta")) {

                                multiCopy(MainActivity.username, MainActivity.userid, MainActivity.ip, slid_Session, rootMoveFolderSlid, dSlid, MetaSearchFileViewActivity.doclistArrayMeta);

                            }


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();


            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    void getFoldername(final String slid) {

        //spinnerlist.clear();

        moveStorageList.clear();
        //horilist.subList(1,horilist.size()-1).clear();
        //horilist.clear();


        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMoveStorage.setVisibility(View.GONE);


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("getfolname", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String str = jsonObject.getString("storagename");

                    storagename = str.substring(0, str.indexOf("&&"));
                    slidStr = str.substring(str.indexOf("&&") + 2, str.length());


                    //329

                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");

                    if (jsonArray.length() == 0) {

                        llnodirectoryfound.setVisibility(View.VISIBLE);
                        rvMoveStorage.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);


                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2, fname.length());

                            if (!slid.equalsIgnoreCase(rootMoveFolderSlid)) {

                                moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));
                            }


                            // spinnerlist.add(fname);

                        }

                        String id = storagename + "&&" + slid_Session;
                        Foldername foldername1 = new Foldername();
                        foldername1.setFoldername(id);
                        horilist.add(foldername1);

                        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horilist, MoveCopyStorageActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String slid, String fullFolderName) {


                                if (slid == slid_Session) {

                                    horilist.clear();
                                    String id = storagename + "&&" + slid_Session;
                                    Foldername foldername1 = new Foldername();
                                    foldername1.setFoldername(id);
                                    horilist.add(foldername1);


                                    fileViewHorizontalAdapter.notifyDataSetChanged();


                                } else {

                                    horilist.subList(position + 1, horilist.size()).clear();
                                    fileViewHorizontalAdapter.notifyDataSetChanged();

                                    getChildFoldername(slid);

                                }


                            }
                        });

                        rvHori.setAdapter(fileViewHorizontalAdapter);


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, MoveCopyStorageActivity.this, new MoveStorageListListener() {
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
                        rvMoveStorage.setAdapter(moveStorageListAdapter);
                        moveStorageListAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        rvMoveStorage.setVisibility(View.VISIBLE);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


              /*  spinnerlist.add("Select Storage");
                spinnerStaticAdapter.notifyDataSetChanged();

                spinnerStaticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerStorageLevel.setAdapter(spinnerStaticAdapter);
                spinnerStorageLevel.setSelected(false);

                spinnerStorageLevel.setSelection(0,true);


                final AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selected = parent.getItemAtPosition(position).toString();
                        String slid = selected.substring(selected.indexOf("&&")+2,selected.length());

                        Log.e("slid",slid);

                        if(slid.matches("[0-9]+")){


                            llInflation.removeViews(3,myLayouts.size());
                            myLayouts.subList(0,myLayouts.size()).clear();

                            Toast.makeText(MoveCopyStorageActivity.this, slid, Toast.LENGTH_SHORT).show();

                            getChildFoldername(slid);
                            //addSpinner();

                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };

               // spinnerStorageLevel.setSelection(0,true);


                spinnerStorageLevel.setSelection(spinnerStaticAdapter.getCount());
                spinnerStorageLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        spinnerStorageLevel.setOnItemSelectedListener(listener);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {



                    }
                });
*/

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
                params.put("ln",sessionManager.getLanguage());
                return params;
            }
        };


        VolleySingelton.getInstance(MoveCopyStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void getChildFoldername(final String slid) {

        // spinnerchildlist.clear();

        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMoveStorage.setVisibility(View.GONE);


        moveStorageList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getchildfoldernames", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("foldername");


                    if (jsonArray.length() == 0) {

                        progressBar.setVisibility(View.GONE);
                        rvMoveStorage.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.VISIBLE);


                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2, fname.length());

                            if (!slid.equalsIgnoreCase(rootMoveFolderSlid)) {

                                moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));
                            }



                         /*   arrayListchild.add(fname);


                            mySpinnerChildlist.add(arrayListchild);
*/
                        }


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, MoveCopyStorageActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {

                                //tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);
                                fileViewHorizontalAdapter.notifyDataSetChanged();

                                getChildFoldername(slid);
                            }
                        });

                        rvMoveStorage.setAdapter(moveStorageListAdapter);


                        moveStorageListAdapter.notifyDataSetChanged();


                        progressBar.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.GONE);
                        rvMoveStorage.setVisibility(View.VISIBLE);


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
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("slid", slid);
                params.put("ln",sessionManager.getLanguage());
                return params;
            }
        };

        VolleySingelton.getInstance(MoveCopyStorageActivity.this).addToRequestQueue(stringRequest);


    }

    //TODO
    //have to mak move storage api and function

    void moveStorage(final String rootMoveFolderSlid, final String MoveFolderDestinationSlid, final String ip, final String username, final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_COPY_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("movestorage", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {

                        Toast.makeText(MoveCopyStorageActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else if (error.equals("false")) {

                        Toast.makeText(MoveCopyStorageActivity.this, message, Toast.LENGTH_SHORT).show();
                       /*Intent intent = new Intent(MoveCopyStorageActivity.this,DmsActivity.class);
                       startActivity(intent);
                       overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                        String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                        String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                        final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2, destinationfoldername.length());

                        getChildFoldername(dSlid);


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
                params.put("rootFolderSlid", rootMoveFolderSlid);
                params.put("destinationFolderSlid", MoveFolderDestinationSlid);
                params.put("moveIp", ip);
                params.put("moveUserId", userid);
                params.put("moveUsername", username);
                params.put("ln",sessionManager.getLanguage());

                return params;
            }
        };

        VolleySingelton.getInstance(MoveCopyStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void copyStorage(final String rootMoveFolderSlid, final String CopyFolderDestinationSlid, final String CopyFolderDestinationFoldername, final String ip, final String username, final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_COPY_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("copyStorage", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {

                        Toast.makeText(MoveCopyStorageActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else if (error.equals("false")) {

                        Toast.makeText(MoveCopyStorageActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MoveCopyStorageActivity.this, DmsActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

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
                params.put("copyrootFolderSlid", rootMoveFolderSlid);
                params.put("copydestinationFolderSlid", CopyFolderDestinationSlid);
                params.put("copyIp", ip);
                params.put("copyUserId", userid);
                params.put("copyUsername", username);
                params.put("copydestinationFoldername", CopyFolderDestinationFoldername);
                params.put("ln",sessionManager.getLanguage());

                JSONObject js = new JSONObject(params);
                Log.e("copystorage js", js.toString());


                return params;
            }
        };

        VolleySingelton.getInstance(MoveCopyStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void multiMove(final String username, final String userid, final String ip, final String movetoParentid, final String moveFromSlid, final String movetoSlid, final String docids) {

        // Log.e("multimoveValues",username+" "+userid+" "+ip+" "+movetoParentid+" "+moveFromSlid+" "+movetoSlid+" "+docids);

        Log.e("-", "------ move multiple -----");
        Log.e("username", username);
        Log.e("userid", userid);
        Log.e("ip", ip);
        Log.e("movetoParentid", movetoParentid);
        Log.e("moveFromSlid", moveFromSlid);
        Log.e("movetoSlid", movetoSlid);
        Log.e("docids", docids);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("multimove", response);
                try {

                    List<SharedFileStatus> sharedFileStatusList = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String error = jsonArray.getJSONObject(i).getString("error");
                        String msg = jsonArray.getJSONObject(i).getString("msg");


                        if (error.equals("null") || error.equals("true")) {

                            sharedFileStatusList.add(new SharedFileStatus(msg, R.drawable.ic_close_black_24dp));


                        } else if (error.equals("false")) {


                            sharedFileStatusList.add(new SharedFileStatus(msg, R.drawable.ic_check_green_24dp));

                        }

                    }


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MoveCopyStorageActivity.this);

                    LayoutInflater inflater = MoveCopyStorageActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);

                    RecyclerView rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                    LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(MoveCopyStorageActivity.this);
                    rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);
                    rvSharedFileStatus.setHasFixedSize(true);
                    rvSharedFileStatus.setItemViewCacheSize(sharedFileStatusList.size());

                    SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, MoveCopyStorageActivity.this);
                    rvSharedFileStatus.setAdapter(sharedFileStatusAdapter);

                    TextView tvBanner = dialogView.findViewById(R.id.alert_shared_multi_banner);
                    tvBanner.setText(R.string.moved_file_status);

                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_no_sharedstatus_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                            //onBackPressed();
                            Intent intent = new Intent();
                            intent.putExtra("slid", DmsActivity.dynamicFileSlid);
                            setResult(1003, intent);
                            finish();//finishing activity

                        }
                    });

                    dialogBuilder.setView(dialogView);

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();


                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(MoveCopyStorageActivity.this, R.string.error_moving_files, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MoveCopyStorageActivity.this,  R.string.error_moving_files, Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("mulMove_ip", ip);
                params.put("mulMove_username", username);
                params.put("mulMove_userid", userid);
                params.put("mulMove_sl_id_move_multi", moveFromSlid);
                params.put("mulMove_lastMoveId", movetoSlid);
                params.put("mulMove_moveToParentId", movetoParentid);
                params.put("mulMove_doc_id_smove_multi", docids);//crct
                params.put("ln",sessionManager.getLanguage());
                return params;
            }


        };


        VolleySingelton.getInstance(MoveCopyStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void multiCopy(final String username, final String userid, final String ip, final String copytoParentid, final String copyFromSlid, final String copytoSlid, final String docids) {

        Log.e("-", "------copy multiple-----");
        Log.e("username", username);
        Log.e("userid", userid);
        Log.e("ip", ip);
        Log.e("copytoParentid", copytoParentid);
        Log.e("copyFromSlid", copyFromSlid);
        Log.e("copytoSlid", copytoSlid);
        Log.e("docids", docids);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("multicopy", response);


                try {

//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String error = jsonObject.getString("error");
//                    String msg = jsonObject.getString("message");
//
//                    if (error.equalsIgnoreCase("true")) {
//
//                        Toast.makeText(MoveCopyStorageActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        onBackPressed();
//                    } else if (error.equalsIgnoreCase("false")) {
//
//                        Toast.makeText(MoveCopyStorageActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        onBackPressed();
//
//                    }


                    List<SharedFileStatus> sharedFileStatusList = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String error = jsonArray.getJSONObject(i).getString("error");
                        String msg = jsonArray.getJSONObject(i).getString("msg");


                        if (error.equals("null") || error.equals("true")) {

                            sharedFileStatusList.add(new SharedFileStatus(msg, R.drawable.ic_close_black_24dp));


                        } else if (error.equals("false")) {


                            sharedFileStatusList.add(new SharedFileStatus(msg, R.drawable.ic_check_green_24dp));

                        }

                    }


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MoveCopyStorageActivity.this);

                    LayoutInflater inflater = MoveCopyStorageActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);

                    RecyclerView rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                    LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(MoveCopyStorageActivity.this);
                    rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);
                    rvSharedFileStatus.setHasFixedSize(true);
                    rvSharedFileStatus.setItemViewCacheSize(sharedFileStatusList.size());

                    SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, MoveCopyStorageActivity.this);
                    rvSharedFileStatus.setAdapter(sharedFileStatusAdapter);

                    TextView tvBanner = dialogView.findViewById(R.id.alert_shared_multi_banner);
                    tvBanner.setText(R.string.copy_file_status);

                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_no_sharedstatus_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                            onBackPressed();
//                            Intent intent = new Intent();
//                            intent.putExtra("slid", DmsActivity.dynamicFileSlid);
//                            setResult(1004, intent);
//                            finish();//finishing activity

                        }
                    });

                    dialogBuilder.setView(dialogView);

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();


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
                params.put("mulCopy_ip", ip);
                params.put("mulCopy_username", username);
                params.put("mulCopy_fromSlid", copyFromSlid);
                params.put("mulCopy_toSlid", copytoSlid);
                params.put("mulCopy_userid", userid);
                params.put("mulCopy_copyToParentId", copytoParentid);
                params.put("mulCopy_doc_ids", docids);
                params.put("ln",sessionManager.getLanguage());

                Util.printParams(params, "multicopy");
                return params;
            }


        };


        VolleySingelton.getInstance(MoveCopyStorageActivity.this).addToRequestQueue(stringRequest);


    }


}
