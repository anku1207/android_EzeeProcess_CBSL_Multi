package in.cbslgroup.ezeepeafinal.interfaces;

import android.view.View;

import in.cbslgroup.ezeepeafinal.model.Recyclebin;

public interface RecycleBinListListener {

    void onRestoreButtonClick(View v, final String docid, final int position , Recyclebin rvObj);

    void onPerDeleteButtonClick(View v, final String docid, final int position, Recyclebin rvObj);



}
