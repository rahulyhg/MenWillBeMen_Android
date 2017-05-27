package sourabh.menwillbemen.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.clockbyte.admobadapter.expressads.AdmobExpressRecyclerAdapterWrapper;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
//import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import sourabh.menwillbemen.R;

import sourabh.menwillbemen.adapter.PostRecyclerViewAdapter;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.AppController;
import sourabh.menwillbemen.app.CacheRequest;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.SessionManager;
import sourabh.menwillbemen.helper.Util;

import static android.media.CamcorderProfile.get;
import static java.security.AccessController.getContext;
import static sourabh.menwillbemen.R.id.latest_fragment;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_CATEGORY;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_LIKED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_SHARED_TO_EXTERNAL;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_SHARED_TO_WHATSAPP;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_POST_UNLIKED;
import static sourabh.menwillbemen.app.AppConfig.FIREBASE_KEY_TAPPED_ON_DETAILED_POST;
import static sourabh.menwillbemen.app.AppConfig.KEY_LIKE;
import static sourabh.menwillbemen.app.AppConfig.KEY_SHARE;
import static sourabh.menwillbemen.app.AppConfig.KEY_WHATSAPP;
import static sourabh.menwillbemen.app.AppConfig.URL_LIKE_POST;
import static sourabh.menwillbemen.app.AppConfig.URL_UPDATE_SHARE_COUNT;
import static sourabh.menwillbemen.app.AppConfig.URL_UPDATE_WHATSAPP_COUNT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LatestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LatestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LatestFragment extends android.support.v4.app.Fragment implements PostRecyclerViewAdapter.AdapterCallBack,
        SharedPreferences.OnSharedPreferenceChangeListener



{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_POST_DATA = "POST_DATA";

    // TODO: Rename and change types of parameters

    //    private ListView  recyclerView;
    private AdView mAdView;

    int pageNo = 1;
    boolean apppend = false;


    XRecyclerView recyclerView;
    PostRecyclerViewAdapter postRecyclerViewAdapter;

    List<Object> mRecyclerViewItems = new ArrayList<>();

    boolean isLatestFragment;
    View view;
    PostsData postsData;
    Context context;
    SessionManager sessionManager;
    Float textSize = null;
    String background_image;

    boolean toAppendOrRefresh;
    boolean isBlankRefresh;
    RelativeLayout relativeLayoutList;
    AdmobExpressRecyclerAdapterWrapper adapterWrapper;
    String TAG = LatestFragment.class.getSimpleName();
    private long mRequestStartTime;
    String type;

    private FirebaseAnalytics mFirebaseAnalytics;

    Activity activity;
    boolean firstTimeGetPosts = true;
    FrameLayout latest_fragment;
    Snackbar snackbar;

    final Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

//            Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
//            relativeLayoutList.startAnimation(fadeIn);

            BitmapDrawable bg = new BitmapDrawable(context.getResources(), bitmap);
            bg.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            relativeLayoutList.setBackgroundDrawable(bg);

//            fadeIn.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//
//
//
//                    Animation fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
//                    relativeLayoutList.startAnimation(fadeOut);
//                }
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });




            Log.d(TAG,"Bitmap Loaded");
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    // A Native Express ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 5;

    // The Native Express ad height.
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;
    private static final int NATIVE_EXPRESS_AD_WIDTH = 320;

    // The Native Express ad unit ID.
