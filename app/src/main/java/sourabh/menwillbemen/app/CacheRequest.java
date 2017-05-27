package sourabh.menwillbemen.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import sourabh.menwillbemen.helper.CommonUtilities;
import sourabh.menwillbemen.helper.ConnectionDetector;
import sourabh.menwillbemen.helper.JsonSeparator;
import sourabh.menwillbemen.helper.ProgressWheel;

/**
 * Created by Sourabh on 5/22/2017.
 */
public class CacheRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> listener;
    private final Response.ErrorListener errorListener;

    ConnectionDetector cd;
    private Context context;
    private Map<String, String> headers;
    private Map<String, String> params;
    ProgressWheel progressWheel;
    private boolean showLoadingWheel;
    private String url;
    private Activity activity;


    public CacheRequest(
            Context con,
            Activity activity,
            boolean showloadingwheel,
            int method,
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            Response.Listener<NetworkResponse> reponseListener,
            Response.ErrorListener errorListener)
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
        showLogs(showloadingwheel, method, url, params, headers);
        if (!this.cd.isConnectingToInternet()) {
            CommonUtilities.showAlertDialog(con, "Internet Connection Error", "Please connect to working Internet connection", Boolean.valueOf(false));
        }
        if (this.showLoadingWheel) {
            this.progressWheel.ShowDefaultWheel();
        }

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


    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {

        final String jsonString;
        try {
            jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            JSONObject jsonObject = new JSONObject(jsonString);

            Log.d("Responce", String.valueOf(jsonObject));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
        if (cacheEntry == null) {
            cacheEntry = new Cache.Entry();
        }
        final long cacheHitButRefreshed =  10 * 1000; //3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        long now = System.currentTimeMillis();
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;
        cacheEntry.data = response.data;
        cacheEntry.softTtl = softExpire;
        cacheEntry.ttl = ttl;
        String headerValue;
        headerValue = response.headers.get("Date");
        if (headerValue != null) {
            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        headerValue = response.headers.get("Last-Modified");
        if (headerValue != null) {
            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        cacheEntry.responseHeaders = response.headers;




        return Response.success(response, cacheEntry);
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }
}