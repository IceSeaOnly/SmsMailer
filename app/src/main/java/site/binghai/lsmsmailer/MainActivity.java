package site.binghai.lsmsmailer;

import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;


public class MainActivity extends AppCompatActivity {
    private ImageView open_net;
    private ImageView delete;
    private ImageView btn_test;
    private ImageView btn_off_phone_call;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private boolean off_phone_state;
    private TextView netstate;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("副卡助手跟踪端 --冰海软件");
        InitView();
        InitAction();
        InitData();

    }

    private MyPhoneBroadcastReceiver mBroadcastReceiver;
    public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;

    private void RegPhoneReceiver(boolean opt) {
        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "解绑电话监听器失败", Toast.LENGTH_SHORT).show();
        }
        if (!opt) return;
        try {
            mBroadcastReceiver = new MyPhoneBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(B_PHONE_STATE);
            intentFilter.setPriority(Integer.MAX_VALUE);
            registerReceiver(mBroadcastReceiver, intentFilter);
            Toast.makeText(this, "注册电话监听器成功", Toast.LENGTH_LONG).show();
            mBroadcastReceiver.setCallBack(new CallBack() {
                @Override
                public void callback(String msg) {
                    Insert(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "注册电话监听器失败", Toast.LENGTH_SHORT).show();
        }

    }

    private String lastMsg = "";
    private void Insert(String msg) {
        if(lastMsg.equals(msg))
            return;
        lastMsg = msg;
        adapter.insert(msg,0);
        adapter.notifyDataSetChanged();
    }

    private void InitData() {
        off_phone_state = false;
        smsObserver = new SmsObserver(this, smsHandler, new CallBack() {
            @Override
            public void callback(String msg) {
                Insert(msg);
            }
        });
        getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);

        adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item);
        listView.setAdapter(adapter);
    }

    public Handler smsHandler = new Handler() {
        //回调操作
    };

    private void InitAction() {
        open_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetControl.setMobileData(MainActivity.this, NetControl.getMobileDataState(MainActivity.this,null));
                open_net.setImageResource(NetControl.getMobileDataState(MainActivity.this,null)?R.mipmap.connect:R.mipmap.disconnect);
                netstate.setText(NetControl.getMobileDataState(MainActivity.this,null)?"网络已连接":"网络未连接");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsObserver.CleanAllSms();
            }
        });
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert(TimeFormater.getSuitableToday()+" "+"本地发送测试.");
                JSONObject msg = new JSONObject();
                msg.put("upload_type","test");
                msg.put("time",System.currentTimeMillis());
                new UploadUtil().Send(msg.toJSONString(), 0);
            }
        });
        btn_off_phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                off_phone_state = !off_phone_state;
                if (off_phone_state)
                    btn_off_phone_call.setImageResource(R.mipmap.call_on);
                else
                    btn_off_phone_call.setImageResource(R.mipmap.call_off);
                RegPhoneReceiver(off_phone_state);
            }
        });
    }

    private void InitView() {
        netstate = (TextView) findViewById(R.id.netstate);
        open_net = (ImageView) findViewById(R.id.open_net);
        delete = (ImageView) findViewById(R.id.delete);
        btn_test = (ImageView) findViewById(R.id.btn_test);
        btn_off_phone_call = (ImageView) findViewById(R.id.btn_off_phone_call);
        listView = (ListView) findViewById(R.id.history);
    }
}
