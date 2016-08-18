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

import org.json.JSONException;
import org.json.JSONObject;

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
    SharedPreferences.Editor editor;

    boolean isFresh = false;

    @Override
    public void onCreate()
    {
        //初始化
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
        sp = getSharedPreferences("URL", MODE_PRIVATE);
        editor = sp.edit();
        // System.out.println("--------url-----success-----" + sp.getString("url",""));
        // GetUrl();
        this.rootNodeInfo = accessibilityEvent.getSource();
        if (rootNodeInfo == null)
        {
            return;
        }

        if (rootNodeInfo != null && rootNodeInfo.getPackageName().equals("com.tencent.mm"))
        {
            if (rootNodeInfo.getClassName().equals("android.widget.FrameLayout") && rootNodeInfo.isClickable() && isFresh == false)
            {
                System.out.println("------------rootNodeInfo-------------" + rootNodeInfo);

                rootNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                //isFresh = true;
            }
        }

        if (rootNodeInfo.getText() != null && rootNodeInfo.getPackageName().equals("com.tencent.mm"))
        {
            if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable() && rootNodeInfo.getText().toString().trim().equals("提示") && sp.getBoolean("isSend", false) == false)
            {
                System.out.println("---------------Success!-------rootNodeInfo----------" + rootNodeInfo);

                if (rootNodeInfo.getText().toString().contains("网页由 " + sp.getString("url", "")))
                {
                    url_get = sp.getString("url", "");
                    System.out.println("--------url-----success-----" + url_get);
                }

                SendMessages();
                SendTxtMsg1("18506461805", url_get);
                SendTxtMsg1("13681849965", url_get);
                SendTxtMsg1("18512177770", url_get);

            } else if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable())
            {
                System.out.println("-----------normal----------rootNodeInfo---------" + rootNodeInfo);
                if (rootNodeInfo.getText().toString().contains("网页由 " + sp.getString("url", "")))
                {
                    url_get = sp.getString("url", "");
                    System.out.println("--------url-----normal-----" + url_get);
                    id = sp.getString(url_get.trim(), "");
                    System.out.println("--------------id-------------:" + id);
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
                //请求成功回调-
                System.out.println("-----------success------给服务端发送----");
                editor.putBoolean("isSend", true);
                editor.commit();
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

    private void SendTxtMsg(String phone)
    {
        String url_real;

        if ("1".equals(sp.getString("code", "1")))
        {
            url_real = "http://bd.shuangla.cc/tongzhi/notice?phone=" + phone + "&status=成功";
        } else
        {
            url_real = "http://bd.shuangla.cc/tongzhi/notice?phone=" + phone + "&status=失败";
        }

        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url_real, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                //请求成功回调
                System.out.println("-----------success-----成功或失败-----");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                //请求失败回调
            }
        });

        mStringRequest.setTag("aaa_get");
        App.getHttpQueues().add(mStringRequest);
    }

    private void SendTxtMsg1(final String phone, String url)
    {
        String url_real = "http://bd.shuangla.cc/tongzhi/index?phone=" + phone + "&cid=" + url;

        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url_real, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                //请求成功回调
                System.out.println("-----------success----短信------");
                editor.putBoolean("isSend", true);
                editor.commit();

                SendTxtMsg(phone);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                //请求失败回调
            }
        });

        mStringRequest.setTag("aab_get");
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