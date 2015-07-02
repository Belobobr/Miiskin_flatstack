package com.miiskin.miiskin.Data;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 25.06.2015.
 */
public enum BodyPart {
    Main,
    LeftHand (R.string.left_hand),
    RightHand (R.string.right_hand),
    LeftForeArm (R.string.left_forearm),
    LeftUpperArm (R.drawable.left_upper_arm_foreground, R.drawable.left_upper_arm_bacground, R.string.left_upper_arm),
    RightForeArm (R.string.right_forearm),
    RightUpperArm (R.drawable.right_upper_arm_foreground, R.drawable.right_upper_arm_background, R.string.right_upper_arm),
    FaceThroat (R.string.face_throat),
    Chest (R.string.chest),
    Stomach (R.string.stomach),
    Genitals (R.string.genitals),
    Groin (R.string.groin),
    RightThigh (R.string.right_thigh),
    LeftThigh (R.string.left_thigh),
    RightShin (R.string.right_shin),
    LeftShin (R.string.left_shin),
    LeftFoot (R.string.left_foot),
    RightFoot (R.string.right_foot);

    private final int resourceIdDescription;
    private final int drawableResourceForeground;
    private final int drawableResourceBackground;

    BodyPart(int resourceIdDescription) {
        this(-1, -1, resourceIdDescription);
    }

    BodyPart(int drawableResourceForeground, int drawableResourceBackground, int resourceIdDescription) {
        this.resourceIdDescription = resourceIdDescription;
        this.drawableResourceForeground = drawableResourceForeground;
        this.drawableResourceBackground = drawableResourceBackground;
    }

    BodyPart() {
        this(-1, -1, -1);
    }


    public int getResourceIdDescription() {
        return resourceIdDescription;
    }

    public int getDrawableResourceForeground() {
        return drawableResourceForeground;
    }

    public int getDrawableResourceBackground() {
        return drawableResourceBackground;
    }
}
