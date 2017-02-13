package com.www.myservice;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
        sp = getSharedPreferences("URLs", MODE_PRIVATE);
        editor = sp.edit();

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
            if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable() && rootNodeInfo.getText().toString().trim().equals("提示"))
            {
                System.out.println("---------------Success!-------rootNodeInfo----------" + rootNodeInfo);
//                SendMessages(url_get);
//                SendTxtMsg1("18506461805", url_get);
//                SendTxtMsg1("13681849965", url_get);
//                SendTxtMsg1("18512177770", url_get);
                editor.putBoolean("isSend", true);
                editor.commit();

            } else if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable())
            {
                System.out.println("-----------normal----------rootNodeInfo---------" + rootNodeInfo);
                if (rootNodeInfo.getText().toString().contains("网页由 " + sp.getString("url", "")))
                {
                    url_get = sp.getString("url", "");
                    System.out.println("--------url-----normal-----" + url_get);
                    id = sp.getString(url_get.trim(), "");
                    System.out.println("--------------id-------------:" + id);
                    editor.putBoolean("isSend", false);
                    editor.commit();
                }
            }
        }
    }

    @Override
    public void onInterrupt()
    {
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