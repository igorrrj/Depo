package com.example.igor.depo;

/**
 * Created by Igor on 19.10.2016.
 */


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class IntersityTransportTabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int int_items = 2 ;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            /**
             *Inflate tab_layout and setup Views.
             */


            View x =  inflater.inflate(R.layout.public_transport_tab_layout,null);

            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);
            tabLayout.setupWithViewPager(viewPager);

            TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            tabOne.setText("Трамвай");
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            tabTwo.setText("Міський автобус");
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
            tabLayout.getTabAt(1).setCustomView(tabTwo);
        /**
         *Set an Apater for the View Pager
         */
            setupViewPager(viewPager);
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */


        return x;

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new Tram(), "Трамвай");
        adapter.addFrag(new CityBus(), "Міський автобус");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}


