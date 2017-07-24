package com.hb.ex01.a0622_dbserver;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by HB04-03 on 2017-06-22.
 */

public class Ex01_db extends Activity{
    ListView list;
    Socket s;
    String msg;
    String result;
    Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_view);
        list = (ListView)findViewById(R.id.list);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msg = "test";
                        final String reData = callServer("10.10.10.133",9797,msg);
                        result = reData;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Ex01_db.this, result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        findViewById(R.id.btn_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msg = "db";
                        final String reData = callServer("10.10.10.133",9797,msg);
                        result = reData;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showData();
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
    }

    public String callServer(String ip, int port, String msg){
        String res = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            s = new Socket(ip,port);
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            msg = msg + System.getProperty("line.separator");
            bw.write(msg);
            bw.flush();

            res = br.readLine();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            try {
                s.close();
                br.close();
                bw.close();
            }catch (Exception e){

            }
        }
        return res;
    }

    public void showData(){
        String[] items = null;
        try {
            if(result.equals("No data")){
                items = new String[1];
                items[0] = "반환된 자료 없음";
            }else{
                String[] str = result.split(",");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,str);
                list.setAdapter(adapter);
            }
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
