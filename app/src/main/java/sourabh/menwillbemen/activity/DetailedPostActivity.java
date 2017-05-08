package sourabh.menwillbemen.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
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
import com.mikepenz.iconics.view.IconicsImageView;

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
import sourabh.menwillbemen.helper.Util;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static sourabh.menwillbemen.R.id.share_button;
import static sourabh.menwillbemen.R.id.whatsapp_button;

public class DetailedPostActivity extends AppCompatActivity {

    @BindView(R.id.txtStatusMsg)
    TextView statusMsg;
    @BindView(R.id.scrollPost)
    ScrollView scrollPost;
    @BindView(R.id.card)
    CardView cardView;
    private AdView mAdView;

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




    List<PostItemData> postItemDataList;
    int position;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        context = this;
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        Point size = new Point();
//        Display display = getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//        int height = size.y;


//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics ();
//        display.getMetrics(outMetrics);
//
//        float density  = getResources().getDisplayMetrics().density;
//        float dpHeight = outMetrics.heightPixels / density;
//        float dpWidth  = outMetrics.widthPixels / density;
//
//
//        scrollPost.getLayoutParams().height = (int) (dpHeight *2);//getWindowManager().getDefaultDisplay().getHeight()-100;

//        ViewGroup.LayoutParams lp = statusMsg.getLayoutParams();
//        lp.height = 300;
//        statusMsg.setLayoutParams(lp);




        postItemDataList = (List<PostItemData>) getIntent().getSerializableExtra(AppConfig.ARG_PARAM_POST_DATA);
        position = getIntent( ).getIntExtra(AppConfig.ARG_PARAM_POSITION,0);


        int color = Util.getRandomColor();
        PostItemData item = postItemDataList.get(position);


        countWhatsApp.setText(item.getPost_whatsapp_count().toString());
        countShare.setText(item.getPost_share_count().toString());
        countLikes.setText(item.getPost_likes_count().toString());


        cardView.setCardBackgroundColor(color);
        whatsapp_button.setColor(color);
        share_button.setColor(color);


        //name.setText(item.getName());

        // Converting timestamp into x ago format
//		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//				Long.parseLong(item.getTimeStamp()),
//				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
//		timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getPost())) {
            statusMsg.setText(item.getPost());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        whatsapp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String text = postItemDataList.get(position).getPost();
                sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");

                startActivity(sendIntent);
                updateCount(postItemDataList.get(position).getId_post(),true);


//                String new_count = String.valueOf(
//                        (mRecyclerViewItems.get(position).getPost_whatsapp_count()+1));
//                countWhatsApp.setText(new_count);


            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String text = postItemDataList.get(position).getPost();
                sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                sendIntent.setType("text/plain");

                startActivity(sendIntent);

                updateCount(postItemDataList.get(position).getId_post(),false);

//                countShare.setText(String.valueOf(
//                        (mRecyclerViewItems.get(position).getPost_share_count()+1)));

            }
        });



        showAd();

    }


    void updateCount(int post_id, boolean isWhatsapp){


        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", post_id+"");

        String url = "";
        if(isWhatsapp){
            url = AppConfig.URL_UPDATE_WHATSAPP_COUNT;
        }else{
            url = AppConfig.URL_UPDATE_SHARE_COUNT;
        }

        Volley.newRequestQueue(context).add(new CustomRequest(this,this,
                false, Request.Method.POST, url,
                params, CommonUtilities.buildGuestHeaders(),


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


        mAdView = (AdView) findViewById(R.id.adView);
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
