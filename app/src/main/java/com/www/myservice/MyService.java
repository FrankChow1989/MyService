package com.www.myservice;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2016/7/28.
 */
public class MyService extends AccessibilityService
{
    //事件
    private AccessibilityNodeInfo rootNodeInfo;
    private List<AccessibilityNodeInfo> mReceiveNode;
    private String lastFetchedHongbaoId = null;
    private long lastFetchedTime = 0;
    private static final int MAX_CACHE_TOLERANCE = 10000;
    String tip;
    private final static String QQ_SEND = "发送";

    List<URL> mList = new ArrayList<>();
    int size;

    boolean isSend = false;

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

    private void GetUrl()
    {
        String url = "http://bbb.18qhx.com/domain/index";
        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                try
                {
                    JSONArray js = new JSONArray(s);

                    for (int i = 0; i < js.length(); i++)
                    {
                        URL url = new URL();
                        JSONObject jsonObject = (JSONObject) js.opt(i);
                        url.setName(jsonObject.getString("name"));
                        url.setId(jsonObject.getString("id"));
                        mList.add(url);
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
            }
        });

        mStringRequest.setTag("abc_get");
        App.getHttpQueues().add(mStringRequest);
    }


    /**
     * 遍历控件，自动发送回复
     *
     * @param
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void recycle2(final AccessibilityNodeInfo info)
    {

        System.out.println("------------info-------------:" + info);

        if (info.getChildCount() == 0)
        {
            if (info.getClassName().toString().equals("android.widget.ImageView"))
            {
//                Bundle arguments = new Bundle();
//                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, mList.get(0).getName());
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

//            if (info.getClassName().toString().equals("android.widget.Button") && info.getText().toString().equals(QQ_SEND))
//            {
//                new Handler().postDelayed(new Runnable()
//                {
//                    public void run()
//                    {
//                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        isSend = true;
//                    }
//                }, 10 * 1000);
//            }
        } else
        {
            for (int i = 0; i < info.getChildCount(); i++)
            {
                if (info.getChild(i) != null)
                {
                    recycle2(info.getChild(i));
                }
            }
        }
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

//        if (rootNodeInfo.getText() != null)
//        {
//            if (rootNodeInfo.getClassName().equals("android.widget.TextView") && !rootNodeInfo.isLongClickable() && rootNodeInfo.getText().toString().contains("提示"))
//            {
//                System.out.println("---------------Success!-----------------");
//                // rootNodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
//                new Handler().postDelayed(new Runnable()
//                {
//                    public void run()
//                    {
//                        //返回
//                        recycle2(rootNodeInfo);
//                    }
//                }, 4 * 1000);
//                SendMessages();
//            }
//        }

        //list先初始化;
        mReceiveNode = null;
        /**
         * 检索事件
         */
        if (mList.size() != 0)
        {
            //checkNodeInfo();
        }

        if (mLuckyMoneyReceived && (mReceiveNode != null))
        {
            size = mReceiveNode.size();
            if (size > 0)
            {

                System.out.println("--------mReceiveNode-------"+mReceiveNode);

                final AccessibilityNodeInfo cellNode = mReceiveNode
                        .get(size - 1);
                final AccessibilityNodeInfo rowNode = getRootInActiveWindow();
                String id = getHongbaoText(mReceiveNode.get(size - 1));
                long now = System.currentTimeMillis();

                //缓存判断是否再次点击
                if (this.shouldReturn(id, now - lastFetchedTime))
                    return;

                lastFetchedHongbaoId = id;
                lastFetchedTime = now;

                if (cellNode != null)
                {
                    if (cellNode.getPackageName().equals("com.tencent.mm"))
                    {
                        System.out.println("------------cellNode------------:" + cellNode);

                        if (cellNode.getText() != null)
                        {
                            if (cellNode.getClassName().equals("android.widget.TextView") && cellNode.getText().toString().contains("爽啦") && cellNode.isClickable() == true)
                            {
                                System.out.println("--------onclick---------");
                                cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                isSend = false;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean mLuckyMoneyReceived;

    List<AccessibilityNodeInfo> nodes;

    private void checkNodeInfo()
    {
        if (rootNodeInfo == null)
        {
            return;
        }

        //System.out.println("-------mList--------" + mList.get(0).getName());

        /* 聊天会话窗口，遍历节点匹配“点击拆开”，“口令红包”，“点击输入口令” */
        nodes = this
                .findAccessibilityNodeInfosByTexts(this.rootNodeInfo,
                        new String[]{"爽啦助手1","爽啦助手3","爽啦助手4"});

        if (!nodes.isEmpty())
        {
            String nodeId = Integer.toHexString(System
                    .identityHashCode(this.rootNodeInfo));
            if (!nodeId.equals(lastFetchedHongbaoId))
            {
                mLuckyMoneyReceived = true;
                mReceiveNode = nodes;
            }
            return;
        }
    }

    private List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(
            AccessibilityNodeInfo nodeInfo, String[] texts)
    {
        for (String text : texts)
        {
            if (text == null)
                continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo
                    .findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty())
            {
                return nodes;
            }
        }
        return new ArrayList<AccessibilityNodeInfo>();
    }

    private String getHongbaoText(AccessibilityNodeInfo node)
    {
        /* 获取红包上的文本 */
        String content;
        try
        {
            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
        } catch (NullPointerException npe)
        {
            return null;
        }
        return content;
    }

    /**
     * 判断是否返回,减少点击次数 现在的策略是当红包文本和缓存不一致时,戳 文本一致且间隔大于MAX_CACHE_TOLERANCE时,戳
     *
     * @param id       红包id
     * @param duration 红包到达与缓存的间隔
     * @return 是否应该返回
     */
    private boolean shouldReturn(String id, long duration)
    {
        // ID为空
        if (id == null)
            return true;

        // 名称和缓存不一致
        if (duration < MAX_CACHE_TOLERANCE && id.equals(lastFetchedHongbaoId))
        {
            return true;
        }
        return false;
    }

    @Override
    public void onInterrupt()
    {

    }

    private void SendMessages()
    {
        String url = "http://bd.shuangla.cc/tongzhi/index?phone=18512177770&cid=http://www.baidu.com";
        //-----------------------StringRequest-----------------------
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s)
            {
                System.out.println("---------------sssssssssssssssss---------------:" + s);
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
