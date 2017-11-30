package com.dml.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.dml.TvMao.TvSearchActivity;
import com.dml.home.MoreActivity;
import com.dml.recommend.recommendActivity;
import com.dml.souqiu.R;  //��Ҫ���ˣ����򱨴�
import com.dml.tvPlay.TvForecastActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChannelActivity extends Activity{
	private GridView grid;
	//先把台球放前边
    private int[] ImagesId = new int[]{R.drawable.football, R.drawable.billiards, R.drawable.tv, R.drawable.basketball, R.drawable.badminton, R.drawable.volleyball, R.drawable.tennis, R.drawable.pingpong,/*R.drawable.tvmao,R.drawable.recommend*/};
    private String[] Titles = new String[]{"足球", "台球" , "直播" ,"篮球", "羽毛球", 
    		"排球", "网球" , "乒乓球", /*"brocast","recommend"*/};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_activity);
		
		List <Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < ImagesId.length; i++)
		{
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("imageItem", ImagesId[i]);
			listItem.put("textItem", Titles[i]); 
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.grid_channel_items, new String[]{"imageItem", "textItem"}, new int[]{R.id.image_item, R.id.text_item} );
	    grid = (GridView) findViewById(R.id.gridView_channel);
	    grid.setAdapter(simpleAdapter);
	   
	    // ��ݵ���¼���ת�����������Ƶ�б�
	    grid.setOnItemClickListener(new OnItemClickListener() {

	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
	    		Intent intent = new Intent();
	    		intent.setClass(ChannelActivity.this, MoreActivity.class);
	    		switch(postion){
	    		//顺序暂时改一下
	    		case 0:
	    			intent.putExtra("videotag", 1);
	    			startActivity(intent);
	    			break;
	    		case 1:
	    			intent.putExtra("videotag", 7);
	    			startActivity(intent);
	    			break;
	    		case 2:
	    			startActivity(new Intent(ChannelActivity.this, TvForecastActivity.class));
	    			break;
	    		case 3:
//	    			intent.putExtra("videotag", 4);
//	    			startActivity(intent);
	    			Toast.makeText(ChannelActivity.this, "功能开发中", Toast.LENGTH_LONG).show();
	    			break;
	    		case 4:
//	    			intent.putExtra("videotag", 5);
//	    			startActivity(intent);
	    			Toast.makeText(ChannelActivity.this, "功能开发中", Toast.LENGTH_LONG).show();
	    			break;
	    		case 5:
//	    			intent.putExtra("videotag", 6);
//	    			startActivity(intent);
	    			Toast.makeText(ChannelActivity.this, "功能开发中", Toast.LENGTH_LONG).show();
	    			break;
	    		case 6:
//	    			intent.putExtra("videotag", 3);
//	    			startActivity(intent);
	    			Toast.makeText(ChannelActivity.this, "功能开发中", Toast.LENGTH_LONG).show();
	    			break;
	    		case 7:
	    			Toast.makeText(ChannelActivity.this, "功能开发中", Toast.LENGTH_LONG).show();
	    			break;
	    		case 8:
	    			//Toast.makeText(getApplicationContext(), resId, duration)
	    			Toast.makeText(ChannelActivity.this, "功能开发中", Toast.LENGTH_LONG).show();
	    			//startActivity(new Intent(ChannelActivity.this,TvSearchActivity.class));
	    		//case 9:
	    		//	startActivity(new Intent(ChannelActivity.this,recommendActivity.class));
	    		default:
	    			break;
	    		}
	    	}
	    });
	}	
	
}
