package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.interfaces.SharedFilesListListener;
import in.cbslgroup.ezeepeafinal.model.SharedFiles;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.Initializer;

public class SharedFilesAdapter extends RecyclerView.Adapter<SharedFilesAdapter.ViewHolder> {

    List<SharedFiles> sharedFilesList;
    Context context;
    int Position;
    AlertDialog alertDialog;
    String filetype;
    SharedFilesListListener listener;


    public SharedFilesAdapter(List<SharedFiles> sharedFilesList, Context context,SharedFilesListListener listener) {
        this.sharedFilesList = sharedFilesList;
        this.context = context;
        this.listener = listener;
    }



    @Override
    public SharedFilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_files_list, parent, false);
         return new ViewHolder(v);



    }



    @Override
    public void onBindViewHolder(@NonNull final SharedFilesAdapter.ViewHolder holder, final int position) {


        holder.tvfilename.setText(sharedFilesList.get(position).getFilename());
        holder.tvnoOfPages.setText(sharedFilesList.get(position).getNoOfPages());
        holder.tvshareDate.setText(sharedFilesList.get(position).getSharedDate());
        holder.tvstorageName.setText(sharedFilesList.get(position).getStorageName());
        holder.tvsharedTo.setText(sharedFilesList.get(position).getSharedTo());
        // holder.tvMetadata.setText(searchList.get(position).getMetadata());
        holder.tvPath.setText(sharedFilesList.get(position).getFilepath());
        holder.tvDocid.setText(sharedFilesList.get(position).getdocid());
        holder.tvExtension.setText(sharedFilesList.get(position).getFileExtension());
        holder.tvtoUserid.setText(sharedFilesList.get(position).getTouserid());


        final String filetype = String.valueOf(holder.tvExtension.getText());
        final String filename = sharedFilesList.get(position).getFilename();
        final String filepath = String.valueOf(holder.tvPath.getText());
        final String filedate = " ";
        final String filesize =" ";

        holder.ivFiletypeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filetype.equals("pdf")) {
                    //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("filename", filename);
                    intent.putExtra("filepath", filepath);
                    intent.putExtra("docid", sharedFilesList.get(position).getdocid());

                    Initializer.globalDynamicSlid = sharedFilesList.get(position).getSlid();

                    context.startActivity(intent);

                } else if (filetype.equals("jpg") || filetype.equals("jpeg") || filetype.equals("png")) {
                    // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                    Intent intent = new Intent(context, ImageViewerActivity.class);
                    intent.putExtra("date", filedate);
                    intent.putExtra("type", filetype);
                    intent.putExtra("name", filename);
                    intent.putExtra("size", filesize);
                    intent.putExtra("path", filepath);
                    intent.putExtra("docid", sharedFilesList.get(position).getdocid());

                    Initializer.globalDynamicSlid = sharedFilesList.get(position).getSlid();

                    context.startActivity(intent);

                } else {
                    // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_file_light_blue);
                    Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

                    //Log.e("File not supported","true");

                }




            }
        });


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

             /*
                holder.ivOpenFile.setImageResource(R.drawable.jpeg);

                break;*/


            case "tiff":

                holder.ivFiletypeIcon.setImageResource(R.drawable.tiff_png);


                break;

            case "doc":

                holder.ivFiletypeIcon.setImageResource(R.drawable.doc);


                break;
            case "mp4":

                holder.ivFiletypeIcon.setImageResource(R.drawable.mp4);


                break;

            default:

                holder.ivFiletypeIcon.setImageResource(R.drawable.file);


                break;
        }



        holder.btnUndoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String undouserid = MainActivity.userid;
                String undousername = MainActivity.username;
                String undoIp = MainActivity.ip;
                String undodocid = holder.tvDocid.getText().toString();
                String undoTouserid = MainActivity.userid;
                String toids = holder.tvtoUserid.getText().toString();

                listener.onUndoButtonClick(v,undodocid,position,toids);





            }
        });


    }

    @Override
    public int getItemCount() {
        return sharedFilesList.size();
    }

   /* void UndoSharedFiles(final String userid, final String username, final String ip, final String docid, final int position) {

        SharedFilesActivity.llNofilefound.setVisibility(View.GONE);
        SharedFilesActivity.rvSharedFiles.setVisibility(View.GONE);
        SharedFilesActivity.progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("undo", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0 || response.equals("") || response.isEmpty()) {

                        SharedFilesActivity.llNofilefound.setVisibility(View.VISIBLE);


                    }


                    else {

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        Log.e("error and message", error + " " + message);

                        switch (error) {
                            case "null":

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                break;
                            case "true":

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                break;

                            case "false":

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                sharedFilesList.remove(position);
                                notifyDataSetChanged();



                                break;
                        }

                    }



                    SharedFilesActivity.llNofilefound.setVisibility(View.GONE);
                    SharedFilesActivity.rvSharedFiles.setVisibility(View.VISIBLE);
                    SharedFilesActivity.progressBar.setVisibility(View.GONE);


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
                parameters.put("UndoUserid", userid);
                parameters.put("UndoIp", ip);
                parameters.put("UndoUsername", username);
                parameters.put("UndoDocid", docid);

                return parameters;

            }


        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }*/



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvToDocid,tvfilename, tvnoOfPages, tvstorageName, tvshareDate, tvsharedTo, tvPath, tvExtension, tvDocid, tvtoUserid;
        ImageView ivFiletypeIcon;
        Button btnUndoShare;

        public ViewHolder(View itemView) {
            super(itemView);

            tvfilename = itemView.findViewById(R.id.tv_shared_file_list_filename);
            tvnoOfPages = itemView.findViewById(R.id.tv_shared_files_list_NoOfPages);
            tvExtension = itemView.findViewById(R.id.tv_shared_file_list_fileExtension);
            tvsharedTo = itemView.findViewById(R.id.tv_shared_files_list_sharedto);
            tvshareDate = itemView.findViewById(R.id.tv_shared_file_list_sharedDate);
            tvDocid = itemView.findViewById(R.id.tv_shared_file_list_docid);
            tvtoUserid = itemView.findViewById(R.id.tv_shared_file_list_touserid);

            ivFiletypeIcon = itemView.findViewById(R.id.iv_sharedfiles_filetype);
            tvstorageName = itemView.findViewById(R.id.tv_shared_files_list_Storagename);
            tvPath = itemView.findViewById(R.id.tv_shared_file_list_filepath);
            btnUndoShare = itemView.findViewById(R.id.btn_shared_files_list_undoshare);






        }
    }


}
