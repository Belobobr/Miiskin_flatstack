package com.miiskin.miiskin.test;

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.ResourcesStub;

/**
 * Created by Newshka on 12.07.2015.
 */
public class ResourcesMaleGeneralTest extends ResourcesStub {

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
    }

    @Override
    protected void checkResources() {
        checkResources(BodyHalf.Front);
        checkResources(BodyHalf.Rear);
    }

    private void checkResources(BodyHalf bodyHalf) {
        for (BodyPart bodyPart :BodyPart.values()) {
            mImageName = Utils.getImageName(bodyPart, UserInfo.MALE, bodyHalf, false, false);
            populateMessage();
        }
    }
}
