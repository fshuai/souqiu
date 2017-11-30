package com.dml.tvPlay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoComment {
	  String videoid="";
	   String videotag="";
	   int start;
	  int total;
	   String path="http://202.114.18.96:8080/SouQiuWang/getComment";   
	   static JSONArray ja;
	   static JSONObject jo;
	   static JSONObject  j1;
	   StringBuffer result=null;
	  static String []com_name;//���ڱ����Ա��
	  static String []com_time;//���ڱ������۵�ʱ��
	  static String []com_content;//���ڱ�����������
		
		
	 public VideoComment(String videoid,String videotag,int start){
		  this.videoid=videoid;
		  this.videotag=videotag;
		  this.start=start;
	  }
	  public  void getCommentMessage(){
		 
		  try {
			    result=new StringBuffer();//һ��Ҫע��ÿ�����
			    URL url=new URL(path);
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST"); 
				conn.setDoInput(true); 
				conn.setDoOutput(true);
				DataOutputStream out=new DataOutputStream(conn.getOutputStream());
				String params="videoid="+URLEncoder.encode(videoid,"utf-8")+
						"&videotag="+URLEncoder.encode(videotag,"utf-8")+
							"&start="+URLEncoder.encode(""+start,"utf-8");
				out.writeBytes(params); 
				out.flush();
				out.close();  
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					InputStreamReader in=new InputStreamReader(conn.getInputStream(),"utf-8"); 
					BufferedReader br=new BufferedReader(in);
					String line="";
					while((line=br.readLine())!=null){
						result.append(line+"\n");
					}
		
					in.close();  
				}	 
				   conn.disconnect();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
		       try {
		    	 jo=new JSONObject(result.toString());
		    	 result=null;
		    	 j1=jo.getJSONObject("com_total");
		    	 total=j1.getInt("total");
				ja=jo.getJSONArray("com_message");
				com_name=new String[ja.length()];
				com_content=new String[ja.length()];
				com_time=new String[ja.length()];
				for(int i=0;i<ja.length();i++){
					JSONObject jo=ja.getJSONObject(i);
					com_name[i]=jo.getString("com_name");
					com_time[i]=jo.getString("com_time");
					com_content[i]=jo.getString("com_content");
					
				} 
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
				
			}
	  
			  
			
	  }
	 

