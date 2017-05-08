package sourabh.menwillbemen.activity;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.CategoryData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.data.SettingData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;

public class PostsActivity extends AppCompatActivity
    {


        Context context;
        PostsData postsData;

        List<CategoryData> categoryDataList;

        List<SettingData> settingDataList;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

//        FloatingActionButton fab = (FloatingActionButton) findViewById(fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override  
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getDashBoard();
    }


    public void getDashBoard()
    {
        Volley.newRequestQueue(this).add(new CustomRequest(this,this,
                true, Request.Method.GET, AppConfig.URL_GET_DASHBOARD,
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
        postsData = CommonUtilities.getObjectFromJson(jsonObject, PostsData.class);

        categoryDataList = postsData.getCategories();
        settingDataList = postsData.getSettings();



        setFragments();
//        setupDrawer(savedInstanceState);


    }


//    public PostsData getPostsDataFromActivity() {
//        return postsData;
//    }

    void   setFragments(){

        Bundle argsLatest = new Bundle();
        argsLatest.putBoolean(AppConfig.ARG_PARAM_IS_LATEST_POST_FRAGMENT,true);
        Bundle argsTop = new Bundle();
        argsTop.putBoolean(AppConfig.ARG_PARAM_IS_LATEST_POST_FRAGMENT,false);

        argsTop.putBoolean(AppConfig.ARG_PARAM_IS_HOME,false);
        argsLatest.putBoolean(AppConfig.ARG_PARAM_IS_HOME,false);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Latest", LatestFragment.class,argsLatest)
                .add("Top 50", LatestFragment.class,argsTop)

                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }





    }

