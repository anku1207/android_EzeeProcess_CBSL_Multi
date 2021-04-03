package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.model.Attachments;
import in.cbslgroup.ezeepeafinal.R;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    List<Attachments> attachmentsList;
    Context context;

    public AttachmentAdapter(List<Attachments> attachmentsList, Context context) {
        this.attachmentsList = attachmentsList;
        this.context = context;

    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    private OnClickListener listener;

    public interface OnClickListener {

        void onAddButtonClicked(int pos);

        void onMinusButtonClicked(int pos);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.attachment_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Attachments item = attachmentsList.get(position);
        holder.tvFileName.setText(item.getFileName());
        holder.tvFileType.setText(item.getFileType().toUpperCase());

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onMinusButtonClicked(position);

            }

        });

        if(item.getSanctioned()!=null && item.getSanctioned()){

            holder.ivDel.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.getFileType().equals("pdf")) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", item.getFileName());
                        intent.putExtra("filepath", item.getFilePath());
                        intent.putExtra("docid",item.getDocid());


                        context.startActivity(intent);

                    }
                    else if (item.getFileType().equals("jpg") || item.getFileType().equals("jpeg") || item.getFileType().equals("png"))
                    {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", "");
                        intent.putExtra("type", item.getFileType());
                        intent.putExtra("name", item.getFileName());
                        intent.putExtra("size", "");
                        intent.putExtra("path", item.getFilePath());
                        intent.putExtra("docid", item.getDocid());

                        context.startActivity(intent);

                    }else{

                        Toast.makeText(context, "Unsupported File type", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return attachmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFileType, tvFileName;
        ImageView ivDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tv_attach_file_name);
            tvFileType = itemView.findViewById(R.id.tv_attach_file_type);
            ivDel = itemView.findViewById(R.id.iv_attachment_del);

        }
    }
}
