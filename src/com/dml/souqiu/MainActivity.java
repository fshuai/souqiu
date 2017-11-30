package com.dml.souqiu;

import com.dml.home.HomeActivity;
import com.dml.channel.ChannelActivity;
import com.dml.search.SearchActivity;
import com.dml.myInfo.InfoActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnCheckedChangeListener {
	private RadioGroup radioGroup;
	private TabHost tabhost;
	private Intent home;
	private Intent channel;
	private Intent search;
	private Intent info;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//�ޱ�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		radioGroup=(RadioGroup)findViewById(R.id.main_tab);
        radioGroup.setOnCheckedChangeListener(this);
        
        // ʵ��TabHost,��TabHost���4������
		tabhost=getTabHost();
		
		home = new Intent(this, HomeActivity.class);
	    tabhost .addTab(tabhost.newTabSpec("home")
	            .setIndicator(getResources().getString(R.string.home), getResources().getDrawable(R.drawable.icon_1_n))
	            .setContent(home));   
	        
	    channel = new Intent(this, ChannelActivity.class);
		tabhost.addTab(tabhost.newTabSpec("channel")
		       .setIndicator(getResources().getString(R.string.channel), getResources().getDrawable(R.drawable.icon_2_n))
		       .setContent(channel));
			
		search = new Intent(this,SearchActivity.class);
		tabhost.addTab(tabhost.newTabSpec("search")
		       .setIndicator(getResources().getString(R.string.search), getResources().getDrawable(R.drawable.icon_3_n))
		       .setContent(search));
			
		info = new Intent(this, InfoActivity.class);
		tabhost.addTab(tabhost.newTabSpec("info")
		       .setIndicator(getResources().getString(R.string.info), getResources().getDrawable(R.drawable.icon_3_n))
		       .setContent(info));
	}
	
	// ��ݵ���İ�ť��ת����Ӧ�Ľ���
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_button1:
			this.tabhost.setCurrentTabByTag("home");
			break;
		case R.id.radio_button2:
			this.tabhost.setCurrentTabByTag("channel");
			break;
		case R.id.radio_button3:
			this.tabhost.setCurrentTabByTag("search");
			break;
		case R.id.radio_button4:
			this.tabhost.setCurrentTabByTag("info");
			break;
		}
	}

	//����ؼ�,�˳�ȷ��
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
	   if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		   //����Ϊfalse�������ؼ����˳���Ĭ��Ϊtrue��
		   new AlertDialog.Builder(this).setCancelable(false).setTitle("提示")
	               .setMessage("缓冲中…")
	               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	    
	                    public void onClick(DialogInterface dialog, int which) {
	                                        finish();
	                                           }
	                                            })
	              .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which){
	            	   
	               }
	       }).show();
	           return true;
	   }
	    return super.dispatchKeyEvent(event);
	}
}
