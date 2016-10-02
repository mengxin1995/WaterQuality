package com.zafu.waterquality.utils;

/**
 * Created by mengxin on 16-10-1.
 */
public class LogUtil {
    private static LogUtil sLogUtil;

    public final int DEGUB = 0;

    public final int INFO = 1;

    public final int ERROR = 2;

    public final int NOTHING = 3;

    public int level = DEGUB;

    private LogUtil() {
    }

    public static LogUtil getInstance() {
        if(sLogUtil == null) {
            synchronized (LogUtil.class) {
                if (sLogUtil == null) {
                    sLogUtil = new LogUtil();
                }
            }
        }
        return sLogUtil;
    }

    public void debug(String msg) {
        if (DEGUB >= level) {
            System.out.println(msg);
        }
    }

    public void info(String msg) {
        if (INFO >= level) {
            System.out.println(msg);
        }
    }

    public void error(String msg) {
        if (ERROR >= level) {
            System.out.println(msg);
        }
    }
}
