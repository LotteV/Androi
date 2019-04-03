package com.reservatiesysteem.lotte.reservatiesysteem.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;


import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.StartActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.CityArrayAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.model.City;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class SearchFragment extends Fragment {
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtTime)
    TextView txtTime;
    @BindView(R.id.lblError)
    TextView lblError;
    @BindView(R.id.btnReserveer)
    Button btnReserveer;
    @BindView(R.id.btnTime)
    ImageView btnTime;
    @BindView(R.id.btnDate)
    ImageView btnDate;

    @BindView(R.id.searchCity)
    AutoCompleteTextView txtSearchCity;
    @BindView(R.id.numberPersons)
    EditText numberOfPersons;

    private final int TIME_PICKER_INTERVAL = 30;
    private int year, month, day, hour, minute;
    private Calendar calendar;
    private CityArrayAdapter cityAdapter ;
    private List<City> cities;

    //transfer data to next fragment
    Bundle bundle = new Bundle();
    //public static final String CHOSEN_POSTALCODE = "chosenPostalCode";
    public static final String CHOSEN_DATE = "chosenDate";
    public static final String CHOSEN_TIME = "chosenTime";
    public static final String CHOSEN_NUMBEROFPERSONS = "chosenNumberOfPersons";


    public SearchFragment() {
        getCities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        btnReserveer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity activity = (StartActivity) getActivity();

                //transfering data to ResultListFragment
                ResultListFragment resultListFragment = new ResultListFragment();


                String postalCode = txtSearchCity.getText().toString();
                int chosenPostalCode = 0;
                if(postalCode.matches(".*- \\d{4}")){
                    chosenPostalCode = Integer.parseInt(postalCode.split("- ")[1]);
                }else if (postalCode.matches("\\d{4}")){
                    chosenPostalCode = Integer.parseInt(postalCode);
                }else {
                    for(City city:cities){
                        if(postalCode.toLowerCase().equals(city.getName().toLowerCase())){
                            chosenPostalCode= Integer.parseInt(city.getPostalCode());
                        }
                    }
                }

                if(chosenPostalCode!=0){
                    bundle.putInt("chosenPostalCode", chosenPostalCode);
                    bundle.putString(CHOSEN_DATE, txtDate.getText().toString());
                    bundle.putString(CHOSEN_TIME, txtTime.getText().toString());
                    bundle.putString(CHOSEN_NUMBEROFPERSONS, numberOfPersons.getText().toString());
                    resultListFragment.setArguments(bundle);

                    activity.changeFragment(resultListFragment,1);
                }else {
                    lblError.setText("Gemeente incorrect, gelieve 1 uit de lijst te kiezen");
                }


            }
        });

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(getContext(), myDateListener, year, month, day);
                dpd.show();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
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

    private void getCities() {
        API_Service service = API.createService(API_Service.class);
        Call<List<City>> call = service.getCities();
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if(response.isSuccessful()){
                    cities = response.body();
                    cityAdapter = new CityArrayAdapter(getContext(),android.R.layout.simple_dropdown_item_1line,cities);
                    cityAdapter.notifyDataSetChanged();
                    txtSearchCity.setAdapter(cityAdapter);
                    txtSearchCity.setThreshold(2);
                }else {
                    lblError.setText("Gemeentes niet correct opgehaald");
                }

            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.d("Error getCity", t.getMessage());
            }
        });
    }
}
