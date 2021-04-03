package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.viewer.FileViewActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeepeafinal.interfaces.onFileClick;
import in.cbslgroup.ezeepeafinal.model.Version;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;


public class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.ViewHolder> {

    List<Version> versionList;
    Context context;
    onFileClick listener;

    ProgressDialog mProgressDialog;
    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;

    DownloadManager downloadManager;
    AlertDialog alertDialog;
    OnButtonClickListener onButtonClickListener;


    String delversion, pdfView, viewmetadata;

    public VersionAdapter(List<Version> versionList, Context context) {
        this.versionList = versionList;
        this.context = context;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }


    interface OnButtonClickListener {

        void onFileViewButtonClick(String path, String filetype, String filename, String docid, String docpath, int pos);

        void onDeleteVersionButtonClick(String docid, int pos);

        void onMetaViewButtonClick(String docname, String docid);

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewfiletype) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.versionlist_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        if (position == versionList.size() - 1) {

            holder.decorator.setVisibility(View.GONE);
        }

        holder.tvVersion.setText(versionList.get(position).getDocVersionName());
        holder.tvDocname.setText(versionList.get(position).getDocName());
        holder.tvDocid.setText(versionList.get(position).getDocId());
        holder.tvpath.setText(versionList.get(position).getDocpath());

        Log.e("version_class", context.getClass().getSimpleName());

        if (context.getClass().getSimpleName().equalsIgnoreCase("FileViewActivity")) {

            delversion = FileViewActivity.delversion;
            pdfView = FileViewActivity.pdfView;
            viewmetadata = FileViewActivity.viewMetadata;

        } else if (context.getClass().getSimpleName().equalsIgnoreCase("MetaSearchFileViewActivity")) {

            delversion = MetaSearchFileViewActivity.delversion;
            pdfView = MetaSearchFileViewActivity.pdfView;
            viewmetadata = MetaSearchFileViewActivity.viewMetadata;

        }


        if (delversion.equalsIgnoreCase("1")) {

            holder.ivDelVer.setVisibility(View.VISIBLE);

        } else {

            holder.ivDelVer.setVisibility(View.GONE);
        }

        if (pdfView.equalsIgnoreCase("1")) {

            holder.ivView.setVisibility(View.VISIBLE);

        } else {

            holder.ivView.setVisibility(View.GONE);
        }

        if (viewmetadata.equalsIgnoreCase("1")) {

            holder.ivMetaView.setVisibility(View.VISIBLE);

        } else {

            holder.ivMetaView.setVisibility(View.GONE);
        }


        holder.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //listener.onDelVersionBtnClick(v,position,holder.tvDocid.getText().toString());
                String path = holder.tvpath.getText().toString();
                String filetype = path.substring(path.lastIndexOf(".") + 1);
                String filename = holder.tvDocname.getText().toString();
                String docid = versionList.get(position).getDocId();
                String docpath = versionList.get(position).getDocpath();

                onButtonClickListener.onFileViewButtonClick(path, filetype, filename, docid, docpath, position);


            }
        });

        holder.ivDelVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String docid = versionList.get(position).getDocId();
                onButtonClickListener.onDeleteVersionButtonClick(docid, position);


            }
        });


        holder.ivMetaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String slid = versionList.get(position).getDocSlid();
                String docid = versionList.get(position).getDocId();

                onButtonClickListener.onMetaViewButtonClick(slid, docid);

            }
        });


    }

    @Override
    public int getItemCount() {
        return versionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvVersion, tvDocname, tvDocid, tvpath;
        ImageView ivView, ivDelVer, ivMetaView;
        View decorator;

        public ViewHolder(View itemView) {
            super(itemView);

            tvVersion = itemView.findViewById(R.id.tv_version);
            tvDocid = itemView.findViewById(R.id.tv_version_docid);
            tvDocname = itemView.findViewById(R.id.tv_version_docname);
            tvpath = itemView.findViewById(R.id.tv_version_docpath);
            ivView = itemView.findViewById(R.id.iv_version_view);
            ivDelVer = itemView.findViewById(R.id.iv_version_del);
            ivMetaView = itemView.findViewById(R.id.iv_version_view_meta);
            decorator = itemView.findViewById(R.id.view_version_decorator);

        }
    }

    //Downloading file
    public void DownloadFile(String url) {

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//
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


}