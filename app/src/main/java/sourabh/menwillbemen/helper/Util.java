package sourabh.menwillbemen.helper;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.List;
import java.util.Random;

import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppController;
import sourabh.menwillbemen.data.SettingData;


/**
 * Created by Kajo on 2/3/2017.
 */

public class Util {

    public  static int getRandomColor()
    {

//        int[] androidColors = AppController.getInstance().getResources().getIntArray(R.array.androidcolors);
//        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//        return randomAndroidColor;

        SessionManager sessionManager = new SessionManager(AppController.getInstance().getApplicationContext());

        Object[] androidColors = sessionManager.getCardColors().toArray();
        String randomAndroidColor = ((String)androidColors[new Random().nextInt(androidColors.length)]);

//        return getMatColor(context,"500");
        return Color.parseColor(randomAndroidColor);
    }

    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        int d;
        if (a < 0.5) {
            d = 0; // bright colors - black font
        } else {
            d = 255; // dark colors - white font
        }

        return Color.rgb(d, d, d);
    }
    public static int getMatColor(Context context,String typeColor)
    {
        int returnColor = Color.BLACK;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

}
