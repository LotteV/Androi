package com.reservatiesysteem.lotte.reservatiesysteem.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.activity.StartActivity;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.ReviewAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.model.AdditionalInfo;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Branch;
import com.reservatiesysteem.lotte.reservatiesysteem.model.OpeningHour;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 13/02/2017.
 */

public class DetailsFragment extends Fragment {

    @BindView(R.id.txtBranchName) TextView txtBranchName;
    @BindView(R.id.btnFavorites) ImageButton btnFavorites;
    @BindView(R.id.btnFotos) Button btnFotos;
    @BindView(R.id.btnPlace) Button btnPlace;
    @BindView(R.id.viewFoto) ImageView viewFoto;
    @BindView(R.id.viewPlaats) LinearLayout viewPlaats;
    @BindView(R.id.btnBeschrijving) Button btnBeschrijving;
    @BindView(R.id.viewBeschrijving) TextView viewBeschrijving;
    @BindView(R.id.btnUren) Button btnUren;
    @BindView(R.id.btnInfo) Button btnInfo;
    @BindView(R.id.layoutUren) TableLayout viewUren;
    @BindView(R.id.layoutInfo) TableLayout viewInfo;
    @BindView(R.id.btnReview) Button btnReview;
    @BindView(R.id.lvReview) ListView lvReview;
    @BindView(R.id.layoutImage3) LinearLayout lyReview;
    @BindView(R.id.empty) TextView txtEmpty;
    @BindView(R.id.btnReserveren) Button btnReserveren;

    @BindView(R.id.txtAdres) TextView txtAdres;
    @BindView(R.id.txtTelnr) TextView txtTelNr;
    @BindView(R.id.txtEmail) TextView txtEmail;
    @BindView(R.id.txtPayment) TextView txtPayment;
    @BindView(R.id.txtFacility) TextView txtFacility;
    @BindView(R.id.txtAccessibility) TextView txtAccessibility;
    @BindView(R.id.txtAtmosphere) TextView txtAtmosphere;

    @BindView(R.id.txtMaandag) TextView txtMaandag;
    @BindView(R.id.txtDinsdag) TextView txtDinsdag;
    @BindView(R.id.txtWoensdag) TextView txtWoensdag;
    @BindView(R.id.txtDonderdag) TextView txtDonderdag;
    @BindView(R.id.txtVrijdag) TextView txtVrijdag;
    @BindView(R.id.txtZaterdag) TextView txtZaterdag;
    @BindView(R.id.txtZondag) TextView txtZondag;


    private int receivedBranchId;
    private int chosenPostalCode = 0;
    private String chosenDate = "";
    private String chosenTime = "";
    private String chosenNumberOfPersons = "";
    private String branchName = "";
    private boolean available;

    private GoogleMap map;
    LatLng coordinates;
    double latitude = 0;
    double longitude = 0;

