package sourabh.menwillbemen.adapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.like.LikeButton;
import com.mikepenz.iconics.view.IconicsImageView;

import org.json.JSONException;
import org.json.JSONObject;

import sourabh.menwillbemen.R;
import sourabh.menwillbemen.activity.DetailedPostActivity;
import sourabh.menwillbemen.activity.LatestFragment;
import sourabh.menwillbemen.app.AppConfig;
import sourabh.menwillbemen.app.AppController;
import sourabh.menwillbemen.app.CustomRequest;
import sourabh.menwillbemen.data.PostItemData;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.Const;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.Util;

import static android.R.attr.ellipsize;
import static android.R.attr.fragment;
import static android.R.attr.maxLines;
import static android.R.attr.value;
import static sourabh.menwillbemen.R.id.share;
import static sourabh.menwillbemen.app.AppConfig.URL_UPDATE_WHATSAPP_COUNT;

public class PostListAdapter extends BaseAdapter {
	private Activity activity;

    private LayoutInflater inflater;
	private List<PostItemData> postItemDataList;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    PostItemData item;
    TextView countWhatsApp;
    TextView countShare;
    TextView countLikes;
	public PostListAdapter(Activity activity,
                           List<PostItemData> postItemDataList
                           ) {
		this.activity = activity;
		this.postItemDataList = postItemDataList;

    }

	@Override
	public int getCount() {
		return postItemDataList.size();
	}

	@Override
	public Object getItem(int location) {
		return postItemDataList.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

        item = postItemDataList.get(position);

//		TextView name = (TextView) convertView.findViewById(R.id.name);
//		TextView timestamp = (TextView) convertView
//				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);

        countLikes = (TextView) convertView
                .findViewById(R.id.count_likes);
        countWhatsApp = (TextView) convertView
                .findViewById(R.id.txtCountWhatsapp);
        countShare = (TextView) convertView
                .findViewById(R.id.count_share);



		CardView cardView = (CardView) convertView
				.findViewById(R.id.card);

		IconicsImageView whatsapp_button =(IconicsImageView) convertView
				.findViewById(R.id.whatsapp_button);
		IconicsImageView share_button =(IconicsImageView) convertView
				.findViewById(R.id.share_button);
		LikeButton likeButton =(LikeButton) convertView
				.findViewById(R.id.thumb_button);





        countWhatsApp.setText(item.getPost_whatsapp_count().toString());
        countShare.setText(item.getPost_share_count().toString());
        countLikes.setText(item.getPost_likes_count().toString());

		int color = Util.getRandomColor();

//		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
//		NetworkImageView profilePic = (NetworkImageView) convertView
//				.findViewById(R.id.profilePic);
//		FeedImageView f eedImageView = (FeedImageView) convertView
//				.findViewById(R.id.feedImage1);
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

            statusMsg.setMaxLines(10);
            statusMsg.setEllipsize(TextUtils.TruncateAt.END);
            statusMsg.setText(item.getPost());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
//		if (item.getUrl() != null) {
//			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//					+ item.getUrl() + "</a> "));
//
//			// Making url clickable
//			url.setMovementMethod(LinkMovementMethod.getInstance());
//			url.setVisibility(View.VISIBLE);
//		} else {
//			// url is null, remove from the view
//			url.setVisibility(View.GONE);
//		}

        // user profile pic
//		profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Feed image
//		if (item.getImge() != null) {
//			feedImageView.setImageUrl(item.getImge(), imageLoader);
//			feedImageView.setVisibility(View.VISIBLE);
//			feedImageView
//					.setResponseObserver(new FeedImageView.ResponseObserver() {
//						@Override
//						public void onError() {
//						}
//
//						@Override
//						public void onSuccess() {
//						}
//					});
//		} else {
//			feedImageView.setVisibility(View.GONE);
//		}





		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create new fragment and transaction
//				android.app.Fragment newFragment = new DetailedPostFragment();
//
//                Bundle arguments = new Bundle();
//                arguments.putSerializable(AppConfig.ARG_PARAM_POST_DATA,
//                        (Serializable) postItemDataList);
//                arguments.putInt(AppConfig.ARG_PARAM_POSITION,position);
//                newFragment.setArguments(arguments);
//
//                android.app.FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//
//                transaction.replace(R.id.latest_fragment, newFragment);
//
//                transaction.addToBackStack(null);
//				transaction.commit();

				activity.startActivity(new Intent(activity, DetailedPostActivity.class)
						.putExtra(AppConfig.ARG_PARAM_POST_DATA,
								(Serializable) postItemDataList)
						.putExtra(AppConfig.ARG_PARAM_POSITION,position)
				);




			}
		});

        whatsapp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String text = postItemDataList.get(position).getPost();
                sendIntent.putExtra(Intent.EXTRA_TEXT, text+"\n\n"+"Men Will Be Men ");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");

                activity.startActivity(sendIntent);
                updateCount(postItemDataList.get(position).getId_post(),true);


//                String new_count = String.valueOf(
//                        (postItemDataList.get(position).getPost_whatsapp_count()+1));
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

                activity.startActivity(sendIntent);

                updateCount(postItemDataList.get(position).getId_post(),false);

//                countShare.setText(String.valueOf(
//                        (postItemDataList.get(position).getPost_share_count()+1)));

            }
        });



		return convertView;
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

        Volley.newRequestQueue(activity).add(new CustomRequest(activity,activity,
                false, Request.Method.POST, url,
                params, CommonUtilities.buildGuestHeaders(),


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



//        CustomRequest jsObjRequest   = new CustomRequest(context,activity,
//                true, Request.Method.POST,url, params, CommonUtilities.buildGuestHeaders(),
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        JsonSeparator js = new JsonSeparator(activity,response);
//
//                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
//
//                        try {
//                            if(js.isError()){
//                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
//                            }else{
////                                Toast.makeText(con, js.getMessage(), Toast.LENGTH_LONG).show();
//
//                                JSONObject jsonObject = js.getData() ;
//
//
//                                //ParseCities(js.getData());
//                            }}
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        try {
//                            throw new IOException("Post failed with error code " + error.getMessage());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//
//                    }
//                });
//        requestQueue.add(jsObjRequest);




    }
}
