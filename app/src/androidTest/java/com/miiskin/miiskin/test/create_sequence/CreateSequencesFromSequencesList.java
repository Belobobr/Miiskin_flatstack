package com.miiskin.miiskin.test.create_sequence;

import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity;
import com.miiskin.miiskin.Gui.CreateSequence.GeneralAreaFragment;
import com.miiskin.miiskin.Gui.General.PointedImageView;
import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.TestHelper;
import com.robotium.solo.*;

import android.graphics.drawable.BitmapDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;


public class CreateSequencesFromSequencesList extends ActivityInstrumentationTestCase2<HomeActivity> {
  	private Solo solo;
  	
  	public CreateSequencesFromSequencesList() {
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
        //Click on ImageView
		solo.clickOnView(solo.getView(com.miiskin.miiskin.R.id.fabManySequence));
        //Wait for activity: 'com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity'
		assertTrue("com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity is not found!", solo.waitForActivity(com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity.class));

        ImageView bodyImageView =  (ImageView)solo.getView(R.id.bodyImageView);
        assertNotNull("Main body part not loaded", ((BitmapDrawable)bodyImageView.getDrawable()).getBitmap());
        int[] locationOnScreen = new int[2];
        bodyImageView.getLocationOnScreen(locationOnScreen);
        solo.clickOnScreen(locationOnScreen[0] + bodyImageView.getWidth() / 2, locationOnScreen[1] + bodyImageView.getHeight() / 2);
        assertNotNull("Body part not loaded", ((BitmapDrawable) bodyImageView.getDrawable()).getBitmap());

        CreateMoleActivity maleCreateSequence = (CreateMoleActivity)solo.getCurrentActivity();
        GeneralAreaFragment generalAreaFragment = (GeneralAreaFragment)maleCreateSequence.getFragmentManager().findFragmentByTag(GeneralAreaFragment.TAG);
        if (generalAreaFragment.getBodyPart().equals(BodyPart.Main)) {
            //select near center point
            solo.clickOnScreen(locationOnScreen[0] + bodyImageView.getWidth() /2 + 40, locationOnScreen[1] + bodyImageView.getHeight() / 2);
        }

        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForFragmentById(R.id.main_layout, 2000);
        PointedImageView pointedImageView =  (PointedImageView)solo.getView(R.id.bodyImageView);
        assertNotNull("Specific body part not loaded", ((BitmapDrawable)pointedImageView.getDrawable()).getBitmap());

        locationOnScreen = new int[2];
        pointedImageView.getLocationOnScreen(locationOnScreen);
        solo.clickOnScreen(locationOnScreen[0] + pointedImageView.getWidth() / 2, locationOnScreen[1] + pointedImageView.getHeight() / 2);
        solo.clickOnView(solo.getView(R.id.fab));

        //Wait for activity: 'com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity'
        assertTrue("com.miiskin.miiskin.Gui.Home.HomeActivity is not found!", solo.waitForActivity(com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity.class));
        //Click on ImageView
		solo.goBack();
        //Wait for activity: 'com.miiskin.miiskin.Gui.Home.HomeActivity'
		assertTrue("com.miiskin.miiskin.Gui.Home.HomeActivity is not found!", solo.waitForActivity(com.miiskin.miiskin.Gui.Home.HomeActivity.class));
	}
}
