package thientt.app.android.xemeuro2016.pojo;

import android.content.Context;
import android.util.Log;

public class TLog {
    private static final String TAG = "ThienTT";

    public static void d(Context context, String mess) {
        String name = context != null ? context.getClass().getName() : null;
        Log.d(TAG, (name != null ? name : "") + ": " + mess);
    }

    public static void i(Context context, String mess) {
        String name = context != null ? context.getClass().getName() : null;
        Log.i(TAG, (name != null ? name : "") + ": " + mess);
    }

    public static void e(Context context, String mess) {
        String name = context != null ? context.getClass().getName() : null;
        Log.e(TAG, (name != null ? name : "") + ": " + mess);
    }
}
