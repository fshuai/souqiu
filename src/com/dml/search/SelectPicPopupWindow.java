package com.dml.search;

import java.io.File;
import java.util.Calendar;
import com.dml.souqiu.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectPicPopupWindow extends Activity implements OnClickListener{
	private Button btn_take_photo;
	private Button btn_pick_photo;
	private Button btn_cancel;
	private LinearLayout layout;
	
	//���ݵĲ���
    private int query1 = 0;   
	private int query2 = 0;  
	private int query3 = 0;   
	private static final int TAKE_PHOTO = 1;
	private static final int PICK_PHOTO = 2;
	private static final String IMAGE_TYPE = "image/*";
	private String picturePath;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		init();
	}
	
	protected void init(){
		Intent intent = this.getIntent();
	    query1 = intent.getIntExtra("query1", 0);   //������
	    query2 = intent.getIntExtra("query2", 0);   //����ʽ
	    query3 = intent.getIntExtra("query3", 0);   //ʱ�䳤��
	    
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
		layout=(LinearLayout)findViewById(R.id.pop_layout);
		
		//���ѡ�񴰿ڷ�Χ�����������Ȼ�ȡ���㣬������ִ��onTouchEvent()��������������ط�ʱִ��onTouchEvent()��������Activity
		layout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�", Toast.LENGTH_SHORT).show();	
					}
				});
		
		//��Ӱ�ť����
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}

	//ʵ��onTouchEvent���������������Ļʱ���ٱ�Activity
		@Override
		public boolean onTouchEvent(MotionEvent event){
			finish();
			return true;
		}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_take_photo:          //�����µ���Ƭ����
			Calendar ca = Calendar.getInstance(); 
            int year = ca.get(Calendar.YEAR);//��ȡ��� 
            int month=ca.get(Calendar.MONTH);//��ȡ�·� 
            int day=ca.get(Calendar.DATE);//��ȡ�� 
            int minute=ca.get(Calendar.MINUTE);//�� 
            int hour=ca.get(Calendar.HOUR);//Сʱ 
            int second=ca.get(Calendar.SECOND);//�� 
            String fileName=String.valueOf(year)+String.valueOf(month)+String.valueOf(day)+String.valueOf(hour)+String.valueOf(minute)+String.valueOf(second);
            picturePath = Environment.getExternalStorageDirectory() + "/" + fileName + ".jpg";
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment .getExternalStorageDirectory(),fileName+".jpg"))); 
            startActivityForResult(intent, TAKE_PHOTO);  
            break;
		case R.id.btn_pick_photo:
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT); 		
            getAlbum.setType(IMAGE_TYPE); 
            startActivityForResult(getAlbum,PICK_PHOTO); 
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		 if(resultCode != RESULT_OK) {   
			 return ;
		 }
		 if(requestCode == TAKE_PHOTO){
			
		 }
		 if(requestCode == PICK_PHOTO){
				try{
					Uri selectedImage = data.getData();   
				    String[] filePathColumn = { MediaStore.Images.Media.DATA };   
				    Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);   
				    cursor.moveToFirst();   
				    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);   
				    picturePath = cursor.getString(columnIndex);   
				    cursor.close();     
				    }catch(Exception e){
						e.printStackTrace();
					}
		 }
		    Intent intent = new Intent();
		    intent.putExtra("path", picturePath);
		    intent.putExtra("mode",2);
		    intent.putExtra("query1", query1);
			intent.putExtra("query2", query2);
			intent.putExtra("query3", query3);
		    intent.setClass(SelectPicPopupWindow.this, SearchResultActivity.class);
		  //  Toast.makeText(getApplicationContext(), query1 + " " + query2 + " " + query3, Toast.LENGTH_LONG).show();
		    startActivity(intent);
		    finish();
		// super.onActivityResult(requestCode, resultCode, data);
	}

}
