package sourabh.menwillbemen.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import com.google.android.gms.ads.MobileAds;

import sourabh.menwillbemen.R;



public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//        int themeColor = PreferenceManager.
//                getDefaultSharedPreferences(getApplicationContext())
//                .getInt("theme_color", Color.YELLOW);
//        String strColor = String.format("#%06X", 0xFFFFFF & themeColor);
//

        MobileAds.initialize(this,getString(R.string.admob_app_id));


        super.onCreate(savedInstanceState);

    }
}
