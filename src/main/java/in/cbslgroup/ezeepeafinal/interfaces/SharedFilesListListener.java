package in.cbslgroup.ezeepeafinal.interfaces;

import android.view.View;

public interface SharedFilesListListener {

    void onUndoButtonClick(View v, final String docid, final int position, final String toIds);
}
