package sourabh.menwillbemen.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.marshalchen.ultimaterecyclerview.expanx.Util.parent;
import com.mikepenz.iconics.view.IconicsImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.menwillbemen.R;
import sourabh.menwillbemen.activity.DetailedPostActivity;
import sourabh.menwillbemen.activity.HomeActivity;
import sourabh.menwillbemen.activity.LatestFragment;
import sourabh.menwillbemen.activity.SwypeActivity;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.Util;

import static android.media.CamcorderProfile.get;

/**
 * Created by Downloader on 5/4/2017.
 */



public class PostRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //    private List<PostItemData> postItemDataList;
    Activity activity;
    Float fontSize;
    private final List<Object> mRecyclerViewItems;

    // A menu item view type.
    private static final int POST_ITEM_VIEW_TYPE = 0;

    // The Native Express ad view type.
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;
    private AdapterCallBack adapterCallBack;


    public PostRecyclerViewAdapter(Activity activity,
//                                   List<PostItemData> postItemDataList,
                                   List<Object> recyclerViewItems,
                                   Float fontSize,
                                   AdapterCallBack adapterCallBack) {

        // this.postItemDataList = postItemDataList;
        this.mRecyclerViewItems = recyclerViewItems;

        this.activity =activity;
        this.fontSize = fontSize;
        this.adapterCallBack = adapterCallBack;
    }




    public class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtStatusMsg)
        protected TextView post;
        @BindView(R.id.count_likes)
        protected TextView countLikes;

        @BindView(R.id.txtCountWhatsapp)
        protected TextView countWhatsApp;

        @BindView(R.id.category)
        protected TextView category;

        @BindView(R.id.count_share)
        protected TextView countShare;

        @BindView(R.id.card)
        protected CardView cardView;

        @BindView(R.id.whatsapp_button)
        protected IconicsImageView whatsapp_button;
        @BindView(R.id.thumb_button)
        protected LikeButton likeButton;
        @BindView(R.id.share_button)
        protected IconicsImageView share_button;

        @BindView(R.id.relativeTimestamp)
        protected RelativeTimeTextView relativeTimestamp;

//        @BindView(R.id.adViewNative)
//        protecte d NativeExpressAdView adViewNative;

        public PostViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(R.id.title);

            ButterKnife.bind(this, view);


        }
    }


    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }





    /**
     * Determines the view type for the given position.
     */
//    @Override
//    public int getItemViewType(int position) {
////        return (position % LatestFragment.ITEMS_PER_AD == 0) ? NATIVE_EXPRESS_AD_VIEW_TYPE
////                : POST_ITEM_VIEW_TYPE;
//
//        if(position % LatestFragment.ITEMS_PER_AD == 0){
//            return NATIVE_EXPRESS_AD_VIEW_TYPE;
//        }else{
//            return POST_ITEM_VIEW_TYPE;
//        }
//
//    }


//        @Override
//    public int getItemViewType(int position) {
//
//            return position;
//
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            case POST_ITEM_VIEW_TYPE:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feed_item, parent, false);

                return new PostViewHolder(itemView);
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                View nativeExpressLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.native_express_ad_container,
                        parent, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }




    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int viewType = getItemViewType(position);


        switch (viewType) {
            case POST_ITEM_VIEW_TYPE:

                final PostViewHolder postViewHolder = (PostViewHolder) holder;


                final PostItemData postItemData = (PostItemData) mRecyclerViewItems.get(position);
                postViewHolder.post.setText(postItemData.getPost());


//                postViewHolder.countWhatsApp.setText(postItemData.getPost_whatsapp_count().toString());
                postViewHolder.countWhatsApp.setText(CommonUtilities.format(Long.parseLong(postItemData.getPost_whatsapp_count().toString())));
                postViewHolder.countShare.setText(CommonUtilities.format(Long.parseLong(postItemData.getPost_share_count().toString())));
                postViewHolder.countLikes.setText(CommonUtilities.format(Long.parseLong(postItemData.getPost_likes_count().toString())));
                postViewHolder.category.setText(postItemData.getCategory_name().toString());


                if(postItemData.getIs_liked() == 1){
                    postViewHolder.likeButton.setLiked(true);

                }else{
                    postViewHolder.likeButton.setLiked(false);
                }


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date date = sdf.parse(postItemData.getPost_created_on());
                    postViewHolder.relativeTimestamp.setReferenceTime(date.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                int color = postItemData.getCard_color();//Util.getRandomColor();
//                postViewHolder.cardView.setCardBackgroundColor(color);

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), postViewHolder.cardView.getCardBackgroundColor().getDefaultColor(), color);
                colorAnimation.setDuration(250); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        postViewHolder.cardView.setCardBackgroundColor((int) animator.getAnimatedValue());

                    }

                });
                colorAnimation.start();


                postViewHolder.whatsapp_button.setColor(color);
                postViewHolder.share_button.setColor(color);
//                postViewHolder.likeButton.setCircleEndColorRes(color);





                if (!TextUtils.isEmpty(postItemData.getPost())) {

                    int maxLines = AppConfig.MAX_LINES;
                    postViewHolder.post.setMaxLines(maxLines);
                    postViewHolder.post.setEllipsize(TextUtils.TruncateAt.END);
                    postViewHolder.post.setText(postItemData.getPost());

                    if(fontSize != null){
                        postViewHolder.post.setTextSize(fontSize);
                    }

                    postViewHolder.post.setVisibility(View.VISIBLE);
                } else {
                    // status is empty, remove from view
                    postViewHolder.post.setVisibility(View.GONE);
                }



                postViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create new fragment and transaction
                        activity.startActivity(new Intent(activity, SwypeActivity.class)
                                .putExtra(AppConfig.ARG_PARAM_POST_DATA,
                                        (Serializable) mRecyclerViewItems)
                                .putExtra(AppConfig.ARG_PARAM_POSITION,position)
                        );




                    }
                });


                postViewHolder.likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {

                        postItemData.setIs_liked(1);
                        postItemData.setPost_likes_count(postItemData.getPost_likes_count()+1);
                        adapterCallBack.onItemChanged();
                        updateCount(postItemData.getId_post(),AppConfig.KEY_LIKE);

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        postItemData.setIs_liked(0);
                        postItemData.setPost_likes_count(postItemData.getPost_likes_count()-1);
                        adapterCallBack.onItemChanged();
                        updateCount(postItemData.getId_post(),AppConfig.KEY_UNLIKE);


                    }
                });

                postViewHolder.whatsapp_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);

                        String text = postItemData.getPost();
                        sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");

                        activity.startActivity(sendIntent);
                        updateCount(postItemData.getId_post(),AppConfig.KEY_WHATSAPP);


                    }
                });

                postViewHolder.share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);

                        String text = postItemData.getPost();
                        sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                        sendIntent.setType("text/plain");

                        activity.startActivity(sendIntent);

                        updateCount(postItemData.getId_post(),AppConfig.KEY_SHARE);

//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));

                    }
                });

                break;

            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mRecyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                // The NativeExpressAdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // NativeExpressAdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled NativeExpressAdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);
        }






    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
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

        Volley.newRequestQueue(activity).add(new CustomRequest(activity,activity,
                false, Request.Method.POST, url,
                params, CommonUtilities.buildHeaders(activity),


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js= new JsonSeparator(activity,jsonObject);

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

                Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();

            }
        }));
    }


    public interface AdapterCallBack{

        void onItemChanged();
    }

}
