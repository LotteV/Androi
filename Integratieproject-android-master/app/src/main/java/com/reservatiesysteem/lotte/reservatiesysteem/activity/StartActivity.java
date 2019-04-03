package com.reservatiesysteem.lotte.reservatiesysteem.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.fragments.SearchFragment;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends BaseActivity {
    @BindView(R.id.vwpHome)
    ViewPager viewPager;
    @BindView(R.id.lytSearch)
    LinearLayout lytSearch;
    @BindView(R.id.lytList)
    LinearLayout lytList;
    @BindView(R.id.lytDetails)
    LinearLayout lytDetails;
    @BindView(R.id.lytBook)
    LinearLayout lytBook;
    HomePagerAdapter homePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homePagerAdapter);

        setTitle("Leisurebooker");

        lytList.setEnabled(false);
        lytDetails.setEnabled(false);
        lytBook.setEnabled(false);


        lytSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        lytList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        lytDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
        lytBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3);
            }
        });
    }

    public void changeFragment(Fragment fragment, int currentItem) {
        homePagerAdapter.setMaxVisibleItems(currentItem + 1);
        homePagerAdapter.setFragment(fragment, currentItem);
        viewPager.setAdapter(homePagerAdapter);
        viewPager.setCurrentItem(currentItem);
        switch (currentItem) {
            case 0:
                lytSearch.setEnabled(true);
                break;
            case 1:
                lytList.setEnabled(true);
                lytDetails.setEnabled(false);
                lytBook.setEnabled(false);
                break;
            case 2:
                lytDetails.setEnabled(true);
                lytBook.setEnabled(false);
                break;
            case 3:
                lytBook.setEnabled(true);
                break;
        }
    }


    private class HomePagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private int maxVisibleItems;

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
            maxVisibleItems = 1;

            fragments.add(new SearchFragment());
            fragments.add(new Fragment());
            fragments.add(new Fragment());
            fragments.add(new Fragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return maxVisibleItems;
        }

        public void setMaxVisibleItems(int maxVisibleItems) {
            this.maxVisibleItems = maxVisibleItems;
        }

        public void setFragment(Fragment fragment, int id) {
            fragments.set(id, fragment);
        }


    }
}
