package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeepeafinal.ui.activity.RecycleBinActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.interfaces.RecycleBinListListener;
import in.cbslgroup.ezeepeafinal.model.Recyclebin;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.Initializer;
import in.cbslgroup.ezeepeafinal.utils.VolleySingelton;

public class RecycleBinListAdapter extends RecyclerView.Adapter<RecycleBinListAdapter.ViewHolder> {

    public static ArrayList<String> docidlist = new ArrayList<>();
    public static ArrayList<String> checkedlist = new ArrayList<>();
    public static int checkeditemcounter = 0;
    //permissions

    AlertDialog alertDialog;
    RecycleBinActivity recycleBinActivity;
    ViewHolder holderDynamic;
    private Context context;
    private List<Recyclebin> recyclebinList;
    private List<Recyclebin> selectedItemList = new ArrayList<>();
    private boolean isSelectedAll;

    RecycleBinListListener listener;

    Recyclebin recyclebinobj;

    public RecycleBinListAdapter(Context context, List<Recyclebin> recyclebinList, RecycleBinListListener listener) {
        this.context = context;
        this.recyclebinList = recyclebinList;
        recycleBinActivity = (RecycleBinActivity) context;
        //getPrivilages(MainActivity.userid);
        this.listener = listener;

    }

    @Override
    public RecycleBinListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclebin_list_2, null);

        return new ViewHolder(v, recycleBinActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleBinListAdapter.ViewHolder holder, final int position) {


        holderDynamic = holder;
        recyclebinobj = recyclebinList.get(position);
        holder.tvFilename.setText(recyclebinList.get(position).getFilename());
        holder.tvFilesize.setText(recyclebinList.get(position).getFileSize());
        holder.tvFileExtension.setText(recyclebinList.get(position).getFileExtension());
        holder.tvstoragename.setText(recyclebinList.get(position).getStorageName());
        holder.tvDocid.setText(recyclebinList.get(position).getDocid());

        if (RecycleBinActivity.MULTI_DEL_PERMISSION.equalsIgnoreCase("1")) {
            holder.btnDelete.setVisibility(View.VISIBLE);

        } else {
            holder.btnDelete.setVisibility(View.GONE);

        }

        if (RecycleBinActivity.RESTORE_PERMISSION.equalsIgnoreCase("1")) {
            holder.btnRestore.setVisibility(View.VISIBLE);

        } else {
            holder.btnRestore.setVisibility(View.GONE);

        }



     /*   final Recyclebin recyclebin = recyclebinList.get(position);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


               *//* if (selectedItemList.contains(recyclebin)) {
                    selectedItemList.remove(recyclebin);
                    unhighlightView(holder);
                } else {
                    selectedItemList.add(recyclebin);
                    highlightView(holder);
                }

                if (selectedItemList.contains(recyclebin))
                    highlightView(holder);
                else
                    unhighlightView(holder);*//*

                return true;

            }
        });*/


        if (!recycleBinActivity.in_action_mode) {

            holder.checkbox.setVisibility(View.GONE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

        } else {

            holder.checkbox.setVisibility(View.VISIBLE);
            // holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.checkbox.setChecked(false);

        }
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recycleBinActivity.prepareSelection(v, position, docidlist);


            }
        });

        if (!isSelectedAll) {
            holder.checkbox.setChecked(false);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));


        } else {
            holder.checkbox.setChecked(true);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
        }


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    checkedlist.add("" + holder.tvDocid.getText().toString());
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


        final String filetype = String.valueOf(holder.tvFileExtension.getText());
        switch (filetype) {
            case "pdf":
                holder.ivFiletype.setImageResource(R.drawable.pdf);
                break;
            case "jpg":
                holder.ivFiletype.setImageResource(R.drawable.jpg);
                break;
            case "png":
                holder.ivFiletype.setImageResource(R.drawable.png);
                break;
            case "jpeg":
                holder.ivFiletype.setImageResource(R.drawable.jpeg);
                break;
            case "docx":
                holder.ivFiletype.setImageResource(R.drawable.doc);
                break;
            case "doc":
                holder.ivFiletype.setImageResource(R.drawable.doc);
                break;
            case "xls":
                holder.ivFiletype.setImageResource(R.drawable.xls);
                break;
            case "pptx":
                holder.ivFiletype.setImageResource(R.drawable.ppt);
                break;
            case "mp4":
                holder.ivFiletype.setImageResource(R.drawable.mp4);
                break;
            default:
                holder.ivFiletype.setImageResource(R.drawable.file);
                break;
        }

        /*if(selected_usersList.contains(usersList.get(position)))
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
*/


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener.onPerDeleteButtonClick(v, holder.tvDocid.getText().toString(), position, recyclebinobj);


            }
        });

        holder.btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onRestoreButtonClick(v, holder.tvDocid.getText().toString(), position, recyclebinobj);


            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RecycleBinActivity.PDF_VIEW_PERMISSION.equalsIgnoreCase("1")
                        || RecycleBinActivity.IMAGE_VIEW_PERMISSION.equalsIgnoreCase("1")) {

                    final String filepath = recyclebinList.get(position).getFilepath();
                    final String filedate = recyclebinList.get(position).getDate();
                    final String filesize = recyclebinList.get(position).getFileSize();
                    final String filetype = String.valueOf(holder.tvFileExtension.getText());
                    final String filename = recyclebinList.get(position).getFilename();


                    if (
                            filetype.equals("pdf")
                    ) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        intent.putExtra("docid", recyclebinList.get(position).getDocid());
                        intent.putExtra("slid", recyclebinList.get(position).getSlid());

                        Initializer.globalDynamicSlid = recyclebinList.get(position).getSlid();

                        context.startActivity(intent);

                    } else if (filetype.equalsIgnoreCase("jpg")
                            || filetype.equalsIgnoreCase("jpeg")
                            || filetype.equalsIgnoreCase("png")
                            || filetype.equalsIgnoreCase("bmp")
                            || filetype.equalsIgnoreCase("gif")
                    ) {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", filedate);
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", filesize);
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", recyclebinList.get(position).getDocid());
                        intent.putExtra("slid", recyclebinList.get(position).getSlid());

                        Initializer.globalDynamicSlid = recyclebinList.get(position).getSlid();

                        context.startActivity(intent);

                    } else {

                        Toast.makeText(context, "File not supported", Toast.LENGTH_SHORT).show();
                        //Log.e("File not supported","true");

                    }

                } else {

                    Toast.makeText(context, "Sorry you don't have the permission to view file", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    public void selectAll() {

        isSelectedAll = true;

        checkedlist.clear();

        for (int i = 0; i < recyclebinList.size(); i++) {


            checkedlist.add(recyclebinList.get(i).getDocid());

        }
        notifyDataSetChanged();


    }

    public void deselectAll() {

        isSelectedAll = false;
        checkedlist.clear();
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return recyclebinList.size();
    }

    void restoreFile(final String docid, final String userid, final String ip, final String username, final int position) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("restore", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {


                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equals("false")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        recyclebinList.remove(position);
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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("resdocid", docid);
                parameters.put("resuserid", userid);
                parameters.put("resusername", username);
                parameters.put("resip", ip);

                return parameters;

            }


        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    void delPermanentFile(final String docid, final String userid, final String ip, final String username, final int position) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("delete permanent", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {


                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equals("false")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        recyclebinList.remove(position);
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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("deldocid", docid);
                parameters.put("deluserid", userid);
                parameters.put("delusername", username);
                parameters.put("delip", ip);

                return parameters;

            }

        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilesize, tvFileExtension, tvFilename, tvstoragename, tvDocid, tvDocdate, tvDocpath;
        Button btnRestore, btnDelete;
        ImageView ivFiletype;

        CardView cardView;

        CheckBox checkbox;

        LinearLayout llRootView;

        RecycleBinActivity recycleBinActivity;

        public ViewHolder(View itemView, RecycleBinActivity recycleBinActivity) {
            super(itemView);

            this.recycleBinActivity = recycleBinActivity;

            tvFilename = itemView.findViewById(R.id.tv_recyclebin_list_filename);
            tvFileExtension = itemView.findViewById(R.id.tv_recyclebin_list_fileextension);
            tvFilesize = itemView.findViewById(R.id.tv_recyclebin_list_filesize);
            ivFiletype = itemView.findViewById(R.id.recyclebin_list_iv_filetype);
            tvstoragename = itemView.findViewById(R.id.tv_recyclebin_list_storagename);
            tvDocid = itemView.findViewById(R.id.tv_recyclebin_list_docid);

            tvDocpath = itemView.findViewById(R.id.tv_recyclebin_list_docpath);
            tvDocdate = itemView.findViewById(R.id.tv_recyclebin_list_docdate);

            btnDelete = itemView.findViewById(R.id.btn_delete_recyclebin_list);
            btnRestore = itemView.findViewById(R.id.btn_restore_recyclebin_list);
            cardView = itemView.findViewById(R.id.cv_recyclebin_list);
            checkbox = itemView.findViewById(R.id.checkbox_recyclebin);

            llRootView = itemView.findViewById(R.id.ll_root_recyclebinlist);

            cardView.setOnLongClickListener(recycleBinActivity);


        }
    }


}
