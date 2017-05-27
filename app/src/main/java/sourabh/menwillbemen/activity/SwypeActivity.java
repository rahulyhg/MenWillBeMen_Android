package sourabh.menwillbemen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.SessionManager;
import sourabh.menwillbemen.helper.Util;

import static android.R.attr.type;
import static sourabh.menwillbemen.app.AppConfig.ARG_PARAM_CATEGORY_ID;
import static sourabh.menwillbemen.app.AppConfig.ARG_PARAM_CURRENT_ITEM_INDEX;
import static sourabh.menwillbemen.app.AppConfig.ARG_PARAM_POSITION;
import static sourabh.menwillbemen.app.AppConfig.ARG_PARAM_POST_DATA;
import static sourabh.menwillbemen.app.AppConfig.ARG_PARAM_TYPE;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_APP_SHARED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_CATEGORY;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_LIKED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_SHARED_TO_EXTERNAL;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_SHARED_TO_WHATSAPP;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_UNLIKED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_USER_ID;
import static sourabh.menwillbemen.app.AppConfig.KEY_LIKE;
import static sourabh.menwillbemen.app.AppConfig.KEY_SHARE;
import static sourabh.menwillbemen.app.AppConfig.KEY_UNLIKE;
import static sourabh.menwillbemen.app.AppConfig.KEY_WHATSAPP;
import static sourabh.menwillbemen.app.AppConfig.URL_GET_POSTS;
import static sourabh.menwillbemen.app.AppConfig.URL_LIKE_POST;
import static sourabh.menwillbemen.app.AppConfig.URL_UNLIKE_POST;
import static sourabh.menwillbemen.app.AppConfig.URL_UPDATE_SHARE_COUNT;
import static sourabh.menwillbemen.app.AppConfig.URL_UPDATE_WHATSAPP_COUNT;


public class SwypeActivity extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    List<PostItemData> postItemDataList;
    int position;
    Context context;
    PostItemData postItemData;
    String background_image;

    LinearLayout linearLayoutBackground;
    SharedPreferences sharedPref     ;

    String transformerPackage = "com.eftimoff.viewpagertransformers.";
