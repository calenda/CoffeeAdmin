package com.calenda.coffeeadmin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
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

import com.calenda.coffeeadmin.database.DBAdapter;

public class Comanda extends Activity {

	private DBAdapter dbHelper;
	private Cursor cursor;

	private String[] mProductos;
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
		case R.id.nuevoProducto:
			addRow();
			return true;

		case R.id.guardar:
			guardar();
			 Toast.makeText(Comanda.this, "Comanda guardada",
			 Toast.LENGTH_SHORT).show();
			return true;
		case R.id.limpiar:
//			 Toast.makeText(Comanda.this, "limpiar",
//			 Toast.LENGTH_SHORT).show();
			 limpiar();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	};

	private void limpiar(){
		TableLayout tl = (TableLayout) findViewById(R.id.spreadsheet);
		tl.removeAllViews();
		mSubtotales.clear();
		refreshTotal();
		
	}
	private void guardar() {
		TextView total = (TextView) findViewById(R.id.total);
		Float totald = Float.parseFloat(total.getText().toString());
		dbHelper.createComanda(totald);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comanda);

		// addActionBarItem(Type.Info, ACTION_BAR_INFO);
		dbHelper = new DBAdapter(this);
		dbHelper.open();
		getProductosArray();
		// addRow = (Button) findViewById(R.id.addRow);

		// addRow.setOnClickListener(new Button.OnClickListener() {
		// public void onClick(View v) {
		// addRow();
		// }
		// });
	}

	// @Override
	// public boolean onHandleActionBarItemClick(ActionBarItem item, int
	// position) {
	// switch (item.getItemId()) {
	// case ACTION_BAR_INFO:
	// startActivity(new Intent(this, InfoActivity.class));
	// break;
	// default:
	// return super.onHandleActionBarItemClick(item, position);
	// }
	// return true;
	// }

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
		// mCantidades.add(txtCant);
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

		cursor = dbHelper.fetchAllProductos();

		if (cursor.getCount() == 0) {
			dbHelper.createProducto("Latte", 18f, "Calientes");
			dbHelper.createProducto("Cap. sabor", 24f, "Calientes");
			dbHelper.createProducto("Americano", 12f, "Calientes");
			dbHelper.createProducto("Capuchino", 18f, "Calientes");
			dbHelper.createProducto("Moka", 28f, "Calientes");
			dbHelper.createProducto("Baguette 3Q", 28f, "Comida");
			dbHelper.createProducto("Baguette", 25f, "Comida");
			dbHelper.createProducto("Baguette C.", 30f, "Comida");
			dbHelper.createProducto("Cuernito 3Q", 24f, "Comida");
			dbHelper.createProducto("Frape Cap", 24f, "Frios");
			dbHelper.createProducto("Frape Moka", 28f, "Frios");
			dbHelper.createProducto("Coca Cola", 10f, "Frios");
			dbHelper.createProducto("Agua", 10f, "Frios");
			cursor = dbHelper.fetchAllProductos();
		}

		startManagingCursor(cursor);

		ArrayList<String> categorias = new ArrayList<String>();

//		for (cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) {
//			categorias.add(cursor.getString(1) + " $" + cursor.getString(2));
//		}
		
		if (cursor.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 categorias.add(cursor.getString(1) + " $" + cursor.getString(2));
		     } while(cursor.moveToNext());
		}
		mProductos = (String[]) categorias
				.toArray(new String[categorias.size()]);
		addRow();
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
		total.setText(Double.toString(total()));
	}

	public void refreshSubtotal(int child) {
		TableLayout t = (TableLayout) findViewById(R.id.spreadsheet);

		TableRow tr = (TableRow) t.getChildAt(child);
		TextView cant = (TextView) tr.getChildAt(0);
		Spinner prod = (Spinner) tr.getChildAt(1);
		TextView subT = (TextView) tr.getChildAt(2);

		Double cantD = Double.parseDouble(cant.getText().toString());
		cursor.moveToPosition(prod.getSelectedItemPosition() );
		Double precio = cursor.getDouble(2);

		subT.setText(Double.toString(cantD * precio));
		refreshTotal();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null)
			dbHelper.close();
	}

	public Double total() {
		Double total = 0.0;
		for (int i = 0; i < mSubtotales.size(); i++) {
			total = total
					+ Double.parseDouble(mSubtotales.get(i).getText()
							.toString());

		}
		return total;
	}
}
