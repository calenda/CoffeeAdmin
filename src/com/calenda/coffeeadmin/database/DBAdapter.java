package com.calenda.coffeeadmin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

	// Campos de la BD
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PRODUCTO_PRODUCTO = "producto";
	public static final String KEY_PRODUCTO_CATEGORIA = "categoria";
	public static final String KEY_COMANDA_TOTAL = "total";
	public static final String KEY_COMANDA_FECHAHORA = "fechahora";
	public static final String DATABASE_PRODUCTO_TABLE = "producto";
	public static final String DATABASE_COMANDA_TABLE = "comanda";
	
	public static final String KEY_PRODUCTO_PRECIO = "precio";
//	public static final String DATABASE_TABLE = "todo";
	private Context context;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;

	public DBAdapter(Context context) {
		this.context = context;
	}

	public DBAdapter open() throws SQLException {
		dbHelper = new DataBaseHelper(context);
		database = dbHelper.getReadableDatabase();
		return this;
	}

	public void close() {
		database.close();
	}

	// Crea una nueva tarea
//	public long createTodo(String category, String summary, String description) {
//		ContentValues initialValues = createContentValues(category, summary,
//				description);
//		return database.insert(DATABASE_TABLE, null, initialValues);
//	}
	
	public long createProducto(String producto, float precio, String categoria){
		ContentValues initialValues = crearValoresProducto(producto, precio, categoria);
		return database.insert(DATABASE_PRODUCTO_TABLE, null, initialValues);
	}

	// Actualiza la tarea