//    String transformerPackage = "com.ToxicBakery.viewpager.transforms.";

    SessionManager sessionManager;
    String categoryId;
    int pageNo = 1;
    int interstitialAdCounter = 1;
    PostsData postsData;
    boolean endOFPosts;
    InterstitialAd mInterstitialAd;
    String type;
    String TAG = SwypeActivity.class.getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;
    Snackbar snackbar;



    final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            BitmapDrawable bg = new BitmapDrawable(context.getResources(), bitmap);
            bg.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            linearLayoutBackground.setBackgroundDrawable(bg);

            Log.d(TAG,"onBitmapLoaded");

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swype);
        Log.d(TAG,"onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPagerArrowIndicator viewPagerArrowIndicator = (ViewPagerArrowIndicator) findViewById(R.id.viewPagerArrowIndicator);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        linearLayoutBackground = (LinearLayout) findViewById(R.id.detailed_post_layout);

        context = getApplicationContext();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sessionManager = new SessionManager(context);
        Fabric.with(this, new Crashlytics());

        postItemDataList = (List<PostItemData>) getIntent().getSerializableExtra(ARG_PARAM_POST_DATA);
        position = getIntent().getIntExtra(ARG_PARAM_POSITION,0);
        categoryId = getIntent().getStringExtra(ARG_PARAM_CATEGORY_ID);
        type = getIntent().getStringExtra(ARG_PARAM_TYPE);




        setAdapter();
        setSwypeAnimation();
        initialiseInterstitialAd();

        background_image = sessionManager.getBackgroundImage();
        if(background_image != null){
            loadBackgroundImage(linearLayoutBackground);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    void setAdapter()
    {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),postItemDataList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setCurrentItem(position);
//        viewPagerArrowIndicator.bind(mViewPager);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //declare key
            Boolean first = true;

            @Override
            public void onPageSelected(int page_no) {


                interstitialAdCounter ++;
                if(interstitialAdCounter % sessionManager.getInterstitialIterations() == 0){
                    showInterstitial();
                    initialiseInterstitialAd();
                }

                if (mViewPager.getCurrentItem() == postItemDataList.size() -1  ) {
                    // left
//                    CommonUtilities.showShortToast(context,"came to end");
                    pageNo++;

                    if(!endOFPosts){
                        getPosts(String.valueOf(categoryId),pageNo,false);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (first && positionOffset == 0 && positionOffsetPixels == 0){
                    onPageSelected(0);
                    first = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }

    void initialiseInterstitialAd(){

        Log.d(TAG,"initialiseInterstitialAd");

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BCDCAE3C6F97EF417DD1D5FFB4F86E3E")
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(context,"onAdClosed",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(context,"onAdFailedToLoad",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Toast.makeText(context,"onAdLeftApplication",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(context,"onAdOpened",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                showInterstitial();

                Toast.makeText(context,"onAdLoaded",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void onItemChanged() {
        Log.d(TAG,"onItemChanged");

        mSectionsPagerAdapter.notifyDataSetChanged();

    }



    public void  getPosts(String category_id, int pageNo, boolean shouldCache)
    {



        String URL = URL_GET_POSTS
                +sessionManager.getSelectedLanguageId()+"/"
                +category_id+"/"
                +type+"/"
                +String.valueOf(pageNo);

        Log.d(TAG,"getPosts "+URL);


//        final YoYo.YoYoString yoYo =  YoYo.with(Techniques.Pulse)
//                                .duration(1000)
//                                .repeat(AppConfig.LARGE_NUMBER)
//                                .playOn(mViewPager);


        snackbar = Snackbar
                .make(getWindow().getDecorView().getRootView(),
                        R.string.loading_more, Snackbar.LENGTH_INDEFINITE)
                ;

        snackbar.show();


        Volley.newRequestQueue(context).add(new CustomRequest(this,this,
                false, Request.Method.GET, URL,shouldCache,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildHeaders(context),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
//                        yoYo.stop();
                        snackbar.dismiss();

                        Log.d(TAG,"getPosts got responce ");

                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){
//                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
//                                endOFPosts();
                            }else{

                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
                                parsePostsJson(js.getData());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
//                yoYo.stop();
                snackbar.dismiss();

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }


    public void  parsePostsJson(JSONObject jsonObject){

        Log.d(TAG,"parsePostsJson");

        postsData = CommonUtilities.getObjectFromJson(jsonObject, PostsData.class);

        if(type.equals(AppConfig.KEY_TYPE_LATEST)){

            if(postsData.getLatest().size()==0) {
                endOfPosts();
                snackbar.show();
            }else{
                postItemDataList.addAll(addColorsToPosts(postsData.getLatest()));
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
        }

        if(type.equals(AppConfig.KEY_TYPE_TOP)){

            if(postsData.getTop().size()==0) {
                endOfPosts();
                snackbar.show();

            }else{
                postItemDataList.addAll(addColorsToPosts(postsData.getTop()));
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
        }



    }

    void endOfPosts(){

        endOFPosts = true;
        snackbar = Snackbar
                .make(getWindow().getDecorView().getRootView(),
                        R.string.no_more_msgs, Snackbar.LENGTH_SHORT)
        ;

    }

    public int totalItems(){
        return postItemDataList.size();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<PostItemData> postItemDataList;

        public SectionsPagerAdapter(FragmentManager fm, List<PostItemData> postItemDataList)
        {
            super(fm);
            this.postItemDataList = postItemDataList;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d(TAG,"getItem");
            return PlaceholderFragment.newInstance(postItemDataList.get(position),position+1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return postItemDataList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    List<PostItemData> addColorsToPosts(List<PostItemData> postItemDatas){

        Log.d(TAG,"addColorsToPosts");

//        List<PostItemData> postItemDataList = new ArrayList<>();
        for (PostItemData postItemData:postItemDatas) {

            postItemData.setCard_color(Util.getRandomColor());
//            postItemDataList.add(postItemData);
        }
        return postItemDatas;
    }


    void setSwypeAnimation(){
        String swype_animation = (sharedPref.getString
                (getResources().getString(R.string.pref_key_swype_animation), ""));


        try {
            String classname = "com.eftimoff.viewpagertransformers."+swype_animation;
            Class<?> cls = Class.forName(classname);
            Object clsInstance = (Object) cls.newInstance();
            mViewPager.setPageTransformer(true, (ViewPager.PageTransformer) clsInstance);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadBackgroundImage(final LinearLayout relativeLayoutList) {



        Picasso.with(context).load(background_image).
                into(target);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.



        getMenuInflater().inflate(R.menu.menu_swype, menu);
        menu.findItem(R.id.action_swype_animation).setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_style)
                .color(Color.WHITE)
                .sizeDp(24));

        menu.findItem(R.id.action_share).setIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_share)
                .color(Color.WHITE)
                .sizeDp(20));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_swype_animation) {

            final String animationsValue[] =getResources().getStringArray(R.array.SwypeAnimationsDescription);
            final String animationsKey[] =getResources().getStringArray(R.array.SwypeAnimations);

            int activeIndex = Arrays.asList(animationsKey)
                    .indexOf(sharedPref.getString(getResources().getString(R.string.pref_key_swype_animation),""));

            new MaterialDialog.Builder(this)
                    .title("Choose Style")
                    .items(animationsValue)
                    .itemsCallbackSingleChoice(activeIndex, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/


                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getResources().getString(R.string.pref_key_swype_animation),animationsKey[which]);
                            editor.commit();

                            setSwypeAnimation();
                            return true;
                        }
                    })
                    .show();
            return true;
        }

        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            String text = "Download App";
            sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
            sendIntent.setType("text/plain");

            startActivity(sendIntent);



            Bundle bundle = new Bundle();
            bundle.putString(FIREBASE_KEY_USER_ID, sessionManager.getUserId());
            mFirebaseAnalytics.logEvent(FIREBASE_KEY_APP_SHARED, bundle);


//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));

            Log.d(TAG,"share clicked");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        ScrollView scrollPost;
        @BindView(R.id.txtStatusMsg)
        TextView statusMsg;

        @BindView(R.id.card_detailed_post)
        LinearLayout cardView;

        @BindView(R.id.adView)
        AdView mAdView;

        @BindView(R.id.txtCountWhatsapp)
        TextView countWhatsApp;
        @BindView(R.id.count_share)
        TextView countShare;

        @BindView(R.id.count_likes)
        TextView countLikes;

        @BindView(R.id.txtCount)
        TextView txtCount;

        @BindView(R.id.whatsapp_button)
        IconicsImageView whatsapp_button;

        @BindView(R.id.share_button)
        IconicsImageView share_button;

        @BindView(R.id.thumb_button)
        LikeButton thumb_button;

        @BindView(R.id.category)
        protected TextView category;

        @BindView(R.id.relativeTimestamp)
        protected RelativeTimeTextView relativeTimestamp;


        Float textSize;

        private static final String ARG_SECTION_NUMBER = "section_number";

        Context context;
        AdRequest adRequest;

        SharedPreferences sharedPref;
        private FirebaseAnalytics mFirebaseAnalytics;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(PostItemData postItemData, int current_item_index)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM_POST_DATA, postItemData);
//            args.putInt(ARG_PARAM_TOTAL_ITEMS, total_items);
            args.putInt(ARG_PARAM_CURRENT_ITEM_INDEX, current_item_index);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_swype, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            ScrollView scrollPost = (ScrollView) rootView.findViewById(R.id.scrollPost);
//
//            TextView statusMsg = (TextView) rootView.findViewById(R.id.txtStatusMsg);
//            CardView cardView = (CardView) rootView.findViewById(R.id.card_detailed_post);
//            AdView adView = (AdView) rootView.findViewById(R.id.adView);
//            TextView countWhatsApp = (TextView) rootView.findViewById(R.id.txtCountWhatsapp);
//            TextView countShare = (TextView) rootView.findViewById(R.id.count_share);
//            TextView countLikes = (TextView) rootView.findViewById(R.id.count_likes);
//            IconicsImageView whatsapp_button = (IconicsImageView) rootView.findViewById(R.id.whatsapp_button);
//            IconicsImageView share_button = (IconicsImageView) rootView.findViewById(R.id.share_button);
//            LikeButton thumb_button = (LikeButton) rootView.findViewById(R.id.thumb_button);






            ButterKnife.bind(this, rootView);
            context = getContext();
            adRequest = new AdRequest.Builder().build();
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);


            final PostItemData postItemData = (PostItemData) getArguments().getSerializable(ARG_PARAM_POST_DATA);
//            final int  total_items =  getArguments().getInt(ARG_PARAM_TOTAL_ITEMS);
            final int current_item_index =  getArguments().getInt(ARG_PARAM_CURRENT_ITEM_INDEX);

            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

            try{
                textSize = Float.valueOf(sharedPref.getString
                        (getResources().getString(R.string.pref_key_fontsize), ""));

            }catch (Exception ex){

            }



            int color = postItemData.getCard_color();


            countWhatsApp.setText(CommonUtilities.format(
                    Long.parseLong(postItemData.getPost_whatsapp_count().toString())));
            countShare.setText(CommonUtilities.format(
                    Long.parseLong(postItemData.getPost_share_count().toString())));
            countLikes.setText(CommonUtilities.format(
                    Long.parseLong(postItemData.getPost_likes_count().toString())));
            category.setText(postItemData.getCategory_name().toString());
            txtCount.setText(current_item_index+" / "+((SwypeActivity)getActivity()).totalItems());


            if(postItemData.getIs_liked() == 1){
                thumb_button.setLiked(true);

            }else{
                thumb_button.setLiked(false);
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date = sdf.parse(postItemData.getPost_created_on());
                relativeTimestamp.setReferenceTime(date.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }


            cardView.setBackgroundColor(color);
            whatsapp_button.setColor(color);
            share_button.setColor(color);




            if (!TextUtils.isEmpty(postItemData.getPost())) {

//                int maxLines = MAX_LINES;
//                statusMsg.setMaxLines(maxLines);
//                statusMsg.setEllipsize(TextUtils.TruncateAt.END);
                statusMsg.setText(postItemData.getPost());

                if(textSize != null){
                    statusMsg.setTextSize(textSize);
                }

                statusMsg.setVisibility(View.VISIBLE);
            } else {
                // status is empty, remove from view
                statusMsg.setVisibility(View.GONE);
            }



            thumb_button.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    postItemData.setIs_liked(1);
                    postItemData.setPost_likes_count(postItemData.getPost_likes_count()+1);
                    ((SwypeActivity )  getActivity()).onItemChanged();

//                    countLikes.setText(Integer.parseInt(countLikes.getText().toString())+1);
                    updateCount(postItemData.getId_post(),KEY_LIKE);


                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
                    bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
                    mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_LIKED, bundle);

                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    postItemData.setIs_liked(0);
                    postItemData.setPost_likes_count(postItemData.getPost_likes_count()-1);
                    ((SwypeActivity )  getActivity()).onItemChanged();
//                    countLikes.setText(Integer.parseInt(countLikes.getText().toString())-1);

                    updateCount(postItemData.getId_post(),KEY_UNLIKE);


                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
                    bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
                    mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_UNLIKED, bundle);


                }
            });

            whatsapp_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);

                    String text = postItemData.getPost();
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");

                    postItemData.setPost_whatsapp_count(postItemData.getPost_whatsapp_count()+1);
                    ((SwypeActivity )  getActivity()).onItemChanged();


                    startActivity(sendIntent);
                    updateCount(postItemData.getId_post(),KEY_WHATSAPP);


                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
                    bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
                    mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_SHARED_TO_WHATSAPP, bundle);

                }
            });

            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);

                    String text = postItemData.getPost();
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                    sendIntent.setType("text/plain");


                    postItemData.setPost_share_count(postItemData.getPost_share_count()+1);
                    ((SwypeActivity )  getActivity()).onItemChanged();

                    startActivity(sendIntent);

                    updateCount(postItemData.getId_post(),KEY_SHARE);

//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));

                    Bundle bundle = new Bundle();
                    bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
                    bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
                    mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_SHARED_TO_EXTERNAL, bundle);

                }
            });





            showAd();


            return rootView;
        }


        void updateCount(int post_id, String what){

            what = what.trim();

            Map<String, String> params = new HashMap<String, String>();
            params.put("post_id", post_id+"");

            String url = "";

            if(what.equals(KEY_WHATSAPP.trim())){
                url = URL_UPDATE_WHATSAPP_COUNT;
            }
            else if(what.equals(KEY_SHARE.trim()))
            {
                url = URL_UPDATE_SHARE_COUNT;
            }
            else if(what.equals(KEY_LIKE.trim())){
                url = URL_LIKE_POST;
            }
            else{
                url = URL_UNLIKE_POST;
            }

            Volley.newRequestQueue(context).add(new CustomRequest(getActivity(),getActivity(),
                    false, Request.Method.POST, url,false,
                    params, CommonUtilities.buildHeaders(context),


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




        void showAd(){


//            mAdView = (AdView) findViewById(R.id.adView);
            adRequest = new AdRequest.Builder()
                    .addTestDevice("BCDCAE3C6F97EF417DD1D5FFB4F86E3E")
                    .build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
//                Toast.makeText(getActivity(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClosed() {
//                Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
//                Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
//                Toast.makeText(getActivity(), "Ad is opened!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(ARG_PARAM_POST_DATA, (Serializable) postItemDataList);
        // add data to Intent
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }


}
