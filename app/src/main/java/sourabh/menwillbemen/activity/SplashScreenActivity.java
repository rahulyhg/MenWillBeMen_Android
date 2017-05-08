package sourabh.menwillbemen.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.LanguageData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.SessionManager;

public class SplashScreenActivity extends BaseActivity {

    SessionManager sessionManager;
    Context context;
//    int themeColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;
        sessionManager = new SessionManager(context);
//        themeColor = sessionManager.getThemeColor();


        getSupportActionBar().hide();
        if(!sessionManager.checkFirstRun()){

            getLanguages();

        }else{
            getDashBoard();
        }
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

    void showChooseLanguageDalog()
    {

        final List<LanguageData> languageDataList = (sessionManager.getLangauges());

        new MaterialDialog.Builder(this)
                .title("Choose Language")
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
