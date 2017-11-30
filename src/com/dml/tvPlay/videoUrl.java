package com.dml.tvPlay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class videoUrl {
	String result="";
    String path="http://202.114.18.96:8080/SouQiuNet/VideoUrlServlet";
	public videoUrl() {
		// TODO Auto-generated constructor stub
	}
	public  String  getUrl(String  address){ 
		try{	
			result="";
		 URL url=new URL(path);
		 HttpURLConnection conn =(HttpURLConnection)url.openConnection();
		 conn.setRequestMethod("POST");
		 conn.setDoOutput(true);
		 conn.setDoInput(true); 
		 DataOutputStream out =new  DataOutputStream(conn.getOutputStream());
		 out.writeBytes(address);
		 out.flush(); 
		 out.close();
		 InputStreamReader in=new InputStreamReader(conn.getInputStream());
		 BufferedReader buff=new BufferedReader(in);
		 String line="";
		 while((line=buff.readLine())!=null){
			 result+=line+"\n";
		 }
	        in.close();
		 conn.disconnect();
	         }
	 catch(Exception e){
	        	 e.printStackTrace();
	        	 
	         }
		//result=result.substring(result.lastIndexOf("{"), result.length());
		return result;
			 
	}
}

