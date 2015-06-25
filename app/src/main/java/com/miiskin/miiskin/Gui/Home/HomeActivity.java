package com.miiskin.miiskin.Gui.Home;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Preferences;


public class HomeActivity extends AppCompatActivity implements FTEHomeFragment.FteCompleteListener, DisclaimerDialogFragment.DisclaimerDialogListener{

    @Override
    public void onFteCompleteDonePressed() {
        setFte(false);
        HomeFragment homeFragment = HomeFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_layout, homeFragment).commit();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        setFteDisclaimer(false);
    }

    @Override
    public void onDialogBackButtonPressed() {
        setFteDisclaimer(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(R.drawable.launcher_icon);

        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        boolean fte = settings.getBoolean(Preferences.FirstTimeUse.FTE, true);
        boolean fteDisclaimer = settings.getBoolean(Preferences.FirstTimeUse.FTE_SHOW_DISCLAIMER, true);


        if (savedInstanceState == null) {
            if (fte) {
                FTEHomeFragment fteHomeFragment = FTEHomeFragment.newInstance();
                getFragmentManager().beginTransaction().add(R.id.main_layout, fteHomeFragment).commit();

                if (fteDisclaimer) {
                    DialogFragment dialog = new DisclaimerDialogFragment();
                    dialog.show(getFragmentManager(), DisclaimerDialogFragment.TAG);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(DisclaimerDialogFragment.TAG) != null) {
            setFteDisclaimer(false);
            super.onBackPressed();
        }
        super.onBackPressed();
    }


}
