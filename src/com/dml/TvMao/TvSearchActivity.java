package com.dml.TvMao;
import com.dml.souqiu.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TvSearchActivity extends TabActivity implements OnCheckedChangeListener {

	private TabHost mHost = null;

	private Intent mTVSearchIntent = null;
	private Intent mTVCurrentIntent = null;
	private Intent mTVOrderIntent = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_guide_main);

		this.mTVSearchIntent = new Intent(this, TVSearch.class);                                                    //?
		this.mTVCurrentIntent = new Intent(this, TVCurrent.class);                                                //?
		this.mTVOrderIntent = new Intent(this, TVOrder.class);                                                    //?

		initRadios();
		getIntent();
		setupIntent();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.tv_search:
				this.mHost.setCurrentTabByTag("tv_search_tab");
				break;
			case R.id.tv_current:
				this.mHost.setCurrentTabByTag("tv_current_tab");
				break;
			case R.id.tv_order:
				this.mHost.setCurrentTabByTag("tv_order_tab");
				break;
			}
		}
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	private void initRadios() {
		((RadioButton) findViewById(R.id.tv_search))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.tv_current))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.tv_order))
				.setOnCheckedChangeListener(this);
	}

	private void setupIntent() {
		this.mHost = getTabHost();
		TabHost localTabHost = this.mHost;

		localTabHost.addTab(buildTabSpec("tv_search_tab",
				R.string.tv_guide_search, R.drawable.main_normal_login,
				this.mTVSearchIntent));

		localTabHost.addTab(buildTabSpec("tv_current_tab",
				R.string.tv_guide_current, R.drawable.main_face_login,
				this.mTVCurrentIntent));

		localTabHost.addTab(buildTabSpec("tv_order_tab",
				R.string.tv_guide_order, R.drawable.main_register,
				this.mTVOrderIntent));
	}
}
