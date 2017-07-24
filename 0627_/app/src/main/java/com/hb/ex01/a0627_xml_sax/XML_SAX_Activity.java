package com.hb.ex01.a0627_xml_sax;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by HB04-03 on 2017-06-27.
 */

public class XML_SAX_Activity extends Activity{
    TextView tv_sax;
    Handler handler = new Handler();
    String responseData;
    String msg,msg1,msg2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_sax_view);
        tv_sax = (TextView)findViewById(R.id.tv_sax);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                callNetWork();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 서버에서 받은 정보를 파싱
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
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://www.kma.go.kr/XML/weather/sfc_web_map.xml");
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

    // SAX방식
    private void process(){
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new MySaxParseProcess());
            xmlReader.parse(new InputSource(new StringReader(responseData)));
            tv_sax.setText(msg1+"/"+msg2);
        }catch (Exception e){
        }
    }

    // SAX를 파싱하는 내부 클래스
    class MySaxParseProcess extends DefaultHandler{
        // characters : 시작 태그와 끝 태그 사이의 문자를 가지고 옴
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String str = new String(ch,start,length);   // 내부에서 한 줄씩 문자열을 만들어서 추가
            if(str.trim().length()!=0){
                msg1 += str+"\n";
            }
        }

        // startElement : 속성을 가지고 옴
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            msg2 += attributes.getValue("ta")+","+attributes.getValue("desc")+"\n";
        }
    }
}