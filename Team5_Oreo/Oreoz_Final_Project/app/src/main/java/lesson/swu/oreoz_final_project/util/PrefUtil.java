package lesson.swu.oreoz_final_project.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;

import lesson.swu.oreoz_final_project.R;

public class PrefUtil {
    private static Dialog mProgDig;

    //저장하는 Preference..String
    public static void setData(Context context, String key, String value) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //불러오는 Preference..String
    public static String getData(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    //저장하는 Preference..Boolean
    public static void setData(Context context, String key, boolean value) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //불러오는 Preference..Boolean
    public static boolean getDataBoolean(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    //화면에 프로그래스를 보여준다.
    public static void showProgress(Context context){
        if(mProgDig!=null&&mProgDig.isShowing()){
            mProgDig.hide();
        }
        mProgDig= new Dialog(context);
        mProgDig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgDig.setContentView(R.layout.view_progress);
        mProgDig.setCancelable(false);
        mProgDig.show();
    }//end showProgress

    //화면에서 프로그래스를 숨기기
    public static void hideProgress(Context context){
        if(mProgDig!=null){
            mProgDig.hide();
        }
    }//end hideProgress

}
