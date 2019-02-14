package com.warn.service.impl;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.warn.dao.PatrolDao;
import com.warn.dto.Urgency;
import com.warn.entity.Cookie;
import com.warn.entity.Patrol;
import com.warn.service.PatrolService;
import com.warn.util.common.Const;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.directwebremoting.json.types.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
@Service
public class PatrolServiceImpl implements PatrolService {

    @Autowired
    PatrolDao patrolDao;

    @Transactional
    public void addPatrolRecords() throws IOException{
        URL url = new URL("http://www.5ixun.com/exun/checkpointLog/query");
        String errorStr = "";
        String status = "";
        String response = "";
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer buffer = new StringBuffer();
        Cookie cookie = patrolDao.getCookie();
        try{
            URLConnection conn = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
            httpUrlConnection.addRequestProperty("Cookie","from=" + cookie.getFrom());
            httpUrlConnection.addRequestProperty("Cookie","secret=" + cookie.getSecret());
            httpUrlConnection.addRequestProperty("Cookie","smuser=" + cookie.getSmuser());
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            out = new PrintWriter(httpUrlConnection.getOutputStream());
            // 发送请求参数
            // flush输出流的缓冲
            out.flush();
            httpUrlConnection.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            status = new Integer(httpUrlConnection.getResponseCode()).toString();
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            errorStr = e.getMessage();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) { out.close();}
                if (in != null) {in.close();}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        JSONObject object = JSONObject.fromObject(response);
        JSONArray json = object.getJSONArray("rows");
        Patrol patrol1 = patrolDao.getLatestRecord();
        int key = 1;
        if(patrol1 == null)
        for(int i=0;i<json.size();i++){
            Patrol patrol = new Patrol();
            patrol.setWorker(json.getJSONObject(i).getString("patrolmanId"));
            patrol.setTime(Const.longToDate(new Long(json.getJSONObject(i).getString("createTime"))));
            JSONObject jsonObject =(JSONObject) json.getJSONObject(i).get("checkpoint");
            patrol.setPoint(jsonObject.get("name").toString());
            patrol.setCardNo(jsonObject.get("card").toString());
            JSONObject jsonObject1 = (JSONObject) json.getJSONObject(i).get("device");
            patrol.setMachineName(jsonObject1.getString("name"));
            patrol.setMachineNo(jsonObject1.getString("code"));
            patrolDao.addPatrolRecords(patrol);
        }
        else
            for(int i=0;i<json.size();i++){
                Patrol patrol = new Patrol();
                String time = Const.longToDate(new Long(json.getJSONObject(i).getString("createTime")));
                if(patrol1.getTime().equals(time))
                    key = 0;
                if(key == 0)
                {
                    patrol.setWorker(json.getJSONObject(i).getString("patrolmanId"));
                    patrol.setTime(Const.longToDate(new Long(json.getJSONObject(i).getString("createTime"))));
                    JSONObject jsonObject = (JSONObject) json.getJSONObject(i).get("checkpoint");
                    patrol.setPoint(jsonObject.get("name").toString());
                    patrol.setCardNo(jsonObject.get("card").toString());
                    JSONObject jsonObject1 = (JSONObject) json.getJSONObject(i).get("device");
                    patrol.setMachineName(jsonObject1.getString("name"));
                    patrol.setMachineNo(jsonObject1.getString("code"));
                    patrolDao.addPatrolRecords(patrol);
                }
            }


    }
    }

