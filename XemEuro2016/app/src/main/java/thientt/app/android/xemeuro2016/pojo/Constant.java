package thientt.app.android.xemeuro2016.pojo;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Constant {
    public static final String IP = "40.83.125.109";
    //    public static final String IP = "192.168.1.107";
    //    public static final String IP = "192.168.12.48";
    public static final String AUTHORITY = "thientt.app.android.xemeuro2016";
    // public static final String URL_DEFAULT = "103.15.50.106";
    // public static final String DATA_SERVICE = AUTHORITY + "DATA_SERVICE";
    public static final String ACTION_SERVICE = AUTHORITY + "ACTION_SERVICE";
    //public static final int ACTION_GET_FULL = 0;
    public static final int ACTION_GET_LINK_SERVER = 1;
//    public static final int ACTION_LIKE_EVENT = 2;

    public static final String RESULT_HANDLER = AUTHORITY + "RESULT_HANDLER";
    public static final String RESULT_ERROR = AUTHORITY + "RESULT_ERROR";
    public static final String RESULT_ERROR_CONTENT = AUTHORITY + "RESULT_ERROR_CONTENT";
    public static final String CHOOSEYOUSIDE = "choose_your_side";
    public static final int VIDEO_YOUTUBE = 0;
    public static final int VIDEO_GOOGLE_DRIVER = 1;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showSnackBar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View sview = snackbar.getView();
        TextView tv = (TextView) sview.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
