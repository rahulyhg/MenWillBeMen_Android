package sourabh.menwillbemen.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.util.Map;

import io.fabric.sdk.android.Fabric;
import sourabh.menwillbemen.helper.ConnectionDetector;
import sourabh.menwillbemen.helper.ProgressWheel;



public class CustomRequest extends Request<JSONObject> {
    ConnectionDetector cd;
    private Context context;
    private ErrorListener errorListener;
    private Map<String, String> headers;
    private Listener<JSONObject> listener;
    private Map<String, String> params;
    ProgressWheel progressWheel;
    private boolean showLoadingWheel;
    private String url;
    private Activity activity;
    private boolean useCache;



    public CustomRequest(
            Context con,
            Activity activity,
            boolean showloadingwheel,
            int method,
            String url,
            boolean useCache,
            Map<String, String> params,
            Map<String, String> headers,
            Listener<JSONObject> reponseListener,
            ErrorListener errorListener
            )

    {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.errorListener = errorListener;
        this.params = params;
        this.headers = headers;
        this.url = url;
        this.context = con;
        this.activity = activity;

        this.showLoadingWheel = showloadingwheel;
        this.cd = new ConnectionDetector(con);
        this.progressWheel = new ProgressWheel(con,activity);
        this.useCache = useCache;
        showLogs(showloadingwheel, method, url, params, headers);

        if (!this.cd.isConnectingToInternet()) {
//            CommonUtilities.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", Boolean.valueOf(false));
        }
        if (this.showLoadingWheel) {
            this.progressWheel.ShowDefaultWheel();
        }

        this.setShouldCache(false);

//        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        if(!useCache){
//            AppController.getInstance().getRequestQueue().getCache().invalidate(url,false);
//        }

        Fabric.with(activity, new Crashlytics());

    }

    void showLogs(boolean showloadingwheel, int method, String url, Map<String, String> params, Map<String, String> headers) {
        Log.d("showloadingwheel", showloadingwheel + BuildConfig.FLAVOR);
        Log.d("method", method + BuildConfig.FLAVOR);
        Log.d("url", url);
        Log.d("params", params.toString());
        Log.d("headers", headers.toString());
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.headers;
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Response<JSONObject> success;
        Cache.Entry cacheEntry = null;

        try {



//            cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//            if (cacheEntry == null) {
//                cacheEntry = new Cache.Entry();
//            }
//            final long cacheHitButRefreshed =  300 * 60 * 1000; //3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
//            long now = System.currentTimeMillis();
//            final long softExpire = now + cacheHitButRefreshed;
//            final long ttl = now + cacheExpired;
//            cacheEntry.data = response.data;
//            cacheEntry.softTtl = softExpire;
//            cacheEntry.ttl = ttl;
//            String headerValue;
//            headerValue = response.headers.get("Date");
//            if (headerValue != null) {
//                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//            }
//            headerValue = response.headers.get("Last-Modified");
//            if (headerValue != null) {
//                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//            }
//            cacheEntry.responseHeaders = response.headers;




            success = Response.success(
                    new JSONObject(
                            new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers))),
                                    HttpHeaderParser.parseCacheHeaders(response));

//            success = Response.success(
//                    new JSONObject(
//                            new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers))),
//                    cacheEntry);


            if (this.showLoadingWheel) {
               this.progressWheel.DismissWheel();
            }
        } catch (Throwable e) {
            success = Response.error(new ParseError(e));
            if (this.showLoadingWheel) {
                this.progressWheel.DismissWheel();
            }
        }



       return success;
    }


//    @Override
//    public String getBodyContentType() {
//        return "application/json";
//    }

//    @Override
//    public byte[] getBody() throws AuthFailureError {
//
//        return params.toString().getBytes();
//    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        NetworkResponse response = volleyError.networkResponse;
        VolleyError error = null;
        String strerrormsg = "";





        if (response != null && response.data != null)
        {
        Crashlytics.setInt(AppConfig.CRASHLYTICS_KEY_ERROR_CODE, response.statusCode);


            switch (response.statusCode)
            {
                case 400:

                    if (volleyError instanceof ServerError)
                    {
                        volleyError = new VolleyError(new String(volleyError.networkResponse.data));
//                        if (volleyErrorHandling.is_empty(volleyError.getLocalizedMessage())) {
//
//                            strerrormsg = "SERVER_ERROR";
//                            error = new VolleyError(strerrormsg);
//                            volleyError = error;
//                        }
//                    } else {
                        strerrormsg = "SERVER_ERROR";
                        error = new VolleyError(strerrormsg);
                        volleyError = error;
                    }
                    break;
                case 401:

                    strerrormsg = "Bad Request";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;
                case 403:
                    strerrormsg = "Unauthorized request to server";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;
                case 404:
                    strerrormsg = "Something went wrong. Please try again later.";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;

                case 408:
                    strerrormsg = "Something went wrong. Please try again later.";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;

                case 500:
                    strerrormsg = "Oops something went wrong. Please try again later.";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;
                case 503:
                    strerrormsg = "Oops something went wrong. Please try again later.";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;
                default:
                    strerrormsg = "Oops something went wrong. Please try again later.";
                    error = new VolleyError(strerrormsg);
                    volleyError = error;
                    break;
            }
        }
        else if (response == null)
        {

            Crashlytics.setInt(AppConfig.CRASHLYTICS_KEY_ERROR_CODE, 504);

            if (volleyError.getClass().equals(TimeoutError.class)) {
                strerrormsg = "Oops. Network connection timed out";
                error = new VolleyError(strerrormsg);
                volleyError = error;
            }

//            activity.runOnUiThread(new Runnable() {
//                public void run() {
//                    CommonUtilities.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection", Boolean.valueOf(false));
//                }
//            });


        }
        return volleyError;
    }



    protected void deliverResponse(JSONObject response) {
        this.listener.onResponse(response);
    }
}
