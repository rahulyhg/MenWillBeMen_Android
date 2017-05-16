package sourabh.menwillbemen.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.util.Map;

import sourabh.menwillbemen.helper.ConnectionDetector;
import sourabh.menwillbemen.helper.ProgressWheel;



public class CustomRequest extends Request<JSONObject> {
    ConnectionDetector cd;
    private Context con;
    private ErrorListener errorListener;
    private Map<String, String> headers;
    private Listener<JSONObject> listener;
    private Map<String, String> params;
    ProgressWheel progressWheel;
    private boolean showLoadingWheel;
    private String url;
    private Activity activity;
    public CustomRequest(String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(0, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.errorListener = errorListener;
    }

    public CustomRequest(Context con,Activity activity,boolean showloadingwheel, int method, String url,
                         Map<String, String> params, Map<String, String> headers,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.errorListener = errorListener;
        this.params = params;
        this.headers = headers;
        this.url = url;
        this.con = con;        this.activity = activity;

        this.showLoadingWheel = showloadingwheel;
        this.cd = new ConnectionDetector(con);
        this.progressWheel = new ProgressWheel(con,activity);
        showLogs(showloadingwheel, method, url, params, headers);
        if (!this.cd.isConnectingToInternet()) {
            //CommonUtilities.showAlertDialog(con, "Internet Connection Error", "Please connect to working Internet connection", Boolean.valueOf(false));
        }
        if (this.showLoadingWheel) {
            this.progressWheel.ShowDefaultWheel();
        }
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
        try {

            success = Response.success(
                    new JSONObject(
                            new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers))),
                                    HttpHeaderParser.parseCacheHeaders(response));
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

    protected void onErrorResponse(VolleyError error) {
        if (this.showLoadingWheel) {
            this.progressWheel.DismissWheel();
        }
    }

    protected void deliverResponse(JSONObject response) {
        this.listener.onResponse(response);
    }
}
