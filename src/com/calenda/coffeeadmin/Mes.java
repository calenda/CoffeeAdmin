package com.calenda.coffeeadmin;

import java.util.Date;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.calenda.coffeeadmin.R;
import com.calenda.coffeeadmin.database.DBAdapter;

public class Mes extends ListActivity {
	private DBAdapter dbHelper;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hoy);

		dbHelper = new DBAdapter(this);
		dbHelper.open();
		cursor = dbHelper.fetchAllComandasMesFixed();
		startManagingCursor(cursor);
//		ListAdapter adapter = new SimpleCursorAdapter(this,
//				R.layout.rowlayout, cursor, new String[] {
//						DBAdapter.KEY_COMANDA_FECHAHORA,
//						DBAdapter.KEY_COMANDA_TOTAL }, new int[] {
//						R.id.row_fechahora,R.id.row_subtotal });
		ListAdapter adapter = new MySimpleCursorAdapter(this,
				R.layout.rowlayout, cursor ,new String[] {DBAdapter.KEY_COMANDA_FECHAHORA,
				DBAdapter.KEY_COMANDA_TOTAL }, new int[] {
				R.id.row_fechahora,R.id.row_subtotal });
		setListAdapter(adapter);
		cursor.requery();
		refreshTotalHoy();
		TextView fechaHoy = (TextView) findViewById(R.id.fecha);
		Date dateHoy = new Date();
		fechaHoy.setText(DateFormat.format("MMMM yyyy", dateHoy));
	}

	@Override
	protected void onResume() {
		cursor.requery();
		refreshTotalHoy();
		super.onResume();

	}

	public void refreshTotalHoy() {
		TextView totalHoy = (TextView) findViewById(R.id.totalHoy);
		totalHoy.setText(Double.toString(totalHoy()));


	}

	public Double totalHoy() {
		Double total = 0.0;

		// startManagingCursor(cursor);
		// cursor.moveToFirst();
		// Toast.makeText(this, "cursor.getCount(): "+cursor.getCount(),
		// Toast.LENGTH_LONG).show();
		// for (cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast())
		// {
		// Double t = cursor.getDouble(1);
		// total = total + t;
		// }

		if (cursor.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				Double t = cursor.getDouble(2);
				total = total + t;
			} while (cursor.moveToNext());
		}

		return total;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null)
			dbHelper.close();
	}
}