//	public boolean updateTodo(long rowId, String category, String summary,
//			String description) {
//		ContentValues updateValues = createContentValues(category, summary,
//				description);
//		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
//				+ rowId, null) > 0;
//	}
	
	public boolean updateProducto(long rowId, String producto, float precio, String categoria) {
		ContentValues updateValues = crearValoresProducto(producto, precio, categoria);
		return database.update(DATABASE_PRODUCTO_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	// Borra la tarea
//	public boolean deleteTodo(long rowId) {
//		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//	}
	public boolean deleteProducto(long rowId) {
		return database.delete(DATABASE_PRODUCTO_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	// Returna un Cursor con todas las tareas
//	public Cursor fetchAllTodos() {
//		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
//				KEY_CATEGORY, KEY_SUMMARY, KEY_DESCRIPTION }, null, null, null,
//				null, null);
//	}
	public Cursor fetchAllProductos() {
		return database.query(DATABASE_PRODUCTO_TABLE, new String[] { KEY_ROWID,
				KEY_PRODUCTO_PRODUCTO, KEY_PRODUCTO_PRECIO, KEY_PRODUCTO_CATEGORIA}, null, null, null,
				null, null);
	}
	// Retorna un Cursor de una tarea en específico
//	public Cursor fetchTodo(long rowId) throws SQLException {
//		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
//				KEY_ROWID, KEY_CATEGORY, KEY_SUMMARY, KEY_DESCRIPTION },
//				KEY_ROWID + "=" + rowId, null, null, null, null, null);
//		if (mCursor!=null){
//			mCursor.moveToFirst();
//		}
//		return mCursor;
//	}
	public Cursor fetchProducto(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_PRODUCTO_TABLE, new String[] {
				KEY_ROWID, KEY_PRODUCTO_PRODUCTO, KEY_PRODUCTO_PRECIO, KEY_PRODUCTO_CATEGORIA},
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor!=null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
//	private ContentValues createContentValues(String category, String summary, String description){
//		ContentValues values = new ContentValues();
//		values.put(KEY_CATEGORY, category);
//		values.put(KEY_SUMMARY, summary);
//		values.put(KEY_DESCRIPTION, description);
//		return values;
//	}
	
	private ContentValues crearValoresProducto(String producto, float precio, String categoria){
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCTO_PRODUCTO, producto);
		values.put(KEY_PRODUCTO_PRECIO, precio);
		values.put(KEY_PRODUCTO_CATEGORIA, categoria);
		return values;
	}

	private ContentValues crearValoresComanda(float total){
		ContentValues values = new ContentValues();
//		Date current = new Date();
		values.put(KEY_COMANDA_TOTAL, total);
//		values.put(KEY_COMANDA_FECHAHORA, current.toString());
		return values;
	}

	public long createComanda(float total){
		ContentValues initialValues = crearValoresComanda(total);
		return database.insert(DATABASE_COMANDA_TABLE, null, initialValues);
	}
	
	public Cursor fetchAllComandas() {
		return database.query(DATABASE_COMANDA_TABLE, new String[] { KEY_ROWID,
				KEY_COMANDA_FECHAHORA, KEY_COMANDA_TOTAL}, null, null, null,
				null, null);
	}
	
	public Cursor fetchAllComandasDateFixed() {
		return database.rawQuery("SELECT "+KEY_ROWID+" , strftime(\"%H:%M:%S\", "+KEY_COMANDA_FECHAHORA+", 'unixepoch','localtime') AS "+KEY_COMANDA_FECHAHORA+", "+KEY_COMANDA_TOTAL+" FROM "+DATABASE_COMANDA_TABLE, null);
//		return database.rawQuery("SELECT "+KEY_ROWID+" , strftime(\"%s\", "+KEY_COMANDA_FECHAHORA+") AS "+KEY_COMANDA_FECHAHORA+", "+KEY_COMANDA_TOTAL+" FROM "+DATABASE_COMANDA_TABLE, null);
	}

	public Cursor fetchAllComandasDiaDateFixed() {
		return database.rawQuery("SELECT "+KEY_ROWID+" , strftime(\"%H:%M:%S\", "+KEY_COMANDA_FECHAHORA+", 'unixepoch','localtime') AS "+KEY_COMANDA_FECHAHORA+", "+KEY_COMANDA_TOTAL+" FROM "+DATABASE_COMANDA_TABLE+" WHERE strftime('%m-%d',"+KEY_COMANDA_FECHAHORA+",'unixepoch','localtime') = strftime('%m-%d','now','localtime')",null);
//		return database.rawQuery("SELECT "+KEY_ROWID+" , strftime(\"%s\", "+KEY_COMANDA_FECHAHORA+") AS "+KEY_COMANDA_FECHAHORA+", "+KEY_COMANDA_TOTAL+" FROM "+DATABASE_COMANDA_TABLE, null);
	}

	public Cursor fetchAllComandasMesFixed() {
		
		String query = "SELECT _id, " +
				"strftime('%Y %m %d', "+KEY_COMANDA_FECHAHORA+", 'unixepoch','localtime') as "+KEY_COMANDA_FECHAHORA+", " +
				"sum("+KEY_COMANDA_TOTAL+") as "+KEY_COMANDA_TOTAL+" FROM "+DATABASE_COMANDA_TABLE+" " +
				"where strftime('%Y-%m', 'now') = strftime('%Y-%m',"+KEY_COMANDA_FECHAHORA+",'unixepoch','localtime') " +
				"group by strftime('%Y %m %d', "+KEY_COMANDA_FECHAHORA+", 'unixepoch','localtime')";
		
		return database.rawQuery(query,null);
//		return database.rawQuery("SELECT "+KEY_ROWID+" , strftime(\"%s\", "+KEY_COMANDA_FECHAHORA+") AS "+KEY_COMANDA_FECHAHORA+", "+KEY_COMANDA_TOTAL+" FROM "+DATABASE_COMANDA_TABLE, null);
	}

//	Cursor row = databaseHelper.query(true, TABLE_NAME, new String[] {
//			COLUMN_INDEX}, ID_COLUMN_INDEX + "=" + rowId,
//			null, null, null, null, null);
//			String dateTime = row.getString(row.getColumnIndexOrThrow(COLUMN_INDEX));
 
}
