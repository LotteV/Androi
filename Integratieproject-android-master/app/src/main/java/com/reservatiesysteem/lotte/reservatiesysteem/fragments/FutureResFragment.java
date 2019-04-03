package com.reservatiesysteem.lotte.reservatiesysteem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.ReservationConfirmedActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.ReservationDetailActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.ReservationsActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.ReservationAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lotte on 27/02/2017.
 */

public class FutureResFragment extends Fragment {
    @BindView(R.id.titleRes) TextView txtTitle;
    @BindView(R.id.listReservations) ListView lvReservations;
    @BindView(R.id.empty) TextView txtEmpty;
    ArrayList<Reservation> reservations = new ArrayList<>();
    ArrayList<Reservation> futureReservations;
    ReservationAdapter reservationAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkres, container, false);
        ButterKnife.bind(this, view);

        txtTitle.setText("Toekomstige reservaties");

        ReservationsActivity reservationsActivity = (ReservationsActivity) getActivity();
        reservations = reservationsActivity.getReservations();

        try {
            checkFutureRes();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void checkFutureRes() throws ParseException {
        ReservationsActivity reservationsActivity = (ReservationsActivity) getActivity();
        reservations = reservationsActivity.getReservations();


        futureReservations = new ArrayList<>();

        Date now = new Date();

        for (Reservation reservation: reservations){
            String dateTime = reservation.getDateTime();
            String[] dateTimeArray = dateTime.split("T");
            String date = dateTimeArray[0];
            String time = dateTimeArray[1];

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.UK);
            Date dateRes = dateFormat.parse(date + " " + time);

            //cancelled reservaties niet weergeven
            if(dateRes.after(now)){
                if(!reservation.isCancelled()) {
                    futureReservations.add(reservation);
                }
            }
        }

        //reservaties van user bekijken
        reservationAdapter = new ReservationAdapter(getContext(), R.layout.view_reservation_entry, futureReservations);

        lvReservations.setAdapter(reservationAdapter);
        lvReservations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ReservationDetailActivity.class);
                Bundle bundle = new Bundle();
                TextView txtBranchName = (TextView) view.findViewById(R.id.txtBranchName);
                TextView txtBranchAddress = (TextView) view.findViewById(R.id.txtBranchAdress);
                TextView txtPersonCount = (TextView)view.findViewById(R.id.txtResAmount);

                bundle.putSerializable("reservation", futureReservations.get(i));
                bundle.putString("branchName",txtBranchName.getText().toString());
                bundle.putString("branchAddress",txtBranchAddress.getText().toString());
                bundle.putString("personCount",txtPersonCount.getText().toString()+" personen");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        txtEmpty.setText(R.string.emptyFutureReservations);
        lvReservations.setEmptyView(txtEmpty);
        lvReservations.setAdapter(reservationAdapter);

    }

    public void changes() {
        try {
            checkFutureRes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
