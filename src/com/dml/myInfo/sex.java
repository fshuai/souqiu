package com.dml.myInfo;

import com.dml.souqiu.R;
import com.dml.widget.StrericWheelAdapter;
import com.dml.widget.WheelView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

public class sex extends Activity{
	Intent it;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sex);
		 it=getIntent();
	    initWheel(R.id.passw_1,new String[]{"男","女","保密"});
	    Button btn=(Button)findViewById(R.id.pwd_status);
	    	btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) { 
					// TODO Auto-generated method stub
					//System.out.println(getWheelValue(R.id.passw_1));
					it.putExtra("sex", getWheelValue(R.id.passw_1));
					sex.this.setResult(2, it);
					sex.this.finish();
				}
			});
	    }
	    
	    private boolean wheelScrolled = false;
	    
	    
	    /**
	     * Updates entered PIN status
	     * 
	     */
	    
	    private String getWheelValue(int id) {
	    	return getWheel(id).getCurrentItemValue();
	    }
	//ȡֵ�ķ���
	    //getWheelValue(R.id.passw_1)
	    //getWheelValue(R.id.passw_2)

	    /**
	     * Returns wheel by Id
	     * @param id the wheel Id
	     * @return the wheel with passed Id
	     * ����ӵ�
	     * 
	     */
	    private WheelView getWheel(int id) {
	    	return (WheelView) findViewById(id);
	    }
	    
	    private void initWheel(int id,String[] strContents) {
	        WheelView wheel = getWheel(id);
	        wheel.setAdapter(new StrericWheelAdapter(strContents));
	        wheel.setCurrentItem(0);
	        
	        wheel.setCyclic(true);
	        wheel.setInterpolator(new AnticipateOvershootInterpolator());
	    }
	    
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	finish();
	    	return true;
	    }
	}
