package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.adapter.ReservationAdapter;
import com.reservatiesysteem.lotte.reservatiesysteem.model.ProfileAccount;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lotte on 26/02/2017.
 */

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.txtProfFirstName)
    TextView txtFirstname;
    @BindView(R.id.txtProfSurname)
    TextView txtSurname;
    @BindView(R.id.txtProfMail)
    TextView txtEmail;
    @BindView(R.id.editFirstname)
    EditText firstname;
    @BindView(R.id.editSurname)
    EditText surname;
    @BindView(R.id.editPhoneNumber)
    EditText phonenumber;
    @BindView(R.id.editEmail)
    TextView email;
    @BindView(R.id.lblError)
    TextView error;
    @BindView(R.id.btnCheckRes)
    Button btnCheckRes;
    @BindView(R.id.btnChangePassword)
    Button btnChangePas;
    @BindView(R.id.btnSaveProfile)
    Button btnSaveProfile;

    ProfileAccount profileAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        ButterKnife.bind(this);

        getAccount();

        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newFirstName = firstname.getText().toString();
                profileAccount.setFirstname(newFirstName);
            }
        });

        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newLastName = surname.getText().toString();
                profileAccount.setLastname(newLastName);
            }
        });
        phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phoneNumber = phonenumber.getText().toString();
                if(phoneNumber.matches("\\d{10}")){
                    profileAccount.setPhoneNumber(phoneNumber);
                    error.setText("");
                }else {
                    error.setText("foutief telefoon nummer");
                }
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(profileAccount);
            }
        });

        btnCheckRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationsActivity.class);
                intent.putExtra("Reservations", profileAccount.getReservations());
                startActivity(intent);
                finish();
            }
        });

        btnChangePas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

    }

    private void getAccount() {
        Activity activity = ProfileActivity.this;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN, "");

        final API_Service service = API.createService(API_Service.class, token);
        Call<ProfileAccount> call = service.getProfile();
        call.enqueue(new Callback<ProfileAccount>() {
            @Override
            public void onResponse(Call<ProfileAccount> call, Response<ProfileAccount> response) {
                profileAccount = response.body();
                if (profileAccount != null) {
                    firstname.setText(profileAccount.getFirstname());
                    surname.setText(profileAccount.getLastname());
                    email.setText(profileAccount.getEmail());
                    phonenumber.setText(profileAccount.getPhoneNumber());
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "login verlopen, opnieuw inloggen aub", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ProfileAccount> call, Throwable t) {

            }
        });
    }

    private void changePassword() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Wachtwoord veranderen");
        final EditText oldPass = new EditText(ProfileActivity.this);
        final EditText newPass = new EditText(ProfileActivity.this);
        final EditText confirmPass = new EditText(ProfileActivity.this);

        oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        oldPass.setHint("Old Password");
        newPass.setHint("New Password");
        confirmPass.setHint("Confirm Password");

        LinearLayout ll = new LinearLayout(ProfileActivity.this);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.addView(oldPass);
        ll.addView(newPass);
        ll.addView(confirmPass);
        alertDialog.setView(ll);

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Activity activity = ProfileActivity.this;
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString(LoginActivity.TOKEN, "");

                        API_Service service = API.createService(API_Service.class, token);
                        Call<Void> call = service.changePassWord(oldPass.getText().toString(), newPass.getText().toString(), confirmPass.getText().toString());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Wachtwoord succesvol gewijzigd", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wachtwoord niet gewijzigd " + response.message(), Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("Error change password", t.getMessage());
                            }
                        });
                    }
                });

        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();

    }

    private void updateUser(ProfileAccount profileAccount) {
        final Activity activity = ProfileActivity.this;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN, "");

        final API_Service service = API.createService(API_Service.class, token);
        Call<Void> call = service.changeAccount(profileAccount);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Gegevens zijn succesvol opgeslagen", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Gegevens niet opgeslagen " + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error changing profile", t.getMessage());
            }
        });
    }
}
