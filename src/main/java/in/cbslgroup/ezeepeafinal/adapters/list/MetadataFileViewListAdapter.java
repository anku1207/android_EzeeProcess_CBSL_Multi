package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.search.MetaDataSearchActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.chip.TokenFilterAdapter;
import in.cbslgroup.ezeepeafinal.chip.User;
import in.cbslgroup.ezeepeafinal.chip.UsernameCompletionView;
import in.cbslgroup.ezeepeafinal.model.MetaDataViewList;
import in.cbslgroup.ezeepeafinal.model.SharedFileStatus;
import in.cbslgroup.ezeepeafinal.model.Version;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.Util;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;


public class MetadataFileViewListAdapter extends RecyclerView.Adapter<MetadataFileViewListAdapter.ViewHolder> {

    List<MetaDataViewList> metaDataViewList;
    Context context;
    MetaSearchFileViewActivity metaSearchFileViewActivity;

    private ArrayList<User> usernamelist;
    private ArrayList<User> usernamelistwithSlid;
    private TokenFilterAdapter filterAdapter;
    private UsernameCompletionView autoCompleteTextView;


    static public int checkeditemcounter = 0;

    public static ArrayList<String> docidlist = new ArrayList<>();
    private ArrayList<String> useridlist = new ArrayList<>();
    private ArrayList<String> shareFilesUseridList = new ArrayList<>();

    static public ArrayList<String> checkedlist = new ArrayList<>();

    RecyclerView rvVersion;
    VersionAdapter versionAdapter;
    ProgressBar progressBarVersion;
    TextView tvNoVersionFound;
    ProgressDialog mProgressDialog;
    private ArrayList<Version> versionList = new ArrayList<>();
    AlertDialog alertDialogVersion;

    //latest

    ArrayList<String> docidList = new ArrayList<>();
    ArrayList<String> shareuseridsList = new ArrayList<>();
    ArrayList<String> useridsList = new ArrayList<>();


    ArrayList<SharedFileStatus> sharedFileStatusList = new ArrayList<>();
    SharedFileStatusAdapter sharedFileStatusAdapter;
    RecyclerView rvSharedFileStatus;

    AlertDialog alertDialog;

    TextView textView;

    DownloadManager downloadManager;


    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    OnClickListener onClickListener;

    public interface OnClickListener {

        void onCheckInBtnClick(View v, int position, String docid, String docname);

        void onCheckOutBtnClick(View v, int position, String docid, String docname);

    }


    public MetadataFileViewListAdapter(List<MetaDataViewList> metaDataViewList, Context context) {
        this.metaDataViewList = metaDataViewList;
        this.context = context;
        metaSearchFileViewActivity = (MetaSearchFileViewActivity) context;
    }

