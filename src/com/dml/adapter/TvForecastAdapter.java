package com.dml.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import com.dml.search.SearchActivity;
import com.dml.search.SearchResultActivity;
import com.dml.souqiu.R;
import com.dml.tvPlay.TvActivity;
import com.dml.tvPlay.TvForecastActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class TvForecastAdapter extends BaseExpandableListAdapter{

	private Context context;
	private ExpandableListView expandableListView = null;
	private JSONObject data;
	private String[] group = new String[]{"CCTV-5", "风云足球", "欧洲足球"};
	private LayoutInflater inflater = null;
	
	public TvForecastAdapter(Context context, JSONObject data, ExpandableListView expandableListView){
		this.context = context;
		this.data = data;
		this.expandableListView = expandableListView;
		inflater = LayoutInflater.from(context); 
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.length;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		try {
			return data.getJSONArray(String.valueOf(groupPosition)).length();
		} catch (JSONException e) {
			return 0;
		}
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group[groupPosition];
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		try {
			return data.getJSONArray(String.valueOf(groupPosition)).getJSONObject(childPosition);
		} catch (JSONException e) {
			return null;
		}
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
    
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	GroupHolder groupHolder = null;
        if(convertView ==null){
        	convertView = inflater.inflate(R.layout.list_group_item,null);
        	groupHolder = new GroupHolder();
            groupHolder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            groupHolder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
          
            groupHolder.image = (ImageView) convertView.findViewById(R.id.image);
            groupHolder.btn = (Button) convertView.findViewById(R.id.play);
            convertView.setTag(groupHolder);
        }else{
        	groupHolder = (GroupHolder) convertView.getTag();
        }
        
        groupHolder.txt1.setText(getGroup(groupPosition).toString());
      
        try {
        	if(data != null){
			   groupHolder.txt2.setText(data.getJSONArray(String.valueOf(groupPosition)).getJSONObject(0).getString("program"));
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        groupHolder.btn.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent();
        		intent.putExtra("channel", groupPosition);
        		try {
					intent.putExtra("title", data.getJSONArray(String.valueOf(groupPosition)).getJSONObject(0).getString("program"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		intent.setClass(context, TvActivity.class);
        		context.startActivity(intent);
        	}
        });
        
        if(isExpanded){
        	groupHolder.image.setImageResource(R.drawable.arrow_up);
        }else{
        	groupHolder.image.setImageResource(R.drawable.arrow_down);
        }
        return convertView;
    }

	@Override
	public View getChildView(int groupPosition, int childPosition,	boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildHolder childHolder = null;
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.list_child_item,null);
        	childHolder = new ChildHolder();
        	childHolder.time = (TextView) convertView.findViewById(R.id.time);
            childHolder.broadcast = (TextView) convertView.findViewById(R.id.forecast);
            convertView.setTag(childHolder);
        }else{
        	childHolder = (ChildHolder) convertView.getTag();
        }
        try {
        	if(data != null){
        	  childHolder.time.setText(( (JSONObject) getChild(groupPosition, childPosition)).getString("time"));
			  childHolder.broadcast.setText(( (JSONObject) getChild(groupPosition, childPosition)).getString("program"));
        	}
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class GroupHolder{
		private TextView txt1 = null;
		private TextView txt2 = null;
		private ImageView image = null;
		private Button btn = null;
	}
	private class ChildHolder{
		private TextView time = null;
		private TextView broadcast = null;
	}

}
