package com.dml.myInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyComment {
	String myname;
	 int start;
	 JSONObject jo;
	 JSONObject j1;
	 JSONArray  ja;
	 String path="http://202.114.18.96:8080/SouQiuNet/MyCommentServlet";
	 StringBuffer result;
	 int m_total;
	 String []m_time;
	 String []m_content;
	 String []m_videotitle;
		public MyComment(String myname, int startposition) {
			// TODO Auto-generated constructor stub
	     this.myname=myname;
	     this.start=startposition;
	    
		}
	    public JSONObject getMyCommentMessage(){
	    	 try {
				    result=new StringBuffer();//һ��Ҫע��ÿ�����
				    URL url=new URL(path);
					HttpURLConnection conn=(HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST"); 
					conn.setDoInput(true); 
					conn.setDoOutput(true);
					DataOutputStream out=new DataOutputStream(conn.getOutputStream());
					String params="myname="+URLEncoder.encode(myname,"utf-8")+						
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
			    	 m_total=j1.getInt("total");
					 ja=jo.getJSONArray("com_message");
					 m_time=new String[ja.length()];
					 m_videotitle=new String[ja.length()];
					 m_content=new String[ja.length()];
					for(int i=0;i<ja.length();i++){
						JSONObject jo=ja.getJSONObject(i);
						m_videotitle[i]=jo.getString("com_videotitle");
						m_time[i]=jo.getString("com_time");
						m_content[i]=jo.getString("com_content");
						
					} 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					return null;
				}
				return jo;
					 
					
				}
	    public static void main(String[]args){
	    	MyComment m=new MyComment("kwb", 0);
	      JSONObject  jj=	m.getMyCommentMessage();
	      System.out.println(jj.toString());
	    }
	}
