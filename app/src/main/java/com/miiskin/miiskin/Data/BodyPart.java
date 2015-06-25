package com.miiskin.miiskin.Data;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 25.06.2015.
 */
public enum BodyPart {
    Hand,
    LeftForeArm,
    LeftUpperArm (R.drawable.left_upper_arm_foreground, R.drawable.left_upper_arm_bacground),
    RightForeArm ,
    RightUpperArm (R.drawable.right_upper_arm_foreground, R.drawable.right_upper_arm_background),
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

    BodyPart(int drawableResourceForeground, int drawableResourceBackground) {
        this.drawableResourceForeground = drawableResourceForeground;
        this.drawableResourceBackground = drawableResourceBackground;
    }

    BodyPart() {
        this(-1, -1);
    }

    public int getDrawableResourceForeground() { return drawableResourceForeground; }
    public int getDrawableResourceBackground() { return drawableResourceBackground; }

}
