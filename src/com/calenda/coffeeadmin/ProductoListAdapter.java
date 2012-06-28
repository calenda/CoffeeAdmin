package com.calenda.coffeeadmin;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.calenda.coffeeadmin.model.Producto;

public class ProductoListAdapter extends ArrayAdapter<Producto> {

	private ArrayList<Producto> ProductosList;

	public ProductoListAdapter(Context context, int textViewResourceId,
			ArrayList<Producto> Productos) {
		super(context, textViewResourceId, Productos);
		this.ProductosList = Productos;
	}

	@Override
	public int getCount() {
		return ProductosList.size();
	}

	@Override
	public Producto getItem(int position) {
		return ProductosList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void notifyDataSetChanged(ArrayList<Producto> Productos) {
		this.ProductosList = Productos;
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.productorowlayout, null);
		}

		Producto c = ProductosList.get(position);
		if (c != null) {
			TextView producto = (TextView) v.findViewById(R.id.row_producto);
			TextView precio = (TextView) v.findViewById(R.id.row_precio);
			if (producto != null) {
				producto.setText(c.getProducto());
			}
			if (precio != null) {
				precio.setText(c.getPrecio().toString());
			}
		}
		return v;

	}

}
