package com.phone.apple.getphone_contacts;

import android.util.Log;

/**
 * 日志管理器
 * 
 * @author lchl 
 * 
 */
public class Logger {
	//show=6  no show=0
	/** 当前日志输出等级 **/
	private static final int inputLevel = 6;//6：打印 日志 -1：不打印 日志
	//private static final int inputLevel = -1;//6：打印 日志 -1：不打印 日志
	public static final int VERBOSE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	

	public static void v(String tag, String msg) {
		if (inputLevel > VERBOSE) {
			//Log.v(tag, msg);
			show(VERBOSE, tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (inputLevel > DEBUG) {
			//Log.d(tag, msg);
			show(DEBUG, tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (inputLevel > INFO) {
			//Log.i(tag, msg);
			show(INFO, tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (inputLevel > WARN) {
			//Log.w(tag, msg);
			show(WARN, tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (inputLevel > ERROR) {
			//Log.e(tag, msg);
			show(ERROR, tag, msg);
		}
	}
	
	private static void show(int type,String tag,String msg) {  
		msg = msg.trim();  
        int index = 0;  
        int maxLength = 3800;
        String sub;
        while (index < msg.length()) {  
            // java的字符不允许指定超过总的长度end  
            if (msg.length() <= index + maxLength) {  
                sub = msg.substring(index);  
            }else {  
                sub = msg.substring(index, index + maxLength);  
            }
            index += maxLength;
            String outString = sub.trim();
            if (index == 0 ) {
            	outString = tag +" --- "+ sub.trim();
			}
            if (type == VERBOSE) {
				Log.v(tag, outString); 
			}else if (type == DEBUG) {
				Log.d(tag, outString); 
			}else if (type == INFO) {
				Log.i(tag, outString); 
			}else if (type == WARN) {
				Log.w(tag, outString); 
			}else {//if (type == ERROR) 
				Log.e(tag, outString); 
			}
        }  
    }  
}