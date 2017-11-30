package com.dml.myInfo;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dml.souqiu.R;
import com.dml.utils.UploadUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Personal extends Activity{
	private  LinearLayout rlayout;
	private LinearLayout llayout1;
	private LinearLayout llayout2;
	private LinearLayout llayout3;
	private  Button b1;//保存资料
	private Button b2;//退出登录
	private TextView t;//昵称
	private TextView tsex;//性别
	private TextView age;//年龄
	private ImageView iv;
    int CHOOSE_PICTURE=1;
    int TAKE_PICTURE=2;
    Bitmap bm;
    private String fileName;
    private String nickname;
    private String sex;
    private String birthtime;
    private String oldname;//������ݿ��޸�
    private ImageView backImg;
    Intent it;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal);//���Ի�����;
		it=getIntent();
		oldname=it.getStringExtra("name");
		rlayout=(LinearLayout)findViewById(R.id.rlayout1);
		llayout1=(LinearLayout)findViewById(R.id.rlayout2);
		llayout2=(LinearLayout)findViewById(R.id.rlayout3);
		llayout3=(LinearLayout)findViewById(R.id.rlayout4);
		backImg = (ImageView) findViewById(R.id.personal_back);
		iv=(ImageView)findViewById(R.id.imageview1);
		b1=(Button)findViewById(R.id.button1);
		b2=(Button)findViewById(R.id.button2);
		t=(TextView)findViewById(R.id.t);//�ǳ�
		tsex=(TextView)findViewById(R.id.sex);
		age=(TextView)findViewById(R.id.age); 	
		MyASyncTask yncTask = new MyASyncTask();
   	    yncTask.execute(oldname);//���name��ȡ�Ա����䣬sex����Ϣ
		t.setText(oldname);
		backImg.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
				
			});
	   //���ڸ��ͼƬ��
		 rlayout.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
        
	    Intent intent=	new Intent(Personal.this,SelectPicPopupWindow.class);	
		startActivityForResult(intent,0);//�����ж��Ǵ���ỹ��ͼ��  ,ע��0��������Ի������code
			} 
		 });
		 //���ڸ���ǳƣ�
		llayout1.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent  intent=new  Intent(Personal.this,nickname.class);
			intent.putExtra("nickname", t.getText().toString());//���û��ǳƸ��޸Ľ��棬��Ȼ������null
			startActivityForResult(intent, 1);// ע��1�������ǳƽ����code
			}
		 });
		//���ڸ���Ա�
       llayout2.setOnClickListener(new View.OnClickListener(){			
			public void onClick(View v) {
				// TODO Auto-generated method stub				
		Intent it=new Intent(Personal.this,sex.class);	
		startActivityForResult(it, 2);// ע��2�������Ա�����code
		
			}
		});
       //���ڸ�ĳ������ڣ� 
       llayout3.setOnClickListener(new View.OnClickListener() {
	     public void onClick(View v) {
		  Intent it =new Intent(Personal.this,date.class);
		  startActivityForResult(it,3);// ע��3��������������code
	
	     } 
       });
   b1.setOnClickListener(new View.OnClickListener() {

	public void onClick(View v) {
	sex=tsex.getText().toString();
		if(sex.equals("男"))
			sex="0";
		else if(sex.equals("女"))
			sex="1"; 
		else
			sex="2";//������
		nickname=t.getText().toString();
		birthtime=age.getText().toString();
	     new Thread(){
			@Override
		     public void run() { 
				try { 		
					URL url=new URL("http://202.114.18.96:8080/SouQiuWang/Save");
					HttpURLConnection conn=(HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");  
					conn.setDoInput(true); 
					conn.setDoOutput(true);
					DataOutputStream out=new DataOutputStream(conn.getOutputStream());
					String params="name="+URLEncoder.encode(oldname,"utf-8")+
							"&newname="+URLEncoder.encode(nickname,"utf-8")+
							"&sex="+URLEncoder.encode(sex,"utf-8")+
							"&birthtime="+URLEncoder.encode(birthtime,"utf-8");
					out.writeBytes(params); 
					out.flush();
					out.close(); 
					if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
						InputStreamReader in=new InputStreamReader(conn.getInputStream());
						BufferedReader br=new BufferedReader(in);
						String line="";
						while((line=br.readLine())!=null)
						System.out.println("chenggong:"+line);
						in.close();  
					}				
					conn.disconnect();
						} catch (Exception e) { 
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	   SharedPreferences.Editor spf =getSharedPreferences("USERPASS",InfoActivity.MODE_WORLD_READABLE).edit();  
	   spf.putString("Login_name", oldname);
	   spf.putInt("Login_state", 1);
	   spf.commit(); 
			}	 
	      }.start();
	    it.putExtra("result", 0);                           /////////kkkkkk
	  	Personal.this.setResult(1,it);
		Personal.this.finish();//ֱ���˵��������Ľ���
	    }
     }); 
   //�����˳���¼
   b2.setOnClickListener(new View.OnClickListener() {
	public void onClick(View v) {
		//ֵ==0
	SharedPreferences.Editor spf =getSharedPreferences("USERPASS",InfoActivity.MODE_WORLD_READABLE).edit();  
	spf.putInt("Login_state", 0);  
	spf.putString("Login_name", null);
	spf.commit();  
	it.putExtra("result", 1);                                    //kkkkkkkkkk
	Personal.this.setResult(1,it);
    Personal.this.finish();//�����˳�
	  }
     });
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		int flag=0;   //flag==1;�������,flag==2�������
		SharedPreferences sp = getSharedPreferences("USERPASS",InfoActivity.MODE_PRIVATE);     		
	    oldname=sp.getString("Login_name", "");	
	    fileName = "/sdcard/myImage/"+oldname+".jpg"; //��Ƭ����ĵ�ַ
		  if(resultCode==0&&requestCode==0){ 
			if(data!=null){	
				flag=data.getExtras().getInt("tag");
		         if(flag==1){
				    ContentResolver resolver = getContentResolver();
				    Uri uri=Uri.parse(data.getExtras().getString("user").toString());
				    FileOutputStream b=null;
				   try {
					  bm = MediaStore.Images.Media.getBitmap(resolver, uri);
					  iv.setImageBitmap(bm);
					  File file = new File("/sdcard/myImage/"); 
				      file.mkdirs();// �����ļ��� 
				      b = new FileOutputStream(fileName); 
				      bm.compress(Bitmap.CompressFormat.JPEG, 40, b);// �����ѹ����д���ļ�     
				      }
				   catch (FileNotFoundException e) {
					  e.printStackTrace();
				      }
				   catch (IOException e) {   
					      e.printStackTrace();
				        }
				   finally{
				        try {
				        	b.flush(); 
							b.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        }
			     new Thread(){
								@Override
					public void run() {
									// TODO Auto-generated method stub
				   try {
                     URL url=new URL("http://202.114.18.96:8080/SouQiuWang/upload");
					 UploadUtil u=new UploadUtil();
					 u.uploadFile(new File(fileName));	//�ϴ��ļ�											 
					}catch(Exception e){
				    	e.printStackTrace();
									}
								}																						
								
							}.start();
				    }
			      else if(flag==2){
			         bm=(Bitmap)data.getExtras().get("bundle");
			         iv.setImageBitmap(bm);
			         FileOutputStream b=null;
			         try {
					      b = new FileOutputStream(fileName); 
					      bm.compress(Bitmap.CompressFormat.JPEG, 40, b);// �����д���ļ�     
					      } catch (FileNotFoundException e) {
						  e.printStackTrace();
					      } catch (IOException e) {   
						      e.printStackTrace();
					        }finally{
					        try {
					        	b.flush(); 
								b.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        }
				    new Thread(){
						@Override
					public void run() {										
						try {
			            URL url=new URL("http://202.114.18.96:8080/SouQiuWang/upload");
					    UploadUtil u=new UploadUtil();
					    u.uploadFile(new File(fileName));												 
									
					   }catch(Exception e){
						   e.printStackTrace();
										}
									}																									
								}.start();
				   }
			      else{                    //�����ȡ��ť��
			    	  if(new File(fileName).exists()==false){	//�����ڸ��ļ���ֱ�Ӽ��أ������ھ��������	
				        	MyASyncTask2 my=new MyASyncTask2();
				        	my.execute("http://202.114.18.96:8888/images/PersonalImages/"+oldname+".jpg");
				        		  
			    	  }
			    	  else{
			    		  iv.setImageBitmap(BitmapFactory.decodeFile(fileName));  
			    	  }			    	  		   
			}
			}
			
		}
	 else if(resultCode==1&&requestCode==1){
				 if(data!=null){
					 String str=data.getExtras().getString("result");
					 if(str.equals("")==false){
					  t.setText(str);
	       
					 }
				 }
			 } 
	 else if(resultCode==2&&requestCode==2){
		 if(data!=null){
			 String str=data.getExtras().getString("sex");
			 tsex.setText(str);
			 
		 }
	 }
	 else if(resultCode==3&&requestCode==3){
		 if(data!=null){
			 String str=data.getExtras().getString("age");
			 age.setText(str);
			 
		 }
	 }		
	}
	public class MyASyncTask extends AsyncTask<String, Integer,  String> {
	      protected  String doInBackground(String... param) {
	    	  StringBuffer sb=new StringBuffer();
	    	  try{
	    	  URL url=new URL("http://202.114.18.96:8080/SouQiuWang/Info");
			  HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST");  
				conn.setDoInput(true); 
				conn.setDoOutput(true); 
				DataOutputStream out=new DataOutputStream(conn.getOutputStream());
				String params="name="+URLEncoder.encode(param[0],"utf-8");
				out.writeBytes(params); 
				out.flush(); 
				out.close(); 
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					InputStreamReader in=new InputStreamReader(conn.getInputStream());
					BufferedReader br=new BufferedReader(in);
					String line="";
					while((line=br.readLine())!=null){
					sb.append(line);
					} 
					in.close();  
				}
				
				conn.disconnect();
					} catch (Exception e) { 
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    	  
			return new String(sb);
	  	       
	         }
	      
	   protected void onPostExecute(String result){
	      //System.out.println("1111111111"+result);	  
			  try {
			   JSONObject jo=new JSONObject(result);
			   String sex=jo.getString("sex");
			   String time=jo.getString("birthtime");
			   String nickName=jo.getString("nickname");
			   if(sex.equals("0")){
				   tsex.setText("男");
			   }
			   else if(sex.equals("1")){// 
				   tsex.setText("女");
			   }else{
				   tsex.setText("保密");
			   } 
				 age.setText(time);	
				 t.setText(nickName);
			    SharedPreferences sp = getSharedPreferences("USERPASS",InfoActivity.MODE_PRIVATE);     		
			    oldname=sp.getString("Login_name", "");	
		        fileName = "/sdcard/myImage/"+oldname+".jpg"; //��Ƭ����ĵ�ַ  		     
		        if(new File(fileName).exists()==false){		
		        	MyASyncTask2 my=new MyASyncTask2();
		        	my.execute("http://202.114.18.96:8888/images/PersonalImages/"+oldname+".jpg");
	
		        }   
		        else
				    iv.setImageBitmap(BitmapFactory.decodeFile(fileName));		              			  
			  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			  }
			 
			  
	         }	
	     } 
	private Bitmap getImage(String path) {
		// TODO Auto-generated method stub
	       Bitmap bm =null;
		try {
			URL aURL = new URL(path);
			URLConnection con = aURL.openConnection();
			con.connect();
			InputStream is = con.getInputStream();
			/* ������������һ�����õı��ϰ��. */
			BufferedInputStream bis = new BufferedInputStream(is);
			/* ���������ϵ�ͼƬ */
	        bm = BitmapFactory.decodeStream(bis);		
			is.close();
			/* ��ʱͼƬ�Ѿ������ص�ImageView��. */
			} catch (IOException e) {
			e.printStackTrace();
			} 
			return bm;
	          }
	public class MyASyncTask2 extends AsyncTask<String, Integer,  Bitmap> {
	      protected  Bitmap doInBackground(String... param) {
	    	  Bitmap bitmap=getImage(param[0]);
			return   bitmap;
	  	       
	         }      
	   protected void onPostExecute(Bitmap result){
	      if(result!=null){//����̨��ݿ���ڸ�ͼƬ��ַ
	    	  iv.setImageBitmap(result);	
	        }
	       else{//����̨��ݿⲻ��Ļ�
	    	  iv.setImageResource(R.drawable.ic_launcher);
	      }
			
	   }				  
	}	
	     } 







