package com.calenda.coffeeadmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.calenda.coffeeadmin.db.DatabaseManager;
import com.calenda.coffeeadmin.model.Comanda;
import com.calenda.coffeeadmin.model.Producto;

public class ComandaActivity extends Activity {

	private String[] mProductos;
	private List<Producto> productos;
	List<EditText> mSubtotales = new ArrayList<EditText>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return true;

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.agregarProducto:
			addRow();
			return true;
		case R.id.guardar:
			guardar();
			Toast.makeText(ComandaActivity.this, "Comanda guardada",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.limpiar:
			limpiar();
			return true;
		case R.id.catProductos:
			Intent i = new Intent(this, CatProductosActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	};

	private void limpiar() {
		TableLayout tl = (TableLayout) findViewById(R.id.spreadsheet);
		tl.removeAllViews();
		mSubtotales.clear();
		refreshTotal();

	}

	private void guardar() {
		TextView total = (TextView) findViewById(R.id.total);
		Float totald = Float.parseFloat(total.getText().toString());
		Date fecha = new Date();
		Comanda c = new Comanda(fecha, totald);
		DatabaseManager.getInstance().addComanda(c);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseManager.init(this);
		setContentView(R.layout.comanda);
	}

	@Override
	protected void onStart() {
		super.onStart();
		getProductosArray();
	}

	public void addRow() {
		TableLayout tl = (TableLayout) findViewById(R.id.spreadsheet);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		EditText txtCant = new EditText(this);
		txtCant.setLayoutParams(lp);
		txtCant.setText("1");
		txtCant.setInputType(InputType.TYPE_CLASS_NUMBER);
		txtCant.setSelectAllOnFocus(true);
		txtCant.setId(tl.getChildCount());
		txtCant.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode >= 7 && keyCode <= 16) {
					EditText cant = (EditText) v;
					if (cant.getText().length() > 0)
						refreshSubtotal(v.getId());
				}
				return false;
			}
		});

		Spinner spinConcepto = new Spinner(this);
		fillSpinProductos(spinConcepto);
		spinConcepto.setLayoutParams(lp);

		EditText txtSubt = new EditText(this);
		txtSubt.setLayoutParams(lp);
		txtSubt.setFocusable(false);
		txtSubt.setFocusableInTouchMode(false);
		txtSubt.setClickable(false);
		mSubtotales.add(txtSubt);

		tr.addView(txtCant);
		tr.addView(spinConcepto);
		tr.addView(txtSubt);
		tr.setLayoutParams(lp);

		tl.addView(tr, lp);
		spinConcepto.setId(tl.getChildCount());
	}

	private void getProductosArray() {

		productos = DatabaseManager.getInstance().getAllProductos();

		if (productos.size() == 0) {

			DatabaseManager.getInstance().addProducto(
					new Producto("Latte", 10f));
			DatabaseManager.getInstance().addProducto(
					new Producto("Americano", 15f));
		}

		List<String> nombresProds = new ArrayList<String>();
		for (Producto prod : productos) {
			nombresProds.add(prod.toString());
		}

		mProductos = (String[]) nombresProds.toArray(new String[nombresProds
				.size()]);
//		addRow();
	}

	private void fillSpinProductos(Spinner s) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mProductos);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);

		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				refreshSubtotal(parent.getId() - 1);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void refreshTotal() {
		TextView total = (TextView) findViewById(R.id.total);
		total.setText(Float.toString(total()));
	}

	public void refreshSubtotal(int child) {
		TableLayout t = (TableLayout) findViewById(R.id.spreadsheet);

		TableRow tr = (TableRow) t.getChildAt(child);
		TextView cant = (TextView) tr.getChildAt(0);
		Spinner prod = (Spinner) tr.getChildAt(1);
		TextView subT = (TextView) tr.getChildAt(2);

		Float cantD = Float.parseFloat(cant.getText().toString());
		Float precio = productos.get(prod.getSelectedItemPosition())
				.getPrecio();
		subT.setText(Float.toString(cantD * precio));
		refreshTotal();
	}

	public Float total() {
		Float total = 0.0f;
		for (int i = 0; i < mSubtotales.size(); i++) {
			total = total
					+ Float.parseFloat(mSubtotales.get(i).getText().toString());

		}
		return total;
	}
}
