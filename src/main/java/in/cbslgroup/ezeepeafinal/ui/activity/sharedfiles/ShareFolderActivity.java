package in.cbslgroup.ezeepeafinal.ui.activity.sharedfiles;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.adapters.pager.SharedFolderViewPagerAdapter;
import in.cbslgroup.ezeepeafinal.ui.activity.MainActivity;
import in.cbslgroup.ezeepeafinal.ui.fragments.sharefolder.SharedFolderFragment;
import in.cbslgroup.ezeepeafinal.ui.fragments.sharefolder.SharedFolderWithMeFragment;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;

public class ShareFolderActivity extends AppCompatActivity {

    private SharedFolderViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_folder);

        initLocale();

        Toolbar toolbar =  findViewById(R.id.toolbar_shared_folder);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();

            }
        }));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SharedFolderViewPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.view_pager_shared_folder);

        if(MainActivity.shareByMe.equalsIgnoreCase("1")){

            mSectionsPagerAdapter.addFragment(new SharedFolderFragment(),getString(R.string.shared_folder_by_me));

        }

        if(MainActivity.shareWithMe.equalsIgnoreCase("1")){

            mSectionsPagerAdapter.addFragment(new SharedFolderWithMeFragment(),getString(R.string.shared_folder_with_me));

        }

        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_shared_folder);
        tabLayout.setupWithViewPager(mViewPager);

    }



}