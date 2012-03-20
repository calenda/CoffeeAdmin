package com.calenda.coffeeadmin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "cafeadmin";
	private static final int DATABASE_VERSION = 1;

	// Consulta para crear la base de datos
	private static final String CREATE_TABLE_PRODUCTO = "create table producto("
			+ "_id integer primary key autoincrement, "
			+ "producto text not null, " 
			+ "precio real not null, " 
			+ "categoria text not null) ;" ;
	private static final String CREATE_TABLE_COMANDA = "create table comanda("
			+ "_id integer primary key autoincrement, "
			+ "fechahora INTEGER NOT NULL DEFAULT (strftime('%s','now')), "
			+ "total real not null) ;";

	// Este método se llama al momento en el que se crea la BD
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_PRODUCTO);
		database.execSQL(CREATE_TABLE_COMANDA);
	}

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// Método que se llama cada vez que se actualiza la BD
	// Sirve para manejar las versiones de la misma
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DataBaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS producto");
		database.execSQL("DROP TABLE IF EXISTS comanda");
		onCreate(database);
	}

}
