package com.dml.myInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dml.souqiu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Register extends Activity{  //注册的时候要交设备Id
	private  Button  Register;
	private Button   reset;
	private EditText  Name;
	private EditText  Password;
	private EditText  Confirm;
	private EditText  Email;
	private ImageView backImg;
	RadioGroup group;
	String name="";
	String password="";
	String Rpassword="";  
	String  sex="0";//0����У�1���Ů
	String email="";
	Intent it;
	String deviceid=null;//增加了一个设备id，用于将用户名和设备名匹配
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		deviceid = tm.getDeviceId();
		it=getIntent();
		reset=(Button)findViewById(R.id.button1);
		 Register=(Button)findViewById(R.id.button2); 
		 Name=(EditText)findViewById(R.id.name);
		 Password=(EditText)findViewById(R.id.password);
		 Confirm=(EditText)findViewById(R.id.confirm);
		 Email=(EditText)findViewById(R.id.email);
		 group=(RadioGroup)findViewById(R.id.radiogroup);
		 backImg = (ImageView) findViewById(R.id.register_back);
		 group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.radio1)
					sex="0";
				else
					sex="1";
			}
		});
		 reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Name.setText("");
				Password.setText("");
				Confirm.setText("");
				Email.setText("");
			}
		});
		 
	   backImg.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
				
			});
			
		Register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 name=Name.getText().toString();
				 password=Password.getText().toString();
				 email=Email.getText().toString();
				 Rpassword=Confirm.getText().toString();
				int state= isInvalid(password,Rpassword,email);
				if(state==4){
				Toast.makeText(Register.this, "密码长度不能少于6！", Toast.LENGTH_SHORT).show();
				Password.setText("");
				Confirm.setText("");
				Password.requestFocus();//���»�ý���
				
				}
				else if(state==3){
					Toast.makeText(Register.this, "密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
					Password.setText("");
					Confirm.setText("");
					Password.requestFocus();//���»�ý���
				}
				else if(state==2){
			Toast.makeText(Register.this, "请输入正确的邮箱！", Toast.LENGTH_SHORT).show();
					Email.setText("");
					Email.requestFocus();
				}
				else{
					
				new Thread(){
					public void run() {  
						try { 		
					URL url=new URL("http://202.114.18.96:8080/SouQiuNet/RegisterServlet");
					HttpURLConnection conn=(HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST"); 
					conn.setDoInput(true); 
					conn.setDoOutput(true);
					DataOutputStream out=new DataOutputStream(conn.getOutputStream());
					String params="name="+URLEncoder.encode(name,"utf-8")+
							"&password="+URLEncoder.encode(password,"utf-8")+
							"&sex="+URLEncoder.encode(sex,"utf-8")+
							"&email="+URLEncoder.encode(email,"utf-8")
							+"&deviceid="+URLEncoder.encode(deviceid,"utf-8");
					out.writeBytes(params); 
					out.flush();
					out.close(); 
					if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
						InputStreamReader in=new InputStreamReader(conn.getInputStream());
						BufferedReader br=new BufferedReader(in);
						String line=br.readLine();
						System.out.println("chenggong:"+line);
						in.close();  
					}
					
					conn.disconnect();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
					
				}.start();
			 SharedPreferences.Editor spf =getSharedPreferences("USERPASS",InfoActivity.MODE_WORLD_READABLE).edit();  
			 spf.putInt("Login_state", 1);  // ��½�ɹ�ʱ��Ҫ�޸������ļ�������ֵ==0
			 spf.putString("Login_name", name);
			 spf.commit();		
			 it.putExtra("val", 1);//����Ķ���ȷ��
			 Register.this.setResult(4, it);
		     Register.this.finish();
			}
			}
		});
	}
	
	
	protected int isInvalid(String password1, String password2, String email) {
		// TODO Auto-generated method stub
		if(password1.length()<6)
			return 4;
		if(password1.equals(password2)==false)
			return 3;//�������벻һ�£�
		
		if (isEmail(email)==false)
				return  2;//���䲻��ȷ��
		
			return 1;//����Ч
		
	}
	//�ж������Ƿ�Ϸ�
	public static boolean isEmail(String email) {
		if (null==email || "".equals(email)) return false;	
	    Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//����ƥ��  
	    Matcher m = p.matcher(email);  
		return m.matches();
	}
}
