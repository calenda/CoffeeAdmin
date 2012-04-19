/**
 * 
 */
package com.calenda.coffeeadmin.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.calenda.coffeeadmin.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;



/**
 * @author Calenda Software
 *
 */

public class ORMDatabaseHelper extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE_NAME = "CoffeAdmin.orm";
	private static final int DATABASE_VERSION = 1;
	
	private Dao<Producto, Integer> simpleDao = null;
	private RuntimeExceptionDao<Producto, Integer> simpleRuntimeDao = null;

	public ORMDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(ORMDatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Producto.class);
		} catch (SQLException e) {
			Log.e(ORMDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

		// here we try inserting data in the on-create as a test
//		RuntimeExceptionDao<Producto, Integer> dao = getProductoDao();
//		long millis = System.currentTimeMillis();
//		// create some entries in the onCreate
//		Producto simple = new Producto(millis);
//		dao.create(simple);
//		simple = new Producto(millis + 1);
//		dao.create(simple);
//		Log.i(ORMDatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(ORMDatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Producto.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(ORMDatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our Producto class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Producto, Integer> getDao() throws SQLException {
		if (simpleDao == null) {
			simpleDao = getDao(Producto.class);
		}
		return simpleDao;
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Producto class. It will
	 * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<Producto, Integer> getProductoDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(Producto.class);
		}
		return simpleRuntimeDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		simpleRuntimeDao = null;
	}
}
