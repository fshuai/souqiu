package com.dml.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.dml.souqiu.R;
import com.dml.utils.FileUtils;
import com.dml.utils.ImageDownLoader;
import com.dml.utils.ImageDownLoader.onImageLoaderListener;

public class HomeAdapter extends BaseAdapter implements OnScrollListener{
	/**
	 * 上下文对象的引用
	 */
	private Context context;
	
	/**
	 * 视频信息
	 */
	private String[][] info;
	
	/**
	 * GridView对象的应用
	 */
	private GridView mGridView;
	
	/**
	 * Image 下载器
	 */
	private ImageDownLoader mImageDownLoader;
	
	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 * 参考http://blog.csdn.net/guolin_blog/article/details/9526203#comments
	 */
	private boolean isFirstEnter = true;
	
	/**
	 * 一屏中第一个item的位置
	 */
	private int mFirstVisibleItem;
	
	/**
	 * 一屏中所有item的个数
	 */
	private int mVisibleItemCount;
	private FileUtils fileUtils;
	
	private LayoutInflater inflater = null;
	public HomeAdapter(Context context, GridView mGridView, String[][] info){
		this.context = context;
		this.mGridView = mGridView;
		this.info = info;
		mImageDownLoader = new ImageDownLoader(context);
		mGridView.setOnScrollListener(this);
		inflater = LayoutInflater.from(context); 
		fileUtils = new FileUtils(context);
	
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务  
		if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			showImage(mFirstVisibleItem, mVisibleItemCount);
		}else{
			cancelTask();
		}
		
	}


	/**
	 * GridView滚动的时候调用的方法，刚开始显示GridView也会调用此方法
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		// 因此在这里为首次进入程序开启下载任务。 
		if(isFirstEnter && visibleItemCount > 0){
			showImage(mFirstVisibleItem, mVisibleItemCount);
			isFirstEnter = false;
		}
	}
	

	@Override
	public int getCount() {
		if(info==null)
			return 0;
		else 
		return info.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	//自定义GridView
	private class ViewHolder{
		private ImageView imageView = null;;
		private TextView textView = null;;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		final String mImageUrl = info[position][1];
		if(convertView == null){
			 convertView = inflater.inflate(R.layout.grid_home_items,null); 
			 viewHolder = new ViewHolder();
			 viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			 viewHolder.textView = (TextView) convertView.findViewById(R.id.title);
			 convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.imageView.setTag(mImageUrl);  //关键代码，showImage有引用
		
		
		if(info != null)   //缓存文件空时，文字区域显示空白，不会导致程序崩溃
	    	viewHolder.textView.setText(info[position][2]);
		
		/*
		ImageView mImageView;
		final String mImageUrl = imageThumbUrls[position];
		if(convertView == null){
			mImageView = new ImageView(context);
		}else{
			mImageView = (ImageView) convertView;
		}
		
		mImageView.setLayoutParams(new GridView.LayoutParams(150, 150));
		mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);   
		
		//给ImageView设置Tag,这里已经是司空见惯了
		mImageView.setTag(mImageUrl);
		*/
		
		/*******************************去掉下面这几行试试是什么效果****************************/
		
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
		//获取Bitmap, 内存中没有就去手机或者sd卡中获取
		if(bitmap != null){
			viewHolder.imageView.setImageBitmap(bitmap);
			
		}else{
			viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_empty));
			
		}
		/**********************************************************************************/
		return convertView;
	}
	
	/**
	 * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 */
	private void showImage(int firstVisibleItem, int visibleItemCount){
		Bitmap bitmap = null;
		for(int i=firstVisibleItem; i<firstVisibleItem + visibleItemCount; i++){
			String mImageUrl = info[i][1];
			final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
			
			bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {
			//这里是定义了一个回调接口的方式	
				@Override
				public void onImageLoader(Bitmap bitmap, String url) {
					if(mImageView != null && bitmap != null){
						mImageView.setImageBitmap(bitmap);
					}
					
				}
			});
			
			if(bitmap != null){
				mImageView.setImageBitmap(bitmap);
			}else{
				mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_empty));
			}
		}
	}

	/**
	 * 取消下载任务
	 */
	public void cancelTask(){
		mImageDownLoader.cancelTask();
	}


}
