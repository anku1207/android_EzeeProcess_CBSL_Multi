package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.model.MultipleSelect;
import in.cbslgroup.ezeepeafinal.utils.Util;

public class MultipleFileChooseAdapter extends RecyclerView.Adapter<MultipleFileChooseAdapter.ViewHolder> {

    List<MultipleSelect> multipleSelectList;
    Context context;

    public MultipleFileChooseAdapter(List<MultipleSelect> multipleSelectList, Context context) {
        this.multipleSelectList = multipleSelectList;
        this.context = context;
    }

    @NonNull
    @Override
    public MultipleFileChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_file_choose_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleFileChooseAdapter.ViewHolder holder, final int position) {


        holder.tvFilesize.setText(Util.formatFileSize(Long.parseLong(multipleSelectList.get(position).getFilesize())));

        final String filepath = multipleSelectList.get(position).getFilePath();
        final String uri = multipleSelectList.get(position).getUri();

        Log.e("file uri",uri);

        final String filename = filepath.substring(filepath.lastIndexOf("/")+1,filepath.length());
        final String filetype = filename.substring(filename.lastIndexOf(".")+1,filename.length());
        Log.e("filename",filename);


        holder.tvFilename.setText(filename);
        /*String filename = holder.tvFilename.getText().toString().substring(holder.tvFilename.getText().toString().lastIndexOf("/"), holder.tvFilename.getText().toString().length());
        String filetype = filename.substring(filename.lastIndexOf("."), filename.length());*/

        Log.e("filetype", filetype);

        switch (filetype) {
            case "pdf":

                holder.ivType.setImageResource(R.drawable.pdf);


                break;
            case "jpg":
                holder.ivType.setImageResource(R.drawable.jpg);


                break;
            case "png":
                holder.ivType.setImageResource(R.drawable.png);

                break;
            case "jpeg":
                holder.ivType.setImageResource(R.drawable.jpeg);


                break;
            case "bmp":

            case "tiff":

                holder.ivType.setImageResource(R.drawable.tiff_png);


                break;
            case "mp4":

                holder.ivType.setImageResource(R.drawable.mp4);

                break;
            case "3gp":

                break;

            default:

                holder.ivType.setImageResource(R.drawable.file);


                break;
        }

        holder.ivType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.fromFile(new File(filepath))));
            }
        });


    }

    @Override
    public int getItemCount() {
        return multipleSelectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilename;
        TextView tvFilesize;
        ImageView ivType;

        public ViewHolder(View itemView) {
            super(itemView);


            tvFilename = itemView.findViewById(R.id.tv_multichoose_filename);
            tvFilesize = itemView.findViewById(R.id.tv_multichoose_filesize);
            ivType = itemView.findViewById(R.id.iv_multichoose_filetype);

        }
    }
}
