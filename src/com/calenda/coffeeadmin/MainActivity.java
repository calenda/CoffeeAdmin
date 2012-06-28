package com.calenda.coffeeadmin;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends TabActivity {

	private TabHost mTabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTabHost = getTabHost();
		setupTab(new TextView(this), "Comanda", ComandaActivity.class,
				R.drawable.comandatab);
		setupTab(new TextView(this), "Hoy", HoyActivity.class,
				R.drawable.hoytab);
		setupTab(new TextView(this), "Mes", MesActivity.class,
				R.drawable.hoytab);


	}

	private void setupTab(final View view, final String tag, Class<?> cls,
			int imgRes) {
		Intent intent;

		intent = new Intent().setClass(MainActivity.this, cls);

		View tabview = createTabView(mTabHost.getContext(), tag, imgRes);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text,
			int imgRes) {

		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);

		ImageView iv = (ImageView) view.findViewById(R.id.tabsIcon);
		iv.setImageResource(imgRes);

		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setTextSize(12f);
		tv.setText(text);
		return view;
	}

}