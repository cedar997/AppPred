package com.rom471.net;

import android.util.Log;

import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import com.rom471.utils.AppUtils;
import com.rom471.utils.MyProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class DataSender {
    public static String getUrl(){
        String host =(String) MyProperties.get(AppUtils.getContext(), "host", "192.168.199.151");
        return "http://"+host+":5000/";
    }
    public static String send(SimpleApp app){
        Log.d("cedar", "sendAppToServer: "+app.getAppName());
        String path = getUrl()+"send";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data = "appname="+app.getAppName()+"&pkgname="+app.getPkgName()+"&timestamp="+app.getStartTimestamp();
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //connection.setRequestProperty("Content-Length", data.length()+"");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if(responseCode ==200){
                //请求成功
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder sb=new StringBuilder();
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }


                return sb.toString();
            }else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String sends(List<OneUse> oneUses) {
        String url_=getUrl();
            StringBuilder sb_appnames = new StringBuilder();
            StringBuilder sb_timestamps = new StringBuilder();
            String coma="";
            for (OneUse oneUse : oneUses) {
                sb_appnames.append(coma).append(oneUse.getAppName());
                sb_timestamps.append(coma).append(oneUse.getStartTimestamp());
                coma=",";
            }

            try {
                URL url = new URL(url_+"sends");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("POST");

                //数据准备
                String data = "appnames=" + sb_appnames.toString() +
                        "&timestamps=" + sb_timestamps.toString();
                //至少要设置的两个请求头
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //connection.setRequestProperty("Content-Length", data.length()+"");

                //post的方式提交实际上是留的方式提交给服务器
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data.getBytes());

                //获得结果码
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder sb=new StringBuilder();
                    String strRead = null;
                    while ((strRead = reader.readLine()) != null) {
                        sb.append(strRead);
                    }

                    return sb.toString();
                } else {
                    //请求失败
                    return "";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
    }


    public static String sends_all(List<OneUse> oneUses) {
        String url_=getUrl();
        StringBuilder sb_appnames = new StringBuilder();
        StringBuilder sb_timestamps = new StringBuilder();
        String coma="";
        for (OneUse oneUse : oneUses) {
            sb_appnames.append(coma).append(oneUse.getAppName());
            sb_timestamps.append(coma).append(oneUse.getStartTimestamp());
            coma=",";
        }

        try {
            URL url = new URL(url_+"sends_all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data = "appnames=" + sb_appnames.toString() +
                    "&timestamps=" + sb_timestamps.toString();
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //connection.setRequestProperty("Content-Length", data.length()+"");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder sb=new StringBuilder();
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }

                return sb.toString();
            } else {
                //请求失败
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
