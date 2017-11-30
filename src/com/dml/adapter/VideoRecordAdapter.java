package com.dml.adapter;

import java.util.List;


import com.dml.bean.VideoBean;
import com.dml.souqiu.R;
import com.dml.utils.ImageDownLoader;
import com.dml.utils.ImageDownLoader.onImageLoaderListener;
import com.dml.widget.XListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VideoRecordAdapter extends BaseAdapter{


	private Context context;
	private ListView listView;
	private List<VideoBean> data;
	private ImageDownLoader mImageDownLoader;
	private LayoutInflater inflater = null;
	private ImageView img;

	public VideoRecordAdapter(Context context, List<VideoBean> data){
		this.context = context;
		this.data = data;
		mImageDownLoader = new ImageDownLoader(context);
		inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
		String url = null;
		if(convertView  == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.tab1_item, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.publish);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
			 url = data.get(position).getImageUrl();
			 viewHolder.videoTitle.setText(data.get(position).getTitle());
			 viewHolder.time.setText(data.get(position).getTime());
		 	 convertView.setTag(viewHolder);
		
	      Bitmap bitmap2 = mImageDownLoader.downloadImage(url, new onImageLoaderListener() {
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
		private ImageView imageView;
		private TextView videoTitle;
		private TextView time;
	}
}
