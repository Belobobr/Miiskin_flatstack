package com.miiskin.miiskin;

import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.Gui.Home.HomeActivity;
import com.robotium.solo.Solo;

/**
 * Created by Newshka on 12.07.2015.
 */
public abstract class ResourcesStub extends ActivityInstrumentationTestCase2<HomeActivity> {
    protected Solo solo;
    protected boolean mTestFailed;
    protected String mMessage;
    protected int mImageResource;
    protected String mImageName;
    protected Resources mResources;
    protected Context mContext;

    public ResourcesStub() {
        super(HomeActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        mContext = getInstrumentation().getTargetContext();
        mMessage = null;
        mImageName = null;
        mResources = mContext.getResources();
        getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
    Don't work without it
     */
    public void testRun() {

    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        checkResources();
        assertNull(mMessage, mMessage);
    }

    protected abstract void checkResources();

    protected void populateMessage() {
        mImageResource = mResources.getIdentifier(mImageName, "drawable", "com.miiskin.miiskin");
        if (mImageResource == 0) {
            if (mMessage == null) {
                mMessage = "Resources not found: " + mImageName;
            } else {
                mMessage = mMessage + ", " + mImageName;
            }
        }
    }
}

