package site.binghai.lsmsmailer;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by Administrator on 2016/12/18.
 */
public class UploadUtil {
    public void Send(final String msg,final int T){
        if(T > 5)
            return;

        RequestParams requestParams = new RequestParams();
        requestParams.add("data",msg);
        requestParams.add("pass","bh1041414957");

        HttpUtils.post("upload.do", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("success","success");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("FAILED","FAILED");
                //Send(msg,T+1);
            }
        });
    }
}
