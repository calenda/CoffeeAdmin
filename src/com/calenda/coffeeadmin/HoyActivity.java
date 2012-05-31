package com.calenda.coffeeadmin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.calenda.coffeeadmin.db.DatabaseManager;
import com.calenda.coffeeadmin.model.Comanda;

public class HoyActivity extends ListActivity {
	private ArrayList<Comanda> comandas;
	MySimpleListAdapter adapter;
	private int mYear;
	private int mMonth;
	private int mDay;
	Calendar fechaSelected;
	private TextView mDateDisplay;
	private Button mPickDate;
	final Calendar cHoy = Calendar.getInstance();
	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hoy);
		
		comandas = (ArrayList<Comanda>) DatabaseManager.getInstance().getAllComandasToday();
		adapter = new MySimpleListAdapter(this, R.layout.rowlayout, comandas);
		
    	setListAdapter(adapter);
		
		
		mDateDisplay = (TextView) findViewById(R.id.fecha);        
	    mPickDate = (Button) findViewById(R.id.fecha);

	    mPickDate.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	            showDialog(DATE_DIALOG_ID);
	        }
	    });

	    
	    mYear = cHoy.get(Calendar.YEAR);
	    mMonth = cHoy.get(Calendar.MONTH);
	    mDay = cHoy.get(Calendar.DAY_OF_MONTH);
	    cHoy.set(Calendar.HOUR_OF_DAY,	0);
	    cHoy.set(Calendar.MINUTE,0);
	    cHoy.set(Calendar.SECOND, 0);
	    cHoy.set(Calendar.MILLISECOND, 0);
	    
	    
	    fechaSelected = new GregorianCalendar(mYear, mMonth, mDay);
	    refreshTotalHoy();
	    updateDisplay();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this,
	                    mDateSetListener,
	                    mYear, mMonth, mDay);
	    }
	    return null;
	}
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    fechaSelected = new GregorianCalendar(mYear, mMonth, mDay);
                    updateDisplay();
                    onResume();
                }
            };
	private void updateDisplay() {
		this.mDateDisplay.setText(DateFormat.format("dd MMMM yyyy", fechaSelected.getTime()));
	}

	@Override
	protected void onResume() {
		
		refreshTotalHoy();
		adapter.notifyDataSetChanged(comandas);
		super.onResume();

	}

	public void refreshTotalHoy() {
		TextView totalHoy = (TextView) findViewById(R.id.totalHoy);
		totalHoy.setText(Double.toString(totalHoy()));
		

	}

	public Double totalHoy() {
		Double total = 0.0;
		if (fechaSelected.getTime().compareTo(cHoy.getTime())==0){ // if date selected on picker is the same that today
			comandas = (ArrayList<Comanda>) DatabaseManager.getInstance().getAllComandasToday();
			Toast.makeText(this, "IGUALES", Toast.LENGTH_SHORT).show();
		}
		else
		{
			comandas = (ArrayList<Comanda>) DatabaseManager.getInstance().getAllComandasByDay(fechaSelected);
			Toast.makeText(this, "DIFERENTES", Toast.LENGTH_SHORT).show();
		}
		

		
		for (Comanda com : comandas) {
			total = total + com.getTotal();
		}

		return total;
	}
}
