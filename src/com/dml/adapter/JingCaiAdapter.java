package com.dml.adapter;

import com.dml.souqiu.R;
import com.dml.tvPlay.MyGridview;
import com.dml.tvPlay.timeFormate;
import com.dml.utils.ImageDownLoader;
import com.dml.utils.ImageDownLoader.onImageLoaderListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class JingCaiAdapter extends BaseAdapter{
	
	private Context context;
	private String[] urls;
	private String[] times;
    private MyGridview gridView;
    private ImageDownLoader mImageDownLoader;
    private LayoutInflater inflater = null;
    
	public JingCaiAdapter(Context context, String[] urls, String[] times, MyGridview gridView){
		this.context = context;
		this.urls = urls;
		this.times = times;
		this.gridView = gridView;
		mImageDownLoader = new ImageDownLoader(context);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if(convertView == null){
			 convertView = inflater.inflate(R.layout.jingcaitape_item, null); 
			 viewHolder = new ViewHolder();
			 viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			 viewHolder.textView = (TextView) convertView.findViewById(R.id.title);
			 convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
	    convertView.setTag(viewHolder);
		if(times[position] != null)   
	    	viewHolder.textView.setText("时长:" + new timeFormate().timeFormate(times[position]));
		
		 Bitmap bitmap2 = mImageDownLoader.downloadImage(urls[position], new onImageLoaderListener() {
				@Override
				public void onImageLoader(Bitmap bitmap, String url) {
					if(viewHolder.imageView != null && bitmap != null){
						viewHolder.imageView.setImageBitmap(bitmap);
					}
				}
			});
	      
			if(bitmap2 != null){
				viewHolder.imageView.setImageBitmap(bitmap2);
			}else{
				viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_empty));
			}
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView imageView = null;;
		private TextView textView = null;;
	}

}
