package com.dml.tvPlay;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class videoID { 
	String videoID;
	ArrayList<String> imageTapeList;
    String path = "";
	static JSONObject jTape_Preview;   
	static JSONArray ja;
	static JSONArray jb; 
	String result="";
	static ArrayList<String>list1;
 static String []arr_tape;//用于保存精彩镜头的图片地址
    	String []arr_starttime;//用于保存精彩镜头的起始时间
	 String []arr_totaltime;//用于保存精彩镜头的总时间
	 String[]arr_preview;//保存视频预览的图片地址
	 String[]arr_Pstarttime;//保存视频预览中每个镜头的起始时间
   String[]arr_Pduration;//保存视频预览中每个视频的持续时间
   
   private String videotag;
  public videoID(String videoID, String videotag){
	  this.videoID=videoID;
	  imageTapeList=new ArrayList<String>();
	  list1=new ArrayList<String>();
	  this.videotag = videotag;
	  switch(Integer.parseInt(videotag)){
	  case 1:
		 path =  "http://202.114.18.96:8080/SouQiuNet/FootballTapeServlet";
		 break;
	  case 2:  
		 path="http://202.114.18.96:8080/SouQiuWang/BasketTapeServlet";
		 break;
	  case 3: 
		 path="http://202.114.18.96:8080/SouQiuWang/TennisTapeServlet"; 
		 break;
	  case 4:
		 path="http://202.114.18.96:8080/SouQiuWang/TableTennisTapeServlet";
		 break;
	  case 5:
		 path="http://202.114.18.96:8080/SouQiuWang/BadmintainTapeServlet"; 
		 break;
	  case 6:
		 path="http://202.114.18.96:8080/SouQiuWang/videoTapeSevlet";
		 break;
	  case 7:
		 path="http://202.114.18.96:8080/SouQiuNet/SnookerTapeServlet"; 
		 break;
	 default:
		 break;
	  }
  }
  public  void getImageList(){
	 
	  try {
		    result="";//注意每次清空
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			 conn.setDoOutput(true);
			 conn.setDoInput(true);
			 conn.setRequestMethod("POST");
			 OutputStream out=conn.getOutputStream();
			 DataOutputStream obj=new DataOutputStream(out);
			 obj.writeUTF(videoID);
			 //obj.writeInt(Integer.parseInt(videoID.trim()));
			 obj.flush();
			 obj.close();
			 InputStreamReader in=new InputStreamReader(conn.getInputStream());
			 BufferedReader buff=new BufferedReader(in);
			 String line="";
			 while((line=buff.readLine())!=null){
				 result+=line+"\n";
			 }
			
		     in.close();
			 conn.disconnect();
			 jTape_Preview=new JSONObject(result);
			 ja=new JSONArray(jTape_Preview.getString("videoTape"));
			 jb=new JSONArray(jTape_Preview.getString("videoPreview"));
			 System.out.println("ja:"+ja.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e1){ 
			e1.printStackTrace();
		}
	    arr_tape=new String[ja.length()];
		arr_starttime=new String[ja.length()];
		arr_totaltime=new String[ja.length()];
		arr_preview=new String[jb.length()];
		arr_Pstarttime=new String[jb.length()];
		arr_Pduration=new String[jb.length()];
		for(int i=0;i<jb.length();i++){
			JSONObject jp=jb.optJSONObject(i);
			try {
				String s1=jp.getString("framefile");
				String s2=jp.getString("BegTime");
				String s3=jp.getString("duration");
				arr_preview[i]=s1;
				arr_Pstarttime[i]=s2;
				arr_Pduration[i]=s3;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		for(int i=0;i<ja.length();i++){
			try {
				JSONObject jo=ja.getJSONObject(i);
				String str1=jo.getString("picaddress");
				String str2=jo.getString("starttime");
				String str3=jo.getString("totaltime");
				arr_tape[i]=str1;
				arr_starttime[i]=str2;
				arr_totaltime[i]=str3;
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		  
		
  }
  public  String[] getArr_tape(){
	  return arr_tape;
	  
  }
  public String[] getArr_starttime(){
	  return arr_starttime;
	  
  }
  public  String[] getArr_totaltime(){
	  return arr_totaltime; 
	  
  }
  public  String[] getArr_preview(){
	  return arr_preview;
	  
  }
  public  String[] getArr_Pstarttime(){
	  return arr_Pstarttime;
	  
  }
  public  String[] getArr_Pduration(){
	  return arr_Pduration;
	  
  }
  
  public static void main(String[] args){
	  videoID v=new videoID("A-00005134","7");
	  v.getImageList();
	  System.out.println(Arrays.toString(v.getArr_tape()));
  }
}

