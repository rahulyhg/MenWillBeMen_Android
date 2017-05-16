package sourabh.menwillbemen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sourabh.menwillbemen.Manifest;
import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.LanguageData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.NotificationUtils;
import sourabh.menwillbemen.helper.SessionManager;

public class SplashScreenActivity extends BaseActivity {

    SessionManager sessionManager;
    Context context;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //    int themeColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;
        sessionManager = new SessionManager(context);
//        themeColor = sessionManager.getThemeColor();
        setBroadcastListener();

        checkPermissions();


        getSupportActionBar().hide();
        if(!sessionManager.checkFirstRun()){

            getLanguages();

        }else{
            getDashBoard();
        }


    }

    void checkPermissions(){



        Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.READ_CONTACTS,
                        android.Manifest.permission.GET_ACCOUNTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
//                .withErrorListener(new Mul)
                .check();

//            Dexter.withActivity(this)
//                    .withPermissions(
//                            android.Manifest.permission.READ_CONTACTS,
//                            android.Manifest.permission.GET_ACCOUNTS
//                    ).withListener(new MultiplePermissionsListener() {
//                @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//
//
//                }
//                @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//                    token.continuePermissionRequest();
//
//                }
//            }).check();

    }




    public void getLanguages()
    {
        Volley.newRequestQueue(this).add(new CustomRequest(this,this,
                false, Request.Method.GET,
                AppConfig.URL_GET_LANUGAGES,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){

                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }else{

                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
                                parseLangauageJson(js.getData());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }


    public void getDashBoard()
    {
        Volley.newRequestQueue(this).add(new CustomRequest(this,this,
                true, Request.Method.GET,
                AppConfig.URL_GET_DASHBOARD+sessionManager.getSelectedLanguageId(),
                CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){

                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }else{

                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
                                parseDashBoardJson(js.getData());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }

    public void parseDashBoardJson(JSONObject jsonObject){

        gotoHome(CommonUtilities.getObjectFromJson(jsonObject, PostsData.class));

    }



    public void parseLangauageJson(JSONObject jsonObject){


        sessionManager.setFirstRun();
        sessionManager.setLangauges(jsonObject);
        showChooseLanguageDalog();

    }


    void setBroadcastListener(){





        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(AppConfig.KEY_REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.KEY_TOPIC_GLOBAL);

                } else if (intent.getAction().equals(AppConfig.KEY_PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.KEY_REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.KEY_PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    void showChooseLanguageDalog()
    {

        final List<LanguageData> languageDataList = (sessionManager.getLangauges());

        new MaterialDialog.Builder(this)
                .title("Choose Language")
                .content("You can change it latter")
                .cancelable(false)
                .items(CommonUtilities.convertLangaugeListToLanguageArray
                        (languageDataList))
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        sessionManager.setSelectedLanguageId(languageDataList.get(which).getId_language());

                        gotoHome(null);
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }


    void gotoHome(PostsData postsData)
    {

        if(postsData == null){
            startActivity(new Intent(SplashScreenActivity.this,HomeActivity.class));
            finish();
        }else{
            startActivity(new Intent(SplashScreenActivity.this,HomeActivity.class)
            .putExtra(AppConfig.ARG_PARAM_POST_DATA,postsData));
            finish();
        }

    }



}
