package site.binghai.lsmsmailer;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.internal.telephony.ITelephony;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyPhoneBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = "收到来电";
    private CallBack callBack;

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        //呼入电话
        if(phoneNumber != null){
            Log.i(TAG,phoneNumber);
            new PhoneController().endCall(phoneNumber);
            Log.i("来电已挂断",phoneNumber);
            callBack.callback(TimeFormater.getSuitableToday()+" "+phoneNumber+"来电.");
            JSONObject msg = new JSONObject();
            msg.put("upload_type","phone");
            msg.put("phone",phoneNumber);
            new UploadUtil().Send(msg.toJSONString(),0);
        }

    }
}