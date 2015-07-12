package com.miiskin.miiskin.test;

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.ResourcesStub;

/**
 * Created by Newshka on 12.07.2015.
 */
public class ResourcesFemaleZoomTest extends ResourcesStub {

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
    }

    @Override
    protected void checkResources() {
        {
            checkResources(BodyHalf.Front, true, true);
            checkResources(BodyHalf.Front, true, false);
            checkResources(BodyHalf.Rear, true, true);
            checkResources(BodyHalf.Rear, true, false);
        }
    }

    private void checkResources(BodyHalf bodyHalf, boolean zoom, boolean mask) {
        for (BodyPart bodyPart :BodyPart.values()) {
            if (bodyPart.equals(BodyPart.Main))
                continue;

            mImageName = Utils.getImageName(bodyPart, UserInfo.FEMALE, BodyHalf.Front, zoom, mask);
            populateMessage();
        }
    }
}
