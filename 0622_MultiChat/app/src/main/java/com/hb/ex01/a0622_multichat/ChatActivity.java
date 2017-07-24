package com.hb.ex01.a0622_multichat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by HB04-03 on 2017-06-22.
 */

public class ChatActivity extends Activity{
    final static String IP = "192.168.0.133";
    final static int PORT = 7878;
    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Handler handler;
    EditText et_msg;
    TextView tv_chat;
    String message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        et_msg = (EditText)findViewById(R.id.et_msg);
        tv_chat = (TextView)findViewById(R.id.tv_chat);
        handler = new Handler();

        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    connect(IP,PORT);
                }catch (Exception e){

                }
            }
        };
        t.setDaemon(true);
        t.start();

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(et_msg.getText().toString().trim());
                et_msg.setText("");
            }
        });
    }

    public void connect(String ip, int port){
        try {
            s = new Socket(ip,port);
            if(s!=null){
                oos = new ObjectOutputStream(s.getOutputStream());
                ois = new ObjectInputStream(s.getInputStream());
                while (s.isConnected()){
                    try {
                        message = (String)ois.readObject();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_chat.setText(tv_chat.getText().toString().trim()+"\n"+message);
                            }
                        });
                    }catch (Exception e){
                    }
                }
            }
        }catch (Exception e){

        }
    }

    public void sendMsg(String msg){
        try {
            oos.writeObject("이홍재#"+msg);
            oos.flush();
        }catch (Exception e){
        }
    }
}