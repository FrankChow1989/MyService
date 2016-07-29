package com.www.myservice;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by pc on 2016/7/28.
 */
public class App extends Application
{

    //请求队列
    public static RequestQueue mRequestQueue;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueues()
    {
        return mRequestQueue;
    }
}
