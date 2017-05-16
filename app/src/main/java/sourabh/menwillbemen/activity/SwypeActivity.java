package sourabh.menwillbemen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mikepenz.iconics.view.IconicsImageView;
import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.menwillbemen.R;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.SessionManager;

public class SwypeActivity extends AppCompatActivity {

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

    LinearLayout linearLayoutBackground
            ;

    final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            BitmapDrawable bg = new BitmapDrawable(context.getResources(), bitmap);
            bg.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            linearLayoutBackground.setBackgroundDrawable(bg);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPagerArrowIndicator viewPagerArrowIndicator = (ViewPagerArrowIndicator) findViewById(R.id.viewPagerArrowIndicator);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        linearLayoutBackground = (LinearLayout) findViewById(R.id.detailed_post_layout);

        context = getApplicationContext();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


        postItemDataList = (List<PostItemData>) getIntent().getSerializableExtra(AppConfig.ARG_PARAM_POST_DATA);
        position = getIntent( ).getIntExtra(AppConfig.ARG_PARAM_POSITION,0);


//        postItemData = postItemDataList.get(position);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);
//        viewPagerArrowIndicator.bind(mViewPager);






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


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        background_image = new SessionManager(context).getBackgroundImage();
        if(background_image != null){
            loadBackgroundImage(linearLayoutBackground);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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


        @BindView(R.id.txtStatusMsg)
        TextView statusMsg;
        @BindView(R.id.scrollPost)
        ScrollView scrollPost;
        @BindView(R.id.card)
        CardView cardView;

        @BindView(R.id.adView)
        AdView mAdView;

        @BindView(R.id.txtCountWhatsapp)
        TextView countWhatsApp;
        @BindView(R.id.count_share)
        TextView countShare;

        @BindView(R.id.count_likes)
        TextView countLikes;

        @BindView(R.id.whatsapp_button)
        IconicsImageView whatsapp_button;

        @BindView(R.id.share_button)
        IconicsImageView share_button;

        @BindView(R.id.thumb_button)
        LikeButton thumb_button;
        Float textSize;

        private static final String ARG_SECTION_NUMBER = "section_number";

        Context context;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(PostItemData postItemData)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putSerializable(AppConfig.ARG_PARAM_POST_DATA, postItemData);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_swype, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            ButterKnife.bind(this, rootView);
            context = getContext();

            final PostItemData postItemData = (PostItemData) getArguments().getSerializable(AppConfig.ARG_PARAM_POST_DATA);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

            try{
                textSize = Float.valueOf(sharedPref.getString
                        (getResources().getString(R.string.pref_key_fontsize), ""));

            }catch (Exception ex){

            }


            int color = postItemData.getCard_color();


            countWhatsApp.setText(postItemData.getPost_whatsapp_count().toString());
            countShare.setText(postItemData.getPost_share_count().toString());
            countLikes.setText(postItemData.getPost_likes_count().toString());


            cardView.setCardBackgroundColor(color);
            whatsapp_button.setColor(color);
            share_button.setColor(color);




            if (!TextUtils.isEmpty(postItemData.getPost())) {

                int maxLines = AppConfig.MAX_LINES;
                statusMsg.setMaxLines(maxLines);
                statusMsg.setEllipsize(TextUtils.TruncateAt.END);
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
                    updateCount(postItemData.getId_post(),AppConfig.KEY_LIKE);

                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    postItemData.setIs_liked(0);
                    postItemData.setPost_likes_count(postItemData.getPost_likes_count()-1);
                    updateCount(postItemData.getId_post(),AppConfig.KEY_UNLIKE);


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

                    startActivity(sendIntent);
                    updateCount(postItemData.getId_post(),AppConfig.KEY_WHATSAPP);


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

                    startActivity(sendIntent);

                    updateCount(postItemData.getId_post(),AppConfig.KEY_SHARE);

//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));

                }
            });





            showAd();


            return rootView;
        }


        void updateCount(int post_id, String what){


            Map<String, String> params = new HashMap<String, String>();
            params.put("post_id", post_id+"");

            String url = "";

            if(what.equals(AppConfig.KEY_WHATSAPP)){
                url = AppConfig.URL_UPDATE_WHATSAPP_COUNT;
            }
            else if(what.equals(AppConfig.KEY_SHARE))
            {
                url = AppConfig.URL_UPDATE_SHARE_COUNT;
            }
            else if(what.equals(AppConfig.KEY_LIKE)){
                url = AppConfig.URL_LIKE_POST;
            }
            else{
                url = AppConfig.URL_UNLIKE_POST;
            }

            Volley.newRequestQueue(context).add(new CustomRequest(getActivity(),getActivity(),
                    false, Request.Method.POST, url,
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
            AdRequest adRequest = new AdRequest.Builder().build();
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(postItemDataList.get(position));
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
}
