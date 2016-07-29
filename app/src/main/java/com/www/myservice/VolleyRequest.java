package com.www.myservice;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/25.
 */
public class VolleyRequest
{

    public static StringRequest mRequest;
    public static Context context;

    public static void RequestGet(Context context, String url, String tag, VolleyInterface volleyInterface)
    {

        App.getHttpQueues().cancelAll(tag);
        mRequest = new StringRequest(Request.Method.GET, url, volleyInterface.loadingListener(),
                volleyInterface.errorListener());
        App.getHttpQueues().add(mRequest);
        App.getHttpQueues().start();
    }

    public static void RequestPost(Context context, String url, String tag, final Map<String, String> params,
                                   VolleyInterface volleyInterface)
    {
        App.getHttpQueues().cancelAll(tag);
        mRequest = new StringRequest(Request.Method.POST, url, volleyInterface.loadingListener(),
                volleyInterface.errorListener())
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };
        App.getHttpQueues().add(mRequest);
        App.getHttpQueues().start();
    }

}
