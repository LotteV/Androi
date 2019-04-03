package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.model.RegisterAccount;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @BindView(R.id.lblError)
    TextView lblError;

    API_Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String eMail = txtEmail.getText().toString();
                String phoneNumber = txtPhoneNumber.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                String error = validatePassword(password,confirmPassword);

                if(firstName.equals("")){
                    lblError.setText("Voornaam mag niet leeg zijn");return;
                }
                if (lastName.equals("")){
                    lblError.setText("Achternaam mag niet leeg zijn");return;
                }
                if(eMail.equals("")){
                    lblError.setText("E-mail incorrect of leeg");return;
                }
                if(phoneNumber.equals("")){
                    lblError.setText("Telefoonnummer incorrect of leeg");
                }
                lblError.setText(error);

                if(error.equals("")){
                    RegisterAccount account = new RegisterAccount(eMail,firstName,lastName,phoneNumber,password,confirmPassword);
                    createNewUser(account);
                }else {
                    lblError.setText(error);
                }
            }
        });
    }

    public void createNewUser(RegisterAccount account){
        service = API.createService(API_Service.class);
        Call<RegisterAccount> call = service.createUser(account);
        call.enqueue(new Callback<RegisterAccount>() {
            @Override
            public void onResponse(Call<RegisterAccount> call, Response<RegisterAccount> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registratie gelukt", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registratie failed: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterAccount> call, Throwable t) {
                Log.d("Failure btnRegister", t.getMessage());
            }
        });
    }

    private String validatePassword(String password,String confirmPassword) {
        if(password.length()<6){
            return "passwoord moet minstens 6 tekens lang zijn";
        }
        if(!password.matches(".*\\d+.*")){
            return "passwoord moet minstens 1 getal bevatten";
        }
        if(!password.matches(".*[!@#$%^&*_]+.*")){
            return "passwoord moet 1 van volgende tekens bevatten !@#$%^&*_";
        }
        if(!password.equals(confirmPassword)){
            return "Wachtwoord en Herhaal wachtwoord moeten gelijk zijn";
        }
        return "";
    }
}
