package in.cbslgroup.ezeepeafinal.utils;

import android.app.Activity;
import android.content.Context;
import java.util.LinkedHashMap;
import in.cbslgroup.ezeepeafinal.R;

public class DmsUtil {

    public static String getEnglishText(String search, Context context){

        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();

        map.put(context.getString(R.string.pending),R.string.pending);
        map.put(context.getString(R.string.processed),R.string.processed);
        map.put(context.getString(R.string.approved),R.string.approved);
        map.put(context.getString(R.string.rejected),R.string.rejected);
        map.put(context.getString(R.string.aborted),R.string.aborted);
        map.put(context.getString(R.string.complete),R.string.complete);
        map.put(context.getString(R.string.done),R.string.done);
        map.put(context.getString(R.string.select_status),R.string.select_status);


        map.put(context.getString(R.string.normal),R.string.normal);
        map.put(context.getString(R.string.medium),R.string.medium);
        map.put(context.getString(R.string.urgent),R.string.urgent);
        map.put(context.getString(R.string.select_priority),R.string.select_priority);

        return  Util.getStringByLocal((Activity) context, map.get(search),"en");


    }



}
