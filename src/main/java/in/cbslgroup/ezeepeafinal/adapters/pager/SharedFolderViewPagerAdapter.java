package in.cbslgroup.ezeepeafinal.adapters.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SharedFolderViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SharedFolderViewPagerAdapter(@NonNull FragmentManager fm) {

        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);

//        Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = new SharedFolderFragment();
//                break;
//            case 1:
//                fragment = new SharedFolderWithMeFragment();
//                break;
//        }
//        return fragment;
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
//        switch (position) {
//            case 0:
//                return "Shared Folder by Me";
//            case 1:
//                return "Shared Folder with Me";
//
//        }
//        return null;
    }

    public void addFragment(Fragment fragment ,String title){


        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }

}

