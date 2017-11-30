package com.dml.myInfo;

import com.dml.souqiu.R;
import com.dml.utils.FileUtils;
import com.dml.utils.MyDataBaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener{

	private TextView delete1;
	private TextView delete2;
	private TextView delete3;
	private ImageView back;
	private MyDataBaseHelper dbhelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_activity);
		
		dbhelper=new MyDataBaseHelper(this, "play.db",  1);
		delete1 = (TextView) findViewById(R.id.delete1);
		delete1.setOnClickListener(this);
		delete2 = (TextView) findViewById(R.id.delete2);
		delete2.setOnClickListener(this);
		delete3 = (TextView) findViewById(R.id.delete3);
		delete3.setOnClickListener(this);
		back = (ImageView) findViewById(R.id.setting_back);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.delete1:
			dbhelper.getWritableDatabase().delete("videoplay", null, null);
			Toast.makeText(getApplicationContext(), "已清除播放记录", Toast.LENGTH_SHORT).show();
			break;
		case R.id.delete2:
			dbhelper.getWritableDatabase().delete("videostore", null, null);
			Toast.makeText(getApplicationContext(), "已清除我的收藏", Toast.LENGTH_SHORT).show();
			break;
		case R.id.delete3:
			new FileUtils().deleteFile();
			Toast.makeText(getApplicationContext(), "已清除图片缓存", Toast.LENGTH_SHORT).show();
			break;
		case R.id.setting_back:
			finish();
			break;
	    default:
		   break;
		}
	}
}
