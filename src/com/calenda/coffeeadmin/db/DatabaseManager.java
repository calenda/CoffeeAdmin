package com.calenda.coffeeadmin.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;

import com.calenda.coffeeadmin.model.Comanda;
import com.calenda.coffeeadmin.model.Producto;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;

public class DatabaseManager {

	static private DatabaseManager instance;

	static public void init(Context ctx) {
		if (null == instance) {
			instance = new DatabaseManager(ctx);
		}
	}

	static public DatabaseManager getInstance() {
		return instance;
	}

	private DatabaseHelper helper;

	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}

	private DatabaseHelper getHelper() {
		return helper;
	}

	public List<Producto> getAllProductos() {
		List<Producto> productos = null;
		try {
			productos = getHelper().getProductoDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productos;
	}

	public List<Comanda> getAllComandas() {
		List<Comanda> comandas = null;
		try {
			comandas = getHelper().getComandaDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comandas;
	}

	public List<Comanda> getAllComandasToday() {
		List<Comanda> comandas = null;
		try {
			Dao<Comanda, Integer> comandaDao = getHelper().getComandaDao();

			Date hoy = new Date();
			Date hoyini = new Date(hoy.getYear(), hoy.getMonth(), hoy.getDate());
			Date hoyfin = new Date(hoy.getYear(), hoy.getMonth(),
					hoy.getDate() + 1);
			comandas = comandaDao.queryBuilder().where()
					.between("fecha", hoyini, hoyfin).query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comandas;
	}

	public List<Comanda> getAllComandasByDay(Calendar fechaSelected) {
		List<Comanda> comandas = null;
		try {
			Dao<Comanda, Integer> comandaDao = getHelper().getComandaDao();

			Date fecha = fechaSelected.getTime();
			Date fechaIni = new Date(fecha.getYear(), fecha.getMonth(),
					fecha.getDate());
			Date fechaFin = new Date(fecha.getYear(), fecha.getMonth(),
					fecha.getDate() + 1);
			comandas = comandaDao.queryBuilder().where()
					.between("fecha", fechaIni, fechaFin).query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comandas;
	}

	public List<Comanda> getAllComandasThisMonth() {
		List<Comanda> comandas = new ArrayList<Comanda>();
		try {

			Dao<Comanda, Integer> comandaDao = getHelper().getComandaDao();

			GenericRawResults<Object[]> comandasRaw = comandaDao
					.queryRaw(
							"SELECT fecha, sum(total) as total FROM comanda group by substr( fecha,0,11)",
							new DataType[] { DataType.DATE_STRING,
									DataType.FLOAT });

			for (Object[] resultArray : comandasRaw) {
//				System.out.println("Fecha: " + resultArray[0] + ", Total: "
//						+ resultArray[1]);
				Date fecha = new Date(resultArray[0].toString());
				fecha.setHours(0);
				fecha.setMinutes(0);
				fecha.setSeconds(0);
				comandas.add(new Comanda(fecha, new Float(resultArray[1]
						.toString())));
			}
			comandasRaw.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comandas;
	}

	public List<Comanda> getAllComandasByMonth(Calendar fechaSelected) {
		List<Comanda> comandas = new ArrayList<Comanda>();
		try {

			Dao<Comanda, Integer> comandaDao = getHelper().getComandaDao();

			Date fechatmp = fechaSelected.getTime();
			Date fechaParam = new Date(fechatmp.getYear(), fechatmp.getMonth(),
					fechatmp.getDate());
			String strFecha = (String) DateFormat.format("yyyy-MM-dd",
					fechaParam.getTime());
					
			GenericRawResults<Object[]> comandasRaw = comandaDao
					.queryRaw(
							"SELECT fecha, sum(total) as total FROM comanda"+ 
" where strftime(\"%Y-%m\",fecha) between strftime(\"%Y-%m\",?) and strftime(\"%Y-%m\",?)"+
" GROUP by substr( fecha,0,11) ",
							new DataType[] { DataType.DATE_STRING,
									DataType.FLOAT }, strFecha, strFecha);

			for (Object[] resultArray : comandasRaw) {
//				System.out.println("Fecha: " + resultArray[0] + ", Total: "
//						+ resultArray[1]);
				Date fecha = new Date(resultArray[0].toString());
				fecha.setHours(0);
				fecha.setMinutes(0);
				fecha.setSeconds(0);
				comandas.add(new Comanda(fecha, new Float(resultArray[1]
						.toString())));
			}
			comandasRaw.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comandas;
	}
	public void addProducto(Producto p) {
		try {
			getHelper().getProductoDao().create(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Producto getProductoWithId(int productoId) {
		Producto wishList = null;
		try {
			wishList = getHelper().getProductoDao().queryForId(productoId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishList;
	}

	public void deleteProducto(Producto p) {
		try {
			getHelper().getProductoDao().delete(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void refreshProducto(Producto p) {
		try {
			getHelper().getProductoDao().refresh(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateProducto(Producto p) {
		try {
			getHelper().getProductoDao().update(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addComanda(Comanda c) {
		try {
			getHelper().getComandaDao().create(c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}