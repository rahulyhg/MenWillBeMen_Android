package sourabh.menwillbemen.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.like.LikeButton;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.helpers.ClickListenerHelper;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.iconics.view.IconicsImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.menwillbemen.R;
import sourabh.menwillbemen.activity.DetailedPostActivity;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.Util;

import static sourabh.menwillbemen.R.id.share_button;
import static sourabh.menwillbemen.R.id.whatsapp_button;


public class PostItemData
//        extends AbstractItem<PostItemData, PostItemData.ViewHolder>

        implements Serializable
{

    private Integer id_post;
    private Integer id_user;
    private Integer id_category;
    private String post;
    private Integer post_likes_count;
    private String post_created_on;
    private Integer post_whatsapp_count;
    private Integer post_share_count;
    private Integer is_liked;

    private Integer card_color;

    private String category_name;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getPost_whatsapp_count() {
        return post_whatsapp_count;
    }

    public void setPost_whatsapp_count(Integer post_whatsapp_count) {
        this.post_whatsapp_count = post_whatsapp_count;
    }

    public Integer getPost_share_count() {
        return post_share_count;
    }

    public void setPost_share_count(Integer post_share_count) {
        this.post_share_count = post_share_count;
    }

    public Integer getId_post() {
        return id_post;
    }

    public void setId_post(Integer id_post) {
        this.id_post = id_post;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_category() {
        return id_category;
    }

    public void setId_category(Integer id_category) {
        this.id_category = id_category;
    }

    public Integer getPost_likes_count() {
        return post_likes_count;
    }

    public void setPost_likes_count(Integer post_likes_count) {
        this.post_likes_count = post_likes_count;
    }

    public String getPost_created_on() {
        return post_created_on;
    }

    public void setPost_created_on(String post_created_on) {
        this.post_created_on = post_created_on;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Integer getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(Integer is_liked) {
        this.is_liked = is_liked;
    }

    public Integer getCard_color() {
        return card_color;
    }

    public void setCard_color(Integer card_color) {
        this.card_color = card_color;
    }

    //    //The unique ID for this type of item
//    @Override
//    public int getType() {
//        return R.id.fastadapter_sampleitem_id;
//    }
//
//    //The layout to be used for this type of item
//    @Override
//    public int getLayoutRes() {
//        return R.layout.feed_item;
//    }
//
//    //The logic to bind your data to the view
//    @Override
//    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
//        //call super so the selection is already handled for you
//        super.bindView(viewHolder, payloads);
//
//        //bind our data
//        //set the text for the name
//        viewHolder.post.setText(post);
//        viewHolder.countWhatsApp.setText(String.valueOf(post_whatsapp_count));
//        viewHolder.countShare.setText(String.valueOf(post_share_count));
//        viewHolder.countLikes.setText(String.valueOf(post_likes_count));
//
//
//        int color = Util.getRandomColor();
//
////		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
////		NetworkImageView profilePic = (NetworkImageView) convertView
////				.findViewById(R.id.profilePic);
////		FeedImageView f eedImageView = (FeedImageView) convertView
////				.findViewById(R.id.feedImage1);
//        viewHolder.cardView.setCardBackgroundColor(color);
//
//        viewHolder.whatsapp_button.setColor(color);
//        viewHolder.share_button.setColor(color);
//
//
//
//        //name.setText(item.getName());
//
//        // Converting timestamp into x ago format
////		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
////				Long.parseLong(item.getTimeStamp()),
////				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
////		timestamp.setText(timeAgo);
//
//        // Chcek for empty status message
//        if (!TextUtils.isEmpty(post)) {
//
//            viewHolder.post.setMaxLines(10);
//            viewHolder.post.setEllipsize(TextUtils.TruncateAt.END);
//            viewHolder.post.setText(post);
//            viewHolder.post.setVisibility(View.VISIBLE);
//        } else {
//            // status is empty, remove from view
//            viewHolder.post.setVisibility(View.GONE);
//        }
//
//
//
//
//
//        //set the text for the description or hide
//    }
//
//    //reset the view here (this is an optional method, but recommended)
//    @Override
//    public void unbindView(ViewHolder holder) {
//        super.unbindView(holder);
//        holder.post.setText(null);
//    }
//
//    //Init the viewHolder for this Item
//    @Override
//    public ViewHolder getViewHolder(View v) {
//        return new ViewHolder(v);
//    }
//
//    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.txtStatusMsg)
//        protected TextView post;
//
//        @BindView(R.id.count_likes)
//        protected TextView countLikes;
//
//        @BindView(R.id.txtCountWhatsapp)
//        protected TextView countWhatsApp;
//
//        @BindView(R.id.count_share)
//        protected TextView countShare;
//
//        @BindView(R.id.card)
//        protected CardView cardView;
//
//        @BindView(R.id.whatsapp_button)
//        protected IconicsImageView whatsapp_button;
//        @BindView(R.id.thumb_button)
//        protected LikeButton likeButton;
//        @BindView(R.id.share_button)
//        protected IconicsImageView share_button;
//
//
//
//        public ViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//
//
////            this.post = (TextView) view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_name);
//
//        }
//    }



//    public static class CardViewClickEvent extends ClickEventHook<PostItemData> {
//        @Override
//        public void onClick(View v, int position, FastAdapter<PostItemData> fastAdapter, PostItemData item) {
//
//            Toast.makeText(v.getContext(), item.getPost() + " - ", Toast.LENGTH_SHORT).show();
//
//        }
//        @Nullable
//        @Override
//        public List<View> onBindMany(@NonNull RecyclerView.ViewHolder viewHolder) {
//            if (viewHolder instanceof PostItemData.ViewHolder) {
//                return ClickListenerHelper.toList(((ViewHolder) viewHolder).cardView);
//            }
//            return super.onBindMany(viewHolder);
//        }
//
//    }


//    public static class WhatsAppClickEvent extends ClickEventHook<PostItemData> {
//
//
//        @Nullable
//        @Override
//        public List<View> onBindMany(@NonNull RecyclerView.ViewHolder viewHolder) {
//            if (viewHolder instanceof PostItemData.ViewHolder) {
//                return ClickListenerHelper.toList(((ViewHolder) viewHolder).whatsapp_button);
//            }
//            return super.onBindMany(viewHolder);
//        }
//
//        @Override
//        public void onClick(View v, int position,
//                            FastAdapter<PostItemData> fastAdapter, PostItemData item) {
//
//
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//
//            ;
//            sendIntent.putExtra(Intent.EXTRA_TEXT, item.getPost()+"\n\n"+"Men Will Be Men ");
//            sendIntent.setType("text/plain");
//            sendIntent.setPackage("com.whatsapp");
//
//            v.getContext().startActivity(sendIntent);
//            updateCount((Activity) v.getContext(),item.getId_post(),true);
//
//        }
//    }
//
//
//    public static class CardViewClickEvent extends ClickEventHook<PostItemData> {
//        @Nullable
//        @Override
//        public List<View> onBindMany(@NonNull RecyclerView.ViewHolder viewHolder) {
//            if (viewHolder instanceof PostItemData.ViewHolder) {
//                return ClickListenerHelper.toList(((ViewHolder) viewHolder).cardView);
//            }
//            return super.onBindMany(viewHolder);
//        }
//
//        @Override
//        public void onClick(View v, int position,
//                            FastAdapter<PostItemData> fastAdapter, PostItemData item) {
//
//            v.getContext().startActivity(new Intent(v.getContext(), DetailedPostActivity.class)
//                    .putExtra(AppConfig.ARG_PARAM_POST_DATA,
//                            (Serializable) item)
//                    .putExtra(AppConfig.ARG_PARAM_POSITION,position)
//            );        }
//    }
//
//
//    static void updateCount(final Activity activity, int post_id, boolean isWhatsapp){
//
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("post_id", post_id+"");
//
//        String url = "";
//        if(isWhatsapp){
//            url = AppConfig.URL_UPDATE_WHATSAPP_COUNT;
//        }else{
//            url = AppConfig.URL_UPDATE_SHARE_COUNT;
//        }
//
//        Volley.newRequestQueue(activity).add(new CustomRequest(activity,activity,
//                false, Request.Method.POST, url,
//                params, CommonUtilities.buildGuestHeaders(),
//
//
//                new com.android.volley.Response.Listener() {
//
//                    @Override
//                    public void onResponse(Object response) {
//                        JSONObject jsonObject = (JSONObject) response;
//                        JsonSeparator js= new JsonSeparator(activity,jsonObject);
//
//                        try {
//                            if(js.isError()){
//
//                                Toast.makeText(activity,js.getMessage().toString(),Toast.LENGTH_LONG).show();
//                            }else{
//
//                                //JSONArray categories = js.getData().getJSONArray(Const.KEY_CATEGORIES);
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//
//                }, new com.android.volley.Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();
//
//            }
//        }));
//
//
//}

}

