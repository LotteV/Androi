package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.fragments.FutureResFragment;
import com.reservatiesysteem.lotte.reservatiesysteem.fragments.PastResFragment;
import com.reservatiesysteem.lotte.reservatiesysteem.model.ProfileAccount;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Reservation;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 27/02/2017.
 */

public class ReservationsActivity extends BaseActivity {
    @BindView(R.id.vpRes) ViewPager vpRes;
    @BindView(R.id.lytToekomst)LinearLayout lytToekomst;
    @BindView(R.id.lytVerleden)LinearLayout lytVerleden;

    CheckReservationAdapter homePagerAdapter;
    ArrayList<Reservation> reservations = new ArrayList<>();

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccount();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        ButterKnife.bind(this);

        lytToekomst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpRes.setCurrentItem(0);
            }
        });
        lytVerleden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpRes.setCurrentItem(1);
            }
        });

        reservations = (ArrayList<Reservation>) getIntent().getExtras().getSerializable("Reservations");

        getAccount();
        homePagerAdapter = new CheckReservationAdapter(getSupportFragmentManager());
        vpRes.setAdapter(homePagerAdapter);

    }

    private class CheckReservationAdapter extends FragmentStatePagerAdapter {
        private PastResFragment pastResFragment;
        private FutureResFragment futureResFragment;
        private int maxVisibleItems;

        public CheckReservationAdapter(FragmentManager fm) {
            super(fm);
            maxVisibleItems = 2;

            pastResFragment = new PastResFragment();
            futureResFragment = new FutureResFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return futureResFragment;
                case 1: return pastResFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return maxVisibleItems;
        }

        public void notifyFragments(){
            futureResFragment.changes();
        }

    }
    private void getAccount(){
        Activity activity = this;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN,"");

        final API_Service service = API.createService(API_Service.class, token);
        Call<ProfileAccount> call = service.getProfile();
        call.enqueue(new Callback<ProfileAccount>() {
            @Override
            public void onResponse(Call<ProfileAccount> call, Response<ProfileAccount> response) {
                ProfileAccount profileAccount = response.body();
                if(profileAccount!=null){
                    reservations  = profileAccount.getReservations();
                    homePagerAdapter = (CheckReservationAdapter) vpRes.getAdapter();
                    homePagerAdapter.notifyFragments();
                    vpRes.setAdapter(homePagerAdapter);
                }else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    Toast.makeText(getApplicationContext(),"login verlopen, opnieuw inloggen aub",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ProfileAccount> call, Throwable t) {

            }
        });
    }
}
