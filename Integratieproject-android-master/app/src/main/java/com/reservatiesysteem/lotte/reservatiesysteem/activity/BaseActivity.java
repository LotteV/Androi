package com.reservatiesysteem.lotte.reservatiesysteem.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.reservatiesysteem.lotte.reservatiesysteem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.reservatiesysteem.lotte.reservatiesysteem.activity.LoginActivity.EXPIRE;

/**
 * Created by Jasper on 23/02/2017.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.TOKEN,MODE_PRIVATE);
            String expireDate = sharedPreferences.getString(EXPIRE,"");

            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE MMM d HH:mm:ss zz yyyy");
            Date stringDate = simpledateformat.parse(expireDate);

            if(stringDate.before(new Date())){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        SharedPreferences preferences = getSharedPreferences(LoginActivity.TOKEN, Context.MODE_PRIVATE);
        String token =  preferences.getString(LoginActivity.TOKEN,"");
        if(token.equals("")){
            menu.findItem(R.id.login).setVisible(true);
        }else{
            menu.findItem(R.id.logout).setVisible(true);
            menu.findItem(R.id.myProfile).setVisible(true);
            menu.findItem(R.id.myFavorites).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.TOKEN,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                startActivity(new Intent(this,StartActivity.class));
                invalidateOptionsMenu();
                return true;
            case R.id.myProfile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.myFavorites:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.reserveren:
                finish();
                startActivity(new Intent(this,StartActivity.class));
        }
        return true;
    }

}