//    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";



    private OnFragmentInteractionListener mListener;
    public LatestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postsData Parameter 1.
     * @return A new instance of fragment LatestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LatestFragment newInstance(PostsData postsData) {
        LatestFragment fragment = new LatestFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_POST_DATA,postsData);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            postsData = (PostsDa ta) getArguments().getSerializable(ARG_PARAM_POST_DATA);
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG,"onActivityCreated");

        view.post(new Runnable() {
            public void run() {
            /* the desired UI update */
                showAd(view);
                initialiseRecyclerView();

//                loadRecyclerViewData();
            }
        });

        // Update the RecyclerView item's list with menu items and Native Express ads.


        Fabric.with(activity, new Crashlytics());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView");

        view = inflater.inflate(R.layout.fragment_latest, container, false);
        recyclerView = (XRecyclerView) view.findViewById(R.id.list);
        relativeLayoutList = (RelativeLayout) view.findViewById(R.id.RelativeLayoutList);
        latest_fragment = (FrameLayout) view.findViewById(R.id.latest_fragment);


        context = getContext();
        activity = getActivity();
        sessionManager = new SessionManager(context);

        MobileAds.initialize(context, getString(R.string.admob_app_id));

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);



        Bundle bundle = this.getArguments();

        if (bundle != null) {
            isLatestFragment = this.getArguments().getBoolean(AppConfig.ARG_PARAM_IS_LATEST_POST_FRAGMENT);
//            background_image = this.getArguments().getString(AppConfig.ARG_BACKGROUND_IMAGE);

        }

        if(isLatestFragment){
            type = AppConfig.KEY_TYPE_LATEST;
        }else{
            type = AppConfig.KEY_TYPE_TOP;
        }
        getPosts(((HomeActivity)activity).getActiveCategoryId(), type,false,pageNo,false);


        background_image = sessionManager.getBackgroundImage();
        if(background_image != null){
            loadBackgroundImage(relativeLayoutList);
        }







        return view;
    }

    private void loadBackgroundImage(final RelativeLayout relativeLayoutList) {



        Picasso.with(activity).load(background_image).
        into(target);

    }


    void initialiseRecyclerView(){
        Log.d(TAG,"initialiseRecyclerView");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(context,getString(R.string.native_medium_ad_unit_id),
                new AdSize(AdSize.FULL_WIDTH,150))
        {

            @NonNull
            @Override
            protected ViewGroup getAdViewWrapper(ViewGroup parent) {
                //return some inflated ViewGroup for wrapping ad view into it
                return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container,
                        parent, false);
            }

            // it's necessary to override this method if wrapper (R.layout.native_express_ad_container) has complex layout, at least ad view is not among of it's direct children. See super implementation, maybe it's enough for you
            @Override
            protected void recycleAdViewWrapper(@NonNull ViewGroup wrapper, @NotNull NativeExpressAdView ad) {
                //get the view which directly will contain ad
                ViewGroup container = (ViewGroup) wrapper.findViewById(R.id.adViewNative);
                /**iterating through all children of the container view and remove the first occured {@link NativeExpressAdView}. It could be different with {@param ad}!!!*/
                for (int i = 0; i < container.getChildCount(); i++) {
                    View v = container.getChildAt(i);
                    if (v instanceof NativeExpressAdView) {
                        container.removeViewAt(i);
                        break;
                    }
                }
            }

            @Override
            protected void addAdViewToWrapper(@NonNull ViewGroup wrapper, @NotNull NativeExpressAdView ad) {
                super.addAdViewToWrapper(wrapper, ad);
                AdListener adListener = ad.getAdListener ();

                ad.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        CommonUtilities.showShortToast(context,"onAdClosed");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        CommonUtilities.showShortToast(context,"onAdFailedToLoad "+i);

                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        CommonUtilities.showShortToast(context,"onAdLeftApplication");

                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        CommonUtilities.showShortToast(context,"onAdOpened");

                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        CommonUtilities.showShortToast(context,"onAdLoaded");

                    }
                });
                // and see https://developers.google.com/android/reference/com/google/android/gms/ads/AdListener to get what you can do with :)
            }
        };

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);




