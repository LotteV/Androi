package com.reservatiesysteem.lotte.reservatiesysteem.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.ReservationConfirmedActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.CityArrayAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Branch;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jasper on 01/03/2017.
 */

public class FavoritesReservationInfoFragment extends Fragment {
    @BindView(R.id.lytTime)
    LinearLayout lytTime;
    @BindView(R.id.lytDate)
    LinearLayout lytDate;
    @BindView(R.id.btnReserveer)
    Button btnReserveer;
    @BindView(R.id.numberPersons)
    EditText numberOfPersons;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtTime)
    TextView txtTime;

    private final int TIME_PICKER_INTERVAL = 30;
    private int year, month, day, hour, minute;
    private Calendar calendar;
    private CityArrayAdapter cityAdapter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_res_info, container, false);
        ButterKnife.bind(this,view);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        btnReserveer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReservation();
            }
        });

        lytDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(getContext(), myDateListener, year, month, day);
                dpd.show();
            }
        });
        lytTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), myTimeListener, hour, minute, true);
                timePickerDialog.show();
            }
        });

        showDate(year, month, day);
        showTime(hour, minute);
        return view;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            showDate(year, month, dayOfMonth);
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay, minute);
        }
    };

    private void showDate(int year, int month, int day) {
        txtDate.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
    }

    private void showTime(int hour, int minute) {
        minute = getRoundedMinute(minute);
        if (minute < 10) {
            txtTime.setText(new StringBuilder().append(hour).append(":0").append(minute));
        } else {
            txtTime.setText(new StringBuilder().append(hour).append(":").append(minute));
        }
    }

    public int getRoundedMinute(int minute) {
        if (minute % TIME_PICKER_INTERVAL != 0) {
            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
            if (minute == 60)
                minute = 0;
        }
        return minute;
    }

    public void createReservation() {

        Activity activity = getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN,"");

        final Bundle bundle = getArguments();
        final int chosenBranchId = bundle.getInt("branchId");
        final String branchName = bundle.getString("branchName");
        final String chosenNumberOfPersons = numberOfPersons.getText().toString();

        final StringBuilder dateTime = new StringBuilder();
        dateTime.append(txtDate.getText().toString());
        dateTime.append("T");
        dateTime.append(txtTime.getText().toString());
        dateTime.append(":00");



        API_Service service = API.createService(API_Service.class, token);
        Call<Object> call = service.createReservation(chosenBranchId, String.valueOf(dateTime), Integer.parseInt(chosenNumberOfPersons));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(getActivity(), ReservationConfirmedActivity.class);
                    i.putExtra("branchName", branchName);
                    i.putExtra(SearchFragment.CHOSEN_DATE, txtDate.getText().toString());
                    i.putExtra(SearchFragment.CHOSEN_TIME, txtTime.getText().toString());
                    i.putExtra(SearchFragment.CHOSEN_NUMBEROFPERSONS, chosenNumberOfPersons);
                    i.putExtra("branchId", chosenBranchId);

                    getActivity().finish();
                    startActivity(i);

                    Toast.makeText(getContext(), "Reservatie gelukt", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), "geen plaats of gesloten", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Error creating res", t.getMessage());
            }
        });
    }

}
