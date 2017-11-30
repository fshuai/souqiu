package com.dml.tvPlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BasketURL_Transfer {

	/** 
	 * @param args
	 */
	 static String title;
     static String imgUrl;
	 static String result;
     static String tem;
     static String []str; 
     static String [][]temp;
	 static String baseURL="http://202.114.18.77:8882/GetRealAdd_Android.aspx?videoid=";//根据videoid来得到真实视频地址

    public BasketURL_Transfer(){	   
    }
  //根据videoid来得到真实视频地址

	public static String getVideoUrl(String videoid) {
		// TODO Auto-generated method stub
		 try {	
			result="";
			 HttpURLConnection con=null;
			URL url=new URL(baseURL+videoid);
		     con=(HttpURLConnection)url.openConnection();
			InputStreamReader in=new InputStreamReader(con.getInputStream(),"utf-8") ;
			BufferedReader buf=new BufferedReader(in);
			while((tem=buf.readLine())!=null){
				result+=tem+"\n";		
			}
			in.close();
			con.disconnect();	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();	
	}
	
  return result.substring(0,result.indexOf(")"));  
	}
}
