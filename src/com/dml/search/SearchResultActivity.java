package com.dml.search;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.dml.adapter.SearchResultAdapter;
import com.dml.bean.VideoBean;
import com.dml.home.MoreActivity;
import com.dml.internet.GetConnectionSearch;
import com.dml.internet.GetConnectionUpdate;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.dml.utils.BitmapUtils;
import com.dml.widget.XListView;
import com.dml.widget.XListView.IXListViewListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends Activity implements IXListViewListener {
	
	private int query1;
	private int query2;
	private int query3;
	private int mode;
	private String key;
	private String path;
	
	private static int flag = 0;   //��ǵ�һ�ν���������Ľ��棬�����������ȫ��Ϊ�յ����
	private final String TAG = "XListViewActivity";
	private final int DATA_EMPTY = 0;
	private final int DATA_SUCCESS = 1;
	private final int UPLOAD_SUCCESS = 2;
	private final int UPLOAD_FAIL = 3;
	
	private List<VideoBean> data;
	private List<VideoBean> data2;
	private XListView listView;
	private View pb;
	private SearchResultAdapter adapter;
	private TextView keyword;
	private ImageView img;
	private ImageView backImg;
	private int pageid = 0;      //����ҳ���
	private Bitmap bm = null;
	
	private String webServiceUrl;
	private static final String NAMESPACE ="http://tempuri.org/"; 
	private static final String METHOD_NAME ="FileUploadImage"; 
	private static String SOAP_ACTION ="http://tempuri.org/FileUploadImage";  
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_result); 
		img = (ImageView) findViewById(R.id.search_img);
		backImg = (ImageView) findViewById(R.id.searchresult_back);
	    keyword = (TextView) findViewById(R.id.keyword);
	    
	    pb = findViewById(R.id.pb);
	    pb.setVisibility(View.VISIBLE);
	    
		listView = (XListView) findViewById(R.id.search_result_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
	    getValue();
	    
	    listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub		
					//GetConnectionUpdate.updateClickNum(query1, data.get(position-1).getId());
					Intent intent = new Intent();
					intent.putExtra("videotag", String.valueOf(query1));
					intent.putExtra("videoID", String.valueOf(data.get(position - 1).getId()));
					intent.putExtra("videoTitle", String.valueOf(data.get(position - 1).getTitle()));
					intent.putExtra("videoUrl", String.valueOf(data.get(position - 1).getVideoUrl()));
					intent.setClass(SearchResultActivity.this, video_show.class);
					startActivity(intent);
			}			
	    });
	    
	    backImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
	    	
	    });
	}
	
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case DATA_SUCCESS:
				if(pageid == 0){    //����ʱ��һ�μ�������
					adapter = new SearchResultAdapter(SearchResultActivity.this, data, listView);
					System.out.println("adpter:" + adapter.getCount());
			    	listView.setAdapter(adapter);
			    	pb.setVisibility(View.INVISIBLE);
			    	onLoad();
				}else{
					//�����ݼ��ھ����ݺ���
					data2.addAll(data);      
					data = data2;
					System.out.println("data after:" + adapter.getCount());
					adapter.notifyDataSetChanged();  //ֱ�Ӹ�������������������set
					System.out.println("adpter:" + adapter.getCount());
			    	onLoad();
				}
				break;
			case DATA_EMPTY:
				data = data2;
				if(flag != 0){
				   adapter.notifyDataSetChanged();
				   onLoad();
				   Toast.makeText(SearchResultActivity.this, "������ȫ���������", Toast.LENGTH_SHORT).show();
				}else{
				   Toast.makeText(SearchResultActivity.this, "�������Ϊ��", Toast.LENGTH_SHORT).show();
				   pb.setVisibility(View.INVISIBLE);
				}
				break;
			case UPLOAD_SUCCESS:
				Toast.makeText(SearchResultActivity.this, "ͼƬ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
				getData();
				break;
			case UPLOAD_FAIL:
				Toast.makeText(SearchResultActivity.this, "ͼƬ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
				pb.setVisibility(View.INVISIBLE);
				break;
				default:
					break;
			}
		}
	};  
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		
		String time = Long.toString(new Date().getTime());
        Long timeLong = Long.valueOf(time);
        listView.setRefreshTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(new java.util.Date (timeLong)));
	}

	@Override
	public void onRefresh() {
		Log.i(TAG, "ˢ������");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageid = 0;
				if(data != null)
				     data.clear();
				getData();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		Log.i(TAG, "���ظ���");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageid++;
				getData();
			//	adapter.notifyDataSetChanged();
			//	onLoad();
			}
		}, 2000);
	}
	
	
	protected void getData(){
		new Thread(){
	    	@Override    
	    	public void run(){
	    		data2 = data;
	    		data = GetConnectionSearch.getCon(mode, pageid, key,query1,query2,query3);
	    		//Toast.makeText(getApplicationContext(), "mode=" + mode + "key=" + key + "query1=" +  + "query2=" + query2+ "query3=" + query3, Toast.LENGTH_LONG).show();
	    		if(data != null && !data.isEmpty()){
	    			flag = 1;
	    			 System.out.println("data before:" + data.size());
		    		  handler.sendEmptyMessage(DATA_SUCCESS);
	    			
	    		}else{
	    			 System.out.println("null");
		    		 handler.sendEmptyMessage(DATA_EMPTY);
	    		}
	    		/*
	    		if(data==null){
	    			System.out.println("yes");
	    			handler.sendEmptyMessage(DATA_EMPTY);
	    		}else{
	    		    System.out.println("data before:" + data.size());
	    		    handler.sendEmptyMessage(DATA_SUCCESS);
	    		}
	    		*/
	    	}
	    }.start();
	}

	/*
	 * ��ȡ�ϸ�ҳ�洫�ݹ����Ĳ���
	 */
	protected void getValue(){
		Intent intent = this.getIntent();
		mode = intent.getIntExtra("mode", 1);       //������ʽ
	    query1 = intent.getIntExtra("query1", 0);   //������
	    query2 = intent.getIntExtra("query2", 0);   //����ʽ
	    query3 = intent.getIntExtra("query3", 0);   //ʱ�䳤��
	    
	    switch(query1){
	    case 1:
	    	//webServiceUrl = "http://202.114.18.76/WebService.asmx";
	    	webServiceUrl="http://202.114.18.68:8805/api/soccer/_search?q=videoname:";
	    	break;
	    case 2:
	    	webServiceUrl = "http://202.114.18.77:8882/WebService.asmx";
	    	break;
	    case 3:
	    	webServiceUrl = "http://202.114.18.77:8883/WebService.asmx";
	    	break;
	    case 4:
	    	webServiceUrl = "http://202.114.18.77:8884/WebService.asmx";
	    	break;
	    case 5:
	    	webServiceUrl = "http://202.114.18.77:8885/WebService.asmx";
	    	break;
	    case 6:
	    	webServiceUrl = "http://202.114.18.96:8886/WebService.asmx";
	    	break;
	    case 7:
	    	//webServiceUrl = "http://202.114.18.96:8888/WebService.asmx";
	    	webServiceUrl="http://202.114.18.68:8805/api/snooker/_search?q=videoname:";
	    	break;
	    default:
	    	break;
	    }
	    if(mode == 1 ){
	    	key = intent.getStringExtra("key");
	    	 keyword.setText(key);
	    	img.setVisibility(View.GONE);
	    	keyword.setVisibility(View.VISIBLE);
	    	getData();
	    }else{
	    	path = intent.getStringExtra("path");
	    	keyword.setVisibility(View.GONE);
	    	img.setVisibility(View.VISIBLE);
	    	bm = BitmapUtils.decodeSampledBitmapFromFile(path, 120, 100);
	    	img.setImageBitmap(bm);
	            new Thread(){
	            	@Override
	            	public void run(){
	            		try {
							key = connectWebService(getBuffer(path)) + "";
							if(!key.equals("0")){
							    handler.sendEmptyMessage(UPLOAD_SUCCESS);
							    Log.i("url","http://202.114.18.77:8882/AndroidSearch.aspx?mode=" + mode + "&key=" + key + "&pageid=" + pageid + "&orderby=" + query2 + "&lengthtype=" + query3);
							}else{
								handler.sendEmptyMessage(UPLOAD_FAIL);
								Log.i("url",key);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            }.start();
			} 
	}
	
	/**
	 * @description ͼƬ�������󵽵�WebService
	 * @param uploadBuffer
	 * @return
	 * @throws IOException
	 */
	 private int connectWebService(String uploadBuffer) throws IOException { 
	        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME); 
	        soapObject.addProperty("title", ""); 
	        soapObject.addProperty("contect","" ); 
	        soapObject.addProperty("bytestr", uploadBuffer);   //����2  ͼƬ�ַ���    
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);    
	        envelope.setOutputSoapObject(soapObject); 
	        envelope.bodyOut = soapObject;    
	        envelope.dotNet = true;    
	        envelope.encodingStyle = SoapSerializationEnvelope.ENC;             
	        HttpTransportSE httpTranstation = new HttpTransportSE(webServiceUrl);    
	        int imgid = 0;
	        try {    
	        	Log.i("info","1");
	            httpTranstation.call(SOAP_ACTION, envelope);     
	            Log.i("info","2");
	            // ��ȡ���ص�����  
	            SoapObject object = (SoapObject) envelope.bodyIn;   
	            Log.i("info","3");
	            // ��ȡ���صĽ��  
	           imgid =  Integer.parseInt(object.getProperty(0).toString()); 
	           Log.i("info",imgid + "");
	         
	        } catch (Exception e) {    
	            e.printStackTrace(); 
	        }    
	      return imgid;
	    } 
	 
	 /**
		 * @description ���ؾ���ѹ�����ͼƬ����
		 * @param path
		 * @return
		 */
	 public String getBuffer(String path){
		   // Bitmap bm = BitmapUtils.decodeSampledBitmapFromFile(path, 120, 100);
			InputStream fis = BitmapUtils.Bitmap2InputStream(bm);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int count = 0;  
	        try {
				while((count = fis.read(buffer)) >= 0){  
				    baos.write(buffer, 0, count); 
				}
				fis.close();
			    String uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //����Base64����       
			    return uploadBuffer; 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return null;
	 }
}
