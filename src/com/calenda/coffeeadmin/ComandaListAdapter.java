package com.calenda.coffeeadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.calenda.coffeeadmin.model.Comanda;

public class ComandaListAdapter extends ArrayAdapter<Comanda> {

	private ArrayList<Comanda> comandasList;

	public ComandaListAdapter(Context context, int textViewResourceId,
			ArrayList<Comanda> comandas) {
		super(context, textViewResourceId, comandas);
		this.comandasList = comandas;
	}

	@Override
	public int getCount() {
		return comandasList.size();
	}

	@Override
	public Comanda getItem(int position) {
		return comandasList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void notifyDataSetChanged(ArrayList<Comanda> comandas) {
		this.comandasList = comandas;
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.rowlayout, null);
		}

		Comanda c = comandasList.get(position);

		Date fecha = c.getFecha();
		String fechahora;
		if (fecha.toString().contains("00:00:00")) {
			SimpleDateFormat formatoDeFecha = new SimpleDateFormat("EEE dd");
			fechahora = formatoDeFecha.format(fecha);
		} else {
			SimpleDateFormat formatoDeFecha = new SimpleDateFormat("hh:mm:ss a");
			fechahora = formatoDeFecha.format(fecha);
		}

		if (c != null) {
			TextView fechaHora = (TextView) v.findViewById(R.id.row_fechahora);
			TextView subtotal = (TextView) v.findViewById(R.id.row_subtotal);
			if (fechaHora != null) {
				fechaHora.setText(fechahora);
			}
			if (subtotal != null) {
				subtotal.setText(c.getTotal().toString());
			}
		}
		return v;

	}

}
