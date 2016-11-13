package mobi.vhly.blackfire;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class SpUtils {

    public  static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences("PasswordManage", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }
    public  static boolean getBoolean(Context context,String key,boolean defvalue){
        SharedPreferences sp = context.getSharedPreferences("PasswordManage", Context.MODE_PRIVATE);
        return sp.getBoolean(key,defvalue);
    }
}
