package com.hb.ex01.a0628_json;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by HB04-03 on 2017-06-28.
 */

public class JSONActivity extends Activity{
    TextView tv_json;
    Handler handler = new Handler();
    String responseData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_view);
        tv_json = (TextView)findViewById(R.id.tv_json);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                callNetWork();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        process();
                    }
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void callNetWork(){
        InputStream in = null;
        String msg =null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://openapi.seoul.go.kr:8088/sample/json/SeoulLibraryTime/1/5/");
            conn = (HttpURLConnection)url.openConnection();
            in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer stringBuffer = new StringBuffer();
            while((msg=br.readLine())!=null){
                stringBuffer.append(msg);
            }
            responseData = stringBuffer.toString();
            br.close();
        }catch (Exception e){
            conn.disconnect();
        }
    }

    private void process(){
        try {
            BufferedReader br = new BufferedReader(new StringReader(responseData));
            String res = br.readLine();
            String result = "";
            JSONObject jsonObject1 = new JSONObject(res.toString());
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("SeoulLibraryTime"));
            JSONArray jsonArray = new JSONArray(jsonObject2.getString("row"));
            for(int i=0;i<jsonArray.length();i++){
                result += "도서관 이름 : "+jsonArray.getJSONObject(i).getString("LBRRY_NAME")+"\n";
                result += "위치 : "+jsonArray.getJSONObject(i).getString("ADRES")+"\n";
                result += "휴무 : "+jsonArray.getJSONObject(i).getString("FDRM_CLOSE_DATE")+"\n";
                result += "전화 : "+jsonArray.getJSONObject(i).getString("TEL_NO")+"\n";
                result += "================================\n";
            }
            tv_json.setText(result);
        }catch (Exception e){

        }
    }
}
