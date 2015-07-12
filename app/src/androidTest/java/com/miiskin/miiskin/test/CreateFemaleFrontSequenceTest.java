package com.miiskin.miiskin.test;

import com.miiskin.miiskin.CreateSequenceStub;
import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.UserInfo;

/**
 * Created by Newshka on 12.07.2015.
 */
public class CreateFemaleFrontSequenceTest extends CreateSequenceStub {
    @Override
    public String getMale() {
        return UserInfo.FEMALE;
    }

    @Override
    public BodyHalf getBodyHalf() {
        return BodyHalf.Front;
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        execute();
    }
}
