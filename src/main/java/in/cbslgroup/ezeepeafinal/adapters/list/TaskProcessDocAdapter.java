package in.cbslgroup.ezeepeafinal.adapters.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.model.TaskProcessDocs;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.Initializer;

public class TaskProcessDocAdapter extends RecyclerView.Adapter<TaskProcessDocAdapter.ViewHolder> {

    List<TaskProcessDocs> taskProcessDocsList;
    Context context;


    public TaskProcessDocAdapter(List<TaskProcessDocs> taskProcessDocsList, Context context) {
        this.taskProcessDocsList = taskProcessDocsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_process_doc_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.docname.setText(taskProcessDocsList.get(position).getDocname());
        holder.docPath.setText(taskProcessDocsList.get(position).getDocPath());
        holder.doctype.setText(taskProcessDocsList.get(position).getExtension());

        final String filetype = holder.doctype.getText().toString();
        final String filepath = holder.docPath.getText().toString();
        final String filename = holder.docname.getText().toString();

        switch (filetype) {
            case "pdf":
                holder.ivDocType.setImageResource(R.drawable.pdf);
                break;
            case "jpg":
                holder.ivDocType.setImageResource(R.drawable.jpg);
                break;
            case "png":
                holder.ivDocType.setImageResource(R.drawable.png);
                break;
            case "jpeg":
                holder.ivDocType.setImageResource(R.drawable.jpeg);
                break;
            case "tiff":
                holder.ivDocType.setImageResource(R.drawable.tiff_png);
                break;
            case "mp4":
                holder.ivDocType.setImageResource(R.drawable.mp4);
                break;

            case "doc":
                holder.ivDocType.setImageResource(R.drawable.doc);
                break;

            case "docx":

                holder.ivDocType.setImageResource(R.drawable.doc);
                break;

            case "apk":
                holder.ivDocType.setImageResource(R.drawable.ic_android_green_24dp);
                break;

            default:

                holder.ivDocType.setImageResource(R.drawable.file);
                break;
        }


        holder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filepath == null || filepath.isEmpty()) {

                    Log.e("file path", "null");
                    Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                } else {

                    if (filetype.equals("pdf")) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        intent.putExtra("docid", taskProcessDocsList.get(position).getDocid());
                        intent.putExtra("workflow", "true");

                        Initializer.globalDynamicSlid = taskProcessDocsList.get(position).getSlid();


                        context.startActivity(intent);

                    } else if (filetype.equals("jpg") || filetype.equals("jpeg") || filetype.equals("png")) {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", "-");
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", "-");
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", taskProcessDocsList.get(position).getDocid());
                        intent.putExtra("workflow", "true");
                        Initializer.globalDynamicSlid = taskProcessDocsList.get(position).getSlid();
                        context.startActivity(intent);

                    } else {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_file_light_blue);
                        Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

                        //Log.e("File not supported","true");

                    }

                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return taskProcessDocsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView docname;
        ImageView ivDocType;
        TextView docPath;
        TextView doctype;
        CardView cardView;
        LinearLayout llmain;


        public ViewHolder(View itemView) {
            super(itemView);

            docname = itemView.findViewById(R.id.tv_task_process_doc_name);
            docname.setPaintFlags(docname.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            doctype = itemView.findViewById(R.id.tv_task_process_doc_type);
            docPath = itemView.findViewById(R.id.tv_task_process_doc_path);
            ivDocType = itemView.findViewById(R.id.iv_task_process_doc_type);
            cardView = itemView.findViewById(R.id.cv_task_in_process_item);
            llmain = itemView.findViewById(R.id.ll_task_in_process_item);

        }
    }
}
