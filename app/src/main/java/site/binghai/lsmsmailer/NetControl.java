package site.binghai.lsmsmailer;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/6/21.
 */
public class NetControl {
    public static void setMobileData(Context pContext, boolean pBoolean) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;

            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);

            method.invoke(mConnectivityManager, pBoolean);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("移动数据设置错误: " + e.toString());
        }
    }

    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg
     *            默认填null
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState(Context pContext, Object[] arg) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }

            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);

            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

            return isOpen;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("得到移动数据状态出错");
            return false;
        }

    }
}

