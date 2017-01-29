package site.binghai.lsmsmailer;

import android.content.Context;
import android.os.IBinder;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/12/18.
 */
public class PhoneController {
    //挂断电话
    public void endCall(String incomingNumber){
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", String.class);
            IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
            iTelephony.endCall();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
