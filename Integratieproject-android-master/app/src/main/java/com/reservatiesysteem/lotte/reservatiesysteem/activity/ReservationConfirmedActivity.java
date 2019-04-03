package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.fragments.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lotte on 24/02/2017.
 */

public class ReservationConfirmedActivity extends BaseActivity{

    @BindView(R.id.branchNameRes) TextView txtBranchName;
    @BindView(R.id.dateRes) TextView txtDateRes;
    @BindView(R.id.timeRes) TextView txtTimeRes;
    @BindView(R.id.numberRes) TextView txtNumberRes;
    @BindView(R.id.btnCalendar) Button btnCalendar;
    @BindView(R.id.btnBack) Button btnBack;

    private String receivedBranchName;
    private String receivedDate;
    private String recievedTime;
    private String receivedAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmreservation);
        ButterKnife.bind(this);

        //transfer data from reservationFragment
        Intent intent = getIntent();
        receivedBranchName = intent.getStringExtra("branchName");
        receivedDate = intent.getStringExtra(SearchFragment.CHOSEN_DATE);
        recievedTime = intent.getStringExtra(SearchFragment.CHOSEN_TIME);
        receivedAmount = intent.getStringExtra(SearchFragment.CHOSEN_NUMBEROFPERSONS);

        txtBranchName.setText(receivedBranchName);
        txtDateRes.setText(receivedDate);
        txtTimeRes.setText(recievedTime);
        txtNumberRes.setText(receivedAmount + " personen");

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCalendarEvent();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
            }
        });
    }

    private void addCalendarEvent(){
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        String[] date = receivedDate.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        String[] time = recievedTime.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        beginTime.set(year, month-1, day, hour, minute);
        endTime.set(year, month-1, day, hour+2, minute);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Reservatie " + receivedBranchName)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(intent);
    }
}
