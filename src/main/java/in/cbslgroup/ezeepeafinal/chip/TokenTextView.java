package in.cbslgroup.ezeepeafinal.chip;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import in.cbslgroup.ezeepeafinal.R;


@SuppressLint("AppCompatCustomView")
public class TokenTextView extends TextView {
    public TokenTextView(Context context) {
        super(context);
    }

    public TokenTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.drawable.ic_cancel_red_24dp : 0, 0);
        setCompoundDrawablePadding(15);
    }
}
