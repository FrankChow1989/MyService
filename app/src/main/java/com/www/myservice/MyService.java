package com.www.myservice;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by pc on 2016/7/28.
 */
public class MyService extends AccessibilityService
{
    //事件
    private AccessibilityNodeInfo rootNodeInfo;
    String id;
    String url_get;

    SharedPreferences sp;

    @Override
    public void onCreate()
    {
        //初始化
        sp = getSharedPreferences("URL", MODE_PRIVATE);
        super.onCreate();
    }

    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        System.out.println("----------start-------Emilia Clarke---");
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent)
    {
        //GetUrl();
        this.rootNodeInfo = accessibilityEvent.getSource();
        if (rootNodeInfo == null)
        {
            return;
        }

        //System.out.println("------------rootNodeInfo-------------" + rootNodeInfo);

        if (rootNodeInfo.getText() != null)
        {
            if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable() && rootNodeInfo.getText().toString().contains("提示"))
            {
                System.out.println("---------------Success!-------rootNodeInfo----------" + rootNodeInfo);

                if (rootNodeInfo.getText().toString().contains("网页由"))
                {
                    url_get = rootNodeInfo.getText().toString().replace("网页由", "").replace("提供", "");
                    System.out.println("--------url-----success-----" + url_get);
                }
                SendMessages();
            } else if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable())
            {
                System.out.println("-----------normal----------rootNodeInfo---------" + rootNodeInfo);
                if (rootNodeInfo.getText().toString().contains("网页由"))
                {
                    url_get = rootNodeInfo.getText().toString().replace("网页由", "").replace("提供", "");
                    System.out.println("--------url-----normal-----" + url_get);
                    id = sp.getString(url_get.trim(), "");
                    System.out.println("--------------id----------------:" + id);
                }
            }
        }
    }

    @Override
    public void onInterrupt()
    {

    }

    private void SendMessages()
    {

        id = sp.getString(url_get.trim(), "");

        System.out.println("--------------id----------------:" + id);


        String url = "http://bbb.18qhx.com/domain/stop";
        String url_real = url + "?id=" + id;

        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url_real, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                //请求成功回调


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                //请求失败回调
            }
        });

        mStringRequest.setTag("abc_get");
        App.getHttpQueues().add(mStringRequest);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
