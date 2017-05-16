package sourabh.menwillbemen.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import sourabh.menwillbemen.R;

import sourabh.menwillbemen.adapter.PostRecyclerViewAdapter;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.data.PostsData;
import sourabh.menwillbemen.data.SettingData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.SessionManager;
import sourabh.menwillbemen.helper.Util;


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

    final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            BitmapDrawable bg = new BitmapDrawable(context.getResources(), bitmap);
            bg.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            relativeLayoutList.setBackgroundDrawable(bg);
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

        Log.d("Home","onActivityCreated");

        view.post(new Runnable() {
            public void run() {
            /* the desired UI update */
                showAd(view);
                initialiseRecyclerView();
                getPosts(((HomeActivity)getActivity()).getActiveCategoryId(), false,pageNo);

//                loadRecyclerViewData();
            }
        });

        // Update the RecyclerView item's list with menu items and Native Express ads.




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Home","onCreateView");

        view = inflater.inflate(R.layout.fragment_latest, container, false);
        recyclerView = (XRecyclerView) view.findViewById(R.id.list);
        relativeLayoutList = (RelativeLayout) view.findViewById(R.id.RelativeLayoutList);

        context = getContext();
        sessionManager = new SessionManager(context);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            isLatestFragment = this.getArguments().getBoolean(AppConfig.ARG_PARAM_IS_LATEST_POST_FRAGMENT);
//            background_image = this.getArguments().getString(AppConfig.ARG_BACKGROUND_IMAGE);

        }

        background_image = sessionManager.getBackgroundImage();
        if(background_image != null){
            loadBackgroundImage(relativeLayoutList);
        }




        return view;
    }

    private void loadBackgroundImage(final RelativeLayout relativeLayoutList) {



        Picasso.with(getActivity()).load(background_image).
        into(target);

    }


    void initialiseRecyclerView(){
        Log.d("Home","loadRecyclerViewData");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

//        recyclerView.setRefreshProgressStyle(ProgressStyle
//                .Pacman);
//        recyclerView.setLoadingMoreProgressStyle(ProgressStyle
//                .Pacman);

        recyclerView.setRefreshProgressStyle(sessionManager.getRefreshStyle());
        recyclerView.setLoadingMoreProgressStyle(sessionManager.getLoadingMoreStyle());


        try{
            textSize = Float.valueOf(sharedPref.getString
                    (getActivity().getResources().getString(R.string.pref_key_fontsize), ""));

        }catch (Exception ex){

        }

        postRecyclerViewAdapter = new PostRecyclerViewAdapter(getActivity(),
                mRecyclerViewItems,textSize,this);

        if(sharedPref.getBoolean(getActivity().getResources().getString(R.string.pref_key_animation),true)){

            ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(postRecyclerViewAdapter);
            alphaAdapter.setDuration(200);
            recyclerView.setAdapter(alphaAdapter);
        }else{
            recyclerView.setAdapter(postRecyclerViewAdapter);
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
                    getPosts(((HomeActivity)getActivity()).getActiveCategoryId(), false,pageNo);
                }

            }

            @Override
            public void onLoadMore() {
                // load more data here
                pageNo = pageNo + 1;
                apppend = true ;
                toAppendOrRefresh = true;

                getPosts(((HomeActivity)getActivity()).getActiveCategoryId(), false,pageNo);

            }
        });


        isBlankRefresh = true;
        recyclerView.refresh();




    }
    void loadRecyclerViewData(){

        if(mRecyclerViewItems != null){
            mRecyclerViewItems.clear();
        }



        if(isLatestFragment){
            mRecyclerViewItems.addAll(addColorsToPosts(postsData.getLatest()));
//            postRecyclerViewAdapter = new PostRecyclerViewAdapter(getActivity(), mRecyclerViewItems,textSize);
        }else{
            mRecyclerViewItems.addAll(addColorsToPosts(postsData.getTop()));
//            postRecyclerViewAdapter = new PostRecyclerViewAdapter(getActivity(), mRecyclerViewItems,textSize);
        }

//        addNativeExpressAds();
//        setUpAndLoadNativeExpressAds();


        postRecyclerViewAdapter.notifyDataSetChanged();


    }

    public void  updateRecyclerViewData(PostsData postsData){



        if(apppend){

            if(isLatestFragment){
                mRecyclerViewItems.addAll(addColorsToPosts(postsData.getLatest()));
            }else{
                mRecyclerViewItems.addAll(addColorsToPosts(postsData.getTop()));
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

        recyclerView.setLoadingMoreEnabled(true);

        pageNo = 1;
        toAppendOrRefresh = false;
        apppend = false;
        initialiseRecyclerView();
        getPosts(categoryId, false,pageNo);

    }
        void showAd(View view){


        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Log.d("Home","showAd");

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
//                    View child = getActivity().getLayoutInflater().inflate(R.layout.native_express_ad_container, null);
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



    public void  getPosts(String category_id, boolean toShowLoading, int pageNo)
    {

        String URL = AppConfig.URL_GET_POSTS
                +sessionManager.getSelectedLanguageId()+"/"
                +category_id+"/"+String.valueOf(pageNo);

        Volley.newRequestQueue(context).add(new CustomRequest(context,getActivity(),
                toShowLoading, Request.Method.GET, URL,
                CommonUtilities.buildBlankParams(), CommonUtilities.buildHeaders(context),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(context,jsonObject);

                        try {
                            if(js.isError()){
                                Toast.makeText(context,js.getMessage().toString(),Toast.LENGTH_LONG).show();
                                endOFPosts();
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

                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }


    public void  parsePostsJson(JSONObject jsonObject){

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

    }





    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemChanged() {

        postRecyclerViewAdapter.notifyDataSetChanged();

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String hello;

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

}
