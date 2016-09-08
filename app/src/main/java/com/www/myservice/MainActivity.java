package com.www.myservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{

    @InjectView(R.id.id_start)
    Button idStart;
    @InjectView(R.id.id_start_check)
    Button idStartCheck;

    WeChatController weChatController;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Handler handler;
    Handler handler1;
    Runnable runnable;
    Runnable runnable1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        sp = getSharedPreferences("URL", MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("isSend", false);
        editor.commit();

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                CmdHelper.sendTap(0x57, 0x96);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);

        handler1 = new Handler();
        runnable1 = new Runnable()
        {
            @Override
            public void run()
            {
                GetUrl();
                handler1.postDelayed(runnable1, 60 * 1000);
            }
        };
        handler1.postDelayed(runnable1, 60 * 1000);
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        if (sp.getBoolean("isSend", false) == true)
        {
            SendMessages(sp.getString("url", ""));
        }
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

    private void GetUrl()
    {
        String url = "http://bbb.18qhx.com/domain/stop";
        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                try
                {
                    JSONObject js = new JSONObject(s);
                    editor.putString("code", js.getString("code"));
                    editor.putString(js.getString("name"), js.getString("id"));
                    editor.putString("url", js.getString("name"));
                    editor.putBoolean("isSend", false);
                    editor.commit();

                    weChatController.openWebView(js.getString("name") + "/show.html");
                    weChatController.openWebView(js.getString("name") + "/show2.html");
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                //请求失败回调
                System.out.println(volleyError);
            }
        });
        mStringRequest.setTag("abc_get");
        App.getHttpQueues().add(mStringRequest);
    }

    @OnClick({R.id.id_start, R.id.id_start_check})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.id_start:
                Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(mAccessibleIntent);
                break;
            case R.id.id_start_check:
                GetUrl();
                break;
        }
    }

    private void SendMessages(final String url_get)
    {
        String id = sp.getString(url_get, "");
        System.out.println("--------------id-----sendMessage-----------:" + id);
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
                SendTxtMsg1("18506461805", sp.getString("url", ""));
                SendTxtMsg1("13681849965", sp.getString("url", ""));
                SendTxtMsg1("18512177770", sp.getString("url", ""));
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
}
