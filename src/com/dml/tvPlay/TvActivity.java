package com.dml.tvPlay;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import com.dml.souqiu.R;






import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageView;
import android.widget.TextView;


public class TvActivity extends Activity implements OnCompletionListener, OnInfoListener, OnBufferingUpdateListener{
	
	private int channel;
	private String mPath;
	private String mTitle;
	private VideoView mVideoView;  
    private View mVolumeBrightnessLayout;  
    private ImageView mOperationBg;  
    private ImageView mOperationPercent;  
    private AudioManager mAudioManager;  
    /** ���� */  
    private int mMaxVolume;  
    /** ��ǰ���� */  
    private int mVolume = -1;  
    /** ��ǰ���� */  
    private float mBrightness = -1f;  
    /** ��ǰ����ģʽ */  
    private int mLayout = VideoView.VIDEO_LAYOUT_STRETCH;  
    private GestureDetector mGestureDetector;  
    private MediaController mMediaController;  
    private View mLoadingView;  
    private TextView mSudu;
    private TextView mBuff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		 if (!LibsChecker.checkVitamioLibs(this))  
	            return;  
		 
		setContentView(R.layout.tv);
		
		Intent intent = this.getIntent();
		channel = intent.getIntExtra("channel", -1);
		mTitle = intent.getStringExtra("title");
		getTvUrl();
		
		mSudu = (TextView) findViewById(R.id.sudu);
		mBuff = (TextView) findViewById(R.id.buff);
		
