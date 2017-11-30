package com.dml.myInfo;

import com.dml.souqiu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;;

public class nickname extends Activity {
private ImageView  back;
private TextView   save;
private EditText  nickname;
Intent it;
String temp=""; 
private String result="";
/**
 * ע��˼·�ǽ���ԭ����nickname�������µ�nickname����
 * 
 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nickename);
		it=getIntent();
		temp=it.getExtras().getString("nickname");
		back=(ImageView)findViewById(R.id.nickname_back);
	    save=(TextView)findViewById(R.id.save);
	    nickname=(EditText)findViewById(R.id.nickname);
	    nickname.setText(temp);
		if(nickname.requestFocus()==false);
		nickname.setFocusable(true);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				it.putExtra("result", "");
				nickname.this.setResult(1, it);
				nickname.this.finish();
			}
		});
        save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result=nickname.getText().toString();
				it.putExtra("result", result);
				nickname.this.setResult(1, it);
				nickname.this.finish();
			}
		});
	}
	

}
