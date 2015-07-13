package com.miiskin.miiskin.test.camera;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.miiskin.miiskin.Gui.ViewSequence.PagerContainer;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.TestHelper;
import com.robotium.solo.Solo;

/**
 * Created by Newshka on 13.07.2015.
 */
public class TakePhotoTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    private Solo solo;

    public TakePhotoTest() {
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

        int photoCountBeforeTakeNewPhoto = pagerContainer.getViewPager().getChildCount();

        solo.clickOnView(solo.getView(R.id.fab));

        solo.clickOnView(solo.getView(R.id.takePhoto));

        solo.waitForActivity(com.miiskin.miiskin.Gui.Camera.CameraActivity.class, 2000);

        solo.clickOnView(solo.getView(R.id.cancel_photo));

        solo.waitForActivity(com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity.class, 2000);

        solo.clickOnView(solo.getView(R.id.fab));

        solo.clickOnView(solo.getView(R.id.takePhoto));

        solo.waitForActivity(com.miiskin.miiskin.Gui.Camera.CameraActivity.class, 2000);

        solo.clickOnView(solo.getView(R.id.take_photo));

        solo.sleep(7000);

        solo.clickOnView(solo.getView(R.id.accept_photo));

        solo.waitForActivity(com.miiskin.miiskin.Gui.AcceptPhoto.AcceptPhotoActivity.class, 2000);

        solo.clickOnView(solo.getView(R.id.accept_photo));

        solo.waitForActivity(com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity.class, 2000);

        pagerContainer = (PagerContainer)solo.getView(R.id.pager_container);

        assertEquals(photoCountBeforeTakeNewPhoto + 1, pagerContainer.getViewPager().getChildCount());

    }
}
