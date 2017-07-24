package com.hb.ex01.a0622_dbserver2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by HB04-03 on 2017-06-22.
 */

public class DBActivity extends Activity{
    static final String IP = "192.168.0.133";
    static final int PORT = 7890;
    ListView list;
    TextView tv_idx_main;
    EditText et_id,et_pwd,et_name,et_age,et_addr;
    Socket s;
    ArrayList<ListVO> voList;
    ListAdapter listAdapter;
    Handler handler;
    String msg;
    String result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_view);
        list = (ListView)findViewById(R.id.list);
        tv_idx_main = (TextView)findViewById(R.id.tv_idx_main);
        et_id = (EditText)findViewById(R.id.et_id);
        et_pwd = (EditText)findViewById(R.id.et_pwd);
        et_name = (EditText)findViewById(R.id.et_name);
        et_age = (EditText)findViewById(R.id.et_age);
        et_addr = (EditText)findViewById(R.id.et_addr);
        handler = new Handler();


        // cmd 목록
        // 100 : ID와 패스워드로 회원정보 하나 조회
        // 200 : 회원정보 입력
        // 300 : 회원정보 삭제
        // 400 : 회원정보 수정
        // 101 : 회원정보 전체 표시
        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_id.getText().length()==0 || et_pwd.getText().length()==0) {
                    Toast.makeText(DBActivity.this, "ID와 패스워드를 모두 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msg = "100,"+et_id.getText().toString().trim()+","+et_pwd.getText().toString().trim()+System.getProperty("line.separator");
                        result = sendMsg("10.10.10.133",PORT,msg);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String[] resultArr = result.split(",");
                                tv_idx_main.setText(resultArr[0]);
                                et_id.setText(resultArr[1]);
                                et_pwd.setText(resultArr[2]);
                                et_name.setText(resultArr[3]);
                                et_age.setText(resultArr[4]);
                                et_addr.setText(resultArr[5]);
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_id.getText().length()==0 ||
                        et_pwd.getText().length()==0 ||
                        et_name.getText().length()==0 ||
                        et_age.getText().length()==0 ||
                        et_addr.getText().length()==0) {
                    Toast.makeText(DBActivity.this, "회원정보를 모두 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                msg = "200,"+et_id.getText().toString().trim()+","+et_pwd.getText().toString().trim()+","+
                                        et_name.getText().toString().trim()+","+et_age.getText().toString().trim()+","+
                                        et_addr.getText().toString().trim()+","+System.getProperty("line.separator");
                                String result = sendMsg(IP,PORT,msg);
                                Toast.makeText(DBActivity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_id.getText().length()==0 || et_pwd.getText().length()==0) {
                    Toast.makeText(DBActivity.this, "ID와 패스워드를 모두 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                msg = "300,"+et_id.getText().toString().trim()+","+et_pwd.getText().toString().trim()+System.getProperty("line.separator");
                                String result = sendMsg(IP,PORT,msg);
                                Toast.makeText(DBActivity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_id.getText().length()==0 ||
                        et_pwd.getText().length()==0 ||
                        et_name.getText().length()==0 ||
                        et_age.getText().length()==0 ||
                        et_addr.getText().length()==0) {
                    Toast.makeText(DBActivity.this, "회원정보를 모두 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                msg = "400,"+et_id.getText().toString().trim()+","+et_pwd.getText().toString().trim()+System.getProperty("line.separator");
                                String result = sendMsg(IP,PORT,msg);
                                Toast.makeText(DBActivity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
        findViewById(R.id.btn_selectall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        voList = new ArrayList<>();
                        msg = "101"+System.getProperty("line.separator");
                        result = sendMsg(IP,PORT,msg);
                        String[] resultArr = result.split("#");
                        for (String k : resultArr){
                            String[] one = k.split(",");
                            voList.add(new ListVO(one[0],one[1],one[2],one[3],one[4],one[5]));
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listAdapter = new ListAdapter(DBActivity.this,R.layout.listadapter_layout,voList);
                                list.setAdapter(listAdapter);
                            }
                        });
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        });
    }

    public String sendMsg(String ip, int port, String msg){
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
}