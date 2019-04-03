package com.reservatiesysteem.lotte.reservatiesysteem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Branch;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Reservation;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 26/02/2017.
 */

public class ReservationAdapter extends BaseAdapter{

    private List<Reservation> reservations = new ArrayList<>();
    private Context context;

    public ReservationAdapter(Context context, int resource, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Reservation getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Reservation reservation = getItem(position);

        final View v;

        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_reservation_entry, parent, false);
        }
        else {
            v = convertView;
        }

        final TextView txtResBranchName = (TextView) v.findViewById(R.id.txtBranchName);
        TextView txtResDate = (TextView) v.findViewById(R.id.txtResDate);
        final TextView txtResAdress = (TextView) v.findViewById(R.id.txtBranchAdress);
        TextView txtResAmount = (TextView) v.findViewById(R.id.txtResAmount);
        final TextView lblHiddenId = (TextView)v.findViewById(R.id.lblHiddenId);

        String[] dateTime = reservation.getDateTime().split("T");
        String resDate = dateTime[0];
        String resTime = dateTime[1].substring(0,5);

        //api call
        API_Service service = API.createService(API_Service.class);
        Call<Branch> call = service.getBranchDetails(reservation.getBranchId());
        call.enqueue(new Callback<Branch>() {
            @Override
            public void onResponse(Call<Branch> call, Response<Branch> response) {
                Branch branch = response.body();
                if(branch!=null){
                    txtResBranchName.setText(branch.getName());
                    txtResAdress.setText(branch.getStreet() + " " + branch.getNumber() + ",\n" + branch.getCity().getPostalCode() + " " + branch.getCity().getName());
                }
            }

            @Override
            public void onFailure(Call<Branch> call, Throwable t) {

            }
        });

        lblHiddenId.setText(reservation.getId()+"");
        txtResDate.setText(resDate + " om " + resTime);
        txtResAmount.setText(String.valueOf(reservation.getAmount()));


        return v;
    }
}
