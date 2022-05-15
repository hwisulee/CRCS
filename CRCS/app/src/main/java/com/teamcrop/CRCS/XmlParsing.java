package com.teamcrop.CRCS;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URL;

public class XmlParsing extends MainActivity {
    private static String service_key = MainActivity.service_key;

    static String getXmlUltraSrtFcstData(int nx, int ny) {
        StringBuffer buffer = new StringBuffer();

        String[] today = getCurrentTimeData("today").split(" ");
        String[] yesterday = getCurrentTimeData("yesterday").split(" ");
        String[] onehour = getCurrentTimeData("onehour").split(" ");

        String returnType = "xml";
        String num_of_rows = "100";
        String pageNo = "1";
        String base_date = today[0];
        String base_time = today[1];

        if (base_time.substring(0, 2).equals("00")) {
            base_date = yesterday[0];
            base_time = onehour[1];
            base_time = base_time.substring(0, 2) + "30";
        }
        else {
            base_time = onehour[1];
            base_time = base_time.substring(0, 2) + "30";
        }

        String queryUrl="http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?"
                + "serviceKey=" + service_key
                + "&returnType=" + returnType
                + "&numOfRows=" + num_of_rows
                + "&pageNo=" + pageNo
                + "&base_date=" + base_date
                + "&base_time=" + base_time
                + "&nx=" + nx
                + "&ny=" + ny;

        try {
            URL url = new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); // url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); // inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // tag 이름 얻어오기
                        if (tag.equals("resultCode")) {
                            xpp.next();
                            buffer.append(xpp.getText()); // category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); // 줄바꿈 문자 추가
                        }

                        if(tag.equals("item")); // 첫번째 검색결과
                        else if(tag.equals("category")) {
                            //buffer.append("자료코드 : ");
                            xpp.next();
                            buffer.append(xpp.getText()); // category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(", "); // 줄바꿈 문자 추가
                        }
                        else if(tag.equals("fcstDate")) {
                            //buffer.append("발표일자 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append(", ");
                        }
                        else if(tag.equals("fcstTime")) {
                            //buffer.append("발표시각 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append(", ");
                        }
                        else if(tag.equals("fcstValue")) {
                            //buffer.append("예보 값 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            //buffer.append(", ");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); // tag 이름 얻어오기

                        if(tag.equals("item")) {
                            buffer.append("\n"); // 첫번째 검색결과종료..줄바꿈
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString(); // StringBuffer 문자열 객체 반환
    }

    static String getXmlVilageFcstData(int nx, int ny) {
        StringBuffer buffer = new StringBuffer();

        String[] today = getCurrentTimeData("today").split(" ");
        String[] yesterday = getCurrentTimeData("yesterday").split(" ");

        String returnType = "xml";
        String num_of_rows = "200";
        String pageNo = "1";
        String base_date = today[0];
        String base_time = today[1];

        if (base_time.substring(0, 2).equals("00") || base_time.substring(0, 2).equals("01")) {
            base_date = yesterday[0];
            base_time = "0200";
        }
        else if (base_time.substring(0, 2).equals("02")) {
            int temp = Integer.parseInt(base_time.substring(2, 4));

            if (temp >= 0 && temp <= 10) {
                base_date = yesterday[0];
                base_time = "0200";
            }
        }

        base_time = "0200";

        String queryUrl="http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"
                + "serviceKey=" + service_key
                + "&returnType=" + returnType
                + "&numOfRows=" + num_of_rows
                + "&pageNo=" + pageNo
                + "&base_date=" + base_date
                + "&base_time=" + base_time
                + "&nx=" + nx
                + "&ny=" + ny;

        try {
            URL url = new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); // url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); // inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // tag 이름 얻어오기
                        if (tag.equals("resultCode")) {
                            xpp.next();
                            buffer.append(xpp.getText()); // category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); // 줄바꿈 문자 추가
                        }

                        if(tag.equals("item")); // 첫번째 검색결과
                        else if(tag.equals("category")) {
                            //buffer.append("자료코드 : ");
                            xpp.next();
                            buffer.append(xpp.getText()); // category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(", "); // 줄바꿈 문자 추가
                        }
                        else if(tag.equals("fcstValue")) {
                            //buffer.append("예보 값 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            //buffer.append(", ");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); // tag 이름 얻어오기

                        if(tag.equals("item")) {
                            buffer.append("\n"); // 첫번째 검색결과종료..줄바꿈
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString(); // StringBuffer 문자열 객체 반환
    }
}
