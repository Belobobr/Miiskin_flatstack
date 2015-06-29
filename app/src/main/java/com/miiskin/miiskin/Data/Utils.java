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
}