    public DetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_branchdetails, container, false);
        ButterKnife.bind(this,view);

        Bundle bundle = getArguments();
        if(bundle != null){
            receivedBranchId = bundle.getInt("branchId", 0);
            chosenPostalCode = bundle.getInt("chosenPostalCode", 0);
            chosenDate = bundle.getString(SearchFragment.CHOSEN_DATE);
            chosenTime = bundle.getString(SearchFragment.CHOSEN_TIME);
            chosenNumberOfPersons = bundle.getString(SearchFragment.CHOSEN_NUMBEROFPERSONS);
            if(!bundle.getString("Beschikbaar").equals("Beschikbaar")){
                btnReserveren.setEnabled(false);
            }

        }

        getBranchDetails();

        //google maps
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorites();
            }
        });

        btnFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewFoto.getVisibility() == View.GONE){
                    viewFoto.setVisibility(View.VISIBLE);
                } else if(viewFoto.getVisibility() == View.VISIBLE){
                    viewFoto.setVisibility(View.GONE);
                }
                if(viewPlaats.getVisibility() == View.VISIBLE){
                    viewPlaats.setVisibility(View.GONE);
                }
            }
        });

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPlaats.getVisibility() == View.GONE){
                    viewPlaats.setVisibility(View.VISIBLE);
                } else if(viewPlaats.getVisibility() == View.VISIBLE){
                    viewPlaats.setVisibility(View.GONE);
                }
                if(viewFoto.getVisibility() == View.VISIBLE){
                    viewFoto.setVisibility(View.GONE);
                }
            }
        });

        btnBeschrijving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewBeschrijving.getVisibility() == View.GONE){
                    viewBeschrijving.setVisibility(View.VISIBLE);
                } else if(viewBeschrijving.getVisibility() == View.VISIBLE){
                    viewBeschrijving.setVisibility(View.GONE);
                }
            }
        });

        btnUren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewUren.getVisibility() == View.GONE){
                    viewUren.setVisibility(View.VISIBLE);
                } else if(viewUren.getVisibility() == View.VISIBLE){
                    viewUren.setVisibility(View.GONE);
                }
                if(viewInfo.getVisibility() == View.VISIBLE){
                    viewInfo.setVisibility(View.GONE);
                }
            }
        });



        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewInfo.getVisibility() == View.GONE){
                    viewInfo.setVisibility(View.VISIBLE);
                } else if(viewInfo.getVisibility() == View.VISIBLE){
                    viewInfo.setVisibility(View.GONE);
                }
                if(viewUren.getVisibility() == View.VISIBLE){
                    viewUren.setVisibility(View.GONE);
                }
            }

        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lyReview.getVisibility() == View.GONE){
                    lyReview.setVisibility(View.VISIBLE);
                } else if(lyReview.getVisibility() == View.VISIBLE){
                    lyReview.setVisibility(View.GONE);
                }
            }
        });

        btnReserveren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(LoginActivity.TOKEN,"");

                if ("".equals(token)){
                    startActivity(new Intent(activity, LoginActivity.class));
                }else {
                    Bundle bundle = new Bundle();
                    StartActivity startActivity = (StartActivity) getActivity();
                    ReservationFragment reservationFragment = new ReservationFragment();

                    bundle.putInt("branchId", receivedBranchId);
                    bundle.putString(SearchFragment.CHOSEN_DATE, chosenDate);
                    bundle.putString(SearchFragment.CHOSEN_TIME, chosenTime);
                    bundle.putString(SearchFragment.CHOSEN_NUMBEROFPERSONS, chosenNumberOfPersons);
                    bundle.putString("branchName", txtBranchName.getText().toString());

                    reservationFragment.setArguments(bundle);
                    startActivity.changeFragment(reservationFragment, 3);
                }
            }
        });


        return view;
    }

    private void getBranchDetails(){
        API_Service service = API.createService(API_Service.class);
        Call<Branch> call = service.getBranchDetails(receivedBranchId);
        call.enqueue(new Callback<Branch>() {
            @Override
            public void onResponse(Call<Branch> call, Response<Branch> response) {
                Branch branch = response.body();

                txtBranchName.setText(branch.getName());
                viewBeschrijving.setText(branch.getDescription());

                //info weergeven
                txtAdres.setText(branch.getStreet() + " " + branch.getNumber() + ", " + branch.getCity().getPostalCode() + " " + branch.getCity().getName());
                txtTelNr.setText(branch.getPhoneNumber());
                txtEmail.setText(branch.getEmail());

                //uren weergeven
                for(OpeningHour openingHour : branch.getOpeningHours()){
                    switch (openingHour.getDay()){
                        case 0: //zondag
                            txtZondag.setText(openingHourText(txtZondag.getText(), openingHour));
                            break;
                        case 1: //maandag
                            txtMaandag.setText(openingHourText(txtMaandag.getText(), openingHour));
                            break;
                        case 2:  //dindsdag
                            txtDinsdag.setText(openingHourText(txtDinsdag.getText(), openingHour));
                            break;
                        case 3: //woensdag
                            txtWoensdag.setText(openingHourText(txtWoensdag.getText(), openingHour));
                            break;
                        case 4: //donderdag
                            txtDonderdag.setText(openingHourText(txtDonderdag.getText(), openingHour));
                            break;
                        case 5: //vrijdag
                            txtVrijdag.setText(openingHourText(txtVrijdag.getText(), openingHour));
                            break;
                        case 6: //zaterdag
                            txtZaterdag.setText(openingHourText(txtZaterdag.getText(), openingHour));
                            break;

                    }
                }

                //additional info weergeven
                for(AdditionalInfo additionalInfo : branch.getAdditionalInfo()){
                    switch (additionalInfo.getType()){
                        case 0:
                            txtPayment.setText(additionalInfoText(txtPayment.getText(), additionalInfo));
                            break;
                        case 1:
                            txtFacility.setText(additionalInfoText(txtFacility.getText(), additionalInfo));
                            break;
                        case 2:
                            txtAccessibility.setText(additionalInfoText(txtAccessibility.getText(), additionalInfo));
                            break;
                        case 3:
                            txtAtmosphere.setText(additionalInfoText(txtAtmosphere.getText(), additionalInfo));
                            break;
                        case 4:
                            break;
                        case 5: break;
                    }
                }

                //reviews weergeven
                final ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(), R.layout.view_review_entry, response.body().getReviews());
                lvReview.setEmptyView(txtEmpty);
                lvReview.setAdapter(reviewAdapter);

                //scroll listview inside scrollview
                lvReview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });

                //picture weergeven
                if(branch.getPicture() == null){
                    String defeaultUrl = "http://leisurebooker.azurewebsites.net/Content/bowling.jpg";
                    Picasso.with(getContext()).load(defeaultUrl).into(viewFoto);
                }else {
                    String url = "https://leisurebooker.azurewebsites.net/" + branch.getPicture();
                    Picasso.with(getContext()).load(url).into(viewFoto);
                }

                //get coordinates
                String address = branch.getStreet() + " " + branch.getNumber() + ", " + branch.getCity().getPostalCode() + " " + branch.getCity().getName();
                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();

                    coordinates = new LatLng(latitude, longitude);

                    //google maps
                    Marker adress = map.addMarker(new MarkerOptions().position(coordinates).title(address));
                    map.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                    map.setMyLocationEnabled(true);

                }

            }

            @Override
            public void onFailure(Call<Branch> call, Throwable t) {
                Log.d("Error receiving details", t.getMessage());
            }
        });
    }

    private String openingHourText(CharSequence startText, OpeningHour openingHour){
        if(startText.equals("")) {
            return openingHour.getFromTime().substring(0,5) + " - " + openingHour.getToTime().substring(0,5);
        }else {
            return startText + "\n" + openingHour.getFromTime().substring(0,5) + " - " + openingHour.getToTime().substring(0,5);
        }
    }

    private String additionalInfoText(CharSequence startText, AdditionalInfo additionalInfo){
        if(startText.equals("")){
            return additionalInfo.getValue().toLowerCase();
        }else {
            return startText + ", " + additionalInfo.getValue().toLowerCase();
        }
    }

    private void addFavorites(){
        SharedPreferences preferences = getActivity().getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token =  preferences.getString(LoginActivity.TOKEN,"");

        API_Service service = API.createService(API_Service.class, token);
        Call<Void> call = service.addFavorites(receivedBranchId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Favoriet succesvol toegevoegd", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Failed" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error favorites", t.getMessage());
            }
        });

    }

}
