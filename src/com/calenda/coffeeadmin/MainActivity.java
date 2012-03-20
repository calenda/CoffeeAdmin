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

import com.calenda.coffeeadmin.R;


public class MainActivity extends TabActivity {

	private TabHost mTabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTabHost = getTabHost();
		// mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		setupTab(new TextView(this), "Comanda", Comanda.class,
				R.drawable.comandatab);
		setupTab(new TextView(this), "Hoy", Hoy.class, R.drawable.hoytab);
		setupTab(new TextView(this), "Mes", Mes.class, R.drawable.hoytab);
		//
		// TabHost.TabSpec spec;
		// // mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		//
		// Intent intent;
		// Resources res = getResources();
		//
		// intent = new Intent().setClass(this, Comanda.class);
		// spec = mTabHost.newTabSpec("Comanda").setIndicator("Comanda" ,
		// res.getDrawable(R.drawable.comandatab))
		// .setContent(intent);
		// mTabHost.addTab(spec);
		//
		// intent = new Intent().setClass(this, Hoy.class);
		// spec = mTabHost.newTabSpec("Hoy").setIndicator("Hoy")// ,
		// // res.getDrawable(R.drawable.hoytab))
		// .setContent(intent);
		// mTabHost.addTab(spec);

	}

	private void setupTab(final View view, final String tag, Class<?> cls,
			int imgRes) {
		Intent intent;
		// Resources res = getResources();

		intent = new Intent().setClass(MainActivity.this, cls);

		View tabview = createTabView(mTabHost.getContext(), tag, imgRes);
		// TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
		// .setContent(new TabContentFactory() {
		// public View createTabContent(String tag) {
		// return view;
		// }
		// });
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