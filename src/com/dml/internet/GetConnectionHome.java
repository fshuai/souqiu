package com.dml.internet;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dml.utils.FileUtils;

public class GetConnectionHome {
    
    public static String[][] getCon(int videotype){
    	String url = null;
    	String[] result = null;
    	String[][] tem = null;
    	String str = null;
    	switch(videotype){
    	case 1:
    		url = "http://202.114.18.96:8080/SouQiuNet/FootBallServlet";
    		break;
    	case 2:
    		url = "http://202.114.18.96:8080/SouQiuWang/BasketServlet";
    		break;
    	case 3:
    		url = "http://202.114.18.96:8080/SouQiuWang/TennisServlet";
    		break;
    	case 4:
    		url = "http://202.114.18.96:8080/SouQiuWang/TableTennisServlet";
    		break;
    	case 5:
    		url = "http://202.114.18.96:8080/SouQiuWang/BadmintainServlet";
    		break;
    	case 6:
    		url = "http://202.114.18.96:8080/SouQiuWang/JsonSevlet";
    		break;
    	case 7:
    		url = "http://202.114.18.96:8080/SouQiuNet/Snooker_Servlet";
    		break;
    	default:
    		break; 
    	}
    	HttpGet httpGet = new HttpGet(url); 
    	HttpClient httpClient  = new DefaultHttpClient();
    	try {
    		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
               str = EntityUtils.toString(httpResponse.getEntity()); 
             if(str != null)  
                   new FileUtils().save(videotype, str);
              
			}else
       	   str = new FileUtils().read(videotype);
			
	} catch (Exception e) {
		System.out.println("Exception");
			
		}
    	System.out.println("GetConnectionHome:"+str);
        try {
     //采用这种方式的好处是原来的是根据特殊字符，但是在html中< >这样的特殊字符检测不出来。所以还是用json格式比较好   	
        	if(str!=null){       		
			  JSONArray ja=new JSONArray(str);
			  tem=new String[4][5];
			  for(int i=0;i<4;i++){
				  JSONObject jo=ja.getJSONObject(i);
				  tem[i][0]=jo.getString("videoid");
				  tem[i][1]=jo.getString("imageurl");
				  tem[i][2]=jo.getString("videotitle");
				  tem[i][3]=jo.getString("videourl");
				  tem[i][4]=jo.getString("videoorigin");
			  }
           	}
			
		     } catch (JSONException e) { 
			     
		    	  e.printStackTrace();
		   }
    	return tem;
    }
}
