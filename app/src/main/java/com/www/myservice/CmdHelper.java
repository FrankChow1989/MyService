package com.www.myservice;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class CmdHelper
{

	/**
	 * 执行shell命令
	 *
	 * @param cmds
	 */
	public static void exec(String... cmds) {
		StringBuffer sb = new StringBuffer();
		try {
			// 申请获取root权限，这一步很重要，不然会没有作用
			Process process = Runtime.getRuntime().exec("su");
			// 获取输出流
			OutputStream outputStream = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(
					outputStream);
			for (int i = 0; i < cmds.length; i++) {
				sb.append(cmds[i]);
				sb.append("\n");
			}
			dataOutputStream.writeBytes(sb.toString());
			dataOutputStream.flush();
			dataOutputStream.close();
			outputStream.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	/**
	 * 发送屏幕点击命令
	 * @param x
	 * @param y
	 */
	public static void sendTap(int x, int y) {
		exec(getTap(x, y));
	}
	/**
	 * 发送键盘命令
	 * @param keys
	 */
	public static void sendKeyEvent(int ...keys) {
		List<String> cmds=new ArrayList<String>();
		for (int i = 0; i < keys.length; i++) {
			cmds.add(getKeyEvent(keys[i]));
		}
		exec((String[])cmds.toArray(new String[0]));
	}
	/**
	 * 根据左边生成点击命令
	 * @param x
	 * @param y
	 * @return
	 */
	public static String getTap(int x, int y) {
		String cmd = "input tap " + x + " " + y;
		return cmd;
	}
	/**
	 * 根据键值生成键盘命令
	 * @param key
	 * @return
	 */
	public static String getKeyEvent(int key) {
		String cmd = "input keyevent " + key;
		return cmd;
	}


}
