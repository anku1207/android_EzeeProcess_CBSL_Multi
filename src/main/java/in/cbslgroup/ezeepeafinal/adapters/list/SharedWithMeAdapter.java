package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.sharedfiles.SharedWithMeActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.model.SharedWithMe;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.Initializer;

public class SharedWithMeAdapter extends RecyclerView.Adapter<SharedWithMeAdapter.ViewHolder> {

    List<SharedWithMe> sharedWithMeList;
    Context context;

    SharedWithMeActivity sharedWithMeActivity ;

    public SharedWithMeAdapter(List<SharedWithMe> sharedWithMeAdapterList, Context context) {
        this.sharedWithMeList = sharedWithMeAdapterList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedwithme_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvDocname.setText(sharedWithMeList.get(position).getDocname());
        holder.tvNoOfPages.setText(sharedWithMeList.get(position).getNoOfPages());
        holder.tvStorageName.setText(sharedWithMeList.get(position).getStorageName());
        holder.tvSharedBy.setText(sharedWithMeList.get(position).getSharedBy());
        holder.tvExtension.setText(sharedWithMeList.get(position).getExtension());
        holder.tvdocPath.setText(sharedWithMeList.get(position).getDocPath());
        holder.tvSharedDate.setText(sharedWithMeList.get(position).getSharedDate());

        final String filetype = String.valueOf(holder.tvExtension.getText());
        final String filename = String.valueOf(holder.tvDocname.getText());
        final String filepath = String.valueOf(holder.tvdocPath.getText());


        switch (filetype) {
            case "pdf":

                holder.ivFileType.setImageResource(R.drawable.pdf);


                break;
            case "jpg":
                holder.ivFileType.setImageResource(R.drawable.jpg);

                break;
            case "png":
                holder.ivFileType.setImageResource(R.drawable.png);
                break;
            case "jpeg":
                holder.ivFileType.setImageResource(R.drawable.jpeg);


                break;

            case "tiff":
                holder.ivFileType.setImageResource(R.drawable.tiff_png);


                break;
            case "docx":
                holder.ivFileType.setImageResource(R.drawable.doc);

                break;

            case "doc":
                holder.ivFileType.setImageResource(R.drawable.doc);

                break;
            case "xls":
                holder.ivFileType.setImageResource(R.drawable.xls);

                break;

            case "pptx":

                holder.ivFileType.setImageResource(R.drawable.ppt);

                break;
            case "mp4":
                holder.ivFileType.setImageResource(R.drawable.mp4);


                break;
            case "3gp":

                break;
            default:
                holder.ivFileType.setImageResource(R.drawable.file);

                break;
        }


        holder.cvSharedWithMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              if (filetype.equals("pdf")){


                  Intent intent = new Intent(context, PdfViewerActivity.class);
                  intent.putExtra("filename", filename);
                  intent.putExtra("filepath", filepath);
                  intent.putExtra("docid", sharedWithMeList.get(position).getDocid());
                  Initializer.globalDynamicSlid = sharedWithMeList.get(position).getSlid();

                  context.startActivity(intent);
              }
              else if (filetype.equals("jpg") || filetype.equals("jpeg") || filetype.equals("png")) {
                  // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                  Intent intent = new Intent(context, ImageViewerActivity.class);
                  intent.putExtra("date", "");
                  intent.putExtra("type", filetype);
                  intent.putExtra("name", filename);
                  intent.putExtra("size", "");
                  intent.putExtra("path", filepath);
                  intent.putExtra("docid", sharedWithMeList.get(position).getDocid());

                  context.startActivity(intent);

              } else {
                  // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_file_light_blue);
                  Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

                  //Log.e("File not supported","true");

              }


            }
        });




    }

    @Override
    public int getItemCount() {

        return sharedWithMeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNoOfPages,tvDocname,tvdocPath,tvSharedBy,tvSharedDate,tvStorageName,tvExtension;
        CardView cvSharedWithMe;
        ImageView ivFileType;


        public ViewHolder(View itemView) {
            super(itemView);



            tvNoOfPages = itemView.findViewById(R.id.tv_shared_file_with_me_NoOfPages);
            tvSharedBy = itemView.findViewById(R.id.tv_shared_file_with_me_sharedBy);
            tvSharedDate = itemView.findViewById(R.id.tv_shared_file_with_me_sharedDate);
            tvStorageName = itemView.findViewById(R.id.tv_shared_file_with_me_Storagename);
            tvDocname= itemView.findViewById(R.id.tv_shared_with_me_filename);
            tvdocPath = itemView.findViewById(R.id.tv_shared_with_me_filepath);
            tvExtension = itemView.findViewById(R.id.tv_shared_with_me_fileExtension);
            ivFileType = itemView.findViewById(R.id.iv_sharedwithme_filetype);

            cvSharedWithMe = itemView.findViewById(R.id.cv_sharedwithme);



        }
    }



}