    @NonNull
    @Override
    public MetadataFileViewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dms_fileview_list_2, parent, false);

        return new MetadataFileViewListAdapter.ViewHolder(v, metaSearchFileViewActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull final MetadataFileViewListAdapter.ViewHolder holder, final int position) {


        holder.tvFileName.setText(metaDataViewList.get(position).getFileName());
        holder.tvFileSize.setText(metaDataViewList.get(position).getFileSize());
        holder.tvUplodedDate.setText(metaDataViewList.get(position).getUploadDate());
        holder.tvUploadedBy.setText(metaDataViewList.get(position).getUploadedBy());
        holder.tvNoOfPages.setText(metaDataViewList.get(position).getNoOfPages());
        holder.tvFilePath.setText(metaDataViewList.get(position).getFilePath());
        holder.tvFileType.setText(metaDataViewList.get(position).getFiletype());
        holder.tvDocid.setText(metaDataViewList.get(position).getDocId());
        holder.tvDocname.setText(metaDataViewList.get(position).getDocName());

        holder.tvMetaData.setText(Html.fromHtml(metaDataViewList.get(position).getMetadata()), TextView.BufferType.SPANNABLE);

        String versionstatus = metaDataViewList.get(position).getVersionShowHide();
        String checkinstatus = metaDataViewList.get(position).getCheckincheckout();


        if (!metaSearchFileViewActivity.in_action_mode) {

            holder.checkBox.setVisibility(View.GONE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

            enableOptions(holder.llOptions);

        } else {

            holder.checkBox.setVisibility(View.VISIBLE);
            // holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.checkBox.setChecked(false);

            disableOptions(holder.llOptions);

        }


        if (MetaSearchFileViewActivity.checkinCheckout.equalsIgnoreCase("0")) {

            holder.ivCheckIn.setVisibility(View.GONE);

            holder.ivCheckout.setVisibility(View.GONE);

            holder.ivOpenFile.setVisibility(MetaSearchFileViewActivity.pdfView.equalsIgnoreCase("1") || MetaSearchFileViewActivity.imageView.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

            holder.ivDelete.setVisibility(MetaSearchFileViewActivity.fileDelete.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

            holder.ivMetadata.setVisibility(MetaSearchFileViewActivity.viewMetadata.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

            if (MetaSearchFileViewActivity.lockfolder.equalsIgnoreCase("1")) {

                if (MetaSearchFileViewActivity.isProtected.equalsIgnoreCase("0")) {

                    holder.ivVersion.setVisibility(versionstatus.equalsIgnoreCase("1") ? View.VISIBLE : View.INVISIBLE);
//
//                    if (MetaSearchFileViewActivity.fileVersion.equalsIgnoreCase("1")) {
//
//
//
//                    }

                    holder.ivShare.setVisibility(MetaSearchFileViewActivity.shareFile.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

                } else {

                    holder.ivVersion.setVisibility(View.GONE);
                    holder.ivShare.setVisibility(View.GONE);
                }


            } else {

                holder.ivVersion.setVisibility(View.VISIBLE);
                holder.ivShare.setVisibility(View.VISIBLE);
            }


        } else {


            if (checkinstatus.equals("0")) {

                holder.ivCheckIn.setVisibility(View.VISIBLE);
                holder.ivShare.setVisibility(View.GONE);
                holder.ivMetadata.setVisibility(View.GONE);
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivOpenFile.setVisibility(View.GONE);
                holder.ivCheckout.setVisibility(View.GONE);


            } else {


                holder.ivCheckIn.setVisibility(View.GONE);
                holder.ivCheckout.setVisibility(View.VISIBLE);


                holder.ivOpenFile.setVisibility(MetaSearchFileViewActivity.pdfView.equalsIgnoreCase("1") || MetaSearchFileViewActivity.imageView.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

                holder.ivDelete.setVisibility(MetaSearchFileViewActivity.fileDelete.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

                holder.ivMetadata.setVisibility(MetaSearchFileViewActivity.viewMetadata.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

                if (MetaSearchFileViewActivity.lockfolder.equalsIgnoreCase("1")) {

                    holder.ivVersion.setVisibility(versionstatus.equalsIgnoreCase("1") ? View.VISIBLE : View.INVISIBLE);

                    if (MetaSearchFileViewActivity.isProtected.equalsIgnoreCase("0")) {

//                        if (MetaSearchFileViewActivity.fileVersion.equalsIgnoreCase("1")) {
//
//
//
//                        }

                        holder.ivShare.setVisibility(MetaSearchFileViewActivity.shareFile.equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);

                    } else {

                        //holder.ivVersion.setVisibility(View.GONE);
                        holder.ivShare.setVisibility(View.GONE);
                    }

                } else {

                    holder.ivVersion.setVisibility(View.VISIBLE);
                    holder.ivShare.setVisibility(View.VISIBLE);
                }

            }


        }

//        if (MetaSearchFileViewActivity.checkinCheckout.equalsIgnoreCase("0")) {
//
//            holder.ivCheckIn.setVisibility(View.GONE);
//            holder.ivCheckout.setVisibility(View.GONE);
//
//
//
//            if (MetaSearchFileViewActivity.pdfView.equalsIgnoreCase("1") || MetaSearchFileViewActivity.imageView.equalsIgnoreCase("1")) {
//
//                holder.ivOpenFile.setVisibility(View.VISIBLE);
//
//            } else {
//
//
//                holder.ivOpenFile.setVisibility(View.GONE);
//            }
//
//            if (MetaSearchFileViewActivity.fileDelete.equalsIgnoreCase("1")) {
//
//                holder.ivDelete.setVisibility(View.VISIBLE);
//
//            } else {
//
//
//                holder.ivDelete.setVisibility(View.GONE);
//            }
//
//            if (MetaSearchFileViewActivity.viewMetadata.equalsIgnoreCase("1")) {
//
//                holder.ivMetadata.setVisibility(View.VISIBLE);
//
//            } else {
//
//
//                holder.ivMetadata.setVisibility(View.GONE);
//            }
//
//
//            if (MetaSearchFileViewActivity.fileVersion.equalsIgnoreCase("1")) {
//
//                if (versionstatus.equals("1")) {
//
//                    holder.ivVersion.setVisibility(View.VISIBLE);
//
//
//                } else {
//
//
//                    holder.ivVersion.setVisibility(View.INVISIBLE);
//
//
//                }
//            }
//
//            if (MetaSearchFileViewActivity.shareFile.equalsIgnoreCase("1")) {
//
//
//                holder.ivShare.setVisibility(View.VISIBLE);
//
//
//            } else {
//
//
//                holder.ivShare.setVisibility(View.GONE);
//
//
//            }
//
//
//        }
//        else {
//
//
//            if (checkinstatus.equals("0")) {
//
//
//                holder.ivCheckIn.setVisibility(View.VISIBLE);
//                holder.ivShare.setVisibility(View.GONE);
//                holder.ivMetadata.setVisibility(View.GONE);
//                holder.ivDelete.setVisibility(View.GONE);
//                holder.ivOpenFile.setVisibility(View.GONE);
//                holder.ivCheckout.setVisibility(View.GONE);
//
//
//            } else {
//
//
//                holder.ivCheckIn.setVisibility(View.GONE);
//                holder.ivCheckout.setVisibility(View.VISIBLE);
//
//
//                if (MetaSearchFileViewActivity.imageView.equalsIgnoreCase("1") || MetaSearchFileViewActivity.pdfView.equalsIgnoreCase("1")) {
//
//                    holder.ivOpenFile.setVisibility(View.VISIBLE);
//
//                } else {
//
//
//                    holder.ivOpenFile.setVisibility(View.GONE);
//                }
//
//                if (MetaSearchFileViewActivity.fileDelete.equalsIgnoreCase("1")) {
//
//                    holder.ivDelete.setVisibility(View.VISIBLE);
//
//                } else {
//
//
//                    holder.ivDelete.setVisibility(View.GONE);
//                }
//
//                if (MetaSearchFileViewActivity.viewMetadata.equalsIgnoreCase("1")) {
//
//                    holder.ivMetadata.setVisibility(View.VISIBLE);
//
//                } else {
//
//
//                    holder.ivMetadata.setVisibility(View.GONE);
//                }
//
//
//                if (MetaSearchFileViewActivity.fileVersion.equalsIgnoreCase("1")) {
//
//                    if (versionstatus.equals("1")) {
//
//                        holder.ivVersion.setVisibility(View.VISIBLE);
//
//
//                    } else {
//
//
//                        holder.ivVersion.setVisibility(View.INVISIBLE);
//
//
//                    }
//                }
//
//                if (MetaSearchFileViewActivity.shareFile.equalsIgnoreCase("1")) {
//
//
//                    holder.ivShare.setVisibility(View.VISIBLE);
//
//
//                } else {
//
//
//                    holder.ivShare.setVisibility(View.GONE);
//
//
//                }
///*
//                holder.ivShare.setVisibility(View.VISIBLE);
//                holder.ivMetadata.setVisibility(View.VISIBLE);
//                holder.ivDelete.setVisibility(View.VISIBLE);
//                holder.ivOpenFile.setVisibility(View.VISIBLE);
//                holder.ivCheckOut.setVisibility(View.VISIBLE);*/
//
//
//            }
//
//
//        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metaSearchFileViewActivity.prepareSelection(v, position, docidlist);


            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    checkedlist.add("" + position);
                    checkeditemcounter++;

                    docidlist.add(holder.tvDocid.getText().toString());
                    //  FileViewActivity.toolbar.setTitle(checkeditemcounter);
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

                } else {

                    docidlist.remove(holder.tvDocid.getText().toString());
                    checkeditemcounter = checkeditemcounter - 1;
                    checkedlist.remove("" + position);
                    //FileViewActivity.toolbar.setTitle(checkeditemcounter);
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

                }

            }
        });


        final String filepath = metaDataViewList.get(position).getFilePath();
        final String filename = metaDataViewList.get(position).getFileName();
        final String filetype = String.valueOf(holder.tvFileType.getText());
        Log.e("filetype", filetype);
        final String filesize = metaDataViewList.get(position).getFileSize();
        final String filedate = metaDataViewList.get(position).getUploadDate();
        final String meta = String.valueOf(holder.tvMetaData.getText());
        final String docid = holder.tvDocid.getText().toString();


        //holder.ivFiletypeIcon.setImageResource();


        switch (filetype) {
            case "pdf":

                holder.ivFiletypeIcon.setImageResource(R.drawable.pdf);


                break;
            case "jpg":
                holder.ivFiletypeIcon.setImageResource(R.drawable.jpg);


                break;
            case "png":
                holder.ivFiletypeIcon.setImageResource(R.drawable.png);


                break;
            case "jpeg":
                holder.ivFiletypeIcon.setImageResource(R.drawable.jpeg);

                break;

            case "tiff":

                holder.ivFiletypeIcon.setImageResource(R.drawable.tiff_png);


                break;
            case "mp4":
                holder.ivFiletypeIcon.setImageResource(R.drawable.mp4);

                break;

            default:
                holder.ivFiletypeIcon.setImageResource(R.drawable.file);

                break;
        }

        holder.ivCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onCheckInBtnClick(v, position, docid, filename);


            }
        });

        holder.ivCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onCheckOutBtnClick(v, position, docid, filename);


            }
        });


        holder.ivVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_version, null);

                progressBarVersion = dialogView.findViewById(R.id.progressbar_version);
                tvNoVersionFound = dialogView.findViewById(R.id.tv_no_version_found);
                progressBarVersion.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progressbar_rotate));
                rvVersion = dialogView.findViewById(R.id.rv_dms_file_view_version_list);
                rvVersion.setLayoutManager(new LinearLayoutManager(context));
                // rvVersion.setHasFixedSize(true);
                //rvVersion.setItemViewCacheSize(fileViewLists.size());


                Log.e("userid: ", MainActivity.userid);
                Log.e("slid", MetaDataSearchActivity.dynamicSlid);
                Log.e("docid", holder.tvDocid.getText().toString());


                versionList(MainActivity.userid, holder.tvDocid.getText().toString(), MetaDataSearchActivity.dynamicSlid);


                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_close_version_popup);
                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialogVersion.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialogVersion = dialogBuilder.create();
                alertDialogVersion.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogVersion.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                alertDialogVersion.show();


            }
        });


        holder.ivOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filepath == null || filepath.isEmpty()) {

                    Log.e("file path", "null");
                    Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                } else {

                    if (
                            filetype.equals("pdf")
                    ) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        intent.putExtra("docid", docid);

                        Initializer.globalDynamicSlid = metaDataViewList.get(position).getDocName();

                        context.startActivity(intent);

                    } else if (filetype.equals("jpg")
                            || filetype.equals("jpeg")
                            || filetype.equals("png")
                            || filetype.equals("bmp")
                            || filetype.equals("gif")) {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", filedate);
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", filesize);
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", docid);

                        Initializer.globalDynamicSlid = metaDataViewList.get(position).getDocName();

                        context.startActivity(intent);

                    } else {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_file_light_blue);
                        //Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Download File");
                        alertDialog.setIcon(R.drawable.file);
                        alertDialog.setMessage("This is file is not supported instead of this you can download the file so do you want to download" + " " + filename + "." + filetype + "?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        Log.e("doc path list", DOCUMENT_URL + filepath);

                                        mProgressDialog = new ProgressDialog(context);
                                        mProgressDialog.setMessage("File is downloading");
                                        mProgressDialog.setIndeterminate(true);
                                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        mProgressDialog.setCancelable(false);


                                      /*  Intent intent = new Intent(context, DownloadService.class);
                                        intent.putExtra("url", DOCUMENT_URL+filepath);
                                        intent.putExtra("filename",filename);
                                        intent.putExtra("type",filetype);
                                        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                        context.startService(intent);*/


                                        //new DownloadFile(context).execute(DOCUMENT_URL + filepath);

                                        DownloadFile(DOCUMENT_URL + filepath);


                                    }
                                });
                        alertDialog.show();


                        //Log.e("File not supported","true");

                    }

                }


            }
        });

        holder.ivMetadata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.show_meta_dialog, null);

                textView = dialogView.findViewById(R.id.tv_dms_file_metadata_popup);
                TextView textView = dialogView.findViewById(R.id.tv_dms_file_metadata_popup);
                ProgressBar pg = dialogView.findViewById(R.id.pg_dms_file_metadata_popup);


                getMetadata(metaDataViewList.get(position).getDocName(), metaDataViewList.get(position).getDocId(), textView, pg); /* if (meta.isEmpty() || meta.equals("")) {
                    textView.setText("No Metadata Found");
                } else {

                    textView.setText(meta);

                }*/

                //String docid = holder.tvDocid.getText().toString();
                // String docname = holder.tvDocname.getText().toString();

            /*   Log.e("docid",docid);
               Log.e("docname",docname);
*/
                // getMetdataSpecific(docid,docname);

                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_cancel_meta_popup);
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


        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);


                Button btn_cancel = dialogView.findViewById(R.id.btn_close_delete_popup);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });


                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_delete_popup);
                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fullname = String.valueOf(holder.tvUploadedBy.getText());
                        String roleid = MainActivity.roleId;
                        String ip = MainActivity.ip;
                        String userid = MainActivity.userid;
                        String filename = String.valueOf(holder.tvFileName.getText());
                        String docid = String.valueOf(holder.tvDocid.getText());

                        //delete doc volley request
                        deleteDoc(docid, fullname, "No", roleid, ip, userid, filename, position);

                        alertDialog.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_share_files, null);


             /*   Spinner spinner = dialogView.findViewById(R.id.spinner_share_dialog);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context,android.R.layout.simple_spinner_dropdown_item, usernames);
                spinner.setAdapter(adapter);*/

                autoCompleteTextView = dialogView.findViewById(R.id.autocomplete_textview);

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

                //Set the action to be taken when a Token is clicked
                autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

                usernamelistwithSlid = new ArrayList<>();
                usernamelist = new ArrayList<>();
              /*  usernamelist.add(new User("Ankit", R.drawable.ic_user));
                usernamelist.add(new User("Mukesh", R.drawable.ic_user));
                usernamelist.add(new User("Ankit 2", R.drawable.ic_user));
                usernamelist.add(new User("Mukesh 2", R.drawable.ic_user));
*/


                //Getting the whole list of group member which will be shown in share file dialog list

                String userid = MainActivity.userid;
                groupMemberList(userid);


              /*  //Initializing and attaching adapter for AutocompleteTextView
                filterAdapter = new FilterAdapter(context, R.layout.item_user, usernamelist);

                autoCompleteTextView.setAdapter(filterAdapter);

                autoCompleteTextView.allowDuplicates(false);
                autoCompleteTextView.setShowAlways(true);*/



           /*     autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                      if(autoCompleteTextView.isPopupShowing()==false){

                          autoCompleteTextView.showDropDown();
                      }

                        return false;
                    }
                });
*/


                Button btn_share = dialogView.findViewById(R.id.btn_share_popup);
                btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        docidlist.clear();
                        useridlist.clear();
                        shareFilesUseridList.clear();

                        JSONObject jsonObject = null;
                        List<User> tokens = autoCompleteTextView.getObjects();
                        StringBuilder content = new StringBuilder();
                        for (int i = 0; i < tokens.size(); i++) {
                            String slid = tokens.get(i).getSlid();
                            content.append(slid.substring(slid.indexOf("&&") + 2)).append(" ");
                            shareFilesUseridList.add(slid.substring(slid.indexOf("&&") + 2));
                        }

                        String docid = String.valueOf(holder.tvDocid.getText());
                        String userid = MainActivity.userid;

                        docidlist.add(docid);
                        useridlist.add(userid);

                        JSONArray jsonArray = new JSONArray();
                        JSONArray jsonArray1 = new JSONArray();

                        useridsList.add(userid);
                        docidList.add(docid);


                        String ip = MainActivity.ip;
                        String username = MainActivity.username;

                        for (int i = 0; i < shareFilesUseridList.size(); i++) {

                            jsonArray1.put(shareFilesUseridList.get(i));

                        }

                        String jsonarraystringUserids = jsonArray1.toString();

                        for (int i = 0; i < docidList.size(); i++) {

                            jsonArray.put(docidList.get(i));

                        }

                        String jsonarraystringDocids = jsonArray.toString();

                        Log.e("jsonarraystringDocids", jsonarraystringDocids);
                        Log.e("jsonarraystringuserids", jsonarraystringUserids);


                        if (shareFilesUseridList.size() != 0) {

                            autoCompleteTextView.deleteText();

                            //sharing single file
                            ShareSingleFiles(userid, jsonarraystringUserids, jsonarraystringDocids, ip, username);


                            //Toast.makeText(context, String.format("docid : "+docid+"\n"+"userid : "+userid+"\n"+"Userid : %s", content.toString())+"\n", Toast.LENGTH_LONG).show();

                            // Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
                            // Log.e("shareJson",jsonObject.toString());

                            alertDialog.dismiss();

                        } else {


                            Toast.makeText(context, "Please select atleast one user to share files", Toast.LENGTH_SHORT).show();

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

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return metaDataViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDocname, tvFileName, tvFileSize, tvNoOfPages, tvUploadedBy, tvUplodedDate, tvFilePath, tvFileType, tvMetaData, tvDocid;

        ImageView ivMetadata, ivOpenFile, ivDelete, ivFiletypeIcon, ivShare, ivCheckIn, ivCheckout, ivVersion;

        CardView cardView;
        CheckBox checkBox;

        LinearLayout llOptions;

        MetaSearchFileViewActivity metaSearchFileViewActivity;

        public ViewHolder(View itemView, MetaSearchFileViewActivity metaSearchFileViewActivity) {
            super(itemView);

            this.metaSearchFileViewActivity = metaSearchFileViewActivity;

            ivVersion = itemView.findViewById(R.id.iv_version);

            tvDocid = itemView.findViewById(R.id.tv_dms_list_docid);
            tvFileName = itemView.findViewById(R.id.tv_dms_list_filename);
            tvFileSize = itemView.findViewById(R.id.tv_dms_list_filesize);
            tvNoOfPages = itemView.findViewById(R.id.tv_dms_list_NoOfPages);
            tvUploadedBy = itemView.findViewById(R.id.tv_dms_list_uploadedby);
            tvUplodedDate = itemView.findViewById(R.id.tv_dms_list_uploadedDate);
            tvFilePath = itemView.findViewById(R.id.tv_dms_list_filepath);
            ivCheckout = itemView.findViewById(R.id.iv_checkout);
            ivCheckIn = itemView.findViewById(R.id.iv_checkin);
            tvFileType = itemView.findViewById(R.id.tv_dms_list_filetype);
            tvMetaData = itemView.findViewById(R.id.tv_dms_list_metadata);
            tvDocname = itemView.findViewById(R.id.tv_dms_list_docname);

            ivMetadata = itemView.findViewById(R.id.iv_show_metadata);
            ivOpenFile = itemView.findViewById(R.id.iv_open_file);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivFiletypeIcon = itemView.findViewById(R.id.iv_filetype);

            ivShare = itemView.findViewById(R.id.iv_share);

            cardView = itemView.findViewById(R.id.cv_search_list);
            checkBox = itemView.findViewById(R.id.checkbox_dms_list);
            cardView.setOnLongClickListener(metaSearchFileViewActivity);

            llOptions = itemView.findViewById(R.id.ll_dms_fileview_options);


        }
    }

    public void groupMemberList(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response in getmembrs", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {


                        String names = jsonArray.getString(i);

                        String namesOnly = names.substring(0, names.indexOf("&&"));

                        if (namesOnly.isEmpty() || namesOnly.equals("")) {

                            Log.e("names", "null");


                        } else {

                            // spinnermemberfullwithidList.add(names);

                            // usernamelist.add(new User(names,R.drawable.ic_user));


                            String username = names.substring(0, names.indexOf("&&"));

                            usernamelistwithSlid.add(new User(username, names, R.drawable.ic_user));
                            usernamelist.add(new User(username, names, R.drawable.ic_user));

                            // String userid  = names.substring(names.indexOf("&&"),names.length());


                            // spinnermemberlist.add(username);

                        }

                    }


                    //Initializing and attaching adapter for AutocompleteTextView
                    filterAdapter = new TokenFilterAdapter(context, R.layout.item_user, usernamelist) {
                        @Override
                        protected boolean keepObject(User obj, String mask) {
                            mask = mask.toLowerCase();
                            return obj.getUsername().toLowerCase().contains(mask);

                        }
                    };

                    autoCompleteTextView.setAdapter(filterAdapter);

                    autoCompleteTextView.allowDuplicates(false);
                    autoCompleteTextView.setShowAlways(true);

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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);


                return parameters;
            }


        };


        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    void ShareSingleFiles(final String userid, final String userids, final String docids, final String ip, final String username) {

        shareuseridsList.clear();
        docidList.clear();

        sharedFileStatusList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("reponse singlefiles", response);

                try {
                  /*  JSONArray jsonArray = new JSONArray(response);
                    String error = jsonArray.getJSONObject(0).getString("error");
                    String message = jsonArray.getJSONObject(0).getString("message");
                    Log.e("error",error);

                    switch (error) {
                        case "null":

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            break;
                        case "true":

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            break;
                        case "false":

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            break;
                    }*/


                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        String toUsername = jsonObject.getString("toUsername");
                        String docname = jsonObject.getString("docname");

                        if (error.equals("null") || error.equals("true")) {

                            String status = docname + " " + "to" + " " + toUsername + " " + message;
                            sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_close_black_24dp));
                        } else if (error.equals("false")) {

                            String status = docname + " " + "to" + " " + toUsername + " " + message;
                            sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_check_green_24dp));

                        }


                    }


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);

                    rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                    LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(context);
                    rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);
                    rvSharedFileStatus.setHasFixedSize(true);
                    rvSharedFileStatus.setItemViewCacheSize(sharedFileStatusList.size());

                    SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, context);
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Userid", userid);
                parameters.put("userids", userids);
                parameters.put("docids", docids);
                parameters.put("ip", ip);
                parameters.put("username", username);

                return parameters;
            }

        };


        alertDialog.dismiss();

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void deleteDoc(final String docid, final String fullname, final String permission, final String roleid, final String ip, final String userid, final String filename, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DELETE_DOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response deldoc", response);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equals("true")) {

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();


                    } else if (error.equals("false")) {


                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        metaDataViewList.remove(position);
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
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("docid", docid);
                params.put("fullname", fullname);
                params.put("ip", ip);
                params.put("permission", permission);
                params.put("roleid", roleid);
                params.put("filename", filename);


                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    void getMetadata(String docname, String docid, TextView tvMeta, ProgressBar pg) {

        pg.setVisibility(View.VISIBLE);
        tvMeta.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String metadata;
                StringBuilder sb = new StringBuilder();
                String meta = null;

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {

                        meta = "No Metadata Found";
                        pg.setVisibility(View.GONE);
                        tvMeta.setText(Html.fromHtml(meta), TextView.BufferType.SPANNABLE);
                        tvMeta.setVisibility(View.VISIBLE);


                    } else {

                        //clearing the stringbuilder string which is dynamically building
                        sb.setLength(0);


                        for (int j = 0; j < jsonArray.length(); j++) {

                            metadata = jsonArray.get(j).toString();//{"AadharNumber":"1003"}
                            String a = metadata.substring(metadata.indexOf("{") + 1, metadata.indexOf("}"));//"AadharNumber":"1003"
                            String b = a.substring(0, a.indexOf(":"));//"AadharNumber"
                            String label = b.substring(1, b.length() - 1);//label = AadharNumber
                            String c = a.substring(a.indexOf(":") + 1);//"1003"

                            if (c.equals("null")) {

                                c = " ";
                                sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");
                            } else {

                                c = c.substring(1, c.length() - 1);
                                sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");

                            }

                        }

                        meta = sb.toString();


                        Log.e("meta in fileview", meta);


                        pg.setVisibility(View.GONE);
                        tvMeta.setText(Html.fromHtml(meta), TextView.BufferType.SPANNABLE);
                        tvMeta.setVisibility(View.VISIBLE);


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
                params.put("docname", docname);
                params.put("docid", docid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    public void clearCheckedList() {

        checkedlist.clear();
        docidList.clear();
        docidlist.clear();


    }

    private static void disableOptions(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableOptions((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    private static void enableOptions(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableOptions((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }

    }

    private void DownloadFile(String url) {

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


//        File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeProcess Downloads");
//
//        if (!myDirectory.exists()) {
//            myDirectory.mkdirs();
//        }
//
//        String PATH = "/EzeeProcess Downloads/";


        String PATH = Environment.DIRECTORY_DOWNLOADS;
        request.setDestinationInExternalPublicDir(PATH, uri.getLastPathSegment());
        downloadManager.enqueue(request);


    }

    private void delVersion(final String docid, final int pos) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DEL_DOC_VERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("onResponse: ", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        versionList.remove(pos);
                        versionAdapter.notifyItemRemoved(pos);
                        //notifyItemChanged(pos);

                        if (versionList.size() == 0) {


                            progressBarVersion.setVisibility(View.GONE);
                            rvVersion.setVisibility(View.GONE);
                            tvNoVersionFound.setVisibility(View.VISIBLE);

                        }


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
                params.put("docid", docid);
                params.put("ip", MainActivity.ip);
                params.put("username", MainActivity.username);
                params.put("userid", MainActivity.userid);

                Util.printParams(params, "version_del_params");
                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void versionList(final String userid, final String docid, final String slid) {

        versionList.clear();
        progressBarVersion.setVisibility(View.VISIBLE);
        rvVersion.setVisibility(View.GONE);
        tvNoVersionFound.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("onVersionResponse: ", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            String versionname = jsonArray.getJSONObject(i).getString("docVersion");
                            String docpath = jsonArray.getJSONObject(i).getString("docpath");
                            String docname = jsonArray.getJSONObject(i).getString("docname");
                            String docid = jsonArray.getJSONObject(i).getString("docid");
                            String fileSlid = jsonArray.getJSONObject(i).getString("file_slid");

                            versionList.add(new Version(docname, docid, docpath, versionname, fileSlid));


                        }

                        progressBarVersion.setVisibility(View.GONE);
                        rvVersion.setVisibility(View.VISIBLE);
                        tvNoVersionFound.setVisibility(View.GONE);


                        versionAdapter = new VersionAdapter(versionList, context);
                        versionAdapter.setOnButtonClickListener(new VersionAdapter.OnButtonClickListener() {
                            @Override
                            public void onFileViewButtonClick(String path, String filetype, String filename, String docid, String docpath, int
                                    pos) {


                                Log.e("filetype", filetype);

                                if (filetype.equals("pdf")
                                ) {


                                    Intent intent = new Intent(context, PdfViewerActivity.class);
                                    intent.putExtra("filename", filename);
                                    intent.putExtra("filepath", path);
                                    intent.putExtra("docid", docid);

                                    context.startActivity(intent);


                                } else if ((filetype.equals("jpg")
                                        || filetype.equals("jpeg")
                                        || filetype.equals("png")
                                        || filetype.equals("bmp")
                                        || filetype.equals("gif"))) {


                                    Intent intent = new Intent(context, ImageViewerActivity.class);
                                    intent.putExtra("date", "");
                                    intent.putExtra("type", filetype);
                                    intent.putExtra("name", filename);
                                    intent.putExtra("size", "");
                                    intent.putExtra("path", docpath);
                                    intent.putExtra("docid", docid);

                                    context.startActivity(intent);

                                } else {


                                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                    alertDialog.setTitle(context.getString(R.string.download_file));
                                    alertDialog.setIcon(R.drawable.file);
                                    alertDialog.setMessage(context.getString(R.string.this_is_file_is_not_supported_instead_of_this_you_can_download_the_file_so_do_you_want_to_download) + " " + filename + "." + filetype + "?");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });

                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.download),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {


                                                    Log.e("doc path list", DOCUMENT_URL + versionList.get(pos).getDocpath());

                                                    mProgressDialog = new ProgressDialog(context);
                                                    mProgressDialog.setMessage( context.getString(R.string.file_is_downloading));
                                                    mProgressDialog.setIndeterminate(true);
                                                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                    mProgressDialog.setCancelable(false);


                                      /*  Intent intent = new Intent(context, DownloadService.class);
                                        intent.putExtra("url", DOCUMENT_URL+filepath);
                                        intent.putExtra("filename",filename);
                                        intent.putExtra("filetype",filefiletype);
                                        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                        context.startService(intent);*/


                                                    DownloadFile(DOCUMENT_URL + versionList.get(pos).getDocpath());


                                                }
                                            });
                                    alertDialog.show();

                                }


                            }

                            @Override
                            public void onDeleteVersionButtonClick(String docid, int pos) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                tvHeading.setText(R.string.delete_version);
                                //tvHeading.setBackgroundColor(getResources().getColor(R.color.green_dark));


                                TextView tvSubheading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                tvSubheading.setText(R.string.version_delete_msg);

                                Button btn_cancel = dialogView.findViewById(R.id.btn_no_pDel_popup);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        alertDialog.dismiss();
                                    }
                                });

                                Button btn_ok = dialogView.findViewById(R.id.btn_yes_pDel_popup);
                                btn_ok.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        // checkIn(docid);
                                        delVersion(docid, pos);

                                        alertDialog.dismiss();
                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                alertDialog.show();


                            }

                            @Override
                            public void onMetaViewButtonClick(String docname, String docid) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.show_meta_dialog, null);


                                TextView textView = dialogView.findViewById(R.id.tv_dms_file_metadata_popup);
                                ProgressBar pg = dialogView.findViewById(R.id.pg_dms_file_metadata_popup);


                                getMetadata(docname, docid, textView, pg);


                                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_cancel_meta_popup);
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
                        });

                        rvVersion.setAdapter(versionAdapter);

                    } else {


                        progressBarVersion.setVisibility(View.GONE);
                        rvVersion.setVisibility(View.GONE);
                        tvNoVersionFound.setVisibility(View.VISIBLE);


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


                params.put("userid", userid);
                params.put("docid", docid);
                params.put("slid", slid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }


}
