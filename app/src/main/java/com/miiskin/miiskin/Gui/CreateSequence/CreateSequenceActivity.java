package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 24.06.2015.
 */
public class CreateSequenceActivity extends ActionBarActivity {

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_secuence);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = GeneralAreaFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_clear);
            mActionBarToolbar.setTitle(R.string.please_select_the_general_area);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void goToSelectSpecificLocationFragment() {
        Fragment fragment = SpecificLocationFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).commit();
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
    }
}
