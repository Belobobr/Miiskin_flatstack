package com.miiskin.miiskin.Data;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 25.06.2015.
 */
public enum BodyPart {
    Hand,
    LeftForeArm,
    LeftUpperArm (R.drawable.left_upper_arm_foreground, R.drawable.left_upper_arm_bacground, R.string.left_upper_arm),
    RightForeArm ,
    RightUpperArm (R.drawable.right_upper_arm_foreground, R.drawable.right_upper_arm_background, R.string.right_upper_arm),
    Face,
    Chest,
    Stomach,
    Genitals,
    Groin,
    Thigh,
    Shin,
    Foot;

    private final int drawableResourceForeground;
    private final int drawableResourceBackground;
    private final int resourceIdDescription;

    BodyPart(int drawableResourceForeground, int drawableResourceBackground, int resourceIdDescription) {
        this.drawableResourceForeground = drawableResourceForeground;
        this.drawableResourceBackground = drawableResourceBackground;
        this.resourceIdDescription = resourceIdDescription;
    }

    BodyPart() {
        this(-1, -1, -1);
    }

    public int getDrawableResourceForeground() { return drawableResourceForeground; }
    public int getDrawableResourceBackground() { return drawableResourceBackground; }

    public int getResourceIdDescription() {
        return resourceIdDescription;
    }
}
