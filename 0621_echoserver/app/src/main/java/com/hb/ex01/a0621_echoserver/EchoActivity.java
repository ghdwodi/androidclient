package com.hb.ex01.a0621_echoserver;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by HB04-03 on 2017-06-21.
 */

// 통신할 때는 반드시 쓰레드 처리
// 쓰레드 처리에서 값 전달은 반드시 handler 이용
public class EchoActivity extends Activity{
    Handler handler;
    Socket s;
    EditText et_serveraddr,et_serverport,et_msg;
    TextView tv_echo;
    String ip,port,msg,result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.echo_view);
        handler = new Handler();
        s = null;
        et_serveraddr = (EditText)findViewById(R.id.et_serveraddr);
        et_serverport = (EditText)findViewById(R.id.et_serverport);
        et_msg = (EditText)findViewById(R.id.et_msg);
        tv_echo = (TextView)findViewById(R.id.tv_echo);
        result = "";

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = et_serveraddr.getText().toString().trim();
                port = et_serverport.getText().toString().trim();
                msg = et_msg.getText().toString().trim();
                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 서버에 가기 위한 메소드 호출, 결과 받기
                        result += callServer(ip,port,msg)+"\n";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_echo.setText(result);
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
    public String callServer(String ip, String port, String msg){
        String res = null;
        // 서버와 통신
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            s = new Socket(ip,Integer.parseInt(port));
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bw.write(msg+"\n");
            bw.flush();

            res = br.readLine();
        }catch (Exception e){

        }finally {
            try {
                s.close();
                br.close();
                bw.close();
            }catch (Exception e2){
            }
        }
        return res;
    }
}
