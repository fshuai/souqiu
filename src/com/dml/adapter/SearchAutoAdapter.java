package com.dml.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dml.search.SearchActivity;
import com.dml.souqiu.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAutoAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<SearchAutoData> mOriginalValues;// ���е�Item
	private List<SearchAutoData> mObjects;// ���˺��item
	private final Object mLock = new Object();
	private int mMaxMatch = 10; // �����ʾ���ٸ�ѡ��,������ʾȫ��
	private OnClickListener mOnClickListener;

	public SearchAutoAdapter(Context context, int maxMatch,
			OnClickListener onClickListener) {
		this.mContext = context;
		this.mMaxMatch = maxMatch;
		this.mOnClickListener = onClickListener;
		initSearchHistory();
		mObjects = mOriginalValues;
	}

	@Override
	public int getCount() {
		return null == mObjects ? 0 : mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return null == mObjects ? 0 : mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AutoHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.auto_seach_list_item, parent, false);
			holder = new AutoHolder();
			holder.content = (TextView) convertView
					.findViewById(R.id.auto_content);
			holder.addButton = (TextView) convertView
					.findViewById(R.id.auto_add);
			
			convertView.setTag(holder);
	// ����ʹ����LayoutInflater�������View�����Tag��ʹ��,���Ǹ�View�����һ����ǩ����ǩ�������κ����ݣ���������������ó���һ������,��
	//�����ǩ����AutoHolderʵ����������һ�����ԡ�����֮�����AutoHolderʵ�����Ķ���holder�Ĳ�����������Ϊjava�����û��ƶ�һֱ���ı�convertView�����ݣ�
	//������ÿ�ζ���ȥnewһ�������Ǿ������ﵽ������
		} else {
			holder = (AutoHolder) convertView.getTag();
		}

		SearchAutoData data = mObjects.get(position);
		holder.content.setText(data.getContent());
		
		//��setTag��������������
		holder.addButton.setTag(data);
		holder.addButton.setOnClickListener(mOnClickListener);
		return convertView;
	}

	/**
	 * ��ȡ��ʷ������¼
	 */
	public void initSearchHistory() {
		SharedPreferences sp = mContext.getSharedPreferences(
				SearchActivity.SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SearchActivity.SEARCH_HISTORY, "");
		String[] hisArrays = longhistory.split(",");
		mOriginalValues = new ArrayList<SearchAutoData>();
		if (hisArrays[0].equals("")) {    //hisArraysΪ�գ���ȻҲ����Ϊ1��ֻ������д��
			return;
		}
		for (int i = 0; i < hisArrays.length; i++) {
			mOriginalValues.add(new SearchAutoData().setContent(hisArrays[i]));
		}
	}

	/**
	 * ƥ�������������
	 * 
	 * @param prefix
	 *            ����������������
	 */
	public void performFiltering(CharSequence prefix) {
		if (prefix == null || prefix.length() == 0) {//����������Ϊ�յ�ʱ,��ʾ������ʷ��¼
			synchronized (mLock) {
				mObjects = mOriginalValues;
			}
		} else {
			String prefixString = prefix.toString().toLowerCase();
			int count = mOriginalValues.size();
			ArrayList<SearchAutoData> newValues = new ArrayList<SearchAutoData>(
					count);
			for (int i = 0; i < count; i++) {
				final String value = mOriginalValues.get(i).getContent();
				final String valueText = value.toLowerCase();
				if (valueText.contains(prefixString)) {

				}
				if (valueText.startsWith(prefixString)) {
					newValues.add(new SearchAutoData().setContent(valueText));
				} else {
					final String[] words = valueText.split(" ");
					final int wordCount = words.length;
					for (int k = 0; k < wordCount; k++) {
						if (words[k].startsWith(prefixString)) {
							newValues.add(new SearchAutoData()
									.setContent(value));
							break;
						}
					}
				}
				if (mMaxMatch > 0) {
					if (newValues.size() > mMaxMatch - 1) {
						break;
					}
				}
			}
			mObjects = newValues;
		}
		notifyDataSetChanged();
	}
 //��������ʷ��¼�ĸ�ʽ    ��content         +��
	private class AutoHolder {
		TextView content;
		TextView addButton;
	}
}
