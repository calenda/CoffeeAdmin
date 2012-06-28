package com.calenda.coffeeadmin;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.calenda.coffeeadmin.db.DatabaseManager;
import com.calenda.coffeeadmin.model.Producto;

public class CatProductosActivity extends ListActivity {
	private ArrayList<Producto> productos;
	ProductoListAdapter adapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_cat_productos, menu);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nuevoProducto:
			// ///////////////////////////////7
			nuevoProducto();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	};

	private static final int MENU_EDIT_PRODUCTO = Menu.FIRST;
	private static final int MENU_DELETE_PRODUCTO = Menu.FIRST + 1;

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		if (v.getId() == getListView().getId()) {
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			final Producto prod = this.productos.get(info.position);
			menu.setHeaderTitle(prod.getProducto());
			menu.add(Menu.NONE, MENU_EDIT_PRODUCTO, MENU_EDIT_PRODUCTO,
					"Editar");
			menu.add(Menu.NONE, MENU_DELETE_PRODUCTO, MENU_DELETE_PRODUCTO,
					"Borrar");
		}
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		final int menuItemIndex = item.getItemId();
		final Producto prod = productos.get(info.position);

		final LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_entry, null);
		final EditText txtProducto = (EditText) textEntryView
				.findViewById(R.id.productoNuevo);
		final EditText txtPrecio = (EditText) textEntryView
				.findViewById(R.id.precioNuevo);

		switch (menuItemIndex) {
		case MENU_EDIT_PRODUCTO:
			txtProducto.setText(prod.getProducto());
			txtPrecio.setText(prod.getPrecio().toString());

			new AlertDialog.Builder(this)
					.setTitle("Editar Producto")
					.setView(textEntryView)
					.setPositiveButton("Guardar",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									prod.setProducto(txtProducto.getText().toString());
									prod.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
									DatabaseManager.getInstance().updateProducto(prod);
									Toast.makeText(CatProductosActivity.this,
											"Producto editado correctamente",
											Toast.LENGTH_SHORT).show();
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									/* User clicked cancel so do some stuff */
								}
							}).show();
			break;
		case MENU_DELETE_PRODUCTO:
			this.productos.remove(info.position);
			DatabaseManager.getInstance().deleteProducto(prod);
			Toast.makeText(CatProductosActivity.this,
					"Producto eliminado correctamente",
					Toast.LENGTH_SHORT).show();
			this.adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		return true;
	}

	public void nuevoProducto() {
		final LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_entry, null);
		final EditText txtProducto = (EditText) textEntryView
				.findViewById(R.id.productoNuevo);
		final EditText txtPrecio = (EditText) textEntryView
				.findViewById(R.id.precioNuevo);

		new AlertDialog.Builder(this)
				.setTitle("Agregar producto")
				.setView(textEntryView)
				.setPositiveButton("Agregar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int whichButton) {

								final Producto prod = new Producto(txtProducto
										.getText().toString(), Float
										.parseFloat(txtPrecio.getText()
												.toString()));
								DatabaseManager.getInstance().addProducto(prod);
								Toast.makeText(CatProductosActivity.this,
										"Producto agregado correctamente",
										Toast.LENGTH_SHORT).show();
								onResume();
							}
						})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								/* User clicked cancel so do some stuff */
							}
						}).show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c_productos);
		findAndCreateAllViews();
		productos = (ArrayList<Producto>) DatabaseManager.getInstance()
				.getAllProductos();
		adapter = new ProductoListAdapter(this, R.layout.productorowlayout,
				productos);

		setListAdapter(adapter);

	}

	@Override
	protected void onResume() {
		refreshListProductos();
		adapter.notifyDataSetChanged(productos);
		super.onResume();

	}

	private void refreshListProductos() {
		productos = (ArrayList<Producto>) DatabaseManager.getInstance()
				.getAllProductos();
	}
	public void findAndCreateAllViews() {
		registerForContextMenu(getListView());
	}
}
