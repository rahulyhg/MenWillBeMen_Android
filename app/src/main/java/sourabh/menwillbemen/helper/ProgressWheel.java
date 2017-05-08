package sourabh.menwillbemen.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;

import sourabh.menwillbemen.R;


public class ProgressWheel {
    Context context;
    ProgressDialog dialog;
    Activity activity;
    public ProgressWheel(Context context, Activity activity) {
        this.context = context;
        this.activity=activity;
    }

    public void ShowWheel(String title, String message) {
        this.dialog = new ProgressDialog(this.context, 0);
        this.dialog.setTitle(title);
        this.dialog.setMessage(message);
        this.dialog.setCancelable(true);
        this.dialog.show();
    }

    public void DismissWheel() {
       this.dialog.dismiss();
    }

    public void ShowDefaultWheel() {
        ShowWheel("Loading", "Please Wait");
        // get your outer relative layout

    }
}
