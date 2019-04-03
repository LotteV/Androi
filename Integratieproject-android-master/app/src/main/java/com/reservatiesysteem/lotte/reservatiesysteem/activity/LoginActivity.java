package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Token;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API;
import com.reservatiesysteem.lotte.reservatiesysteem.service.API_Service;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A btnLogin screen that offers btnLogin via email/password.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.txtUsername)
    EditText txtUsername;
    @BindView(R.id.txtPassword)
    EditText txtPassword;

    @BindView(R.id.lblError)
    TextView lblError;

    public static final String TOKEN = "MYTOKEN" ;
    public static final String EXPIRE = "EXPIRE" ;

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                API_Service service = API.createService(API_Service.class);
                Call<Token> call = service.getToken(username,password,"password");
                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Login succesvol",Toast.LENGTH_LONG).show();
                            SharedPreferences preferences = getSharedPreferences(TOKEN,Context.MODE_PRIVATE);
                            Editor editor = preferences.edit();

                            Date now = new Date();
                            now.setDate(now.getDate()+1);
                            String expireDate = now.toString();

                            editor.putString(TOKEN,response.body().getAccessToken());
                            editor.putString(EXPIRE,expireDate);

                            editor.commit();
                            invalidateOptionsMenu();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else {
                            lblError.setText("Foutieve gebruikersnaam of passwoord");
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Server niet bereikbaar",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}

