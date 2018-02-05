package com.example.lllh.dfraud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReciver extends BroadcastReceiver {
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //判断广播消息
        if (action.equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            //如果不为空
            if (bundle != null) {
                //将pdus里面的内容转化成Object[]数组
                Object pdusData[] = (Object[]) bundle.get("pdus");// pdus ：protocol data unit  ：
                //解析短信
                SmsMessage[] msg = new SmsMessage[pdusData.length];
                for (int i = 0; i < msg.length; i++) {
                    byte pdus[] = (byte[]) pdusData[i];
                    msg[i] = SmsMessage.createFromPdu(pdus);
                }
                StringBuffer content = new StringBuffer();//获取短信内容
                StringBuffer phoneNumber = new StringBuffer();//获取地址
                //分析短信具体参数
                for (SmsMessage temp : msg) {
                    content.append(temp.getMessageBody());
                    phoneNumber.append(temp.getOriginatingAddress());
                }
                Message message=new Message(phoneNumber.toString(),content.toString());
                //Toast.makeText(context, "发送者号码：" + phoneNumber.toString() + "  短信内容：" + content.toString(), Toast.LENGTH_SHORT).show();
                if (message.checkIsDraud()){
                    Toast.makeText(context,"小心！该短信很有可能是诈骗短信",Toast.LENGTH_LONG).show();
                }
             //可以去掉
                else {
                    Toast.makeText(context,"不是诈骗",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }





    private boolean checkIsDraud(Message message){

        return false;
    }
}