//        recyclerView.setRefreshProgressStyle(ProgressStyle
//                .Pacman);
//        recyclerView.setLoadingMoreProgressStyle(ProgressStyle
//                .Pacman);

        recyclerView.setRefreshProgressStyle(sessionManager.getRefreshStyle());
        recyclerView.setLoadingMoreProgressStyle(sessionManager.getLoadingMoreStyle());



        try{
            textSize = Float.valueOf(sharedPref.getString
                    (activity.getResources().getString(R.string.pref_key_fontsize), ""));

        }catch (Exception ex){

        }

        postRecyclerViewAdapter = new PostRecyclerViewAdapter(activity,type, ((HomeActivity)activity).getActiveCategoryId(),
                mRecyclerViewItems,textSize,this,mFirebaseAnalytics);
        postRecyclerViewAdapter.setHasStableIds(true);

        adapterWrapper.setAdapter(postRecyclerViewAdapter);

        adapterWrapper.setLimitOfAds(AppConfig.LARGE_NUMBER);
        adapterWrapper.setNoOfDataBetweenAds(sessionManager.getNativeIterations());
        adapterWrapper.setFirstAdIndex(sessionManager.getNativeIterations());


        if(sharedPref.getBoolean(activity.getResources().getString(R.string.pref_key_animation),true)){

            SlideInRightAnimationAdapter alphaAdapter = new SlideInRightAnimationAdapter(adapterWrapper);
            alphaAdapter.setDuration(200);
            recyclerView.setAdapter(alphaAdapter);
        }else{
            recyclerView.setAdapter(adapterWrapper);
        }


        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here

                if(!isBlankRefresh){


                    pageNo = 1;
                    toAppendOrRefresh = true;
                    apppend = false;

                    recyclerView.setLoadingMoreEnabled(true);
                    getPosts(((HomeActivity)activity).getActiveCategoryId(), type,false,pageNo,false);
                }

            }

            @Override
            public void onLoadMore() {
                // load more data here
                pageNo = pageNo + 1;
                apppend = true ;
                toAppendOrRefresh = true;

                getPosts(((HomeActivity)activity).getActiveCategoryId(),type, false,pageNo,false);

            }
        });


        isBlankRefresh = true;
        recyclerView.refresh();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    ((HomeActivity)activity).hideFab();
                else if (dy < 0)
                    ((HomeActivity)activity).showFab();
            }
        });



    }

    void addHeaders(){

        View header = activity.getLayoutInflater().inflate(R.layout.drawer_header,null);
        recyclerView.addHeaderView(header);

    }
    void loadRecyclerViewData(){

        Log.d(TAG,"loadRecyclerViewData");


        if(mRecyclerViewItems != null){
            mRecyclerViewItems.clear();
        }



        if(isLatestFragment){
            mRecyclerViewItems.addAll(addColorsToPosts(postsData.getLatest()));
//            postRecyclerViewAdapter = new PostRecyclerViewAdapter(activity, mRecyclerViewItems,textSize);
        }else{
            mRecyclerViewItems.addAll(addColorsToPosts(postsData.getTop()));
//            postRecyclerViewAdapter = new PostRecyclerViewAdapter(activity, mRecyclerViewItems,textSize);
        }

//        addNativeExpressAds();
//        setUpAndLoadNativeExpressAds();


        postRecyclerViewAdapter.notifyDataSetChanged();


    }

    public void  updateRecyclerViewData(PostsData postsData){


        Log.d(TAG,"updateRecyclerViewData");

        if(apppend){

            if(isLatestFragment){

                if(postsData.getLatest().size()==0){

                    endOFPosts();
                }else{
                    mRecyclerViewItems.addAll(addColorsToPosts(postsData.getLatest()));
                }
            }else{
                if(postsData.getTop().size()==0){
                    endOFPosts();

                }else{
                    mRecyclerViewItems.addAll(addColorsToPosts(postsData.getTop()));
                }
            }

            recyclerView.loadMoreComplete();

        }else{

            if(isLatestFragment){
                mRecyclerViewItems.clear();
                mRecyclerViewItems.addAll(addColorsToPosts(postsData.getLatest()));
            }else{
                mRecyclerViewItems.clear();
                mRecyclerViewItems.addAll(addColorsToPosts(postsData.getTop()));
            }
            recyclerView.refreshComplete();


        }

//        addNativeExpressAds();
//        setUpAndLoadNativeExpressAds();

        postRecyclerViewAdapter.notifyDataSetChanged();


    }

    public void  categoryChanged(String categoryId) {

        Log.d(TAG,"categoryChanged");


        recyclerView.setLoadingMoreEnabled(true);

        pageNo = 1;
        toAppendOrRefresh = false;
        apppend = false;
        initialiseRecyclerView();
        getPosts(categoryId, type,false,pageNo,false);

    }
        void showAd(View view){


        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BCDCAE3C6F97EF417DD1D5FFB4F86E3E")

                .build();
        mAdView.loadAd(adRequest);
        Log.d("Home","showAd");

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                Toast.makeText(activity, "Ad is loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
//                Toast.makeText(activity, "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(activity, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
//                Toast.makeText(activity, "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
//                Toast.makeText(activity, "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * Adds Native Express ads to the items list.
     */
//    private void addNativeExpressAds() {
//
//        // Loop through the items array and place a new Native Express ad in every ith position in
//        // the items List.
////        ITEMS_PER_AD*((pageNo-1)*5)
//
////        int counter = (mRecyclerViewItems.size()-5 == 0) ? 0 : mRecyclerViewItems.size()-4;
//
//        for (int i = 0;  i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
//            final NativeExpressAdView adView = new NativeExpressAdView(context);
//            mRecyclerViewItems.add(i, adView);
//        }
//    }

    /**
     * Sets up and loads the Native Express ads.
     */
//    private void setUpAndLoadNativeExpressAds() {
//        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
//        // ad size for the Native Express ad. This allows us to set the Native Express ad's
//        // width to match the full width of the RecyclerView.
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                final float scale = context.getResources().getDisplayMetrics().density;
//                // Set the ad size and ad unit ID for each Native Express ad in the items list.
//
////                int counter = (mRecyclerViewItems.size()-(5*ITEMS_PER_AD+1) == 0) ? 0 : mRecyclerViewItems.size()-(5*ITEMS_PER_AD)+1;
//
//
//                for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
//                    final NativeExpressAdView adView =
//                            (NativeExpressAdView) mRecyclerViewItems.get(i);
//
//
//                    View child = activity.getLayoutInflater().inflate(R.layout.native_express_ad_container, null);
//
//                    final CardView cardView = (CardView)child.findViewById(R.id.ad_card_view);
//                    final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
//                            - cardView.getPaddingRight();
////                    AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
//                    AdSize adSize = new AdSize(NATIVE_EXPRESS_AD_WIDTH, NATIVE_EXPRESS_AD_HEIGHT);
//
//                    adView.setAdSize(adSize);
//                    adView.setAdUnitId(getResources().getString(R.string.native_ad_unit_id));
//                }
//
//                // Load the first Native Express ad in the items list.
//                loadNativeExpressAd(0);
//            }
//        });
//    }

    /**
     * Loads the Native Express ads in the items list.
     */
    private void loadNativeExpressAd(final int index) {

        if (index >= mRecyclerViewItems.size()) {
            return;
        }

        Object item = mRecyclerViewItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());
    }



//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    public void  getPosts(final String category_id, final String type, final boolean toShowLoading, final int pageNo, boolean useCache)
    {

        Log.d(TAG,"getPosts");
        mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.


        String URL = AppConfig.URL_GET_POSTS
                +sessionManager.getSelectedLanguageId()+"/"
                +category_id+"/"
                +type+"/"
                +String.valueOf(pageNo);


//        AppController.getInstance().getRequestQueue().getCache().invalidate(URL, true);


//        CacheRequest cacheRequest = new CacheRequest(activity, activity,
//                false,
//                Request.Method.GET,
//                URL,
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
//                            parsePostsJson(js.getData());
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
//
//
//
//        // Add the request to the RequestQueue.
//        AppController.getInstance().addToRequestQueue(cacheRequest);





        CustomRequest jsonObjReq = new CustomRequest(context,activity,
                toShowLoading, Request.Method.GET, URL,useCache,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildHeaders(context),



        new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;

                        Log.d(TAG,"getPosts got Responce "+type+" in time : "+totalRequestTime);

                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){
                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
//                                endOFPosts();
                            }else{

                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
                                parsePostsJson(js.getData());

//                                if(firstTimeGetPosts){
//
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            //Do something after 100ms
//                                            recyclerView.refresh();
//
//                                            getPosts(category_id,type,toShowLoading,pageNo,false);
//                                            firstTimeGetPosts = false;
//                                        }
//                                    }, 200);


//                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    CommonUtilities.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", Boolean.valueOf(false));


                    snackbar = Snackbar
                            .make(latest_fragment, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                            .setAction( R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbar1 = Snackbar.make(latest_fragment, "Retrying!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                    getPosts(category_id,type,toShowLoading,pageNo,false);
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
                Log.d(TAG,"getPosts got error in time : "+totalRequestTime);

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);





//        category_id = "null";
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//
//        Call<PostsData> call = apiService
//                .getPosts(sessionManager.getSelectedLanguageId()
//                        ,category_id,type
//                        ,String.valueOf(pageNo)
//                        ,sessionManager.getAPIKEY());
//
//        mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.
//
//        call.enqueue(new Callback<PostsData>() {
//            @Override
//            public void onResponse(Call<PostsData>call, Response<PostsData> response) {
////                List<Movie> movies = response.body().get();
//                 long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
//
//                CommonUtilities.showLongToast(context, "Retro "+String.valueOf(totalRequestTime));
//                 Log.d(TAG,"getPosts got Responce "+type+" in time : "+totalRequestTime);
//

//                parsePostsJson(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<PostsData>call, Throwable t) {
//                // Log error here since request failed
//                Log.e(TAG, t.toString());
//            }
//        });
    }


    public void  parsePostsJson(JSONObject jsonObject){
//public void  parsePostsJson(PostsData postsData){

        Log.d(TAG,"parsePostsJson");



        recyclerView.refreshComplete();
        isBlankRefresh = false;

        postsData = CommonUtilities.getObjectFromJson(jsonObject, PostsData.class);



        if(toAppendOrRefresh){
            updateRecyclerViewData(postsData);
        }else{
            loadRecyclerViewData();
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


    public void endOFPosts(){
        recyclerView.refreshComplete();
        isBlankRefresh = false;
        recyclerView.setLoadingMoreEnabled(false);


        snackbar = Snackbar
                .make(latest_fragment,
                        R.string.no_more_msgs, Snackbar.LENGTH_SHORT)
        ;
        snackbar.show();

//        LayoutInflater inflater = (LayoutInflater)context.getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.nav_header_home,null);
//
//
//        recyclerView.addHeaderView(view);
//        recyclerView.setFootView(view);

    }





    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void itemChanged(int position){

        postRecyclerViewAdapter.notifyItemChanged(position+1);
    }

    @Override
    public void onWhatsAppClicked(int position) {

        PostItemData postItemData = (PostItemData) mRecyclerViewItems.get(position);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        String text = postItemData.getPost();
        sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");

        postItemData.setPost_whatsapp_count(postItemData.getPost_whatsapp_count()+1);
        itemChanged(position);


        activity.startActivity(sendIntent);
        updateCount(postItemData.getId_post(), KEY_WHATSAPP);

        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
        bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
        mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_SHARED_TO_WHATSAPP, bundle);


        Log.d(TAG,"whatsapp clicked");

    }

    @Override
    public void onShareClicked(int position) {

        PostItemData postItemData = (PostItemData) mRecyclerViewItems.get(position);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        String text = postItemData.getPost();
        sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
        sendIntent.setType("text/plain");
        postItemData.setPost_share_count(postItemData.getPost_share_count()+1);

        activity.startActivity(sendIntent);

        updateCount(postItemData.getId_post(), KEY_SHARE);


        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
        bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
        mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_SHARED_TO_EXTERNAL, bundle);


//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));
        itemChanged(position);

        Log.d(TAG,"share clicked");
    }

    @Override
    public void onLiked(final int position) {

        PostItemData postItemData = (PostItemData) mRecyclerViewItems.get(position);

        postItemData.setIs_liked(1);
        postItemData.setPost_likes_count(postItemData.getPost_likes_count()+1);
        updateCount(postItemData.getId_post(), KEY_LIKE);

        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
        bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
        mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_LIKED, bundle);

        Log.d(TAG,"Post liked");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                itemChanged(position);

            }
        }, 500);


    }
    @Override
    public void onUnliked(final int position) {

        PostItemData postItemData = (PostItemData) mRecyclerViewItems.get(position);

        postItemData.setIs_liked(0);
        postItemData.setPost_likes_count(postItemData.getPost_likes_count()-1);
        updateCount(postItemData.getId_post(),AppConfig.KEY_UNLIKE);
         Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemData.getId_post());
        bundle.putString(FIREBASE_KEY_CATEGORY, postItemData.getCategory_name());
        mFirebaseAnalytics.logEvent(FIREBASE_KEY_POST_UNLIKED, bundle);
        Log.d(TAG,"Post uniked");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                itemChanged(position);

            }
        }, 500);

    }

    @Override
    public void onCardClicked(int position) {


        PostItemData postItemClicked = (PostItemData) mRecyclerViewItems.get(position);


        startActivityForResult(new Intent(activity, SwypeActivity.class)
                                .putExtra(AppConfig.ARG_PARAM_POST_DATA,
                                        (Serializable) mRecyclerViewItems)
                                .putExtra(AppConfig.ARG_PARAM_POSITION,position)
                                .putExtra(AppConfig.ARG_PARAM_CATEGORY_ID,((HomeActivity)activity).getActiveCategoryId())
                                .putExtra(AppConfig.ARG_PARAM_TYPE,type),1
                        );

                        Bundle bundle = new Bundle();
                        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, postItemClicked.getId_post());
                        bundle.putString(FIREBASE_KEY_CATEGORY, postItemClicked.getCategory_name());
                        mFirebaseAnalytics.logEvent(FIREBASE_KEY_TAPPED_ON_DETAILED_POST, bundle);


                        Log.d(TAG,"Clicked on card");

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String hello;

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
            url = URL_UPDATE_SHARE_COUNT.trim();
        }
        else if(what.equals(KEY_LIKE.trim())){
            url = URL_LIKE_POST;
        }
        else{
            url = AppConfig.URL_UNLIKE_POST;
        }

        Log.d(TAG,"updateCount url : "+url);


        Volley.newRequestQueue(context).add(new CustomRequest(activity,activity,
                false, Request.Method.POST, url,false,
                params, CommonUtilities.buildHeaders(context),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(activity,jsonObject);
                        Log.d(TAG,"got responce");

                        try {
                            if(js.isError()){

                                Toast.makeText(activity,js.getMessage().toString(),Toast.LENGTH_LONG).show();
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
                Log.d(TAG,"got error");

                Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(PostsData postsData);

//        void loadPosts(boolean isLatestFragment,int pageNo);

    }



    public void postDataUpdatedFromDetailedScreen(List<PostItemData> postItemDataList){

        mRecyclerViewItems.clear();
        mRecyclerViewItems.addAll(postItemDataList);

        postRecyclerViewAdapter.notifyDataSetChanged();
    }




}
