package com.calenda.coffeeadmin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.calenda.coffeeadmin.R;
import com.calenda.coffeeadmin.database.DBAdapter;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {
	private final Context mContext;
	private final int mLayout;
	private final Cursor mCursor;
	private final int mFechaHora;
	private final int mSubTotal;
	private final LayoutInflater mLayoutInflater;

	private final class ViewHolder {
		public TextView txtfechahora;
		public TextView subtotal;
	}

	public MySimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);

		this.mContext = context;
		this.mLayout = layout;
		this.mCursor = c;
		this.mFechaHora = mCursor
				.getColumnIndex(DBAdapter.KEY_COMANDA_FECHAHORA);
		this.mSubTotal = mCursor.getColumnIndex(DBAdapter.KEY_COMANDA_TOTAL);
		this.mLayoutInflater = LayoutInflater.from(mContext);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mCursor.moveToPosition(position)) {
			ViewHolder viewHolder;

			if (convertView == null) {
				convertView = mLayoutInflater.inflate(mLayout, null);

				viewHolder = new ViewHolder();
				viewHolder.txtfechahora = (TextView) convertView
						.findViewById(R.id.row_fechahora);
				viewHolder.subtotal = (TextView) convertView
						.findViewById(R.id.row_subtotal);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			String fechahora = mCursor.getString(mFechaHora);
			String subtotal = mCursor.getString(mSubTotal);

			SimpleDateFormat format;
			if (fechahora.contains(":")) // si es una hora, o una fecha
				format = new SimpleDateFormat("HH:mm:ss");
			else
				format = new SimpleDateFormat("yyyy MM dd");
			Date fecha = null;
			try {

				fecha = format.parse(fechahora);

			} catch (ParseException ex) {

				ex.printStackTrace();

			}
			if (fechahora.contains(":")) {// si es una hora, o una fecha
				SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
						"hh:mm:ss a");
				fechahora = formatoDeFecha.format(fecha);
			} else {

				SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
						"EEE dd");
				fechahora = formatoDeFecha.format(fecha);
			}
			viewHolder.txtfechahora.setText(fechahora);
			viewHolder.subtotal.setText(subtotal);
		}

		return convertView;

	}

}
