package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.MessageAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Message;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Reservation;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jasper on 02/03/2017.
 */

public class ReservationDetailActivity extends BaseActivity {
    @BindView(R.id.lblBranchName) TextView lblBranchName;
    @BindView(R.id.lblBranchAdress) TextView lblBranchAdress;
    @BindView(R.id.lblDate) TextView lblDate;
    @BindView(R.id.lblStartTime) TextView lblStartTime;
    @BindView(R.id.lblPersonCount) TextView lblPersonCount;
    @BindView(R.id.btnCancel) Button btnCancel;
    @BindView(R.id.btnPostMessage) Button btnPostMessage;
    @BindView(R.id.lvMessages) ListView lvMessages;
    @BindView(R.id.empty) TextView txtEmpty;

    Reservation reservation;
    MessageAdapter messageAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        reservation = (Reservation) bundle.getSerializable("reservation");
        String branchName = bundle.getString("branchName");
        String branchAddress = bundle.getString("branchAddress");

        String date = reservation.getDateTime().split("T")[0];
        String startTime = reservation.getDateTime().split("T")[1].substring(0, 5);
        String endTime = reservation.getEndDateTime().split("T")[1].substring(0, 5);

        String personCount = bundle.getString("personCount");
        String[] adres = branchAddress.split(",\n");
        String adresStreet = adres[0];
        String adresCity = adres[1];

        lblBranchName.setText(branchName);
        lblBranchAdress.setText(adresStreet + ", " + adresCity);
        lblDate.setText("Datum: " + date);
        lblStartTime.setText("Van " + startTime+ " tot " + endTime);
        lblPersonCount.setText(personCount);

        getMessages();

        //annuleren niet meer mogelijk als reservatie al gepasseerd is
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.UK);
        try {
            Date now = new Date();
            Date dateRes = dateFormat.parse(date + " " + startTime);

            if(dateRes.before(now)){
                btnCancel.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation();
            }
        });

        btnPostMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });
    }

    private void getMessages(){
        API_Service service = API.createService(API_Service.class);
        final int resId = reservation.getId();
        Call<List<Message>> call = service.getMessagesByResId(resId);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                if(response.body() == null) {
                    lvMessages.setEmptyView(txtEmpty);
                    txtEmpty.setVisibility(View.VISIBLE);
                }else {
                     messageAdapter = new MessageAdapter(getApplicationContext(), R.layout.view_message_entry, response.body());
                    txtEmpty.setVisibility(View.GONE);
                    lvMessages.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

    private void postMessage(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReservationDetailActivity.this);

        alertDialog.setTitle("Berichten Verzenden");
        alertDialog.setMessage("Geef je bericht:");

        //use an edittext to get message
        final EditText txtMessage = new EditText(this);
        txtMessage.setId(reservation.getId());
        alertDialog.setView(txtMessage);

        alertDialog.setPositiveButton("Verzenden", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = ReservationDetailActivity.this;
                SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(LoginActivity.TOKEN,"");

                final Message message = new Message(txtMessage.getId(), reservation.getBranchId(), txtMessage.getText().toString());

                API_Service service = API.createService(API_Service.class, token);
                Call<Message> call = service.createMessage(message);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Bericht succesvol verzonden", Toast.LENGTH_LONG).show();
                            messageAdapter.addMessage(response.body());
                        }else{
                            Toast.makeText(getApplicationContext(), "Bericht niet verzonden " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.d("Error posting message", t.getMessage());
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void cancelReservation(){
        Activity activity = ReservationDetailActivity.this;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN,"");

        API_Service service = API.createService(API_Service.class, token);
        Call<Void> call = service.deleteReservation(reservation.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Reservatie succesvol geannuleerd", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Reservatie niet geannuleerd! " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }
}
