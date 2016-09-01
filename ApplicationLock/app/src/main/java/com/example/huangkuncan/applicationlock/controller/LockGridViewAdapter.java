package com.example.huangkuncan.applicationlock.controller;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.model.LockAppInfo;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class LockGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<LockAppInfo> list;

	public LockGridViewAdapter(Context context, List<LockAppInfo> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.gridview_item_iv);
			holder.cb = (CheckBox) convertView.findViewById(R.id.gridview_item_cb);
			holder.tv = (TextView) convertView.findViewById(R.id.gridview_item_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).packageInfo.applicationInfo.loadLabel(LockStore.getInstance().getGetApplication()));
		holder.cb.setChecked(list.get(position).isChoosed);
		holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					LockStore.getInstance().lock(list.get(position).packageInfo.packageName);
				} else {
					LockStore.getInstance().unlock(list.get(position).packageInfo.packageName);
				}
			}
		});
		holder.iv.setImageDrawable(list.get(position).packageInfo.applicationInfo.loadIcon(LockStore.getInstance().getGetApplication()));
		return convertView;
	}

	public class ViewHolder {
		ImageView iv;
		CheckBox cb;
		TextView tv;
	}
}
