package com.www.myservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //Handler handler2;
    Runnable runnable;
    Runnable runnable1;
    //Runnable runnable2;
//    @InjectView(R.id.id_edittext)
//    EditText idEdittext;

    String x = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        sp = getSharedPreferences("URLs", MODE_PRIVATE);
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
                handler.postDelayed(runnable, 63000);
                System.out.println("---------关闭---------");
            }
        };
        handler.postDelayed(runnable, 63000);

        handler1 = new Handler();
        runnable1 = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    GetUrl();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                handler1.postDelayed(runnable1, 120 * 1000);
            }
        };
        handler1.postDelayed(runnable1, 120 * 1000);

//        handler2 = new Handler();
//        runnable2 = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                weChatController.openWebView(sp.getString("main_web", "") + "/1/1/v1.html");
//                editor.putBoolean("sendTxt", true);
//                editor.commit();
//                handler2.postDelayed(runnable2, 80 * 1000);
//            }
//        };
//        handler2.postDelayed(runnable2, 80 * 1000);
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        System.out.println("------main_web--------:" + sp.getString("main_web", ""));
        System.out.println("------isSend--------:" + sp.getString("url", ""));

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
        new MyThread().start();
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
                try
                {
                    GetUrl();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
//            case R.id.id_bt_sure:
//
//                if (!"".equals(idEdittext.getText()))
//                {
//                    String new_web = idEdittext.getText().toString().trim();
//                    sp = getSharedPreferences("URLs", MODE_PRIVATE);
//                    editor = sp.edit();
//                    editor.putString("main_web", new_web);
//                    editor.commit();
//                    Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
//
//                    idEdittext.setText("");
//                }
//                break;
        }
    }

    private void SendMessages(final String url_get)
    {
        String id = sp.getString(url_get, "");
        System.out.println("--------------id-----sendMessage-----------:" + id);
        String url = "http://www.uku99.net/domain/stop";
        String url_real = url + "?id=" + id;

        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url_real, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                //请求成功回调-
                System.out.println("-----------success------给服务端发送----");
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

    /**
     * 正则匹配url
     */
    public String getHtml(String path) throws Exception
    {
        // 通过网络地址创建URL对象
        URL url = new URL(path);
        // 根据URL
        // 打开连接，URL.openConnection函数会根据URL的类型，返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设定URL的请求类别，有POST、GET 两类
        conn.setRequestMethod("GET");
        //设置从主机读取数据超时（单位：毫秒）
        conn.setConnectTimeout(5000);
        //设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(5000);
        // 通过打开的连接读取的输入流,获取html数据
        InputStream inStream = conn.getInputStream();

        // 得到html的二进制数据
        byte[] data = readInputStream(inStream);
        // 是用指定的字符集解码指定的字节数组构造一个新的字符串
        String html = new String(data, "utf-8");
        return html;
    }

    /**
     * 读取输入流，得到html的二进制数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public byte[] readInputStream(InputStream inStream) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    class MyThread extends Thread
    {
        public void run()
        {
            //你要实现的代码
            try
            {
                String urlsource = getHtml("http://res.sijiys.com/pub/wap/161031_h5_hb.html");

//                Pattern pattern = Pattern
//                        .compile("<a[^>]*href=(\\\"http([^\\\"]*))\\\">(.*?)</a>");
//                System.out.println(pattern.matcher(urlsource).matches());

//                List<String> list = new ArrayList<String>();
//                //regular expression of http url
//                Pattern p = Pattern.compile("<a[^>]*href=(\"http([^\"]*))\">(.*?)</a>");
//                Matcher m = p.matcher(urlsource.toString());
//                while (m.find())
//                    list.add(m.group());                                                //get matched URL
//                System.out.println("c) URL list:");
//                for(String s : list)
//                {
//                    s = s.replaceAll("\">.*</a>", "");                                  //remove the tags before url
//                    s = s.replaceAll("<a .*href=\"", "");                               //remove the tags after url
//                    System.out.println(s);
//                }


                int s = urlsource.indexOf("'");
                int s1 = urlsource.indexOf("?");

                String u = urlsource.substring(s + 1, s1);
                x = u.substring(111, u.lastIndexOf("/"));

                System.out.println("--------x--------------:" + x);

                String url = "http://www.uku99.net/domain/stop";
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
                            // editor.putBoolean("sendTxt", false);
                            editor.commit();

                            System.out.println("---------------" + js.getString("name"));

                            if (x.equals(js.getString("name")))
                            {
                                weChatController.openWebView(js.getString("name") + "/show2.html?" + Math.random());
                            } else if (js.getString("id").equals("0") || !x.equals(js.getString("name")))
                            {
                                System.out.println("---------sendTxt------------");

                                SendTxtMsg1("18506461805", sp.getString("url", ""));
                                SendTxtMsg1("18325611110", sp.getString("url", ""));
                                SendTxtMsg1("13681849965", sp.getString("url", ""));
                            }

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

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

