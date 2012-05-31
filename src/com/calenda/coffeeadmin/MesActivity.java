package com.calenda.coffeeadmin;

import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.calenda.coffeeadmin.db.DatabaseManager;
import com.calenda.coffeeadmin.model.Comanda;

public class MesActivity extends ListActivity {
	private ArrayList<Comanda> comandas;
	MySimpleListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hoy);

		comandas = (ArrayList<Comanda>) DatabaseManager.getInstance()
				.getAllComandasThisMonth();
		adapter = new MySimpleListAdapter(this, R.layout.rowlayout, comandas);

		setListAdapter(adapter);

		refreshTotalMes();
		TextView fechaHoy = (TextView) findViewById(R.id.fecha);
		Date dateHoy = new Date();
		fechaHoy.setText(DateFormat.format("MMMM yyyy", dateHoy));
	}

	@Override
	protected void onResume() {
		refreshTotalMes();
		adapter.notifyDataSetChanged(comandas);
		super.onResume();

	}

	public void refreshTotalMes() {
		TextView totalHoy = (TextView) findViewById(R.id.totalHoy);
		totalHoy.setText(Double.toString(totalMes()));

	}

	public Double totalMes() {
		Double total = 0.0;
		comandas = (ArrayList<Comanda>) DatabaseManager.getInstance()
				.getAllComandasThisMonth();
		for (Comanda com : comandas) {
			total = total + com.getTotal();
		}

		return total;
	}
}
