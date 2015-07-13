package com.miiskin.miiskin.test.create_sequence;

import com.miiskin.miiskin.CreateSequenceStub;
import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.UserInfo;

/**
 * Created by Newshka on 12.07.2015.
 */
public class CreateMaleFrontSequenceTest extends CreateSequenceStub {
    @Override
    public String getMale() {
        return UserInfo.MALE;
    }

    @Override
    public BodyHalf getBodyHalf() {
        return BodyHalf.Front;
    }

    public void testRun() {
        execute();
    }
}
