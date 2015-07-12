package com.miiskin.miiskin;

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity;
import com.miiskin.miiskin.Gui.CreateSequence.GeneralAreaFragment;
import com.miiskin.miiskin.Gui.CreateSequence.SpecificLocationFragment;
import com.miiskin.miiskin.Gui.General.PointedImageView;
import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.miiskin.miiskin.Gui.Home.HomeFragment;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.MiiskinDbHelper;
import com.miiskin.miiskin.Storage.Preferences;
import com.miiskin.miiskin.TestHelper;
import com.robotium.solo.*;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.preference.Preference;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.ListView;


public abstract class CreateSequenceStub extends ActivityInstrumentationTestCase2<HomeActivity> {
  	private Solo solo;
  	
  	public CreateSequenceStub() {
		super(HomeActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
        clearData();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}

    private void clearData() {
        Instrumentation instrumentation = getInstrumentation();
        TestHelper.clearData(instrumentation.getTargetContext());
    }
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}

    public void execute() {
        solo.takeScreenshot();
        //Wait for activity: 'com.miiskin.miiskin.Gui.Home.HomeActivity'
        solo.waitForActivity(com.miiskin.miiskin.Gui.Home.HomeActivity.class, 2000);
        //Click on START USING MIISKIN
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Click on gender buttons
        if (getMale().equals(UserInfo.MALE)) {
            solo.clickOnView(solo.getView(com.miiskin.miiskin.R.id.male_button));
        } else if (getMale().equals(UserInfo.FEMALE)) {
            solo.clickOnView(solo.getView(R.id.female_button));
        }
        //Click on Empty Text View
        solo.clickOnView(solo.getView(com.miiskin.miiskin.R.id.action_done));
        //Click on ImageView
        solo.clickOnView(solo.getView(com.miiskin.miiskin.R.id.fabNoSequence));

        solo.waitForActivity(com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity.class);

        ImageView bodyImageView =  (ImageView)solo.getView(R.id.bodyImageView);
        assertNotNull("Main body part not loaded", ((BitmapDrawable)bodyImageView.getDrawable()).getBitmap());

        if (getBodyHalf().equals(BodyHalf.Front)) {
            solo.clickOnView(solo.getView(R.id.buttonFront));
        } else if (getBodyHalf().equals(BodyHalf.Rear)) {
            solo.clickOnView(solo.getView(R.id.buttonRear));
        }
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
    }

    abstract public String getMale();
    abstract public BodyHalf getBodyHalf();
}
