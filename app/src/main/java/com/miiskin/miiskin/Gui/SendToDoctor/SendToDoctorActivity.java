package com.miiskin.miiskin.Gui.SendToDoctor;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.miiskin.miiskin.Gui.MiiskinActivity;
import com.miiskin.miiskin.R;

public class SendToDoctorActivity extends MiiskinActivity implements UnderDevelopmentDialog.UnderDevelopmentDialogListener {

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_toctor);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_clear);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = SendToDoctorFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionBarToolbar.setTitle("");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogBackButtonPressed() {

    }
}
