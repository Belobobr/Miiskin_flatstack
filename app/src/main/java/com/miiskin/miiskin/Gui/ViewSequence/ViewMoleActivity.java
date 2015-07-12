package com.miiskin.miiskin.Gui.ViewSequence;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.miiskin.miiskin.Gui.MiiskinActivity;
import com.miiskin.miiskin.R;

public class ViewMoleActivity extends MiiskinActivity {

    public static final String EXTRA_MOLE_ID = "EXTRA_MOLE_ID";

    Long mMoleId;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoleId = (Long)getIntent().getLongExtra(EXTRA_MOLE_ID, -1);

        setContentView(R.layout.activity_view_sequence);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = ViewMoleFragment.newInstance(mMoleId);
            getFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
