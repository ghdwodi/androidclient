package com.hb.ex01.a0628_xmlpullparser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HB04-03 on 2017-06-28.
 */

public class XmlPullParserActivity extends Activity{
    WebView wv;
    Handler handler = new Handler();
    String year, month, day, hour;
    ArrayList<XmlVO> xmlVoList = new ArrayList<>();
    final String WEATHER_URL="http://www.kma.go.kr/XML/weather/sfc_web_map.xml";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xmlview);
        wv = (WebView)findViewById(R.id.wv);
        recieverForCast();
    }

    private void recieverForCast(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                buildXmlPullParser(getStreamFromUrl());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showData();
                    }
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    // 해당 사이트 접속하는 InputStream
    private InputStream getStreamFromUrl(){
        InputStream in =null;
        String msg = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(WEATHER_URL);
            conn = (HttpURLConnection)url.openConnection();
            in = conn.getInputStream();
        }catch (Exception e){
        }
        return in;
    }
    private void buildXmlPullParser(InputStream in){
        String local="", desc="", ta="", icon="";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(in,"utf-8");
            while (xmlPullParser.getEventType()!=XmlPullParser.END_DOCUMENT){
                if(xmlPullParser.getEventType()==XmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("weather")){
                        year = xmlPullParser.getAttributeValue(0);
                        month = xmlPullParser.getAttributeValue(1);
                        day = xmlPullParser.getAttributeValue(2);
                        hour = xmlPullParser.getAttributeValue(3);
                    }
                    if(xmlPullParser.getName().equals("local")){
                        icon = xmlPullParser.getAttributeValue(1);
                        desc = xmlPullParser.getAttributeValue(2);
                        ta = xmlPullParser.getAttributeValue(3);
                    }
                }else if(xmlPullParser.getEventType()==XmlPullParser.TEXT){
                    local = xmlPullParser.getText();
                }else if(xmlPullParser.getEventType()==XmlPullParser.END_TAG){
                    if(!local.trim().equals("")){
                        xmlVoList.add(new XmlVO(local,desc,ta,icon));
                    }
                }
                xmlPullParser.next();
            }
        }catch (Exception e){
        }
    }

    // 리스트를 웹뷰에 넣기
    private void showData(){
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body><h2>기상청 제공 날씨 정보</h2>");
        sb.append("<p>"+year+"년 "+month+"월 "+day+"일 "+hour+"시 현재</p>");
        sb.append("<table width='100%' border='1px' text-align='center'>");
        sb.append("<thead>");
        sb.append("<tr><th>지역</th><th>날씨</th><th>온도</th><th>아이콘</th></tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        for (XmlVO k : xmlVoList){
            sb.append("<tr>");
            sb.append("<td>"+k.getLocal()+"</td>");
            sb.append("<td>"+k.getDesc()+"</td>");
            sb.append("<td>"+k.getTa()+"</td>");
            if(k.getIcon().equals("")){
                sb.append("<td></td>");
            }else{
                sb.append("<td>");
                sb.append("<img src='http://www.kma.go.kr/images/icon/NW/NB"+k.getIcon()+".png'/>");
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</body></html>");
        wv.loadDataWithBaseURL(null,sb.toString(),"text/html","utf-8",null);
    }
}
