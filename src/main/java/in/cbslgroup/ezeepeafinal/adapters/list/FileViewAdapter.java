package in.cbslgroup.ezeepeafinal.adapters.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeepeafinal.ui.activity.dms.DmsActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.dms.MoveCopyStorageActivity;
import in.cbslgroup.ezeepeafinal.ui.activity.viewer.FileViewActivity;
import in.cbslgroup.ezeepeafinal.interfaces.CustomItemClickListener;
import in.cbslgroup.ezeepeafinal.model.Foldername;
import in.cbslgroup.ezeepeafinal.R;


public class FileViewAdapter extends RecyclerView.Adapter<FileViewAdapter.ViewHolder> {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Context context;
    private List<Foldername> folderName;
    private CustomItemClickListener listener;

    private int lastPosition = -1;

    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    AlertDialog alertDialog;
    DmsActivity dmsActivity;

    int weight = 3; //number of parts in the recycler view.


    public FileViewAdapter(Context context, List<Foldername> folderName, CustomItemClickListener listener) {

        this.context = context;
        this.folderName = folderName;
        this.listener = listener;
        dmsActivity = (DmsActivity) context;

    }

    @Override
    public FileViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_icon, null);
        // v.getLayoutParams().height = parent.getHeight() / weight;
        return new ViewHolder(v, dmsActivity);


    }

    @Override
    public void onBindViewHolder(final FileViewAdapter.ViewHolder holder, final int position) {

        //logic may be not so effiecient
        String regex = "[0-9]+";

        // fName = folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&"));

        if (folderName.get(position).getFoldername().matches(regex)) {

            if (folderName.get(position).getFoldername().equals("0")) {

                Log.e("files", "no file found");
                holder.lv.setVisibility(View.GONE);
            } else {


                holder.checkBoxDmsHierarcy.setVisibility(View.GONE);
                holder.tvFullFoldername.setText(folderName.get(position).getFoldername() + "&&" + DmsActivity.dynamicFileSlid);
                holder.tvFoldername.setText(folderName.get(position).getFoldername() + " files");
                final String fileName = folderName.get(position).getFoldername() + "&&" + DmsActivity.dynamicFileSlid;
                String slidFile = fileName.substring(fileName.indexOf("&&") + 2, fileName.length());
                holder.tvFolderSlid.setText(slidFile);

                // Here you apply the animation when the view is bound
                setAnimation(holder.itemView, position);
                holder.ivFolder.setImageResource(R.drawable.ic_no_of_files);

                holder.lv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        String slidFile = fileName.substring(fileName.indexOf("&&") + 2, fileName.length());
//                        String fldrname = DmsActivity.foldernameDyanamic;
//                        Log.e("Slid in FileViewAdap", slidFile);
//                        Intent intent = new Intent(context, FileViewActivity.class);
//                        intent.putExtra("slid", slidFile);
//                        intent.putExtra("foldername", fldrname);
//                        context.startActivity(intent);
//                        // Toast.makeText(context,"files",Toast.LENGTH_LONG).show();


                        String slidFile = fileName.substring(fileName.indexOf("&&") + 2);
                        String fldrname = DmsActivity.foldernameDyanamic;
                        Log.e("Slid in FileViewAdap", slidFile);
                        Intent intent = new Intent(context, FileViewActivity.class);
                        intent.putExtra("slid", slidFile);
                        intent.putExtra("foldername", fldrname);
                        intent.putExtra("mode", "dms");
                        ((Activity) context).startActivityForResult(intent, 101);

                    }
                });

                holder.lv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        // String foldername = folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&"));


                        Log.e("longclick", "no long click");


                        return true;
                    }
                });


                if (folderName.get(position).getRootlockfolder().equals("0")) {
                    holder.ivLockFolder.setVisibility(View.GONE);
                } else {
                    holder.ivLockFolder.setVisibility(View.VISIBLE);

                }

            }


        } else {

            // Log.e("foldername",folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&")));
            holder.tvFoldername.setText(folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&")));
            //holder.tvFoldername.setText(folderName.get(position).getFoldername());


            // Changing the folder icon if folder has no child or data

            String checkFolderNull = folderName.get(position).getFoldername().substring(folderName.get(position).getFoldername().lastIndexOf("&&") + 2, folderName.get(position).getFoldername().length());
            Log.e("checknull", checkFolderNull);

            //checkBlank(checkFolderNull,holder);


            if (folderName.get(position).getBlankfolder().equals("1")) {
                holder.ivFolder.setImageResource(R.drawable.ic_folder_light_blue_24dp);
            } else if (folderName.get(position).getBlankfolder().equals("0")) {
                holder.ivFolder.setImageResource(R.drawable.ic_folder_open_blue_24dp);


            }


            if (folderName.get(position).getLockfolder().equals("0")) {
                holder.ivLockFolder.setVisibility(View.GONE);
            } else {
                holder.ivLockFolder.setVisibility(View.VISIBLE);

            }

            // Here you apply the animation when the view is bound
            setAnimation(holder.itemView, position);


            //  checkBlank();


          /*  if(checkFolderNull.equalsIgnoreCase("0")){

                holder.ivFolder.setImageResource(R.drawable.ic_folder_open_blue_24dp);

            }
*/
            /////////////////////////////////////////////////

            holder.lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //view.startAnimation(buttonClick);
                    // String foldrName = folderName.get(position).getFoldername().substring(0,folderName.indexOf("&&"));
                    String slid = folderName.get(position).getFoldername().substring(folderName.get(position).getFoldername().indexOf("&&") + 2, folderName.get(position).getFoldername().length());
                    String fullFolderName = folderName.get(position).getFoldername();
                    listener.onItemClick(view, position, slid, fullFolderName);


                }
            });

            holder.lv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (!folderName.get(position).getLockfolder().equals("0")) {

                        Toast.makeText(context, "Folder is locked", Toast.LENGTH_SHORT).show();

                    } else {

                        final String foldername = folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&"));
                        final String fullfoldername = folderName.get(position).getFoldername();


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.alertdialog_move_copy, null);

                        TextView tvmove = dialogView.findViewById(R.id.alertdialog_tv_move);
                        TextView tvcopy = dialogView.findViewById(R.id.alertdialog_tv_copy);


                        if (DmsActivity.copy_storage.equalsIgnoreCase("1"))
                        {

                            tvcopy.setVisibility(View.VISIBLE);

                        } else {

                            tvcopy.setVisibility(View.GONE);

                        }

                        if (DmsActivity.move_storage.equalsIgnoreCase("1"))
                        {

                            tvmove.setVisibility(View.VISIBLE);

                        }
                        else {

                            tvmove.setVisibility(View.GONE);

                        }


                        tvmove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(context, MoveCopyStorageActivity.class);
                                intent.putExtra("foldername&&slid", fullfoldername);
                                intent.putExtra("mode", "move");
                                context.startActivity(intent);

                                alertDialog.dismiss();


                            }
                        });


                        tvcopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(context, MoveCopyStorageActivity.class);
                                intent.putExtra("foldername&&slid", fullfoldername);
                                intent.putExtra("mode", "copy");
                                context.startActivity(intent);

                                alertDialog.dismiss();

                            }
                        });


                 /*   Button btn_cancel_ok = dialogView.findViewById(R.id.btn_cancel_meta_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });*/

                        dialogBuilder.setView(dialogView);
                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        if(folderName.get(position).getLockfolder().equals("0")){

                            if (DmsActivity.copy_storage.equalsIgnoreCase("1") || DmsActivity.move_storage.equalsIgnoreCase("1")) {

                                Log.e("fileviewadap", "1");
                                alertDialog.show();

                            } else if (DmsActivity.copy_storage.equalsIgnoreCase("1") && DmsActivity.move_storage.equalsIgnoreCase("1")) {

                                Log.e("fileviewadap", "2");
                                alertDialog.show();
                            }

                        }

                        else{

                            Toast.makeText(context, "Folder is Locked", Toast.LENGTH_SHORT).show();
                        }


                    }


                    return true;
                }
            });

           /* if(!dmsActivity.in_action_mode_dms){

                holder.checkBoxDmsHierarcy.setVisibility(View.GONE);
                // Toast.makeText(context, "in action mode true ", Toast.LENGTH_SHORT).show();

            }

            else{


                holder.checkBoxDmsHierarcy.setVisibility(View.VISIBLE);
                holder.checkBoxDmsHierarcy.setChecked(false);

            }

            holder.checkBoxDmsHierarcy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if(isChecked){

                        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

                    }

                    else{

                        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

                    }

                }


            });*/


        }


    }

    @Override
    public int getItemCount() {

        return folderName.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoldername, tvFolderSlid, tvFullFoldername, tvCheckBlank;
        ImageView ivFolder, ivLockFolder;
        LinearLayout lv;
        CheckBox checkBoxDmsHierarcy;
        CardView cardView;

        DmsActivity dmsActivity;

        public ViewHolder(View itemView, DmsActivity dmsActivity) {
            super(itemView);

            this.dmsActivity = dmsActivity;

            cardView = itemView.findViewById(R.id.cv_dms_hierarcy_folder_list);

            tvFoldername = itemView.findViewById(R.id.tv_fileview_foldername);
            ivFolder = itemView.findViewById(R.id.iv_fileview_foldericon);
            ivLockFolder = itemView.findViewById(R.id.iv_fileview_lock);
            lv = itemView.findViewById(R.id.linearlayoutclickedinlist);
            tvFolderSlid = itemView.findViewById(R.id.tv_fileview_folder_slid);
            tvFullFoldername = itemView.findViewById(R.id.tv_fileview_folder_fullfoldername);
            checkBoxDmsHierarcy = itemView.findViewById(R.id.checkbox_dms_hierarcy);
            tvCheckBlank = itemView.findViewById(R.id.tv_fileview_folder_checkblank);


         /*   if(!dmsActivity.in_action_mode){

                checkBoxDmsHierarcy.setVisibility(View.GONE);

                //cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

            }

            else{


            checkBoxDmsHierarcy.setVisibility(View.VISIBLE);
                // holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
               checkBoxDmsHierarcy.setChecked(false);
              // notifyDataSetChanged();
            }
*/

        /*    checkBoxDmsHierarcy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){

                       cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                    }

              else{

                       cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

                    }

                }
            });*/


        }


    }

    @Override
    public int getItemViewType(int position) {
        if (isSwitchView) {
            return LIST_ITEM;
        } else {
            return GRID_ITEM;
        }
    }

    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }


    private static void disable(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disable((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

}



