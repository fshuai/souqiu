package com.dml.myInfo;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.Header;
import com.dml.souqiu.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
public class SelectPicPopupWindow extends Activity {  
	Intent  it;
	private Bitmap bm;
    private Button btn_take_photo, btn_pick_photo, btn_cancel;  
    private LinearLayout layout;   
    FileOutputStream b = null; 
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.alert_dialog); 
        it=getIntent();
        btn_take_photo = (Button) findViewById(R.id.btn_take_photo);  
        btn_pick_photo = (Button) findViewById(R.id.btn_pick_photo);  
        btn_cancel = (Button) findViewById(R.id.btn_cancel);    
        layout=(LinearLayout)findViewById(R.id.pop_layout);        
        layout.setOnClickListener(new OnClickListener() {         
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�",   
                        Toast.LENGTH_SHORT).show();   
            }  
        });  
        //��Ӱ�ť����  
        btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			setResult(0, it);
			it.putExtra("tag", 3);
			finish();
			}
		});  
        btn_pick_photo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				startActivityForResult(openAlbumIntent, 1);
			}
		});  
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub		
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		    startActivityForResult(intent, 2);											
			}
		}); 
      
    }   
    //ʵ��onTouchEvent������������Ļʱ��ٱ�Activity  
    @Override  
    public boolean onTouchEvent(MotionEvent event){ 
    	it.putExtra("tag", 3);                                ///kkkkkkkkkkk
    	setResult(0, it);
        finish();  
        return true;  
    }  
   
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {	
			case 1://flag==1;�������,flag==2�������
				ContentResolver resolver = getContentResolver();
				//��Ƭ��ԭʼ��Դ��ַ
				Uri originalUri = data.getData(); 
	             try {
	            	//ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
					bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					it.putExtra("user", originalUri.toString());
					it.putExtra("tag", 1);
	             }catch (Exception e) {
						e.printStackTrace();
					}
	            
				break;
			 case 2:  	       		       
		           bm = (Bitmap) data.getExtras().get("data");// ��ȡ���ص���ݣ���ת��ΪBitmapͼƬ��ʽ 
		           it.putExtra("bundle", bm);	         	         
		           it.putExtra("tag", 2);
		        		        		                                
	           break;
			  default:
				  it.putExtra("user", "");
		          it.putExtra("tag", 0);             
				break;
			}
			setResult(0, it);
			
		}
		else{
			 it.putExtra("tag", 3);    
			  setResult(0,it);
		}
		finish();//��������
	} 
}  
