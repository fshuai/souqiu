package com.dml.adapter;

import com.dml.souqiu.R;
import com.dml.utils.ImageDownLoader;
import com.dml.utils.ImageDownLoader.onImageLoaderListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GalleryAdapter extends BaseAdapter{

	private Context context;
	private ImageDownLoader mImageDownLoader;
	private String[] arr_preview;
	private boolean isFirstEnter = true;
	private Gallery mGallery;
	
	
	public GalleryAdapter(Context context, String[] arr_preview, Gallery mGallery){
		this.context = context;
		this.arr_preview = arr_preview;
		this.mGallery = mGallery;
		mImageDownLoader = new ImageDownLoader(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return  arr_preview.length;
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
	    ImageView imageView = null;
		if(convertView == null){
			imageView = new ImageView(context);
		}else{
			imageView = (ImageView) convertView;
		}
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(240, 200));
		imageView.setTag(arr_preview[position]);
	
		final ImageView imageView2 = imageView;
        Bitmap bitmap2 = mImageDownLoader.downloadImage(arr_preview[position], new onImageLoaderListener() {
			@Override
			public void onImageLoader(Bitmap bitmap, String url) {
				
				if(imageView2 != null && bitmap != null){
					imageView2.setImageBitmap(bitmap);
				}
				
			}
		});
		if(bitmap2 != null){
			imageView.setImageBitmap(bitmap2);
		}else{
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_empty));
		}
		
		
		return imageView;
	}

}
