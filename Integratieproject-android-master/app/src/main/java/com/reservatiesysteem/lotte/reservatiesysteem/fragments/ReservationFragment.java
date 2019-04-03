package com.reservatiesysteem.lotte.reservatiesysteem.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.ReservationConfirmedActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 24/02/2017.
 */

public class ReservationFragment extends Fragment {

    @BindView(R.id.branchNameRes)
    TextView txtBranchNameRes;
    @BindView(R.id.dateRes)
    TextView txtDateRes;
    @BindView(R.id.timeRes)
    TextView txtTimeRes;
    @BindView(R.id.numberRes)
    TextView txtNumberRes;

    @BindView(R.id.btnConfirmRes)
    Button btnConfirmRes;

    //transfer data from searchfragment
    private String chosenDate = "";
    private String chosenTime = "";
    private String chosenNumberOfPersons = "";
    private String branchName = "";
    private int chosenBranchId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //transfer data from searchfragment
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            chosenDate = bundle.getString(SearchFragment.CHOSEN_DATE);
            chosenTime = bundle.getString(SearchFragment.CHOSEN_TIME);
            chosenNumberOfPersons = bundle.getString(SearchFragment.CHOSEN_NUMBEROFPERSONS);
            branchName = bundle.getString("branchName");
            chosenBranchId = bundle.getInt("branchId", 0);
        }

        txtBranchNameRes.setText(branchName);
        txtDateRes.setText(chosenDate);
        txtTimeRes.setText(chosenTime);
        txtNumberRes.setText(chosenNumberOfPersons + " personen");

        btnConfirmRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createReservation();
            }
        });

        return view;
    }

    public void createReservation() {

        Activity activity = getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN,"");

        final StringBuilder dateTime = new StringBuilder();
        dateTime.append(chosenDate);
        dateTime.append("T");
        dateTime.append(chosenTime);
        dateTime.append(":00");

        API_Service service = API.createService(API_Service.class, token);
        Call<Object> call = service.createReservation(chosenBranchId, String.valueOf(dateTime), Integer.parseInt(chosenNumberOfPersons));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(getActivity(), ReservationConfirmedActivity.class);
                    i.putExtra("branchName", branchName);
                    i.putExtra(SearchFragment.CHOSEN_DATE, chosenDate);
                    i.putExtra(SearchFragment.CHOSEN_TIME, chosenTime);
                    i.putExtra(SearchFragment.CHOSEN_NUMBEROFPERSONS, chosenNumberOfPersons);
                    i.putExtra("branchId", chosenBranchId);

                    getActivity().finish();
                    startActivity(i);

                    Toast.makeText(getContext(), "Reservatie gelukt", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), "Reservatie failed: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Error creating res", t.getMessage());
            }
        });
    }
}
