package view;

import android.content.Context;

public class ResourceUtil {
    public static String getResourceStr(Context context,int resourseId){
       return context.getResources().getString(resourseId) ;
    }
    /**
     * @param parameter String 类型的参数
     * */
    public static String getResourceStrOneParameter(Context context,int resourseId,String parameter){
        return String.format(context.getResources().getString(resourseId),parameter) ;
    }
}
