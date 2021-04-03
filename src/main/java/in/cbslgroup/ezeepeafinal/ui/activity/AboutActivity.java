package in.cbslgroup.ezeepeafinal.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.cbslgroup.ezeepeafinal.BuildConfig;
import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;

public class AboutActivity extends AppCompatActivity {

    TextView tvVersion,tvCopyRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initLocale();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        Date date = Calendar.getInstance().getTime();
        Log.e("date", date.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String formattedDate = simpleDateFormat.format(date);
        Log.e("formatteddate", formattedDate);

        tvVersion = findViewById(R.id.tv_about_version_name);
        tvVersion.setText(getString(R.string.version)+" "+ BuildConfig.VERSION_NAME);

        tvCopyRight = findViewById(R.id.tv_about_CopyRight);
        tvCopyRight.setText(getString(R.string.copyright) +" © " + formattedDate+" "+ "CBSL Group."+ getString(R.string.all_rights_reserved));


    }

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(this, null);
        if (lang == null) {

            LocaleHelper.persist(this, "en");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }
}
