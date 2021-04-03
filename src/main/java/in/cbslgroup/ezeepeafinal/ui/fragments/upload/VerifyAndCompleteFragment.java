package in.cbslgroup.ezeepeafinal.ui.fragments.upload;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lowagie.text.pdf.PdfReader;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.UploadActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.ConnectivityReceiver;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;
import in.cbslgroup.ezeepeafinal.utils.NotificationActions;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

import static android.app.Activity.RESULT_OK;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyAndCompleteFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    public static String pagecount;
    LinearLayout llVerifyComplete;
    String filePath = UploadActivity.filePath;
    AlertDialog alertDialog;

    Button btnBack, btnNxt, btnupload, btnUploadinWorkflow;


    CheckBox checkBox;

    JSONObject jsonObject;
    TextView tvFileSize, tvFileFormat, tvFilename, tvPageCount;
    ArrayList<String> metaenteredlist = new ArrayList<>();
    ArrayList<String> metalabellist = new ArrayList<>();
    String uploadingPercent, uploadingTotalSize;
    Long uploadedBytes;
    Context mContext;

    ProgressDialog pd;

    // declare a variable activity in your fragment
    MultipartUploadRequest multipartUploadRequest;
    private List<View> myLayouts = new ArrayList<>();




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(LocaleHelper.onAttach(context, "en"));
    }

    public VerifyAndCompleteFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_verify_and_complete, container, false);
        llVerifyComplete = v.findViewById(R.id.ll_frag_verify_complete_dynamicview);

       /* SharedPreferences prefs = getActivity().getSharedPreferences("jsonobject", MODE_PRIVATE);

        String meta_label= prefs.getString("meta_label",null);
        String meta_entered = prefs.getString("meta_entered",null);*/

        tvPageCount = v.findViewById(R.id.tv_frag_verify_complete_pagecount);
        tvFileFormat = v.findViewById(R.id.tv_frag_verify_complete_fileformat);
        tvFilename = v.findViewById(R.id.tv_frag_verify_complete_filename);
        tvFileSize = v.findViewById(R.id.tv_frag_verify_complete_filesize);
        btnNxt = v.findViewById(R.id.next);
        btnBack = v.findViewById(R.id.back);
        checkBox = v.findViewById(R.id.upload_frag_verify_checkbox);
        btnupload = v.findViewById(R.id.btn_upload_file_storage);
        btnUploadinWorkflow = v.findViewById(R.id.btn_upload_file_workflow);


        btnNxt.setVisibility(View.INVISIBLE);


        PdfReader reader;
        try {
            if (UploadActivity.filetypedynamic.equals("pdf") || UploadActivity.filetypedynamic.equals("PDF")) {
                reader = new PdfReader(filePath);
                int noOfPages = reader.getNumberOfPages();
                String pages = String.valueOf(noOfPages);
                tvPageCount.setText(pages);

                pagecount = pages;

            } else {

                tvPageCount.setText("1");
                pagecount = "1";
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        Long filesize = UploadActivity.filesize;
        String fileSize = String.valueOf(filesize);


        tvFilename.setText(UploadActivity.fileName);
        tvFileSize.setText(fileSize);
        tvFileFormat.setText(UploadActivity.filetypedynamic);

        UploadActivity.stepView.done(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadActivity.stepView.done(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, new UploadFileFragment());
                ft.commit();

                UploadActivity.stepView.go(1, true);

            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    btnupload.setEnabled(true);
                    btnUploadinWorkflow.setEnabled(true);

                } else {

                    btnupload.setEnabled(false);
                    btnUploadinWorkflow.setEnabled(false);

                }

            }
        });


        pd = new ProgressDialog(getActivity());
        pd.setTitle(getString(R.string.file_uploading));
        pd.setMessage(getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.setButton(getString(R.string.run_in_background), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = getActivity().getIntent();
                intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();

            }
        });


        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                int count = DescribesFragment.metalabellist.size();


                jsonObject = null;

                try {

                    jsonObject = makeJsonObject(DescribesFragment.metalabellist, DescribesFragment.metaEnteredlist, count);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("json", String.valueOf(jsonObject));


                checkSameFileExist(tvFilename.getText().toString(),DmsActivity.dynamicFileSlid);




            }
        });

        btnUploadinWorkflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UploadActivity.stepView.done(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, new UploadInWorkflowFragment());
                ft.commit();

                UploadActivity.stepView.go(3, true);


            }
        });

        Log.e("metalabel on verify", Arrays.toString(new ArrayList[]{DescribesFragment.metalabellist}));
        Log.e("metaname in verify", Arrays.toString(new ArrayList[]{DescribesFragment.metaEnteredlist}));

        for (int i = 0; i < DescribesFragment.metalabellist.size(); i++) {

            createDynamicView(DescribesFragment.metalabellist.get(i), DescribesFragment.metaEnteredlist.get(i));

        }


        // Inflate the layout for this fragment
        return v;
    }


    private void createDynamicView(String metaname, String metafilled) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.upload_verfiy_complete_dynamic_layout, null);

        myLayouts.add(rowView);

        TextView tvMetaName = rowView.findViewById(R.id.tv_frag_verify_complete_metadata_name);
        tvMetaName.setText(metaname);

        TextView tvMetaFilled = rowView.findViewById(R.id.tv_frag_verify_complete_metadata_entered);
        tvMetaFilled.setText(metafilled);

        llVerifyComplete.addView(rowView, llVerifyComplete.getChildCount() - 1);


    }

    private void checkSameFileExist(String filename,String slid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPLOAD_DOC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response_check",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);



                    String msg = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if(error.equalsIgnoreCase("false"))
                    {

                        uploadMultipart();

                    }

                    else if (error.equalsIgnoreCase("true")){

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                    }

                    else{

                        Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("file_name",filename);
                params.put("folder_slid",slid);

                Util.printParams(params,"checfilealraedy");
                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


    public void uploadMultipart() {
        //getting the actual path of the image
        String path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filePath;
        }

        if (path == null) {

            Toast.makeText(getActivity(), R.string.move_file_in_internal_storage, Toast.LENGTH_LONG).show();
        } else {
            //Uploading code


            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);
                final ProgressDialog progress = new ProgressDialog(getActivity());

                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                String ipadress = MainActivity.ip;
                String userId = MainActivity.userid;
                String pageCount = tvPageCount.getText().toString();
                String sliD = DmsActivity.dynamicFileSlid;
                String userName = MainActivity.username;
                String metadata = String.valueOf(jsonObject);

                Log.e("uploading ", ipadress + "/" + userId + "/" + pageCount + "/" + sliD + "/" + userName + "/" + metadata + "/" + path);


                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(getActivity(), uploadId, ApiUrl.UPLOAD_DOC_URL);
                multipartUploadRequest.addFileToUpload(path, "file");


                Log.e("Upload_rate", UPLOAD_RATE.toString());

          /*      uploadNotificationConfig.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                        + " at " + UPLOAD_RATE + " - " + PROGRESS;*/

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(getActivity(), 1, uploadId)));

                //uploadNotificationConfig.getProgress().iconResourceID = R.drawable.ic_upload;
                //uploadNotificationConfig.getProgress().iconColorResourceID = Color.BLUE;

                uploadNotificationConfig.getCompleted().message = getString(R.string.upload_complete_successfully_in)+" "+ ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;

                //converting the drawable to bitmap
                // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_upload_blue_dark_24dp);
                //uploadNotificationConfig.getCompleted().largeIcon = bm;
                //uploadNotificationConfig.getCompleted().iconColorResourceID = Color.GREEN;

                uploadNotificationConfig.getError().message = getString(R.string.error_while_uploading);
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                // uploadNotificationConfig.getError().iconColorResourceID = Color.RED;

                uploadNotificationConfig.getCancelled().message = getString(R.string.upload_has_been_cancelled);
                uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;
                // uploadNotificationConfig.getCancelled().iconColorResourceID = Color.YELLOW;


                //Adding file
                multipartUploadRequest

                        .addParameter("metadata", metadata)
                        .addParameter("slid", sliD)
                        .addParameter("pagecount", pageCount)
                        .addParameter("username", userName)
                        .addParameter("userid", userId)
                        .addParameter("ip", ipadress)

                        .setNotificationConfig(

                                uploadNotificationConfig
                                        .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                        .setClearOnActionForAllStatuses(true)
                        )

                        .setMaxRetries(0)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {


                                uploadingPercent = String.valueOf(uploadInfo.getProgressPercent());
                                uploadingTotalSize = String.valueOf(uploadInfo.getTotalBytes() / 1024);

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(getActivity(), R.string.upload_error, Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse));
                                Log.e("Server response error", String.valueOf(exception));


                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                //progress.dismiss();

                                String response = serverResponse.getBodyAsString();

                                Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("message");
                                    String error = jsonObject.getString("error");


                                    if (error.equalsIgnoreCase("false")) {

                                        pd.dismiss();
                                        if (getActivity() != null) {

                                            Intent intent = getActivity().getIntent();
                                            intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                                            getActivity().setResult(RESULT_OK, intent);
                                            getActivity().finish();


                                        }
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                                    } else if (error.equalsIgnoreCase("true")) {

                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    } else {

                                        pd.dismiss();
                                        Toast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pd.dismiss();

                                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Toast.makeText(context, R.string.upload_has_been_cancelled, Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadId);
                                pd.dismiss();
                            }
                        });


                if (isNetworkAvailable()) {

                    multipartUploadRequest.startUpload();
                    pd.show();

                    Toast.makeText(getActivity(), R.string.upload_started, Toast.LENGTH_SHORT).show();


                } else {

                    pd.dismiss();
                    Toast.makeText(getActivity(), R.string.no_internet_connection_found, Toast.LENGTH_SHORT).show();

                }


            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }


    private JSONObject makeJsonObject(ArrayList<String> label, ArrayList<String> value,
                                      int count)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();


        for (int i = 0; i < count; i++) {

            obj = new JSONObject();

            try {
                obj.put("metaLabel", label.get(i));
                obj.put("metaEntered", value.get(i));


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("meta", jsonArray1);
        return finalobject;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


        if (!isConnected) {

            Toast.makeText(getActivity(), R.string.no_internet_connection_found, Toast.LENGTH_SHORT).show();

            UploadService.stopAllUploads();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
