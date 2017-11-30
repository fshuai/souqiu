package com.dml.souqiu.wxapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.Util;

import com.dml.myInfo.InfoActivity;
import com.dml.share.ThreadManager;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.connect.share.QQShare;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private GridView grid;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;
	String videourl = "";
	String videoid = "";
	String videotag = "";
	String imageurl = "";
	String videotitle = "";
	private String username;
	private String DEVICE_ID;
	private String[] text = new String[] { "微信好友", "QQ好友", "QQ空间" };
	private int[] imgid = new int[] { R.drawable.share_wechat,
			R.drawable.share_qq, R.drawable.share_qq_space };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode, resultCode, data,
				qqShareListener);// 交给qqshareListener处理
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_tencent);
		Intent it = getIntent();
		videourl = it.getStringExtra("videourl");
		videotitle = it.getStringExtra("videotitle");
		videotag = it.getStringExtra("videotag");
		videoid = it.getStringExtra("videoid");
		imageurl = it.getStringExtra("imageurl");
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);// 注意如果这里想获取回调结果，要摄者为true
		api.handleIntent(getIntent(), this); // 处理回调以后的结果
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		DEVICE_ID = tm.getDeviceId();
		SharedPreferences sp = getSharedPreferences("USERPASS",
				InfoActivity.MODE_PRIVATE);
		username = sp.getString("Login_name", "");
		grid = (GridView) findViewById(R.id.share_tencent);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imgid.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("imageItem", imgid[i]);
			listItem.put("textItem", text[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.share_grid_item, new String[] { "imageItem",
						"textItem" }, new int[] { R.id.share_item_image,
						R.id.share_item_text });
		grid.setAdapter(simpleAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int postion, long id) {
				final Bundle params = new Bundle();
				int tem = 0;
				switch (postion) {
				case 0:// 发送到微信好友中
					tem = 2;
					final Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg1) {
							// TODO Auto-generated method stub
							super.handleMessage(msg1);
							if (msg1.what == 1) {
								Toast.makeText(getApplicationContext(), "this",
										Toast.LENGTH_SHORT).show();
								WXVideoObject video = new WXVideoObject();
								video.videoLowBandUrl = "http://202.114.18.96:8080/SouQiuWang/VideoPlay.jsp?videoid="
										+ videoid;
								WXMediaMessage msg = new WXMediaMessage(video);
								msg.title = "搜球网";
								msg.description = videotitle;
								// *** 如果图片太大无法分享，所以才用压缩图 ***//*
								int THUMB_SIZE = 150;
								Bitmap thumbBmp = Bitmap.createScaledBitmap(
										(Bitmap) msg1.obj, THUMB_SIZE,
										THUMB_SIZE, true);
								msg.thumbData = Util.bmpToByteArray(thumbBmp,
										true); // 设置缩略图
								SendMessageToWX.Req req = new SendMessageToWX.Req();
								req.transaction = buildTransaction("webpage");
								req.message = msg;
								req.scene = SendMessageToWX.Req.WXSceneSession;
								api.sendReq(req);
								finish();
							}
						}
					};
					/*** 由于获取网络照片，所以不能在主线程里面 **/
					new Thread() {
						@Override
						public void run() {
							Bitmap bm = returnBitmap(imageurl);
							Message msg = new Message();
							msg.what = 1;
							msg.obj = bm;// 把bitmap图像放入到obj中
							handler.sendMessage(msg);
						}

					}.start();
					break;
				case 1:// 发送给QQ好友;
					tem = 2;
					checkTencentInstance();
					if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
						params.putString(QQShare.SHARE_TO_QQ_TITLE, "搜球网");
						params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
								videourl);
						params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
								videotitle);
					}
					params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageurl);
					params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "搜球网");
					params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
							QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
					params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
					doShareToQQ(params);
					Toast.makeText(getApplicationContext(), "发送给QQ好友;",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:// 发送给QQ空间
					tem = 3;
					checkTencentInstance();
					params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
					params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "搜球网");
					params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, videotitle);
					params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
							videourl);
					// 支持传多个imageUrl
					ArrayList<String> imageUrls = new ArrayList<String>();
					imageUrls.add(imageurl);
					params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
							imageUrls);
					if (shareType == QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT) {
						doShareToQzone(params);
					} else {
						doPublishToQzone(params);
					}
					Toast.makeText(getApplicationContext(), "发送给QQ空间;",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				final int mode = tem;
				new Thread() {
					@Override
					public void run() {
						try {
							URL url = new URL(
									"http://202.114.18.96:8080/SouQiuWang/playInfo");// 将用户分享操作写入对应的用户文件里
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setDoOutput(true);
							conn.setDoInput(true);
							conn.setRequestMethod("POST");
							OutputStream out = conn.getOutputStream();
							DataOutputStream obj = new DataOutputStream(out);
							// mod=2转发
							String params = "playid="
									+ URLEncoder.encode(videoid.trim(), "utf-8")
									+ "&mod="
									+ URLEncoder.encode(String.valueOf(mode),
											"utf-8") + "&deviceid="
									+ URLEncoder.encode(DEVICE_ID, "utf-8")
									+ "&username="
									+ URLEncoder.encode(username, "utf-8");
							obj.writeBytes(params);
							obj.flush();
							obj.close();
							InputStreamReader in = new InputStreamReader(conn
									.getInputStream());
							BufferedReader buff = new BufferedReader(in);
							String line = "";
							while ((line = buff.readLine()) != null) {
								System.out.println("分享信息：" + line);
							}
							in.close();
							conn.disconnect();

						} catch (Exception e) {

							e.printStackTrace();
						}
					}
				}.start();
			}
		});

	}

	protected String buildTransaction(final String type) {
		// TODO Auto-generated method stub
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	protected void checkTencentInstance() {
		// TODO Auto-generated method stub
		if (null == video_show.mTencent)
			video_show.mTencent = Tencent.createInstance("222222",
					this.getApplicationContext());
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {

	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:// 发送成功
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:// 发送取消
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:// 发送拒绝
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;// 发送返回
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		this.finish();
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
				Toast.makeText(WXEntryActivity.this, "onCancel: ",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			Toast.makeText(WXEntryActivity.this, "onComplete: ",
					Toast.LENGTH_SHORT).show();
			// 视频流行度的预测
			new Thread() {
				public void run() {
					try {
						URL url = new URL(
								"http://202.114.18.96:8080//Prediction/play");// 原来对应的路径为Prediction//prediction,存储的数据格式有变化
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setDoOutput(true);
						conn.setDoInput(true);
						conn.setRequestMethod("POST");
						OutputStream out = conn.getOutputStream();
						DataOutputStream obj = new DataOutputStream(out);
						String params = "videoid="
								+ URLEncoder.encode(videoid.trim(), "utf-8")
								+ "&mode=" + URLEncoder.encode("2", "utf-8");
						obj.writeBytes(params);
						obj.flush();
						obj.close();
						InputStreamReader in = new InputStreamReader(
								conn.getInputStream());
						BufferedReader buff = new BufferedReader(in);
						String line = "";
						while ((line = buff.readLine()) != null) {
							System.out.println("日期22222222:" + line);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(WXEntryActivity.this, "onError: " + e.errorMessage,
					Toast.LENGTH_SHORT).show();

		}
	};

	private void doShareToQzone(final Bundle params) {
		// QZone分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != video_show.mTencent) {
					video_show.mTencent.shareToQzone(WXEntryActivity.this,
							params, qqShareListener);
				}
			}
		});
	}

	private void doPublishToQzone(final Bundle params) {
		ThreadManager.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				if (null != video_show.mTencent) {
					video_show.mTencent.publishToQzone(WXEntryActivity.this,
							params, qqShareListener);
				}
			}
		});
	}

	private void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != video_show.mTencent) {
					video_show.mTencent.shareToQQ(WXEntryActivity.this, params,
							qqShareListener);
				}
			}
		});
	}

	/**
	 * 根据图片的url路径获得Bitmap对象
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap returnBitmap(String url) {
		URL fileUrl = null;
		Bitmap bitmap = null;
		try {
			fileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) fileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}
}