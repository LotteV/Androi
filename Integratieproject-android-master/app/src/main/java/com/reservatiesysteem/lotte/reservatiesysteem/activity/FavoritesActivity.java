package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.BranchAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.fragments.FavoritesListFragment;
import com.reservatiesysteem.lotte.reservatiesysteem.fragments.SearchFragment;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Branch;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jasper on 27/02/2017.
 */
public class FavoritesActivity extends BaseActivity {
    @BindView(R.id.vwpFavorites)
    ViewPager vwpFavorites;
    @BindView(R.id.lytList)
    LinearLayout lytList;
    @BindView(R.id.lytSearch)
    LinearLayout lytSearch;
    @BindView(R.id.lytBook)
    LinearLayout lytBook;

    FavoritesPageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        adapter = new FavoritesPageAdapter(getSupportFragmentManager());
        vwpFavorites.setAdapter(adapter);

        lytSearch.setEnabled(false);
        lytBook.setEnabled(false);

        lytSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwpFavorites.setCurrentItem(1);
            }
        });
        lytList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwpFavorites.setCurrentItem(0);
            }
        });

        lytBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwpFavorites.setCurrentItem(2);
            }
        });
    }

    public void changeFragment(Fragment fragment, int currentItem) {
        adapter.setMaxVisibleItems(currentItem + 1);
        adapter.setFragment(fragment, currentItem);
        vwpFavorites.setAdapter(adapter);
        vwpFavorites.setCurrentItem(currentItem);
        switch (currentItem) {
            case 0:
                lytList.setEnabled(true);
                lytBook.setEnabled(false);
                break;
            case 1:
                lytList.setEnabled(true);
                lytSearch.setEnabled(true);
                lytBook.setEnabled(false);
                break;
            case 2:
                lytSearch.setEnabled(true);
                lytBook.setEnabled(true);
                break;
        }
    }


    private class FavoritesPageAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private int maxVisibleItems;

        public FavoritesPageAdapter(FragmentManager fm) {
            super(fm);
            maxVisibleItems = 1;
            fragments.add(new FavoritesListFragment());
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