		mVideoView = (VideoView) findViewById(R.id.surface_view);  
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);  
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);  
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);  
        mLoadingView = findViewById(R.id.video_loading);  
        
        // ~~~ ���¼�  
        mVideoView.setOnCompletionListener(this);  
        mVideoView.setOnInfoListener(this);  
    	mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setVideoLayout(mLayout, 0);
  
        // ~~~ ������  
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
          
        
        if (mPath.startsWith("http:") || mPath.startsWith("rtmp:")){
            mVideoView.setVideoURI(Uri.parse(mPath));
            System.out.println("after set live:"+mPath);
        }
        else{
            mVideoView.setVideoPath(mPath);  
        }
        
        
        //������ʾ����  
        mMediaController = new MediaController(this);  
        mMediaController.setFileName(mTitle);  
        mVideoView.setMediaController(mMediaController);  
        mVideoView.requestFocus();  
  
        mGestureDetector = new GestureDetector(this, new MyGestureListener());  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
	}
	
	protected void getTvUrl(){
		switch(channel){
		case 0:
			mPath = "rtmp://202.114.12.125/multicast/cctv-5?fms.multicast.type=3";
			break;
		case 1:
			mPath = "rtmp://202.114.12.125/multicast/fengyunzuqiu?fms.multicast.type=3";
			break;
		case 2:
			mPath = "rtmp://202.114.12.125/multicast/ouzhouzuqiu?fms.multicast.type=3";
			break;
		default:
			break;
		}
	}
	

    @Override  
    protected void onPause() {  
        super.onPause();  
        if (mVideoView != null)  
            mVideoView.pause();  
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        if (mVideoView != null)  
            mVideoView.resume();  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        if (mVideoView != null)  
            mVideoView.stopPlayback();  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        if (mGestureDetector.onTouchEvent(event))  
            return true;  
  
        // �������ƽ���  
        switch (event.getAction() & MotionEvent.ACTION_MASK) {  
        case MotionEvent.ACTION_UP:  
            endGesture();  
            break;  
        }  
  
        return super.onTouchEvent(event);  
    }  
  
    /** ���ƽ��� */  
    private void endGesture() {  
        mVolume = -1;  
        mBrightness = -1f;  
  
        // ����  
        mDismissHandler.removeMessages(0);  
        mDismissHandler.sendEmptyMessageDelayed(0, 500);  
    }  
  
    private class MyGestureListener extends SimpleOnGestureListener {  
  
        /** ˫�� */  
        @Override  
        public boolean onDoubleTap(MotionEvent e) {  
            if (mLayout == VideoView.VIDEO_LAYOUT_STRETCH)  
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;  
            else  
                mLayout++;  
            Log.e("layout",mLayout + "");
            if (mVideoView != null)  
                mVideoView.setVideoLayout(mLayout, 0);  
            return true;  
        }  
  
        /** ���� */  
        @SuppressWarnings("deprecation")  
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
            float mOldX = e1.getX(), mOldY = e1.getY();  
            int y = (int) e2.getRawY();  
            Display disp = getWindowManager().getDefaultDisplay();  
            int windowWidth = disp.getWidth();  
            int windowHeight = disp.getHeight();  
  
            if (mOldX > windowWidth * 4.0 / 5)// �ұ߻���  
                onVolumeSlide((mOldY - y) / windowHeight);  
            else if (mOldX < windowWidth / 5.0)// ��߻���  
                onBrightnessSlide((mOldY - y) / windowHeight);  
  
            return super.onScroll(e1, e2, distanceX, distanceY);  
        }  
    }  
  
    /** ��ʱ���� */  
    private Handler mDismissHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            mVolumeBrightnessLayout.setVisibility(View.GONE);  
        }  
    };  
  
    /** 
     * �����ı�������С 
     *  
     * @param percent 
     */  
    private void onVolumeSlide(float percent) {  
        if (mVolume == -1) {  
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
            if (mVolume < 0)  
                mVolume = 0;  
  
            // ��ʾ  
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);  
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);  
        }  
  
        int index = (int) (percent * mMaxVolume) + mVolume;  
        if (index > mMaxVolume)  
            index = mMaxVolume;  
        else if (index < 0)  
            index = 0;  
  
        // �������  
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);  
  
        // ���������  
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();  
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;  
        mOperationPercent.setLayoutParams(lp);  
    }  
  
    /** 
     * �����ı����� 
     *  
     * @param percent 
     */  
    private void onBrightnessSlide(float percent) {  
        if (mBrightness < 0) {  
            mBrightness = getWindow().getAttributes().screenBrightness;  
            if (mBrightness <= 0.00f)  
                mBrightness = 0.50f;  
            if (mBrightness < 0.01f)  
                mBrightness = 0.01f;  
  
            // ��ʾ  
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);  
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);  
        }  
        WindowManager.LayoutParams lpa = getWindow().getAttributes();  
        lpa.screenBrightness = mBrightness + percent;  
        if (lpa.screenBrightness > 1.0f)  
            lpa.screenBrightness = 1.0f;  
        else if (lpa.screenBrightness < 0.01f)  
            lpa.screenBrightness = 0.01f;  
        getWindow().setAttributes(lpa);  
  
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();  
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);  
        mOperationPercent.setLayoutParams(lp);  
    }  
  
    @Override  
    public void onConfigurationChanged(Configuration newConfig) {  
        if (mVideoView != null)  
            mVideoView.setVideoLayout(mLayout, 0);  
        super.onConfigurationChanged(newConfig);  
    }  
    
    @Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
    	Log.e("buffering", mVideoView.getBufferPercentage() + "%");
		mBuff.setText(mVideoView.getBufferPercentage() + "%");
	}
  
    @Override  
    public void onCompletion(MediaPlayer player) {  
    	 Log.e("tet", "�������");  
    }  
  
    private void stopPlayer() {  
        if (mVideoView != null)  
            mVideoView.pause();  
    }  
  
    private void startPlayer() {  
        if (mVideoView != null)  
            mVideoView.start();  
    }  
  
    private boolean isPlaying() {  
        return mVideoView != null && mVideoView.isPlaying();  
    }  
  
    /** �Ƿ���Ҫ�Զ��ָ����ţ������Զ���ͣ���ָ����� */  
   // private boolean needResume; 
   // private boolean flag = true;  //��ǣ���һ�ν��벥������������ɺ��Զ�����
  
    @Override  
    public boolean onInfo(MediaPlayer arg0, int arg1, int down_rate) {  
        switch (arg1) {  
        case MediaPlayer.MEDIA_INFO_BUFFERING_START:  
            //��ʼ���棬��ͣ����  
            if (isPlaying()) {  
                stopPlayer();  
              //  needResume = true;  
            }  
            mLoadingView.setVisibility(View.VISIBLE);  
            break;  
        case MediaPlayer.MEDIA_INFO_BUFFERING_END:  
            //������ɣ��������� 
            if (!isPlaying()){  
                startPlayer();  
            } 
            mLoadingView.setVisibility(View.GONE);   
            /*
            if(flag == true){
            	startPlayer();
            	flag = false;
            	Log.e("test","true");
            } else{
            	Log.e("test","false");
            }
            */
            break;  
        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:  
            //��ʾ �����ٶ�  
        	mSudu.setText(down_rate + "kB/s");
            Log.e("download rate", down_rate + "KB/s");   
            //mListener.onDownloadRateChanged(arg2);  
            break;  
        }
        return true;  
    }
	
}
