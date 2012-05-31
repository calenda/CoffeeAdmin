package com.calenda.coffeeadmin.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.calenda.coffeeadmin.model.Comanda;
import com.calenda.coffeeadmin.model.Producto;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "coffeeadmin";
	private static final int DATABASE_VERSION = 7;

	private Dao<Producto, Integer> productoDao = null;
	private Dao<Comanda, Integer> comandaDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Comanda.class);
			TableUtils.createTable(connectionSource, Producto.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} 

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Producto.class, true);
			TableUtils.dropTable(connectionSource, Comanda.class, true);
			onCreate(db, connectionSource);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade",
					e);
			throw new RuntimeException(e);
		}

	}
	
	
//	@Override
//	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//		try {
//			
//			
//			// after we drop the old databases, we create the new ones
//			onCreate(db, connectionSource);
//		} catch (SQLException e) {
//			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//			throw new RuntimeException(e);
//		}	
//	}

	public Dao<Producto, Integer> getProductoDao() {
		if (null == productoDao) {
			try {
				productoDao = getDao(Producto.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return productoDao;
	}

	public Dao<Comanda, Integer> getComandaDao() {
		if (null == comandaDao) {
			try {
				comandaDao = getDao(Comanda.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return comandaDao;
	}
}
