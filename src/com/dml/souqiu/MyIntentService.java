package com.dml.souqiu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dml.utils.FileUtils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

public class MyIntentService extends IntentService{

	public MyIntentService() {
		super("MyIntentService");
	
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		getTheLastedViedeoInfo();
	}
	
	//最新视频信息缓存
   public void getTheLastedViedeoInfo(){
	 String  path =  "http://202.114.18.96:8080/SouQiuWang/homePage";
	 String result="";
	  
	try {
	     URL url=new URL(path);
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		 conn.setRequestMethod("GET");
		 InputStreamReader in=new InputStreamReader(conn.getInputStream());
		 BufferedReader buff=new BufferedReader(in);
		 String line="";
		 while((line=buff.readLine())!=null){
			 result+=line+"\n";
			 
		 }	
//		 System.out.print(result);
	     in.close();
		 conn.disconnect();
	     writeFile(result);// //写入到文件中
		 
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(Exception e1){ 
		e1.printStackTrace();
	}
	   
   }
   //最欢迎的视频
   public void getTheMostPopularVideoInfo(){
	   
	   
	   
   }
   //猜你喜欢的视频消息
   public  String get_YourLiking_VideoInfo(){
	return null;
	   
   }
   //写入到文件中
  public void writeFile(String result) throws Exception{
	  String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	  mSdRootPath+="/SouQiu";
	  File files=new File(mSdRootPath);
	  if(files.exists()==false)
		  files.mkdirs();
	   File file =new File(mSdRootPath+"/latestVideoInfo.txt");
		 if(file.exists()==false)
			 file.createNewFile();
		
        FileOutputStream output=new FileOutputStream(file);
		
		 output.write(result.getBytes());
		 output.flush();
		 output.close();
	  
	  
  } 
   
}
