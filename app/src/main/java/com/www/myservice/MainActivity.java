package com.www.myservice;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{

    @InjectView(R.id.id_start)
    Button idStart;
    List<URL> mList;

    WeChatController weChatController;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {


                super.handleMessage(msg);
            }
        };
    }


    private void CheckUrl()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                weChatController.openWebView("http://m.gqjcm.com/go.html?rnd=1469509861#s%@mp.weixin.qq.com/oks/@mp.weixin.qq.com/oks%40mp.weixin.qq.com/oks&#http://mp.weixin.qq.com/oks");
                weChatController.openWebView("http://www.163.com");

                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //Log.i(LogTag.WEBBIEW, "触摸指令-关闭webview");
                        System.out.println("-----触摸指令-关闭webview---");
                        CmdHelper.sendTap(0x57, 0x96);
                    }
                }, 4000);

            }
        }).start();
    }

    private void StartWeixin()
    {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.id_start)
    public void onClick()
    {
//        Intent mAccessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        startActivity(mAccessibleIntent);
        CheckUrl();
    }
}
