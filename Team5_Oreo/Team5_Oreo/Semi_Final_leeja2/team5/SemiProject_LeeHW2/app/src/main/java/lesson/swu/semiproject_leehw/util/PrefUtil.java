package lesson.swu.semiproject_leehw.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {

    //저장하는 Preference
    public static void setData(Context context, String key, String value) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //불러오는 Preference
    public static String getData(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
