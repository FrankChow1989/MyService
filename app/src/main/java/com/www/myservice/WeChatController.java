package com.www.myservice;


import android.os.Handler;

/**
 * Created by mochy-acer1 on 2016/6/23.
 */
public class WeChatController
{
    private static WeChatController instance=null;
    public static Handler mHandler=null;

    public WeChatController(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public static WeChatController getInstance(Handler handler) {
        if(instance==null){
            instance=new WeChatController(handler);
        }
        return instance;
    }

    public static void refreshMoments(){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CmdHelper.sendTap(0x29e,0x708);
            }
        },1000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CmdHelper.sendTap(0x212,0x15e);
            }
        },3000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CmdHelper.exec("input swipe 1400 230 400 400");
            }
        },7000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CmdHelper.exec("input swipe 400 400 230 1400");
            }
        },8000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CmdHelper.exec("input swipe 400 400 230 1400");
            }
        },9000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShellUtils.execCommand("am force-stop com.tencent.mm",true);
            }
        },9000);

    }

    private static void goHmoePage() {
        CmdHelper.sendTap(0x55,0x82);

    }

    public static void openWebView(String url){
        ShellUtils.execCommand(String.format("am start -n com.tencent.mm/.plugin.webview.ui.tools.WebViewUI -a android.intent.action.VIEW -d %s",url),true);
    }
    public static void openWebView(){
          ShellUtils.execCommand("a.tools.WebViewUI -a android.intent.action.VIEW -d %s",true);

    }


    public static void openWeiXin(){
        CmdHelper.exec("am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI");

    }

    public static  void closeWeiXin(){
                ShellUtils.execCommand("am force-stop com.tencent.mm",true);
    }


    public static void share(String url) {
//        openWebView(url);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(LogTag.WEBBIEW, "触摸指令");
//                CmdHelper.sendTap(0x27b,0x485);
//            }
//        }, 4000);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(LogTag.WEBBIEW, "触摸指令");
//                CmdHelper.sendTap(0x3e8,0xa5);
//            }
//        }, 5000);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(LogTag.WEBBIEW, "触摸指令");
//                CmdHelper.sendTap(0x319,0x1bc);
//            }
//        }, 6000);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(LogTag.WEBBIEW, "触摸指令");
//                CmdHelper.sendTap(0x3b0,0x82);
//            }
//        }, 7000);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               CmdHelper.exec("am force-stop com.tencent.mm");
//            }
//        }, 8000);

    }

    public static void checkShare() {
        refreshMoments();
    }

    public void openMoments() {
        CmdHelper.exec("am start -n com.tencent.mm/.plugin.sns.ui.SnsTimeLineUI");
    }
}
