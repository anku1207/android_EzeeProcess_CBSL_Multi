package in.cbslgroup.ezeepeafinal.utils;

import android.content.Context;
import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class VolleySingelton {

    private static VolleySingelton volleySingelton;
    private RequestQueue requestQueue;
    private static Context mctx;

    private VolleySingelton(Context context){
        mctx=context;
        this.requestQueue=getRequestQueue();

    }
    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized VolleySingelton getInstance(Context context){
        if (volleySingelton==null){
            volleySingelton=new VolleySingelton(context);
        }
        return volleySingelton;
    }
    public<T> void addToRequestQueue(@NonNull Request<T> request){

        request.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       // request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }



}
