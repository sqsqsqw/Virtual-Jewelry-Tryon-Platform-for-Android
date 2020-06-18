package com.Hashqi.try_on_client.Util;

import android.content.Context;
import android.os.StrictMode;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {
    public static HttpAttribute Post(String string, String get, Context context)//string POST参数,get 请求的URL地址,context 联系上下文
    {
        String html = "";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            String urldizhi=get; //请求地址
            System.out.println(get);
            URL url=new URL(urldizhi);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(50000);//超时时间
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("User-Agent", Other.getUserAgent(context));
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            if(string != null) {
                out.write(string);
            }
            out.flush();
            out.close();

            InputStream inputStream=conn.getInputStream();
            byte[] data=StreamTool.read(inputStream);
            html = new String(data, "utf-8");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-----"+e);
            HttpAttribute http = new HttpAttribute();
            http.setCode(-1);
            http.setMsg("服务器连接失败");
            http.setData("none");
            return http;
        }
        System.out.println(html);
        return GsonUtil.GsonToBean(html, HttpAttribute.class);
    }
}
