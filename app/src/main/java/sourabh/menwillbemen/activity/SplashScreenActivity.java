package sourabh.menwillbemen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.AppController;
import sourabh.menwillbemen.app.CacheRequest;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.LanguageData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.NotificationUtils;
import sourabh.menwillbemen.helper.SessionManager;

import static sourabh.menwillbemen.R.id.textView;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_LANGUAGE_SELECTED;
import static sourabh.menwillbemen.app.AppConfig.URL_GET_DASHBOARD;

public class SplashScreenActivity extends BaseActivity {

    SessionManager sessionManager;
    Context context;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private long mRequestStartTime;
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;
    //    int themeColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;
        sessionManager = new SessionManager(context);
//        themeColor = sessionManager.getThemeColor();
        setBroadcastListener();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        checkPermissions();


        getSupportActionBar().hide();
        if(!sessionManager.checkFirstRun()){

            getLanguages(false);

        }else{


            getDashBoard(false);


        }

        Log.d(TAG,"onCreate");


//        YoYo.with(Techniques.Hinge)
//                .duration(2000)
//                .repeat(1)
//                .playOn(findViewById(R.id.imageViewIcon));

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




    public void getLanguages(boolean useCache)
    {
        mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.

        Log.d(TAG,"getLanguages");

        Volley.newRequestQueue(this).add(new CustomRequest(this,this,
                false, Request.Method.GET,
                AppConfig.URL_GET_LANUGAGES,useCache,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                        Log.d(TAG,"getLanguages got responce in : "+totalRequestTime );

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

                long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                Log.d(TAG,"getLanguages got error in : "+totalRequestTime );

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }


    public void getDashBoard(boolean useCache)
    {

        mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.


        String url = AppConfig.URL_GET_DASHBOARD+sessionManager.getSelectedLanguageId();


//        CacheRequest cacheRequest = new CacheRequest(this, this,
//                false,
//                Request.Method.GET,
//                url,
//                CommonUtilities.buildBlankParams(),
//                CommonUtilities.buildGuestHeaders(),
//                new Response.Listener<NetworkResponse>() {
//                    @Override
//                    public void onResponse(NetworkResponse response) {
//
//
//                        try {
//                            long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
//                            Log.d(TAG,"getDashBoard got responce in : "+totalRequestTime );
//
//                            final String jsonString = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers));
//                            JSONObject jsonObject = new JSONObject(jsonString);
//
//                            JsonSeparator js= new JsonSeparator(context,jsonObject);
//
//                            parseDashBoardJson(js.getData());
//
//
//                        } catch (UnsupportedEncodingException | JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });


//        cacheRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // Add the request to the RequestQueue.
//        AppController.getInstance().addToRequestQueue(cacheRequest);


        CustomRequest customRequest = new CustomRequest(this,this,
                false, Request.Method.GET,
                url,useCache,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                        Log.d(TAG,"getDashBoard got responce in : "+totalRequestTime );

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



                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    CommonUtilities.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", Boolean.valueOf(false));


                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView().getRootView(), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                            .setAction( R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbar1 = Snackbar.make(getWindow().getDecorView().getRootView(), "Retrying!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                    getDashBoard(false);
                                }
                            });

                    snackbar.show();


                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }


                long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                Log.d(TAG,"getDashBoard got error in : "+totalRequestTime );


            }
        });

        AppController.getInstance().addToRequestQueue(customRequest,AppConfig.URL_GET_DASHBOARD);

    }

    public void parseDashBoardJson(JSONObject jsonObject){

        gotoHome(CommonUtilities.getObjectFromJson(jsonObject, PostsData.class));

        Log.d(TAG,"parseDashBoardJson");
    }



    public void parseLangauageJson(JSONObject jsonObject){

        Log.d(TAG,"parseLangauageJson");

        sessionManager.setFirstRun();
        sessionManager.setLangauges(jsonObject);
        showChooseLanguageDalog();

    }


    void setBroadcastListener(){



        Log.d(TAG,"setBroadcastListener");


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

        Log.d(TAG,"onResume");

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

        Log.d(TAG,"showChooseLanguageDalog");

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

                        LanguageData selectedLanguageData = languageDataList.get(which);


                        Bundle bundle = new Bundle();
                        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, selectedLanguageData.getId_language());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, selectedLanguageData.getLanguage_title());
                        mFirebaseAnalytics.logEvent(FIREBASE_KEY_LANGUAGE_SELECTED, bundle);
                        mFirebaseAnalytics.setUserProperty(FIREBASE_KEY_LANGUAGE_SELECTED, selectedLanguageData.getLanguage_title());


                        FirebaseMessaging.getInstance().subscribeToTopic(selectedLanguageData.getLanguage_code());
                        sessionManager.setSelectedLanguage(
                                selectedLanguageData.getId_language(),
                                selectedLanguageData.getLanguage_title(),
                                selectedLanguageData.getLanguage_code());


                        gotoHome(null);
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }


    void gotoHome(PostsData postsData)
    {

        Log.d(TAG,"gotoHome");

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
