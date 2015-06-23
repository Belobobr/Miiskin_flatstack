package com.miiskin.miiskin.Gui;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Preferences;


public class HomeActivity extends Activity implements FTEHomeFragment.FteCompleteListener{

    @Override
    public void onFteCompleteDonePressed() {
        setFte(false);
        HomeFragment homeFragment = HomeFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_layout, homeFragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        boolean fte = settings.getBoolean(Preferences.FirstTimeUse.FTE, true);
        boolean fteDisclaimer = settings.getBoolean(Preferences.FirstTimeUse.FTE_SHOW_DISCLAIMER, false);


        if (savedInstanceState == null) {
            if (fte) {
                FTEHomeFragment fteHomeFragment = FTEHomeFragment.newInstance();
                getFragmentManager().beginTransaction().add(R.id.main_layout, fteHomeFragment).commit();

                if (fteDisclaimer) {

                }
            } else {
                HomeFragment homeFragment = HomeFragment.newInstance();
                getFragmentManager().beginTransaction().add(R.id.main_layout, homeFragment).commit();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFte(boolean fte) {
        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Preferences.FirstTimeUse.FTE, fte);
        editor.commit();
    }

    private void setFteDisclaimer(boolean fteDisclaimer) {
        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Preferences.FirstTimeUse.FTE, fteDisclaimer);
        editor.commit();
    }
}
