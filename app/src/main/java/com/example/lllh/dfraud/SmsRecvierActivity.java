package com.example.lllh.dfraud;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SmsRecvierActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private TextView textView;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public ArrayList data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_recvier);
        queryAuthority();
        ListView listView= (ListView) findViewById(R.id.message);
        data =new ArrayList();
        initMessages();
        ArrayAdapter<String > adapter=new ArrayAdapter<String>(SmsRecvierActivity.this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
    }
    private void initMessages(){
        ContentResolver cr = getContentResolver();
        Cursor cursor=cr.query(Uri.parse("content://sms/"),null,null,null,null);
        while (cursor.moveToNext()) {
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            if (type == 1) {
                Message message = new Message(cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("body")));
                ; //获取短信的号码
                //String date = simpleDateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex("date"))));//获取短信的日期
                if(message.checkIsDraud()){
                    data.add(message.getNumber()+"\n"+message.getContent());
                }
                //Toast.makeText(this,message.getNumber()+"\n"+message.getContent(),Toast.LENGTH_SHORT).show();

            }
            //获取类型，看是否是接收还是发送
   /*         String typeStr = "";
            if (type == 1) {
                typeStr = "接收";
            } else if (type == 2) {
                typeStr = "发送";
            } else {
                typeStr = null;
            }*/

            //   buffter.append(address + ":\t\t" + date + ":\t\t" + typeStr + ":\t\t" + body + "\r\n\n");//拼接短信内容
            //   Log.i("test", address + "   " + body + "  " + type);
        }
        cursor.close();
    }

    private boolean checkIsDraud(Message message){


        return true;
    }

    private void queryAuthority() {
        int hasReadContactsPermission = 0;
        //Android Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_SMS);
        }
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.READ_SMS},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }
    }

}
