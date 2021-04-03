package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.viewer.ImageViewerActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.PdfViewerActivity;
import in.cbslgroup.ezeepeafinal.model.DocumentListWorkflow;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import in.cbslgroup.ezeepeafinal.utils.Initializer;

public class WorkflowDocListAdapter extends RecyclerView.Adapter<WorkflowDocListAdapter.ViewHolder> {



    List<DocumentListWorkflow> documentListWorkflow;
    Context context;

    ProgressDialog mProgressDialog;
    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;

    public WorkflowDocListAdapter(List<DocumentListWorkflow> documentListWorkflow, Context context) {
        this.documentListWorkflow = documentListWorkflow;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workflow_document_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DocumentListWorkflow documentList = documentListWorkflow.get(position);

        holder.tvFilename.setText(documentList.getDocname());
        holder.tvExtension.setText(documentList.getExtension());
        holder.tvPath.setText(documentList.getDocpath());
        holder.tvDocid.setText(documentList.getDocid());

        String filetype = holder.tvExtension.getText().toString().trim();
        String filename = holder.tvFilename.getText().toString().trim();
        String filepath = holder.tvPath.getText().toString().trim();

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

            case "doc":
                holder.ivFiletype.setImageResource(R.drawable.doc);

                break;

            case "apk":
                holder.ivFiletype.setImageResource(R.drawable.ic_android_green_24dp);

                break;

            case "docx":
                holder.ivFiletype.setImageResource(R.drawable.doc);

                break;
            case "jpeg":
                holder.ivFiletype.setImageResource(R.drawable.jpeg);

                break;


            case "tiff":

                holder.ivFiletype.setImageResource(R.drawable.tiff_png);


                break;
            case "mp4":
                holder.ivFiletype.setImageResource(R.drawable.mp4);


                break;
            case "3gp":

                break;
            default:
                holder.ivFiletype.setImageResource(R.drawable.file);

                break;
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
                        intent.putExtra("docid", documentList.getDocid());
                        intent.putExtra("workflow","true");

                        Initializer.globalDynamicSlid = documentList.getSlid();

                        Log.e("doc path list pdf",filepath);


                        context.startActivity(intent);

                    } else if (filetype.equals("jpg") || filetype.equals("jpeg") || filetype.equals("png")) {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", "-");
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", "-");
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", documentList.getDocid());
                        intent.putExtra("workflow", "true");


                        Initializer.globalDynamicSlid = documentList.getSlid();

                        Log.e("doc path list pic",filepath);


                        context.startActivity(intent);

                    } else {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_file_light_blue);
                        //Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Download File");
                        alertDialog.setIcon(R.drawable.file);
                        alertDialog.setMessage("This is file is not supported instead of this you can download the file so do you want to download"+" "+filename+"."+filetype+"?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        Log.e("doc path list",DOCUMENT_URL+filepath);

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



                                        new DownloadFile(context).execute(DOCUMENT_URL+filepath);




                                    }
                                });
                        alertDialog.show();



                        //Log.e("File not supported","true");

                    }

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return documentListWorkflow.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPath, tvExtension, tvFilename, tvDocid;
        CardView cardView;
        ImageView ivFiletype;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cv_bottom_sheet_workflow_doclist_item);
            tvFilename = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_filename);
            tvExtension = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_fileextension);
            tvPath = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_filepath);
            ivFiletype = itemView.findViewById(R.id.iv_bottom_sheet_workflow_doc_filetype);
            tvDocid = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_docid);


        }
    }



/*    public static class DownloadService extends IntentService {
        public static final int UPDATE_PROGRESS = 8344;


        public DownloadService(){
            super("DownloadService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String urlToDownload = intent.getStringExtra("url");
            String filename = intent.getStringExtra("filename");
            String type = intent.getStringExtra("type");
            ResultReceiver receiver = intent.getParcelableExtra("receiver");
            try {
                OutputStream output;

                URL url = new URL(urlToDownload);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(connection.getInputStream());

                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeFile Downloads");

                if(!myDirectory.exists()) {
                    myDirectory.mkdirs();

                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+filename+"."+type);
                }
                else{
                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+filename+"."+type);
                }


                *//*OutputStream output = new FileOutputStream("/sdcard/BarcodeScanner-debug.apk");*//*

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    Bundle resultData = new Bundle();
                    resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                    receiver.send(UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bundle resultData = new Bundle();
            resultData.putInt("progress" ,100);
            receiver.send(UPDATE_PROGRESS, resultData);
        }
    }

    private  class DownloadReceiver extends ResultReceiver{

        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == PdfViewerActivity.DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");


                mNotifyManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context);

                mBuilder.setContentTitle("File Download")
                        .setContentText("Download in progress")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setProgress(100,progress,false)
                        .setAutoCancel(true)

                ;
                mNotifyManager.notify(0, mBuilder.build());

                PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        getClass().getName());
                wakeLock.acquire();

                //mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    Log.e("wakelock","working 1");
                    wakeLock.release();

                    Toast.makeText(context, "File Download Successfully", Toast.LENGTH_SHORT).show();
                    mNotifyManager.cancel(0);
            *//*        mBuilder.setContentTitle("File Download Successfully")
                            .setContentText("")
                            .setColor(getResources().getColor(R.color.colorPrimary))
                            .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                            //.setProgress(0,0,false)
                    ;
                    mNotifyManager.notify(0, mBuilder.build());*//*

                    Log.e("wakelock","working 2");

                    //mProgressDialog.dismiss();


                }
            }
        }
    }*/

    //Download file from server
    public class DownloadFile extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;

        public DownloadFile(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;


            String filename = sUrl[0].substring(sUrl[0].lastIndexOf("/"),sUrl[0].length());
            String  filetype = filename.substring(filename.lastIndexOf("."),filename.length());
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeProcess Downloads");

                if(!myDirectory.exists()) {
                    myDirectory.mkdirs();

                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/"+filename+"."+filetype);
                }
                else{
                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/"+filename+"."+filetype);
                }


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

           /* mNotifyManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setContentTitle("File Download")
                    .setContentText("Download in progress")
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_file_download_white_24dp)*/
                     ;


            //Toast.makeText(context, "Downloading the file... The download progress is on notification bar.", Toast.LENGTH_LONG).show();

            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);

           /* mBuilder.setProgress(100, progress[0], false);
            // Displays the progress bar on notification
            mNotifyManager.notify(0, mBuilder.build());*/

        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Download error", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"File downloaded Succesfully", Toast.LENGTH_SHORT).show();
           /* mBuilder.setContentTitle("File Download Succesfully")
                     .setContentText("")
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_file_download_white_24dp);

             mNotifyManager.notify(0, mBuilder.build());*/


        }
    }







}
