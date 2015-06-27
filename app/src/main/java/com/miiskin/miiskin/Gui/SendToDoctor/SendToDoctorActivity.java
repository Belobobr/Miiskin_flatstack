package com.miiskin.miiskin.Gui.SendToDoctor;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miiskin.miiskin.Gui.ViewSequence.ViewSequenceFragment;
import com.miiskin.miiskin.R;

public class SendToDoctorActivity extends AppCompatActivity {

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_toctor);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //mActionBarToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_clear);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mActionBarToolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = SendToDoctorFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
        }
    }

}
