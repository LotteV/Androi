package com.reservatiesysteem.lotte.reservatiesysteem.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.model.ProfileAccount;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Review;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 2/03/2017.
 */

public class ReviewAdapter extends BaseAdapter {

    private List<Review> reviews = new ArrayList<>();
    private Context context;

    public ReviewAdapter(Context context, int resource, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Review review = getItem(position);

        final View v;

        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_review_entry, parent, false);
        }
        else {
            v = convertView;
        }


        TextView txtReviewText = (TextView) v.findViewById(R.id.reviewText);
        final TextView txtReviewUserDate = (TextView) v.findViewById(R.id.reviewUserDate);
        ImageView imgResult = (ImageView) v.findViewById(R.id.imgResult);

        String[] dateTime = review.getDateTime().split("T");
        final String resDate = dateTime[0];

        txtReviewText.setText(review.getText());
        txtReviewUserDate.setText("Gepost door " + review.getUser().getFirstname() + " " + review.getUser().getLastname() + " op " + resDate);

        if(review.isResult()){
            imgResult.setImageResource(R.drawable.thumb_up);
        } else {
            imgResult.setImageResource(R.drawable.thumb_down);
        }


        return v;
    }
}
