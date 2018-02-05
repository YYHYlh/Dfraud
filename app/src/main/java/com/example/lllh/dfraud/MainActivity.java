package com.example.lllh.dfraud;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText reportNumber;
    private TextView responseText;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String dbUrl="http://192.168.1.107/cgi-bin/3.py?number=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   queryAuthority();
        Button report=(Button)findViewById(R.id.report);
        Button newspaper=(Button)findViewById(R.id.newspaper);
        Button message_analyse=(Button)findViewById(R.id.message_analyse);
        reportNumber=(EditText)findViewById(R.id.report_number);
        responseText=(TextView)findViewById(R.id.response_text);
        report.setOnClickListener(this);
        newspaper.setOnClickListener(this);
        message_analyse.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.report:
                String  number=reportNumber.getText().toString();
                Toast.makeText(this,number,Toast.LENGTH_SHORT).show();
                OkHttpClient client = new OkHttpClient();
                String url=dbUrl+number;
                sendRequest(url);
                break;
            case R.id.newspaper:
                Intent intent=new Intent(MainActivity.this,newspaper.class);
                startActivity(intent);
                break;
            case R.id.message_analyse:
                Intent intent2 = new Intent(MainActivity.this,SmsRecvierActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }



    private void sendRequest(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request =new Request.Builder().url(url).build();
                    Response response=client.newCall(request).execute();
                    String jsondata=response.body().string();
                    parseJson(jsondata);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

    private void parseJson(String jsonData){
        Gson gson=new Gson();
        ResponseData responseData = gson.fromJson(jsonData, ResponseData.class);
        if (responseData.getSucess()==1){
            showResponse(new String("举报成功！该用户已被举报"+responseData.getTimes()+"次"));
        }
        else {
            showResponse(new String ("操作失败"));
        }

    }
  /*    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

  private void queryAuthority() {
        int hasReadContactsPermission = 0;
        //Android Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasReadContactsPermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
        }
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }
    }*/
}
