package com.teamcrop.CRCS;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class transCoord extends MainActivity {
    private static String apikey = MainActivity.restkey;

    static String transCoord(double longitude, double latitude) {
        StringBuffer buffer = new StringBuffer();

        URL queryUrl = null;
        try {
            queryUrl = new URL("https://dapi.kakao.com/v2/local/geo/transcoord.xml?"
                    + "x=" + longitude
                    + "&y=" + latitude
                    + "&input_coord=WGS84"
                    + "&output_coord=TM");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection con = (HttpURLConnection) queryUrl.openConnection();
            con.setRequestMethod("GET");

            String userCredentials = apikey;
            String basicAuth = "KakaoAK " + userCredentials;

            con.setRequestProperty("Authorization", basicAuth); //
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);

            InputStream is = new BufferedInputStream(con.getInputStream());

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("documents")) ;// 첫번째 검색결과
                        else if (tag.equals("x")) {
                            buffer.append("x : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(","); //줄바꿈 문자 추가
                        } else if (tag.equals("y")) {
                            buffer.append("y : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("documents")) {
                            //buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
