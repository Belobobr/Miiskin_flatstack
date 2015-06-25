package com.miiskin.miiskin.Gui.ViewSequence;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Gui.CreateSequence.GeneralAreaFragment;
import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.miiskin.miiskin.R;

public class ViewSequenceActivity extends AppCompatActivity {

    public static final String EXTRA_SEQUENCE_DATA = "EXTRA_SEQUENCE_DATA";

    SequenceData mSequenceData;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSequenceData = (SequenceData)getIntent().getSerializableExtra(EXTRA_SEQUENCE_DATA);

        setContentView(R.layout.activity_view_sequence);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = ViewSequenceFragment.newInstance(mSequenceData);
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
