package com.miiskin.miiskin.Data;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Newshka on 29.06.2015.
 */
public class Utils {

    public static int pixelsFromDp(Context context, int pdSize) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pdSize, r.getDisplayMetrics());
        return (int)px;
    }

    public static int getImageResourceId(Context context, BodyPart bodyPart, String gender,  BodyHalf bodyHalf, boolean mask) {
        Resources r = context.getResources();
        int drawableResourceId = r.getIdentifier(Utils.getImageName(bodyPart, gender, bodyHalf, mask), "drawable", "com.miiskin.miiskin");
        return drawableResourceId;
    }

    public static String getImageName(BodyPart bodyPart, String gender,  BodyHalf bodyHalf, boolean mask) {
        String bodyPartNamePart = Utils.bodyPartName(bodyPart);
        String genderPart = gender.equals(UserInfo.MALE) ? "_male" : "_female";
        String frontModePart = bodyHalf == BodyHalf.Front ? "_front" : "_rear";

        return bodyPartNamePart + genderPart + frontModePart + (mask ? "_mask_zoom" : "_zoom");
    }

    public static String bodyPartName(BodyPart bodyPart) {
        String bodyPartString = null;
        switch (bodyPart) {
            case LeftHand :
                bodyPartString = "left_hand";
                break;
            case RightHand :
                bodyPartString = "right_hand";
                break;
            case LeftForeArm :
                bodyPartString = "left_forearm";
                break;
            case RightForeArm :
                bodyPartString = "right_forearm";
                break;
            case LeftUpperArm :
                bodyPartString = "left_upper_arm";
                break;
            case RightUpperArm :
                bodyPartString = "right_upper_arm";
                break;
            case FaceThroat :
                bodyPartString = "face_throat";
                break;
            case Chest :
                bodyPartString = "chest";
                break;
            case Stomach :
                bodyPartString = "stomach";
                break;
            case Genitals :
                bodyPartString = "genitals";
                break;
            case Groin :
                bodyPartString = "groin";
                break;
            case RightThigh :
                bodyPartString = "right_thigh";
                break;
            case LeftThigh :
                bodyPartString = "left_thigh";
                break;
            case RightShin :
                bodyPartString = "right_shin";
                break;
            case LeftShin :
                bodyPartString = "left_shin";
                break;
            case RightFoot:
                bodyPartString = "right_foot";
                break;
            case LeftFoot:
                bodyPartString = "left_foot";
                break;
            case Main:
                bodyPartString ="";
            default:
                bodyPartString ="";
                break;
        }
        return bodyPartString;
    }
}
