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
	 * �����Ķ��������
	 */
	private Context context;
	
	/**
	 * ��Ƶ��Ϣ
	 */
	private String[][] info;
	
	/**
	 * GridView�����Ӧ��
	 */
	private GridView mGridView;
	
	/**
	 * Image ������
	 */
	private ImageDownLoader mImageDownLoader;
	
	/**
	 * ��¼�Ƿ�մ򿪳������ڽ��������򲻹�����Ļ����������ͼƬ�����⡣
	 * �ο�http://blog.csdn.net/guolin_blog/article/details/9526203#comments
	 */
	private boolean isFirstEnter = true;
	
	/**
	 * һ���е�һ��item��λ��
	 */
	private int mFirstVisibleItem;
	
	/**
	 * һ��������item�ĸ���
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
		//����GridView��ֹʱ��ȥ����ͼƬ��GridView����ʱȡ�������������ص�����  
		if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			showImage(mFirstVisibleItem, mVisibleItemCount);
		}else{
			cancelTask();
		}
		
	}


	/**
	 * GridView������ʱ����õķ������տ�ʼ��ʾGridViewҲ����ô˷���
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		// ���������Ϊ�״ν���������������� 
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

	//�Զ���GridView
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
		viewHolder.imageView.setTag(mImageUrl);  //�ؼ����룬showImage������
		
		
		if(info != null)   //�����ļ���ʱ������������ʾ�հף����ᵼ�³������
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
		
		//��ImageView����Tag,�����Ѿ���˾�ռ�����
		mImageView.setTag(mImageUrl);
		*/
		
		/*******************************ȥ�������⼸��������ʲôЧ��****************************/
		
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
		//��ȡBitmap, �ڴ���û�о�ȥ�ֻ�����sd���л�ȡ
		if(bitmap != null){
			viewHolder.imageView.setImageBitmap(bitmap);
			
		}else{
			viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_empty));
			
		}
		/**********************************************************************************/
		return convertView;
	}
	
	/**
	 * ��ʾ��ǰ��Ļ��ͼƬ���Ȼ�ȥ����LruCache��LruCacheû�о�ȥsd�������ֻ�Ŀ¼���ң���û�оͿ����߳�ȥ����
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 */
	private void showImage(int firstVisibleItem, int visibleItemCount){
		Bitmap bitmap = null;
		for(int i=firstVisibleItem; i<firstVisibleItem + visibleItemCount; i++){
			String mImageUrl = info[i][1];
			final ImageView mImageView = (ImageView) mGridView.findViewWithTag(mImageUrl);
			
			bitmap = mImageDownLoader.downloadImage(mImageUrl, new onImageLoaderListener() {
			//�����Ƕ�����һ���ص��ӿڵķ�ʽ	
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
	 * ȡ����������
	 */
	public void cancelTask(){
		mImageDownLoader.cancelTask();
	}


}
