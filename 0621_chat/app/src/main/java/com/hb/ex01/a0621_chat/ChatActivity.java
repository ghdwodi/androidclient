package com.hb.ex01.a0621_chat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by HB04-03 on 2017-06-21.
 */

public class ChatActivity extends Activity{
    Handler handler;
    Socket s;
    EditText et_msg,et_playername;
    TextView tv_chat;
    Button btn_send;
    String ip,port,msg,result,playerName;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        handler = new Handler();

        s = null;
        et_msg = (EditText)findViewById(R.id.et_msg);
        et_playername = (EditText)findViewById(R.id.et_playername);
        tv_chat = (TextView)findViewById(R.id.tv_chat);
        btn_send = (Button)findViewById(R.id.btn_send);
        result="";

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = "192.168.0.133";
                port = "7979";
                msg = et_msg.getText().toString().trim();
                playerName = et_playername.getText().toString().trim();
                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 서버에 가기 위한 메소드 호출, 결과 받기
                        result += callServer(ip,port,200,msg,playerName)+"\n";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_chat.setText(result);
                                et_msg.setText("");
                            }
                        });
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });
    }
    public String callServer(String ip, String port, int cmd, String msg, String playerName){
        String res = null;
        // 서버와 통신
        try {
            Protocol pro = new Protocol();
            pro.setCmd(cmd);
            pro.setMessage(msg);
            pro.setPlayerName(playerName);
            s = new Socket(ip,Integer.parseInt(port));
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(pro);
            oos.flush();

            pro = (Protocol)ois.readObject();

            res = pro.getPlayerName()+"님 : "+pro.getMessage();

        }catch (Exception e){

        }
        return res;
    }
}
