package com.dml.myInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.dml.souqiu.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends Activity{
private Button login;
private Button register;
private EditText Name;
private EditText  Password;
private ImageView backImg;
String name="";
String password="";
int status=-1;
Intent it;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login=(Button)findViewById(R.id.click_To_LogIn);
		register=(Button)findViewById(R.id.click_To_Register);
		Name=(EditText)findViewById(R.id.name);
		Password=(EditText)findViewById(R.id.password);
		backImg = (ImageView) findViewById(R.id.login_back);
		login.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				it=getIntent();
				name=Name.getText().toString();
				password=Password.getText().toString();
			    MyASyncTask yncTask = new MyASyncTask();
		        yncTask.execute(name,password);						
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    it=getIntent();//注意获取intent的值
			    Intent intent =new Intent(Login.this,Register.class);
				startActivityForResult(intent, 4);
			}
		});
		
		backImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==4&&resultCode==4){
			if(data!=null){
				int val=data.getExtras().getInt("val");
				if(val==1){//只有为1的时候才将改变management界面
					it.putExtra("status", 1); 
				    Login.this.setResult(0, it);
				    Login.this.finish();
				}
			}
		}
	}
 public class MyASyncTask extends AsyncTask<String, Integer, Integer> {
  	      protected Integer doInBackground(String... param) {
  	    	if(param[0].equals("")||param[1].equals("")){
  	  		return 0;
  	  	 }
  	    	try {
  	    		System.out.println("name:"+name+" password:"+password);
				URL url=new URL("http://202.114.18.96:8080/SouQiuNet/LoginServlet");
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST"); 
				conn.setDoInput(true); 
				conn.setDoOutput(true);
				DataOutputStream out=new DataOutputStream(conn.getOutputStream());
				String params="name="+URLEncoder.encode(name,"utf-8")+
						"&password="+URLEncoder.encode(password,"utf-8");
				out.writeBytes(params); 
				out.flush();
				out.close();   
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					InputStreamReader in=new InputStreamReader(conn.getInputStream()); 
					BufferedReader br=new BufferedReader(in);
					String line=br.readLine();
					status=Integer.valueOf(line);
					System.out.println("chenggong:"+status); 
					in.close();  
				}	 
				conn.disconnect();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			return status;			
  	         }
  	      /*在 onPostExecute(Result) 中是请求获得结果后更新UI部分。你会看到他的参数就是我们类中的类型参数。代码如下*/
  	      protected void onPostExecute(Integer Result){
  	             status=Result;
  	           if(status==0){//即name和password中有一个为null
					Toast toast= Toast.makeText(Login.this, "请把内容输入完整", 2000);
					 toast.setGravity(Gravity.CENTER,0,0);
					 toast.show(); 
				 }
				else if(status==1){// 
				SharedPreferences.Editor spf =getSharedPreferences("USERPASS",InfoActivity.MODE_WORLD_READABLE).edit();  
				spf.putInt("Login_state", 1);  //登陆成功时，要修改配置文件，让其值==0
				spf.putString("Login_name", name);
					 spf.commit();		
					it.putExtra("status", status);
					Login.this.setResult(0, it);
					Login.this.finish();
					}	
				else if(status==2){
					Password.setText("");
					Toast.makeText(Login.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
				}
				else if(status==3){
					Toast.makeText(Login.this, "您还未注册，请重新注册", Toast.LENGTH_SHORT).show();
					Password.setText("");
					Name.setText("");
					Intent intent =new Intent(Login.this,Register.class);
					startActivityForResult(intent, 4);//修改后的结果
				}
				
  	         }	
  	     }
	
	

}
