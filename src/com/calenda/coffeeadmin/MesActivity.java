package com.calenda.coffeeadmin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.calenda.coffeeadmin.dateslider.DateSlider;
import com.calenda.coffeeadmin.dateslider.MonthYearDateSlider;
import com.calenda.coffeeadmin.db.DatabaseManager;
import com.calenda.coffeeadmin.model.Comanda;

public class MesActivity extends ListActivity {
	private ArrayList<Comanda> comandas;
	ComandaListAdapter adapter;
//	private int mYear;
//	private int mMonth;
//	private int mDay;
	private Calendar fechaSelected;
	private TextView mDateDisplay;
	private Button mPickDate;
	final Calendar cHoy = Calendar.getInstance();

	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mes);

		comandas = (ArrayList<Comanda>) DatabaseManager.getInstance()
				.getAllComandasThisMonth();
		adapter = new ComandaListAdapter(this, R.layout.rowlayout, comandas);

		setListAdapter(adapter);

		mDateDisplay = (TextView) findViewById(R.id.mesDisplay);
		mPickDate = (Button) findViewById(R.id.mesDisplay);

		mPickDate.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// call the internal showDialog method using the predefined ID
				showDialog(DATE_DIALOG_ID);
			}
		});

		// mPickDate.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// showDialog(DATE_DIALOG_ID);
		// }
		// });

//		mYear = cHoy.get(Calendar.YEAR);
//		mMonth = cHoy.get(Calendar.MONTH);
//		mDay = cHoy.get(Calendar.DAY_OF_MONTH);
		cHoy.set(Calendar.HOUR_OF_DAY, 0);
		cHoy.set(Calendar.MINUTE, 0);
		cHoy.set(Calendar.SECOND, 0);
		cHoy.set(Calendar.MILLISECOND, 0);

		fechaSelected = cHoy;
//		fechaSelected = new GregorianCalendar(mYear, mMonth, mDay);
		refreshTotalMes();
		 TextView mes = (TextView) findViewById(R.id.mesDisplay);
		 Date dateHoy = new Date();
		 mes.setText(DateFormat.format("MMMM yyyy", dateHoy));
//		updateDisplay();

	}

	private MonthYearDateSlider.OnDateSetListener mDateSetListener = new MonthYearDateSlider.OnDateSetListener() {
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			// update the mDateDisplay view with the corresponding date
			fechaSelected = selectedDate; 
			mDateDisplay.setText(String.format("%tB %tY",
					selectedDate, selectedDate));
			onResume();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		// this method is called after invoking 'showDialog' for the first time
		// here we initiate the corresponding DateSlideSelector and return the
		// dialog to its caller

		// get todays date and the time
		final Calendar c = Calendar.getInstance();
		switch (id) {
		case DATE_DIALOG_ID:
			return new MonthYearDateSlider(this, mDateSetListener, c);
		}
		return null;
	}

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// switch (id) {
	// case DATE_DIALOG_ID:
	// return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
	// mDay);
	// }
	// return null;
	// }

//	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//
//		public void onDateSet(DatePicker view, int year, int monthOfYear,
//				int dayOfMonth) {
//			mYear = year;
//			mMonth = monthOfYear;
//			mDay = dayOfMonth;
//			fechaSelected = new GregorianCalendar(mYear, mMonth, mDay);
//			updateDisplay();
//			onResume();
//		}
//	};

//	private void updateDisplay() {
//		this.mDateDisplay.setText(DateFormat.format("MMMM yyyy",
//				fechaSelected.getTime()));
//	}

	@Override
	protected void onResume() {
		refreshTotalMes();
		adapter.notifyDataSetChanged(comandas);
		super.onResume();

	}

	public void refreshTotalMes() {
		TextView totalHoy = (TextView) findViewById(R.id.total);
		totalHoy.setText(Double.toString(totalMes()));

	}

	public Double totalMes() {
		Double total = 0.0;
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////
		System.out.println("fechaSelected.getTime(): "+fechaSelected.get(Calendar.YEAR)+" "+fechaSelected.get(Calendar.MONTH));
		System.out.println("cHoy.getTime(): "+cHoy.get(Calendar.YEAR)+" "+cHoy.get(Calendar.MONTH));
		if ((fechaSelected.get(Calendar.YEAR) == cHoy.get(Calendar.YEAR)) &&
				(fechaSelected.get(Calendar.MONTH) == cHoy.get(Calendar.MONTH))
				) {
			comandas = (ArrayList<Comanda>) DatabaseManager.getInstance()
					.getAllComandasByMonth(cHoy);			
//			 Toast.makeText(this, "IGUALES", Toast.LENGTH_SHORT).show();
		} else {
			comandas = (ArrayList<Comanda>) DatabaseManager.getInstance()
					.getAllComandasByMonth(fechaSelected);
//			 Toast.makeText(this, "DIFERENTES", Toast.LENGTH_SHORT).show();
		}
		
		for (Comanda com : comandas) {
			total = total + com.getTotal();
		}

		return total;
	}
}
