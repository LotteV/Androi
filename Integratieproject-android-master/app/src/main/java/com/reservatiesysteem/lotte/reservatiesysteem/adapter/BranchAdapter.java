package com.reservatiesysteem.lotte.reservatiesysteem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Branch;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 15/02/2017.
 */

public class BranchAdapter extends BaseAdapter {

    private List<Branch> branches = new ArrayList<>();
    private int amount;
    private String dateTime;
    private Context context;
    private String defaultUrl = "http://leisurebooker.azurewebsites.net/Content/bowling.jpg";


    public BranchAdapter(Context context, int amount,String dateTime, List<Branch> branches) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.branches = branches;
        this.context = context;
    }

    @Override
    public int getCount() {
        return branches.size();
    }

    @Override
    public Branch getItem(int position) {
        return branches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return branches.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Branch branch = getItem(position);

        final View v;

        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_branch_entry, parent, false);
        }
        else {
            v = convertView;
        }

        final TextView txtAvailable = (TextView) v.findViewById(R.id.lblAvailable);

        if(dateTime!=null){
            API_Service service = API.createService(API_Service.class);
            Call<Branch> call = service.getBranchAvailability(branch.getId(),dateTime,amount);
            call.enqueue(new Callback<Branch>() {
                @Override
                public void onResponse(Call<Branch> call, Response<Branch> response) {

                    Branch returnBranch = response.body();
                    if(returnBranch!=null){
                        if(returnBranch.isAvailable()){
                            txtAvailable.setText("Beschikbaar");
                            txtAvailable.setTextColor(Color.GREEN);
                        }else {
                            txtAvailable.setText("Niet beschikbaar");
                            txtAvailable.setTextColor(Color.RED);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Branch> call, Throwable t) {

                }
            });
        }else {
            txtAvailable.setVisibility(View.GONE);
        }


        TextView txtBranchName = (TextView) v.findViewById(R.id.branchName);
        TextView txtBranchStreet = (TextView) v.findViewById(R.id.branchStreet);
        TextView txtBranchMail = (TextView) v.findViewById(R.id.branchMail);
        ImageView imgBranch = (ImageView) v.findViewById(R.id.imgBranch);

        if (branch.getPicture() == null){
            Picasso.with(context).load(defaultUrl).resize(80,80).into(imgBranch);
        }else {
            Picasso.with(context).load("http://leisurebooker.azurewebsites.net/"+branch.getPicture()).resize(80,80).into(imgBranch);
        }

        txtBranchName.setText(branch.getName());
        txtBranchStreet.setText(branch.getStreet() + " " + branch.getNumber() + ", " + branch.getCity().getPostalCode() + " " + branch.getCity().getName() );
        txtBranchMail.setText(branch.getEmail());
        return v;
    }

}
