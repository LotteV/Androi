package com.reservatiesysteem.lotte.reservatiesysteem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 13/02/2017.
 */

public class CityArrayAdapter extends ArrayAdapter<City> {
    private final Context mContext;
    private final List<City> mCities_All;
    private final List<City> mCities_Suggestion;
    private final int mLayoutResourceId;

    public CityArrayAdapter(Context context, int resource, List<City> cities) {
        super(context, resource,cities);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mCities_All = cities;
        this.mCities_Suggestion = new ArrayList<>();
    }

    public int getCount() {
        return mCities_Suggestion.size();
    }

    public City getItem(int position) {
        return mCities_Suggestion.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            City department = getItem(position);
            TextView name = (TextView) convertView.findViewById(android.R.id.text1);
            name.setText(department.getName() + " - " + department.getPostalCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            public String convertResultToString(Object resultValue) {
                City city = (City) resultValue;
                return String.format("%s - %s", city.getName(), city.getPostalCode());
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mCities_Suggestion.clear();
                    for (City city : mCities_All) {
                        if (city.getName().toLowerCase().startsWith(constraint.toString().toLowerCase()) || city.getPostalCode().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            mCities_Suggestion.add(city);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mCities_Suggestion;
                    filterResults.count = mCities_Suggestion.size();
                    return filterResults;
                }
                return new FilterResults();
            }
        };
    }
}
