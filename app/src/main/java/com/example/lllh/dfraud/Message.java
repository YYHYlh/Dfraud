package com.example.lllh.dfraud;

/**
 * Created by lllh on 2018/2/2.
 */

public class Message {
    String number;
    String content;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Message(String number, String content) {
        this.number = number;
        this.content = content;
    }
    public boolean checkIsDraud(){

        return true;
    }
}
