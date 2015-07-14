package com.miiskin.miiskin.test.send_to_doctor;

import android.test.ActivityInstrumentationTestCase2;

import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.miiskin.miiskin.Gui.SendToDoctor.UnderDevelopmentDialog;
import com.miiskin.miiskin.Gui.ViewSequence.PagerContainer;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.TestHelper;
import com.robotium.solo.Solo;

/**
 * Created by Newshka on 14.07.2015.
 */
public class SendToDoctorTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private Solo solo;

    public SendToDoctorTest() {
        super(HomeActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        TestHelper.initFirstSequence(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        //Wait for activity: 'com.miiskin.miiskin.Gui.Home.HomeActivity'
        solo.waitForActivity(com.miiskin.miiskin.Gui.Home.HomeActivity.class, 2000);

        solo.clickInList(0);

        solo.waitForActivity(com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity.class, 2000);

        PagerContainer pagerContainer = (PagerContainer)solo.getView(R.id.pager_container);

        solo.clickOnView(solo.getView(R.id.fab));

        solo.clickOnView(solo.getView(R.id.sendDoctor));

        solo.waitForActivity(com.miiskin.miiskin.Gui.SendToDoctor.SendToDoctorActivity.class, 2000);

        solo.clickOnView(solo.getView(R.id.licence_agreement_check_box));

        solo.clickOnView(solo.getView(R.id.send_to_doctor_button));

        solo.waitForDialogToOpen();

        assertTrue("Under development dialog not opened!", solo.waitForFragmentByTag(UnderDevelopmentDialog.TAG));
    }
}
